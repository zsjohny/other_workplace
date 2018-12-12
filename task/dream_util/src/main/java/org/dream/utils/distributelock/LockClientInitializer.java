package org.dream.utils.distributelock;


import org.dream.utils.distributelock.codec.SimpleMessageCodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class LockClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new SimpleMessageCodec());
		pipeline.addLast(new LockClientHandler());
	}

}
