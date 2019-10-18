package com.hy.core.secure.utils;

import com.hy.common.tool.Utils;
import com.hy.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class RoleMatcher {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String roles;

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean matches(HttpServletRequest request) {
        String[] roleArr = roles.split(",");
        String httpMethod = request.getMethod();
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        Map<String, String> entries = userService.selectRolePerms();//redisTemplate.opsForHash().entries(RedisConfiguration.CACHE_NAME_PERMS);
        String matchUrl = httpMethod.toLowerCase() + ":/" + pathInfo + "/";
        String requireRoles = "," + entries.get(matchUrl) + ",";
        if (Utils.isNotEmpty(requireRoles)) {//"all:/system/"
            for (String role : roleArr) {
                if (requireRoles.contains(role)) {
                    return true;
                }
            }
        }

        matchUrl = "all:/" + pathInfo + "/";
        requireRoles = "," + entries.get(matchUrl) + ",";
        if (Utils.isEmpty(requireRoles)) {
            for (String role : roleArr) {
                if (requireRoles.contains(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
