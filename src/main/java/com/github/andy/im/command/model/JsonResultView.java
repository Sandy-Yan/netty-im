package com.github.andy.im.command.model;

import java.io.Serializable;
import java.util.Collections;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 18:11
 */
public class JsonResultView implements Serializable {

    // 返回状态位(0.正确，其他错误)
    private int code;

    // 内部提示
    private String innerMsg;

    // 用户信息
    private String msg;

    // 返回数据列
    private Object data;

    public static final JsonResultView SUCCESS = new JsonResultView(0, "SUCCESS", "SUCCESS");

    public JsonResultView(int code) {
        this(code, "", "");
    }

    public JsonResultView(int code, String innerMsg, String msg) {
        this(code, innerMsg, msg, Collections.EMPTY_MAP);
    }

    public JsonResultView(int code, String innerMsg, String msg, Object data) {
        this.code = code;
        this.innerMsg = innerMsg;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getInnerMsg() {
        return innerMsg;
    }

    public String getMsg() {
        return msg;
    }

    public long getNow() {
        return System.currentTimeMillis();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
