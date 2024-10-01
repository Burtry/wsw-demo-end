package com.example.wswdemo.service;

import com.example.wswdemo.pojo.vo.SearchVO;

import java.util.List;

public interface ISearchService{
    /**
     * 用户搜索
     * @param searchInput
     * @return
     */
    List<SearchVO> search(String searchInput);
}
