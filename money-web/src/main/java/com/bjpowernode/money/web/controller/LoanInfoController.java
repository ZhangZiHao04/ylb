package com.bjpowernode.money.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.model.BidInfo;
import com.bjpowernode.money.model.LoanInfo;
import com.bjpowernode.money.model.User;
import com.bjpowernode.money.service.BidInfoService;
import com.bjpowernode.money.service.FinanceAccountService;
import com.bjpowernode.money.service.LoanInfoService;
import com.bjpowernode.money.vo.TopRank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/loan")
public class LoanInfoController {
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @GetMapping("/loan")
    public String loan(Model model,Integer pageNum, Integer pType ){
        int pageSize = 9;
        pageNum = pageNum==null?1:pageNum;
        List<LoanInfo> uLoanInfoList = loanInfoService.queryByTypeWithPage(pType,pageNum,pageSize);
        int rows = loanInfoService.queryByTypeRows(pType);
        int pages = rows%pageSize==0?rows/pageSize:rows/pageSize+1;
        model.addAttribute(Constants.INVEST_TOP, bidInfoService.queryTopRankFromRedies(5));
        model.addAttribute("pages",pages);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("uLoanInfoList",uLoanInfoList);
        return "loan";
    }

    @GetMapping("/loanInfo")
    public String loanInfo(Model model , Integer id, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if(user!=null){
            model.addAttribute("account",financeAccountService.queryByUid(user.getId()));
        }
        model.addAttribute("loan",loanInfoService.queryById(id));
        model.addAttribute("history",bidInfoService.queryById(id));
        return "loanInfo";
    }
}