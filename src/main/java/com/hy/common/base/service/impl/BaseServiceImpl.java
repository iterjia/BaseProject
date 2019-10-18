package com.hy.common.base.service.impl;

import com.hy.common.base.entity.BaseEntity;
import com.hy.common.base.entity.QueryParam;
import com.hy.common.base.mapper.BaseMapper;
import com.hy.common.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> implements BaseService<T> {
    @Autowired
    protected M baseMapper;

    @Override
    public List<T> selectList(QueryParam param) {
        return baseMapper.selectList(param);
    }

    @Override
    public T selectById(Serializable id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int insert(T entity) {
        return baseMapper.insert(entity);
    }

    @Override
    public int updateById(T entity) {
        return baseMapper.updateById(entity);
    }

    @Override
    public int deleteById(Serializable id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int deleteByIds(String ids) {
        return baseMapper.deleteByIds(ids);
    }
}
