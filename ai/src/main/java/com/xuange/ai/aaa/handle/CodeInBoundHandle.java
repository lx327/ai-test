package com.xuange.ai.aaa.handle;


import com.xuange.ai.aaa.codec.RSAEncryption;
import com.xuange.ai.aaa.codec.codeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
//xuange
public class CodeInBoundHandle extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf =(ByteBuf) msg;

        try {
            codeFactory codeFactory = new codeFactory();
            CharSequence sequence = byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.UTF_8);
            RSAEncryption rsaEncryption = codeFactory.getRSAEncryption();
            String decrypt = rsaEncryption.decrypt(sequence.toString());
            System.out.println(decrypt);
            byteBuf = Unpooled.copiedBuffer(decrypt, StandardCharsets.UTF_8);
            ctx.writeAndFlush(byteBuf);
        } catch (Exception e){
            ctx.close();
        } finally {

            byteBuf.release();
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
