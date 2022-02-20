package com.yutao.nba_search.ServiceTests;


import com.alibaba.fastjson.JSON;
import com.yutao.nba_search.service.ESSearchService;
import com.yutao.nba_search.util.Result;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ESSearchServiceTests {

    @Autowired
    private ESSearchService esSearchService;


    @Test
    public void searchWithTerm() throws IOException {
        Result res = esSearchService.searchWithTerm("cname", "林", 0, 5);
        System.out.println("--------searchWithTerm---------");
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void searchWithMatch() throws IOException {
        Result res = esSearchService.searchWithMatch("team_name", "快船", 0, 5);
        System.out.println("--------searchWithMatch---------");
        System.out.println(JSON.toJSONString(res.getData()));
    }


    @Test
    public void searchWithBoolQueryTest() throws IOException {
        Result res = esSearchService.searchWithBoolQuery("快船",  "后卫", 0, 5);
        System.out.println("--------searchWithBoolQuery---------");
        System.out.println(JSON.toJSONString(res));
    }


    @Test
    public void searchWithAggregationCountTest() throws IOException {
        SearchHit[] res = esSearchService.searchWithAggregationCount("team_name", "快船");
        System.out.println(res.length);
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void searchWithAggregationTermsTest() throws IOException {
        Result res = esSearchService.searchWithAggregationGroup("position", "后卫");
        System.out.println(JSON.toJSONString(res));
    }
}
