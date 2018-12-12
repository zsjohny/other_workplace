package com.finace.miscroservice.borrow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.borrow.dao.BorrowDao;
import com.finace.miscroservice.borrow.dao.FinanceBidDao;
import com.finace.miscroservice.borrow.entity.enums.BorrowTabEnums;
import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.LoanMoneyService;
import com.finace.miscroservice.commons.entity.AccountLogPo;
import com.finace.miscroservice.commons.entity.CreditGoAccount;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.credit.CreditConstant;
import com.finace.miscroservice.commons.utils.credit.CreditUtils;
import com.finace.miscroservice.commons.utils.credit.DateUtil;
import com.finace.miscroservice.commons.utils.credit.SignUtil;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanMoneyServiceImpl implements LoanMoneyService {
    private static Log logger = Log.getInstance(LoanMoneyServiceImpl.class);

    @Autowired
    private CreditUtils creditUtils;

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private FinanceBidDao financeBidDao;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Value("${base.host}")
    private String baseHost;

    @Value("${creditgo.hb.account.id}")
    private String hbAccountId;


    @Override
    public void LoanMoney(String borrowId) {

        //BorrowPO borrowPO = borrowDao.getBorrowByStatusId(borrowId, "1");
        BorrowPO borrowPO = borrowDao.getBorrowById(Integer.valueOf(borrowId));

        if (borrowPO == null) {
            logger.warn("放款失败，标的{}不存在", borrowId);
            return;
        }

        if (borrowPO.getRemmoney() > 0) {
            logger.warn("标的{}还没有满标，rmmoney={}", borrowId, borrowPO.getRemmoney());
            return;
        }

        CreditGoAccount creditGoAccount = userRpcService.getUserAccountByUserId(String.valueOf(borrowPO.getUserId()));
        if (creditGoAccount == null) {
            logger.warn("标的{}放款失败，借款人{}没有开通存管账户", borrowId, borrowPO.getUserId());
            return;
        }

        //获取投资记录
        List<FinanceBidPO> flist = financeBidDao.getRealFidByBorrowId(borrowId);
        if (flist != null && flist.size() > 0) {
            if (Integer.valueOf(borrowPO.getTenderTimes()) != flist.size()) {
                logger.info("标的{}的投资笔数和标的记录的投资笔数不相同，tenderTimes={},", borrowPO.getTenderTimes(), borrowPO.getRemmoney());
                return;
            }

            Map<String, String> reqMap = new TreeMap<>();
            creditUtils.getHeadReq(reqMap);
            reqMap.put("txCode", "batchLendPay");

            reqMap.put("batchNo", borrowId);   //批次号
            reqMap.put("txAmount", borrowPO.getAccountYes());   //交易金额
            reqMap.put("txCounts", borrowPO.getTenderTimes());  //交易笔数
            reqMap.put("notifyURL", baseHost + "/borrow/loanMoneyNotify");  //合法性校验通知链接
            reqMap.put("retNotifyURL", baseHost + "/borrow/loanMoneyRetNotify");  //业务结果通知

            JSONArray aJson = new JSONArray();
            String lastGrantHbAccount = null;
            for (FinanceBidPO financeBidPO : flist) {
                CreditGoAccount userAccount = userRpcService.getUserAccountByUserId(String.valueOf(financeBidPO.getUserId()));
                if (userAccount == null) {
                    logger.warn("标的{}放款失败，投资人{}没有开通存管账户", borrowId, financeBidPO.getUserId());
                    continue;
                }

                JSONObject json = new JSONObject();
                json.put("accountId", userAccount.getAccountId());
                json.put("orderId", CreditConstant.loanId + financeBidPO.getOrderSn());
                json.put("txAmount", financeBidPO.getBuyAmt());
//                json.put("bidFee", oId);
//                json.put("debtFee", oId);
                json.put("forAccountId", creditGoAccount.getAccountId());
                json.put("productId", CreditConstant.productIdPre + borrowId);
                json.put("authCode", financeBidPO.getSummary());
                aJson.add(json);
                if (lastGrantHbAccount == null) {
                    lastGrantHbAccount = userAccount.getAccountId();
                }
            }

            reqMap.put("subPacks", aJson.toString());

            //生成待签名字符串
            String requestMapMerged = creditUtils.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);

            try {
                logger.info("投标请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
                Map map = creditUtils.sendRequest(CreditUtils.URI + "/batchLendPay", reqMap);
                //放款成功处理
                if (map != null && "success".equals(map.get("received"))) {   //received
                    logger.info("标的{}，放款成功", borrowId);

                    borrowPO.setStatus(7);//已放款
                    //给该标的最后一笔的投资的用户发放现金红包
                    doGrantHb(lastGrantHbAccount, "10");

                } else {
                    logger.error("标的{}，放款失败", borrowId);
                }

                borrowPO.setSuccessTime(DateUtils.getNowTimeStr());  //放款时间
                borrowPO.setEndTime(DateUtils.getTimeStr(DateUtils.rollDay(DateUtils.getDate(DateUtils.getNowTimeStr()), Integer.valueOf(borrowPO.getTimeLimitDay())+1)));
                borrowDao.updateAllBorrow(borrowPO);
            } catch (Exception e) {
                logger.error("标的{}放款异常信息{}", borrowId, e);
            }
        }
    }


    /**
     * 给该标的最后一笔的投资的用户发放现金红包
     */
    public void doGrantHb(String toAccountId, String hbMoney) {

        Map<String, String> reqMap = new TreeMap<>();
        creditUtils.getHeadReq(reqMap);
        reqMap.put("txCode", "voucherPay");
        reqMap.put("accountId", hbAccountId);   //红包电子账号
        reqMap.put("txAmount", hbMoney);   //交易金额
        reqMap.put("forAccountId", toAccountId);  //对手电子账号
        reqMap.put("desLineFlag", "1");  //是否使用交易描述  1-使用 0-不使用
        reqMap.put("desLine", "标的最后一笔的投资的用户发放10元现金红包");  //交易描述
        try {
            logger.info("开始给toAccountId={}发放红包hbMoney={}", toAccountId, hbMoney);

            //生成待签名字符串
            String requestMapMerged = creditUtils.mergeMap(reqMap);
            //生成签名
            String sign = SignUtil.sign(requestMapMerged);
            reqMap.put("sign", sign);
            logger.info("投标请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
            Map map = creditUtils.sendRequest(CreditUtils.URI + "/voucherPay", reqMap);

            //发放现金红包 成功
            if (map != null && hbAccountId.equals(map.get("accountId")) && hbMoney.equals(map.get("txAmount"))) {   //received
                CreditGoAccount creditGoAccount = userRpcService.getUserAccountByAccountId(toAccountId);
                //添加资金流水记录
                AccountLogPo accountLog  = new AccountLogPo();
                accountLog.setUserId(creditGoAccount.getUserId());  // loan_hb 红包
                accountLog.setTxMoney(Double.valueOf(hbMoney));
                accountLog.setTxCode("loan_hb");
                accountLog.setIsSuccess(0);  //投资成功
                accountLog.setAccountId(toAccountId);
                userRpcService.saveCallBack(accountLog);

                logger.info("给toAccountId={}发放红包hbMoney={},成功", toAccountId, hbMoney);
            } else {
                logger.error("给toAccountId={}发放红包hbMoney={},失败", toAccountId, hbMoney);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("给toAccountId={}发放红包hbMoney={},异常信息{}", toAccountId, hbMoney, e);
        }
    }

    @Override
    public void AutoUpAgreeBorrow(String borrowGroup) {

        //自动上标处理
        List<BorrowPO> bList = this.borrowDao.getBorrowPOBySellOut(borrowGroup);
        logger.info("协议支付 重复上标记录自动上标：分组={}是否有该标的={}", borrowGroup, bList.size());
        if (bList.size() == 0) {
            BorrowPO borrowPO = this.borrowDao.getBorrowAgreeByAutoAdd(borrowGroup);
            if (null == borrowPO) {
                logger.warn("协议支付 发布标的分组{}失败,没有可以发布的标的", borrowGroup);
                return;
            }
            if (borrowPO.getLitpic().equals(BorrowTabEnums.TAB_BKJX.getValue())){
                logger.warn("活动标签标 不允自动上标");
                return;
            }
            logger.info("协议支付 重复上标记录自动上标开始：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
//                borrowPO.setStatus(1);//审核通过
//                borrowPO.setVerifyRemark("自动审核通过");
//                borrowPO.setVerifyTime(DateUtils.getNowTimeStr());
//                this.borrowDao.updateAutoCheckBorrow(borrowPO);

            this.doUpAgreeBorrow(borrowPO);
            logger.info("协议支付 重复上标记录自动上标结束：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
        }

    }

    private void doUpAgreeBorrow(BorrowPO borrowPO) {
        borrowPO.setStatus(1);
        Map<String,String> map = new HashMap<>();
        map.put("status","1");
        map.put("id",String.valueOf(borrowPO.getId()));
        borrowDao.updateBorrowStatusById(map);
    }

    @Override
    public void AutoUpBorrow(String borrowGroup) {

        //自动上标处理
        List<BorrowPO> bList = this.borrowDao.getBorrowPOBySellOut(borrowGroup);
        logger.info("重复上标记录自动上标：分组={}是否有该标的={}", borrowGroup, bList.size());
        if (bList.size() == 0) {
            BorrowPO borrowPO = this.borrowDao.getBorrowByAutoAdd(borrowGroup);
            if (null == borrowPO) {
                logger.warn("发布标的分组{}失败,没有可以发布的标的", borrowGroup);
                return;
            }

            logger.info("重复上标记录自动上标开始：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
//                borrowPO.setStatus(1);//审核通过
//                borrowPO.setVerifyRemark("自动审核通过");
//                borrowPO.setVerifyTime(DateUtils.getNowTimeStr());
//                this.borrowDao.updateAutoCheckBorrow(borrowPO);

            this.doUpBorrow(borrowPO);
            logger.info("重复上标记录自动上标结束：分组={}标的信息id={},名称={} ", borrowPO.getBorrow_group(), borrowPO.getId(), borrowPO.getName());
        }

    }

    /**
     * 上架标的
     *
     * @param borrowPO
     */
    private void doUpBorrow(BorrowPO borrowPO) {

        CreditGoAccount creditGoAccount = userRpcService.getUserAccountByUserId(String.valueOf(borrowPO.getUserId()));
        if (null == creditGoAccount) {
            logger.warn("发布标的{}失败,借款人{}没有在银行存管开户", borrowPO.getId(), borrowPO.getUserId());
        }

        logger.info("开始登记标的{},name={},userid={}", borrowPO.getId(), borrowPO.getName(), borrowPO.getUserId());
        try {
            Map<String, String> reqMap = new TreeMap<>();
            creditUtils.getHeadReq(reqMap);
            reqMap.put("txCode", CreditConstant.debtRegister);
            reqMap.put("accountId", creditGoAccount.getAccountId());   //电子账号
            reqMap.put("productId", CreditConstant.productIdPre + borrowPO.getId());   //标的号
            reqMap.put("productDesc", borrowPO.getName()); //标的描述
            reqMap.put("raiseDate", DateUtils.dateStr5(DateUtils.getNowTimeStr()));   //募集日

            String raiseEndDate = DateUtils.dateStr5(DateUtils.rollDay(DateUtils.getDate(DateUtils.getNowTimeStr()), Integer.valueOf(borrowPO.getValidTime())));
            reqMap.put("raiseEndDate", raiseEndDate);//募集结束日期
            reqMap.put("intType", "0");     //0-到期与本金一起归还
            reqMap.put("duration", String.valueOf(borrowPO.getTimeLimitDay()));    //借款期限  天数，从满标日期开始计算
            reqMap.put("txAmount", String.valueOf(borrowPO.getAccount()));    //交易金额
            reqMap.put("rate", String.valueOf(borrowPO.getApr()));        //年化利率
//           reqMap.put("txFee", "0");       //平台手续费

            Map map = creditUtils.sendRequest(CreditUtils.URI + "/debtRegister", reqMap);
            if (map != null && map.get("retCode").equals(CreditConstant.CREDIT_SUCCESS)) {

                borrowPO.setStatus(1);
                borrowPO.setEachTime(DateUtils.dateStr2(DateUtils.rollDay(DateUtils.getDate(DateUtils.getNowTimeStr()), Integer.valueOf(borrowPO.getValidTime()))));
                borrowDao.updateBorrow(borrowPO);
                logger.info("发布标的{}成功,name={},userid={}", borrowPO.getId(), borrowPO.getName(), borrowPO.getUserId());
            } else {
                logger.warn("发布标的{}失败,name={},userid={},retCode={},map={}", borrowPO.getId(), borrowPO.getName(), borrowPO.getUserId(), map.get("retCode"), map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发布标的登记接口调用异常：{}", e);
        }

    }


}
