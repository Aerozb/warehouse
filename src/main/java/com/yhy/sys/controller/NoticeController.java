package com.yhy.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhy.sys.common.DataGridView;
import com.yhy.sys.common.ResultObj;
import com.yhy.sys.common.WebUtils;
import com.yhy.sys.domain.Notice;
import com.yhy.sys.domain.User;
import com.yhy.sys.service.NoticeService;
import com.yhy.sys.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * @author ${author}
 * @since 2020-02-03
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/loadAllNotice")
    public DataGridView loadAllNotice(NoticeVo noticeVo) {
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        IPage<Notice> page = new Page<>(noticeVo.getPage(), noticeVo.getLimit());
        Date startTime = noticeVo.getStartTime();
        Date endTime = noticeVo.getEndTime();
        wrapper.ge(startTime != null, "createtime", startTime);
        wrapper.le(startTime != null, "createtime", endTime);
        wrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()), "title", noticeVo.getTitle());
        wrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()), "opername", noticeVo.getOpername());
        noticeService.page(page, wrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("/addNotice")
    public ResultObj addNotice(Notice notice) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            notice.setCreatetime(LocalDateTime.now());
            notice.setOpername(user.getName());
            noticeService.saveOrUpdate(notice);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteNotice")
    public ResultObj deleteNotice(Integer id) {
        try {
            this.noticeService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteNotice")
    public ResultObj batchDeleteNotice(NoticeVo noticeVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            Collections.addAll(idList, noticeVo.getIds());
            this.noticeService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

