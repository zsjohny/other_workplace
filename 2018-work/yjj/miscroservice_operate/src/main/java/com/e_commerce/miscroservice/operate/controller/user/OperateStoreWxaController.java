package com.e_commerce.miscroservice.operate.controller.user;

import com.e_commerce.miscroservice.commons.entity.user.StoreWxaDataQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import com.e_commerce.miscroservice.operate.rpc.user.StoreBusinessRpcService;
import com.e_commerce.miscroservice.operate.service.user.StoreWxaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/17 15:38
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "operator/store/wxa" )
public class OperateStoreWxaController{


    @Autowired
    private StoreWxaService storeWxaService;
    @Autowired
    private StoreBusinessRpcService storeBusinessRpcService;

    /**
     * 店铺小程序资料
     * @param pageSize pageSize
     * @param pageNumber pageNumber
     * @param businessName 店长姓名
     * @param phone 手机号
     * @param shopName 店铺名称
     * @param wxaOpenTimeCeil 开通时间
     * @param wxaOpenTimeFloor 开通时间
     * @param businessType 店铺形式 1共享,2专享
     * @return data:[{
     *       "memberEndTime": "会员过期", //会员过期时间
     *       "phone": "1826810702323", //手机号
     *       "wxaOpenTime": "2018-06-17 11:20:36", //店铺开通时间
     *       "shopName": "Aisons", //店铺名称
     *       "industry": "Aisons", //行业
     *       "mainBusiness": "Aisons", //主营业务
     *       "isOpenWxa": 1, //1开通,0关闭
     *       "memberDelState": 0, //0正常,1删除,2冻结
     *       "isMember": 1, //1是会员,2不是会员
     *       "storeId": 52, //
     *       "businessType": 2, //1共享版,2专项版
     *       "wxaCloseTime": "店铺过期" //店铺过期时间
     *     }]
     * @author Charlie
     * @date 2018/12/17 15:41
     */
    @RequestMapping( "listAll" )
    public Response listAll(
            @RequestParam( value = "pageSize", defaultValue = "1" ) Integer pageSize,
            @RequestParam( value = "pageNumber", defaultValue = "14" ) Integer pageNumber,
            @RequestParam( value = "businessName", required = false ) String businessName,
            @RequestParam( value = "phone", required = false ) String phone,
            @RequestParam( value = "shopName", required = false ) String shopName,
            @RequestParam( value = "wxaOpenTimeCeil", required = false ) String wxaOpenTimeCeil,
            @RequestParam( value = "wxaOpenTimeFloor", required = false ) String wxaOpenTimeFloor,
            @RequestParam( value = "businessType", required = false ) Integer businessType
    ) {
        StoreWxaDataQuery query = new StoreWxaDataQuery ();
        query.setPageSize (pageSize);
        query.setPageNumber (pageNumber);
        query.setBusinessName (businessName);
        query.setPhone (phone);
        query.setShopName (shopName);
        query.setWxaOpenTimeCeilStr (wxaOpenTimeCeil);
        query.setWxaOpenTimeFloorStr (wxaOpenTimeFloor);
        query.setBusinessType (businessType);
        return Response.success (storeWxaService.listAll (query));
    }




    /**
     * 打开/关闭店铺
     *
     * @param storeId storeId
     * @param isOpenWxa 是否开通小程序 1已开通(正常),2冻结(手工关闭)
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/17 20:13
     */
    @RequestMapping( "openWxaStatus" )
    public Response openWxaStatus(@RequestParam("storeId") Long storeId,
                                  @RequestParam("isOpenWxa") Integer isOpenWxa) {
        return Response.success (storeBusinessRpcService.openWxaStatus(storeId, isOpenWxa));
    }






    /**
     * 初始化小程序店铺开通关闭时间
     *
     * @param storeId storeId
     * @param wxaOpenTime 开通时间
     * @param wxaCloseTime 关闭时间
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/12/18 14:20
     */
    @RequestMapping( "initWxaOpenTime" )
    public Response initWxaOpenTime(
            @RequestParam( "storeId" ) Long storeId,
            @RequestParam( "wxaOpenTime" ) String wxaOpenTime,
            @RequestParam( "wxaCloseTime" ) String wxaCloseTime) {

        return Response.success (storeBusinessRpcService.initWxaOpenTime(storeId, TimeUtils.str2Long (wxaOpenTime), TimeUtils.str2Long (wxaCloseTime)));
    }


}


