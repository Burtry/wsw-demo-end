package com.example.wswdemo.service.impl;

import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<SearchVO> search(String searchInput) {
        return null;
    }
}
