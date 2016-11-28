package com.corundumstudio.socketio.command.cmd;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.command.model.MetaCode;
import com.corundumstudio.socketio.command.util.ObjectMapperUtils;
import com.corundumstudio.socketio.command.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 16:28
 */
public abstract class AbsCmd<A extends ICmdArgs> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private volatile Class<?> argsClass;

    public Class<?> getArgsClass() {
        if (argsClass == null) {
            argsClass = ReflectUtil.find(this, AbsCmd.class, "A");
        }

        return argsClass;
    }

    public Object handle(String argsJson, BroadcastOperations broadcastOperations, SocketIOClient client) {

        if (logger.isDebugEnabled()) {
            logger.debug("## cmd:{},argsClass:{},argsJson:{}", getClass(), getArgsClass(), argsJson);
        }

        A args = null;
        try {
            Object argsObj = ObjectMapperUtils.fromJSON(argsJson, getArgsClass());
            logger.info("## argsObj class:{}", argsObj.getClass());
            args = (A) argsObj;
        } catch (Throwable e) {
            logger.warn("## cmd:{}, param error,argsJson:{}", getClass(), argsJson);
            return MetaCode.ParamError.toJsonView();
        }

        try {
            return exec(args, broadcastOperations, client);
        } catch (Throwable e) {
            logger.error("##　class:" + getClass() + " exec error.", e);
            return MetaCode.ServerError.toJsonView();
        }
    }

    /**
     * 命令执行
     *
     * @param args
     * @return
     */
    public abstract Object exec(A args, BroadcastOperations broadcastOperations, SocketIOClient client);

}
