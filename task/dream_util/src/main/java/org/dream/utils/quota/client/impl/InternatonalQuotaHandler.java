/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.dream.utils.quota.client.impl;


import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.dream.utils.prop.SpringProperties;
import org.dream.utils.quota.msg.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dream.utils.quota.SimpleMessageCodec.recordLogFlag;

/**
 * Handles a client-side channel.
 *
 */
@Sharable
public abstract class InternatonalQuotaHandler extends SimpleChannelInboundHandler<AbstractXtraderQuota> {
    /**
     * 日志文件
     */
    private Logger logger = LoggerFactory.getLogger(InternatonalQuotaHandler.class);
    protected InternationalClient client;


    void bindClient(InternationalClient client) {

        this.client = client;
    }


    protected String appName;
    protected String userName;
    protected String password;
    protected String authCode;


    {
        try {
            SpringProperties properties = SpringProperties.getBean(SpringProperties.class);
            appName = properties.getProperty("sys.utils.quotaClient.appName");

            userName = properties.getProperty("sys.utils.quotaClient.userName");
            password = properties.getProperty("sys.utils.quotaClient.password");
            authCode = properties.getProperty("sys.utils.quotaClient.authCode");
        } catch (Exception e) {

        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        XtraderRegisterReq reg = new XtraderRegisterReq();
        reg.AppName = appName;
        ctx.writeAndFlush(reg);
        Thread.sleep(200);
        XtraderLoginReq req = new XtraderLoginReq();
        req.UserName = userName;
        req.Password = password;
        req.AuthCode = authCode;
        ctx.writeAndFlush(req);
        Thread.sleep(500);

        if (recordLogFlag) {
            ctx.writeAndFlush(new XtraderVarietyReq());
        }

    }

    

    /**
     * 相应的消息结果处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractXtraderQuota msg) throws Exception {


        try {

            XtraderQuotaResp quota = (XtraderQuotaResp) msg;


            onQuota(quota.quota);
        } catch (Exception e) {

            logger.error("收到行情后处理发生异常", e);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        client.restart();
    }


    public abstract void onQuota(String quota);


}
