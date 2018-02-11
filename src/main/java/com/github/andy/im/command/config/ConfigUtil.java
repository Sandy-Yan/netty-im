package com.github.andy.im.command.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.Transport;
import com.github.andy.im.command.model.ImConfig;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 15:36
 */
public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    private static final String CONFIG_FILE = "netty-im.properties";

    private static final ImConfig IM_CONFIG = new ImConfig();

    static {
        Properties properties = new Properties();
        InputStream configIS = null;
        try {
            configIS = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (configIS != null) {
                properties.load(configIS);
            } else {
                LOGGER.info("[netty-im] netty-im.properties not found. will use default value.");
            }
        } catch (Exception e) {
            LOGGER.error("[netty-im] load config file [netty-im.properties] error.", e);
            throw new RuntimeException("load config file netty-im.properties error.", e);
        } finally {
            if (configIS != null) {
                try {
                    configIS.close();
                } catch (IOException e) {
                    LOGGER.error("[netty-im] load config file close io error.", e);
                }
            }
        }

        try {
            String property;

            property = properties.getProperty("host");
            IM_CONFIG.setHost(StringUtil.isNullOrEmpty(property) ? "127.0.0.1" : property);

            property = properties.getProperty("port");
            IM_CONFIG.setPort(StringUtil.isNullOrEmpty(property) ? 9092 : Integer.valueOf(property));

            property = properties.getProperty("origin");
            IM_CONFIG.setOrigin(StringUtil.isNullOrEmpty(property) ? "" : property);

            property = properties.getProperty("bossThreads");
            IM_CONFIG.setBossThreads(StringUtil.isNullOrEmpty(property) ? 5 : Integer.valueOf(property));

            property = properties.getProperty("workerThreads");
            IM_CONFIG.setWorkerThreads(StringUtil.isNullOrEmpty(property) ? 10 : Integer.valueOf(property));

            property = properties.getProperty("useLinuxNativeEpoll");
            IM_CONFIG.setUseLinuxNativeEpoll(StringUtil.isNullOrEmpty(property) ? false : Boolean.valueOf(property));

            property = properties.getProperty("allowCustomRequests");
            IM_CONFIG.setAllowCustomRequests(StringUtil.isNullOrEmpty(property) ? false : Boolean.valueOf(property));

            property = properties.getProperty("context");
            IM_CONFIG.setContext(StringUtil.isNullOrEmpty(property) ? "" : property);

            property = properties.getProperty("socket.tcpKeepAlive");
            IM_CONFIG.setSocketTcpKeepAlive(StringUtil.isNullOrEmpty(property) ? true : Boolean.valueOf(property));

            property = properties.getProperty("socket.tcpNoDelay");
            IM_CONFIG.setSocketTcpNoDelay(StringUtil.isNullOrEmpty(property) ? true : Boolean.valueOf(property));

            property = properties.getProperty("socket.tcpSendBufferSize");
            IM_CONFIG.setSocketTcpSendBufferSize(StringUtil.isNullOrEmpty(property) ? 1024 : Integer.valueOf(property));

        } catch (Exception e) {
            LOGGER.error("[netty-im] load im config params error.", e);
            throw new RuntimeException("[netty-im] load im config params error.", e);
        }
    }

    public static Configuration buildConfiguration() {
        Configuration config = new Configuration();
        config.setHostname(IM_CONFIG.getHost());
        config.setPort(IM_CONFIG.getPort());
        config.setOrigin(IM_CONFIG.getOrigin());
        config.setBossThreads(IM_CONFIG.getBossThreads());
        config.setWorkerThreads(IM_CONFIG.getWorkerThreads());
        config.setUseLinuxNativeEpoll(IM_CONFIG.isUseLinuxNativeEpoll());
        config.setAllowCustomRequests(IM_CONFIG.isAllowCustomRequests());
        config.setContext(IM_CONFIG.getContext());
        //config.setJsonSupport();
        config.setTransports(Transport.WEBSOCKET, Transport.POLLING);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpKeepAlive(IM_CONFIG.isSocketTcpKeepAlive());
        socketConfig.setTcpNoDelay(IM_CONFIG.isSocketTcpNoDelay());
        socketConfig.setTcpSendBufferSize(IM_CONFIG.getSocketTcpSendBufferSize());

        config.setSocketConfig(socketConfig);

        return config;
    }

    public static ImConfig getImConfig() {
        return IM_CONFIG;
    }

}
