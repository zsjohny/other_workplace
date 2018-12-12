import com.sun.deploy.util.StringUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by nessary on 16-8-12.
 */
public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup work = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        //添加小组
        bootstrap.group(work);


        //添加channel
        bootstrap.channel(NioSocketChannel.class);

        //添加处理器

        bootstrap.handler(new ClientHandler());


        Channel channel = bootstrap.connect("localhost", 10068).sync().channel();

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("请开始输入...");

        for (; ; ) {


            String str = read.readLine();

            if (str == null || str.isEmpty()) {
                System.out.println("请不要输入空字符串");
                continue;
            }


            System.out.println("请继续输入...");

            channel.writeAndFlush(str + "\n");

        }

    }

}
