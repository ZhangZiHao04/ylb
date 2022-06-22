package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.mapper.BidInfoMapper;
import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.vo.InvestmentHistory;
import com.bjpowernode.money.vo.InvestmentRanking;
import com.bjpowernode.money.vo.TopRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 5000)
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<InvestmentHistory> queryByUid(Integer uid,Integer pageNum, Integer pageSize) {
        return bidInfoMapper.selectByUid(uid,pageNum,pageSize);
    }

    @Override
    public int queryByRows(Integer uid) {
        return bidInfoMapper.selectByRows(uid);
    }

    @Override
    public void initInvestTop() {
        List<TopRank> topRankList = bidInfoMapper.selectTopRank(1000000);
        for(TopRank topRank:topRankList){
            stringRedisTemplate.opsForZSet().add(Constants.INVEST_TOP,topRank.getPhone(),topRank.getMoney());
        }
    }

    @Override
    public List<TopRank> queryTopRankFromRedies(int num) {
        List<TopRank> topRankList = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, num - 1);
        for (ZSetOperations.TypedTuple<String> tt : typedTuples){
            TopRank topRank = new TopRank();
            topRank.setPhone(tt.getValue());
            topRank.setMoney(tt.getScore());
            topRankList.add(topRank);
        }
        return topRankList;
    }

    @Override
    public void updateInvestTop(String phone, Double bidMoney) {
        stringRedisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,phone,bidMoney);
    }

    @Override
    public void insert(BidInfo bidInfo) {
        bidInfoMapper.insert(bidInfo);
    }

    @Override
    public List<InvestmentRanking> queryById(Integer loanId) {
        return bidInfoMapper.selectById(loanId);
    }
}
