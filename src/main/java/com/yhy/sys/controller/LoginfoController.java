package com.yhy.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhy.sys.common.DataGridView;
import com.yhy.sys.common.ResultObj;
import com.yhy.sys.domain.Loginfo;
import com.yhy.sys.service.LoginfoService;
import com.yhy.sys.vo.LoginfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author ${author}
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/loginfo")
public class LoginfoController {

    @Autowired
    private LoginfoService loginfoService;

    /**
     * 全查询
     */
    @RequestMapping("/loadAllLoginfo")
    public DataGridView loadAllLoginfo(LoginfoVo loginfoVo) {
        QueryWrapper<Loginfo> wrapper = new QueryWrapper<>();
        IPage<Loginfo> page = new Page<>(loginfoVo.getPage(), loginfoVo.getLimit());
        wrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginname()), "loginname", loginfoVo.getLoginname());
        wrapper.like(StringUtils.isNotBlank(loginfoVo.getLoginip()), "loginip", loginfoVo.getLoginip());
        wrapper.ge(loginfoVo.getStartTime() != null, "logintime", loginfoVo.getStartTime());
        wrapper.le(loginfoVo.getEndTime() != null, "logintime", loginfoVo.getEndTime());
        wrapper.orderByDesc("logintime");
        loginfoService.page(page, wrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 删除
     */
    @RequestMapping("deleteLoginfo")
    public ResultObj deleteLoginfo(Integer id) {
        try {
            this.loginfoService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteLoginfo")
    public ResultObj batchDeleteLoginfo(LoginfoVo loginfoVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            Collections.addAll(idList, loginfoVo.getIds());
            this.loginfoService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

