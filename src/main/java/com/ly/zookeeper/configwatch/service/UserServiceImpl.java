package com.ly.zookeeper.configwatch.service;

import com.ly.zookeeper.configwatch.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leiyang on 2018/12/03
 */
@Service
public class UserServiceImpl extends BaseDao {

    public List<User> getAllUser(){
        return getSession().selectList("getAllUser");
    }
}
