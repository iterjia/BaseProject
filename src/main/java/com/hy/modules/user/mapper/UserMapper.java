package com.hy.modules.user.mapper;

import com.hy.common.base.mapper.BaseMapper;
import com.hy.modules.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    User selectByLoginName(String loginName);
    List<Map<String, String>> selectRolePerms();
    Map<String, String> selectRoles(int userId);
}
