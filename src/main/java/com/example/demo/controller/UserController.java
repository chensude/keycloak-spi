package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("test")
    public List<UserEntity> getTest(String username) {
        List<UserEntity> byUserNameAndPass = userMapper.findByUserNameAndPass("123", "123");


       return byUserNameAndPass;

    }
}
