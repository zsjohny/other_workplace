package com.e_commerce.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.activity.dao.ShopSignDao;
import com.e_commerce.miscroservice.activity.rpc.impl.DistributionRpcService;
import com.e_commerce.miscroservice.activity.service.ShopSignService;
import com.e_commerce.miscroservice.activity.utils.StringUtils;
import com.e_commerce.miscroservice.commons.entity.application.activity.Sign;
import com.e_commerce.miscroservice.commons.entity.application.activity.SignLog;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccount;
import com.e_commerce.miscroservice.commons.entity.application.distributionSystem.ShopMemberAccountCashOutIn;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
@Service
public class ShopSignServiceImpl implements ShopSignService {
    private Log logger = Log.getInstance(ShopSignServiceImpl.class);
    @Resource
    private ShopSignDao shopSignDao;
    @Autowired
    private DistributionRpcService distributionRpcService;

    /**
     * 签到
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response doSign(Long userId, Integer conDay) {
//        Integer isSign = 0;
        Integer isSign = shopSignDao.getNowSignByUser(userId);
        if (isSign > 0) {
            logger.warn("用户{},今日已签到，不能重复签到", userId);
            return Response.errorMsg("今日已签到，不能重复签到");
        }else {
            try {
                Sign oSgin = shopSignDao.getSignByUser(userId);
                Integer conNum = 1; //天数
                Integer num = 1; //天数
                if (null != oSgin) {
                    String fdate = TimeUtils.dateFormatString(TimeUtils.rollDate(new Date(), 0,0,conDay-1));
//                    String fdate = TimeUtils.dateFormatString(TimeUtils.rollDate(new Date(), 0,0,-1));
//                    String toDay = TimeUtils.dateFormatString(new Date());
                    String toDay = TimeUtils.dateFormatString(TimeUtils.rollDate(new Date(), 0,0,conDay));
                    String ddate = TimeUtils.secondToStringDate(oSgin.getAddDate());
                    //这次签到和上次签到时间是否在同一个月，在一个月并且在上次签到时间的后一天，就认为是连续签到
                    if (TimeUtils.dateEqMonth(toDay, ddate) && fdate.equals(ddate)) {
                        num = oSgin.getNumber()+num;
                        //当月连续签到天数
                        oSgin.setNumber(num);

                    } else {
                        //当月连续签到天数
                        oSgin.setNumber(1);

                    }
                    if (fdate.equals(ddate)){
                        conNum = oSgin.getContinueNumber() + conNum;
                        //最大连续签到天数
                        oSgin.setContinueNumber(oSgin.getContinueNumber()+1);
                    }else {
                        //最大连续签到天数
                        oSgin.setContinueNumber(1);
                    }

                    //签到总天数
                    oSgin.setTotalNumber(oSgin.getTotalNumber() + 1);
                    //签到时间
                    oSgin.setAddDate(TimeUtils.dateFormatSecond(TimeUtils.rollDate(new Date(), 0,0,conDay)));
//                    oSgin.setAddDate(TimeUtils.getNowTimeStr());
                    oSgin.setId(null);
                    shopSignDao.updateSign(oSgin);
                } else {
                    Sign sign = new Sign();
                    sign.setUserId(userId);
                    //签到总天数
                    sign.setTotalNumber(1);
                    //签到时间
                    sign.setAddDate(TimeUtils.dateFormatSecond(TimeUtils.rollDate(new Date(), 0,0,conDay)));
//                    sign.setAddDate(TimeUtils.getNowTimeStr());
                    //当月连续签到天数
                    sign.setNumber(1);
                    //最大连续签到天数
                    sign.setContinueNumber(1);
                    shopSignDao.saveSign(sign);
                }

                //送金币
                BigDecimal getCoin = this.giveCoin(userId, conNum);
                //新增用户签到日志
                SignLog sginLog = new SignLog();
                sginLog.setUserId(userId);
                sginLog.setAddDate(TimeUtils.dateFormatSecond(TimeUtils.rollDate(new Date(), 0,0,conDay)));
//                sginLog.setAddDate(TimeUtils.getNowTimeStr());
                sginLog.setRemark(getCoin + "".trim());
                this.shopSignDao.saveSignLog(sginLog);

                logger.info("用户{},今日签到成功", userId);

                return Response.successDataMsg(getCoin, "签到成功");
            } catch (Exception e) {
                logger.error("用户{},签到失败,异常信息{}", userId, e);
                return Response.errorMsg("签到失败");
            }
        }
    }
    /**
     * 当月签到日期
     * @param userId
     * @return
     */
    @Override
    public Response showDays(Long userId) {
        Map<String, Object> map = new HashMap<>();
        List<String> days = shopSignDao.getSignLogMonthByUser(userId);
        DataDictionary dataDictionary = shopSignDao.getDataDictionary(DataDictionaryEnums.SIGN_DATA_COIN.getCode(),DataDictionaryEnums.SIGN_DATA_COIN.getGroupCode());
        DataDictionary award = shopSignDao.getDataDictionary(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getCode(),DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getGroupCode());
        JSONObject jsonObject = JSONObject.parseObject(award.getComment());
        JSONArray dayArray = jsonObject.getJSONArray("day");
        JSONArray conditionArray = jsonObject.getJSONArray("condition");
        map.put("conditionArray",conditionArray);
        String config = dataDictionary.getComment ();
        String[] strings = StringUtils.isBlank (config) ? new String[]{"0"} :config.split(",");
        List<ShopMemberAccountCashOutIn> list = distributionRpcService.findPeriodicalPrizeMonthLog(userId);
        if (list!=null&&list.size()>0){
            List  getDays = new ArrayList();
            for (ShopMemberAccountCashOutIn in : list){
                if (in.getRemark()!=null){
                    getDays.add(Integer.parseInt(in.getRemark()));
                }
            }
            dayArray.removeAll(getDays);
        }
        map.put("dayAward",dayArray);
//        JSONObject jsonObject = JSONObject.parseObject(dataDictionary.getComment());
//        JSONArray prizeArray = jsonObject.getJSONArray("prize");
//        Double getPrize = prizeArray.getDouble(days.size()-1);
        map.put("days", days);
        ShopMemberAccount shopMemberAccount = distributionRpcService.findByUser(userId);
        if( null != shopMemberAccount ){
            //可用金币
            map.put("coin", shopMemberAccount.getAliveGoldCoin());
            map.put("signCoin", shopMemberAccount.getSignGoldCoin());
        }else{
            map.put("coin", BigDecimal.ZERO);
            map.put("signCoin", BigDecimal.ZERO);
        }
        Integer lastSignDay = 0;
        if (days==null||days.size()<=0){
            lastSignDay=0;
        }else{
            lastSignDay = Integer.valueOf(days.get(days.size()-1));
        }

        Integer today = TimeUtils.getYearOrMonthOrDay(new Date(),5);
        Integer monthNumber = 0;
        if (today.equals(lastSignDay+1)||today.equals(lastSignDay)){
            monthNumber=-1;
        }
        Sign sign = shopSignDao.getSignByUser(userId);
        logger.info ("签到 monthNumber={}, sign.isNull", monthNumber, sign == null);
        if( null != sign ){
            if (monthNumber==-1){
                monthNumber =  sign.getNumber();
            }
            //当月连续签到天数
            map.put("monthNumber", monthNumber);
            //总连续签到天数
            map.put("continueNumber", sign.getContinueNumber());
            //总签到天数
            map.put("totalNumber", sign.getTotalNumber());
        }else{
            //当月连续签到天数
            map.put("monthNumber",0);
            //总连续签到天数
            map.put("continueNumber", 0);
            //总签到天数
            map.put("totalNumber", 0);
        }
        Integer len = 0;
        if (monthNumber<=0){
            len=0;
        }else if (monthNumber<strings.length){
            len=monthNumber;
        }else {
            len=strings.length-1;
        }
        Double coin = Double.parseDouble(strings[len]);
        map.put("getPrize", coin);

        return Response.success(map);
    }
    /**
     * 金币记录
     * @param page
     * @param userId
     * @return
     */
    @Override
    public Response getGoldCoinLog(Integer page, Long userId) {
        if (page==null||page<0){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        List<ShopMemberAccountCashOutIn>  shopMemberAccountCashOutIn = distributionRpcService.findLogList(page,userId);
        return Response.success(shopMemberAccountCashOutIn);
    }




    /**
     * 获取签到阶段奖励
     * @param userId
     * @return
     */
    @Override
    public Response getPeriodicalPrize(Long userId, Integer num) {
        logger.info("获取签到阶段奖励userId={},num={}",userId,num);
        DataDictionary dataDictionary = shopSignDao.getDataDictionary(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getCode(),DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getGroupCode());
        String comment = dataDictionary.getComment ();
        logger.info ("签到配置 {}", comment);
        JSONObject jsonObject = JSONObject.parseObject(comment);
        JSONArray dayArray = jsonObject.getJSONArray("day");
        JSONArray conditionArray = jsonObject.getJSONArray("condition");
        JSONArray prizeArray = jsonObject.getJSONArray("prize");
        Integer getDaySize =  -1;
        String numStr = num + "";
        for (int i = 0;i<dayArray.size();i++){
            //领取日期相等
            if (dayArray.get(i).equals(numStr)){
                getDaySize=i;
            }
        }
        if (getDaySize==-1){
            logger.warn("领取失败");
            return Response.errorMsg("领取失败");
        }
        //获取奖励的日期
        Integer getPrizeDay = dayArray.getInteger(getDaySize);
        //获取奖励获取条件
        Integer getCondition = conditionArray.getInteger(getDaySize);
        //获取奖励
        Double getPrize = prizeArray.getDouble(getDaySize);
        Sign sign = shopSignDao.getSignByUser(userId);
        SignLog signLog = shopSignDao.getSignLogLimitDescByUser(userId);
        Integer day = 0;
        if (signLog!=null&&signLog.getAddDate()!=null){
            day = TimeUtils.getYearOrMonthOrDay(TimeUtils.getDate(signLog.getAddDate()),5);
        }
        List<ShopMemberAccountCashOutIn> list = distributionRpcService.findPeriodicalPrizeMonthLog(userId);
        if (list.size()>=dayArray.size()){
            logger.warn("本月奖励已领取完毕");
            return Response.errorMsg("本月奖励已领取完毕");
        }else {
            for (ShopMemberAccountCashOutIn in : list){
                if (Integer.parseInt(in.getRemark())==num){
                    logger.warn("该奖励已领取");
                    return Response.errorMsg("该奖励已领取");
                }
            }
        }
        List<String> days = shopSignDao.getSignLogMonthByUser(userId);
        if (day>getPrizeDay&&(days==null||days.size()<getCondition)){
            logger.warn("该奖励已过期");
            return Response.errorMsg("该奖励已过期");
        }
        if (days==null||days.size()<getCondition||Integer.parseInt(days.get(getCondition-1))>getPrizeDay){
            logger.warn("连续签到天数不足，无法领取");
            return Response.errorMsg("连续签到天数不足，无法领取");
        }
        if (sign!=null&&sign.getNumber()>=getCondition&&day>=getPrizeDay&&days!=null){

            ShopMemberAccount account = distributionRpcService.findByUser(userId);
//            ShopMemberAccount shopMemberAccount = new ShopMemberAccount();
            BigDecimal prize = new BigDecimal(getPrize);
            BigDecimal aliveGoldCoin = account.getAliveGoldCoin();
            BigDecimal allGoldCoin = account.getAllGoldCoin();
            BigDecimal signGoldCoin = account.getSignGoldCoin();
            BigDecimal historyGoldCoinEarning = account.getHistoryGoldCoinEarning ();

//            shopMemberAccount.setAliveGoldCoin(prize.add(aliveGoldCoin));
//            shopMemberAccount.setAllGoldCoin(prize.add(allGoldCoin));
//            shopMemberAccount.setSignGoldCoin(prize.add(signGoldCoin));
            distributionRpcService.updateShopMemberAccountByUserId(prize.add(aliveGoldCoin),prize.add(allGoldCoin),prize.add(signGoldCoin),prize.add (historyGoldCoinEarning), userId);
//            ShopMemberAccountCashOutIn shopMemberAccountCashOutIn = new ShopMemberAccountCashOutIn();
//            shopMemberAccountCashOutIn.setOperTime(System.currentTimeMillis());
//            shopMemberAccountCashOutIn.setInOutType(1);
//            shopMemberAccountCashOutIn.setUserId(userId);
//            shopMemberAccountCashOutIn.setStatus(2);
//            shopMemberAccountCashOutIn.setOperGoldCoin(prize);
//            shopMemberAccountCashOutIn.setOriginalGoldCoin(aliveGoldCoin);
//            shopMemberAccountCashOutIn.setType(21);
////            String remark = "连续签到"+getPrizeDay+"天，送" + getPrize + "奖励金币";
            String remark = String.valueOf(num);
//            shopMemberAccountCashOutIn.setRemark(remark);
            distributionRpcService.saveAccountLog(System.currentTimeMillis(),1,userId,2,2,prize,aliveGoldCoin,21,remark);


            return Response.success(getPrize);
        }

        return Response.errorMsg("未满足领取条件");
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(5);
        list.add(5);
        jsonObject.put("day",list);
        jsonObject.put("condition",list);
        jsonObject.put("prize",list);
//        {"condition":[5,10,15],"day":[5,15,25],"prize":[3,5,7]}
        System.out.println(jsonObject);
    }
    /**
     * 签到 获取奖励条件
     * 连续签到 签到奖励递增 （可配置 规则）
     * 阶段奖 连续签到 可获得 ，固定在 每月的 5  15  25
     *      5号 连续签到 5天 且 当前签到日期大于等于5号
     *      15号 连续签到 10天且当前签到日期大于等于15号
     *      25号 连续签到 15天且当前签到日期大于等于25号
     */
    public BigDecimal giveCoin(Long userId, Integer conNum){

        DataDictionary value = shopSignDao.getDataDictionary(DataDictionaryEnums.SIGN_DATA_COIN.getCode(),DataDictionaryEnums.SIGN_DATA_COIN.getGroupCode());
        String goldCoinConfig = value.getComment ();
        if (StringUtils.isBlank (goldCoinConfig)) {
            //没有金币配置
            logger.info ("签到送金币, 没有设置奖励");
            return BigDecimal.ZERO;
        }

        String[] strings = goldCoinConfig.split(",");
        Integer len = strings.length;
        BigDecimal coin = BigDecimal.ZERO;
//        Double totalJd = 0d;
        if (conNum<=len){
            Integer num = conNum-1;
//            coin = Double.parseDouble(strings[num]);
            coin = new BigDecimal (strings[num]);
        }else {
//            coin = Double.parseDouble(strings[len-1]);
            coin = new BigDecimal (strings[len-1]);
        }
        String remark = "连续签到" + conNum + "天，送" + coin.toString () + "金币";
        ShopMemberAccount shopMemberAccount = distributionRpcService.findByUser(userId);

//        ShopMemberAccountCashOutIn log = new ShopMemberAccountCashOutIn();
        BigDecimal aliveGoldCoin=null;
        if (null == shopMemberAccount) {
            //                      totalJd=coin;
//            ShopMemberAccount credit = new ShopMemberAccount();
//            credit.setUserId(Long.valueOf(userId));
//            credit.setAliveGoldCoin(new BigDecimal(coin));
            distributionRpcService.saveShopMemberAccount(userId,coin);
            aliveGoldCoin=BigDecimal.ZERO; //原金币
        } else {
            distributionRpcService.updateShopMemberAccountByUserId(
//                    new BigDecimal(coin).add(shopMemberAccount.getAliveGoldCoin()),
//                    new BigDecimal(coin).add(shopMemberAccount.getAllGoldCoin()),
//                    new BigDecimal(coin).add(shopMemberAccount.getSignGoldCoin()),
//                    new BigDecimal(coin).add(shopMemberAccount.getHistoryGoldCoinEarning ()),
                    coin.add(shopMemberAccount.getAliveGoldCoin()),
                    coin.add(shopMemberAccount.getAllGoldCoin()),
                    coin.add(shopMemberAccount.getSignGoldCoin()),
                    coin.add(shopMemberAccount.getHistoryGoldCoinEarning ()),
                    userId);
            //原可用金币
            aliveGoldCoin=shopMemberAccount.getAliveGoldCoin();
            //                      totalJd = coin +shopMemberAccount.getAliveGoldCoin().doubleValue() ;
        }
//        log.setUserId(userId);
//        log.setRemark(remark);
//        log.setOperTime(System.currentTimeMillis());
//        log.setStatus(2);
//        log.setOperGoldCoin(new BigDecimal(coin));
//        log.setType(20);
//        log.setInOutType(1);
        distributionRpcService.saveAccountLog(System.currentTimeMillis(),1,userId,2,2,coin,aliveGoldCoin,20,remark);
        return coin;
    }
}
