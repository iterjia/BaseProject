package com.hy.common.base.mapper;

import com.hy.common.base.entity.QueryParam;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T> {
    List<T> selectList(QueryParam param);
	T selectById(Serializable id);
	int insert(T entity);
    int updateById(T entity);
	int deleteById(Serializable id);
    int deleteByIds(String ids);
}
