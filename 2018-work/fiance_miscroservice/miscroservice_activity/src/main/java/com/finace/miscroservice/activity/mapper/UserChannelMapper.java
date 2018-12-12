package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.UserChannelPO;
import com.finace.miscroservice.commons.utils.JwtToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserChannelMapper {

    /**
     *
     * @param userChannelPO
     */
    void addUserChannel(UserChannelPO userChannelPO);

    /**
     * 根据手机号码获取渠道信息
     * @param phone
     * @return
     */
    List<UserChannelPO> getUserChannelByPhone(@Param("phone") String phone);

    /**
     * 根据uid码获取渠道信息
     * @param uid
     * @return
     */
    List<UserChannelPO> getUserChannelByUid(@Param(JwtToken.UID) String uid);

}
