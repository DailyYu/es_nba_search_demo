package com.yutao.nba_search.ServiceTests;


import com.alibaba.fastjson.JSON;
import com.yutao.nba_search.service.ESSearchService;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ESSearchServiceTests {

    @Autowired
    private ESSearchService esSearchService;


    @Test
    public void searchWithTerm() throws IOException {
        SearchHit[] res = esSearchService.searchWithTerm("ename", "George Hill");
        System.out.println("--------searchWithTerm---------");
        System.out.println(res.length);
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void searchWithMatch() throws IOException {
        SearchHit[] res = esSearchService.searchWithMatch("cname", "林书豪");
        System.out.println("--------searchWithMatch---------");
        System.out.println(res.length);
        System.out.println(JSON.toJSONString(res));
    }


    @Test
    public void searchWithBoolQueryTest() throws IOException {
        SearchHit[] res = esSearchService.searchWithBoolQuery("team_name", "快船", "position", "后卫");
        System.out.println("--------searchWithBoolQuery---------");
        System.out.println(res.length);
        System.out.println(JSON.toJSONString(res));
    }
}
