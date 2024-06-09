package com.example.wswdemo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.pojo.entity.textUser;
import com.example.wswdemo.pojo.vo.UserVO;
import com.example.wswdemo.properties.JwtProperties;
import com.example.wswdemo.service.IUserService;
import com.example.wswdemo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/user")
public class FindController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtProperties jwtProperties;


    @GetMapping("/find")
    public UserVO find(textUser textUser) {

        //查询用户
        User user = userService.getById(1);

        //为用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        //放入用户的唯一标识(userId: 用户id)
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);


        UserVO userVo = UserVO.builder()
                .token(token)
                .build();
        BeanUtil.copyProperties(user,userVo);
        return userVo;
    }






}
