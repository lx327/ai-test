package com.xuange.ai.aaa.handle;


import com.xuange.ai.aaa.codec.RSAEncryption;
import com.xuange.ai.aaa.codec.codeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;
//xuange
public class CodeOutBoundHandle extends ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            codeFactory codeFactory = new codeFactory();
            RSAEncryption rsaEncryption = codeFactory.getRSAEncryption();
            CharSequence sequence = byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.UTF_8);
            String decrypt = rsaEncryption.encrypt(sequence.toString());
            byteBuf = Unpooled.copiedBuffer(decrypt, StandardCharsets.UTF_8);
            ctx.write(byteBuf,promise);
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
