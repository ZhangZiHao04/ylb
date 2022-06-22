package com.bjpowernode.money.service;

import com.bjpowernode.money.model.RechargeRecord;
import com.bjpowernode.money.vo.TopUpRecords;

import java.util.List;

public interface RechargeRecordService {
    List<TopUpRecords> queryByUid(Integer uid, Integer pageNum, int pageSize);

    int queryByRow(Integer uid);

    void add(String rechargeNo, double rechargeMoney, Integer id);

    void updateStatusSuccess(String out_trade_no);

    void updateStatusFailed(String out_trade_no);

    int automaticallyQueryRechargeResults(String out_trade_no);

    List<RechargeRecord> queryByRechargeStatus();
}
