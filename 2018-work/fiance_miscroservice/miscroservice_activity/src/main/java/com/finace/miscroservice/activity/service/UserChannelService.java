package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.activity.po.UserChannelPO;
import org.apache.yetus.audience.InterfaceAudience;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 */
public interface UserChannelService {

    /***
     *
     * @param userChannelPO
     */
    public void addUserChannel(UserChannelPO userChannelPO);

    /**
     *
     * @param phone
     * @return
     */
    public List<UserChannelPO> getUserChannelByPhone(String phone);

    /**
     * 根据uid码获取渠道信息
     * @param uid
     * @return
     */
    public List<UserChannelPO> getUserChannelByUid(String uid);
}
