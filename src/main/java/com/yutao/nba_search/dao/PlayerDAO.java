package com.yutao.nba_search.dao;

import com.yutao.nba_search.model.Player;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PlayerDAO {

    //查询所有数据
    public List<Player> listAllPlayer();


    //分页查询
    public List<Player> listPlayer(int offset, int pageSize);

}
