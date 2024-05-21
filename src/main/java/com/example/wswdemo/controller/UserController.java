package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.User;
import com.example.wswdemo.pojo.textUser;
import com.example.wswdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-05-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping("/find")
    public User findUser(textUser user) {
        System.out.println(user);
        return new User();
    }

    @PostMapping("/find")
    public User textPost(@RequestBody textUser user) {
        System.out.println(user);
        return new User();
    }
}
