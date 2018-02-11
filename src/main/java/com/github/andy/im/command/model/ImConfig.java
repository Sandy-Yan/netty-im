package com.github.andy.im.command.model;

/**
 * Created by yan.s.g on 18/2/11.
 */
public class ImConfig {

    private String host;

    private int port;

    private String origin;

    private int bossThreads;

    private int workerThreads;

    private boolean useLinuxNativeEpoll;

    private boolean allowCustomRequests;

    private String context;

    private boolean socketTcpKeepAlive;

    private boolean socketTcpNoDelay;

    private int socketTcpSendBufferSize;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public boolean isUseLinuxNativeEpoll() {
        return useLinuxNativeEpoll;
    }

    public void setUseLinuxNativeEpoll(boolean useLinuxNativeEpoll) {
        this.useLinuxNativeEpoll = useLinuxNativeEpoll;
    }

    public boolean isAllowCustomRequests() {
        return allowCustomRequests;
    }

    public void setAllowCustomRequests(boolean allowCustomRequests) {
        this.allowCustomRequests = allowCustomRequests;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isSocketTcpKeepAlive() {
        return socketTcpKeepAlive;
    }

    public void setSocketTcpKeepAlive(boolean socketTcpKeepAlive) {
        this.socketTcpKeepAlive = socketTcpKeepAlive;
    }

    public boolean isSocketTcpNoDelay() {
        return socketTcpNoDelay;
    }

    public void setSocketTcpNoDelay(boolean socketTcpNoDelay) {
        this.socketTcpNoDelay = socketTcpNoDelay;
    }

    public int getSocketTcpSendBufferSize() {
        return socketTcpSendBufferSize;
    }

    public void setSocketTcpSendBufferSize(int socketTcpSendBufferSize) {
        this.socketTcpSendBufferSize = socketTcpSendBufferSize;
    }
}
