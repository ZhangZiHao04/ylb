package com.bjpowernode.money.mapper;

import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.vo.InvestmentHistory;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;

import java.util.List;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    List<InvestmentHistory> selectByUid(Integer uid,Integer pageNum, Integer pageSize);

    int selectByRows(Integer uid);

    List<TopRank> selectTopRank(int num);

    List<InvestmentRanking> selectById(Integer id);

    List<BidInfo> selectByLoanInfoId(Integer loanId);
}