package com.yhy.sys.controller;


import com.yhy.sys.common.ActiverUser;
import com.yhy.sys.common.LoginfoSender;
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

    @Autowired
    private LoginfoSender loginfoSender;

    @RequestMapping("/login")
    public ResultObj login(String loginname, String pwd) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname, pwd);
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user", activerUser.getUser());

            //登录日志对象生成
            Loginfo loginfo = new Loginfo();
            loginfo.setLoginname(activerUser.getUser().getLoginname());
            loginfo.setLoginip(WebUtils.getRequest().getRemoteAddr());
            loginfo.setLogintime(new Date());
            //使用mq保存登陆日志
            loginfoSender.sendMessage(loginfo);
            //使用mq之前的方式
            //loginfoService.save(loginfo);
            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }

    }
}

