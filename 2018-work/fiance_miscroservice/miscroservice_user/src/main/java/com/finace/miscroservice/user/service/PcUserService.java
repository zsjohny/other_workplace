package com.finace.miscroservice.user.service;


import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.entity.response.MyBorrowInfoResponse;

import javax.servlet.http.HttpServletResponse;


/**
 * 用户service
 */
public interface PcUserService {


    Response pcAccountLog(String startTime, String endTime, Integer page, Integer userId);

    Response pcMyProperty(Integer userId);

    Response pcBackMoney(Integer integer, String month);

    Response pcMyCoupons(Integer type, Integer page, Integer userId);

    Response myInvitation(Integer userId);

    Response myRewardsRecord(Integer userId, Integer page);

    Response myInformation(Integer userId);

    Response myFinanceBid(Integer page, String userId);

    Response register(String username, String pass);

    Response myBorrowinfoById(String userId,Integer type,Integer page);
    MyBorrowInfoResponse getInfoByBorrowId(Integer borrowId);
}
