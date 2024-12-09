package com.xuange.ai.aaa.webSocket.publishAandBuy;

//public class webSocketConfig implements WebSocketConfigurer {
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new ChatEndpoint(),"/chat")
//                .addInterceptors(new AuthHandshakeInterceptor()) // 注册拦截器
//                .setAllowedOrigins("*");// 根据需求设置允许的来源
//    }
//}

//import com.xuange.ai.aaa.webSocket.AuthHandshakeInterceptor;
//import com.xuange.ai.aaa.webSocket.chat.ChatHandle;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebMvc
//@EnableWebSocket
//public class WebSocketConfig  implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        String[] allow ={"http://www.xxx.com"};
//     registry.addHandler(new ChatHandle(),"/ws").setAllowedOrigins(allow)
//             .withSockJS()
//             .setInterceptors(new AuthHandshakeInterceptor());
//    }
//
//}

import com.xuange.ai.aaa.webSocket.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//xuange
@Configuration
@EnableWebSocketMessageBroker
public class PublishAndBuyWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allow ={"http://www.xxx.com"};
        registry.addEndpoint("/ws")
                .addInterceptors(new AuthHandshakeInterceptor())
                .setAllowedOrigins(allow).withSockJS()
               ;
    }


}
