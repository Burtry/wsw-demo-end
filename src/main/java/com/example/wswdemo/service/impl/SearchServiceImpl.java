package com.example.wswdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.wswdemo.constant.IDConstant;
import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@Slf4j
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<SearchVO> search(String searchInput) {

        SearchRequest searchRequest = new SearchRequest(IDConstant.WSW_DEMO_TEST);

        //构建DSL语句
        searchRequest.source().query(QueryBuilders.matchQuery("name",searchInput));

        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //解析结果
            SearchHits hits = response.getHits();
            SearchHit[] searchList = hits.getHits();

            ArrayList<SearchVO> searchVOS = new ArrayList<>();
            for (SearchHit documentFields : searchList) {
                String sourceAsString = documentFields.getSourceAsString();
                searchVOS.add(JSON.parseObject(sourceAsString, SearchVO.class));
            }

            return searchVOS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
