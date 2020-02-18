package com.yhy.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhy.sys.common.DataGridView;
import com.yhy.sys.common.ResultObj;
import com.yhy.sys.common.TreeNode;
import com.yhy.sys.common.WebUtils;
import com.yhy.sys.domain.Permission;
import com.yhy.sys.domain.Role;
import com.yhy.sys.domain.User;
import com.yhy.sys.service.PermissionService;
import com.yhy.sys.service.RoleService;
import com.yhy.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ${author}
 * @since 2020-02-07
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/loadAllRole")
    public DataGridView loadAllRole(RoleVo roleVo) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        IPage<Role> page = new Page<>(roleVo.getPage(), roleVo.getLimit());
        wrapper.like(StringUtils.isNotBlank(roleVo.getName()), "name", roleVo.getName());
        wrapper.like(StringUtils.isNotBlank(roleVo.getRemark()), "remark", roleVo.getRemark());
        wrapper.eq(roleVo.getAvailable() != null, "available", roleVo.getAvailable());
        roleService.page(page, wrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("/addRole")
    public ResultObj addRole(Role role) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            role.setCreatetime(LocalDateTime.now());

            roleService.saveOrUpdate(role);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/deleteRole")
    public ResultObj deleteRole(Integer id) {
        try {
            this.roleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据角色ID加载菜单和权限的树的json串
     */
    @RequestMapping("/initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer roleId) {
        List<Permission> permissionAll = permissionService.list();
        List<Integer> pids = roleService.queryRolePermissionIdsByRid(roleId);
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission permission : permissionAll) {
            String checkArr = "0";
            for (Integer pid : pids) {
                if (permission.getId().equals(pid)) {
                    checkArr = "1";
                    break;
                }
            }
            Boolean spread = permission.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(permission.getId(), permission.getPid(), permission.getTitle(), spread, checkArr));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 保存角色和菜单权限之间的关系
     */
    @RequestMapping("/saveRolePermission")
    public ResultObj saveRolePermission(Integer rid, Integer[] ids) {
        try {
            this.roleService.saveRolePermission(rid, ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }
    }
}

