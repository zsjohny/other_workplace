package org.dream.utils.quota.msg;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

/**
 * @author Boyce 2016年8月9日 下午3:22:15
 */
public class XtraderRegisterReq extends AbstractXtraderQuota {
	{
		wAssitantCmd=1;
		MsgType="Registe";
	}
	public String AppName;
	public static void main(String[] args) {
		XtraderRegisterReq login=new XtraderRegisterReq();
		login.AppName="a";
		login.send(null);
	}
}
