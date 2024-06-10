package com.example.wswdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wswdemo.pojo.dto.UserDTO;
import com.example.wswdemo.pojo.dto.UserRegisterDTO;
import com.example.wswdemo.pojo.dto.UserUpdateDTO;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public User getByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);

        return userMapper.selectOne(wrapper);
    }


    @Override
    public void register(UserRegisterDTO userRegisterDTO) {

        //进行md5加密
        String md5PassWord = Md5Util.getMD5String(userRegisterDTO.getPassword());
        User user = new User();
        BeanUtil.copyProperties(userRegisterDTO,user);
        user.setPassword(md5PassWord);
        //设置为普通用户
        user.setRole(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        lambdaUpdate().set(User::getUsername,userUpdateDTO.getUsername())
                .set(User::getSex,userUpdateDTO.getSex())
                .set(User::getPhone,userUpdateDTO.getPhone())
                .set(User::getEMail,userUpdateDTO.getEMail())
                .set(User::getAddress,userUpdateDTO.getAddress())
                .set(User::getDetailAddress,userUpdateDTO.getDetailAddress())
                .set(User::getUpdateTime,LocalDateTime.now())
                .eq(User::getId,userUpdateDTO.getId()).update();

    }
}
