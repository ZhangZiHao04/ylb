package com.bjpowernode.money.mapper;

import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;

import java.util.List;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    double selectHistoryAverageRate();

    int selectALlBidMoney();

    List<LoanInfo> selectByTypeWithPage(Integer pType,int pageNum,int pageSize);

    int selectByTypeRows(Integer pType);

    List<TopRank> selectByTopRank();

    List<InvestmentRanking> selectByIdToInvestmentHistory(Integer id);

    List<LoanInfo> selectByProductStatus(int productStatus);
}