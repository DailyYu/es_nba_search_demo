package com.yutao.nba_search.ServiceTests;


import com.alibaba.fastjson.JSON;
import com.yutao.nba_search.model.Player;
import com.yutao.nba_search.service.ESBasicService;
import com.yutao.nba_search.service.PlayerService;
import com.yutao.nba_search.util.Result;
import org.apache.ibatis.ognl.ObjectElementsAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ESBasicServiceTests {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ESBasicService esBasicService;

    @Test
    public void addPlayerTest() throws IOException {
        Player player = new Player();
        player.setCname("小乔丹");
        player.setEname("Jordan");
        boolean res = false;
        try{
            res = esBasicService.addPlayer(player, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(res) {
            System.out.println("--------- add player success!");
        } else {
            System.out.println("--------- add player failure!");
        }
    }

    @Test
    public void batchAddPlayerTest() throws IOException {
        List<Player> players = new ArrayList<Player>();
        Player player1 = playerService.getPlayerById(101).toJavaObject(Player.class);
        players.add(player1);
        Player player2 = playerService.getPlayerById(102).toJavaObject(Player.class);
        players.add(player2);

        boolean res = esBasicService.batchAddPlayer(players);
        if(res) {
            System.out.println("--------- add player success!");
        } else {
            System.out.println("--------- add player failure!");
        }
    }

/*    @Test
    public void addAllPlayerToESTest() throws IOException {
        boolean res =esBasicService.addAllPlayerToES();
        if(res) {
            System.out.println("--------- add players success!");
        } else {
            System.out.println("--------- add players failure!");
        }
    }*/

    @Test
    public void deletePlayerTest() throws IOException {
        boolean res = esBasicService.deletePlayer(1);
        if(res) {
            System.out.println("--------- delete player success!");
        } else {
            System.out.println("--------- delete player failure!");
        }
    }

    /*@Test
    public void updatePlayerTest() throws IOException {
        Player player = new Player();
        player.setCname("大乔丹");
        player.setEname("Jordan");
        boolean res = esBasicService.updatePlayer(player, 1);
        if(res) {
            System.out.println("--------- update player success!");
        } else {
            System.out.println("--------- update player failure!");
        }
    }*/

    @Test
    public void getPlayerTest() throws IOException {
        Result res = esBasicService.getPlayer(200);
        System.out.println(JSON.toJSONString(res.getData()));
    }

/*    @Test public void sendRequest() throws IOException, InterruptedException {
        for(int i=0; i<100; i++) {
            esBasicService.getPlayer(i+100);
            Thread.sleep(1);
        }
    }*/

}
