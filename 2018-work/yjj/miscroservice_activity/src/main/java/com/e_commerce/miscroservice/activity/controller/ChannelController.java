package com.e_commerce.miscroservice.activity.controller;

import com.e_commerce.miscroservice.activity.PO.ChannelOrderRecordVO;
import com.e_commerce.miscroservice.activity.service.ChannelUserService;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 9:24
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("activity/channel")
public class ChannelController {

    @Autowired
    private ChannelUserService channelUserService;



    /**
     * 老系统小粉蝶用户支付成功后回调
     *
     * @param orderId 订单id
     * @param storeId 订单所属门店
     * @param shopMemberId 下单用户
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/28 14:04
     */
    @RequestMapping( "fansPaySuccess" )
    public Response fansPaySuccess(
            @RequestParam("orderId")Long orderId,
            @RequestParam("storeId")Long storeId,
            @RequestParam("shopMemberId")Long shopMemberId
    ) {
        try {
            ChannelOrderRecordVO vo = new ChannelOrderRecordVO();
            vo.setOrderId(orderId);
            vo.setStoreId(storeId);
            vo.setShopMemberId(shopMemberId);
            channelUserService.fansPaySuccess(vo);
            return Response.success();
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler(e);
        }
    }


}
