package com.bjpowernode.money.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.model.FinanceAccount;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.model.User;
import com.bjpowernode.money.service.BidInfoService;
import com.bjpowernode.money.service.FinanceAccountService;
import com.bjpowernode.money.service.LoanInfoService;
import com.bjpowernode.money.vo.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.Date;

@Controller
public class BidInfoController {

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;


    @GetMapping("/loan/api/initInvestTop")
    @ResponseBody
    public String initInvestTop(){
        bidInfoService.initInvestTop();
        return "投资排行榜初始成功";
    }

    @PostMapping("/api/invest")
    @ResponseBody
    public ApiResult invest(Double bidMoney, Integer infoId, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount account = financeAccountService.queryByUid(user.getId());
        if(account.getAvailableMoney()<bidMoney){
            return ApiResult.error("投资金额不能超过可用资金");
        }else {
            account.setAvailableMoney(account.getAvailableMoney()-bidMoney);
            financeAccountService.update(account);
        }
        LoanInfo loanInfo = loanInfoService.queryById(infoId);
        if(loanInfo.getProductStatus()!=0){
            return ApiResult.error("该产品已经满标");
        }
        if(loanInfo.getLeftProductMoney()<bidMoney){
            return ApiResult.error("投资金额不能超过剩余可投金额");
        }else {
            loanInfo.setLeftProductMoney(loanInfo.getLeftProductMoney()-bidMoney);
            if(loanInfo.getLeftProductMoney()-bidMoney==0){
                loanInfo.setProductStatus(1);
                loanInfo.setProductFullTime(new Date());
                loanInfoService.update(loanInfo);
            }
        }
        if(loanInfo.getBidMaxLimit()<bidMoney){
            return ApiResult.error("投资金额不能超过最大投资限额");
        }
        if(loanInfo.getBidMinLimit()>bidMoney){
            return ApiResult.error("投资金额不能低于起投金额");
        }
        double income = 0.0;
        if(loanInfo.getProductStatus()==0){
            income = bidMoney * (loanInfo.getRate()/100/365) * loanInfo.getCycle();
        }else {
            income = bidMoney * (loanInfo.getRate()/100/365) * loanInfo.getCycle() * 30 ;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        String money = nf.format(income+bidMoney);

        BidInfo bidInfo = new BidInfo();
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidStatus(1);
        bidInfo.setBidTime(new Date());
        bidInfo.setLoanId(loanInfo.getId());
        bidInfo.setUid(user.getId());
        bidInfoService.insert(bidInfo);

        bidInfoService.updateInvestTop(user.getPhone(),bidMoney);

        return ApiResult.success(money);
    }


}
