package com.finace.miscroservice.activity.rpc;

import com.finace.miscroservice.commons.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "user")
public interface UserRpcService {

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserByUserId", method = RequestMethod.GET)
    public User getUserByUserId(@RequestParam("userId") String userId);

    /**
     * 根据邀请人id获取被邀请人列表
     * @param inviter
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserListByInviter", method = RequestMethod.POST)
    public List<User> getUserListByInviter(@RequestParam("inviter") int inviter, @RequestParam("page") int page);


    /**
     *  根据邀请人id获取被邀请人总数
     * @param inviter
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserCountByInviter", method = RequestMethod.POST)
    public int getUserCountByInviter(@RequestParam("inviter") int inviter);



    /**
     *  添加消息
     * @param userId 用户id
     * @param msgCode 消息类型
     * @param topic 标题
     * @param msg 消息
     */
    @RequestMapping(value = "/sys/user/addMsg", method = RequestMethod.POST)
    void addMsg(@RequestParam("userId")Integer userId,@RequestParam("msgCode")Integer msgCode,
                       @RequestParam("topic")String topic,@RequestParam("msg")String msg);

    /**
     * 根据邀请人id 活动时间 获取被邀请人列表
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "/sys/user/getUsersByInviterInTime", method = RequestMethod.POST)
    public List<User> getUsersByInviterInTime(@RequestParam("inviter") int inviter,
                                              @RequestParam("page") int page, @RequestParam("starttime") String starttime,
                                              @RequestParam("endtime") String endtime);


    /**
     * 根据邀请人id 活动时间 获取被邀请人总数
     *
     * @param inviter
     * @return
     */
    @RequestMapping(value = "/sys/user/getUserCountByInviterInTime", method = RequestMethod.POST)
    public int getUserCountByInviterInTime(@RequestParam("inviter") int inviter, @RequestParam("starttime") String starttime,
                                           @RequestParam("endtime") String endtime);

}



