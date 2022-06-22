package com.bjpowernode.money.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.money.common.constant.Constants;
import com.bjpowernode.money.common.util.HttpClientUtils;
import com.bjpowernode.money.mapper.FinanceAccountMapper;
import com.bjpowernode.money.mapper.UserMapper;
import com.bjpowernode.money.model.FinanceAccount;
import com.bjpowernode.money.model.User;
import com.bjpowernode.money.vo.ApiResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Service(interfaceClass = UserService.class,version = "1.0.0",timeout = 1500)
public class UserServiceImpl implements UserService {

    @Value("${wx.appkey")
    private String wxAppkey;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public int queryUserNumber() {
        Integer historyRate = (Integer) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
        if(historyRate==null){
            synchronized (this){
                historyRate = (Integer) redisTemplate.opsForValue().get(Constants.ALL_USER_COUNT);
                if(historyRate==null){
                    historyRate = userMapper.selectUserNumber();
                    redisTemplate.opsForValue().set(Constants.ALL_USER_COUNT, historyRate, 30, TimeUnit.MINUTES);
                }
            }
        }
        return historyRate;
    }

    @Override
    public User login(String phone, String loginPassword) {
        return userMapper.selectByPhoneAndPassword(phone,loginPassword);
    }

    @Override
    public boolean sendRegSmsCode(String phone) {
        String code = RandomStringUtils.randomNumeric(4);
        stringRedisTemplate.opsForValue().set("reg-sms-"+phone,code,5,TimeUnit.MINUTES);
        try{
            String url = "https://way.jd.com/chuangxin/dxjk";
            Map<String,String> params= new HashMap<>();
            params.put("appKey",wxAppkey);
            params.put("mobile",phone);
            params.put("content", "【创信】你的验证码是：" + code);
            //String result = HttpClientUtils.doGet(url,params);
            String result = "{\n" +                                     //模拟结果
                            "    \"code\": \"10000\",\n" +
                            "    \"charge\": false,\n" +
                            "    \"remain\": 0,\n" +
                            "    \"msg\": \"查询成功\",\n" +
                            "    \"result\": {\n" +
                            "        \"ReturnStatus\": \"Success\",\n" +
                            "        \"Message\": \"ok\",\n" +
                            "        \"RemainPoint\": 925997,\n" +
                            "        \"TaskID\": 80517716,\n" +
                            "        \"SuccessCounts\": 1\n" +
                            "    },\n" +
                            "    \"requestId\": \"dda6c018da234a228f8afad98b5f05e9\"\n" +
                            "}";
            JSONObject jsonObject = JSON.parseObject(result);
            if("10000".equals(jsonObject.getString("code"))){
                JSONObject resultObject = jsonObject.getJSONObject("result");
                if("Success".equals(resultObject.getString("ReturnStatus"))){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyRegSmsCode(String phone, String messageCode) {
        String code = stringRedisTemplate.opsForValue().get("reg-sms-" + phone);
        if(code==null || code.equals(messageCode)==false){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public User queryByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public User register(String phone, String loginPassword) {
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        Date now = new Date();
        user.setAddTime(now);
        user.setLastLoginTime(now);
        userMapper.insert(user);
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setAvailableMoney(0d);
        financeAccount.setUid(user.getId());
        financeAccountMapper.insert(financeAccount);
        return user;
    }

    @Override
    public ApiResult verifyRealName(String realName, String cardNo) {
        String url="https://way.jd.com/hl/idcardcheck";
        Map<String,String> params = new HashMap<>();
        params.put("appkey", this.wxAppkey);
        params.put("realName", realName);
        params.put("cardNo", cardNo);
        try{
            //String json = HttpClientUtils.doGet(url, params);
            String json = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 0,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"张**\",\n" +
                    "            \"idcard\": \"123456************\",\n" +
                    "            \"isok\": true,\n" +
                    "            \"IdCardInfor\": {\n" +
                    "                \"province\": \"广东省\",\n" +
                    "                \"city\": \"广州市\",\n" +
                    "                \"district\": \"天河区\",\n" +
                    "                \"area\": \"广东省广州市天河区\",\n" +
                    "                \"sex\": \"男\",\n" +
                    "                \"birthday\": \"1990-8-12\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"requestId\": \"96933f51599e4a3b9c46519651a3e323\"\n" +
                    "}";
            JSONObject jsonObject = JSON.parseObject(json);
            if("10000".equals(jsonObject.getString("code"))){
                JSONObject resultObject = jsonObject.getJSONObject("result").getJSONObject("result");
                Boolean ok = resultObject.getBoolean("isok");
                if(ok){
                    return ApiResult.success();
                }else {
                    return ApiResult.error("真实姓名和身份证号不一致，无法实名。");
                }
            }else {
                return ApiResult.error("身份认证失败，请稍后重试。");
            }
        }catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error("身份认证失败，请联系管理员");
        }
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }
}
