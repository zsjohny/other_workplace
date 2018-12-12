package com.finace.miscroservice.borrow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.BorrowTenderDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.Contract.ContractService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.borrow.service.fuiou.FuiouOrderCheckService;
import com.finace.miscroservice.borrow.utils.lock.DistributedLockHandler;
import com.finace.miscroservice.borrow.utils.lock.Lock;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.*;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.handler.impl.RedisDistributeLockHandler;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceBidServiceImpl implements FinanceBidService {
    private Log logger = Log.getInstance(FinanceBidServiceImpl.class);

    @Autowired
    private FinanceBidDao financeBidDao;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Autowired
    private FuiouOrderCheckService fuiouOrderCheckService;

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private BorrowTenderDao borrowTenderDao;

    @Autowired
    private RedisDistributeLockHandler redisDistributeLockHandler;


    @Override
    public FinanceMoney getFinanceMoneyInfo(String userId) {
        FinanceMoney financeMoney = financeBidDao.getFinanceMoneyInfo(userId);
        logger.info("根据用户id={}获取用户富有投资总数={}",userId,financeMoney);
        return financeMoney;
    }

    @Override
    public Integer getUserFidCount(String userId) {
        return financeBidDao.getUserFidCount(userId);
    }

    @Override
    public List<InvestRecords> getUserInvestRecords(Map<String, Object> map, int pageNum) {
        List<InvestRecords> list = financeBidDao.getUserInvestRecords(map, pageNum);
        for (InvestRecords investRecords : list){
            if( null != investRecords.getHbid() && !"".equals(investRecords.getHbid())){
                UserRedPackets userRedPackets = activityRpcService.getRpById(Integer.valueOf(investRecords.getHbid()));
                if( null != userRedPackets ){
                    investRecords.setHbmoney(String.valueOf(userRedPackets.getHbmoney()));
                    investRecords.setHbtype(String.valueOf(userRedPackets.getHbtype()));
                    //使用红包添加红包
                    if( 1 == userRedPackets.getHbtype() ){
                        investRecords.setMoney(String.valueOf(Double.valueOf(investRecords.getMoney()) + Double.valueOf(investRecords.getHbmoney())) );
                    }
                }
            }

            if( null != investRecords.getUserId() ){
                BorrowerInfo borrowerInfo = userRpcService.getBorrowerInfo(investRecords.getUserId());
                if( null != borrowerInfo ){
                    investRecords.setBmoney(borrowerInfo.getMoney());
                    investRecords.setManage(borrowerInfo.getManage());
                    investRecords.setFinance(borrowerInfo.getFinance());
                    investRecords.setRepayment(borrowerInfo.getRepayment());
                    investRecords.setOverdue(borrowerInfo.getOverdue());
                    investRecords.setAppeal(borrowerInfo.getAppeal());
                    investRecords.setPunish(borrowerInfo.getPunish());
                    investRecords.setTrack(borrowerInfo.getTrack());
                }
            }
            investRecords.setUserId("");
            investRecords.setRepaymentType("到期本息还款");
        }
        return list;
    }

    @Override
    public FinanceMoney getFinanceMonthInfo(String userId, String tmonth) {
        return financeBidDao.getFinanceMonthInfo(userId, tmonth);
    }

    @Override
    public Map<String, Object> getReturnCalendar(String userId, String tday, String tmonth) {
        Map<String, Object> map = new HashMap<>();

        List<FinanceMoney> fmList = financeBidDao.getFinanceDayInfo(userId, tday);
        for (FinanceMoney financeMoney : fmList ){
              if("counting".equals(financeMoney.getBidStatus())){
                  financeMoney.setBidStatus("未还款");
              }else if("repayment".equals(financeMoney.getBidStatus())){
                  financeMoney.setBidStatus("已还款");
              }
        }

        map.put("showDay",financeBidDao.getDayByMonth(userId));
        map.put("showDayList",fmList);
        map.put("showMonth",financeBidDao.getFinanceMonthInfo(userId,tmonth));
        return map;
    }

    @Override
    public List<String> getDayByMonth(String userId) {
        return financeBidDao.getDayByMonth(userId);
    }

    @Override
    public FinanceBidPO getFidById(int fbid) {
        return financeBidDao.getFidById(fbid);
    }

    @Override
    public Map<String, Object> getInvestmentRecordByBorrowId(int id, int page) {
        Map<String, Object> rlist = new HashMap();
        BasePage basePage=new BasePage();
        basePage.setPageNum(page);
        List<FinanceBidPO> list = financeBidDao.getInvestmentRecordByBorrowId(id);
        List<Map<String, Object>> lmap = new ArrayList<>();
        for (FinanceBidPO financeBidPO:list) {
            Map<String, Object> map = new HashMap<>();
            map.put("borrowId", financeBidPO.getBorrowId());
            map.put("account", financeBidPO.getAccount());
            map.put("addtime", financeBidPO.getAddtime());
            User user = userRpcService.getUserByUserId(String.valueOf(financeBidPO.getUserId()));
            map.put("username", TextUtil.hideUsernameChar(user.getUsername()));
            lmap.add(map);
        }
        rlist.put("finance",lmap);

        rlist.put("totalSize",  basePage.getTotal(list));
        return rlist;
    }

    @Override
    public double getAllFinaceByUserId(int userId) {
        Double allAccount = financeBidDao.getAllFinaceByUserId(userId) + borrowTenderDao.getAllTenderByUserId(userId);
        return allAccount;
    }

    @Override
    public FinanceBidPO getFidByOrderId(String orderId) {
        return financeBidDao.getFidByOrderId(orderId);
    }

    @Override
    public int updatePayFinanceBidByOrderId(Map<String, Object> map) {
        return financeBidDao.updatePayFinanceBidByOrderId(map);
    }

    @Override
    public Double getUserFirstBuyAmt(int userId) {
        return financeBidDao.getUserFirstBuyAmt(userId);
    }


    @Override
    public String closeOrder(String orderId, String goToFuiouHtml, String version) {
        logger.info("开始关闭订单{}", orderId);
        try {

            FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(orderId);
            if( null == financeBidPO ){
                return "error";
            }

            BorrowPO borrow = borrowDao.getBorrowById(financeBidPO.getBorrowId());
            if( null == borrow ){
                return "error";
            }

            FuiouOrderCheckService.CheckOrderResult checkOrderResult = fuiouOrderCheckService.checkMchntcdResult(orderId);

            if (checkOrderResult != null) {
                //0000 表示支付成功 5185 表示“订单已支付”  5077 表示“无此订单”   11V3 表示“订单失效”  11E3 表示“订单支付失败”  51B3 表示 “订单支付中(隔段时间之后再来查询或等待异步通知)”
                logger.info("订单{},code={}", orderId,checkOrderResult.getPescode());

                if ("5185".equals(checkOrderResult.getPescode()) || "0000".equals(checkOrderResult.getPescode())) {  //已支付
                    logger.info("订单{},已支付，开始自动上标检查", orderId);
                   // borrowDao.autoUpBorrow(fbid);
                    //自动上标处理
                    List<BorrowPO> bList = this.borrowDao.getBorrowPOBySellOut(borrow.getBorrow_group());
                    logger.info("重复上标记录自动上标：分组={}是否有该标的={}", borrow.getBorrow_group(), bList.size());
                    if( bList.size() == 0 ){
                        BorrowPO borrowPO = this.borrowDao.getBorrowPOByAutoAdd(borrow.getBorrow_group());
                        if( null != borrowPO){
                            logger.info("重复上标记录自动上标开始：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
                            borrowPO.setStatus(1);//审核通过
                            borrowPO.setVerifyRemark("自动审核通过");
                            borrowPO.setVerifyTime(DateUtils.getNowTimeStr());
                            this.borrowDao.updateAutoCheckBorrow(borrowPO);
                            logger.info("重复上标记录自动上标结束：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
                        }
                    }

                } else if ("51B3".equals(checkOrderResult.getPescode())) {  //支付中
                    logger.info("订单{},支付中，等待2分钟后重新检查该订单是否已支付");

                    //支付中2分钟后重新处理该订单
                    SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
                    TimerScheduler timerScheduler = new TimerScheduler();
                    timerScheduler.setType(TimerSchedulerTypeEnum.ORDER_FAILURE_INSPECT.toNum());
                    timerScheduler.setName("order_failure_inspect" + UUIdUtil.generateUuid());
                    timerScheduler.setCron(sdf.format(DateUtils.dateAndDayByDate(String.valueOf(Integer.valueOf(DateUtils.getNowTimeStr()) + 120), "0")));
                    mqTemplate.sendMsg(MqChannelEnum.TIMER_SCHEDULER_TIMER_ACCEPT.toName(), JSONObject.toJSONString(timerScheduler));

                }else{  //支付失败

                    logger.info("订单{},支付失败,开始处理资金解冻,pay={}", orderId, financeBidPO.getPay());

                    if( financeBidPO.getPay() == 0 ){

                        if( "0".equals(goToFuiouHtml) ||  "undefined".equals(version)){
                            Double amt = financeBidPO.getBuyAmt() != null ? financeBidPO.getBuyAmt().doubleValue() : 0D;
                            Double hbamt = financeBidPO.getCouponAmt() != null ? financeBidPO.getCouponAmt().doubleValue() : 0D;
                            logger.info("订单{},支付失败，开始关闭订单，恢复标的{}中的可购买金额, amt={}, hbamt={}", orderId, financeBidPO.getBorrowId(), amt, hbamt);
                            String rmoney = userStrHashRedisTemplate.get(Constant.PURCHASE_AMT + financeBidPO.getBorrowId());
                            if (null != rmoney) {
                                userStrHashRedisTemplate.set(Constant.PURCHASE_AMT + financeBidPO.getBorrowId(), String.valueOf(Double.valueOf(rmoney) + amt));
                            }
                            logger.info("订单{},支付失败，关闭订单结束，标的{}可购买金额已恢复,rmoney={},amt={}, hbamt={}", orderId, financeBidPO.getBorrowId(), rmoney, amt, hbamt);
                        }

                        //关闭我们自己订单
                        Map<String, Object> map = new HashMap<>();
                        map.put("pay", -2);
                        map.put("orderId", orderId);
                        if( financeBidDao.updatePayFinanceBidByOrderId(map) > 0 ){
                            logger.info("订单失效任务处理成功transferData={}", orderId);
                        }else{
                            logger.info("订单失效任务处理失败transferData={}", orderId);
                        }

                        return "success";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("订单{}失效处理。异常信息：{}", orderId, e);
        }

        return "error";
    }



    /**
     * 显示合同信息
     * @return
     */
    public Map<String, Object> showHt(String fbid){

        Map<String, Object> map = new HashMap<>();
        logger.info("开始查询合同信息,{}", fbid);

        FinanceBidPO financeBid = financeBidDao.getFidById(Integer.valueOf(fbid));
        if( null == financeBid ){
            return null;
        }
        financeBid.setQxr(DateUtils.dateStr2(financeBid.getBeginProfit()));
        financeBid.setDqr(DateUtils.dateStr2(financeBid.getEndProfit()));
        map.put("buyAmt", financeBid.getBuyAmt());
        map.put("interest", financeBid.getInterest());
        map.put("dqr", financeBid.getDqr());
        map.put("qxr", financeBid.getQxr());

        String contract = DateUtils.dateStr5(financeBid.getPayTime()) + financeBid.getId();
        map.put("contract", contract); //合同编号

        //获取投资人信息
        User tuser = this.userRpcService.getUserByUserId(String.valueOf(financeBid.getUserId()));
        map.put("tusername", tuser.getUsername());
        map.put("tphone", tuser.getPhone());
        map.put("tuserId", tuser.getUser_id());
        map.put("trealname", financeBid.getPayName());
        map.put("tcard", financeBid.getPayPid());

        BorrowPO borrow = borrowDao.getBorrowById(financeBid.getBorrowId());
        map.put("limitDay", borrow.getTimeLimitDay());
        map.put("apr", borrow.getApr());
        map.put("loanUage", borrow.getLoan_usage());
        map.put("limitDay", borrow.getTimeLimitDay());
        map.put("account", borrow.getAccount());

        //获取借款人信息
        User juser = this.userRpcService.getUserByUserId(String.valueOf(borrow.getUserId()));
        if( null != juser ){
            map.put("jusername", juser.getUsername());
            map.put("jphone", TextUtil.hidePhoneNo(juser.getPhone()));
            map.put("jrealname", TextUtil.hideCenterChar(juser.getRealname()));

            map.put("jcard", juser.getCardId());
            map.put("juserId", juser.getUser_id());
            map.put("jphone2", juser.getPhone());
            map.put("jtype", juser.getTypeId());
            map.put("jrealname2", juser.getRealname());
            map.put("privacy", juser.getPrivacy() != null ? TextUtil.hideRealnameChar(juser.getPrivacy()) : "");
            map.put("jaddress", juser.getAddress() != null ? juser.getAddress() : "");
        }else{
            map.put("jusername", " ");
            map.put("jphone", " ");
            map.put("jrealname", "杭州**网络科技有限公司");
        }

        User puser = this.userRpcService.getUserByUserId("1");
        map.put("prealname", puser.getRealname());
        logger.info("结束查询合同信息,{}", fbid);

        return map;
    }


    @Override
    public Map<String, Object> getCumulativeData() {
        FinanceBidPO financeBidPO = financeBidDao.getCumulativeData();
        if( null == financeBidPO ){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("cumulativeMoney", financeBidPO.getPrincipal());
        map.put("cumulativeCount", financeBidPO.getNowProfit());
        return map;
    }

    @Transactional
    @Override
    public void updateYunContractIdById(String contractId, String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("contractId", contractId);
        map.put("orderId", orderId);
        financeBidDao.updateYunContractIdById(map);
    }


    @Override
    public String getFinanceBidByDesc(int borrowId, String timeout) {
        FinanceBidPO financeBidPO = financeBidDao.getFinanceBidByDesc(borrowId);
        if( financeBidPO.getGmtCreate() != null ){
            long difDate = Long.valueOf(DateUtils.getNowTimeStr()) - Long.valueOf(DateUtils.getTimeStr(financeBidPO.getGmtCreate()));
            if( Long.valueOf(timeout) + 60 < difDate ){
                return financeBidPO.getOrderSn();
            }
        }
        return null;
    }


    public FinanceBidPO getFidByOrderIdAndFail(String orderId){
        return financeBidDao.getFidByOrderIdAndFail(orderId);
    }


    public Response canBePay(String userId,String orderId){
        FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(orderId);
        if( null == financeBidPO){
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}投资记录不存在", userId, orderId);
            return Response.errorMsg("购买记录不存在");
        }

        BorrowPO borrowPO = borrowDao.getBorrowById(financeBidPO.getBorrowId());
        if( null == borrowPO){
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}标的不存在", userId, orderId);
            return Response.errorMsg("标的不存在");
        }

        if( borrowPO.getRemmoney() < financeBidPO.getBuyAmt().doubleValue() ){
            return Response.errorMsg("可购买金额不足,请选择其他标的!!!");
        }
        String borrowId = String.valueOf(borrowPO.getId());
        /**
        try {
            //加锁
            redisDistributeLockHandler.lock(Constant.BORROW_LOCK+borrowId);

            String amt = financeBidPO.getBuyAmt().toString();
            String rmoney = userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId) != null ? userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId) : borrowPO.getRemmoney().toString();
            logger.info("标的{}的可购买金额rmoney={}, amt={},Remmoney={}", borrowId, rmoney, amt, borrowPO.getRemmoney());
            if (Double.valueOf(rmoney) - Double.valueOf(amt) < 0) {
                logger.warn("用户{}购买{}标的。该标的剩余金额已有其他用户正在购买，请选择其他标的购买!", userId, borrowId);
                redisDistributeLockHandler.unlock(Constant.BORROW_LOCK+borrowId);
                return Response.errorMsg("该标的剩余金额已有其他用户正在购买，请选择其他标的购买或稍后再试!");
            }
            //保存可购金额
            userStrHashRedisTemplate.set(Constant.NEW_PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) - Double.valueOf(amt)));

            //解锁
            redisDistributeLockHandler.unlock(Constant.BORROW_LOCK+borrowId);
            return Response.success();
        }catch (Exception e){
            logger.info("标的{},锁标异常{}",borrowId, e);
        }finally{
            redisDistributeLockHandler.unlock(Constant.BORROW_LOCK+borrowId);
        }
         return Response.errorMsg("该标的剩余金额已有其他用户正在购买，请选择其他标的购买或稍后再试!");
        */
        return Response.success();
    }

    public Response agreeCanBePay(String userId,String borrowId, Double buyAmt){
    /*    FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(orderId);
        if( null == financeBidPO){
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}投资记录不存在", userId, orderId);
            return Response.errorMsg("购买记录不存在");
        }*/

        BorrowPO borrowPO = borrowDao.getBorrowById(Integer.valueOf(borrowId));
        if( null == borrowPO){
            logger.warn("用户{}购买支付，判读是否可以支付,borrowId={}标的不存在", userId, borrowId);
            return Response.errorMsg("标的不存在");
        }

        if( borrowPO.getRemmoney() < buyAmt ){
            return Response.errorMsg("可购买金额不足,请选择其他标的!");
        }

        // 判断可投金额小于最小额度限制时 最小额度不做判断 account - acccount_yes > lowestAccount
        if (borrowPO.getRemmoney() < Double.valueOf(borrowPO.getLowestAccount())) {
            logger.warn("用户{}购买{}标的。剩余金额不足，请投其他标的!", userId, borrowId);
            return Response.errorMsg("剩余金额不足，请投其他标的!");
        }

        if (Double.valueOf(buyAmt) < Double.valueOf(borrowPO.getLowestAccount())) {
            logger.warn("用户{}购买{}标的。购买金额不能小于起投金额!", userId, borrowId);
            return Response.errorMsg("购买金额不能小于起投金额!");
        }

        if (Double.valueOf(buyAmt) > Double.valueOf(borrowPO.getRemmoney())) {
            logger.warn("用户{}购买{}标的。超过可购金额!", userId, borrowId);
            return Response.errorMsg("超过可购金额!");
        }

        if (Double.valueOf(borrowPO.getMostAccount()) != 0 && Double.valueOf(buyAmt) > Double.valueOf(borrowPO.getMostAccount())) {
            logger.warn("用户{}购买{}标的。超过限额!", userId, borrowId);
            return Response.errorMsg("超过限额!");
        }

    /**
        try {
            //加锁
            redisDistributeLockHandler.lock(Constant.AGREE_BORROW_LOCK+borrowId);

            String amt = buyAmt.toString();
            String rmoney = userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId) != null ? userStrHashRedisTemplate.get(Constant.NEW_PURCHASE_AMT + borrowId) : borrowPO.getRemmoney().toString();
            logger.info("标的{}的可购买金额rmoney={}, amt={},Remmoney={}", borrowId, rmoney, amt, borrowPO.getRemmoney());
            if (Double.valueOf(rmoney) - Double.valueOf(amt) < 0) {
                logger.warn("用户{}购买{}标的。该标的剩余金额已有其他用户正在购买，请选择其他标的购买!", userId, borrowId);
                redisDistributeLockHandler.unlock(Constant.AGREE_BORROW_LOCK+borrowId);
                return Response.errorMsg("该标的剩余金额已有其他用户正在购买，请选择其他标的购买或稍后再试!");
            }
            //保存可购金额
            String canPayMoney=String.valueOf(Double.valueOf(rmoney) - Double.valueOf(amt));
            logger.info("111=================================canBePay={},rmoney={},amt={}",canPayMoney,rmoney,amt);
            userStrHashRedisTemplate.set(Constant.NEW_PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(borrowPO.getRemmoney()) - Double.valueOf(amt)));
//            userStrHashRedisTemplate.set(Constant.NEW_PURCHASE_AMT + borrowId, canPayMoney);

            //解锁
            redisDistributeLockHandler.unlock(Constant.AGREE_BORROW_LOCK+borrowId);
            return Response.success();
        }catch (Exception e){
            logger.info("标的{},锁标异常{}",borrowId, e);
        }finally{
            redisDistributeLockHandler.unlock(Constant.AGREE_BORROW_LOCK+borrowId);
        }
        return Response.errorMsg("该标的剩余金额已有其他用户正在购买，请选择其他标的购买或稍后再试!");
     */
    return Response.success();
    }


    public Response recoveryAmt(String userId,String orderId){
        FinanceBidPO financeBidPO = financeBidDao.getFidByOrderId(orderId);
        if( null == financeBidPO){
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}投资记录不存在", userId, orderId);
            return Response.errorMsg("购买记录不存在");
        }

        BorrowPO borrowPO = borrowDao.getBorrowById(financeBidPO.getBorrowId());
        if( null == borrowPO){
            logger.warn("用户{}购买支付，判读是否可以支付,orderId={}标的不存在", userId, orderId);
            return Response.errorMsg("标的不存在");
        }

        if( borrowPO.getRemmoney() < financeBidPO.getBuyAmt().doubleValue() ){
            return Response.error();
        }

        String borrowId = String.valueOf(borrowPO.getId());
        Double amt = financeBidPO.getBuyAmt() != null ? financeBidPO.getBuyAmt().doubleValue() : 0D;
        Double hbamt = financeBidPO.getCouponAmt() != null ? financeBidPO.getCouponAmt().doubleValue() : 0D;
        logger.info("订单{},支付失败，开始关闭订单，恢复标的{}中的可购买金额, amt={}, hbamt={}", orderId, financeBidPO.getBorrowId(), amt, hbamt);
        String rmoney = userStrHashRedisTemplate.get(Constant.PURCHASE_AMT + borrowId);
        if (null != rmoney) {
            userStrHashRedisTemplate.set(Constant.PURCHASE_AMT + borrowId, String.valueOf(Double.valueOf(rmoney) + amt));
        }
        logger.info("订单{},支付失败，关闭订单结束，标的{}可购买金额已恢复,rmoney={},amt={}, hbamt={}", orderId, borrowId, rmoney, amt, hbamt);

        return Response.success();

    }


}
