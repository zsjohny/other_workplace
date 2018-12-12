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
package org.dream.utils.distributelock;


import io.netty.channel.ChannelHandler.Sharable;

import java.util.Queue;

import org.dream.utils.distributelock.codec.Monitor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a client-side channel.
 */
@Sharable
public class LockClientHandler extends SimpleChannelInboundHandler<Monitor> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Monitor msg) throws Exception {
    	System.out.println(msg);
    	if(msg.cmdId==Monitor.CMD_RESP_UNLOCK){
    		Queue<Monitor> queue=LockClient.monitors.get(msg.monitor);
    		if(queue!=null&&!queue.isEmpty()){
    			Monitor m=queue.poll();
    			synchronized (m) {
					m.notify();
				}
    		}
    	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
