package com.example.wswdemo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.wswdemo.pojo.dto.UserDTO;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.pojo.vo.UserVO;
import com.example.wswdemo.properties.JwtProperties;
import com.example.wswdemo.service.IUserService;
import com.example.wswdemo.utils.JwtUtil;
import com.example.wswdemo.utils.Md5Util;
import com.example.wswdemo.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<UserVO> userLogin(String account, String password) {
            User user = userService.getByAccount(account);

            //是否存在用户
        if (BeanUtil.isEmpty(user)) {
            return Result.error("账号错误！");
        }
        if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
            return Result.error("密码错误！");
        }

        //为用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        //放入用户的唯一标识(userId: 用户id)
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);


        UserVO userVo = UserVO.builder()
                .token(token)
                .build();
        BeanUtil.copyProperties(user,userVo);

        return Result.success(userVo,"登录成功!");
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        User user = userService.lambdaQuery().eq(User::getAccount, userDTO.getAccount()).one();
        if (user == null) {
            //注册
            userService.register(userDTO);
            return Result.success();
        }
        //账号存在
        return Result.error("账号已存在!");
    }




}
