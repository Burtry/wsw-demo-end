package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.User;
import com.example.wswdemo.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-05-20
 */
@RestController
@RequestMapping("/user")
@Tag(name = "userController")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "findUser")
    @GetMapping("/find")
    public User findUser(Long id) {
        User user = userService.getById(id);
        return user;
    }
}
