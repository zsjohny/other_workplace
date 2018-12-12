package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.GiftPO;
import com.finace.miscroservice.activity.po.InvitationPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.commons.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserJiangPinMapper {


    /**
     * 新增用户奖励信息
     * @param userJiangPinPO
     */
     void addUserJiangPin(UserJiangPinPO userJiangPinPO);

    /**
     * 获取用户奖励信息
     * @param userId
     * @return
     */
     List<UserJiangPinPO> getUserJiangPinByUserId(@Param("userId") String userId);

    /**
     * 获取所有用户奖励信息
     * @return
     */
     List<UserJiangPinPO> getAllUserJiangPin();

    /**
     * 获取奖励类型
     * @return
     */
    List<UserJiangPinPO> getUserJplx(@Param("userId") String userId);

    /**
     * 添加奖品
     * @param userId
     * @param jiangPinName
     * @param addTime
     * @param remark
     * @param code
     */
    void addUserAward(@Param("underUser")Integer underUser,@Param("userId")Integer userId, @Param("jiangPinName")String jiangPinName, @Param("addTime")String addTime,
                      @Param("remark")String remark, @Param("code")Integer code, @Param("isSend")Integer isSend);

    List<UserJiangPinPO> findUserAward(@Param("underUser")Integer underUser,@Param("userId") Integer userId, @Param("code")Integer code);

    List<GiftPO> findUserAwards(@Param("userId")int userId, @Param("code")Integer code, @Param("starttime")String starttime, @Param("endtime")String endtime);

    Integer findJdCardMoneyAmtByUserId(@Param("userId")int userId);

    InvitationPO getInvitations(@Param("userId")Integer userId,@Param("date")String date,@Param("endTime")String endTime);


    Integer getUserCountByInviterInTime(@Param("userId")Integer userId, @Param("starttime")String starttime,@Param("endtime") String endtime);

    List<User> getUsersByInviterInTime(@Param("userId")Integer userId, @Param("starttime")String starttime,@Param("endtime") String endtime);
}
