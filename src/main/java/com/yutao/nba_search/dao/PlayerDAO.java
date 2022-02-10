package com.yutao.nba_search.dao;

import com.alibaba.fastjson.JSONObject;
import com.yutao.nba_search.model.Player;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlayerDAO {

    //根据id查询数据
    public JSONObject getPlayerById(int pid);

    //查询所有数据
    public List<JSONObject> listAllPlayer();


    //分页查询
    public List<JSONObject> listPlayer(int offset, int pageSize);

}
