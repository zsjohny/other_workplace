package org.dream.utils.quota;

import com.alibaba.fastjson.JSON;

/**
 * Created by nessary on 16-8-16.
 */
public class Get extends QuotaHandler {

    public Get() {

    }

    public Get(String appName, String userName, String password, String authCode) {
        this.appName = appName;
        this.userName = userName;
        this.password = password;
        this.authCode = authCode;
    }


    @Override
    public void onQuota(String quota) {
        System.out.println(quota);
        System.out.println(JSON2QuotaObject.getQuota2InterJson(JSON.parseObject(quota)).getInstrumentId());

    }




}
