package com.hy.common.base.entity;

import lombok.Data;

import java.util.List;

@Data
public class QueryParam {

	protected int id;
	/**
	 * 页号
	 */
	protected int page;
	public void setPage(int page) {
		if (page > 0) {
			this.page = page - 1;
		}
	}
	/**
	 * 获取记录条数
	 */
	protected int limit;
	/**
	 * 是否随机排序
	 */
	protected boolean randomOrder;
	/**
	 * 排序规则
	 */
	protected List<OrderParam> orderParams;

}
