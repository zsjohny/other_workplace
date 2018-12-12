package org.dream.utils.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.dream.utils.quota.msg.AbstractXtraderQuota;
import org.dream.utils.quota.msg.XtraderHeartbeatReq;
import org.dream.utils.quota.msg.XtraderQuotaResp;
import org.dream.utils.quota.msg.XtraderSubContractReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Sharable
public class SimpleMessageCodec extends ChannelDuplexHandler {
    public static Boolean recordLogFlag = true;

    private Logger logger = LoggerFactory.getLogger(SimpleMessageCodec.class);
    private final MessageToByteEncoder<AbstractXtraderQuota> encoder = new MessageToByteEncoder<AbstractXtraderQuota>() {

        @Override
        protected void encode(ChannelHandlerContext ctx, AbstractXtraderQuota msg, ByteBuf out) throws Exception {
            msg.send(out);
        }

    };

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final ByteToMessageDecoder decoder = new ByteToMessageDecoder() {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            while (true) {
                if (in.readableBytes() < 8) {
                    break;
                }
                in.markReaderIndex();
                in.readUnsignedShortLE();
                in.readByte();
                int wlen = in.readUnsignedShortLE();
                in.readByte();
                int wCmd = in.readUnsignedShortLE();
                if (in.readableBytes() < wlen - 6) {
                    in.resetReaderIndex();
                    break;
                }
                byte[] resp = new byte[wlen - 6];
                in.readBytes(resp);

                if (wCmd == 12) {
                    XtraderQuotaResp quota = new XtraderQuotaResp();
                    quota.quota = new String(resp, AbstractXtraderQuota.charset);
                    out.add(quota);
                } else {
                    String xResp = new String(resp, AbstractXtraderQuota.charset);


                    if (wCmd == 7 && recordLogFlag) {
                        logger.debug("可订阅品种应答 {}", xResp.replaceAll("[\r|\n]", ""));
                        XtraderSubContractReq contract = new XtraderSubContractReq();

                        JSONObject xresp = JSON.parseObject(xResp);
                        contract.ExchangeNo = xresp.getJSONObject("MsgData").getString("ExchangeNo");
                        contract.CommodityNo = xresp.getJSONObject("MsgData").getString("CommodityNo");
                        ctx.channel().writeAndFlush(contract);
                    } else if (wCmd == 9) {
                        logger.debug("可订阅合约应答 {}", xResp.replaceAll("[\\s]+", ""));
                    } else if (wCmd == 3) {
                        logger.debug("登录应答信息{}", xResp.replaceAll("[\\s]+",""));
                    } else if (wCmd == 5) {
                        logger.debug("登出应答信息{}", xResp.replaceAll("[\\s]+",""));
                    } else if (wCmd == 11) {
                        logger.debug("行情订阅信息{}", xResp.replaceAll("[\\s]+",""));
                    } else if( wCmd == 14) {
                        logger.debug("行情取消订阅信息应答{}", xResp.replaceAll("[\\s]+",""));
                    }else if(wCmd == 15) {
                        logger.debug("心跳检测包{}", xResp.replaceAll("[\\s]+",""));

                        XtraderHeartbeatReq heartbeatReq = new XtraderHeartbeatReq();
                        JSONObject xresp = JSON.parseObject(xResp);

                        heartbeatReq.ClientSendTime =getCurrentDateString();
                        heartbeatReq.ServerSendTime =xresp.getJSONObject("MsgData").getString("ServerSendTime");

                        ctx.channel().writeAndFlush(heartbeatReq);
                    }


                }


            }
        }

    };

    private String getCurrentDateString(){
        return dateFormat.format(new Date());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        decoder.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        encoder.write(ctx, msg, promise);
    }

    /**
     * 更改状态
     *
     * @param flag
     */
    public static void changeStatusRecord(Boolean flag) {
        if (flag == null) {
            return;
        }
        recordLogFlag = flag;
    }


}
