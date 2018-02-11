package com.github.andy.im.command.store;

import com.github.andy.im.command.cmd.AbstractCmd;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:02
 */
public class CommandStoreHub {

    private CommandStoreHub() {
    }

    private static final ConcurrentHashMap<String, AbstractCmd> store = new ConcurrentHashMap<String, AbstractCmd>();

    public static AbstractCmd get(String command) {
        return store.get(command);
    }

    public static void put(String command, AbstractCmd iCommand) {
        if (store.containsKey(command)) {
            throw new RuntimeException("duplicate command,cmd:" + command + ",class:" + iCommand.getClass());
        }
        store.putIfAbsent(command, iCommand);
    }
}
