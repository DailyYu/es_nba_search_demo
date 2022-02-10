package com.yutao.nba_search.ServiceTests;


import com.alibaba.fastjson.JSON;
import com.yutao.nba_search.model.Player;
import com.yutao.nba_search.service.ESBasicService;
import com.yutao.nba_search.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ESBasicServiceTests {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ESBasicService esBasicService;

    @Test
    public void addPlayerTest() {
        Player player = playerService.getPlayerById(100).toJavaObject(Player.class);
        System.out.println(JSON.toJSONString(player));

        boolean res = esBasicService.addPlayer(player, player.getPid());
        if(res) {
            System.out.println("--------- add player success!");
        } else {
            System.out.println("--------- add player failure!");
        }
    }

    @Test
    public void batchAddPlayerTest() {
        List<Player> players = new ArrayList<Player>();
        Player player1 = playerService.getPlayerById(101).toJavaObject(Player.class);
        players.add(player1);
        Player player2 = playerService.getPlayerById(102).toJavaObject(Player.class);
        players.add(player2);

        boolean res = false;
        try{
            res = esBasicService.batchAddPlayer(players);
        }catch (IOException e) {
            System.out.println(e.toString());
        }
        if(res) {
            System.out.println("--------- add player success!");
        } else {
            System.out.println("--------- add player failure!");
        }
    }

    @Test
    public void addAllPlayerToESTest() {
        boolean res =esBasicService.addAllPlayerToES();
        if(res) {
            System.out.println("--------- add players success!");
        } else {
            System.out.println("--------- add players failure!");
        }
    }
}
