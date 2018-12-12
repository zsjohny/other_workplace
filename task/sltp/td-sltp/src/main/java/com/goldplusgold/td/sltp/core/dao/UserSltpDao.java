package com.goldplusgold.td.sltp.core.dao;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;

import java.util.List;

/**
 * 用户止盈止损的dao
 * Created by Ness on 2017/5/10.
 */
public interface UserSltpDao {
    /**
     * 根据用户的止盈止损的id查询单个详细信息
     *
     * @param uuid 止盈止损的id
     * @return
     */
    UserSltpRecord findUserSltpOneByUuid(String uuid);

    /**
     * 根据用户Id查找所有用户的数据
     *
     * @param userId       用户的id
     * @param clickType       用户点击类型
     * @param contractName 合约的名称
     * @param bearBull     空头或者多头 0 空 1多
     * @param page         开始的页数 默认是第一页
     * @param isOperateDb  是否是删除mysql还是redis true是mysql
     * @return
     */
    List<UserSltpRecord> findAllUserSltpInByUserId(String userId, Integer clickType, String contractName, Integer bearBull, Integer page, Boolean isOperateDb);


    /**
     * 根据用户的止盈止损的id删除
     *
     * @param uuid 用户的止盈止损的id
     */
    boolean removeUserSltpRecordByUuid(String uuid);


    /**
     * 根据止盈止损的id 更新止盈止损的详细信息
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    boolean updateUserSltpRecordInfoByUuid(UserSltpRecord userSltpRecord, Boolean isOperateDb);


    /**
     * 保存用户的止盈止损的数据
     *
     * @param userSltpRecord 止盈止损实体
     * @param isOperateDb    是否是删除mysql还是redis true是mysql
     */
    boolean saveUserSltpRecordInfo(UserSltpRecord userSltpRecord, Boolean isOperateDb);


//    void removeUserSltpCacheBy


}
