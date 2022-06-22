package com.bjpowernode.money.mapper;

import com.bjpowernode.money.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int selectUserNumber();

    User selectByPhoneAndPassword(String phone, String loginPassword);

    User selectByPhone(String phone);
}