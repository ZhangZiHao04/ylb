package com.bjpowernode.money.common.util;

public class Test {
    public static void main(String[] args) throws Exception {
        String result = HttpClientUtils.doGet("http://www.baidu.com");
        System.out.println(result);
    }
}
