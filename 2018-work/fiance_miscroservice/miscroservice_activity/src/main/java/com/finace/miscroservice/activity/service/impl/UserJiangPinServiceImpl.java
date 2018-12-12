package com.finace.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.dao.UserJiangPinDao;
import com.finace.miscroservice.activity.po.GiftPO;
import com.finace.miscroservice.activity.po.InvitationPO;
import com.finace.miscroservice.activity.po.MyFriendsPO;
import com.finace.miscroservice.activity.po.UserJiangPinPO;
import com.finace.miscroservice.activity.rpc.BorrowRpcService;
import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.activity.service.UserJiangPinService;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.enums.ActiveGiftEnums;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.TimeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserJiangPinServiceImpl implements UserJiangPinService {

    Log logger = Log.getInstance(UserJiangPinServiceImpl.class);
    @Autowired
    private UserJiangPinDao userJiangPinDao;
    @Resource
    private UserRpcService userRpcService;
    @Resource
    private BorrowRpcService borrowRpcService;

    @Value("${activity.start.time}")
    private String starttime;
    @Value("${activity.end.time}")
    private String endtime;

    @Override
    @Transactional
    public void addUserJiangPin(UserJiangPinPO userJiangPinPO) {
        this.userJiangPinDao.addUserJiangPin(userJiangPinPO);
    }

    @Override
    public List<UserJiangPinPO> getUserJiangPinByUserId(String userId) {
        return userJiangPinDao.getUserJiangPinByUserId(userId);
    }

    @Override
    public List<UserJiangPinPO> getAllUserJiangPin() {
        Map<String, Object> map = new HashMap<>();
        return userJiangPinDao.getAllUserJiangPin();
    }

    @Override
    public Map<String, Object> rewardHome(String userId) {
        Map<String, Object> map = new HashMap<>();
        List<UserJiangPinPO> rlist = userJiangPinDao.getUserJplx(userId);
        for (UserJiangPinPO userJiangPinPO : rlist){
            if("88".equals(userJiangPinPO.getRemark())){
                map.put("invitationNum", userJiangPinPO.getId());
            }else if("20".equals(userJiangPinPO.getRemark())){
                map.put("bill", Integer.valueOf(userJiangPinPO.getId()) * 20 );
            }else if("50".equals(userJiangPinPO.getRemark())){
                map.put("fiftyJdk", Integer.valueOf(userJiangPinPO.getId()) * 50 );
            }else if("100".equals(userJiangPinPO.getRemark())){
                map.put("ohJdk", userJiangPinPO.getId());
            }
        }
        return map;
    }

    @Override
    @Transactional
    public void addUserAward(Integer underUser,Integer userId, String jiangPinName, String addTime, String remark, Integer code, Integer isSend) {
        userJiangPinDao.addUserAward(underUser,userId,jiangPinName,addTime,remark,code,isSend);
    }

    @Override
    public List<UserJiangPinPO> findUserAward(Integer underUser,Integer userId, Integer code) {
        return userJiangPinDao.findUserAward(underUser,userId,code);
    }

    @Override
    public JSONObject findUserAwards(int userId) {
        logger.info("用户={}获取我的奖励",userId);
        //累计获取红包数量
        List<UserJiangPinPO>  userJiangPinPOList = userJiangPinDao.findUserAward(null,userId, ActiveGiftEnums.SING_UP_GIRT.getCode());
        //根据邀请人id获取被邀请人数量
        Integer invitations = userRpcService.getUserCountByInviter(userId);
        //京东金额数量
        Integer jdCardMoneyAmt = userJiangPinDao.findJdCardMoneyAmtByUserId(userId);

        //佣金奖
        List<GiftPO> list= userJiangPinDao.findUserAwards(userId,ActiveGiftEnums.INVITATION_GIRT.getCode(),starttime,endtime);
        //荐面奖
        List<GiftPO> list2= userJiangPinDao.findUserAwards(userId,ActiveGiftEnums.SING_UP_GIRT.getCode(),starttime,endtime);
        //人脉奖
        List<GiftPO> list3= userJiangPinDao.findUserAwards(userId,ActiveGiftEnums.TEAM_GIRT.getCode(),starttime,endtime);

        Map<String,GiftPO> map = new HashMap<>();
        List list1 = new ArrayList();
          for (int i =0;i<list.size();i++){
              GiftPO giftPO = list.get(i);
                 //若该手机已存入 map 则 获取该信息
                  GiftPO mapPo = map.get(giftPO.getPhone());
                  if (mapPo!=null){
                     //添加相同 手机号的Send
                     List<String> li  = mapPo.getSend();
                     li.add(giftPO.getIsSend());
                     mapPo.setSend(li);
                     //重新 存入map中
                     map.put(giftPO.getPhone(),mapPo);
                 }else {
                     //默认添加 Send
                     List<String> li = new ArrayList<>();
                     li.add(giftPO.getIsSend());
                     giftPO.setSend(li);
                     map.put(giftPO.getPhone(),giftPO);
                  }

                  list1.add(giftPO);
          }
          list.removeAll(list1);

         for (GiftPO giftPO : map.values()){
                if (giftPO.getSend().size()==1){
                    giftPO.setJiangPinName("100元京东卡");
                }
                 if (giftPO.getSend().size()==2){
                     giftPO.setJiangPinName("200元京东卡");
                 }
                 if (giftPO.getSend().size()==3){
                     giftPO.setJiangPinName("300元京东卡");
                 }
              list.add(giftPO);
         }
         //添加荐面奖
         list.addAll(list2);
         //若 邀请人数满足 3 - 6 - 9 人  则添加
        logger.info("人脉奖获得数量={}",list3.size());
         if (list3.size()>=3&&list3.size()<6){
             list.addAll(list3.subList(0,3));
             jdCardMoneyAmt=jdCardMoneyAmt+200;
         }else if(list3.size()>=6&&list3.size()<9){
             list.addAll(list3.subList(0,6));
             jdCardMoneyAmt=jdCardMoneyAmt+400;
         }else if (list3.size()>=9){
             list.addAll(list3.subList(0,9));
             jdCardMoneyAmt=jdCardMoneyAmt+600;
         }
        list.stream().forEach(giftPO ->
                giftPO.setMoney(String.valueOf(borrowRpcService.getUserAmtMoneyInTime(Integer.parseInt(giftPO.getUnderUser()),starttime,endtime))));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userJiangPinPOList",userJiangPinPOList.size());
        jsonObject.put("invitations",invitations);
        jsonObject.put("jdCardMoneyAmt",jdCardMoneyAmt);
        jsonObject.put("list",list);
        return jsonObject;
    }

    @Override
    public JSONObject findMyFriends(String userId, Integer page) {
         Integer size = userRpcService.getUserCountByInviterInTime(Integer.parseInt(userId),String.valueOf(DateUtils.getTime(starttime)),String.valueOf(DateUtils.getTime(endtime)));
         List<User> userList = userRpcService.getUsersByInviterInTime(Integer.parseInt(userId),page,String.valueOf(DateUtils.getTime(starttime)),String.valueOf(DateUtils.getTime(endtime)));
        List<MyFriendsPO> myFriendsPOS = new ArrayList<>();
        for (User user:userList){
            Double money = borrowRpcService.getUserFirstBuyAmt(user.getUser_id());
            String type;
            if (money!=null && money>0d){
                type="已出借";
            }else {
                type="未出借";

            }
            myFriendsPOS.add(new MyFriendsPO(user.getPhone(),user.getAddtime(),type));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",myFriendsPOS);
        jsonObject.put("total",size);
        return jsonObject;

    }

    @Override
    public InvitationPO getInvitations(Integer userId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Long start =null;
        Long end =null;
        try {
            start = df.parse(starttime).getTime()/1000;
            end = df.parse(endtime).getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        System.out.println(start+"<-->"+end);
        InvitationPO invitationPO = userJiangPinDao.getInvitations(userId,String.valueOf(start),String.valueOf(end));
        return invitationPO;
    }

    public static void main(String[] args) {
       JSONObject jsonObject = new JSONObject();
       jsonObject.put("list",new ArrayList<>());
        jsonObject.put("total",1);
        System.out.println(jsonObject);
//        2018-04-15 23:59:59
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Long start =null;
        Long end =null;
        try {
            start = df.parse("2018-04-15 23:59:59").getTime()/1000;
            end = df.parse("2018-04-15 23:59:59").getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(start);
        System.out.println(end);

    }

}
