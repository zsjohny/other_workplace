package org.dream.utils.quota;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Boyce
 *         2016年8月9日 下午3:05:51
 */
public class QuotaInitializer extends ChannelInitializer<SocketChannel> {

    private QuotaHandler quotaHandler;

    public QuotaInitializer() {

    }

    public QuotaInitializer(QuotaHandler quotaHandler) {
        this.quotaHandler = quotaHandler;

    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("codec", new SimpleMessageCodec());

        ch.pipeline().addLast("handler", quotaHandler);
    }

}
