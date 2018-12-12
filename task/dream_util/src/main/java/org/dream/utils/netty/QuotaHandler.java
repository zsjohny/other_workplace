package org.dream.utils.netty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;

/**
 * @author Boyce 2016年7月4日 下午3:45:11
 */
@Deprecated
public class QuotaHandler implements NettyHandler {
	private static Logger logger = LoggerFactory.getLogger(QuotaHandler.class);
	Channel ch;
	public long lastRecTime = 0;
	private Timer timer = new Timer();
	private Heartbeat heartbeat;
	private static long TIME_BEAT_MS = 10000;
	private static long RESTART_TIME_MS = 500;// 行情断开，隔500ms重启
	private QuotaClient client;

	public QuotaHandler(QuotaClient client) {
		this.client = client;
	}

	@Override
	public void channelRegistered(Channel channel) {
		ch = channel;
		lastRecTime = System.currentTimeMillis();
		heartbeat = new Heartbeat();
		timer.schedule(heartbeat, TIME_BEAT_MS, TIME_BEAT_MS);
	}

	@Override
	public void handleMsg(Channel channel, String json) {
		lastRecTime = System.currentTimeMillis();
		if(json.length()>100){
			JSONObject data=JSON.parseObject(json).getJSONObject("DATA");
			client.jmsSender.sendMessage(data.toJSONString());
		}
	}

	@Override
	public void channelRemoved(Channel channel) {
		logger.warn("this quota client has been shutdown! try restarting!");
		try {
			heartbeat.cancel();
			client.start=false;
			Thread.sleep(RESTART_TIME_MS);
			client.restart();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	class Heartbeat extends TimerTask {
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("timeStamp", now);
			Map<String, Object> req = new HashMap<String, Object>();
			req.put("CMDID", 1001);
			req.put("DATA", data);
			ch.writeAndFlush(JSON.toJSONString(req));
			if ((now - lastRecTime) > 30000) {
				logger.info("期货行情行心跳调超时，lastTime{}" + new Date(lastRecTime));
				if (ch.isActive()) {
					ch.close();
				}
				this.cancel();
			}
		}
	}
}
