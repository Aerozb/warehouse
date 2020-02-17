package com.yhy.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhy.sys.common.*;
import com.yhy.sys.domain.Permission;
import com.yhy.sys.domain.User;
import com.yhy.sys.service.PermissionService;
import com.yhy.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yhy.sys.common.Constast.TYPE_MNEU;

/**
 * @author ${author}
 * @since 2020-02-01
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("available", Constast.AVAILABLE_TRUE).eq("type", TYPE_MNEU);
        List<Permission> menus = null;
        User user = (User) WebUtils.getSession().getAttribute("user");
        if (user.getType().equals(Constast.USER_TYPE_SUPER)) {
            menus = permissionService.list(wrapper);
        }  else {
            //根据用户ID+角色+权限去查询
            menus = permissionService.queryUserMenuAndPermissionByUserId(user.getId(), TYPE_MNEU);
        }
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (Permission menu : menus) {
            Integer id = menu.getId();
            Integer pid = menu.getPid();
            String title = menu.getTitle();
            String icon = menu.getIcon();
            String href = menu.getHref();
            Boolean spread = menu.getOpen().equals(Constast.OPEN_TRUE) ? true : false;
            treeNodes.add(new TreeNode(id, pid, title, icon, href, spread));
        }
        treeNodes = TreeNodeBuilder.build(treeNodes, 1);
        return new DataGridView(treeNodes);
    }

/****************菜单管理开始****************/

    /**
     * 加载菜单管理左边的菜单树的json
     */
    @RequestMapping("loadMenuManagerLeftTreeJson")
    public DataGridView loadMenuManagerLeftTreeJson(PermissionVo permissionVo) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", TYPE_MNEU);
        List<Permission> list = this.permissionService.list(queryWrapper);
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Permission menu : list) {
            Boolean spread = menu.getOpen() == 1 ? true : false;
            treeNodes.add(new TreeNode(menu.getId(), menu.getPid(), menu.getTitle(), spread));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询
     */
    @RequestMapping("loadAllMenu")
    public DataGridView loadAllMenu(PermissionVo permissionVo) {
        IPage<Permission> page = new Page<>(permissionVo.getPage(), permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(permissionVo.getId() != null, "id", permissionVo.getId()).or().eq(permissionVo.getId() != null, "pid", permissionVo.getId());
        queryWrapper.eq("type", TYPE_MNEU);//只能查询菜单
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()), "title", permissionVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        this.permissionService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 加载最大的排序码
     */
    @RequestMapping("loadMenuMaxOrderNum")
    public Map<String, Object> loadDeptMaxOrderNum() {
        Map<String, Object> map = new HashMap<>();
        Integer maxOrderNum = permissionService.getMaxOrderNum();
        if (maxOrderNum != null) {
            map.put("maxOrderNum", maxOrderNum + 1);
        } else {
            map.put("maxOrderNum", 1);
        }
        return map;
    }


    /**
     * 添加
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("addMenu")
    public ResultObj addMenu(PermissionVo permissionVo) {
        try {
            permissionVo.setType(TYPE_MNEU);//设置添加类型
            this.permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }


    /**
     * 修改
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("updateMenu")
    public ResultObj updateMenu(PermissionVo permissionVo) {
        try {
            this.permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }


    /**
     * 查询当前的ID的菜单有没有子菜单
     */
    @RequestMapping("checkMenuHasChildrenNode")
    public Map<String, Object> checkMenuHasChildrenNode(PermissionVo permissionVo) {
        Map<String, Object> map = new HashMap<String, Object>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", permissionVo.getId());
        List<Permission> list = this.permissionService.list(queryWrapper);
        if (list.size() > 0) {
            map.put("value", true);
        } else {
            map.put("value", false);
        }
        return map;
    }

    /**
     * 删除
     *
     * @param permissionVo
     * @return
     */
    @RequestMapping("deleteMenu")
    public ResultObj deleteMenu(PermissionVo permissionVo) {
        try {
            this.permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /****************菜单管理结束****************/

}

