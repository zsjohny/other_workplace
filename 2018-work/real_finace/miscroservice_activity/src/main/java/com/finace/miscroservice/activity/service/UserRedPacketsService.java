package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.activity.po.UserRedPacketsPO;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.utils.Response;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 用户红包service层
 */
public interface UserRedPacketsService {

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
     * 红包使用修改红包状态
     * @param map
     */
    public UserRedPackets updateHbStatus(Map<String, Object> map);

    /**
     * 发红包
     * @param userId
     * @param inviter
     * @param nid
     */
    public void grantFlq(int userId, int inviter, String nid, double total) throws ParseException ;

    /**
     *  发放新手福利券
     * @param userId
     */
    public void grantXsFlq(int userId) throws ParseException ;

    /**
     *邀请好友获取的红包个数和红包总金额
     * @param userId
     * @return
     */
    public Map<String, Object> getInviterCountSumHb(int userId);

    /**
     * 获取邀请红包列表
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getInviterList(int userId, int page);
    
    /**
     * 将到期红包标记为已过期
     * @param
     */
    public void updateUserRedPacketsEnded(String hbid);
    
    /**
     * 获取未使用且已到期的红包
     * @return
     */
    public List<UserRedPackets> getEndedUserRedPackets();
   
    /**
     * 更新到期红包mq消息状态为已发送
     * @param
     */
    public void updateUserRedPacketsFlag(UserRedPackets userRedPackets);

    /**
     * 2018年新年活动邀请好友注册送88元红包
     * @param userId
     */
    public void newYearGrantRedPackets(int userId) throws ParseException;

    /**
     * 获取红包过期前三天给用户发短信
     * @return
     */
    List<String> getWillExpiredUserId();

    /**
     * 获取福利券信息
     * @param map
     * @param page
     * @return
     */
    Response hongbao(Map<String, Object> map, int page);

    /**
     * 获取 全民狂欢红包
     * @param code
     * @param userId
     * @return
     */
    Response addCarnival(Integer code, String userId);

    /**
     * 获取全民 狂欢活动红包 已选取的红包
     * @return
     */
    String getCarnival(String userId);
}






