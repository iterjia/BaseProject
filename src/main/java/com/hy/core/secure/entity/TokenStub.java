package com.hy.core.secure.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 附在token中的扩展信息，前端需要
 */
@Data
public class TokenStub {
	public final static String USER_ID = "userId";
	public final static String ACCOUNT = "account";
	public final static String ROLE_IDS = "roleIds";
	public final static String ROLE_NAMES = "roleNames";

	/**
	 * 用户id
	 */
	private int userId;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 角色id
	 */
	private String roleIds;
	/**
	 * 角色名
	 */
	private String roleNames;

}
