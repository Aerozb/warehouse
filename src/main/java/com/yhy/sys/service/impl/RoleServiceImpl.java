package com.yhy.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhy.sys.domain.Role;
import com.yhy.sys.mapper.RoleMapper;
import com.yhy.sys.service.RoleService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public boolean removeById(Serializable id) {
        //根据角色ID删除sys_role_permission
        this.getBaseMapper().deleteRolePermissionByRid(id);
        //根据角色ID删除sys_role_user
        this.getBaseMapper().deleteRoleUserByRid(id);
        return super.removeById(id);
    }

    @Override
    public List<Integer> queryRolePermissionIdsByRid(Integer roleId) {
        return getBaseMapper().queryRolePermissionIdsByRid(roleId);
    }

    @Override
    public void saveRolePermission(Integer roleId, Integer[] ids) {
        RoleMapper roleMapper = this.getBaseMapper();
        //根据rid删除sys_role_permission
        roleMapper.deleteRolePermissionByRid(roleId);
        if(ids!=null&&ids.length>0) {
            for (Integer pid : ids) {
                roleMapper.saveRolePermission(roleId,pid);
            }
        }
    }


    /**
     * 查询当前用户拥有的角色ID集合
     */
    @Override
    public List<Integer> queryUserRoleIdsByUid(Integer id) {
        return this.getBaseMapper().queryUserRoleIdsByUid(id);
    }
}
