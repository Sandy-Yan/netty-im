package com.github.andy.im.command.client;

import com.github.andy.im.command.model.RawCommand;
import com.corundumstudio.socketio.protocol.AuthPacket;
import com.corundumstudio.socketio.protocol.PacketProtocol;
import com.corundumstudio.socketio.protocol.PacketType;
import com.github.andy.im.command.util.ObjectMapperUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Collections;

/**
 * Created by yan.s.g on 18/2/11.
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    private WebSocketClient webSocketClient;

    private String sessionId;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker, WebSocketClient webSocketClient) {
        this.handshaker = handshaker;
        this.webSocketClient = webSocketClient;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("[WebSocket-Client] handlerAdded");
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("[WebSocket-Client] channelActive handshaking");
        Channel ch = ctx.channel();
        ChannelPromise channelPromise = ch.newPromise();
        //handshaker.handshake(ch, channelPromise);
        handshaker.handshake(ch);
        System.out.println("[WebSocket-Client] channelActive cause:" + channelPromise.cause());
        System.out.println("[WebSocket-Client] channelActive isHandshakeComplete:" + handshaker.isHandshakeComplete());
        System.out.println("[WebSocket-Client] channelActive handshake completed.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.close();
        System.out.println("[WebSocket-Client] channelInactive disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[WebSocket-Client] channelRead0 msg class:" + msg.getClass().getSimpleName());
        System.out.println("[WebSocket-Client] channelRead0 msg:" + msg);
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            if (msg instanceof FullHttpResponse) {
                try {
                    handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                    System.out.println("[WebSocket-Client] channelRead0 handshaker.finishHandshake(*)");
                    System.out.println("[WebSocket-Client] channelRead0 client connected!");
                    handshakeFuture.setSuccess();
                    System.out.println("[WebSocket-Client] channelRead0 handshakeFuture setSuccess");
                } catch (WebSocketHandshakeException e) {
                    System.out.println("[WebSocket-Client] Client failed to connect");
                    handshakeFuture.setFailure(e);
                }
            } else {
                handleMsg(ch, msg);
            }

            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        handleMsg(ch, msg);
    }

    private void handleMsg(Channel ch, Object msg) {
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String text = textFrame.text();
            System.out.println("[WebSocket-Client] received message: " + text);

            try {
                PacketProtocol packetProtocol = ObjectMapperUtils.fromJSON(text, PacketProtocol.class);
                if (packetProtocol != null) {
                    if (packetProtocol.getType() == PacketType.OPEN && packetProtocol.getData() != null) {
                        String authStr = ObjectMapperUtils.toJSON(packetProtocol.getData());
                        HandShakeModel handShakeModel = ObjectMapperUtils.fromJSON(authStr, HandShakeModel.class);
                        sessionId = handShakeModel.getSid();
                        handshaker = webSocketClient.newClientHandshaker(Collections.singletonMap("sid", sessionId));

                        System.out.println("[WebSocket-Client] sessionId:" + sessionId);
                    } else {
                        // TODO
                        System.out.println("[WebSocket-Client] server result:" + packetProtocol);
                    }
                } else {
                    System.out.println("[WebSocket-Client] server response null !!!!!");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
            System.out.println("[WebSocket-Client] received binary");
        } else if (frame instanceof PongWebSocketFrame) {
            PongWebSocketFrame pongFrame = (PongWebSocketFrame) frame;
            System.out.println("[WebSocket-Client] received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            CloseWebSocketFrame closeFrame = (CloseWebSocketFrame) frame;
            System.out.println("[WebSocket-Client] received closing");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

}
