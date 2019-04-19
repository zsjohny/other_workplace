package com.e_commerce.miscroservice.activity.mapper;

import com.e_commerce.miscroservice.activity.entity.channel.*;
import com.e_commerce.miscroservice.activity.entityvo.ChannelRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelResponse;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserResponse;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelMapper {

    ChannelUser selectUser(@Param("phone") String phone);
    /**
     * 添加渠道商用户
     * @return
     */
    int addMychannel(@Param("channelUser") ChannelUser channelUser);

    /**
     * 修改渠道商用户信息
     */
    int updateChannel(@Param("id")Long id,@Param("name")String name,@Param("phone")String phone,@Param("status")Integer status);


    /**
     * 搜索全部或者根据条件查询供应商
     */
    List<ChannelResponse> searchAll(@Param("request") ChannelRequest request);

    /**
     * 搜索渠道商下的用户列表
     */
    List<ChannelUserResponse> selectAllUser(@Param("request") ChannelUserRequest request);


    /**
     * 根据渠道商id查询粉丝数量和粉丝下单数量
     */
    ChannelUserGather selectMyFans(@Param("request") ChannelRequest  request);
    ChannelUserGather selectMyFanNew(@Param("request")ChannelUserRequest request);

    /**
     *
     */
    /**
     * 根据渠商id查询实际下过单的粉丝
     */
    List<ChannelUserFans> selectActualFans(@Param("request") ChannelRequest  request);
    List<ChannelUserFans> selectActualFanNew(@Param("request")ChannelUserRequest request);

    List<ChannelUserFans> selectActualFanCount(@Param("channelId")Long channelId,@Param("startTime")String time,@Param("overTime")String overTime);


    /**
     * 根据时间查询当天的新增用户
     */
    Long selectTodayUser(@Param("startTime") String startTime,@Param("overTime") String overTime, @Param("id") Long id);

    /**
     * 根据时间查询当天时间的渠道商下用户的新增单数
     */
    Long selectTodayOrder(@Param("startTime") String startTime,@Param("overTime") String overTime, @Param("id") Long id,@Param("shopMemberId") Long shopMemberId);

    /**
     * 根据小程序id 会员信息
     */
    ShopMemberVo selectShopMemeber(@Param("memberId") Long memberId);

    Long selectAllOrderCount(@Param("list")List list,@Param("orderStatus")Integer orderStatus);

    /**
     * 根据channelId查询渠道商的二维码
     */
    String selectQrCode(@Param("channelId")Long channelId);

    /**
     * 插入二维码
     */
    int updateImg(@Param("channelId") Long channelId,@Param("img") String img);

    List<StoreWxa>selectStoreWxaList(@Param("storeId")Long storeId);

}
