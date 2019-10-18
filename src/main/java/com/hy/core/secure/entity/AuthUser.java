package com.hy.core.secure.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private Integer id;
	/**
	 * 昵称
	 */
	private String name;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 角色id
	 */
	private String roleId;
	/**
	 * 角色名
	 */
	private String roleName;

}
