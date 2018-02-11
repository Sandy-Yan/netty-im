package com.github.andy.im.command.model;

import java.io.Serializable;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 17:47
 */
public class RawCommand implements Serializable {

    private String command;

    private String uid;

    private String argsBody;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArgsBody() {
        return argsBody;
    }

    public void setArgsBody(String argsBody) {
        this.argsBody = argsBody;
    }
}
