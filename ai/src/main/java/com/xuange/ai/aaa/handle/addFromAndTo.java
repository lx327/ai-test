package com.xuange.ai.aaa.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
//xuange
public class addFromAndTo extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("addddddddddadadadadadad");
        super.channelRead(ctx, msg);
    }
}
