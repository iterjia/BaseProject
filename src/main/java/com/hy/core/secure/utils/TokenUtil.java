package com.hy.core.secure.utils;

import com.hy.core.secure.entity.AuthUser;
import com.hy.core.secure.entity.TokenInfo;
import com.hy.common.tool.Func;
import com.hy.common.tool.WebUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.Charsets;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT标准中注册的声明 (建议但不强制使用) ：
 * iss: jwt签发者
 * sub: jwt所面向的用户
 * aud: 接收jwt的一方
 * exp: jwt的过期时间，这个过期时间必须要大于签发时间
 * nbf: 定义在什么时间之前，该jwt都是不可用的.
 * iat: jwt的签发时间
 * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放
 */
public class TokenUtil {
    public final static String ACCESS_TOKEN = "access_token";
    public final static String REFRESH_TOKEN = "refresh_token";

    private final static String TOKEN_SECRET_STR = "hello_hy";
    private final static int ACCESS_TOKEN_VALIDITY = 3600;
    private final static int REFRESH_TOKEN_VALIDITY = 604800;
    private final static byte[] TOKEN_SECRET = TOKEN_SECRET_STR.getBytes(Charsets.UTF_8);

    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("name", "yyy");
        TokenInfo info = createJWT(claims, ACCESS_TOKEN);
        Claims parseClaims = parseJWT(info.getToken());
        System.out.println(parseClaims);
    }

    public static TokenInfo createJWT(Map<String, Object> claims, String tokenType) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        Key signingKey = new SecretKeySpec(TOKEN_SECRET, signatureAlgorithm.getJcaName());

        //构建JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
                .setIssuer("issuser")
                .setAudience("audience")
                .signWith(signatureAlgorithm, signingKey);
        if (claims != null) {
            builder.addClaims(claims);
        }

        //添加Token过期时间
        long expireMillis;
        if (tokenType.equals(ACCESS_TOKEN)) {
            expireMillis = ACCESS_TOKEN_VALIDITY * 1000;
        } else if (tokenType.equals(REFRESH_TOKEN)) {
            expireMillis = REFRESH_TOKEN_VALIDITY * 1000;
        } else {
            expireMillis = getExpire();
        }
        long expMillis = nowMillis + expireMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        // 组装Token信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire((int) expireMillis / 1000);

        return tokenInfo;
    }

    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    private static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    public static Claims getClaims(HttpServletRequest request) {
        String auth = request.getHeader("self-auth");
        if ((auth != null) && (auth.length() > 9)) {
            return parseJWT(auth);
        }
        return null;
    }

    public static AuthUser extractUser() {
        HttpServletRequest request = WebUtil.getRequest();
        return extractUser(request);
    }

    public static AuthUser extractUser(HttpServletRequest request) {
        Object user = request.getAttribute("_USER_REQUEST_ATTR_");
        if (user == null) {
            Claims claims = getClaims(request);
            if (claims == null) {
                return null;
            }

            user = new AuthUser();
            ((AuthUser) user).setId(Func.toInt(claims.get("id")));
            ((AuthUser) user).setName(Func.toStr(claims.get("name")));
            request.setAttribute("_USER_REQUEST_ATTR_", user);
        }
        return (AuthUser) user;
    }
}
