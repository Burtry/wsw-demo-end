package com.example.wswdemo.service;

import com.example.wswdemo.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
public interface IUserService extends IService<User> {

    /**
     * 根据账号和密码查询用户
     * @param account
     * @param password
     * @return
     */
    User getByAccountAndPassword(String account, String password);
}
