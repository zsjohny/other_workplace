package com.jiuy.supplier.modular.myProduct.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.service.common.IProductSkuNewService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title sku web服务
 * @package jiuy-biz
 * @description
 * @date 2018/6/8 9:00
 * @copyright 玖远网络
 */
@Controller
@RequestMapping( "/sku" )
public class SkuController extends BaseController{

    @Autowired
    private IProductSkuNewService productSkuNewService;



    /**
     * 添加,编辑 sku定时修改库存
     *
     * @param productSkuList sku数组字符串
     * @return com.jiuyuan.web.help.JsonResponse
     * @auther Charlie(唐静)
     * @date 2018/6/11 16:35
     */
    @RequestMapping( "/timingSetRemainCount/addOrUpdBatch" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse addOrUpdBatch(String productSkuList) {
        ArrayList<ProductSkuNew> query = BizUtil.jsonStrToListObject(productSkuList, ArrayList.class, ProductSkuNew.class);
        try {
            long supplierId = ShiroKit.getUser().getId();
            List<Map<String, Object>> result = productSkuNewService.timingSetRemainCountUpd(query, supplierId);
            return JsonResponse.getInstance().setSuccessful().setData(result);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 添加,编辑 sku定时修改库存
     *
     * @param sku
     * @return com.jiuyuan.web.help.JsonResponse
     * @auther Charlie(唐静)
     * @date 2018/6/11 16:35
     */
    @RequestMapping( "/timingSetRemainCount/addOrUpd" )
    @ResponseBody
    @AdminOperationLog
    public JsonResponse timingSetRemainCount(ProductSkuNew sku) {
        try {
            long supplierId = ShiroKit.getUser().getId();
            List<ProductSkuNew> query = new ArrayList<>(1);
            query.add(sku);
            List<Map<String, Object>> maps = productSkuNewService.timingSetRemainCountUpd(query, supplierId);
            if (maps != null && maps.size() == 1) {
                return JsonResponse.getInstance().setSuccessful().setData(maps.get(0));
            }
            return JsonResponse.getInstance().setSuccessful().setData(maps);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

}
