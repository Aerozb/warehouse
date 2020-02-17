package com.yhy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhy.sys.domain.Dept;
import com.yhy.sys.mapper.DeptMapper;
import com.yhy.sys.service.DeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {


    @Override
    public Integer getMaxOrderNum() {
        return getBaseMapper().getMaxOrderNum();
    }

    @Override
    public List<Dept> list() {
        return super.list();
    }

    @Override
    public List<Dept> list(Wrapper<Dept> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    public Dept getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Dept Dept) {
        return super.save(Dept);
    }

    @Override
    public boolean updateById(Dept entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

}
