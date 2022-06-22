package com.bjpowernode.money.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.service.LoanInfoService;
import com.bjpowernode.money.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE,loanInfoService.queryHistoryAverageRate());
        model.addAttribute(Constants.ALL_USER_COUNT,userService.queryUserNumber());
        model.addAttribute(Constants.ALL_BID_MONEY,loanInfoService.queryAllBidMoney());
        List<LoanInfo> xLoanInfoList = loanInfoService.queryByTypeWithPage(Constants.PRODUCT_TYPE_X,1,1);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryByTypeWithPage(Constants.PRODUCT_TYPE_U,1,4);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryByTypeWithPage(Constants.PRODUCT_TYPE_S,1,8);
        model.addAttribute("xLoanInfoList",xLoanInfoList);
        model.addAttribute("uLoanInfoList",uLoanInfoList);
        model.addAttribute("sLoanInfoList",sLoanInfoList);
        return "index";
    }


}