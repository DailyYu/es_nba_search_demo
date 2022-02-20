package com.yutao.nba_search.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yutao.nba_search.model.Player;
import com.yutao.nba_search.util.Result;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ESSearchService {

    private final String INDEX_NAME = "nba"; //将索引名设置成一个常量

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 完全匹配搜索,term是代表完全匹配，即不进行分词器分析，文档中必须包含整个搜索的词汇
     * 模糊匹配搜索（所有字段）
     * 模糊匹配搜索 （某一字段）
     * 搜索结果排序：单个字段，多个字段
     * 搜索结果分页
     * 组合查询（布尔查询）
     * 聚合搜索
     */


    /**
     * 不分词查询某个字段
      */

    public Result searchWithTerm(String keyword, String val, int from, int size) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //TermQueryBuilder builder =  QueryBuilders.termQuery(keyword + ".keyword", val);
        TermQueryBuilder builder =  QueryBuilders.termQuery(keyword, val); //因为不分词，如果是text类型字段则汉字只能查询一个字，英语是一个单词。如果是keyword字段则是完全匹配才能查询出结果
        searchSourceBuilder.query(builder).from(from).size(size);
        request.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        if(response.status() != RestStatus.OK) {
            return Result.failure();
        }
        SearchHit[] hits = response.getHits().getHits(); //固定写法
        List<Map<String, Object>> data = new ArrayList<>();
        for(SearchHit hit:hits) {
            data.add(hit.getSourceAsMap());
        }
        return Result.success(data);
    }


    /**
     * 分词查询某个字段
     */

    public Result searchWithMatch(String keyword, String val, int from, int size) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME); //创建SearchRequest实例

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder =  QueryBuilders.matchQuery(keyword, val); //具体的查询条件
        searchSourceBuilder.query(builder).from(from).size(size); //设置分页，如果不设置分页默认返回十条记录
        request.source(searchSourceBuilder); //设置请求源

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT); //查询并得到结果
        if(response.status() != RestStatus.OK) {
            return Result.failure();
        }
        //System.out.println(JSON.toJSONString(response));
        SearchHit[] hits = response.getHits().getHits(); //固定写法
        List<Map<String, Object>> data = new ArrayList<>();
        for(SearchHit hit:hits) {
            data.add(hit.getSourceAsMap());
        }
        return Result.success(data);
    }

    /**
     * 组合查询（布尔查询），多个查询条件
     * 查询某个球队的某个位置，按照薪资降序
     */

    public Result searchWithBoolQuery(String team_name, String position, int from, int size) throws IOException {
        SearchRequest  request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*
        QueryBuilders.boolQuery().must();//文档必须完全匹配条件，相当于and
        QueryBuilders.boolQuery().mustNot();//文档必须不匹配条件，相当于not
        QueryBuilders.boolQuery().should();//至少满足一个条件，这个文档就符合should，相当于or
        */

        // 组合条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.matchQuery("team_name", team_name)); //既是该球队
        builder.must(QueryBuilders.matchQuery("position", position));  //又是该位置
        searchSourceBuilder.query(builder);
        //排序写法1
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("salary").order(SortOrder.DESC);
        searchSourceBuilder.sort(sortBuilder); //这里可以构建一个FieldSortBuilder的List，来进行多个字段的排序

        //排序写法2
        //searchSourceBuilder.sort("salary", SortOrder.DESC);
        //对text类型字段进行排序，执行会报错误，比如：searchSourceBuilder.sort("career", SortOrder.ASC);

        //分页
        searchSourceBuilder.from(from); //设置分页，起始位置
        searchSourceBuilder.size(size); //设置分页，每页数量*/

        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        if(response.status() != RestStatus.OK) {
            return Result.failure();
        }
        SearchHit[] hits = response.getHits().getHits();
        List<Map<String, Object>> data = new ArrayList<>();
        for(SearchHit hit:hits) {
            data.add(hit.getSourceAsMap());
        }
        return Result.success(data);
    }


    /**
     * 聚合查询，根据球员位置查询，然后按照球队分组,返回每个球队该位置的人数
     */

    public Result searchWithAggregationGroup(String keyword, String val) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder = QueryBuilders.matchQuery(keyword, val);
        //不能在text类型字段上分组，不然执行会报错
        AggregationBuilder aggregation = AggregationBuilders.terms("teamName").field("team_name");
        searchSourceBuilder.aggregation(aggregation);
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        if(response.status() != RestStatus.OK) {
          return Result.failure();
        }
        Terms terms = response.getAggregations().get("teamName");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<Map<String, Long>> data = new ArrayList<Map<String, Long>>();
        for(Terms.Bucket bucket:buckets) {
            Map<String, Long> temp = new HashMap<String, Long>();
            temp.put(bucket.getKeyAsString(), bucket.getDocCount());
            data.add(temp);
        }
        return Result.success(data);
    }

    /**
     * 聚合查询嵌套
     */

}
