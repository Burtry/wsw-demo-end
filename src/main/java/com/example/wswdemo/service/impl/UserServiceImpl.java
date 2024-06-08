package com.example.wswdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByAccountAndPassword(String account, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account)
                .eq("password",password);

        User user = userMapper.selectOne(wrapper);
        return user;
    }
}
