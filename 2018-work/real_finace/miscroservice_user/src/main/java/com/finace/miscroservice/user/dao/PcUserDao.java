package com.finace.miscroservice.user.dao;




import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.entity.response.*;

import java.util.List;

/**
 * 用户Dao层
 */
public interface PcUserDao {

    List<AccountLogResponse> pcAccountLog(String startTime, String endTime, Integer userId);

    MyPropertyResponse pcMyProperty(Integer userId);

    FinanceMoneyResponse pcBackMoney(Integer userId, String month);

    List<MyCouponsResponse> pcMyCoupons(Integer type, Integer userId);

    MyInvitationResponse myInvitation(Integer userId);

    UserRedPackets getUserIdInviter(Integer userId, int user_id);

    /**
     * 我的个人信息
     * @param userId
     * @return
     */
    MyInformationResponse myInformation(Integer userId);

    List<MyFinanceBidResponse> myFinanceBid(String userId);

    void register(String username, String pass);

    Register findRegisterTmp(String username);

    void upRegisterTmp(String phone);

    List<MyBorrowInfoResponse> myBorrowinfoById(String userId,Integer type);
    MyBorrowInfoResponse getInfoByBorrowId(Integer borrowId);
}
