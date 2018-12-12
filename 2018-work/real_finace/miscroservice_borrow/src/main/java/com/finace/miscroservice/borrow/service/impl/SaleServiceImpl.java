package com.finace.miscroservice.borrow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;

import com.finace.miscroservice.commons.enums.ActiveGiftEnums;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.SaleService;
import com.finace.miscroservice.borrow.service.FuiouH5PayService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouBinCardQueryService;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.enums.PushExtrasEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.JiguangPush;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.github.pagehelper.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.*;
import static com.finace.miscroservice.commons.utils.Constant.SERVICE_PHONE;

@Service
public class SaleServiceImpl implements SaleService {
	private Log logger = Log.getInstance(SaleServiceImpl.class);

	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyMMddHHmmss");
	private ApplicationEventPublisher applicationEventPublisher;
	private static final Logger LOG = Logger.getLogger(SaleServiceImpl.class);
	private static long MINITE10 = 600;

	@Autowired
	private FinanceBidDao financeBidDao;

	@Autowired
	private BorrowDao borrowDao;

	@Autowired
	private FuiouBinCardQueryService fuiouBinCardQueryService;

	@Autowired
	private UserRpcService userRpcService;

	@Autowired
	private ActivityRpcService activityRpcService;

	@Autowired
	@Lazy
	private MqTemplate mqTemplate;

	@Value("${borrow.pay.timeout}")
	private String timeout;

	@Value("${borrow.acitve.starttime}")
    private String starttime;

	@Value("${borrow.acitve.endtime}")
    private String endtime;

	@Autowired
	@Qualifier("userStrHashRedisTemplate")
	private ValueOperations<String, String> userStrHashRedisTemplate;

	@Override
	@Transactional
	public FinanceBidPO makeOrder(Param orderParam, String version) {

		FinanceBidPO financeBid = new FinanceBidPO();
		financeBid.setOrderSn(Long.toString(orderParam.getUserId()) + yyyyMMddHHmmss.format(new Date()));
		financeBid.setBorrowId(orderParam.getBorrowId());
		financeBid.setBuyAmt(orderParam.getBuyAmt());
		financeBid.setUserId(orderParam.getUserId());
		financeBid.setRate(orderParam.getApr());
		financeBid.setPayName(orderParam.getName());
		financeBid.setPayPid(orderParam.getPid());
		financeBid.setBankCardNo(orderParam.getBankCard());
		financeBid.setPayChannel("fuiou");
		if( orderParam.getRegChannel() != null ){
			financeBid.setRegChannel(orderParam.getRegChannel());
		}
		if( orderParam.getHbid() != null ){
			financeBid.setHbid(orderParam.getHbid());
		}
		if( orderParam.getChannel() != null ){
			financeBid.setChannel(orderParam.getChannel());
		}

		int istrue = financeBidDao.addFinanceBid(financeBid);
		if( istrue > 0 ){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
				TimerScheduler timerScheduler = new TimerScheduler();
				//订单是否成功处理
				timerScheduler.setType(TimerSchedulerTypeEnum.ORDER_FAILURE_INSPECT.toNum());
				timerScheduler.setName("order_failure_inspect" + UUIdUtil.generateUuid());
				timerScheduler.setCron(sdf.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr()) + Integer.valueOf(timeout) + 20), "0")));
				timerScheduler.setParams(financeBid.getOrderSn()+"##"+version);
				mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));
			}catch (Exception e){
				logger.error("订单{},失效消息发送发送失败，异常信息{}", financeBid.getOrderSn(), e);
			}
		}

		return financeBid;
	}


	@Override
	@Transactional
	public FinanceBidPO makeAgreeOrder(Param orderParam, String version) {

		FinanceBidPO financeBid = new FinanceBidPO();
		financeBid.setOrderSn(Long.toString(orderParam.getUserId()) + yyyyMMddHHmmss.format(new Date()));
		financeBid.setBorrowId(orderParam.getBorrowId());
		financeBid.setBuyAmt(orderParam.getBuyAmt());
		financeBid.setUserId(orderParam.getUserId());
		financeBid.setRate(orderParam.getApr());
		financeBid.setPayName(orderParam.getName());
		financeBid.setPayPid(orderParam.getPid());
		financeBid.setBankCardNo(orderParam.getBankCard());
		financeBid.setPayChannel("agreeFuiou");
		if( orderParam.getRegChannel() != null ){
			financeBid.setRegChannel(orderParam.getRegChannel());
		}
		if( orderParam.getHbid() != null ){
			financeBid.setHbid(orderParam.getHbid());
		}
		if( orderParam.getChannel() != null ){
			financeBid.setChannel(orderParam.getChannel());
		}

		financeBidDao.addFinanceBid(financeBid);

		return financeBid;
	}




	@Override
	@Transactional
	public FinanceBidPO onFuiouPaySuccess(final FuiouH5PayService.PayCallBackResult result) {
		final FinanceBidPO bid = financeBidDao.getFidByOrderId(result.getMchntOrderId());
		BorrowPO borrow = borrowDao.getBorrowById(bid.getBorrowId());

		if ( bid.getPay() == 1 ) {
			return bid;
		}
		bid.setPay(1);
		bid.setBankCardNo(result.getBankCard());
		bid.setSummary(result.getOrderId());
		// 起息T+1
		Date today0Hour = null;
		try {
			today0Hour = yyyyMMdd.parse(yyyyMMdd.format(new Date()));
		} catch (Exception e) {

		}
		today0Hour.setDate(today0Hour.getDate() + 1);
		bid.setBeginProfit(new Date(today0Hour.getTime()));
		today0Hour.setDate(today0Hour.getDate() + borrow.getTimeLimitDay());
		bid.setEndProfit(new Date(today0Hour.getTime()));

		BigDecimal bidByAmt = bid.getBuyAmt();
		//判断是否有红包使用
		if( !"".equals(bid.getHbid()) && bid.getHbid() != null ){
			UserRedPackets userRedPackets = this.activityRpcService.updateHbStatus(bid.getHbid(), bid.getUserId(), borrow.getName(),  bidByAmt.doubleValue());
			// 1--红包  2--加息劵
			if( userRedPackets.getHbtype() == 1 ){
				bid.setCouponAmt(BigDecimal.valueOf(userRedPackets.getHbmoney()));
				/*double buyamt = NumberUtil.add(bidByAmt.doubleValue(), userRedPackets.getHbmoney());
				bid.setBuyAmt(BigDecimal.valueOf(buyamt));*/
			}else if(userRedPackets.getHbtype() == 2){
				bid.setCouponRate(BigDecimal.valueOf(userRedPackets.getHbmoney()));
				double rate = NumberUtil.add(bid.getRate().doubleValue(), userRedPackets.getHbmoney());
				bid.setRate(BigDecimal.valueOf(rate));
			}
		}

		financeBidDao.updateFinanceBid(bid);

		UserBankCard existBankCardList = userRpcService.getUserBankCardByCard(result.getBankCard());
		// 绑卡
		if ( null == existBankCardList) {
			UserBankCard card = new UserBankCard();
			card.setUserId(bid.getUserId());
			card.setStatus("enable");
			card.setPid(bid.getPayPid());
			card.setName(bid.getPayName());
			card.setBankCard(result.getBankCard());
			FuiouBinCardQueryService.Result rs = fuiouBinCardQueryService.queryByCard(result.getBankCard());
			if (rs != null && StringUtil.isNotEmpty(rs.getBankName())) {
				card.setBankName(rs.getBankName());
				card.setInscd(rs.getInscd());
			}
			userRpcService.addUserBankCard(card);
		}

		logger.info("投资成功，修改标的{}的已购买次数tenderTimes={}和已购买金额accountYes={},本次购买金额={}",borrow.getId(),borrow.getTenderTimes(), borrow.getAccountYes(), bidByAmt);
		// 修改borrow信息
		//borrow.setTenderTimes(Integer.toString((StringUtil.isNotEmpty(borrow.getTenderTimes()) ? Integer.parseInt(borrow.getTenderTimes()) : 0) + 1));
		//borrow.setAccountYes(BigDecimal.valueOf(StringUtil.isNotEmpty(borrow.getAccountYes()) ? Double.parseDouble(borrow.getAccountYes()) : 0).add().doubleValue() + "");
		borrow.setAccountYes(bidByAmt.toString());
		if( Double.valueOf(borrow.getAccount()) - Double.valueOf(borrow.getAccountYes()) <= 0){
			borrow.setStatus(6);
		}

		borrowDao.updateBorrow(borrow);


		AccountLog log = new AccountLog();
		log.setMoney(bid.getBuyAmt().doubleValue());
		log.setTotal(0.0);
		log.setUseMoney(0.0);
		log.setUser_id(bid.getUserId());//
		log.setType("tender");
		log.setNoUseMoney(0.0);
		log.setToUser(borrow.getUserId());
		log.setAddtime(DateUtils.getNowTimeStr());
		// truncate(fd.rate/36500*fd.buy_amt*datediff(fd.end_profit,fd.begin_profit),2)
		log.setCollection(0.0);
		log.setRemark("投资成功，冻结投资者的投标资金" + NumberUtil.format4(bid.getBuyAmt().doubleValue()));
		userRpcService.addAccountLog(log);


		//判断是否有推荐人
		User user = userRpcService.getUserByUserId(String.valueOf(bid.getUserId()));
        try {

			//当前时间
			String now = DateUtils.getNowDateStr();
			//开始时间小于于当前时间 且 结束时间大于当前时间
			logger.info("活动时间-->{}<-<-->->{}",starttime,endtime);
			logger.info("用户注册时间-->{}",user.getAddtime());
			if (DateUtils.compareDate(now,starttime)&&DateUtils.compareDate(endtime,now)&&Long.valueOf(user.getAddtime())>DateUtils.getTime(starttime)){
				logger.info("活动期间投资送红包");
			    //投资金额
                Double money = bid.getBuyAmt().doubleValue();
                /*if( bid.getCouponAmt() != null ){
                    money = bid.getBuyAmt().doubleValue() - bid.getCouponAmt().doubleValue();
                }else{
                    money =  bid.getBuyAmt().doubleValue();
                }*/

				DateUtils.getDistanceTime(DateUtils.dateStr2(user.getAddtime()),DateUtils.dateStr2(String.valueOf(System.currentTimeMillis()/1000)));
				String nowDay = DateUtils.getNowDateStr();
				Date time1 = DateUtils.getDate(user.getAddtime());
				String date = DateUtils.dateStr4(time1);
				String distance = DateUtils.getDistanceTime(date,nowDay).split("天")[0];
				String distance2 = DateUtils.getDistanceTime(date,nowDay).split("天")[0];
                //判断是否是首次投资
                if( bid.getBuyAmt().compareTo(BigDecimal.valueOf(financeBidDao.getAllFinaceByUserId(bid.getUserId()))) == 0 ){
                    //第一次投资推送现象
                    pushMsg(userStrHashRedisTemplate.get(Constant.PUSH_ID+user.getUser_id()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID+user.getUser_id()) : null);

                    //被邀请用户 注册之日7天内 首投5000元以上
                    if( null != user && user.getInviteUserid() != 0&& money>=5000d&&Integer.parseInt(distance)<7){
                        logger.info("被邀请人{}首次投资{}，送红包", bid.getUserId(), bid.getBuyAmt());
                        Map<String, Object> map = new HashMap<>();
                        map.put("inviter", user.getInviteUserid());
                        map.put("userid", user.getUser_id());
                        map.put("buyamt", money);
                        map.put("code", ActiveGiftEnums.SING_UP_GIRT.getCode());
                        mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));

                        map.put("timeLimitDay", borrow.getTimeLimitDay());
                    }
                }
                //根据用户id查找 累计投资金额
                String amountMoney = financeBidDao.findAmountMoneyByUserId(user.getUser_id(),starttime,endtime);
				amountMoney = amountMoney == null ? "0" : amountMoney;
				//根据 用户id和邀请人id查询  佣金奖
				List<UserJiangPin> list =  activityRpcService.findUserJiangPin(user.getUser_id(),user.getInviteUserid(),ActiveGiftEnums.INVITATION_GIRT.getCode());
                //推荐人不为空 且 邀请单个好友累计投资金额每满10,000元 投资新手标除外，邀请单个好友可赚取的奖励上限为300元京东卡
                logger.info("用户={}累计投资={}",user.getUser_id(),amountMoney);

                if (null != user && user.getInviteUserid() != 0&&list.size()<3&&Integer.parseInt(distance2)<30){
                	if (Double.parseDouble(amountMoney)>=10000d){
						Map<String, Object> map = new HashMap<>();
						map.put("inviter", user.getInviteUserid());
						map.put("userid", user.getUser_id());
						map.put("buyamt", amountMoney);
						map.put("code", ActiveGiftEnums.INVITATION_GIRT.getCode());
						mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
					}
				}
				//推荐人不为空 且 邀请每满3人，且该3人每人累计投资金额至少满10,000元 获得次数少于3次
				List<UserJiangPin> list2 =  activityRpcService.findUserJiangPin(null,user.getInviteUserid(),ActiveGiftEnums.TEAM_GIRT.getCode());
				List<UserJiangPin> lis3 =  activityRpcService.findUserJiangPin(user.getUser_id(),user.getInviteUserid(),ActiveGiftEnums.TEAM_GIRT.getCode());
				//邀请好友人数
                Integer invitationDistinctSize = financeBidDao.findInvitationDistanct(user.getInviteUserid(),starttime,endtime);
                if (null != user && user.getInviteUserid() != 0&&list2.size()<9&&lis3.size()<1&&Integer.parseInt(distance2)<30){
//                	if (invitationDistinctSize==3||invitationDistinctSize==6||invitationDistinctSize==9){
					if (Double.valueOf(amountMoney)>=10000d){
						Map<String, Object> map = new HashMap<>();
						map.put("inviter", user.getInviteUserid());
						map.put("userid", user.getUser_id());
						map.put("buyamt", amountMoney);
						map.put("code", ActiveGiftEnums.TEAM_GIRT.getCode());
						mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
					}

//					}
                }


            }
		}catch (Exception e){
        	e.printStackTrace();
        	logger.error("用户{}被邀请人{}投资{}，送红包失败", user.getInviteUserid(),bid.getUserId(), bid.getBuyAmt());
		}
		try {
        	logger.info("用户={}投资{}元，{}天标送金豆",bid.getUserId(),bid.getBuyAmt(),borrow.getTimeLimitDay());
        	activityRpcService.investGrantGlodBean(String.valueOf(bid.getUserId()),borrow.getTimeLimitDay(),bid.getBuyAmt().doubleValue());
		}catch (Exception e){
        	e.printStackTrace();
        	logger.error("投资送金豆失败");
		}
        //生成合同
		mqTemplate.sendMsg(GENERATE_CONTRACT.toName(), String.valueOf(bid.getId()));
        //计算预期收益
        Double dev = NumberUtil.multiply(2,bid.getBuyAmt(),borrow.getTimeLimitDay(),bid.getRate());
        Double insterst = NumberUtil.divide(2,dev,36500);
        //投标成功后 添加消息 至 消息中心
		userRpcService.addMsg(bid.getUserId(), MsgCodeEnum.SYS_MSG.getCode(),MsgCodeEnum.SYS_SUBTYPE_BID.getValue(),
				String.format(MsgCodeEnum.SYS_MSG_TEXT.getValue(),borrow.getName(),String.valueOf(bid.getBuyAmt()),String.valueOf(insterst),borrow.getTimeLimitDay(),SERVICE_PHONE));

		String alias = userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) : "";
		if (!"".equals(alias)) {
			logger.info("开始向用户{}推送消息pushId={}", bid.getUserId(), alias);
			Map<String, String> map = new HashMap<>();
			map.put("msgCode", String.valueOf(MsgCodeEnum.SYS_MSG.getCode()));
			JiguangPush.sendPushIosAndroidByAlias(alias, MsgCodeEnum.SYS_MSG.getValue(), MsgCodeEnum.SYS_SUBTYPE_BID.getValue(), map);
		}

		return bid;
	}



	@Override
	@Transactional
	public FinanceBidPO onAgreeFuiouPaySuccess(final FuiouH5PayService.PayCallBackResult result) {
		final FinanceBidPO bid = financeBidDao.getFidByOrderId(result.getMchntOrderId());
		BorrowPO borrow = borrowDao.getBorrowById(bid.getBorrowId());

		if ( bid.getPay() == 1 ) {
			return bid;
		}
		bid.setPay(1);
		bid.setBankCardNo(result.getBankCard());
		bid.setSummary(result.getOrderId());
		// 起息T+1
		Date today0Hour = null;
		try {
			today0Hour = yyyyMMdd.parse(yyyyMMdd.format(new Date()));
		} catch (Exception e) {

		}
		today0Hour.setDate(today0Hour.getDate() + 1);
		bid.setBeginProfit(new Date(today0Hour.getTime()));
		today0Hour.setDate(today0Hour.getDate() + borrow.getTimeLimitDay());
		bid.setEndProfit(new Date(today0Hour.getTime()));

		BigDecimal bidByAmt = bid.getBuyAmt();
		//判断是否有红包使用
		if( !"".equals(bid.getHbid()) && bid.getHbid() != null ){
			UserRedPackets userRedPackets = this.activityRpcService.updateHbStatus(bid.getHbid(), bid.getUserId(), borrow.getName(),  bidByAmt.doubleValue());
			// 1--红包  2--加息劵
			if( userRedPackets.getHbtype() == 1 ){
				bid.setCouponAmt(BigDecimal.valueOf(userRedPackets.getHbmoney()));
				/*double buyamt = NumberUtil.add(bidByAmt.doubleValue(), userRedPackets.getHbmoney());
				bid.setBuyAmt(BigDecimal.valueOf(buyamt));*/
			}else if(userRedPackets.getHbtype() == 2){
				bid.setCouponRate(BigDecimal.valueOf(userRedPackets.getHbmoney()));
				double rate = NumberUtil.add(bid.getRate().doubleValue(), userRedPackets.getHbmoney());
				bid.setRate(BigDecimal.valueOf(rate));
			}
		}

		financeBidDao.updateFinanceBid(bid);

		/*UserBankCard existBankCardList = userRpcService.getUserBankCardByCard(result.getBankCard());
		// 绑卡
		if ( null == existBankCardList) {
			UserBankCard card = new UserBankCard();
			card.setUserId(bid.getUserId());
			card.setStatus("enable");
			card.setPid(bid.getPayPid());
			card.setName(bid.getPayName());
			card.setBankCard(result.getBankCard());
			FuiouBinCardQueryService.Result rs = fuiouBinCardQueryService.queryByCard(result.getBankCard());
			if (rs != null && StringUtil.isNotEmpty(rs.getBankName())) {
				card.setBankName(rs.getBankName());
				card.setInscd(rs.getInscd());
			}
			userRpcService.addUserBankCard(card);
		}*/

		logger.info("投资成功，修改标的{}的已购买次数tenderTimes={}和已购买金额accountYes={},本次购买金额={}",borrow.getId(),borrow.getTenderTimes(), borrow.getAccountYes(), bidByAmt);
		// 修改borrow信息
		//borrow.setTenderTimes(Integer.toString((StringUtil.isNotEmpty(borrow.getTenderTimes()) ? Integer.parseInt(borrow.getTenderTimes()) : 0) + 1));
		//borrow.setAccountYes(BigDecimal.valueOf(StringUtil.isNotEmpty(borrow.getAccountYes()) ? Double.parseDouble(borrow.getAccountYes()) : 0).add().doubleValue() + "");
		borrow.setAccountYes(bidByAmt.toString());
		if( Double.valueOf(borrow.getAccount()) - Double.valueOf(borrow.getAccountYes()) <= 0){
			borrow.setStatus(6);
		}

		borrowDao.updateBorrow(borrow);


		AccountLog log = new AccountLog();
		log.setMoney(bid.getBuyAmt().doubleValue());
		log.setTotal(0.0);
		log.setUseMoney(0.0);
		log.setUser_id(bid.getUserId());//
		log.setType("tender");
		log.setNoUseMoney(0.0);
		log.setToUser(borrow.getUserId());
		log.setAddtime(DateUtils.getNowTimeStr());
		// truncate(fd.rate/36500*fd.buy_amt*datediff(fd.end_profit,fd.begin_profit),2)
		log.setCollection(0.0);
		log.setRemark("投资成功，冻结投资者的投标资金" + NumberUtil.format4(bid.getBuyAmt().doubleValue()));
		userRpcService.addAccountLog(log);


		//判断是否有推荐人
		User user = userRpcService.getUserByUserId(String.valueOf(bid.getUserId()));
		try {

			//当前时间
			String now = DateUtils.getNowDateStr();
			//开始时间小于于当前时间 且 结束时间大于当前时间
			logger.info("活动时间-->{}<-<-->->{}",starttime,endtime);
			logger.info("用户注册时间-->{}",user.getAddtime());
			if (DateUtils.compareDate(now,starttime)&&DateUtils.compareDate(endtime,now)&&Long.valueOf(user.getAddtime())>DateUtils.getTime(starttime)){
				logger.info("活动期间投资送红包");
				//投资金额
				Double money = bid.getBuyAmt().doubleValue();
                /*if( bid.getCouponAmt() != null ){
                    money = bid.getBuyAmt().doubleValue() - bid.getCouponAmt().doubleValue();
                }else{
                    money =  bid.getBuyAmt().doubleValue();
                }*/

				DateUtils.getDistanceTime(DateUtils.dateStr2(user.getAddtime()),DateUtils.dateStr2(String.valueOf(System.currentTimeMillis()/1000)));
				String nowDay = DateUtils.getNowDateStr();
				Date time1 = DateUtils.getDate(user.getAddtime());
				String date = DateUtils.dateStr4(time1);
				String distance = DateUtils.getDistanceTime(date,nowDay).split("天")[0];
				String distance2 = DateUtils.getDistanceTime(date,nowDay).split("天")[0];
				//判断是否是首次投资
				if( bid.getBuyAmt().compareTo(BigDecimal.valueOf(financeBidDao.getAllFinaceByUserId(bid.getUserId()))) == 0 ){
					//第一次投资推送现象
					pushMsg(userStrHashRedisTemplate.get(Constant.PUSH_ID+user.getUser_id()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID+user.getUser_id()) : null);

					//被邀请用户 注册之日7天内 首投5000元以上
					if( null != user && user.getInviteUserid() != 0&& money>=5000d&&Integer.parseInt(distance)<7){
						logger.info("被邀请人{}首次投资{}，送红包", bid.getUserId(), bid.getBuyAmt());
						Map<String, Object> map = new HashMap<>();
						map.put("inviter", user.getInviteUserid());
						map.put("userid", user.getUser_id());
						map.put("buyamt", money);
						map.put("code", ActiveGiftEnums.SING_UP_GIRT.getCode());
						mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));

						map.put("timeLimitDay", borrow.getTimeLimitDay());
					}
				}
				//根据用户id查找 累计投资金额
				String amountMoney = financeBidDao.findAmountMoneyByUserId(user.getUser_id(),starttime,endtime);
				amountMoney = amountMoney == null ? "0" : amountMoney;
				//根据 用户id和邀请人id查询  佣金奖
				List<UserJiangPin> list =  activityRpcService.findUserJiangPin(user.getUser_id(),user.getInviteUserid(),ActiveGiftEnums.INVITATION_GIRT.getCode());
				//推荐人不为空 且 邀请单个好友累计投资金额每满10,000元 投资新手标除外，邀请单个好友可赚取的奖励上限为300元京东卡
				logger.info("用户={}累计投资={}",user.getUser_id(),amountMoney);

				if (null != user && user.getInviteUserid() != 0&&list.size()<3&&Integer.parseInt(distance2)<30){
					if (Double.parseDouble(amountMoney)>=10000d){
						Map<String, Object> map = new HashMap<>();
						map.put("inviter", user.getInviteUserid());
						map.put("userid", user.getUser_id());
						map.put("buyamt", amountMoney);
						map.put("code", ActiveGiftEnums.INVITATION_GIRT.getCode());
						mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
					}
				}
				//推荐人不为空 且 邀请每满3人，且该3人每人累计投资金额至少满10,000元 获得次数少于3次
				List<UserJiangPin> list2 =  activityRpcService.findUserJiangPin(null,user.getInviteUserid(),ActiveGiftEnums.TEAM_GIRT.getCode());
				List<UserJiangPin> lis3 =  activityRpcService.findUserJiangPin(user.getUser_id(),user.getInviteUserid(),ActiveGiftEnums.TEAM_GIRT.getCode());
				//邀请好友人数
				Integer invitationDistinctSize = financeBidDao.findInvitationDistanct(user.getInviteUserid(),starttime,endtime);
				if (null != user && user.getInviteUserid() != 0&&list2.size()<9&&lis3.size()<1&&Integer.parseInt(distance2)<30){
//                	if (invitationDistinctSize==3||invitationDistinctSize==6||invitationDistinctSize==9){
					if (Double.valueOf(amountMoney)>=10000d){
						Map<String, Object> map = new HashMap<>();
						map.put("inviter", user.getInviteUserid());
						map.put("userid", user.getUser_id());
						map.put("buyamt", amountMoney);
						map.put("code", ActiveGiftEnums.TEAM_GIRT.getCode());
						mqTemplate.sendMsg(INVITATION_USER_GRANT_HB.toName(), JSON.toJSONString(map));
					}

//					}
				}


			}
		}catch (Exception e){
			e.printStackTrace();
			logger.error("用户{}被邀请人{}投资{}，送红包失败", user.getInviteUserid(),bid.getUserId(), bid.getBuyAmt());
		}
		try {
			logger.info("用户={}投资{}元，{}天标送金豆",bid.getUserId(),bid.getBuyAmt(),borrow.getTimeLimitDay());
			activityRpcService.investGrantGlodBean(String.valueOf(bid.getUserId()),borrow.getTimeLimitDay(),bid.getBuyAmt().doubleValue());
		}catch (Exception e){
			e.printStackTrace();
			logger.error("投资送金豆失败");
		}
		//生成合同
		mqTemplate.sendMsg(GENERATE_CONTRACT.toName(), String.valueOf(bid.getId()));
		//计算预期收益
		Double dev = NumberUtil.multiply(2,bid.getBuyAmt(),borrow.getTimeLimitDay(),bid.getRate());
		Double insterst = NumberUtil.divide(2,dev,36500);
		//投标成功后 添加消息 至 消息中心
		userRpcService.addMsg(bid.getUserId(), MsgCodeEnum.SYS_MSG.getCode(),MsgCodeEnum.SYS_SUBTYPE_BID.getValue(),
				String.format(MsgCodeEnum.SYS_MSG_TEXT.getValue(),borrow.getName(),String.valueOf(bid.getBuyAmt()),String.valueOf(insterst),borrow.getTimeLimitDay(),SERVICE_PHONE));

		String alias = userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) != null ? userStrHashRedisTemplate.get(Constant.PUSH_ID + bid.getUserId()) : "";
		if (!"".equals(alias)) {
			logger.info("开始向用户{}推送消息pushId={}", bid.getUserId(), alias);
			Map<String, String> map = new HashMap<>();
			map.put("msgCode", String.valueOf(MsgCodeEnum.SYS_MSG.getCode()));
			JiguangPush.sendPushIosAndroidByAlias(alias, MsgCodeEnum.SYS_MSG.getValue(), MsgCodeEnum.SYS_SUBTYPE_BID.getValue(), map);
		}
		//自动上标
//		mqTemplate.sendMsg(AUTO_UP_BORROW.toName(), String.valueOf(borrow.getBorrow_group()));
		return bid;
	}


	/**
	 * 第一次购买
	 * @param pushId
	 */
	private void pushMsg(String pushId){
		if( null == pushId){
			return;
		}
		Map<String, String> map  = new HashMap<>();
		map.put("type", PushExtrasEnum.BUY_FIRST.getCode());
		JiguangPush.sendPushIosAndroidMsgByalias(PushExtrasEnum.BUY_FIRST.getValue(), pushId, map);
	}



}
