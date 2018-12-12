package com.finace.miscroservice.user.rpc;

import com.finace.miscroservice.commons.entity.UserChannel;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.utils.JwtToken;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "activity")
public interface ActivityRpcService {

    /**
     * 获取用户红包个数
     * @param userId
     * @param hbtype
     * @param hbstatus
     * @return
     */
    @RequestMapping(value = "/sys/activity/getCountRedPacketsByUserId", method = RequestMethod.POST)
    public int getCountRedPacketsByUserId(@RequestParam("userId") String userId,
                                          @RequestParam("hbtype") String hbtype,
                                          @RequestParam("hbstatus")String hbstatus);

    /**
     *  获取用户红包列表  分页
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "/sys/activity/getRpByUserId", method = RequestMethod.POST)
    public List<UserRedPackets> getRpByUserId(@RequestParam("userId") String userId,
                                              @RequestParam("hbtype") String hbtype,
                                              @RequestParam("hbstatus") String hbstatus,
                                              @RequestParam("page") int page);

    /**
     * 发放新手福利券
     * @param userId
     */
    @RequestMapping(value = "/sys/activity/grantXsFlq", method = RequestMethod.POST)
    public void grantXsFlq(@RequestParam("userid") int userId);

    /**
     *根据手机号码获取渠道信息
     * @param phone
     * @return
     */
    @RequestMapping(value = "/sys/activity/getUserChannelByPhone", method = RequestMethod.POST)
    public List<UserChannel> getUserChannelByPhone(@RequestParam("phone") String phone);

    /**
     * 头条注册检测链接回调
     * @param uid
     */
    @RequestMapping(value = "/sys/activity/doRegHeadChannelCallback", method = RequestMethod.POST)
    public void doRegHeadChannelCallback(@RequestParam(JwtToken.UID) String uid);

    /**
     * 2018年活动邀请好友注册送邀请人88元红包
     * @param userId
     */
    @RequestMapping(value = "/sys/activity/newYearGrantRedPackets", method = RequestMethod.POST)
    public void newYearGrantRedPackets(@RequestParam("userid") int userId);

    /**
     * 获取IOS头条渠道
     * @param uid
     * @return
     */
    @RequestMapping(value = "/sys/activity/getIosChannel", method = RequestMethod.POST)
    public String getIosChannel(@RequestParam(JwtToken.UID) String uid);


}
