package com.yhy.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhy.sys.domain.Permission;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-02-01
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    public Integer getMaxOrderNum();

    /**
     * /根据权限或菜单ID删除权限表各和角色的关系表里面的数据
     *
     * @param id
     */
    void deleteRolePermissionByPid(Serializable id);

    List<Permission> queryUserMenuAndPermissionByUserId(Integer id, String type);
}
