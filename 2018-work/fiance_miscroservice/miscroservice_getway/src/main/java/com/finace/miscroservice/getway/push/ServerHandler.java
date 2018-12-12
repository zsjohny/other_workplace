package com.finace.miscroservice.getway.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import com.finace.miscroservice.commons.utils.DesUtil;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.getway.enums.PushTypeEnum;
import com.finace.miscroservice.getway.util.UidDistribute;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.COLLECT_CLIENT_INFO;
import static com.finace.miscroservice.commons.enums.MqChannelEnum.IDFA_OR_IMEI;
import static com.finace.miscroservice.commons.enums.MqChannelEnum.NEW_USER_GRANT_HB;
import static com.finace.miscroservice.getway.enums.PushTypeEnum.*;

/**
 * 建立处理器
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private UidDistribute uidDistribute;

    public ServerHandler(UidDistribute uidDistribute) {
        this.uidDistribute = uidDistribute;
    }

    private final long TIMER_DEVIATION = 1000 * 60 * 60 * 25L;

    /**
     * 日志文件
     */
    private Log logger = Log.getInstance(ServerHandler.class);


    /**
     * 建立连接
     *
     * @param ctx
     * @param msg 发送的格式  时间戳+"_"+a/h/i(设备标识)+"_"+{uid:(没有可为空),imei:xxx}(json格式)
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();
        logger.info("长连接获取uid,所传参数{}", msg);
        try {
            //首次验证
            msg = DesUtil.decrypt(msg, DesUtil.KEY);
            logger.info("长连接获取uid解密后,所传参数{}", msg);
            //格式 时间戳+":"+设备标识
            if (msg.isEmpty() || !msg.contains("_")) {
                logger.warn("客户端={} 所传消息={} 不符合规则", channel.remoteAddress(), msg);
                return;
            }

            //再次验证 时间是否过期
            String[] arr = msg.split("_");

            if (arr.length != 3) {
                logger.warn("客户端={} 所传参数长度不符合规则", channel.remoteAddress());
                return;
            }

            try {
                long time = Long.parseLong(arr[0]);
                logger.info("客户端={} 客户端获取uid，系统时间{}，设备时间={}, 相差时间={}", System.currentTimeMillis(), time, Math.abs(System.currentTimeMillis() - time));
                if (Math.abs(System.currentTimeMillis() - time) > TIMER_DEVIATION) {
                    logger.warn("客户端={}所传时间={}超前 不给予通过", channel.remoteAddress(), time);
                    return;
                }
            } catch (NumberFormatException e) {
                logger.error("客户端={} 所传的信息时间参数={}格式不正确", channel.remoteAddress(), arr[0]);
                return;
            }


            DeviceEnum deviceEnum = null;
            switch (arr[1]) {
                case "a":
                    deviceEnum = DeviceEnum.ANDROID;

                    break;
                case "i":
                    deviceEnum = DeviceEnum.IOS;
                    break;
                case "h":
                    deviceEnum = DeviceEnum.H5;
                    break;
                default:
                    logger.warn("客户端={} 分配uid 所传信息不符合规范", channel.remoteAddress());
            }

            JSONObject jsonObject = JSONObject.parseObject(arr[2]);
            if (jsonObject == null || jsonObject.isEmpty()) {
                logger.warn("客户端={} 第三个参数不是json格式 参数为={}", channel.remoteAddress(), msg);
                return;
            }
            //获取分配类型
            PushTypeEnum pushTypeEnum;
            String uid = "";

            for (Map.Entry<String, Object> entries : jsonObject.entrySet()) {

                pushTypeEnum = converPushTypeByStr(entries.getKey());
                if (pushTypeEnum == null) {
                    logger.warn("客户端={} 获取分配类型为空", channel.remoteAddress());
                    continue;
                }
                JSONObject jsonObject1 = null;
                //判断分配类型
                switch (pushTypeEnum) {
                    case UID:
                        if (uid.isEmpty()) {
                            uid = uidDistribute.distribute(deviceEnum);
                            channel.writeAndFlush(uid);
                            logger.info("客户端={} 成功获取uid为={}", channel.remoteAddress(), uid);
                        }
                        break;
                    case IMEI:
                        if (uid.isEmpty() && jsonObject.containsKey(UID)) {
                            uid = uidDistribute.distribute(deviceEnum);
                            channel.writeAndFlush(uid);
                            logger.info("客户端={} 成功获取uid为={}", channel.remoteAddress(), uid);
                        }
                        logger.info("客户端={} 发送 {} 为 {}", channel.remoteAddress(), IMEI.toType(), entries.getValue());
                        switch (deviceEnum) {
                            case ANDROID:
                                logger.info("android客户端={} 发送 {} 为 {}", channel.remoteAddress(), IMEI.toType(), entries.getValue());
                                //android
                                jsonObject1 = JSONObject.parseObject(entries.getValue().toString());
                                jsonObject1.put(JwtToken.UID, uid);
                                ApplicationContextUtil.getBean(MqTemplate.class).sendMsg(COLLECT_CLIENT_INFO.toName(), jsonObject1.toString());
                                ApplicationContextUtil.getBean(MqTemplate.class).sendMsg(IDFA_OR_IMEI.toName(),entries.getValue().toString());
                                break;
                            case IOS:
                                logger.info("ios客户端={} 发送 {} 为 {}", channel.remoteAddress(), IMEI.toType(), entries.getValue());
                                //ios
                                jsonObject1 = JSONObject.parseObject(entries.getValue().toString());
                                jsonObject1.put(JwtToken.UID, uid);
                                ApplicationContextUtil.getBean(MqTemplate.class).sendMsg(COLLECT_CLIENT_INFO.toName(), jsonObject1.toString());
                                ApplicationContextUtil.getBean(MqTemplate.class).sendMsg(IDFA_OR_IMEI.toName(),  entries.getValue().toString());
                                break;
                            default:
                                logger.warn("客户端={} 不支持此设备", channel.remoteAddress());
                                continue;
                        }
                        break;
                    case VERSION:
                        logger.info("判断客户端={}是否需要更新 ", channel.remoteAddress());
                        String vs = JSON.toJSONString(ApplicationContextUtil.getBean(UpdateVersion.class).toJsonString());
                        channel.writeAndFlush(vs);
                        logger.info("判断客户端={}是否需要更新,更新数据={}", channel.remoteAddress(), vs);
                        break;
                    default:
                        logger.warn("客户端={} 所传的分配类型暂不支持", channel.remoteAddress());

                }

            }

        } catch (Exception e) {
            logger.error("客户端={} 处理请求出错", channel.remoteAddress(), e);

        } finally {

            channel.close();
        }


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("客户端={} 异常退出", ctx.channel().remoteAddress());
        super.exceptionCaught(ctx, cause);
    }


}

