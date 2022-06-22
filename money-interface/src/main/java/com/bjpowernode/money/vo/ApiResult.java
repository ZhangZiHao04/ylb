package com.bjpowernode.money.vo;

import java.io.Serializable;

public class ApiResult implements Serializable {

    public static ApiResult success(){
        return new ApiResult(200,"执行成功",null);
    }
    public static ApiResult success(Object data){
        return new ApiResult(200,"执行成功",data);
    }

    public static ApiResult error(String message){
        return new ApiResult(500,message,null);
    }

    public ApiResult() {
    }

    public ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
