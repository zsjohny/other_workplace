package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.BorrowTender;
import com.finace.miscroservice.commons.entity.FinanceMoney;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.enums.MsgCodeEnum;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.Rc4Utils;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.MD5;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.message.SendMessageUtil;
import com.finace.miscroservice.user.config.LoadRealm;
import com.finace.miscroservice.user.dao.PcUserDao;
import com.finace.miscroservice.user.dao.UserDao;
import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.entity.response.*;
import com.finace.miscroservice.user.po.UserPO;
import com.finace.miscroservice.user.rpc.ActivityRpcService;
import com.finace.miscroservice.user.rpc.BorrowRpcService;
import com.finace.miscroservice.user.service.PcUserService;
import com.finace.miscroservice.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.finace.miscroservice.commons.enums.MqChannelEnum.NEW_USER_GRANT_HB;


/**
 * 用户的service 实现层
 */
@Service
public class PcUserServiceImpl implements PcUserService {
    Logger logger = LoggerFactory.getLogger(PcUserServiceImpl.class);
    private static Integer DEFAULT_PAGE_SIZE=10;

    @Autowired
    private PcUserDao pcUserDao;
    @Autowired
    private UserDao userDao;
    @Resource
    private BorrowRpcService borrowRpcService;
    @Value("${user.shareKey:11}")
    protected String shareKey;
    @Value("${user.server.status:111}")
    protected String ustatus;


    @Override
    public Response pcAccountLog(String startTime,String endTime,Integer page,Integer userId) {

        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<AccountLogResponse> list = pcUserDao.pcAccountLog(startTime,endTime,userId);
        PageInfo<AccountLogResponse> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

    @Override
    public Response pcMyProperty(Integer userId) {
        MyPropertyResponse response =  pcUserDao.pcMyProperty(userId);
//        总收益和累计投资总额参考APP端接口函数homeIndex
        double totalInterest = 0d; //总收益
        double investTotal = 0d; //投资总额

        BorrowTender borrowTender = borrowRpcService.getBorrowTenderMoneyInfo(userId.toString());
        if (null != borrowTender) {
            totalInterest = NumberUtil.add(totalInterest, borrowTender.getInterestPast());
            investTotal = NumberUtil.add(investTotal, borrowTender.getAccountPast());
        }

        FinanceMoney financeMoney = borrowRpcService.getFinanceMoneyInfo(userId.toString());
        if (null != financeMoney) {
            totalInterest = NumberUtil.add(totalInterest, financeMoney.getNowProfit().doubleValue());
            investTotal = NumberUtil.add(investTotal, financeMoney.getPrincipal().add(financeMoney.getWithdrawPrincipal()).doubleValue());
        }
        response.setAccumulatedIncome(totalInterest);
        response.setCumulativeInvestmentAmount(investTotal);
        logger.info("总收益"+totalInterest+"总投资额"+investTotal);


        return Response.success(response);
    }

    @Override
    public Response pcBackMoney(Integer userId, String month) {
        if (month==null){
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }
        FinanceMoneyResponse response = pcUserDao.pcBackMoney(userId,month);
        return Response.success(response);
    }

    @Override
    public Response pcMyCoupons(Integer type, Integer page, Integer userId) {
        if (page==null||page<1){
            logger.warn("页码错误page={}",page);
            return Response.errorMsg("页码错误page="+page);
        }

        if (type==null){
            logger.warn("参数错误");
            return Response.errorMsg("参数错误");
        }
        /**
         * 我的优惠券
         */
        logger.info("我的优惠券");
        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<MyCouponsResponse> response = pcUserDao.pcMyCoupons(type,userId);
        PageInfo<MyCouponsResponse> pageInfo = new PageInfo<>(response);
        return Response.success(pageInfo);
    }

    @Override
    public Response myInvitation(Integer userId) {
        logger.info("用户{}邀请好友获取的红包个数和红包总金额", userId);
        UserPO user = userDao.findUserOneById(String.valueOf(userId));
        MyInvitationResponse response = pcUserDao.myInvitation(userId);
        response.setShareId( Rc4Utils.toHexString(user.getPhone(), shareKey));
        return Response.success(response);
    }

    @Override
    public Response myRewardsRecord(Integer userId, Integer page) {
        logger.info("用户{}邀请好友投资奖励记录", userId);

        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<UserPO> list =  userDao.getUserListByInviter(userId);
        List<MyRewardsRecordResponse> list2 =  new ArrayList<>();
        for (UserPO user : list){
            MyRewardsRecordResponse myRewardsRecordResponse = new MyRewardsRecordResponse();
            myRewardsRecordResponse.setPhone(user.getPhone());
            UserRedPackets userRedPackets = pcUserDao.getUserIdInviter(userId, user.getUser_id());
            if (userRedPackets!=null){
                myRewardsRecordResponse.setMoney(userRedPackets.getHbmoney());
                String my = userRedPackets.getHbdetail() != null && userRedPackets.getHbdetail() != "0" ? userRedPackets.getHbdetail() : "--";
                myRewardsRecordResponse.setContent(my);
            }else {
                Double firstAmt = borrowRpcService.getUserFirstBuyAmt(user.getUser_id());
                myRewardsRecordResponse.setContent(String.valueOf(firstAmt != 0 ? firstAmt : null));
                myRewardsRecordResponse.setMoney(0d);
            }
            list2.add(myRewardsRecordResponse);
        }
        return Response.success(list2);
    }

    @Override
    public Response myInformation(Integer userId) {
        MyInformationResponse response = pcUserDao.myInformation(userId);
        return Response.success(response);
    }

    @Override
    public Response myFinanceBid(Integer page, String userId) {

        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<MyFinanceBidResponse> response = pcUserDao.myFinanceBid(userId);
        PageInfo<MyFinanceBidResponse> pageInfo = new PageInfo<>(response);
        return Response.success(pageInfo);
    }

    @Override
    public Response register(String username, String pass) {
        Register register = pcUserDao.findRegisterTmp(username);
        if (register==null){
            pcUserDao.register(username,pass);
        }else {
            return Response.errorMsg("已注册，请下载APP激活验证");
        }
        return Response.success();
    }

    @Override
    public Response myBorrowinfoById(String userId,Integer type,Integer page) {

        PageHelper.startPage(page,DEFAULT_PAGE_SIZE);
        List<MyBorrowInfoResponse> response = pcUserDao.myBorrowinfoById(userId,type);
        PageInfo<MyBorrowInfoResponse> pageInfo = new PageInfo<>(response);
        return Response.success(pageInfo);
    }
    @Override
    public  MyBorrowInfoResponse getInfoByBorrowId(Integer borrowId) {

        MyBorrowInfoResponse borrowinfo = pcUserDao.getInfoByBorrowId(borrowId);
        return borrowinfo;
    }

}


