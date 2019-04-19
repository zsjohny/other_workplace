package com.e_commerce.miscroservice.activity.service.impl;

import com.e_commerce.miscroservice.activity.PO.ChannelOrderRecordVO;
import com.e_commerce.miscroservice.activity.dao.ChannelOrderRecordDao;
import com.e_commerce.miscroservice.activity.dao.ChannelUserDao;
import com.e_commerce.miscroservice.activity.dao.ChannelUserFansDao;
import com.e_commerce.miscroservice.activity.dao.ChannelUserGatherDao;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelOrderRecord;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUser;
import com.e_commerce.miscroservice.activity.entity.channel.ChannelUserFans;
import com.e_commerce.miscroservice.activity.service.ChannelUserService;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 9:41
 * @Copyright 玖远网络
 */
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    private Log logger = Log.getInstance(ChannelUserServiceImpl.class);

    @Autowired
    private ChannelOrderRecordDao channelOrderRecordDao;
    @Autowired
    private ChannelUserDao channelUserDao;
    @Autowired
    private ChannelUserGatherDao channelUserGatherDao;
    @Autowired
    private ChannelUserFansDao channelUserFansDao;

    @Value("${xiaoFenDie.storeId}")
    private Long xiaoFenDieStoreId;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fansPaySuccess(ChannelOrderRecordVO vo) {
        logger.info("粉丝支付成功 request={}", vo);
        long shopMemberId = vo.getShopMemberId();
        long orderId = vo.getOrderId();
        long storeId = vo.getStoreId();
        //892测试账号
        if (storeId - xiaoFenDieStoreId != 0 &&  storeId - 892 != 0) {
            logger.info("不是小粉蝶或测试账号的订单");
            return;
        }

        ChannelUserFans fans = channelUserFansDao.findByFansId(shopMemberId);
        if (BeanKit.hasNull(fans)) {
            logger.info("没有粉丝信息");
            return;
        }

        long channelUserId = fans.getChannelUserId();
        logger.info("查询渠道商用户id={}", channelUserId);
        ChannelUser channelUser = channelUserDao.findNormalById(channelUserId);
        if (channelUser.getPartnerStatus().equals(0)) {
            logger.info("合作终止");
            return;
        }

        ChannelOrderRecord orderHistory = channelOrderRecordDao.findByOrderId(orderId);
        ErrorHelper.declare(orderHistory == null, "订单记录已存在");

        int upd = channelUserFansDao.payOrderSuccess(shopMemberId, 1, false);
        ErrorHelper.declare(upd == 1, "更细粉丝下单数量失败");

        logger.info("更新渠道商统计信息");
        Integer orderCount = fans.getShopMemberOrderCount();
        int orderFansCount = orderCount > 0 ? 0 : 1;
        channelUserGatherDao.appendByChannelUserId(channelUserId, 0, orderFansCount, 1, false);

        logger.info("记录订单");
        ChannelOrderRecord newOrderRec = new ChannelOrderRecord();
        newOrderRec.setChannelUserId(channelUserId);
        newOrderRec.setOrderId(orderId);
        newOrderRec.setShopMemberId(shopMemberId);
        int save = channelOrderRecordDao.save(newOrderRec);
        ErrorHelper.declare(save == 1, "记录订单信息失败");

    }
}
