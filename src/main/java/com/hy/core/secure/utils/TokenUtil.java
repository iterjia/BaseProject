package com.hy.core.secure.utils;

import com.hy.core.secure.entity.TokenStub;
import com.hy.core.secure.entity.TokenInfo;
import com.hy.common.tool.Utils;
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
import java.util.UUID;

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
    private final static String AUTH_TYPE_BEARER = "Bearer";
    private final static String REQUEST_ATTR_TOKEN_CLAIMS = "_REQUEST_ATTR_TOKEN_CLAIMS_";
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
                .setId(UUID.randomUUID().toString())
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

    public static boolean isExpired(Claims claims) {
        Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(claims.getExpiration());
        return Calendar.getInstance().after(checkDate);
    }

    public static Claims getClaims(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Object claims = request.getAttribute(REQUEST_ATTR_TOKEN_CLAIMS);
        if (claims != null) {
            return (Claims)claims;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null) {
            return null;
        }

        String[] authPart = auth.split(" ");
        if (authPart == null || authPart.length < 2 || !AUTH_TYPE_BEARER.equals(authPart[0])) {
            return null;
        }

        Claims parseResult = parseJWT(authPart[1]);
        request.setAttribute(REQUEST_ATTR_TOKEN_CLAIMS, parseResult);
        return parseResult;
    }

    public static TokenStub extractTokenStub() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return new TokenStub();
        }

        Claims claims = getClaims(request);
        return extractTokenStub(claims);
    }

    public static TokenStub extractTokenStub(Claims claims) {
        TokenStub tokenStub = new TokenStub();
        if (claims == null) {
            return tokenStub;
        }

        tokenStub.setUserId(Utils.toInt(claims.get(TokenStub.USER_ID)));
        tokenStub.setAccount(Utils.toStr(claims.get(TokenStub.ACCOUNT)));
        tokenStub.setName(Utils.toStr(claims.get(TokenStub.NAME)));
        tokenStub.setRoleBits(Utils.toInt(claims.get(TokenStub.ROLE_BITS)));
        tokenStub.setRoleIds(Utils.toStr(claims.get(TokenStub.ROLE_IDS)));
        tokenStub.setRoleNames(Utils.toStr(claims.get(TokenStub.ROLE_NAMES)));
        return tokenStub;
    }
}
