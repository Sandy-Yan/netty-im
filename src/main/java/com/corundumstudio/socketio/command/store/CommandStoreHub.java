package com.corundumstudio.socketio.command.store;

import com.corundumstudio.socketio.command.cmd.AbsCmd;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:02
 */
public class CommandStoreHub {

    private CommandStoreHub() {
    }

    private static final ConcurrentHashMap<String, AbsCmd> store = new ConcurrentHashMap<String, AbsCmd>();

    public static AbsCmd get(String command) {
        return store.get(command);
    }

    public static void put(String command, AbsCmd iCommand) {
        if (store.containsKey(command)) {
            throw new RuntimeException("duplicate command,cmd:" + command + ",class:" + iCommand.getClass());
        }
        store.putIfAbsent(command, iCommand);
    }
}
