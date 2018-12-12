package com.finace.miscroservice.activity.service;

import java.util.Map;

public interface UserChannelGeneralizeService {


    Map<String,String> insertUserChannelGeneralize(String appid, String idfa, String keywords, String channel, String timestamp, String callback, String phone);

    Map<String,String> findUserChannelGeneralize(String idfa, String phone);

    void test(Integer pageNum);
}
