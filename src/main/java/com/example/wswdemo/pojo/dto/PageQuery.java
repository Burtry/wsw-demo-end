package com.example.wswdemo.pojo.dto;

import lombok.Data;

@Data
public class PageQuery {

    /**
     * 当前页数
     */
    private Integer pageNum;

    /**
     * 页面数
     */
    private Integer pageSize;

    /**
     * 根据sortBy排序
     */
    private String sortBy;
}
