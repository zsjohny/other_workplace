package org.dream.utils.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class InitHandler extends ChannelInitializer<SocketChannel> {
	NettyHandler handler;
	ExecutorService pool;
	public InitHandler(NettyHandler handler,ExecutorService worker){
		this.handler=handler;
		pool=worker;
	}
	private  SimpleChannelInboundHandler<String> coreHandler=new SimpleChannelInboundHandler<String>(){
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			handler.channelRegistered(ctx.channel());
		};
		@Override
		protected void channelRead0(final ChannelHandlerContext ctx, final String msg) throws Exception {
			pool.execute(new Runnable() {
				
				@Override
				public void run() {
					handler.handleMsg(ctx.channel(), msg);
				}
			});
		}
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			handler.channelRemoved(ctx.channel());
		};
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			ctx.close();
			cause.printStackTrace();
			handler.channelRemoved(ctx.channel());
		};
		public boolean isSharable() { 
			return true;
		}
	};
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new DreamJSONDecoder());
		pipeline.addLast(new DreamJSONEncoder());
		pipeline.addLast(coreHandler);
	}

}