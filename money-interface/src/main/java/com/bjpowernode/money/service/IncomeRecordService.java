package com.bjpowernode.money.service;

import com.bjpowernode.money.vo.RevenueRecords;

import java.util.List;

public interface IncomeRecordService {
    List<RevenueRecords> queryByUid(Integer uid, Integer pageNum, int pageSize);

    int queryByRows(Integer uid);

    void generateIncomeRecords();

    void CollectionOfPayments();
}