package com.yhy.sys.controller;


import com.yhy.sys.common.ActiverUser;
import com.yhy.sys.common.ResultObj;
import com.yhy.sys.common.WebUtils;
import com.yhy.sys.domain.Loginfo;
import com.yhy.sys.service.LoginfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author ${author}
 * @since 2020-01-31
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginfoService loginfoService;

    @RequestMapping("/login")
    public ResultObj login(String loginname, String pwd) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user", activerUser.getUser());
            Loginfo loginfo = new Loginfo();
            loginfo.setLoginname(activerUser.getUser().getLoginname());
            loginfo.setLoginip(WebUtils.getRequest().getRemoteAddr());
            loginfo.setLogintime(new Date());
            loginfoService.save(loginfo);
            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }

    }
}

