package com.hy.core.secure.entity;

import lombok.Data;

@Data
public class TokenInfo {

	/**
	 * 令牌值
	 */
	private String token;

	/**
	 * 过期秒数
	 */
	private int expire;

}
