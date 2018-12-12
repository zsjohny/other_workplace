package com.finace.miscroservice.borrow.rpc;

import com.finace.miscroservice.commons.entity.ChannelBanner;
import com.finace.miscroservice.commons.entity.UserJiangPin;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "ACTIVITY")
public interface ActivityRpcService {
    /**
     * 根据红包id获取红包信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sys/activity/getRpById", method = RequestMethod.POST)
    public UserRedPackets getRpById(@RequestParam("id") int id);

    /**
     * 红包使用修改红包状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/sys/activity/updateHbStatus", method = RequestMethod.POST)
    public UserRedPackets updateHbStatus(@RequestParam("hbid") String hbid,
                                         @RequestParam("userId") int userId,
                                         @RequestParam("borrowName") String borrowName,
                                         @RequestParam("account") double account);

    /**
     * @param channel
     * @return
     */
    @RequestMapping(value = "/sys/activity/getChannelBanner", method = RequestMethod.GET)
    public List<ChannelBanner> getChannelBanner(@RequestParam("channel") String channel);


    /**
     * 获取红包过期前三天给用户发短信
     *
     * @return
     */
    @RequestMapping(value = "/sys/activity/getWillExpiredUserId", method = RequestMethod.POST)
    public List<String> getWillExpiredUserId();


    /**
     * 投资送金豆
     *
     * @param userId     用户id
     * @param times 投资标的天数
     * @param amt        投资金额
     */
    @RequestMapping(value = "/sys/activity/investGrantGlodBean", method = RequestMethod.POST)
    public void investGrantGlodBean(@RequestParam("userId") String userId,
                                    @RequestParam("times") Integer times,
                                    @RequestParam("amt") Double amt);

    /**
     * 添加奖品
     * @param userId
     * @param underUser
     * @param jiangPinName
     * @param addTime
     * @param remark
     * @param code
     */
    @RequestMapping(value = "/sys/activity/addUserJiangPin",method = RequestMethod.POST)
    public void addUserJiangPin(@RequestParam("underUser") Integer underUser,@RequestParam("userId") Integer userId, @RequestParam("jiangPinName") String jiangPinName,
                                @RequestParam("addTime")String addTime, @RequestParam("remark")String remark,@RequestParam("code")Integer code,@RequestParam("isSend")Integer isSend);

    /**
     * 根据 奖品类型查找奖品
     * @param underUser 被邀请用户id
     * @param userId 用户id
     * @param code 奖品类型
     * @return
     */
    @RequestMapping(value = "/sys/activity/findUserJiangPin",method = RequestMethod.POST)
    public List<UserJiangPin> findUserJiangPin(@RequestParam("underUser")Integer underUser, @RequestParam("userId")Integer userId, @RequestParam("code")Integer code);
}
