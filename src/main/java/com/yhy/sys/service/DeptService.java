package com.yhy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhy.sys.domain.Dept;

import java.io.Serializable;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-04
 */
public interface DeptService extends IService<Dept> {
    public Integer getMaxOrderNum();

}
