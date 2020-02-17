package com.yhy.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhy.sys.domain.Permission;
import com.yhy.sys.mapper.PermissionMapper;
import com.yhy.sys.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author ${author}
 * @since 2020-02-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public Integer getMaxOrderNum() {
        return getBaseMapper().getMaxOrderNum();
    }

    @Override
    public List<Permission> queryUserMenuAndPermissionByUserId(Integer id, String type) {
        return getBaseMapper().queryUserMenuAndPermissionByUserId(id, type);
    }

    @Override
    public boolean removeById(Serializable id) {
        PermissionMapper permissionMapper = this.getBaseMapper();
        //根据权限或菜单ID删除权限表各和角色的关系表里面的数据
        permissionMapper.deleteRolePermissionByPid(id);
        //删除 权限表的数据
        return super.removeById(id);
    }
}
