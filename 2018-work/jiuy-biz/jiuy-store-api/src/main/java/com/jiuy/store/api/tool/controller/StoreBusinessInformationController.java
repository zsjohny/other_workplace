package com.jiuy.store.api.tool.controller;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;
import com.store.service.IBusinessInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家信息
 * @author hyf
 * @version V1.0
 * @date 2018/8/15 15:54
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/business")
@Login
public class StoreBusinessInformationController {
    Logger logger = LoggerFactory.getLogger(StoreBusinessInformationController.class);
    @Autowired
    private IBusinessInformationService businessInformationService;

    /**
     *  添加商家信息
     * @param businessInformation businessInformation
     * @param userDetail userDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 16:34
     */
    @RequestMapping("/information/add/auth")
    public JsonResponse addInformation(BusinessInformation businessInformation, UserDetail userDetail){

        Long id = userDetail.getId();
        if (id==0){
            logger.warn("用户id为空");
            return JsonResponse.getInstance().setResultCode(-1,"用户未登录");
        }
        businessInformation.setUserId(id);
        return businessInformationService.addInformation(businessInformation);
    }

    /**
     *  根据用户id查询 商家信息
     * @param userDetail userDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 17:49
     */
    @RequestMapping({"/information/find/auth"})
    public JsonResponse findInformationByUserId(UserDetail userDetail){

        Long id = userDetail.getId();
        if (id==0){
            logger.warn("用户id为空");
            return JsonResponse.getInstance().setResultCode(-1,"用户未登录");
        }

        return JsonResponse.getInstance().setSuccessful ().setData(businessInformationService.findInformationByUserId(id));
    }

    /**
     *  根据商品信息id 修改商品信息
     * @param businessInformation businessInformation
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 17:53
     */
    @RequestMapping("/information/update/auth")
    public JsonResponse updateInformationById(BusinessInformation businessInformation, UserDetail userDetail){
        Long id = userDetail.getId();
        if (id==0){
            logger.warn("用户id为空");
            return JsonResponse.getInstance().setResultCode(-1,"用户未登录");
        }
        return businessInformationService.updateInformationById(businessInformation,id);
    }


}
