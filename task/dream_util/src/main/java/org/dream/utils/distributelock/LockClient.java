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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.dream.utils.distributelock.codec.Monitor;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author Boyce 2016年8月19日 下午3:05:39
 */
public final class LockClient {
	static ConcurrentHashMap<String, Queue<Monitor>> monitors = new ConcurrentHashMap<>();// 同一个锁上的所有等待请求信号放在一个queue上面
	Channel lockChannel = null;// 之后改为连接池的概念，一个lockClient有多个channel
	EventLoopGroup group;
	UUID clientId = null;

	public  void reqLock(String monitor) {
		Monitor m = new Monitor();
		m.clientId = clientId;
		m.cmdId = Monitor.CMD_REQ_LOCK;
		m.monitor = monitor;
		m.remoteThreadId = (int) Thread.currentThread().getId();
		lockChannel.writeAndFlush(m);
		synchronized (m) {
			Queue<Monitor> queue = null;
			if (!monitors.containsKey(monitor)) {
				monitors.putIfAbsent(monitor, new LinkedBlockingQueue<Monitor>());
			}
			queue = monitors.get(monitor);
			try {
				queue.offer(m);
				m.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public  void reqUnLock(String monitor) {
		Monitor m = new Monitor();
		m.clientId = clientId;
		m.cmdId = Monitor.CMD_REQ_UNLOCK;
		m.monitor = monitor;
		m.remoteThreadId = (int) Thread.currentThread().getId();
		lockChannel.writeAndFlush(m);
	}
	public void init() {
		JSON.toJSONString(new Monitor());
		try {
			Bootstrap b = new Bootstrap();
			group = new NioEventLoopGroup();
			b.group(group).channel(NioSocketChannel.class).handler(new LockClientInitializer());
			lockChannel = b.connect("127.0.0.1", 2001).sync().channel();
			clientId = UUID.randomUUID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutdown() {
		if (lockChannel != null && lockChannel.isActive()) {
			lockChannel.close();
		}
		try {
			if (lockChannel != null) {
				lockChannel.closeFuture().sync();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (group != null) {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		
		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					LockClient lc=new LockClient();
					lc.init();
					lc.reqLock("ttttt");
					System.out.println("do"+Thread.currentThread());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lc.reqUnLock("ttttt");
				};
			}.start();
		}
	}
}
