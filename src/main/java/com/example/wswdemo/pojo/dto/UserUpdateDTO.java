package com.example.wswdemo.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateDTO implements Serializable {
    private Long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 现居城市
     */
    private String address;

    /**
     * 详细地址
     */
    private String detailAddress;

}
