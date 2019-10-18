package com.hy.common.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageData<T> {

	/**
	 * 数据总数
	 */
	private int total;
	/**
	 * 单页数据列表
	 */
	private List<T> list;

}
