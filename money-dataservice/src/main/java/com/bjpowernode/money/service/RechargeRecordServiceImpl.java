package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.bjpowernode.money.config.AlipayConfig;
import com.bjpowernode.money.mapper.FinanceAccountMapper;
import com.bjpowernode.money.mapper.RechargeRecordMapper;
import com.bjpowernode.money.model.FinanceAccount;
import com.bjpowernode.money.model.RechargeRecord;
import com.bjpowernode.money.vo.TopUpRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Service(interfaceClass = RechargeRecordService.class,version = "1.0.0",timeout = 5000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<TopUpRecords> queryByUid(Integer uid, Integer pageNum, int pageSize) {
        return rechargeRecordMapper.selectByUid(uid,pageNum,pageSize);
    }

    @Override
    public int queryByRow(Integer uid) {
        return rechargeRecordMapper.selectByRows(uid);
    }

    @Override
    public void add(String rechargeNo, double rechargeMoney, Integer id) {
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setUid(id);
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("用户充值");
        rechargeRecordMapper.insert(rechargeRecord);
    }

    @Override
    public void updateStatusSuccess(String out_trade_no) {
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByRecordNo(out_trade_no);
        rechargeRecord.setRechargeStatus("1");
        rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
        FinanceAccount financeAccount = financeAccountMapper.selectByUid(rechargeRecord.getUid());
        financeAccount.setAvailableMoney(financeAccount.getAvailableMoney()+rechargeRecord.getRechargeMoney());
        financeAccountMapper.updateByPrimaryKey(financeAccount);
    }

    @Override
    public void updateStatusFailed(String out_trade_no) {
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByRecordNo(out_trade_no);
        rechargeRecord.setRechargeStatus("2");
        rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
    }

    @Override
    public int automaticallyQueryRechargeResults(String out_trade_no){
        try{
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
                    //充值记录状态改为充值成功，账号增加金额...
                    RechargeRecord rechargeRecord = rechargeRecordMapper.selectByRecordNo(out_trade_no);
                    rechargeRecord.setRechargeStatus("1");
                    rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
                    FinanceAccount financeAccount = financeAccountMapper.selectByUid(rechargeRecord.getUid());
                    financeAccount.setAvailableMoney(financeAccount.getAvailableMoney()+rechargeRecord.getRechargeMoney());
                    financeAccountMapper.updateByPrimaryKey(financeAccount);
                    return 1;
                }else if("TRADE_CLOSED".equals(tradeStatus)){
                    //充值记录状态改为充值失败,....
                    RechargeRecord rechargeRecord = rechargeRecordMapper.selectByRecordNo(out_trade_no);
                    rechargeRecord.setRechargeStatus("2");
                    rechargeRecordMapper.updateByPrimaryKey(rechargeRecord);
                    return 0;
                }
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<RechargeRecord> queryByRechargeStatus() {
        return rechargeRecordMapper.selectByRechargeStatus();
    }
}
