package com.example.wswdemo;

import com.alibaba.fastjson.JSON;
import com.example.wswdemo.constant.IDConstant;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.service.ISpaceService;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
public class ElasticSearchDemo {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private IEquipmentService equipmentService;

    //@BeforeEach
    //void setUp() {
    //    this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
    //            HttpHost.create("http://192.168.50.128:9200")
    //    ));
    //}
    //
    //@AfterEach
    //void tearDown() throws Exception{
    //    this.restHighLevelClient.close();
    //}

    @Test
    void esTest() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("test");
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    void esTestDelete() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
        restHighLevelClient.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
    }


    @Test
    void testAddDocument() throws IOException {
        List<Space> spaceList = spaceService.list();

        BulkRequest bulkRequest = new BulkRequest();

        for (Space space : spaceList) {
            bulkRequest.add(new IndexRequest("wsw-demo-space").id(space.getId().toString()).source(JSON.toJSONString(space), XContentType.JSON));
        }

        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
    }

    @Test
    void testAddDocumentDemo() throws IOException {
        List<Space> spaceList = spaceService.list();
        List<Equipment> equipmentList = equipmentService.list();


        List<SearchVO> searchVOList = new ArrayList<>();

        for (Space space : spaceList) {
            SearchVO searchVO = new SearchVO();
            searchVO.setId(space.getId() + 10000L); //场地id 1开头
            searchVO.setName(space.getSpaceName());
            searchVO.setPrice(space.getPrice());
            searchVO.setUrl(space.getImg());
            searchVO.setType("space");
            searchVOList.add(searchVO);
        }

        for (Equipment equipment : equipmentList) {
            SearchVO searchVO = new SearchVO();
            searchVO.setId(equipment.getId() + 20000L);//器材id 2开头
            searchVO.setName(equipment.getEquipmentName());
            searchVO.setPrice(equipment.getRentalPrice());
            searchVO.setUrl(equipment.getImg());
            searchVO.setType("equipment");
            searchVOList.add(searchVO);
        }


        BulkRequest bulkRequest = new BulkRequest();

        for (SearchVO searchVO : searchVOList) {
            bulkRequest.add(new IndexRequest("wsw-demo-test").id(String.valueOf(searchVO.getId())).source(JSON.toJSONString(searchVO), XContentType.JSON));
        }

        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
    }






    @Test
    void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("wsw-demo-space",String.valueOf(38));
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        System.out.println(sourceAsString);

        Space space = JSON.parseObject(sourceAsString, Space.class);
        System.out.println("------------------");
        System.out.println(space);
    }

    @Test
    void testDeleteDoc() throws IOException {
        List<Space> list = spaceService.list();
        BulkRequest request = new BulkRequest();
        for (Space space : list) {
            request.add(new DeleteRequest("wsw-demo-space").id(String.valueOf(space.getId())));
        }
        restHighLevelClient.bulk(request,RequestOptions.DEFAULT);

    }

    @Test
    void testUpdateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("wsw-demo-space",String.valueOf(38));
        request.doc("price",233);

        restHighLevelClient.update(request,RequestOptions.DEFAULT);
    }

    @Test
    void testSearch() throws IOException {
        //准备request
        SearchRequest searchRequest = new SearchRequest("wsw-demo-space");
        //构建DSL参数

        searchRequest.source().query(QueryBuilders.matchQuery("spaceName","测试"));

        //searchRequest.source().query(QueryBuilders.termQuery("id", "38"));

        //searchRequest.source().query(QueryBuilders.rangeQuery("price").gt(20));


        //BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("spaceName", "滑雪"));

        //searchRequest.source().query(boolQueryBuilder);


        ////算分控制
        //FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQueryBuilder,
        //        new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{new FunctionScoreQueryBuilder.FilterFunctionBuilder(
        //                QueryBuilders.termQuery("id",42),ScoreFunctionBuilders.weightFactorFunction(10)
        //        )}
        //        );

        //functionScoreQuery.boostMode(CombineFunction.SUM);

        //searchRequest.source().query(boolQueryBuilder);
        ////添加排序则不会进行算分，按照排序规则排序
        //searchRequest.source().sort("price", SortOrder.DESC);
        //searchRequest.source().from(1).size(1);

        //将查询请求放入查询
        //searchRequest.source().query(functionScoreQuery);


        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        handResponse(response);
    }

    private static void handResponse(SearchResponse response) {
        SearchHits hits = response.getHits();
        System.out.println(Arrays.toString(hits.getHits()));

        System.out.println("------------------------------------------------------------------------------");
        TotalHits totalHits = hits.getTotalHits();

        System.out.println("====");
        System.out.println(totalHits);
        System.out.println("====");

        SearchHit[] searchList = hits.getHits();
        List<Space> spaceList = new ArrayList<>();
        for (SearchHit documentFields : searchList) {
            String sourceAsString = documentFields.getSourceAsString();
            spaceList.add(JSON.parseObject(sourceAsString,Space.class));
        }
        System.out.println(spaceList);
    }

    @Test
    void testHighLight() throws IOException {
        //准备Request
        SearchRequest searchRequest = new SearchRequest(IDConstant.WSW_DEMO_TEST);

        //构建DSL
        //1.准备query
        searchRequest.source().query(QueryBuilders.matchQuery("name","测试"));

        //2.准备highlight
        searchRequest.source().highlighter(new HighlightBuilder().field("name"));

        //发送请求
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //处理搜索结果
        //handResponse(response);
        //处理高亮
        System.out.println("处理高亮如下：---------------------------------------");
        handResponseWithHigh(response);
    }

    private void handResponseWithHigh(SearchResponse response) {
        SearchHit[] hits = response.getHits().getHits();
        List<Space> spaceList = new ArrayList<>();
        for (SearchHit hit : hits) {

            Space space = JSON.parseObject(hit.getSourceAsString(), Space.class);

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (!highlightFields.isEmpty()) {
                HighlightField spaceName = highlightFields.get("name");
                //System.out.println(spaceName);
                space.setSpaceName(spaceName.getFragments()[0].string());
                spaceList.add(space);
            }
        }
        System.out.println(spaceList);
    }

    @Test
    void testAgg() throws IOException {

        SearchRequest searchRequest = new SearchRequest("wsw-demo-space");

        //准备DSL query
        searchRequest.source().query(QueryBuilders.matchQuery("spaceName","滑雪"));

        //agg
        //searchRequest.source().aggregation(AggregationBuilders.terms("idAgg").field("id").size(10));
        searchRequest.source().aggregation(AggregationBuilders.terms("typeAgg").field("description").size(10));

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //处理搜索结果
        //handResponse(response);

        Aggregations aggregations = response.getAggregations();
        Terms idAggTerms = aggregations.get("typeAgg");
        List<? extends Terms.Bucket> buckets = idAggTerms.getBuckets();

        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println(keyAsString);
        }
    }
}















