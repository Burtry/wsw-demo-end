package com.example.wswdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.wswdemo.constant.IndexConstant;
import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@Slf4j
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<SearchVO> search(String searchInput) {

        SearchRequest searchRequest = new SearchRequest(IndexConstant.WSW_DEMO_TEST);

        //构建DSL语句
        searchRequest.source().query(QueryBuilders.matchQuery("name",searchInput));

        searchRequest.source().highlighter(new HighlightBuilder().field("name"));

        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //解析结果
            SearchHits hits = response.getHits();
            SearchHit[] searchList = hits.getHits();



            ArrayList<SearchVO> searchVOS = new ArrayList<>();
            for (SearchHit hit : searchList) {

                SearchVO searchVO = JSON.parseObject(hit.getSourceAsString(), SearchVO.class);

                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (!highlightFields.isEmpty()) {
                    HighlightField name = highlightFields.get("name");
                    searchVO.setName(name.getFragments()[0].string());
                    searchVOS.add(searchVO);
                }
            }
            return searchVOS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
