package com.yhy.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhy.sys.domain.Dept;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-02-04
 */
public interface DeptMapper extends BaseMapper<Dept> {

    public Integer getMaxOrderNum();

}
