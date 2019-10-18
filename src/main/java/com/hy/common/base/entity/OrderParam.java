package com.hy.common.base.entity;

import lombok.Data;

@Data
public class OrderParam {

	/**
	 * 排序列
	 */
	protected String col;
	/**
	 * 排序方向：desc/asc
	 */
	protected String direction;

}
