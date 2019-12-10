package com.hy.core.secure.entity;

import com.hy.common.tool.Kv;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 附在token中的扩展信息，前端需要
 */
@Data
public class TokenStub {
	public final static String USER_ID = "userId";
	public final static String ACCOUNT = "account";
	public final static String NAME = "name";
	public final static String ROLE_BITS = "roleBits";
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
	 * 姓名
	 */
	private String name;
	/**
	 * 权限标志位，简单预置权限
	 */
	private int roleBits;
	/**
	 * 角色id
	 */
	private String roleIds;
	/**
	 * 角色名
	 */
	private String roleNames;

	/**
	 * 转成Map，方便直接使用添加JWT Token附加信息
	 * @return
	 */
	public Map<String, Object> toMap() {
		Kv kv = Kv.init();
		kv.set(TokenStub.USER_ID, userId)
				.set(TokenStub.ACCOUNT, account)
				.set(TokenStub.NAME, name)
				.set(TokenStub.ROLE_BITS, roleBits)
				.set(TokenStub.ROLE_IDS, roleIds)
				.set(TokenStub.ROLE_NAMES, roleNames);
		return kv;
	}

}
