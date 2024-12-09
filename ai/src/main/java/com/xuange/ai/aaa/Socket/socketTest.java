package com.xuange.ai.aaa.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class socketTest {
    public static void main(String[] args) {
//        sendMessage("flakjld");
        udpSend("wdqadadada");

    }


        // 接受TCP连接并读取消息
        public static void accept() {
            try {
                // 创建一个ServerSocket，监听端口4399
                ServerSocket serverSocket = new ServerSocket(4399);
                // 等待客户端连接
                Socket accept = serverSocket.accept();
                // 获取输入流以读取客户端发送的数据
                InputStream inputStream = accept.getInputStream();
                // 使用BufferedReader读取输入流中的数据
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                // 读取一行消息
                String s = bufferedReader.readLine();
                // 打印接收到的消息
                System.out.println(s);
                // 关闭输入流和缓冲读取器
                inputStream.close();
                bufferedReader.close();
                // 关闭ServerSocket
                serverSocket.close();
            } catch (IOException e) {
                // 处理IO异常
                throw new RuntimeException(e);
            }
        }

        // 发送TCP消息到指定的服务器
        public static void sendMessage(String mes) {
            try {
                // 创建一个Socket连接到localhost的4399端口
                Socket socket = new Socket("localhost", 4399);
                // 发送消息
                socket.getOutputStream().write(mes.getBytes(StandardCharsets.UTF_8));
                // 关闭Socket
                socket.close();
            } catch (Exception e) {
                // 处理异常，但不做任何事情
            }
        }

        // 发送UDP消息
        public static void udpSend(String message) {
            try {
                // 创建一个DatagramSocket
                DatagramSocket datagramSocket = new DatagramSocket();
                // 获取本地主机的InetAddress
                InetAddress localhost = InetAddress.getByName("localhost");
                // 创建一个DatagramPacket，指定要发送的消息、长度、目标地址和端口
                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(), localhost, 5000);
                // 发送数据包
                datagramSocket.send(datagramPacket);
                // 关闭DatagramSocket
                datagramSocket.close();
            } catch (IOException e) {
                // 处理IO异常
                throw new RuntimeException(e);
            }
        }

        // 接收UDP消息
        public static void udpReceive() throws RuntimeException {
            try {
                // 创建一个DatagramSocket，监听5000端口
                DatagramSocket datagramSocket = new DatagramSocket(5000);
                // 创建一个字节数组以存储接收到的数据
                byte[] bytes = new byte[128];
                // 创建一个DatagramPacket以接收数据
                DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                // 接收数据包
                datagramSocket.receive(datagramPacket);
                // 将接收到的数据转换为字符串
                String s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                // 关闭DatagramSocket
                datagramSocket.close();
                // 打印接收到的消息
                System.out.println(s);
            } catch (IOException e) {
                // 处理IO异常
                throw new RuntimeException(e);
            }
        }
    }