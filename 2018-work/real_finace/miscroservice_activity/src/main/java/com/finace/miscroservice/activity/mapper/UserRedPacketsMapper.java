package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.UserRedPacketsPO;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRedPacketsMapper {


    /**
     * 新增红包信息
     * @param userRedPacketsPO
     */
    void addUserRedPackets(UserRedPacketsPO userRedPacketsPO);

    /**
     * 查询用户红包列表
     * @param map
     * @return
     */
    List<UserRedPacketsPO> getRedPacketsByUserId(Map<String, Object> map);

    /**
     *获取用户可用福利券总数
     * @param map
     * @return
     */
    int getCountRedPacketsByUserId(Map<String, Object> map);

    /**
     * 修改红包状态
     * @param map
     */
    void updateRedPacketsStatus(Map<String, Object> map);

    /**
     * 查询用户红包列表
     * @param map
     * @return
     */
    List<UserRedPackets> getRpByUserId(Map<String, Object> map);

    /**
     * getRpById
     * @param id
     * @return
     */
    UserRedPackets getRpById(@Param("id") int id);

    /**
     * 获取可使红包列表
     * @param map
     * @return
     */
    List<UserRedPacketsPO> getHbByParam(Map<String, Object> map);

    /**
     * 获取可使用红包个数
     * @param map
     * @return
     */
    int getCountHbByParam(Map<String, Object> map);

    /**
     *邀请好友获取的红包个数和红包总金额
     * @param userId
     * @return
     */
    UserRedPackets getInviterCountSumHb(@Param("userId") int userId);

    /**
     * 获取邀请红包列表
     * @param userId
     * @return
     */
    List<UserRedPackets> getInviterList(@Param("userId") int userId);

    /**
     * getUserIdInviter
     * @param map
     * @return
     */
    UserRedPackets getUserIdInviter(Map<String, Integer> map);
    
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
