package com.yutao.nba_search.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yutao.nba_search.dao.PlayerDAO;
import com.yutao.nba_search.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerDAO playerDAO;


    public JSONObject getPlayerById(int pid) {
        return playerDAO.getPlayerById(pid);
    }

    public List<JSONObject> listPlayerByPage(int offset, int pageSize) {
        return playerDAO.listPlayer(offset, pageSize);
    }

}
