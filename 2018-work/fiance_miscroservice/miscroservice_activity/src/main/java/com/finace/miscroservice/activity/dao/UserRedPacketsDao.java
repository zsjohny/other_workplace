package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.UserRedPacketsPO;
import com.finace.miscroservice.commons.entity.UserRedPackets;

import java.util.List;
import java.util.Map;

/**
 * 用户红包dao层
 */
public interface UserRedPacketsDao {

    /**
     * 获取用户福利券总数
     * @return
     */
    public int getCountRedPacketsByUserId(Map<String, Object> map);

    /**
     * 获取用户红包信息 分页
     * @param map
     * @return
     */
    public List<UserRedPackets> getRpByUserId(Map<String, Object> map, int page);

    /**
     * 获取用户红包信息
     * @param map
     * @return
     */
    public List<UserRedPacketsPO> getRedPacketsByUserId(Map<String, Object> map);

    /**
     * 根据红包id获取红包信息
     * @param id
     * @return
     */
    public UserRedPackets getRpById(int id);

    /**
     * 获取红包列表
     * @param map
     * @return
     */
    public List<UserRedPacketsPO> getHbByParam(Map<String, Object> map);

    /**
     * 获取可使用红包个数
     * @param map
     * @return
     */
    public int getCountHbByParam(Map<String, Object> map);

    /**
     * 修改红包状态
     * @param map
     */
    public void updateRedPacketsStatus(Map<String, Object> map);

    /**
     * 新增红包信息
     * @param userRedPacketsPO
     */
    public void addUserRedPackets(UserRedPacketsPO userRedPacketsPO);

    /**
     *邀请好友获取的红包个数和红包总金额
     * @param userId
     * @return
     */
    public UserRedPackets getInviterCountSumHb(int userId);

    /**
     * 获取邀请红包列表
     * @param userId
     * @return
     */
    public List<UserRedPackets> getInviterList(int userId);

    /**
     * getUserIdInviter
     * @param userId
     * @return
     */
    public UserRedPackets getUserIdInviter(int userId, int inviter);
    
    /**
     * 获取未使用且已到期的红包
     * @return
     */
    public List<UserRedPackets> getEndedUserRedPackets();

    /**
     * 获取红包过期前三天给用户发短信
     * @return
     */
    List<String> getWillExpiredUserId();

}
