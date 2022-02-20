package com.yutao.nba_search.ServiceTests;

import com.alibaba.fastjson.JSON;
import com.yutao.nba_search.model.Player;
import com.yutao.nba_search.service.PlayerService;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PlayerServiceTests {
    @Autowired
    private PlayerService playerService;


    @Test
    public void getPlayerById() {
        Player player = playerService.getPlayerById(100).toJavaObject(Player.class);
        System.out.println(JSON.toJSONString(player));
    }


    @Test
    public void listPlayerByPageTest() {
        List<JSONObject> res = playerService.listPlayerByPage(0,5);
//        for(Player p : res) {
//            System.out.println(JSON.toJSONString(p));
//        }
    }
}
