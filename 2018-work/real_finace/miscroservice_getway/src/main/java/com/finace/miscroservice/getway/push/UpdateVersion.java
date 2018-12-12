package com.finace.miscroservice.getway.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class UpdateVersion{

    /**
     * 判断是否需要强制更新0--不需要1--需要
     */
    @Value("${app.update.isupdate}")
    private int isupdate;

    /**
     * 更新的版本号
     */
    @Value("${app.update.vname}")
    private String vname;

    /**
     * 更新下载地址
     */
    @Value("${app.update.updateurl}")
    private String updateurl;

    /**
     * 更新内容
     */
    @Value("${app.update.content}")
    private String content;


    public int getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(int isupdate) {
        this.isupdate = isupdate;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isupdate",isupdate);
        jsonObject.put("updateurl",updateurl);
        jsonObject.put("content",content);
        jsonObject.put("vname",vname);

        return jsonObject.toJSONString();
    }

}




