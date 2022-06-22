package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.money.mapper.FinanceAccountMapper;
import com.bjpowernode.money.model.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = FinanceAccountService.class,version = "1.0.0",timeout = 5000)
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryByUid(int uid) {
        return financeAccountMapper.selectByUid(uid);
    }

    @Override
    public void update(FinanceAccount account) {
        financeAccountMapper.updateByPrimaryKey(account);
    }
}
