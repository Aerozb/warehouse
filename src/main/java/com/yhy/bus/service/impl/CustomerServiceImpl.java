package com.yhy.bus.service.impl;

import com.yhy.bus.domain.Customer;
import com.yhy.bus.mapper.CustomerMapper;
import com.yhy.bus.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-11
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Override
    @Cacheable(cacheNames = "customer", key = "#entity.id", unless = "#entity == null")
    public boolean save(Customer entity) {
        return super.save(entity);
    }

    @Override
    @CachePut(cacheNames = "customer", key = "#entity.id", unless = "#entity == null")
    public boolean updateById(Customer entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(cacheNames = "customer", key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Cacheable(cacheNames = "customer", key = "#id", unless = "#result == null")
    public Customer getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(cacheNames = "customer", allEntries = true, beforeInvocation = true)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }
}
