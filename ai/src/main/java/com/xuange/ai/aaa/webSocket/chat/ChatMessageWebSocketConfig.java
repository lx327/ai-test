package com.xuange.ai.aaa.webSocket.chat;

import com.xuange.ai.aaa.webSocket.AuthHandshakeInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//xuange
public class ChatMessageWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String[] allow ={"http://www.xxx.com"};
     registry.addHandler(new ChatHandle(),"/ws").setAllowedOrigins(allow)
             .withSockJS()
             .setInterceptors(new AuthHandshakeInterceptor());
    }

}
