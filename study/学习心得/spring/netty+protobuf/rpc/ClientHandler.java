package com.newman.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<TestProtoc.TestProto> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TestProtoc.TestProto testProto) throws Exception {
        System.out.println("client: " + testProto.getNameList());
    }
}
