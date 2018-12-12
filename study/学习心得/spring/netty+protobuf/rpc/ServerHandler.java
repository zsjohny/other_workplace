package com.newman.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<TestProtoc.TestProto> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TestProtoc.TestProto testProto) throws Exception {
        System.out.println("server read " + testProto.getNameList());
        TestProtoc.TestProto.Builder builder = TestProtoc.TestProto.newBuilder();
        builder.addName("hello");
        channelHandlerContext.writeAndFlush(builder.build());
    }
}
