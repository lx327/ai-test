package com.xuange.ai.aaa.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
//xuange
public class rabbitMq {
    public static final String EXCHANGE = "EXCHANGEOFXUANGE";
    public static final String ROUTING = "ROUTINGOFXUANGE";
    public static final String QUEUE = "QUEUEOFXUANGE";

    public static void publish(String message) {


        try (Connection connection = getConnect();
             Channel channel = connection.createChannel()) {
            // 声明一个队列
            channel.queueDeclare(QUEUE, false, false, false, null);

            channel.basicPublish(EXCHANGE, ROUTING, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (TimeoutException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receive() {

        try (Connection connection = getConnect();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE, false, false, false, null);

//         System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            };
             channel.basicConsume(QUEUE, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnect() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // 设置 RabbitMQ 服务器地址
        factory.setUsername("user");
        factory.setPassword("123");
        try {
            return factory.newConnection();

        } catch (IOException | TimeoutException e) {
            return null;
        }
    }

}
