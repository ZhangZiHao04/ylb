package com.bjpowernode.money.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanInfoServiceImplTest {

    @Autowired
    private LoanInfoService loanInfoService;

    @Test
    void queryHistoryAverageRate() {
        System.out.println(loanInfoService.queryHistoryAverageRate());
    }

    @Test
    void queryAllBidMoney(){
        System.out.println(loanInfoService.queryAllBidMoney());
    }

    @Test
    void queryByTypeWithPage(){
        loanInfoService.queryByTypeWithPage(2,1,9).forEach(x-> System.out.println(x));
    }

    @Test
    void queryByTypeCount(){
        System.out.println(loanInfoService.queryByTypeRows(2));
    }

    @Test
    void queryById(){
        System.out.println(loanInfoService.queryById(1310694));
    }
}