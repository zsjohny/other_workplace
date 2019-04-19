/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.wxaapp.msg.bean;

import com.jfinal.weixin.sdk.msg.in.InMsg;
import com.jfinal.wxaapp.msg.bean.WxaMsg;

/**
 * <pre>
    回复文本消息
 &lt;xml&gt;
 &lt;ToUserName&gt;&lt;![CDATA[toUser]]&gt;&lt;/ToUserName&gt;
 &lt;FromUserName&gt;&lt;![CDATA[fromUser]]&gt;&lt;/FromUserName&gt;
 &lt;CreateTime&gt;12345678&lt;/CreateTime&gt;
 &lt;MsgType&gt;&lt;![CDATA[text]]&gt;&lt;/MsgType&gt;
 &lt;Content&gt;&lt;![CDATA[你好]]&gt;&lt;/Content&gt;
 &lt;/xml&gt;
 </pre>
 */
@SuppressWarnings("serial")
public class WxaOutTextMsg extends WxaOutMsg {

    private String content;

    public WxaOutTextMsg() {
        this.msgType = "text";
    }

    public WxaOutTextMsg(WxaMsg wxaMsg) {
        super(wxaMsg);
        this.msgType = "text";
    }
    

    @Override
    protected void subXml(StringBuilder sb) {
        if (null == content) {
            throw new NullPointerException("content is null");
        }
        sb.append("<Content><![CDATA[").append(content).append("]]></Content>\n");
    }

    public String getContent() {
        return content;
    }

    public WxaOutTextMsg setContent(String content) {
        this.content = content;
        return this;
    }

}


