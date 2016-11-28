package com.corundumstudio.socketio.command.spring;

import com.corundumstudio.socketio.command.annotations.RequestCmd;
import com.corundumstudio.socketio.command.cmd.AbsCmd;
import com.corundumstudio.socketio.command.store.CommandStoreHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:01
 */
public class CommandInitializer {

    private static Logger logger = LoggerFactory.getLogger(CommandInitializer.class);

    private static volatile boolean isInit = false;

    public static final void init() {
        if (isInit == true) {
            return;
        }
        synchronized (CommandInitializer.class) {
            if (isInit == true) {
                return;
            }
            logger.info("init spring");
            BeanFactory.init();
            logger.info("init spring finish.");
            logger.info("build RequestCmd instances...");
            Map<String, Object> reqCmdInstances = BeanFactory.getBeansWithAnnotation(RequestCmd.class);
            if (!CollectionUtils.isEmpty(reqCmdInstances)) {
                for (Map.Entry<String, Object> entry : reqCmdInstances.entrySet()) {
                    Object cmdInstance = entry.getValue();
                    if (cmdInstance == null || !(cmdInstance instanceof AbsCmd)) {
                        throw new RuntimeException("CommandInitializer init failed. cause instance is not AbsCmd subClass");
                    }

                    RequestCmd reqCmdAnnotation = cmdInstance.getClass().getAnnotation(RequestCmd.class);
                    String[] commands = reqCmdAnnotation.value();
                    AbsCmd cmd = (AbsCmd) cmdInstance;
                    for (String command : commands) {
                        CommandStoreHub.put(command, cmd);
                    }
                }
            }
            isInit = true;
            logger.info("build RequestCmd finish.");
        }
    }

}
