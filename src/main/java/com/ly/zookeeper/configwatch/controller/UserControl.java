package com.ly.zookeeper.configwatch.controller;

import com.ly.zookeeper.configwatch.model.User;
import com.ly.zookeeper.configwatch.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by leiyang on 2018/12/03
 */
@RestController
@RequestMapping("/user")
public class UserControl {

    @Autowired
    private UserServiceImpl userService;


    @RequestMapping("/list")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }
}
