package com.xuange.ai.aaa.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//xuange
public class FileHandle extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String m =(String) msg;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/home/xuange/提取/user file of message/char_example.txt"))) {
            writer.write(m); // 写入内容
            writer.newLine(); // 添加换行
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.write(ctx, msg, promise);
    }
}
