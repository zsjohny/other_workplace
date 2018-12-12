package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.CreditDao;
import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.po.CreditLogPO;
import com.finace.miscroservice.activity.po.CreditPO;
import com.finace.miscroservice.activity.service.CreditService;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 金豆service 实现层
 */
@Service
public class CreditServiceImpl implements CreditService {
    private static Log logger = Log.getInstance(CreditServiceImpl.class);

    private static final int BASEBEAN = 100;
    private static final String BASEREMARK = "用户每日首次分享,奖励100金豆";


    @Autowired
    private CreditDao creditDao;

    @Autowired
    private CreditLogDao creditLogDao;


    @Transactional
    @Override
    public void investGrantGlodBean(String userId, Integer investType, Double amt) {
        logger.info("用户{}投资{},开始送金豆, investType={}", userId, amt, investType);
        Double peas = NumberUtil.divide(0,NumberUtil.multiply(0,investType,amt),365);

        if (peas > 0) {
            logger.info("用户{}投资{},开始送金豆, 金豆bean={}", userId, amt, peas);
            this.grantGlodBean(userId, peas.intValue(), "投资" + amt + "元,送" + peas + "金豆");
        }
    }

    @Transactional
    @Override
    public Response shareGrantGlodBean(String userId) {
        try {
            this.grantGlodBean(userId, BASEBEAN, BASEREMARK);
            return Response.successMsg("分享送金豆成功");
        } catch (Exception e) {
            logger.error("用户{}, 分享送金豆失败，异常信息{}", userId, e);
            return Response.error("分享送金豆失败");
        }
    }


    /**
     * 送金豆
     *
     * @param userId
     * @param bean
     * @param remark
     */
    private void grantGlodBean(String userId, Integer bean, String remark) {
        CreditPO userCredit = creditDao.getCreditByUserId(userId);
        //送金豆
        CreditLogPO log = new CreditLogPO();
        if (null == userCredit) {
            CreditPO credit = new CreditPO();
            credit.setUserId(Long.valueOf(userId));
            credit.setOpUser(0);
            credit.setAddtime(Long.valueOf(DateUtils.getNowTimeStr()));
            credit.setValue(bean);
            credit.setTenderValue(0);
            creditDao.saveCredit(credit);
            log.setValue(0); //原金豆值
        } else {
            this.creditDao.updateCreditAddByUserId(userId, String.valueOf(bean));
            log.setValue(userCredit.getValue()); //原金豆值
            bean = bean + Integer.valueOf(userCredit.getValue());
        }

        log.setUser_id(Long.valueOf(userId));
        log.setType_id(22); //投资送
        log.setOp(1);
        log.setRemark(remark);
        log.setAddtime(Long.valueOf(DateUtils.getNowTimeStr()));
        log.setTotal(bean.toString());
        //添加金豆记录日志
        this.creditLogDao.saveCreditLog(log);

        logger.info("用户{},{}", userId, remark);
    }


}

























