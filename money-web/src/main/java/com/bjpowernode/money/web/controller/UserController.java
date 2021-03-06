package com.bjpowernode.money.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.model.FinanceAccount;
import com.bjpowernode.money.model.User;
import com.bjpowernode.money.service.*;
import com.bjpowernode.money.vo.ApiResult;
import com.bjpowernode.money.vo.InvestmentHistory;
import com.bjpowernode.money.vo.RevenueRecords;
import com.bjpowernode.money.vo.TopUpRecords;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/loan")
public class UserController {

    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false)
    private RechargeRecordService rechargeRecordService;

    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",check = false)
    private IncomeRecordService incomeRecordService;

    @GetMapping("/page/login")
    public String login(){
        return "login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ApiResult login(String phone, String loginPassword, String captcha, HttpSession session){
        String code = (String)session.getAttribute("loginCode");
        if(captcha==null || !code.equalsIgnoreCase(captcha)){
            return ApiResult.error("?????????????????????");
        }
        //??????
        User loginUser = userService.login(phone,loginPassword);
        if(loginUser==null){
            return ApiResult.error("?????????????????????");
        }else {
            session.setAttribute(Constants.SESSION_USER,loginUser);
            return ApiResult.success();
        }
    }

    @RequestMapping("/login-captcha")
    public void loginCaptcha(HttpServletResponse resp, HttpSession session) throws IOException {
        //????????????
        BufferedImage img = new BufferedImage(78,36, BufferedImage.TYPE_INT_RGB);
        //????????????
        Graphics graphics = img.createGraphics();
        //????????????
        graphics.setColor(Color.WHITE);
        graphics.fillRect(1,1,76,34);
        //??????????????????(4???)
        String code = RandomStringUtils.randomAlphabetic(4);
        //??????????????????
        graphics.setColor(Color.GREEN);
        //??????????????????
        Font font = new Font("Arial",Font.ITALIC,26);
        //????????????????????????
        graphics.setFont(font);
        //?????????????????????????????????
        graphics.drawString(code,5,30);
        //?????????session???
        session.setAttribute("loginCode",code);
        //????????????
        ImageIO.write(img,"jpg",resp.getOutputStream());
    }

    @GetMapping("/page/register")
    public String register(){
        return "register";
    }

    @RequestMapping("/api/reg-sms-code")
    @ResponseBody
    public ApiResult regSmsCode(String phone){
        boolean ok = userService.sendRegSmsCode(phone);
        if(ok){
            return ApiResult.success();
        }else {
            return ApiResult.error("????????????????????????????????????");
        }
    }

    @RequestMapping("/api/register")
    @ResponseBody
    public ApiResult register(String phone, String messageCode, String loginPassword, HttpSession session){
        User user  = userService.queryByPhone(phone);
        if(user!=null){
            return ApiResult.error("????????????????????????");
        }
        boolean ok = userService.verifyRegSmsCode(phone,messageCode);
        if(ok==false){
            return ApiResult.error("??????????????????");
        }
        User loginUser = userService.register(phone,loginPassword);
        session.setAttribute(Constants.SESSION_USER,loginUser);
        return ApiResult.success();
    }

    @GetMapping("/page/realName")
    public String realName(){
        return "realName";
    }

    @RequestMapping("/api/realName")
    @ResponseBody
    public ApiResult realName(String realName,String cardNo ,String captcha,HttpSession session){
        String code = (String)session.getAttribute("loginCode");
        if(captcha==null || !code.equalsIgnoreCase(captcha)){
            return ApiResult.error("?????????????????????");
        }
        ApiResult apiResult = userService.verifyRealName(realName,cardNo);
        if(apiResult.getCode()!=200){
            return ApiResult.error(apiResult.getMessage());
        }
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        user.setIdCard(cardNo);
        user.setName(realName);
        userService.update(user);
        return ApiResult.success();
    }

    @RequestMapping("/page/myCenter")
    public String myCenter(Model model, HttpSession session){
        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = financeAccountService.queryByUid(loginUser.getId());
        model.addAttribute("account",financeAccount);
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("histories",bidInfoService.queryByUid(loginUser.getId(),1,5));
        model.addAttribute("upRecords",rechargeRecordService.queryByUid(loginUser.getId(),1,5));
        model.addAttribute("revenue",incomeRecordService.queryByUid(loginUser.getId(),1,5));
        return "myCenter";
    }

    //????????????
    @RequestMapping("/myAccount")
    public String myAccount(Model model,HttpSession session){
        User loginUser = (User) session.getAttribute(Constants.SESSION_USER);
        model.addAttribute("account",financeAccountService.queryByUid(loginUser.getId()));
        model.addAttribute("loginUser",loginUser);
        return "myAccount";
    }

    //????????????
    @RequestMapping("/myInvest")
    public String myInvest(Model model,HttpSession session,Integer pageNum){
        int pageSize = 6;
        pageNum = pageNum == null?1:pageNum;
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        List<InvestmentHistory> investmentHistories = bidInfoService.queryByUid(user.getId(),pageNum,pageSize);
        int rows = bidInfoService.queryByRows(user.getId());
        int pages = rows%pageSize==0?rows/pageSize:rows/pageSize+1;
        model.addAttribute("rows",rows);
        model.addAttribute("pages",pages);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("histories",investmentHistories);
        return "myInvest";
    }

    //????????????
    @RequestMapping("/myRecharge")
    public String myRecharge(Model model,HttpSession session,Integer pageNum){
        int pageSize=6;
        pageNum = pageNum==null?1:pageNum;
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        List<TopUpRecords> upRecords = rechargeRecordService.queryByUid(user.getId(),pageNum,pageSize);
        int rows = rechargeRecordService.queryByRow(user.getId());
        int pages = rows%pageSize==0?rows/pageSize:rows/pageSize+1;
        model.addAttribute("rows",rows);
        model.addAttribute("pages",pages);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("upRecords",upRecords);
        return "myRecharge";
    }

    //????????????
    @RequestMapping("/myIncome")
    public String myIncome(Model model,HttpSession session,Integer pageNum){
        int pageSize=6;
        pageNum = pageNum==null?1:pageNum;
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        List<RevenueRecords> revenueRecords = incomeRecordService.queryByUid(user.getId(),pageNum,pageSize);
        int rows = incomeRecordService.queryByRows(user.getId());
        int pages = rows%pageSize==0?rows/pageSize:rows/pageSize+1;
        model.addAttribute("rows",rows);
        model.addAttribute("pages",pages);
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("revenue",revenueRecords);
        return "myIncome";
    }
}