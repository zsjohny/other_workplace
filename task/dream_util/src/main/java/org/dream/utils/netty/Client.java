/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package org.dream.utils.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 *
 * 描述:
 *
 * @author  boyce
 * @created 2015年7月23日 下午2:01:09
 * @since   v1.0.0
 */
public class Client {

	public static void connect(NettyHandler handler, int workers, String host, int port) throws Exception {
		ExecutorService pool=Executors.newFixedThreadPool(Math.max(workers, 1));
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup).channel(NioSocketChannel.class).handler(new InitHandler(handler, pool));

			b.connect(host, port).sync().channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			pool.shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread(){
				public void run() {
					try {
						Client.connect(new NettyHandler() {
							@Override
							public void handleMsg(Channel channel, String json) {
//								System.out.println(json);
//							channel.writeAndFlush("clientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclient");
							}
							@Override
							public void channelRemoved(Channel channel) {
//								System.out.println("remove");
							}
							@Override
							public void channelRegistered(Channel channel) {
								channel.writeAndFlush( System.currentTimeMillis()+" hello1");
								channel.writeAndFlush( System.currentTimeMillis()+" aclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientx");
								channel.writeAndFlush( System.currentTimeMillis()+" uuclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclient"
										+ "clientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclient"
										+ "clientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclient"
										+ "clientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientmm");
//								System.out.println("add");
							}
						}, 16, "127.0.0.1", 13502);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//192.168.1.20
				};
			}.start();//1437998421709  1437998412356
		}
	}
	

}
