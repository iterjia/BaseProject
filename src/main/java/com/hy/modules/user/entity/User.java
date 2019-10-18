package com.hy.modules.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hy.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    @JsonIgnore
    private String password;
    private String loginName;
    private String roleIds;
    private String roleNames;
}
