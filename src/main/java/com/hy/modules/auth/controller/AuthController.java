package com.hy.modules.auth.controller;

import com.hy.common.constant.MsgConst;
import com.hy.common.tool.Kv;
import com.hy.common.tool.R;
import com.hy.common.tool.Utils;
import com.hy.core.config.RedisConfig;
import com.hy.core.secure.entity.TokenInfo;
import com.hy.core.secure.entity.TokenStub;
import com.hy.core.secure.utils.TokenUtil;
import com.hy.modules.auth.entity.AuthInfo;
import com.hy.modules.user.entity.User;
import com.hy.modules.user.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 登录，获取token
     */
    @PostMapping("/login")
    public R<AuthInfo> login(String loginName, String password, HttpServletRequest request) {
        User user = userService.selectByLoginName(loginName);
        if (user != null && Utils.verifyPassword(password, user.getPassword())) {
            Map<String, String> roles = userService.selectRoles(user.getId());
            user.setRoleIds(roles.get("roleIds"));
            user.setRoleNames(roles.get("roleNames"));

            TokenStub tokenStub = new TokenStub();
            tokenStub.setUserId(user.getId());
            tokenStub.setAccount(user.getLoginName());
            tokenStub.setName(user.getName());
            TokenInfo refreshToken = TokenUtil.createJWT(tokenStub.toMap(), TokenUtil.REFRESH_TOKEN);
            AuthInfo authInfo = createAuthInfo(tokenStub);
            authInfo.setRefreshToken(refreshToken.getToken());

            Map<String, String> rolePerms = userService.selectRolePerms();
            redisTemplate.opsForHash().putAll(RedisConfig.CACHE_NAME_PERMS, rolePerms);
            request.setAttribute(RedisConfig.CACHE_NAME_PERMS, rolePerms);
            Object value = redisTemplate.opsForHash().get(RedisConfig.CACHE_NAME_PERMS, "all:/system/");
            log.info(value.toString());
            return R.data(authInfo);
        } else {
            return R.fail(MsgConst.WRONG_PASSWORD);
        }
    }

    /**
     * 刷新token
     */
    @GetMapping("/token")
    public R<AuthInfo> refreshToken(String refreshToken) {
        Integer loginUserId = Utils.getLoginUserId();
        Claims claims = TokenUtil.parseJWT(refreshToken);
        if (loginUserId == null || TokenUtil.isExpired(claims)) {
            return R.fail(MsgConst.LOGIN_AGAIN);
        } else {
            User user = userService.selectById(loginUserId);
            TokenStub tokenStub = TokenUtil.extractTokenStub(claims);
            tokenStub.setRoleIds(user.getRoleIds());
            tokenStub.setRoleNames(user.getRoleNames());
            AuthInfo authInfo = createAuthInfo(tokenStub);
            return R.data(authInfo);
        }
    }

    /**
     * 返回授权信息，包括前端需要的当前用户信息
     *
     * @param tokenStub
     * @return
     */
    private AuthInfo createAuthInfo(TokenStub tokenStub) {
        TokenInfo accessToken = TokenUtil.createJWT(tokenStub.toMap(), TokenUtil.ACCESS_TOKEN);
        AuthInfo authInfo = new AuthInfo();
        authInfo.setAccessToken(accessToken.getToken());
        authInfo.setExpiresIn(accessToken.getExpire());
        authInfo.setTokenStub(tokenStub);
        return authInfo;
    }
}
