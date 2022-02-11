package com.yutao.nba_search.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yutao.nba_search.model.Player;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


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
     *
     * 因为倒排索引的原因，这个里可能会出现有数据，但是查询不到的情况
      */

    public SearchHit[] searchWithTerm(String keyword, String val) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //TermQueryBuilder builder =  QueryBuilders.termQuery(keyword + ".keyword", val); //这样写英文能查出内容，中文查不出
        TermQueryBuilder builder =  QueryBuilders.termQuery(keyword, val); //这样写中文和英文都查不出内容
        searchSourceBuilder.query(builder);
        request.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();

        return hits;
    }


    /**
     * 分词查询某个字段
     */

    public SearchHit[] searchWithMatch(String keyword, String val) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME); //创建SearchRequest实例

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder =  QueryBuilders.matchQuery(keyword, val); //具体的查询条件
        searchSourceBuilder.query(builder).from(0).size(5); //设置分页，如果不设置分页，好像是默认显示十条记录
        /* 分页设置也可以这样
        searchSourceBuilder.from(0); //设置分页，起始位置
        searchSourceBuilder.size(5); //设置分页，每页数量*/
        request.source(searchSourceBuilder); //设置请求源

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT); //查询并得到结果
        SearchHit[] hits = response.getHits().getHits(); //固定写法

        return hits;
    }

    /**
     * 组合查询（布尔查询），多个查询条件
     * 查询某个球队的某个位置，按照职业生涯开始时间排序
     */

    public SearchHit[] searchWithBoolQuery(String keyword1, String team_name, String keyword2, String position) throws IOException {
        SearchRequest  request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


//        QueryBuilders.boolQuery().must();//文档必须完全匹配条件，相当于and
//        QueryBuilders.boolQuery().mustNot();//文档必须不匹配条件，相当于not
//        QueryBuilders.boolQuery().should();//至少满足一个条件，这个文档就符合should，相当于or

        // 组合条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.matchQuery(keyword1, team_name)); //既是该球队
        builder.must(QueryBuilders.matchQuery(keyword2, position));  //又是该位置
        searchSourceBuilder.query(builder);
        //排序写法1
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("birthday").order(SortOrder.ASC);
        searchSourceBuilder.sort(sortBuilder); //这里可以构建一个FieldSortBuilder的List，来进行多个字段的排序

        //排序写法2
        //searchSourceBuilder.sort("birthday", SortOrder.ASC);

        //对text类型字段进行排序，执行会报错误，比如：searchSourceBuilder.sort("career", SortOrder.ASC);

        //分页
        searchSourceBuilder.from(0); //设置分页，起始位置
        searchSourceBuilder.size(5); //设置分页，每页数量*/

        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        return response.getHits().getHits();
    }



    /**
     * 聚合查询，计算查询结果数量
     */

    public SearchHit[] searchWithAggregationCount(String keyword, String val) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder = QueryBuilders.matchQuery(keyword, val);
        AggregationBuilder aggregation = AggregationBuilders.count("player_num").field("pid"); //计数
        searchSourceBuilder.query(builder);
        searchSourceBuilder.aggregation(aggregation);
        searchSourceBuilder.from(0).size(20);

        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getAggregations()));
        return  response.getHits().getHits();
    }

    /**
     * 聚合查询，查询结果根据球队名分组
     */

    public SearchHit[] searchWithAggregationTerms(String keyword, String val) throws IOException {
        SearchRequest request = new SearchRequest(INDEX_NAME);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder = QueryBuilders.matchQuery(keyword, val);
        //分组要在keyword字段上才行，不然执行会报错
        AggregationBuilder aggregation = AggregationBuilders.terms("term_birthday").field("birthday");
        searchSourceBuilder.query(builder);
        searchSourceBuilder.aggregation(aggregation);

        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        System.out.println(JSON.toJSONString(response.getAggregations()));
        return response.getHits().getHits();
    }

}
