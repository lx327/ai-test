package com.xuange.ai;

import com.xuange.ai.aaa.common.FromMessageTo;
import com.xuange.ai.aaa.common.Tread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
//xuange
@SpringBootApplication
public class AiApplication {
    public static Map<String,List<String>> friendList=new HashMap<>();
    public static Map<String,List<String>> need=new HashMap<>();
    public static List<String> param1List;
    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
        param1List.addAll(Arrays.asList(args));
        startServer(10088);
    }
    public static HashMap<String, Channel> serverChannelList=new HashMap<>();
    public static List<FromMessageTo> messageList=new ArrayList<>();
    static ThreadPoolExecutor threadPool = Tread.getTreadPool(10, 20, 1, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
    static AtomicInteger index = new AtomicInteger(0);

    public static   void lunxun(){
        int i = index.get(); // Get the current index
        index.compareAndSet(i, i);
        CompletableFuture.runAsync(() -> {
            int w = 0;
            while (true) {
                if (messageList.isEmpty()){
                    try {
                        Thread.sleep(3000);
                        continue;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (w >= messageList.size()) {
                    w=0;
                    continue;
                }
                FromMessageTo message = messageList.get(w); // Get the message at the current index
                boolean exists = serverChannelList.containsKey(message.getTo());

                if (exists) {

                    Channel channel = serverChannelList.get(message.getTo());
                    channel.writeAndFlush(message.toString());
                    messageList.remove(w);


                }
                w++;
                index.incrementAndGet();
            }
        }, threadPool);
    }
    public static void startServer(int port) {


// 创建一个 boss 线程组，用于接受连接
        NioEventLoopGroup boss = new NioEventLoopGroup();
// 创建一个 worker 线程组，用于处理已接受的连接
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    // 设置 boss 和 worker 事件循环组
                    .group(boss, work)

                    // 指定使用的通道类型（NIO 非阻塞 IO）
                    .channel(NioServerSocketChannel.class)
                    // 初始化每个接受的连接的通道
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //出栈处理器，先处理最后一个入时 1,2，3，出时3,2,1 数据从3 到1流动
                            socketChannel.pipeline().addLast(new StringEncoder()); // 添加编码器









                            //入栈处理器，先处理最后一个入时 1,2，3，出时3,2,1 数据从1 到3流动
                            socketChannel.pipeline().addLast(new StringDecoder());
                            // 添加解码器
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
                                    Channel channel = channelHandlerContext.channel();
                                    String byteBuf = (String) s;
                                    FromMessageTo fromMessageTo = FromMessageTo.fromString(byteBuf);
                                    String phoneFrom = fromMessageTo.getFrom();
                                    String msg = fromMessageTo.getMsg();
                                    String phoneTo = fromMessageTo.getTo();

                                    if(phoneTo.equals("server")){
                                        if(!serverChannelList.containsKey(phoneFrom)){
                                            serverChannelList.put(phoneFrom,channel);
                                        }
                                    }
                                    else {
                                        serverChannelList.put(phoneFrom,channel);
                                        messageList.add(fromMessageTo);
//                                        System.out.println(messageList);
//                                        if(serverChannelList.containsKey(phoneTo)){
//                                            serverChannelList.get(phoneTo).writeAndFlush(msg);
//                                        }
                                    }


                                }
                            });
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                    System.out.println(s);
                                    channelHandlerContext.fireChannelRead(s);
                                }
                            });

                        }
                    })
                    .option(ChannelOption.SO_RCVBUF, 1048576)// 设置接收缓冲区大小
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    // 将服务器绑定到指定端口
                    .bind(port).sync();// 添加监听器以处理绑定操作的完成
            lunxun();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
        } finally {
            work.shutdownGracefully(); // 优雅关闭 worker 线程组
            boss.shutdownGracefully(); // 优雅关闭 boss 线程组
        }
    }

}
