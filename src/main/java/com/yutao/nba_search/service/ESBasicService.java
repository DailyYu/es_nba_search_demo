package com.yutao.nba_search.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yutao.nba_search.dao.PlayerDAO;
import com.yutao.nba_search.model.Player;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ESBasicService {

    private final String INDEX_NAME = "nba"; //将索引名设置成一个常量

    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 单条数据插入
     */
    public boolean addPlayer(Player player, int id) throws IOException {
        //构建请求
        IndexRequest request = new IndexRequest(INDEX_NAME)
                .id(String.valueOf(id)) //指明文档的id
                .source(JSON.toJSONString(player), XContentType.JSON); //指明文档的内容和文档的格式
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT); //发送请求
        if(response.status() == RestStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 批量插入数据
     */
    public boolean batchAddPlayer(List<Player> players) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();

        for(int i = 0; i<players.size(); i++) {
            IndexRequest request = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(players.get(i).getPid()))
                    .source(JSON.toJSONString(players.get(i)), XContentType.JSON);
            bulkRequest.add(request);
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        if(bulkResponse.status() == RestStatus.OK) {
            System.out.println("插入成功");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 插入postgresql中的所有数据到ElasticSearch中
     */
    public boolean addAllPlayerToES() throws IOException {
        List<JSONObject> jsonPlayers = playerDAO.listAllPlayer();

        List<Player> players = new ArrayList<Player>();
        for(JSONObject jo : jsonPlayers) {
            players.add(jo.toJavaObject(Player.class));
        }
        return batchAddPlayer(players);
    }

    /**
     * 删除文档
     */

    public boolean deletePlayer(int pid) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME).id(String.valueOf(pid));
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        if(response.status() == RestStatus.OK) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 更新文档（修改文档）
     */

    public boolean updatePlayer(Player player, int pid) throws IOException {
        UpdateRequest request = new UpdateRequest(INDEX_NAME, String.valueOf(pid))
                .doc(JSON.toJSONString(player), XContentType.JSON);

        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        if(response.status() == RestStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询文档（简单查询）
     */

    public Map<String, Object> getPlayer(int pid) throws IOException {
        //构建request
        GetRequest request = new GetRequest(INDEX_NAME)
                .id(String.valueOf(pid));
        //发送请求，并得到response
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        Map<String, Object> source = response.getSource();
        return source;
    }






}
