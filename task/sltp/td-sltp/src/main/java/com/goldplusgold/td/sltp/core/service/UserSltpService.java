package com.goldplusgold.td.sltp.core.service;


import com.goldplusgold.td.sltp.core.viewmodel.UserSltpAllVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOneVM;

import com.goldplusgold.td.sltp.core.viewmodel.UserSltpOperVM;
import com.goldplusgold.td.sltp.core.viewmodel.UserSltpVM;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;

/**
 * 用户止盈止损的service
 * Created by Ness on 2017/5/10.
 */
public interface UserSltpService {
    /**
     * 根据用户的止盈止损的id查询单个详细信息
     *
     * @param uuid 止盈止损的id
     * @return
     */
    UserSltpOneVM findUserSltpOneByUuid(String uuid);

    /**
     * 根据用户Id查找所有用户的数据
     *
     * @param userId       用户的id
     * @param clickType       用户点击类型
     * @param contractName 合约的名称
     * @param bearBull     空头或者多头 0 空 1多
     * @param page         开始的页数 默认是第一页
     * @return
     */
    UserSltpAllVM findAllUserSltpInByUserId(String userId, Integer clickType, String contractName, Integer bearBull, Integer page);


    /**
     * 根据用户的止盈止损的id删除
     *
     * @param uuid 用户的止盈止损的id
     */
    UserSltpOperVM removeUserSltpRecordByUuid(String uuid);


    /**
     * 根据止盈止损的id 更新止盈止损的详细信息
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    UserSltpOperVM updateUserSltpRecordInfoByUuid(UserSltpRecord userSltpRecord, Boolean isOperateDb);

    /**
     * 保存用户的止盈止损的数据
     *
     * @param userSltpRecord 止盈止损上传实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    UserSltpOperVM saveUserSltpRecordInfo(UserSltpRecord userSltpRecord, Boolean isOperateDb);


}
