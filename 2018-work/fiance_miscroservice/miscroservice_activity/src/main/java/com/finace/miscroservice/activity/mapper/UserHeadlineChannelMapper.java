package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserHeadlineChannelMapper {


    /**
     * 获取修改头条
     * @param map
     * @return
     */
    UserHeadlineChannelPO getByImei(Map<String, Object> map);

    /**
     * 修改头条渠道状态
     * @param map
     * @return
     */
    int updateStatusByImei(Map<String, Object> map);

    /**
     * 新增头条渠道
     * @param userHeadlineChannelPO
     */
    void addUserHeadlineChannel(UserHeadlineChannelPO userHeadlineChannelPO);


}
