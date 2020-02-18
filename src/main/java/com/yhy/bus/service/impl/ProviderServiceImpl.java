package com.yhy.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhy.bus.domain.Provider;
import com.yhy.bus.mapper.ProviderMapper;
import com.yhy.bus.service.ProviderService;
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
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {

    @Override
    public boolean save(Provider entity) {
        boolean save = super.save(entity);
        return save;
    }

    @Override
    @CachePut(cacheNames = "provider", key = "#entity.id", unless = "#entity == null")
    public boolean updateById(Provider entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(cacheNames = "provider", key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Cacheable(cacheNames = "provider", key = "#id", unless = "#result == null")
    public Provider getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(cacheNames = "provider", allEntries = true, beforeInvocation = true)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }
}
