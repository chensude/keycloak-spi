package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

     UserEntity getUserByUserName(String username);

    List<UserEntity> findByUserNameAndPass(@Param("username") String username, @Param("password") String value);
}
