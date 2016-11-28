package com.corundumstudio.socketio.command.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.github.andy.common.properties.PropertiesManager;

/**
 * @Author yan.s.g
 * @Date 2016年09月19日 15:36
 */
public class ConfigUtil {

    private static final String IO_PROPERTIES_FILE = "socket-io.properties";

    public static Configuration get() {
        Configuration config = new Configuration();
        config.setHostname(getHost());
        config.setPort(getPort());
        config.setOrigin(getOrigin());
        config.setBossThreads(getBossThreads());
        config.setWorkerThreads(getWorkerThreads());
        config.setUseLinuxNativeEpoll(isUseLinuxNativeEpoll());
        config.setAllowCustomRequests(isAllowCustomRequests());
        config.setContext(getContext());
        //config.setJsonSupport();
        //config.setTransports(Transport.WEBSOCKET);
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpKeepAlive(isSocketTcpKeepAlive());
        socketConfig.setTcpNoDelay(isSocketTcpNoDelay());
        socketConfig.setTcpSendBufferSize(getSocketTcpSendBufferSize());
        config.setSocketConfig(socketConfig);

        return config;
    }

    public static String getHost() {
        return PropertiesManager.getString(IO_PROPERTIES_FILE, "host", "localhost");
    }

    public static int getPort() {
        return PropertiesManager.getInt(IO_PROPERTIES_FILE, "port", 9092);
    }

    public static String getOrigin() {
        return PropertiesManager.getString(IO_PROPERTIES_FILE, "origin", "test");
    }

    public static int getBossThreads() {
        return PropertiesManager.getInt(IO_PROPERTIES_FILE, "bossThreads", 5);
    }

    public static int getWorkerThreads() {
        return PropertiesManager.getInt(IO_PROPERTIES_FILE, "workerThreads", 5);
    }

    public static boolean isUseLinuxNativeEpoll() {
        return PropertiesManager.getBoolean(IO_PROPERTIES_FILE, "useLinuxNativeEpoll", false);
    }

    public static boolean isAllowCustomRequests() {
        return PropertiesManager.getBoolean(IO_PROPERTIES_FILE, "allowCustomRequests", false);
    }

    public static String getContext() {
        return PropertiesManager.getString(IO_PROPERTIES_FILE, "context", "");
    }

    public static boolean isSocketTcpKeepAlive() {
        return PropertiesManager.getBoolean(IO_PROPERTIES_FILE, "socket.tcpKeepAlive", true);
    }

    public static boolean isSocketTcpNoDelay() {
        return PropertiesManager.getBoolean(IO_PROPERTIES_FILE, "socket.tcpNoDelay", true);
    }

    public static int getSocketTcpSendBufferSize() {
        return PropertiesManager.getInt(IO_PROPERTIES_FILE, "socket.tcpSendBufferSize", 1024);
    }


}
