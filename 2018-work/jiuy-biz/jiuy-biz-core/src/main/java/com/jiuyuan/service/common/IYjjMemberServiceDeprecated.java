package com.jiuyuan.service.common;

import com.jiuyuan.constant.SystemPlatform;
import com.yujj.entity.product.YjjMember;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/17 6:46
 * @Copyright 玖远网络
 */
public interface IYjjMemberServiceDeprecated{
    void openMemberShipAccount(Long storeId);

    void closeMemberShipAccount(Long storeId);

    YjjMember findMemberByUserId(SystemPlatform systemPlatform, Long userId);


    void refreshExpirationTime(YjjMember member);

    void clearHistoryDirtyData();
}
