package com.hy.modules.user.service.impl;

import com.hy.common.base.service.impl.BaseServiceImpl;
import com.hy.common.tool.Utils;
import com.hy.core.config.RedisConfiguration;
import com.hy.modules.user.entity.User;
import com.hy.modules.user.mapper.UserMapper;
import com.hy.modules.user.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User selectByLoginName(String loginName) {
        return baseMapper.selectByLoginName(loginName);
    }

    @Override
    @Cacheable(value = RedisConfiguration.CACHE_NAME_PERMS)
    public Map<String, String> selectRolePerms() {
        Map<String, String> rolePerms = new HashMap<>();
        List<Map<String, String>> rolePermList = baseMapper.selectRolePerms();
        if (Utils.isNotEmpty(rolePermList)) {
            for (Map<String, String> rolePerm : rolePermList) {
                rolePerms.put(rolePerm.get("opUrl"), rolePerm.get("roles"));
            }
        }
        return rolePerms;
    }

    @Override
    public Map<String, String> selectRoles(int userId) {
        return baseMapper.selectRoles(userId);
    }
}
