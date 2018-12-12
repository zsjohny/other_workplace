import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by nessary on 16-8-12.
 */
public class ServerHanderOwner extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        System.out.println(channelHandlerContext.channel().remoteAddress() + "..." + s);

        channelHandlerContext.writeAndFlush("have received");





    }

    //待写


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush("you local  success");

    }
}
