package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.UUIdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.dao.ShopMemberDao;
import com.e_commerce.miscroservice.operate.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.operate.service.user.StoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/14 13:48
 * @Copyright 玖远网络
 */
@Service
public class StoreBusinessServiceImpl implements StoreBusinessService {

    private Log logger = Log.getInstance(StoreBusinessServiceImpl.class);
    @Autowired
    private StoreBusinessDao storeBusinessDao;
    @Autowired
    private ShopMemberDao shopMemberDao;

    /**
     * 充值
     * @param memberId
     * @param money
     * @return
     */
    @Override
    public Response storeAccountRecharge(Long memberId, Double money) {

        logger.info("userId={}金额操作={}",memberId,money);
        Member member = shopMemberDao.findMemberById(memberId);
        if (member==null){
            logger.warn("会员不存在");
            return Response.errorMsg("会员不存在");
        }
        YjjStoreBusinessAccount yjjStoreBusinessAccount = storeBusinessDao.findAccountByUserId(member.getUserId());
        Double remainderMoney = 0d;
        if (yjjStoreBusinessAccount==null){
            if (money<0){
                logger.warn("金额错误={}",money);
                return Response.errorMsg("金额错误");
            }
            logger.info("创建新账户");
            storeBusinessDao.addStroeBusinessAccount(member.getUserId());
        }
        if (money<0&&yjjStoreBusinessAccount.getUseMoney()<money){
            logger.warn("操作金额小于可用金额");
            return Response.errorMsg("操作金额小于可用金额");
        }
        if (yjjStoreBusinessAccount!=null){
            remainderMoney=yjjStoreBusinessAccount.getCountMoney();
        }
        String uuid = UUIdUtil.generateOrderNo();
        storeBusinessDao.updateStoreBusinessAccountMoney(member.getUserId(),money);
        YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
        yjjStoreBusinessAccountLog.setUserId(member.getUserId());
        yjjStoreBusinessAccountLog.setStatusType(0);
        yjjStoreBusinessAccountLog.setInOutType(0);
        yjjStoreBusinessAccountLog.setRemainderMoney(remainderMoney);
        yjjStoreBusinessAccountLog.setType(0);
        yjjStoreBusinessAccountLog.setOperMoney(money);
        yjjStoreBusinessAccountLog.setRemarks("平台充值");
        yjjStoreBusinessAccountLog.setOrderNo(uuid);
        storeBusinessDao.saveStoreBusinessAccountLog(yjjStoreBusinessAccountLog);
        return Response.success();
    }
}
