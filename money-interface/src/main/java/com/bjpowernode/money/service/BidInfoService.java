package com.bjpowernode.money.service;

import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.vo.InvestmentHistory;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;

import java.util.List;

public interface BidInfoService {
    List<InvestmentHistory> queryByUid(Integer uid,Integer pageNum,Integer pageSize);

    int queryByRows(Integer uid);

    void initInvestTop();

    List<TopRank> queryTopRankFromRedies(int num);

    void updateInvestTop(String phone,Double bidMoney);

    void insert(BidInfo bidInfo);

    List<InvestmentRanking> queryById(Integer loanId);
}
