package com.github.andy.im.command.server.cmd;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.github.andy.im.command.annotations.RequestCmd;
import com.github.andy.im.command.cmd.AbstractCmd;
import com.github.andy.im.command.model.JsonResultView;
import com.github.andy.im.command.server.model.TestCmdParam;
import org.springframework.stereotype.Component;

/**
 * Created by yan.s.g on 18/2/11.
 */
@RequestCmd("testCmd")
@Component
public class TestCmd extends AbstractCmd<TestCmdParam> {

    @Override
    public Object exec(TestCmdParam cmdParam, BroadcastOperations broadcastOperations, SocketIOClient client) {
        System.out.println("TestCmd param:" + cmdParam);
        return JsonResultView.SUCCESS;
    }
}
