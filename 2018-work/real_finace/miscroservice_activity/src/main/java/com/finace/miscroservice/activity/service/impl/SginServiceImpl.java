package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.CreditDao;
import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.dao.SginDao;
import com.finace.miscroservice.activity.dao.SginLogDao;
import com.finace.miscroservice.activity.po.CreditLogPO;
import com.finace.miscroservice.activity.po.CreditPO;
import com.finace.miscroservice.activity.po.SginLogPO;
import com.finace.miscroservice.activity.po.SginPO;
import com.finace.miscroservice.activity.service.SginService;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Constant;
import com.finace.miscroservice.commons.utils.PageUtils;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签到实现层
 */
@Service
public class SginServiceImpl implements SginService {
    private Log logger = Log.getInstance(SginServiceImpl.class);

    @Autowired
    private SginDao sginDao;

    @Autowired
    private SginLogDao sginLogDao;

    @Autowired
    private CreditDao creditDao;

    @Autowired
    private CreditLogDao creditLogDao;


    @Transactional
    @Override
    public Response doSign(String userId) {
        Integer isSgin = sginDao.getNowSginByUser(userId);
        if (isSgin > 0) {
            logger.info("用户{},今日已签到，不能重复签到", userId);
            return Response.errorMsg("今日已签到，不能重复签到");

        } else {
            try {
                SginPO oSgin = sginDao.getSginByUser(userId);
                Integer conNum = 1; //天数
                if (null != oSgin) {
                    String fdate = DateUtils.dateAndDay(DateUtils.getNowTimeStr(), "-1");
                    String toDay = DateUtils.dateStr2( DateUtils.getNowTimeStr());
                    String ddate = DateUtils.dateStr2(oSgin.getAddtime());
                    //这次签到和上次签到时间是否在同一个月，在一个月并且在上次签到时间的后一天，就认为是连续签到
                    if (DateUtils.dateEqMonth(toDay, ddate) && fdate.equals(ddate)) {
                        conNum = oSgin.getNumber() + conNum;
                        oSgin.setNumber(conNum); //连续签到天数
                        oSgin.setTotalNumber(oSgin.getTotalNumber() + 1); //签到总天数
                    } else {
                        oSgin.setNumber(1); //连续签到天数
                        oSgin.setTotalNumber(1); //签到总天数
                    }
                    oSgin.setAddtime(DateUtils.getNowTimeStr()); //签到时间
                    this.sginDao.updateSgin(oSgin);
                } else {
                    SginPO sgin = new SginPO();
                    sgin.setUserId(Integer.valueOf(userId));
                    sgin.setUsername(userId);
                    sgin.setTotalNumber(1); //签到总天数
                    sgin.setAddtime(DateUtils.getNowTimeStr()); //签到时间
                    sgin.setNumber(1); //连续签到天数
                    this.sginDao.saveSgin(sgin);
                }

                //送金豆
                Integer giveJf = this.giveJd(userId, conNum);

                //新增用户签到日志
                SginLogPO sginLog = new SginLogPO();
                sginLog.setUserId(Integer.valueOf(userId));
                sginLog.setUsername(userId);
                sginLog.setAddtime(DateUtils.getNowTimeStr());
                sginLog.setRemark(giveJf + "".trim()); //签到送的金豆值
                this.sginLogDao.saveSginLog(sginLog);

                logger.info("用户{},今日签到成功", userId);

                return Response.successDataMsg(giveJf, "签到成功");
            } catch (Exception e) {
                logger.error("用户{},签到失败,异常信息{}", userId, e);
                return Response.errorMsg("签到失败");
            }

        }
    }

    /**
     * 送金豆
     * userId 用户id
     * num 天数
     */
    private Integer giveJd(String userId, Integer num) {
        /*
        第1天 10积分
        第2天 20积分
        第3天 30积分
        第4天 40积分
        第5天以上 50积分
        */

        Integer signJd = 0; //送的积分
        Integer totalJd = 0; //总金豆
        if (num == 1) {
            signJd = 10;
        } else if (num == 2) {
            signJd = 20;
        } else if (num == 3) {
            signJd = 30;
        } else if (num == 4) {
            signJd = 40;
        } else if (num >= 5) {
            signJd = 50;
        }

        String remark = "连续签到" + num + "天，送" + signJd + "金豆";
        CreditPO userCredit = creditDao.getCreditByUserId(userId);
        //送金豆
        CreditLogPO log = new CreditLogPO();
        if (null == userCredit) {
            totalJd=signJd;
            CreditPO credit = new CreditPO();
            credit.setUserId(Long.valueOf(userId));
            credit.setOpUser(0);
            credit.setAddtime(Long.valueOf(DateUtils.getNowTimeStr()));
            credit.setValue(signJd);
            credit.setTenderValue(0);
            creditDao.saveCredit(credit);
            log.setValue(0); //原金豆值
        } else {
            this.creditDao.updateCreditAddByUserId(userId, String.valueOf(signJd));
            log.setValue(userCredit.getValue()); //原金豆值
            totalJd = signJd + Integer.valueOf(userCredit.getValue());
        }

        log.setUser_id(Long.valueOf(userId));
        log.setType_id(25); //签到送
        log.setOp(1);
        log.setRemark(remark);
        log.setAddtime(Long.valueOf(DateUtils.getNowTimeStr()));
        log.setTotal(totalJd.toString());

        //添加金豆记录日志
        this.creditLogDao.saveCreditLog(log);

        return signJd;
    }

    @Override
    public Response getSginDay(String userId) {
        Map<String, Object> map = new HashMap<>();
        List<String> days = sginLogDao.getSginLogMonthByUser(userId);
        map.put("days", days);
        CreditPO userCredit = creditDao.getCreditByUserId(userId);
        if( null != userCredit ){
            map.put("beanVal", userCredit.getValue()); //金豆值
        }else{
            map.put("beanVal", "0"); //金豆值
        }

        SginPO oSgin = sginDao.getSginByUser(userId);
        if( null != oSgin ){
            map.put("sginNum", oSgin.getNumber()); //签到天数
        }else{
            map.put("sginNum", "0"); //签到天数
        }

        return Response.success(map);
    }

    @Override
    public Response getGoldPeasLog(Integer page, String userId) {
        //获取金豆签到获取日志
        PageHelper.startPage(page,10);
        List<CreditLogPO> list = creditLogDao.getCreditLogByUserId(userId);
        PageUtils<CreditLogPO> pageUtils = new PageUtils<>(list);
        return Response.success(pageUtils);
    }


}







