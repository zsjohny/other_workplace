package com.finace.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.dao.HongbaoLogDao;
import com.finace.miscroservice.activity.dao.NewhbTypeDao;
import com.finace.miscroservice.activity.dao.UserJiangPinDao;
import com.finace.miscroservice.activity.dao.UserRedPacketsDao;
import com.finace.miscroservice.activity.po.HongbaoLogPO;
import com.finace.miscroservice.activity.po.NewhbTypePO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.po.UserRedPacketsPO;
import com.finace.miscroservice.activity.rpc.BorrowRpcService;
import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.activity.service.UserRedPacketsService;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.MD5Util;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户红包service实现类
 */
@Service
public class UserRedPacketsServiceImpl implements UserRedPacketsService {
    private Log logger = Log.getInstance(UserRedPacketsServiceImpl.class);

    @Autowired
    private UserRedPacketsDao userRedPacketsDao;

    @Autowired
    private HongbaoLogDao hongbaoLogDao;

    @Autowired
    private NewhbTypeDao newhbTypeDao;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private BorrowRpcService borrowRpcService;

    @Autowired
    private UserJiangPinDao userJiangPinDao;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Value("${activity.carnival.start}")
    protected String carnivalStart;
    @Value("${activity.carnival.end}")
    protected String carnivalEnd;

    private final static String USER_CARNIVAL_TIME="%s:carnival:time";

    @Override
    public int getCountRedPacketsByUserId(Map<String, Object> map) {
        return userRedPacketsDao.getCountRedPacketsByUserId(map);
    }

    @Override
    public List<UserRedPackets> getRpByUserId(Map<String, Object> map, int page) {

        return userRedPacketsDao.getRpByUserId(map, page);
    }

    @Override
    public UserRedPackets getRpById(int id) {
        return userRedPacketsDao.getRpById(id);
    }

    @Override
    public List<UserRedPacketsPO> getHbByParam(Map<String, Object> map) {
        return userRedPacketsDao.getHbByParam(map);
    }

    @Override
    public int getCountHbByParam(Map<String, Object> map) {
        return userRedPacketsDao.getCountHbByParam(map);
    }

    @Override
    @Transactional
    public UserRedPackets updateHbStatus(Map<String, Object> map) {
        int hbid = Integer.valueOf(map.get("hbid").toString());
        int userId = (int)map.get("userId");

        UserRedPackets userRedPackets = this.userRedPacketsDao.getRpById(hbid);
        if( null != userRedPackets){
            Map<String, Object> mapData = new HashMap<String, Object>();
            mapData.put("hbid", userRedPackets.getHbid());
            mapData.put("hbstatus", "3");//红包   0-- 红包未使用  2-- 红包已过期 3-- 红包已使用
            //修改 红包未已使用
            this.userRedPacketsDao.updateRedPacketsStatus(mapData);

            //生成使用红包日志没录
            HongbaoLogPO hbl = new HongbaoLogPO();
            hbl.setUserId(userId);
            hbl.setHongbaoMoney(Double.valueOf(userRedPackets.getHbmoney()));
            hbl.setActiveId(hbid); //红包id
            hbl.setRemark("投资"+map.get("borrowName") + map.get("account")+"元, 福利卷"+userRedPackets.getHbid()+"已使用");
            hbl.setAddtime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            // 1--红包  2--加息劵
            if( userRedPackets.getHbtype() == 2 ){
                hbl.setStatus(2);
            }else{
                hbl.setStatus(1);
            }
            //保存红包日志
            hongbaoLogDao.addHongbaoLog(hbl);

            return userRedPackets;
        }
        return null;
    }

    @Override
    public void grantFlq(int userId, int inviter, String nid, double total) throws ParseException {
        this.publicGiveFlq(userId, inviter, nid, total);
    }

    @Override
    @Transactional
    public void grantXsFlq(int userId) throws ParseException {
        String lxid[] = {"341", "342", "343", "344", "345", "351"};
        for (String id : lxid){
            publicGiveFlq(userId, 0, id, 0);
        }
    }

    @Override
    @Transactional
    public void newYearGrantRedPackets(int userId) throws ParseException {
        //送红包
        publicGiveFlq(userId, 0, "364", 0);
        //邀请奖励记录
        UserJiangPinPO userJiangPinPO = new UserJiangPinPO();
        userJiangPinPO.setAddTime(DateUtils.getNowDateStr());
        userJiangPinPO.setUserId(String.valueOf(userId));
        userJiangPinPO.setJiangPinName("88元红包");
        userJiangPinPO.setRemark("88");
        userJiangPinDao.addUserJiangPin(userJiangPinPO);
    }



    /**
     * 发福利券
     * @param userId
     * @param inviter
     * @param nid
     * @throws ParseException 
     */
    private void publicGiveFlq(int userId, Integer inviter, String nid,  double total) throws ParseException{
        NewhbTypePO newhbType = this.newhbTypeDao.getNewhbTypeByLxId(nid);
        logger.info("开始发放红包nid={}", nid);
        if( null != newhbType ){
            String rmk = "发放"+newhbType.getHbname(); //备注信息
            //加息卷
            UserRedPacketsPO urp = new UserRedPacketsPO();
            urp.setHbleixingid(newhbType.getHbleixingid());
            urp.setUserid(userId); //用户
            urp.setHbstartime(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+" 00:00:00"); //生效日期
            urp.setHbendtime(this.getDateByDay(newhbType.getYday())); //结束日期
            urp.setHbstatus(0); //红包未使用
            urp.setFlag(0);	//红包到期消息发送状态
            if( null != inviter){
                urp.setInviter(inviter);
            }

            //保存 红包
            userRedPacketsDao.addUserRedPackets(urp);

            //红包过期消息发送，红包过期消息提现任务消息发送
            if(hbTask(String.valueOf(urp.getHbid()), String.valueOf(userId), urp.getHbendtime())){
                //修改红包消息发送状态
                Map<String, Object> mapData = new HashMap<String, Object>();
                mapData.put("hbid", urp.getHbid());
                mapData.put("flag", 1);//红包到期mq消息状态  0-- 未发送  1-- 已发送
                userRedPacketsDao.updateRedPacketsStatus(mapData);
            }

            //生成红包日志没录
            HongbaoLogPO hbl = new HongbaoLogPO();
            hbl.setUserId(userId);
            hbl.setHongbaoMoney(Double.valueOf(newhbType.getHbmoney()));
            hbl.setActiveId(urp.getHbid()); //加息卷id
            hbl.setRemark(rmk);
            hbl.setAddtime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            hbl.setStatus(0);
            hbl.setUserTotal(String.valueOf(total));
            //保存红包日志
            hongbaoLogDao.addHongbaoLog(hbl);
        }
    }

    /**
     * 红包过期消息发送，红包过期消息提现任务消息发送
     * @param hbid
     * @param userId
     * @param endTime
     * @return
     */
    private Boolean hbTask(String hbid, String userId, String endTime){
        try {
            //发送红包到期任务
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("s m H d M ? y");
            SimpleDateFormat sdf2 = new SimpleDateFormat("d M ? y");
            SimpleDateFormat sdf3 = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
            TimerScheduler timerScheduler = new TimerScheduler();
            JSONObject jsonObject = new JSONObject();

            /** 红包过期处理*/
            timerScheduler.setType(TimerSchedulerTypeEnum.RED_PACKET_ENDED.toNum());
            timerScheduler.setName("timer_end_red_packet"+UUIdUtil.generateUuid());
            timerScheduler.setCron(sdf1.format(sdf.parse(endTime)));
            //timerScheduler.setCron(sdf3.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr())+100), "0")));
            jsonObject.put("type", 1); //1--红包过期  2--红包过期提醒
            jsonObject.put("hbid", hbid);
            jsonObject.put("userId", userId);
            timerScheduler.setParams(JSON.toJSONString(jsonObject));
            //红包过期处理
            mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
            /** 红包过期处理 */

            Date endData = this.getDateBefore(sdf.parse(endTime), 3);
            String cron = "0 0 17 "+sdf2.format(endData);
            Long endday = DateUtils.getDistanceDays(DateUtils.getNowDateStr(),DateUtils.dateStr2(endData));
            String userCronKey = MD5Util.getLowercaseMD5(userId+cron);
            if( userStrHashRedisTemplate.get(userCronKey) == null ){
                /** 红包过期前3天给用户发短信提醒 */
                jsonObject.clear();
                jsonObject.put("type", 2); //1--红包过期  2--红包过期提醒
                jsonObject.put("userId", userId);
                jsonObject.put("hbid", hbid);
                timerScheduler.setName("timer_end_red_packet"+UUIdUtil.generateUuid());
                timerScheduler.setCron(cron);
                logger.info("时间调试"+endday+"-----------"+endTime+"---"+this.getDateBefore(sdf.parse(endTime), 3)+"---"+cron+"------"+JSON.toJSONString(jsonObject));
                timerScheduler.setParams(JSON.toJSONString(jsonObject));
                mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
                userStrHashRedisTemplate.set(userCronKey, "sendSuccess", endday, TimeUnit.DAYS);
                /** 红包过期前3天给用户发短信提醒 */
            }

            return Boolean.TRUE;
        }catch (Exception e){
            logger.error("红包发送，过期任务消息发送失败,异常信息：{}", e);
        }
       return Boolean.FALSE;
    }



    /**
     *  根据天数获取截止日期
     * @param i 天数
     * @return
     */
    private String getDateByDay(int i){
        Date dNow = new Date();   //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DATE, i);
        Date dAfter = new Date(calendar.getTimeInMillis()); //获取i天后的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dAfter)+" 23:59:59";
    }


    /**
     * 获取时间前几天
     * @param d
     * @param day
     * @return
     */
    private Date getDateBefore(Date d, int day) throws Exception{
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }


    @Override
    public Map<String, Object> getInviterCountSumHb(int userId) {
        Map<String, Object> map = new HashMap<>();
        UserRedPackets userRedPackets = userRedPacketsDao.getInviterCountSumHb(userId);
        map.put("money", userRedPackets.getHbmoney() != null ? userRedPackets.getHbmoney() : 0 );
        map.put("count", userRpcService.getUserCountByInviter(userId));
        return map;
    }

    @Override
    public List<Map<String, Object>> getInviterList(int userId, int page) {
        List<Map<String, Object>> rlist = new ArrayList<>();
        List<User> list =  userRpcService.getUserListByInviter(userId, page);
        for (User user : list ){
            Map<String, Object> map = new HashMap<>();
            map.put("phone", TextUtil.hidePhoneNo(user.getPhone()));
            UserRedPackets userRedPackets = userRedPacketsDao.getUserIdInviter(userId, user.getUser_id());
            if( null != userRedPackets){
                map.put("money", userRedPackets.getHbmoney());
                String my = userRedPackets.getHbdetail() != null && userRedPackets.getHbdetail() != "0" ? userRedPackets.getHbdetail() : "--";
                if(userRedPackets.getHbmoney() == 10 ){
                    map.put("content", "首投"+my);
                }else if(userRedPackets.getHbmoney() == 20){
                    map.put("content", "首投"+my);
                }else if(userRedPackets.getHbmoney() == 50){
                    map.put("content", "首投"+my);
                }else if(userRedPackets.getHbmoney() == 80){
                    map.put("content", "首投"+my);
                }else if(userRedPackets.getHbmoney() == 150){
                    map.put("content", "首投"+my);
                }
            }else{
                Double firstAmt = borrowRpcService.getUserFirstBuyAmt(user.getUser_id());
                map.put("content", firstAmt != 0 ? "首投"+firstAmt : "未投资");
                map.put("money", "0");
            }
            rlist.add(map);
        }
        return rlist;
    }

    @Override
    public void updateUserRedPacketsEnded(String hbid) {
        if(null != hbid) {
            Map<String, Object> mapData = new HashMap<String, Object>();
            mapData.put("hbid", hbid);
            mapData.put("hbstatus", 2);//红包未使用已过期  0-- 红包未使用  2-- 红包已过期 3-- 红包已使用
            //修改 未使用并且已到期 的红包状态
            userRedPacketsDao.updateRedPacketsStatus(mapData);
        }
    }

    @Override
    public List<UserRedPackets> getEndedUserRedPackets() {
        return userRedPacketsDao.getEndedUserRedPackets();
    }

    @Override
    public void updateUserRedPacketsFlag(UserRedPackets userRedPackets) {
        if(null != userRedPackets) {
            Map<String, Object> mapData = new HashMap<String, Object>();
            mapData.put("hbid", userRedPackets.getHbid());
            mapData.put("flag", 1);//红包到期mq消息状态  0-- 未发送  1-- 已发送
            //修改 未使用并且已到期 的红包状态
            userRedPacketsDao.updateRedPacketsStatus(mapData);
        }
    }

    @Override
    public List<String> getWillExpiredUserId() {
        return userRedPacketsDao.getWillExpiredUserId();
    }

    @Override
    public Response hongbao(Map<String, Object> map, int page) {
        List<UserRedPackets> list = userRedPacketsDao.getRpByUserId(map, page);

        Iterator<UserRedPackets> ib = list.iterator();
        while (ib.hasNext()) {
            UserRedPackets userRedPackets = (UserRedPackets) ib.next();
            if(userRedPackets.getHbstatus() != 0 && DateUtils.getDistanceTimes(userRedPackets.getHbendtime(), DateUtils.getNowDateStr())[0] > 7){
                ib.remove();
            }
        }

        return Response.success(list);
    }

    @Override
    public Response addCarnival(Integer code, String userId) {
        //当前时间
        String now = DateUtils.getNowDateStr();
        if (DateUtils.compareDate(carnivalStart,now)){
            logger.warn("当前时间小于全面狂欢活动开始时间");
            return Response.errorMsg("当前活动未开始");
        }
        if (DateUtils.compareDate(now,carnivalEnd)){
            logger.warn("当前时间大于全面狂欢活动结束时间");
            return Response.errorMsg("当前活动已结束");
        }
        if (code==null){
            logger.warn("红包类型为空");
            return Response.errorMsg("红包类型不能为空");
        }
        String saveCode = getCarnival(userId);
        if (StringUtils.isNotEmpty(saveCode)){
            logger.warn("该用户userId = {} 今日已领取过红包",userId);
            return Response.errorMsg("该用户今日已领取过红包");
        }
        //第二天凌晨 清零 用户领红包存储
        userStrHashRedisTemplate.set(String.format(USER_CARNIVAL_TIME,userId),String.valueOf(code),DateUtils.calSec(), TimeUnit.SECONDS);

        String nid =null;
        switch (code){
            case 1:
                nid = "426";
                break;
            case 2:
                nid = "428";
                break;
            case 3:
                nid = "430";
                break;
            case 4:
                nid = "427";
                break;
            case 5:
                nid = "429";
                break;
            case 6:
                nid = "431";
                break;
            default:
                logger.warn("code = {}红包类型错误",code);
                return Response.errorMsg("红包类型错误");
        }
        try {
            grantFlq(Integer.valueOf(userId),0,nid,0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Response.success();
    }

    @Override
    public String getCarnival(String userId) {

        //第二天凌晨 清零 用户领红包存储
        String code =  userStrHashRedisTemplate.get(String.format(USER_CARNIVAL_TIME,userId));
        return code;


    }
}
