package com.jiuy.store.api.tool.controller;


import com.jiuyuan.entity.newentity.PropertyValueNew;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.anno.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.store.service.ShopPropertyValueService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;


/**
* 后台工具属性Controller
* @author Qiuyuefan
*/
@Controller
@RequestMapping("/property")
public class ShopPropertyValueController {
	 private static final Log logger = LogFactory.get();
    
    @Autowired
    private ShopPropertyValueService shopPropertyValueService;

    /**
     * 添加或者修改属性值
     * @param propertyNameId
     * @param propertyValueId
     * @param propertyValue
     * @return
     */
    @RequestMapping("/propertyvalue/addupdate/auth")
    @ResponseBody
    public JsonResponse addUpdatePropertyValue(@RequestParam("propertyNameId")Long propertyNameId,@RequestParam("propertyValueId")Long propertyValueId,
    		@RequestParam("propertyValue")String propertyValue,@ClientIp String ip, ClientPlatform client,UserDetail userDetail) {
    	return shopPropertyValueService.addUpdatePropertyValue(propertyNameId,propertyValueId,propertyValue,ip,client,userDetail.getId());
    }  
    
    /**
     * 获取属性值列表
     * @param propertyNameId
     * @return
     */
    @RequestMapping("/propertylist/auth")
    @ResponseBody
    public JsonResponse getPropertyValueList(@RequestParam("propertyNameId")Long propertyNameId,UserDetail userDetail) {
    	return shopPropertyValueService.getPropertyValueList(propertyNameId,userDetail.getId());
    }
    
    /**
     * 删除属性值
     * @param propertyValueId
     * @return
     */
    @RequestMapping("/propertyvalue/delete/auth")
    @ResponseBody
    public JsonResponse deletePropertyValue(@RequestParam("propertyValueId")Long propertyValueId,@ClientIp String ip, ClientPlatform client,UserDetail userDetail) {
    	return shopPropertyValueService.deletePropertyValue(propertyValueId,ip,client,userDetail.getId());
    }


    /**
     * 添加门店用户自己的商品属性值
     *
     * @param userDetail userDetail
     * @param propertyValueNew
     *   主要包括:
     *  propertyValueGroupId 属性值分组id
     *  PropertyValue 属性值名称
     *  OrderIndex 排序
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie(唐静)
     * @date 2018/7/8 22:53
     */
    @RequestMapping( "value/storeUser/add/auth" )
    @ResponseBody
    @Login
    public JsonResponse addShopPropValue(UserDetail userDetail, PropertyValueNew propertyValueNew) {
        try {
            propertyValueNew.setStoreId(userDetail.getId());
            PropertyValueNew result = shopPropertyValueService.addShopPropValue(propertyValueNew);
            return JsonResponse.getInstance().setSuccessful().setData(result);
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 查询门店用户的属性
     *
     * @param userDetail userDetail
     * @param codes   属性值关键字的逗号拼接, eg: 'color,size'
     * @return java.lang.Object
     * @author Charlie(唐静)
     * @date 2018/7/8 23:42
     */
    @RequestMapping( "value/storeUser/find/auth" )
    @ResponseBody
    @Login
    public JsonResponse listShopPropertyVos(UserDetail userDetail, String codes) {
        try {
            return JsonResponse.getInstance()
                    .setSuccessful()
                    .setData(shopPropertyValueService.listPropValGroupVos(userDetail.getId(), codes));
        } catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }
}
