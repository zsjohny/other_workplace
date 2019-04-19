package com.jiuy.supplier.modular.web.controller;

import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IProductSkuNewService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.web.help.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title quartz定时任务的回调接口
 * @package jiuy-biz
 * @description
 * @date 2018/5/30 18:21
 * @copyright 玖远网络
 */
@Controller
@RequestMapping("quartzJob")
public class JobController{

    @Autowired
    private IProductNewService productNewService;
    @Autowired
    private IProductSkuNewService productSkuNewService;


    /**
     * job回调,商品定时上架
     *
     * @param supplierId
     * @param productId
     * @param token      服务调用者认证信息
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/5/30 18:28
     */
    @RequestMapping( "timingProductPutaway" )
    @ResponseBody
    public JsonResponse timingProductPutaway(@RequestParam( required = true ) Long productId,
                                             @RequestParam( required = true ) Long supplierId,
                                             @RequestParam( required = false ) String token
    ) {
        try {
            productNewService.productPutawayFromJob(supplierId, productId, token);
            return JsonResponse.getInstance().setSuccessful();
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 更新sku库存
     *
     * @param supplierId
     * @param skuId
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/5/30 18:28
     */
    @RequestMapping( "updSkuRemainCount" )
    @ResponseBody
    public JsonResponse updSkuRemainCount(@RequestParam() Long skuId, @RequestParam() Long supplierId, @RequestParam Integer count,
                                          @RequestParam( required = false ) String token) {
        try {
            productSkuNewService.updateRemainCount(skuId, supplierId, count, token);
            return JsonResponse.getInstance().setSuccessful();
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


}

