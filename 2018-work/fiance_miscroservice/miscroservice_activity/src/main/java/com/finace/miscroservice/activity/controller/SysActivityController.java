package com.finace.miscroservice.activity.controller;


import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.UserChannelPO;
import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.service.*;
import com.finace.miscroservice.commons.annotation.Auth;
import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.ChannelBanner;
import com.finace.miscroservice.commons.entity.UserChannel;
import com.finace.miscroservice.commons.entity.UserJiangPin;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户积分控制类
 */
@InnerRestController
@Auth
@RequestMapping("/sys/activity/*")
public class SysActivityController extends BaseController {
    private Log logger = Log.getInstance(SysActivityController.class);


    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
    private ChannelBannerService channelBannerService;

    @Autowired
    private UserChannelService userChannelService;

    @Autowired
    private UserHeadlineChannelService userHeadlineChannelService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private UserJiangPinService userJiangPinService;

    @Value("${activity.start.time}")
    private String startTime;

    @Value("${activity.end.time}")
    private String endTime;


    /**
     * 根据手机号码获取
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "getUserChannelByPhone", method = RequestMethod.POST)
    public List<UserChannel> getUserChannelByPhone(@RequestParam("phone") String phone) {
        List<UserChannel> list = new ArrayList<>();
        try {
            List<UserChannelPO> userChannelPOList = userChannelService.getUserChannelByPhone(phone);
            if (null != userChannelPOList && userChannelPOList.size() > 0) {
                for (UserChannelPO userChannelPO : userChannelPOList) {
                    UserChannel userChannel = new UserChannel();
                    BeanUtils.copyProperties(userChannelPO, userChannel);
                    list.add(userChannel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取用户可用红包数量
     *
     * @param userId
     * @param hbtype
     * @param hbstatus
     * @return
     */
    @RequestMapping(value = "getCountRedPacketsByUserId", method = RequestMethod.POST)
    public int getCountRedPacketsByUserId(@RequestParam("userId") String userId,
                                          @RequestParam("hbtype") String hbtype,
                                          @RequestParam("hbstatus") String hbstatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("hbtype", hbtype);
        map.put("hbstatus", hbstatus);

        return userRedPacketsService.getCountRedPacketsByUserId(map);
    }

    /**
     * 获取用户红包列表
     *
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping(value = "getRpByUserId", method = RequestMethod.POST)
    public List<UserRedPackets> getRpByUserId(@RequestParam("userId") String userId,
                                              @RequestParam("userId") String hbtype,
                                              @RequestParam("userId") String hbstatus,
                                              @RequestParam("page") int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("hbtype", hbtype);
        map.put("hbstatus", hbstatus);

        return userRedPacketsService.getRpByUserId(map, page);
    }

    /**
     * 根据红包id获取红包信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getRpById", method = RequestMethod.POST)
    public UserRedPackets getRpById(@RequestParam("id") int id) {

        return userRedPacketsService.getRpById(id);
    }

    /**
     * 红包使用修改红包状态
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateHbStatus", method = RequestMethod.POST)
    public UserRedPackets updateHbStatus(@RequestParam("hbid") String hbid,
                                         @RequestParam("userId") int userId,
                                         @RequestParam("borrowName") String borrowName,
                                         @RequestParam("account") double account) {

        Map<String, Object> map = new HashMap<>();
        map.put("hbid", hbid);
        map.put("userId", userId);
        map.put("borrowName", borrowName);
        map.put("account", account);

        return userRedPacketsService.updateHbStatus(map);
    }

    /**
     * 获取首页banner
     *
     * @param channel
     * @return
     */
    @RequestMapping(value = "getChannelBanner", method = RequestMethod.GET)
    public List<ChannelBanner> getChannelBanner(@RequestParam("channel") String channel) {
        try {
            return channelBannerService.getChannelBanner(channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发放红包
     *
     * @param nid
     * @param userId
     */
    @RequestMapping(value = "grantFlq", method = RequestMethod.GET)
    public void grantFlq(@RequestParam("nid") String nid,
                         @RequestParam("userid") int userId,
                         @RequestParam(value = "inviter", required = false) int inviter) {
        try {
            userRedPacketsService.grantFlq(userId, inviter, nid, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发放新手福利券
     *
     * @param userId
     */
    @RequestMapping(value = "grantXsFlq", method = RequestMethod.POST)
    public void grantXsFlq(@RequestParam("userid") int userId) {
        try {
            userRedPacketsService.grantXsFlq(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2018年活动邀请好友注册送邀请人88元红包
     *
     * @param userId
     */
    @RequestMapping(value = "newYearGrantRedPackets", method = RequestMethod.POST)
    public void newYearGrantRedPackets(@RequestParam("userid") int userId) {
        try {
            //活动时间 2018-02-08 到 2018-02-25
            if (!DateUtils.compareDate(startTime, DateUtils.getNowDateStr()) && DateUtils.compareDate(endTime, DateUtils.getNowDateStr())) {
                userRedPacketsService.newYearGrantRedPackets(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头条注册检测链接回调
     *
     * @param uid
     */
    @RequestMapping(value = "doRegHeadChannelCallback", method = RequestMethod.POST)
    public void doRegHeadChannelCallback(@RequestParam(JwtToken.UID) String uid) {
        List<UserChannelPO> userChannelPOList = userChannelService.getUserChannelByUid(uid);

        if (null != userChannelPOList && userChannelPOList.size() > 0) {
            String imei = userChannelPOList.get(0).getUser_id();
            UserHeadlineChannelPO userHeadlineChannel = userHeadlineChannelService.getByImei(imei, "5");
            if (null != userHeadlineChannel) {
                Map<String, String> toMap = new HashMap<>();
                String url = "http://ad.toutiao.com/track/activate/?callback=?" + userHeadlineChannel.getCallback() + ""
                        + "&muid=" + imei + "&source=td&os=0&event_type=" + 1 + "&conv_time=" + userHeadlineChannel.getTimestamp();
//                String signature = HeadTools.getHmacSHA1(url, HeadTools.HEAD_KEY);
//                signature = Base64.encode(signature);
//                url = url+"&signature="+signature;
                String teString = HttpUtil.doGet(url, "UTF-8");

                JSONObject jObject = JSONObject.parseObject(teString);
                String msString = jObject.getString("msg");
                if ("success".equals(msString)) {
                    logger.info("头条注册激活成功，imei={}, url={}", imei, url);
                    // userHeadlineChannelService.updateStatusByImei(userHeadlineChannel.getImei(), "2");
                }
            }
        }
    }


    /**
     * 获取IOS头条渠道
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "getIosChannel", method = RequestMethod.POST)
    public String getIosChannel(@RequestParam(JwtToken.UID) String uid) {
        String channel = null;
        try {
            List<UserChannelPO> userChannelPOList = userChannelService.getUserChannelByUid(uid);
            for (UserChannelPO userChannelPO : userChannelPOList) {
                if ((!"toutiao".equals(userChannelPO.getChannel())) &&(!"ifengwo".equals(userChannelPO.getChannel()))){
                    channel = userChannelPO.getChannel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取IOS头条渠道，异常信息：e", e);
        }
        return channel;
    }

    /**
     * 获取红包过期前三天给用户发短信
     *
     * @return
     */
    @RequestMapping(value = "getWillExpiredUserId", method = RequestMethod.POST)
    public List<String> getWillExpiredUserId() {
        return userRedPacketsService.getWillExpiredUserId();
    }


    /**
     * 投资送金豆
     *
     * @param userId     用户id
     * @param times 投资标的天数
     * @param amt        投资金额
     */
    @RequestMapping(value = "investGrantGlodBean", method = RequestMethod.POST)
    public void investGrantGlodBean(@RequestParam("userId") String userId,
                                    @RequestParam("times") Integer times,
                                    @RequestParam("amt") Double amt) {
        if (StringUtils.hasEmpty(userId) || amt == null||times==null) {
            logger.warn("用户{}投资{}送金豆,参数错误,times={}", userId, amt, times);
            return;
        }
        creditService.investGrantGlodBean(userId, times, amt);
    }


    /**
     * 添加奖品
     * @param underUser
     * @param userId
     * @param jiangPinName
     * @param addTime
     * @param remark
     * @param code
     * @param isSend
     */
    @RequestMapping(value = "addUserJiangPin",method = RequestMethod.POST)
    public void addUserJiangPin(Integer underUser,Integer userId, String jiangPinName, String addTime, String remark,Integer code, Integer isSend){
        userJiangPinService.addUserAward(underUser,userId,jiangPinName,addTime,remark,code, isSend);
    }

    /**
     * 根据 奖品类型查找奖品
     * @param underUser 被邀请用户id
     * @param userId 用户id
     * @param code 奖品类型
     * @return
     */
    @RequestMapping(value = "findUserJiangPin",method = RequestMethod.POST)
    public List<UserJiangPin> findUserJiangPin(Integer underUser, Integer userId, Integer code){
        List<UserJiangPin> userJiangPinList = new ArrayList<>();
        List<UserJiangPinPO> list = userJiangPinService.findUserAward(underUser,userId,code);
        for (UserJiangPinPO userJiangPinPO : list){
            UserJiangPin userJiangPin= new UserJiangPin();
            BeanUtils.copyProperties(userJiangPinPO,userJiangPin);
            userJiangPinList.add(userJiangPin);
        }
        return userJiangPinList;
    }
}
