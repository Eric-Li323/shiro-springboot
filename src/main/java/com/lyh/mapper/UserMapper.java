package com.lyh.mapper;

import com.lyh.pojo.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    User queryUserByName(String name);
}
