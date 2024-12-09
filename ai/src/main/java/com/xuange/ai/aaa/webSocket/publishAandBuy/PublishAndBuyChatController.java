package com.xuange.ai.aaa.webSocket.publishAandBuy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
//xuange
@Controller
public class PublishAndBuyChatController {

    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public PublishAndBuyChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send") // 接收来自客户端的消息
    @SendTo("/topic/messages") // 发送到该主题
    public void sendMessage(String message) {
        // 发送消息到指定的主题
        messagingTemplate.convertAndSend("/topic/messages", message);
    }


    // 接收来自客户端的消息做处理
    public String handleMessage(String message) {
        // TODO 处理接收到的消息




        // 返回处理后的消息（这也会发送到客户端）
        return message;
    }

    public void buy(String message){

    }
}

