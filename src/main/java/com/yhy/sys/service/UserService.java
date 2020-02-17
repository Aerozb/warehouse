package com.yhy.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhy.sys.domain.User;

import java.io.Serializable;

/**
 * @author ${author}
 * @since 2020-01-31
 */
public interface UserService extends IService<User> {

    /**
     * 保存用户和角色之间的关系
     * @param uid
     * @param ids
     */
    void saveUserRole(Integer uid, Integer[] ids);
}
