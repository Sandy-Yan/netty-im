package com.corundumstudio.socketio.command;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.command.config.ConfigUtil;
import com.corundumstudio.socketio.command.constant.EventConstants;
import com.corundumstudio.socketio.command.handler.CommandHandler;
import com.corundumstudio.socketio.command.model.RawCommand;
import com.corundumstudio.socketio.command.spring.CommandInitializer;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 15:11
 */
public class Main {

    public static void main(String args[]) {

        // build config and server
        Configuration config = ConfigUtil.get();
        final SocketIOServer server = new SocketIOServer(config);

        // custom command init
        CommandInitializer.init();

        // server add event listener
        server.addEventListener(EventConstants.EVENT_KEY, RawCommand.class, new DataListener<RawCommand>() {
            @Override
            public void onData(SocketIOClient client, RawCommand data, AckRequest ackSender) throws Exception {
                CommandHandler.handle(server.getBroadcastOperations(), client, data, ackSender);
            }
        });

        // server startup
        server.start();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("server shutdowning");
                server.stop();
                System.out.println("server shutdown success");
            }
        });

    }
}
