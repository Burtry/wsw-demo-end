package com.example.wswdemo.controller;

import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.ISearchService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Autowired
    private ISearchService searchService;

    @GetMapping("/{searchInput}")
    public Result search(@PathVariable("searchInput") String searchInput) {
      log.info("搜索开始:{}",searchInput);
      List<SearchVO> list = searchService.search(searchInput);
      return Result.success(list,"搜索成功!");
    }
}
