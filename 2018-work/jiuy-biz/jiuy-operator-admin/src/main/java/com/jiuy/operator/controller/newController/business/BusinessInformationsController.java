package com.jiuy.operator.controller.newController.business;

import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.service.common.OperatorBusinessIMService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新运营-商家信息-店铺信息
 *
 * @author hyf
 * @version V1.0
 * @date 2018/8/20 11:52
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/operator/business")
public class BusinessInformationsController  extends BaseController {
    private Logger logger = LoggerFactory.getLogger(BusinessInformationsController.class);
    @Autowired
    private OperatorBusinessIMService businessIMService;

    /**
     * 店铺信息查询
     * @param name
     * @param phone
     * @return
     */
    @RequestMapping("information/page")
    public JsonResponse informationFind(String name,String phone,String startTime,String endTime){
        Page<BusinessInformation>  page = businessIMService.findInformationAll(name,phone,startTime,endTime);
        return JsonResponse.successful(super.packForBT(page));
    }


    /**
     *  根据用户id查询 商家信息
     * @param id userDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 17:49
     */
    @RequestMapping("/information/find")
    public JsonResponse findInformationByUserId(   @Length(min = 1, max = 20) Long id ){
        return businessIMService.findInformationByUserId(id);
    }

    /**
     *  根据商品信息id 修改商品信息
     * @param businessInformation businessInformation
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 17:53
     */
    @RequestMapping("/information/update")
    public JsonResponse updateInformationById(BusinessInformation businessInformation){
        return businessIMService.updateInformationById(businessInformation);
    }

}
