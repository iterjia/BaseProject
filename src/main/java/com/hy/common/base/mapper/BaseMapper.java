package com.hy.common.base.mapper;

import com.hy.common.base.entity.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T> {
    List<T> selectList(QueryParam param);
	T selectById(Serializable id);
	int selectCount(QueryParam param);
	int insert(T entity);
    int updateById(T entity);
    int delete(@Param("ids") String ids);
    int logicDelete(@Param("ids") String ids);
}
