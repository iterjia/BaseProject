package com.hy.common.base.service;

import com.hy.common.base.entity.QueryParam;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {
    List<T> selectList(QueryParam param);
    T selectById(Serializable id);
    int selectCount(QueryParam param);
    int insert(T entity);
    int updateById(T entity);
    int delete(String ids);
    int logicDelete(String ids);
}
