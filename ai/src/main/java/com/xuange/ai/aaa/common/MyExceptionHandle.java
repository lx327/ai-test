package com.xuange.ai.aaa.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//xuange
public class MyExceptionHandle extends Exception {
    protected MyExceptionHandle(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        ByteBuffer messaege = ByteBuffer.wrap(message.getBytes());
        ByteBuffer caus = ByteBuffer.wrap(cause.toString().getBytes());
        try {
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("src/main/resources/ExceptionLogin.log"), StandardOpenOption.WRITE);
            channel.write(messaege,0);
            String cas="cause:";
            int x=cas.getBytes().length;
            channel.write(caus,message.getBytes().length+x);
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}


//public class AsyncFileReadExample {
//    public static void main(String[] args) {
//        try {
//            // 创建异步文件通道
//            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
//                    Paths.get("example.txt"), StandardOpenOption.READ);
//
//            // 创建ByteBuffer
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//
//            // 异步读取文件
//            Future<Integer> result = fileChannel.read(buffer, 0);
//
//            // 可以在这里执行其他操作
//            System.out.println("正在读取文件...");
//
//            // 等待读取完成
//            while (!result.isDone()) {
//                // 这里可以执行其他任务
//            }
//
//            // 获取读取的字节数
//            int bytesRead = result.get();
//            System.out.println("读取的字节数: " + bytesRead);
//
//            // 处理读取的内容
//            buffer.flip(); // 切换到读取模式
//            byte[] data = new byte[bytesRead];
//            buffer.get(data);
//            System.out.println("读取的内容: " + new String(data));
//
//            // 关闭通道
//            fileChannel.close();
//
//        } catch (IOException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//}