package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.activity.entityvo.ChannelRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserRequest;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

import java.util.List;
import java.util.Map;

public interface ChannelService {
    /**
     * 添加渠道商用户
     */
    void addChannel(String name, String phone);

    /**
     * 修改渠道商信息
     */
    Response updateChannel(Long id, String name, String phone, Integer status);

    /**
     * 渠道商的搜索
     */
    Response search(ChannelRequest req);

    /**
     * 用户的搜索
     */
    Response searchUser(ChannelUserRequest request);

    /**
     * yonghuchaxun
     */
    Response test(Long memberId,Long storeId);

    /**
     * 根据channelId查询二维码 没有则生成
     */
    Response searchQrCode(Long channelId);

    /**
     * 用户的搜索
     */
    List<Map<String, Object>> exUser(ChannelUserRequest request);
}
