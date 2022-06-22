package com.bjpowernode.money.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.config.AlipayConfig;
import com.bjpowernode.money.model.User;
import com.bjpowernode.money.service.RechargeRecordService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequestMapping("/loan")
public class RechargeController {

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false)
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("/page/toRecharge")
    public String toRecharge(){
        return "toRecharge";
    }

    @PostMapping("/toRecharge")
    public void pay(double rechargeMoney, HttpServletResponse resp, HttpSession session) throws AlipayApiException, IOException, AlipayApiException, IOException {

        //获取登录用户
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        //充值记录号
        String rechargeNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        //创建充值记录
        rechargeRecordService.add(rechargeNo,rechargeMoney,user.getId());

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ rechargeNo +"\","
                + "\"total_amount\":\""+ rechargeMoney +"\","
                + "\"subject\":\"recharge\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //输出
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(result);
    }

    //支付后的回调界面
    @RequestMapping("/toRechargeBack")
    public String toRechargeBack(Model model,HttpServletRequest request,String out_trade_no) throws UnsupportedEncodingException, AlipayApiException {
        //拼装所有的支付宝返回的参数，进行“验证签名”
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        if(signVerified) {
            //验签正确，才能信任这些参数
            //调用支付宝的查询功能，检查订单是否支付成功
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            //设置请求参数
            AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");
            //请求
            String result = alipayClient.execute(alipayRequest).getBody();
            //分析查询后的JSON结果
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject responseObject = jsonObject.getJSONObject("alipay_trade_query_response");
            if("10000".equals(responseObject.getString("code"))){
                String tradeStatus = responseObject.getString("trade_status");
                if("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)){
                    model.addAttribute("trade_msg","支付成功");
                    //充值记录状态改为充值成功，账号增加金额...
                    rechargeRecordService.updateStatusSuccess(out_trade_no);
                }else if("TRADE_CLOSED".equals(tradeStatus)){
                    model.addAttribute("trade_msg","支付失败，订单作废");
                    //充值记录状态改为充值失败,....
                    rechargeRecordService.updateStatusFailed(out_trade_no);
                }else{
                    model.addAttribute("trade_msg","尚未支付，请及时付款");
                }
            }else{
                model.addAttribute("trade_msg","支付结果查询失败，请稍后再检查交易结果");
            }
        }else {
            //验签失败，返回的结果就不算数
            model.addAttribute("trade_msg","验签失败，请稍后再检查交易结果");
        }
        return "toRechargeBack";
    }
}
