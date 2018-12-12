package org.dream.utils.distributelock.codec;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import io.netty.channel.Channel;

public class Monitor {
	public static int CMD_HEARTBEAT = 0;//心跳包
	public static int CMD_REQ_LOCK = 1;//请求锁
	public static int CMD_REQ_UNLOCK = 2;//当前无锁，或者其他客户端已经解锁，通知客户端已经获得了锁
	public static int CMD_RESP_LOCK = -1;//加锁请求已经发到服务端，但是当前锁被其他客户端持有，请请等待
	public static int CMD_RESP_UNLOCK = -2;//解锁请求成功。
	public static int CMD_NOTIFY_UNLOCK=3;
	public static int CMD_NOTIFY_UNLOCK_REC=-3;

	public int cmdId;//请求编号
	public UUID clientId;//客户端唯一标识
	public int remoteThreadId;//远程客户端线程id
	public String monitor;//同步锁监视器名称
	@JSONField(serialize = false, deserialize = false)
	public Channel channel;//客户端channel

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}

	@Override
	public int hashCode() {//同一个客户端同一个线程同一个锁监视器表示是同一个monitor对象

		long hilo = clientId.hashCode() + remoteThreadId + monitor.hashCode();
		return ((int) (hilo >> 1)) ^ (int) hilo;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return obj != null && obj instanceof Monitor && obj.hashCode() == this.hashCode();
	}

	public static void main(String[] args) {
		Monitor m = new Monitor();
		m.clientId = UUID.randomUUID();
		m.remoteThreadId = 12;
		m.monitor = "1234";
		m.cmdId = 0;
		System.out.println(m.hashCode());
	}
}
