package com.xuange.ai.aaa.webSocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
//xuange
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 从请求头中获取 Authorization
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
            attributes.put("token", token); // 将 token 存入 attributes，后续可以在 WebSocket session 中使用
        } else {
            System.out.println("Authorization header is missing or invalid");
            return false; // 如果没有 token 或者格式不对，拒绝握手
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        //TODO 握手后的处理（可选）
    }
}
