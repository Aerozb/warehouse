package com.yhy.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yhy.sys.common.*;
import com.yhy.sys.domain.Permission;
import com.yhy.sys.domain.User;
import com.yhy.sys.service.PermissionService;
import com.yhy.sys.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MenuControllerTest {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Test
    public DataGridView loadIndexLeftMenuJson() throws JsonProcessingException {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("available", Constast.AVAILABLE_TRUE).eq("type", Constast.TYPE_MNEU);
        List<Permission> menus = null;
        WebUtils.getSession().setAttribute("user", userService.getById(1));
        User user = (User) WebUtils.getSession().getAttribute("user");
        if (user.getType().equals(Constast.USER_TYPE_SUPER)) {
            menus = permissionService.list(wrapper);
        } else if (user.getType().equals(Constast.USER_TYPE_NORMAL)) {
            menus = permissionService.list(wrapper);
        }
            else {
            //根据用户ID+角色+权限去查询

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
}