import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by nessary on 16-8-12.
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup work = new NioEventLoopGroup();

        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();


        //添加小组
        bootstrap.group(work, group);

        //添加listener
        bootstrap.channel(NioServerSocketChannel.class);

        //添加handle
        bootstrap.childHandler(new ServerHandler());


        //添加端口
        ChannelFuture channelFuture = bootstrap.bind(10068).sync();


        channelFuture.channel().closeFuture().sync();

//        channelFuture.sync().channel().close();

        System.out.println("success...." );

    }

}
