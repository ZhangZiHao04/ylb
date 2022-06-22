package com.bjpowernode.money.service;

import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;

import java.util.List;

public interface LoanInfoService {
    double queryHistoryAverageRate();

    int queryAllBidMoney();

    List<LoanInfo> queryByTypeWithPage(Integer pType,int pageNum,int pageSize);

    int queryByTypeRows(Integer pType);

    List<TopRank> queryByTopRank();

    LoanInfo queryById(Integer id);

    List<InvestmentRanking> queryByIdToInvestmentHistory(Integer id);

    void update(LoanInfo loanInfo);
}
