package com.bjpowernode.money.mapper;

import com.bjpowernode.money.model.RechargeRecord;
import com.bjpowernode.money.vo.TopUpRecords;

import java.util.List;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    List<TopUpRecords> selectByUid(Integer uid, Integer pageNum, int pageSize);

    int selectByRows(Integer uid);

    RechargeRecord selectByRecordNo(String out_trade_no);

    List<RechargeRecord> selectByRechargeStatus();
}