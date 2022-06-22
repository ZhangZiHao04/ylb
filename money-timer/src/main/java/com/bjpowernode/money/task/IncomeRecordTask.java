package com.bjpowernode.money.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.money.model.RechargeRecord;
import com.bjpowernode.money.service.IncomeRecordService;
import com.bjpowernode.money.service.RechargeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncomeRecordTask {
    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",check = false)
    private IncomeRecordService incomeRecordService;

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false)
    private RechargeRecordService rechargeRecordService;

    @Scheduled(cron = "50 30 16 * * ?")
    public void generateIncomeRecords(){
        incomeRecordService.generateIncomeRecords();
        System.out.println("---生成收益计划");
    }

    @Scheduled(cron = "30 36 16 * * ?")
    public void CollectionOfPayments(){
        incomeRecordService.CollectionOfPayments();
        System.out.println("---生成收益回款");
    }

    @Scheduled(cron = "0 1 17 * * ?")
    private void automaticallyQueryRechargeResults(){
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryByRechargeStatus();
        for(RechargeRecord rechargeRecord : rechargeRecordList) {
            int i = rechargeRecordService.automaticallyQueryRechargeResults(rechargeRecord.getRechargeNo());
            if(i==1){
                System.out.println("---充值成功");
            }
        }
    }
}
