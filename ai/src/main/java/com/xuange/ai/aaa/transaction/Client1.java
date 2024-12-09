package com.xuange.ai.aaa.transaction;


import com.xuange.ai.aaa.common.FromMessage;
import com.xuange.ai.aaa.common.FromMessageTo;
import com.xuange.ai.aaa.common.Tread;
import com.xuange.ai.aaa.handle.FilterOutBoundHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.xuange.ai.AiApplication.friendList;
import static com.xuange.ai.AiApplication.serverChannelList;
import static com.xuange.ai.aaa.common.FromMessageTo.fromString;
import static com.xuange.ai.aaa.controller.PhoneLogin.phoneMap;

@Component
//xuange
public class Client1 {
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    public    List<FromMessageTo> clientmessageList=new ArrayList<>();
    public    List<FromMessage> clientmessageListOfIn=new ArrayList<>();

     ThreadPoolExecutor client_threadPool = Tread.getTreadPool(10, 20, 1, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    static ThreadPoolExecutor allcanuse = Tread.getTreadPool(10, 20, 1, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
//     AtomicInteger client_index = new AtomicInteger(0);


    public void startClient(int port, String ipadd, String phone) {

        EventLoopGroup group = new NioEventLoopGroup(); // 创建事件循环组
        try {
            ChannelFuture future = new Bootstrap()
                    // 设置 boss 和 worker 事件循环组
                    .group(group)
                    // 指定使用的通道类型（NIO 非阻塞 IO）
                    .channel(NioSocketChannel.class)
                    // 初始化每个连接的通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //出栈处理器，先处理最后一个入时 1,2，3，出时3,2,1 数据从3 到1流动
                            socketChannel.pipeline().addLast(new StringEncoder()); // 添加编码器
                            socketChannel.pipeline().addLast(new FilterOutBoundHandle());









                            //入栈处理器，先处理最后一个入时 1,2，3，出时3,2,1 数据从1 到3流动
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                static long keyId=3;
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                    FromMessageTo fromMessageTo = fromString(s);
                                    String from = fromMessageTo.getFrom();
                                    String msg = fromMessageTo.getMsg();
                                    FromMessage fromMessage = new FromMessage(keyId,from, msg);
                                    clientmessageListOfIn.add(fromMessage);
                                    keyId++;

                                }
                            });

                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 连接到服务器并添加监听器
                    .connect(ipadd, port)

                    .addListener((ChannelFutureListener) channelFuture -> {
                        if (channelFuture.isSuccess()) {

                            Channel channel = channelFuture.channel();
                            lunxun(channel);
                            // 发送消息

//                            channel.writeAndFlush(fromMessageTo.toString());
                            //                             监听通道关闭
                            channel.closeFuture().addListener((ChannelFutureListener) future1 -> {
                                System.out.println("通道已关闭");
//                                client_threadPool.
                                phoneMap.remove(phone);
                                serverChannelList.remove(phone);
                                group.shutdownGracefully(); // 优雅关闭事件循环组
                            });
                        } else {

                            group.shutdownGracefully(); // 连接失败时也关闭事件循环组
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            group.shutdownGracefully(); // 确保在异常时也关闭事件循环组
        }
    }
    public void lunxun(Channel channel){
        CompletableFuture.runAsync(() -> {
            while (true){
                if(clientmessageList.isEmpty()){
                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                for (int j = 0; j < clientmessageList.size(); j++) {
                    String MessageString = clientmessageList.get(j).toString();
                    FromMessageTo fromMessageTo = fromString(MessageString);
                    String from = fromMessageTo.getFrom();
                    String toPhone = fromMessageTo.getTo();

                    if(friendList.get(from).contains(toPhone)){
                        channel.writeAndFlush(MessageString);
                    }

                    if(j==clientmessageList.size()-1){
                        clientmessageList=new ArrayList<>();
                    }
                }
            }
        }, client_threadPool);
    }


    public Boolean removeByKeyId(Long keyId) {
        Iterator<FromMessage> iterator = clientmessageListOfIn.iterator();
        if(iterator.hasNext()){
            FromMessage next = iterator.next();
            if(next.getKeyid()==keyId){
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
