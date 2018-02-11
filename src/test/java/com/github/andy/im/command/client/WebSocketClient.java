package com.github.andy.im.command.client;

import com.github.andy.im.command.model.RawCommand;
import com.corundumstudio.socketio.protocol.PacketProtocol;
import com.corundumstudio.socketio.protocol.PacketType;
import com.github.andy.im.command.util.ObjectMapperUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yan.s.g on 18/2/11.
 */
public class WebSocketClient {

    private static final int MAX_CONTENT_LENGTH = 8192;

    private String host;

    private int port;

    private URI uri;

    private EventLoopGroup group;

    private Channel ch;

    private DefaultHttpHeaders httpHeaders;

    private WebSocketClientHandler handler;

    private AtomicBoolean isConnected = new AtomicBoolean(false);

    public WebSocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        if (isConnected.get()) {
            return;
        }
        try {
            // configure the client.
            group = new NioEventLoopGroup();
            handler = new WebSocketClientHandler(newClientHandshaker(null), this);

            Bootstrap b = new Bootstrap();
            b.group(group)//
                    .channel(NioSocketChannel.class)//
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(
                                    new HttpClientCodec(),//
                                    new HttpObjectAggregator(MAX_CONTENT_LENGTH),//
                                    WebSocketClientCompressionHandler.INSTANCE,//
                                    handler);
                        }
                    });

            // start the client.
            ChannelFuture f = b.connect(uri.getHost(), uri.getPort()).sync();
            ch = f.channel();
            handler.handshakeFuture().sync();

            isConnected.set(true);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            if (isConnected.get()) {
                ch.writeAndFlush(new CloseWebSocketFrame());
                ch.closeFuture().sync();
                ch = null;

                group.shutdownGracefully();
                group = null;

                handler = null;

                isConnected.set(false);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void ping() {
        WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
        ch.writeAndFlush(frame);
    }

    public boolean sendMsg(String msg) {
        try {
            PacketProtocol packet = new PacketProtocol();
            packet.setType(PacketType.MESSAGE);
            packet.setSubType(PacketType.EVENT);
            packet.setNsp("");
            RawCommand rawCommand = new RawCommand();
            rawCommand.setCommand("testCmd");
            rawCommand.setArgsBody("{\"key\":\"aaabababa\"}");
            packet.setData(rawCommand);

            WebSocketFrame frame = new TextWebSocketFrame(ObjectMapperUtils.toJSON(packet));
            ch.writeAndFlush(frame).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        System.out.println("sendMsg succss");

                    } else {
                        System.out.println("sendMsg failed");
                        System.out.println(future.cause());
                    }
                }
            });
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public WebSocketClientHandshaker newClientHandshaker(Map<String, String> params) {
        try {
            URI newUri = new URI("ws://" + host + ":" + port + "/?transport=websocket" /*+ buildQueryString(params)*/);
            DefaultHttpHeaders newHttpHeaders = new DefaultHttpHeaders();
            //newHttpHeaders.add("transport", "websocket");
            //httpHeaders.add("client", clientHead);
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(newUri, WebSocketVersion.V13, null, true, newHttpHeaders);
            uri = newUri;
            httpHeaders = newHttpHeaders;
            return handshaker;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("");
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            /*if (isFirst) {
                isFirst = false;
            } else {*/
            sb.append("&");
            //}
            sb.append(entry.getKey());
            sb.append("&");
            sb.append(entry.getValue());
        }

        return sb.toString();
    }


    public static void main(String args[]) throws Exception {
        String HOST = "127.0.0.1";
        int PORT = 9092;

        WebSocketClient webSocketClient = new WebSocketClient(HOST, PORT);
        webSocketClient.start();

        TimeUnit.SECONDS.sleep(2);

        // sendMsg to server
        for (int i = 0; i < 3; i++) {
            webSocketClient.sendMsg("[client-A] hello server " + i);
            System.out.println("#####");
        }

        //System.exit(0);

        /*while(true){
            TimeUnit.SECONDS.sleep(30);
            webSocketClient.sendMsg("hello");
            System.out.println("say hello");
        }*/

    }

}
