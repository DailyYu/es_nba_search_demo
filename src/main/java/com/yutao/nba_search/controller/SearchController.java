package com.yutao.nba_search.controller;


import com.yutao.nba_search.service.ESSearchService;
import com.yutao.nba_search.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ESSearchService esSearchService;

    //http://localhost:8080/search/term?field=ename&val=%E5%BF%AB%E8%88%B9&from=0&size=5
    @RequestMapping("/term")
    public Result searchByFieldWithTerm(@RequestParam("field") String keyword,
                                         @RequestParam("val") String val,
                                         @RequestParam("from") int from,
                                         @RequestParam("size") int size) throws IOException {

        return esSearchService.searchWithTerm(keyword, val, from, size);
    }

    //http://localhost:8080/search/match?field=team_name&val=%E5%BF%AB%E8%88%B9&from=0&size=5
    @GetMapping("/match")
    public Result searchByFieldWithMatch(@RequestParam("field") String keyword,
                                         @RequestParam("val") String val,
                                         @RequestParam("from") int from,
                                         @RequestParam("size") int size) throws IOException {
        return esSearchService.searchWithMatch(keyword, val, from, size);
    }

    //http://localhost:8080/search/boolQuery?team_name=快船&position=后卫&from=0&size=5
    @GetMapping("/boolQuery")
    public Result searchByFieldWithBoolQuery(@RequestParam("team_name") String teamName,
                                         @RequestParam("position") String position,
                                         @RequestParam("from") int from,
                                         @RequestParam("size") int size) throws IOException {
        return esSearchService.searchWithBoolQuery(teamName, position, from, size);
    }

}
