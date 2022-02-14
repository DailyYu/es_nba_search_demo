package com.yutao.nba_search.util;

import com.alibaba.fastjson.JSONObject;

public class Result {

    private int code;

    private String msg;

    private Object data;

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return success(200, "成功！", null);
    }

    public static Result success(Object data) {
        return success(200, "成功！", data);
    }

    public static Result success(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result failure() {
        return success(400, "失败！", null);
    }

    public static Result failure(Object data) {
        return success(400, "失败！", data);
    }

    public static Result failure(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
