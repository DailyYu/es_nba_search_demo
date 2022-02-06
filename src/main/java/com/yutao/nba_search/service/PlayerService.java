package com.yutao.nba_search.service;


import com.yutao.nba_search.dao.PlayerDAO;
import com.yutao.nba_search.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerDAO playerDAO;

    public List<Player> listPlayerByPage(int offset, int pageSize) {
        return playerDAO.listPlayer(offset, pageSize);
    }

}
