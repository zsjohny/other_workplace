package org.dream.utils.distributelock.codec;

import java.util.List;
import java.util.UUID;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
@Sharable
public class SimpleMessageCodec extends ChannelDuplexHandler{

	private static final String CHARSET="UTF-8";
	private final MessageToByteEncoder<Monitor> encoder = new MessageToByteEncoder<Monitor>() {

		@Override
		protected void encode(ChannelHandlerContext ctx, Monitor msg, ByteBuf out) throws Exception {
			byte[] bs=msg.monitor.getBytes(CHARSET);
			out.writeInt(1+16+4+bs.length);
			out.writeByte(msg.cmdId);
			out.writeLong(msg.clientId.getMostSignificantBits());
			out.writeLong(msg.clientId.getLeastSignificantBits());
			out.writeInt(msg.remoteThreadId);
			out.writeBytes(bs);
		}

    };

    private final ByteToMessageDecoder decoder = new ByteToMessageDecoder() {

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
			while (true) {
				if (in.readableBytes() <= 4) {
					break;
				}
				in.markReaderIndex();
				int length = in.readInt();
				if(length<21){//1+16+4
					throw new Exception("a negative length occurd while decode!");
				}
				if (in.readableBytes() < length) {
					in.resetReaderIndex();
					break;
				}
				Monitor m=new Monitor();
				m.cmdId=in.readByte();
				m.clientId=new UUID(in.readLong(), in.readLong());
				m.remoteThreadId=in.readInt();
				byte[] bs=new byte[length-21];
				in.readBytes(bs);
				m.monitor=new String(bs, CHARSET);
				out.add(m);
			}
		}

    };

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        decoder.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        encoder.write(ctx, msg, promise);
    }
    public static void main(String[] args) {
		System.out.println("".getBytes().length);
	}
}
