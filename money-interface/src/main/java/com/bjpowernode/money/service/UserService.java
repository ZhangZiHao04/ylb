package com.bjpowernode.money.service;

import com.bjpowernode.money.model.User;
import com.bjpowernode.money.vo.ApiResult;

public interface UserService {
    int queryUserNumber();

    User login(String phone, String loginPassword);

    boolean sendRegSmsCode(String phone);

    boolean verifyRegSmsCode(String phone,String messageCode);

    User queryByPhone(String phone);

    User register(String phone,String loginPassword);

    ApiResult verifyRealName(String realName, String cardNo);

    void update(User user);
}
