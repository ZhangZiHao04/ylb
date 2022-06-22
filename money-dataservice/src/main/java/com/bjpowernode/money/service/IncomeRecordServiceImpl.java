package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.mapper.BidInfoMapper;
import com.bjpowernode.money.mapper.FinanceAccountMapper;
import com.bjpowernode.money.mapper.IncomeRecordMapper;
import com.bjpowernode.money.mapper.LoanInfoMapper;
import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.model.FinanceAccount;
import com.bjpowernode.money.model.IncomeRecord;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.vo.RevenueRecords;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = IncomeRecordService.class,version = "1.0.0",timeout = 5000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RevenueRecords> queryByUid(Integer uid, Integer pageNum, int pageSize) {
        return incomeRecordMapper.selectByUid(uid,pageNum,pageSize);
    }

    @Override
    public int queryByRows(Integer uid) {
        return incomeRecordMapper.selectByRows(uid);
    }

    @Override
    public void generateIncomeRecords() {
        List<LoanInfo> infoList = loanInfoMapper.selectByProductStatus(1);
        for(LoanInfo loanInfo : infoList) {
            List<BidInfo> bidInfoList = bidInfoMapper.selectByLoanInfoId(loanInfo.getId());
            for(BidInfo bidInfo:bidInfoList){
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setIncomeStatus(0);
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setUid(bidInfo.getUid());

                Date incomeDate = null;
                Double incomeMoney = null;
                if(loanInfo.getProductType() == Constants.PRODUCT_TYPE_X){
                    incomeDate = DateUtils.addDays(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate()/100/365) * loanInfo.getCycle();
                }else {
                    incomeDate = DateUtils.addDays(loanInfo.getProductFullTime(),loanInfo.getCycle()*30);
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate()/100/365) * loanInfo.getCycle()*30;
                }
                incomeMoney = (double)Math.round(incomeMoney*100)/100;
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecordMapper.insert(incomeRecord);
            }
            loanInfo.setProductStatus(2);
            loanInfoMapper.updateByPrimaryKey(loanInfo);
        }
    }

    @Override
    public void CollectionOfPayments() {
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectByProductStatusAndDate(0, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        for(IncomeRecord incomeRecord : incomeRecordList){
            Double money = incomeRecord.getIncomeMoney() + incomeRecord.getBidMoney();
            FinanceAccount account = financeAccountMapper.selectByUid(incomeRecord.getUid());
            account.setAvailableMoney(account.getAvailableMoney() + money);
            financeAccountMapper.updateByPrimaryKey(account);
            incomeRecord.setIncomeStatus(1);
            int i = incomeRecordMapper.updateByPrimaryKey(incomeRecord);
        }
    }
}