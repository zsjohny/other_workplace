package org.dream.utils.quota.msg;

/**
 * Created by yhj on 16/12/2.
 */
public class XtraderHeartbeatReq extends AbstractXtraderQuota {
    {
        wAssitantCmd = 15;
        MsgType = "Heartbeat";
    }


    public String ClientSendTime; //客户端响应时间
    public String ServerSendTime; // 服务器发送时间

}
