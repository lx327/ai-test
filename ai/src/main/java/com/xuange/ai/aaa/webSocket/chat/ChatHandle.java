package com.xuange.ai.aaa.webSocket.chat;

import com.xuange.ai.aaa.common.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//xuange
@Component
public class ChatHandle extends TextWebSocketHandler {

    private static final Map<String, ChatHandle> onlineUsers = new ConcurrentHashMap<>();
    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        String fromPhone = this.getToken(session);
        // 在连接建立后，可以将用户加入到在线用户列表中
        onlineUsers.put(fromPhone, this);
        String s="";

        TextMessage textMessage = new TextMessage(s.getBytes());
        session.sendMessage(textMessage);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        super.handleMessage(session, message);
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        super.handleBinaryMessage(session, message);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        super.handleTextMessage(session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    private String getToken(WebSocketSession session) {
        // 从 WebSocketSession 的 attributes 中获取 token
        String token = (String) session.getAttributes().get("token");
        if (token != null) {
            // 从 token 中解析用户 ID
            String fromPhone = JwtHelper.getUserId(token);
            return fromPhone;
        }
        return null;
    }
}



