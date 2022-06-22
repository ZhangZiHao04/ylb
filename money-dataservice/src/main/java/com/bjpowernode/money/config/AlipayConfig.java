package com.bjpowernode.money.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000121600318";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvHr4Ha9STZZ0HNyvQX2K3NDys6360XoNyJZmXgwEhzZDLvkK8PVO9/ZrEoQTvz6eewUedpoOKnLPfccm/53O8xhMksL6fVkwRh6NAPSIH1NG4PAWXWQDUnHGQjW5A1YKPgErl9eughOyQO14XR4J4N0bw2AOhMnsnsBPOeX0YqMLVmwsF3kBOQZAhYy+Y/jM22xh66hEcfpzbOgF4cc1AUU32BgyikPtgwgHbDTLabOHdZ4kMAYuxEB2bAAUAM42OVqHVh9yAuzf1F5NAiXboDap+B2642Tz9OU8jEhlGBQQaTOLnqUJxMig3/X6sI0mdynlXLttNXXylPjjVnYbzAgMBAAECggEAVPK1vcXZ/ZJKJQjnURIkQbiuQjeOZBKA8us4YV3kO7CLL7pghw28xKnz3LQ7P9Vce09I3Tm8D45KVvbMpjGxW/49pQuzvHlRNyOz1mKRUnkzLoq3L562H2ywTi29MOghiFj0fC7aGSjB1Iln8CT5LjWqTQSMZjKluS/Zu8wFGeNbqcQlo/y3zuhtLrqtkxtjYfBN300wm52DMnpZGXwn6/DnsKJRH2Nl733vHeRpc1M8FOXaM1QKwTfxREL3a3ovaGwf+n3DDyggvbi3s4vQA81cv0aIrwqYm6OcLCii4R2E8x5TsHnzMToKTH3a5sXFnLlqBVYDPbwGZRcD54z4cQKBgQD2jUx7gP88kO8Yq63/i4awQBi/CKH2QBWjqiRQn+2Ptt4y2p2zzkYcmBgbgsvSEnvbBjPcnvFWE9dW4i/gKhHAc3HE2GyjafBNe4n0edppuDbCJoFK0IlzAHq1LI6/e0kP3+3vkMdl5cBI4Kt9LMEGHEdUFbHT39BpoDAs/DolewKBgQC11LBz5cXus3zvbyNSEYigoIS3ZCH8E9XIFDxnLIb3iPGXSCG0G5gPT0WZjn5PLA/bWPY9UJyOsqkEd7KqeUXQNV3buC+wMhLFIMs4GnmV+Kj6rjlXz1J9TPBFdfy4GQzuLOWNGvERRYvi2QWwMYzTnHKwJiIqvZd6KhwOztce6QKBgHWaztm2nYkpZTNyaOwVhOi7mlI9buxEg03vUaU3tByyUOkXCNSE10K6df1hdZG+CWBfZAufsWSq0DP8bnPR/We8aY9I4QFN/jcnb5WzLjDxkwUnUtL6CCZYGzAikfDagO5WlWuJtuNvJTpeGM77sxMUGB4OvyVehE5t/6zln2LTAoGAeOrNxOTiMZDO3nLr7CGY2Cim2UYPi5CtM/axDHOB9Y7uCgp/z0QiZNv0jt9Bg+jrSpToe3HmCscour3W5KLtGD7FAd6EaAmEje729YI1pNPNm5bMr2ZBlu3ZQnfp7YtLHABjTujBsxiqo9pqe/cXjYmWgyl4wy99UlI6gmZujDECgYB/0LzBvUY0HHzV8lCPjsEQvOK5iKeHcaoFQdOWhQwopm3j3Umv+rYKmifB1hwLXEPd3rW9bCo78Se6E47ZLMlgTYXtA2QP+95Oij78TvNmwdTj82Y78aipLQHUXhccA7AOhsYC4/APTNlM2bfQUh46XDEsm7TqgaawQ1Y8t2nZSg==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn/WQNdd8IOXtuEq0yihGbe6mQGGYB6fgHuXVQFhhVuNx3w3k3YvmGnPjga1LWO6ajfguA9rpa1c19aLnnSIu/IN1G7XmdN0uJzuLVYCcthJ8KNEsXxvTDPsmIWq7ni+ArObrx49/YePlqx67KG9x/lNLXn8awrmQ2ElfiIc8hjYUMBzBo115hYoxBwPymrqK4qiGqljlh0S83cmrZccqyvEbQIus0lSFHFcAhytLTvzhE2KwPH2z6TgrDFMKWApDjYkAR/yAv6HNN4rGezBxJ9JFBRgqelhtRaXpf3uQrPKWIp78sVNY+hc1zSEs0u9WnwMoQI8YuHEQEg5BhXE1YQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/money/loan/toRechargeBack";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关（沙箱网关）
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

