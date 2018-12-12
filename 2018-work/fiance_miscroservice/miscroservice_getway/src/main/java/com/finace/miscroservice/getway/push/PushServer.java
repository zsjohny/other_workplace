package com.finace.miscroservice.getway.push;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.getway.util.UidDistribute;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 移动端push服务
 */
@Component
public class PushServer implements DisposableBean {


    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    @Value("${push.server.port}")
    private int port;
    @Autowired
    private UidDistribute uidDistribute;


    /**
     * 日志文件
     */
    private Log logger = Log.getInstance(PushServer.class);


    @PostConstruct
    public void startServer() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new JSONDecoder());
                pipeline.addLast(new JSONEncoder());
                pipeline.addLast(new ServerHandler(uidDistribute));
            }

        });


        channel = b.bind(port).sync().channel();


        logger.info("分配Uid的Push服务初始化完成");

    }


    @Override
    public void destroy() throws Exception {
        try {


            if (channel != null && channel.isActive()) {
                channel.close();
            }


            if (bossGroup != null) {

                bossGroup.shutdownGracefully();
            }

            if (workerGroup != null) {

                workerGroup.shutdownGracefully();

            }


        } catch (Exception e) {
            logger.error("分配Uid的Push服务关闭出错...", e);
        }
    }

}
