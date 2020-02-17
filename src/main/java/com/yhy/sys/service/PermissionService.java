package com.yhy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhy.sys.domain.Permission;

import java.util.List;

/**
 * @author ${author}
 * @since 2020-02-01
 */
public interface PermissionService extends IService<Permission> {

    public Integer getMaxOrderNum();

    /**
     * 查询用户拥有的菜单或权限
     * @param id 用户id
     */
    public List<Permission> queryUserMenuAndPermissionByUserId(Integer id, String type);
}
