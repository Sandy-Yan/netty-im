package com.github.andy.im.command.cmd;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.github.andy.im.command.model.MetaCode;
import com.github.andy.im.command.util.ObjectMapperUtils;
import com.github.andy.im.command.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:28
 */
public abstract class AbstractCmd<P extends CmdParam> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private volatile Class<?> cmdParamClass;

    public Class<?> getCmdParamClass() {
        if (cmdParamClass == null) {
            cmdParamClass = ReflectUtil.find(this, AbstractCmd.class, "P");
        }
        return cmdParamClass;
    }

    public Object handle(String cmdParamJson, BroadcastOperations broadcastOperations, SocketIOClient client) {

        if (logger.isDebugEnabled()) {
            logger.debug("[netty-im] cmd:{}, cmdParamClass:{}, cmdParamJson:{}", getClass(), getCmdParamClass(), cmdParamJson);
        }

        P cmdParam = null;
        try {
            Object cmdParamObj = ObjectMapperUtils.fromJSON(cmdParamJson, getCmdParamClass());
            if (logger.isDebugEnabled()) {
                logger.debug("[netty-im] cmdParamObj class:{}", cmdParamObj.getClass());
            }
            cmdParam = (P) cmdParamObj;
        } catch (Throwable e) {
            logger.warn("[netty-im] cmd:{}, param error, cmdParamJson:{}", getClass(), cmdParamJson);
            return MetaCode.ParamError.toJsonView();
        }

        try {
            return exec(cmdParam, broadcastOperations, client);
        } catch (Throwable e) {
            logger.error("[netty-im]　class:" + getClass() + " exec error.", e);
            return MetaCode.ServerError.toJsonView();
        }
    }

    /**
     * 命令执行
     *
     * @param cmdParam
     * @return
     */
    public abstract Object exec(P cmdParam, BroadcastOperations broadcastOperations, SocketIOClient client);

}
