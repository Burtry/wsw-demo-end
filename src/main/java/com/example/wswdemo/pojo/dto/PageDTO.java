package com.example.wswdemo.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<V> {
    /**
     * 总数
     */
    private Long total;

    /**
     * 当前页数
     */
    private Long pages;


    /**
     * 数据列表
     */
    private List<V> list;

}
