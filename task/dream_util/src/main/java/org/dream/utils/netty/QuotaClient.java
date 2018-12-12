/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package org.dream.utils.netty;

import org.dream.utils.jms.JmsSender;

/**
 *
 * 描述:之前的架构中，行情采取的是tcp直连c++服务器的方式，现在要改用c++发送topic消息的方式。
 * 本代码主要在迁移过程中替代c++先往mq中发送消息的功能，在各服务中不会用到。
 *
 * @author boyce
 * @created 2015年7月27日 下午1:57:02
 * @since v1.0.0
 */
@Deprecated
public class QuotaClient {


	boolean work = true;
	boolean start = false;
	private static long ERR_RESTART_TIME_MS=500;//启动失败，隔500ms重启

	private String host;
	private int port;

	public QuotaClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private QuotaHandler handler = new QuotaHandler(this);
	JmsSender jmsSender;
	public void setJmsSender(JmsSender jmsSender) {
		this.jmsSender = jmsSender;
	}
	public synchronized void restart() {
		if (!work) {//work=false;说明有人或者系统强制关闭了行情。除了人工start，其余重启机制均应失效。
			return;
		}
		if (start) {// 如果已经启动，则不再启动
			return;
		}
		start = true;// 标示已经在启动
		new Thread() {
			public void run() {
				try {
					Client.connect(handler, 1, host, port);
				} catch (Exception e) {
					start = false;// 启动失败
					try {
						Thread.sleep(ERR_RESTART_TIME_MS);
						QuotaClient.this.restart();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			};
		}.start();
	}

	public synchronized void start() {
		work = true;// 人工启动
		restart();
	}

	public synchronized void shutdown() {
		work = false;// 人工关闭
		handler.ch.close();
	}

	public static void main(String[] args) {
		QuotaClient client=new QuotaClient("120.27.240.3", 12904);
		client.start();
	}
}













