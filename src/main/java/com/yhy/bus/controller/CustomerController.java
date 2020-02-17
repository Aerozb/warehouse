package com.yhy.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhy.bus.domain.Customer;
import com.yhy.bus.service.CustomerService;
import com.yhy.bus.vo.CustomerVo;
import com.yhy.sys.common.DataGridView;
import com.yhy.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/loadAllCustomer")
    public DataGridView loadAllCustomer(CustomerVo customerVo) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        IPage<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
        wrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername",
                customerVo.getCustomername());
        wrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "phone", customerVo.getPhone());
        wrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson",
                customerVo.getConnectionperson());
        customerService.page(page, wrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 修改
     */
    @RequestMapping("updateCustomer")
    public ResultObj updateCustomer(CustomerVo customerVo) {
        try {
            this.customerService.updateById(customerVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 添加
     */
    @RequestMapping("/addCustomer")
    public ResultObj addCustomer(Customer customer) {
        try {
            customerService.saveOrUpdate(customer);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteCustomer")
    public ResultObj deleteCustomer(Integer id) {
        try {
            this.customerService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }


    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteCustomer")
    public ResultObj batchDeleteCustomer(CustomerVo customerVo) {
        try {
            Collection<Serializable> idList = new ArrayList<Serializable>();
            Collections.addAll(idList, customerVo.getIds());
            this.customerService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}

