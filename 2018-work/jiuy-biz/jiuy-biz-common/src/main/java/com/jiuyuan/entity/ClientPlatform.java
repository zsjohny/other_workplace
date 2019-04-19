package com.jiuyuan.entity;

import java.io.Serializable;

import org.aspectj.apache.bcel.generic.ReturnaddressType;

import com.jiuyuan.util.ClientUtil;
import com.jiuyuan.constant.Platform;


public class ClientPlatform implements Serializable {

    private static final long serialVersionUID = -3184655541421886643L;

    private Platform platform;

    private String version;
    
    /**
     * 微信永远是最大版本
     * @return
     */
    public static ClientPlatform getWeiXinClient(){
    	return new ClientPlatform(Platform.WENXIN,"100.0.0");
    }

    public ClientPlatform(Platform platform, String version) {
        this.platform = platform;
        this.version = version;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isAndroid() {
        return getPlatform() == Platform.ANDROID;
    }

    public boolean isIphone() {
        return getPlatform() == Platform.IPHONE;
    }

    public boolean needRegisterCaptcha() {
        if (isAndroid()) {
            return ClientUtil.compareTo(getVersion(), "1.0.1") > 0;
        }
        return ClientUtil.compareTo(getVersion(), "1.0.0") > 0;
    }

}
