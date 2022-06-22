package com.bjpowernode.money.mapper;

import com.bjpowernode.money.model.IncomeRecord;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.vo.RevenueRecords;

import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    List<RevenueRecords> selectByUid(Integer uid, Integer pageNum, int pageSize);

    int selectByRows(Integer uid);

    List<IncomeRecord> selectByProductStatusAndDate(int productStatus, String date);
}