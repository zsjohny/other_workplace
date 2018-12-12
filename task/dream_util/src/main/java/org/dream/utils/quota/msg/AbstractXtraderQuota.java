package org.dream.utils.quota.msg;

import java.nio.charset.Charset;
import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;

import io.netty.buffer.ByteBuf;

/**
 * @author Boyce 2016年8月9日 下午3:19:14
 */
@JSONType(ignores = {"wAssitantCmd", "MsgType"})
public abstract class AbstractXtraderQuota {
    public static Charset charset = Charset.forName("GBK");
    public static final int VER = 3;
    public static final int CMD = 1;
    protected Byte wAssitantCmd;
    protected String MsgType;

    public void send(ByteBuf out) {
        JSONObject obj = new JSONObject();
        obj.put("MsgType", MsgType);
        obj.put("MsgData", this);
        String body = obj.toJSONString();
        int len = 6 + body.getBytes(charset).length;
        out.writeShortLE(len);
        out.writeByte(VER);
        out.writeShortLE(len);
        out.writeByte(CMD);
        out.writeShortLE(wAssitantCmd);
        out.writeBytes(body.getBytes(charset));

    }


}
