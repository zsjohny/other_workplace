package com.finace.miscroservice.user.mapper;


import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.entity.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PcUserMapper {

    List<AccountLogResponse> pcAccountLog(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") Integer userId);

    MyPropertyResponse pcMyProperty(@Param("userId") Integer userId);

    FinanceMoneyResponse pcBackMoney(@Param("userId") Integer userId, @Param("month") String month);

    List<MyCouponsResponse> pcMyCoupons(@Param("type") Integer type, @Param("userId") Integer userId);

    MyInvitationResponse myInvitation(@Param("userId") Integer userId);

    UserRedPackets getUserIdInviter(@Param("userId")Integer userId, @Param("user_id")int user_id);

    MyInformationResponse myInformation(@Param("userId")Integer userId);

    List<MyFinanceBidResponse> myFinanceBid(@Param("userId")String userId);
    List<MyBorrowInfoResponse> myBorrowinfoById(@Param("userId")String userId,@Param("type")Integer type);
    MyBorrowInfoResponse getInfoByBorrowId(@Param("borrowId")Integer borrowId);

    void register(@Param("username")String username, @Param("pass")String pass);

    Register findRegisterTmp(@Param("username")String username);

    void upRegisterTmp(@Param("phone")String phone);
}
