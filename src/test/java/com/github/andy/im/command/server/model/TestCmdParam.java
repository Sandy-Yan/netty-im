package com.github.andy.im.command.server.model;

import com.github.andy.im.command.cmd.CmdParam;

/**
 * Created by yan.s.g on 18/2/11.
 */
public class TestCmdParam implements CmdParam {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "TestCmdParam{" +
                "key='" + key + '\'' +
                '}';
    }
}
