package com.corundumstudio.socketio.command.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.command.cmd.AbsCmd;
import com.corundumstudio.socketio.command.constant.EventConstants;
import com.corundumstudio.socketio.command.model.MetaCode;
import com.corundumstudio.socketio.command.model.RawCommand;
import com.corundumstudio.socketio.command.store.CommandStoreHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 17:09
 */
public class CommandHandler {

    private static Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    public static final void handle(BroadcastOperations broadcastOperations, SocketIOClient client, RawCommand data, AckRequest ackSender) {

        if (logger.isDebugEnabled()) {
            logger.debug("## data:{}", data);
        }

        AbsCmd cmd = CommandStoreHub.get(data.getCommand());
        if (cmd == null) {
            client.sendEvent(EventConstants.EVENT_KEY, MetaCode.NoExistCmd.toJsonView());
            return;
        }

        Object result = cmd.handle(data.getArgsBody(), broadcastOperations, client);
        client.sendEvent(EventConstants.EVENT_KEY, result);
        return;
    }
}
