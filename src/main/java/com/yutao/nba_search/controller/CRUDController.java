package com.yutao.nba_search.controller;


import com.yutao.nba_search.service.ESBasicService;
import com.yutao.nba_search.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/curd")
public class CRUDController {

    @Autowired
    private ESBasicService esBasicService;

    @GetMapping()
    public String hello() {
        return "hello world!";
    }

    //http://localhost:8080/curd/getPlayerById/101
    @GetMapping("/getPlayerById/{id}")
    public Result getPlayerById(@PathVariable("id") int playerId) throws IOException {
        return esBasicService.getPlayer(playerId);
    }

}
