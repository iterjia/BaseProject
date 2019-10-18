package com.hy.modules.auth.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用于请求API
	 */
	private String accessToken;
	/**
	 * 用于刷新token
	 */
	private String refreshToken;
	/**
	 * 用户Id
	 */
	private int userId;
	/**
	 * 登录用户名
	 */
	private String loginName;
	/**
	 * 过期时间
	 */
	private long expiresIn;

}
