package com.github.andy.im.command.model;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 18:20
 */
public enum MetaCode {
    ParamError(400, "param error", "param error"),//
    NoExistCmd(401, "no exist cmd", "no exist cmd"),//

    ServerError(500, "server error", "server error"),//
    ;

    private int code;

    private String innerMsg;

    private String msg;

    MetaCode(int code, String innerMsg, String msg) {
        this.code = code;
        this.innerMsg = innerMsg;
        this.msg = msg;
    }

    public JsonResultView toJsonView() {
        return new JsonResultView(code, innerMsg, msg);
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
}
