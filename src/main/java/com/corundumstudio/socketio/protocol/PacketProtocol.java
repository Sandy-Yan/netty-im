package com.corundumstudio.socketio.protocol;

import java.io.Serializable;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 11:50
 */
public class PacketProtocol implements Serializable {

    private static final long serialVersionUID = 4560159536486711429L;

    private PacketType type;

    private PacketType subType;

    private String nsp;

    private String command;

    private Object data;

    public PacketType getType() {
        return type;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public PacketType getSubType() {
        return subType;
    }

    public void setSubType(PacketType subType) {
        this.subType = subType;
    }

    public String getNsp() {
        return nsp;
    }

    public void setNsp(String nsp) {
        this.nsp = nsp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PacketProtocol{" +
                "type=" + type +
                ", subType=" + subType +
                ", nsp='" + nsp + '\'' +
                ", command='" + command + '\'' +
                ", data=" + data +
                '}';
    }
}
