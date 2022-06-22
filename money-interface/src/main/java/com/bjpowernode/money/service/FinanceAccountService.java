package com.bjpowernode.money.service;

import com.bjpowernode.money.model.FinanceAccount;

public interface FinanceAccountService {

    FinanceAccount queryByUid(int uid);

    void update(FinanceAccount account);
}
