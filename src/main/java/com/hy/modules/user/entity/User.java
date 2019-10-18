package com.hy.modules.user.entity;

import com.hy.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
	private int id;
	private String name;
}
