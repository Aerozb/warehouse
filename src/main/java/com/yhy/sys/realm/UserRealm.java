package com.yhy.sys.realm;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yhy.sys.common.ActiverUser;
import com.yhy.sys.common.Constast;
import com.yhy.sys.domain.Permission;
import com.yhy.sys.domain.Role;
import com.yhy.sys.domain.User;
import com.yhy.sys.service.PermissionService;
import com.yhy.sys.service.RoleService;
import com.yhy.sys.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        User user = userService.getOne(queryWrapper.eq("loginname", token.getPrincipal()));
        if (user != null) {
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            //查询当前用户拥有的角色
            Integer userId = user.getId();
            List<Integer> rids = roleService.queryUserRoleIdsByUid(userId);
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.in("id", rids);
            List<Role> roles = roleService.list(wrapper);
            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
            activerUser.setRoles(roleNames);
            //查询当前用户拥有的权限
            List<Permission> permissions = permissionService.queryUserMenuAndPermissionByUserId(userId, Constast.TYPE_PERMISSION);
            List<String> permissionNames = new ArrayList<>();
            for (Permission permission : permissions) {
                permissionNames.add(permission.getPercode());
            }
            activerUser.setPermissions(permissionNames);
            //创建盐
            ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            //第二个参数，传的是从数据库中获取的password，然后再与token中的password进行对比，
            // 匹配上了就通过，匹配不上就报异常
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser, user.getPwd(), salt, getName());
            return info;
        }
        return null;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        ActiverUser activerUser = (ActiverUser) principals.getPrimaryPrincipal();
        User user = activerUser.getUser();
        List<String> permissions = activerUser.getPermissions();
        List<String> roles = activerUser.getRoles();
        if (user.getType().equals(Constast.USER_TYPE_SUPER)) {
            info.addStringPermission("*:*");
            info.addRole("*");
        } else {
            if (CollectionUtil.isNotEmpty(permissions)) {
                info.addStringPermissions(permissions);
                info.addRoles(roles);
            }
        }
        return info;
    }

}
