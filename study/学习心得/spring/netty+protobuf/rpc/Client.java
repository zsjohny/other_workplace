package com.newman.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
//                    ch.pipeline().addLast("encoder", new ProtobufEncoder());
//                    ch.pipeline().addLast("decoder", new ProtobufDecoder(TestProtoc.TestProto.getDefaultInstance()));
//                    ch.pipeline().addLast("handler", new ClientHandler());
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                            .addLast(new ProtobufDecoder(TestProtoc.TestProto.getDefaultInstance()))
                            .addLast(new ProtobufVarint32LengthFieldPrepender())
                            .addLast(new ProtobufEncoder())
                            .addLast(new ClientHandler());

                }

                ;
            });

            Channel ch = bootstrap.connect("127.0.0.1", 8888).sync().channel();

            // 控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null || "".equals(line)) {
                    continue;
                }
                TestProtoc.TestProto.Builder builder = TestProtoc.TestProto.newBuilder();
                builder.addName(line);

                ch.writeAndFlush(builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}