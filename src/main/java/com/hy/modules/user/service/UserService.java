package com.hy.modules.user.service;

import com.hy.common.base.service.BaseService;
import com.hy.modules.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends BaseService<User> {
    User selectByLoginName(String loginName);
    Map<String, String> selectRolePerms();
    Map<String, String> selectRoles(int userId);
}
