package com.github.andy.im.command;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.andy.im.command.config.ConfigUtil;
import com.github.andy.im.command.constant.EventConstants;
import com.github.andy.im.command.handler.CommandHandler;
import com.github.andy.im.command.model.RawCommand;
import com.github.andy.im.command.spring.CommandInitializer;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 15:11
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) {

        // build configuration
        final Configuration configuration = ConfigUtil.buildConfiguration();

        // build server
        final SocketIOServer server = new SocketIOServer(configuration);

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
                LOGGER.info("[netty-im] server shutdowning.");
                server.stop();
                LOGGER.info("[netty-im] server shutdown success.");
            }
        });

    }
}
