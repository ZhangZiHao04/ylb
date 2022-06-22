package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.mapper.LoanInfoMapper;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 1500)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public double queryHistoryAverageRate() {
        Double historyRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        if(historyRate==null){
            synchronized (this){
                historyRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
                if(historyRate==null){
                    historyRate = loanInfoMapper.selectHistoryAverageRate();
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyRate, 30, TimeUnit.MINUTES);
                }
            }
        }
        return historyRate;
    }

    @Override
    public int queryAllBidMoney() {
        Integer historyRate = (Integer) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
        if(historyRate==null){
            synchronized (this){
                historyRate = (Integer) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
                if(historyRate==null){
                    historyRate = loanInfoMapper.selectALlBidMoney();
                    redisTemplate.opsForValue().set(Constants.ALL_BID_MONEY, historyRate, 30, TimeUnit.MINUTES);
                }
            }
        }
        return historyRate;
    }

    @Override
    public List<LoanInfo> queryByTypeWithPage(Integer pType,int pageNum,int pageSize) {
        return loanInfoMapper.selectByTypeWithPage(pType,pageNum,pageSize);
    }

    @Override
    public int queryByTypeRows(Integer pType) {
        return loanInfoMapper.selectByTypeRows(pType);
    }

    @Override
    public List<TopRank> queryByTopRank() {
        return loanInfoMapper.selectByTopRank();
    }

    @Override
    public LoanInfo queryById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<InvestmentRanking> queryByIdToInvestmentHistory(Integer id) {
        return loanInfoMapper.selectByIdToInvestmentHistory(id);
    }

    @Override
    public void update(LoanInfo loanInfo) {
        loanInfoMapper.updateByPrimaryKey(loanInfo);
    }

}
