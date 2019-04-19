package com.jiuy.operator.controller.newController.common;

import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.service.common.IGlobalSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 全局配置查询接口
 * @package jiuy-biz
 * @description
 * @date 2018/6/21 11:51
 * @copyright 玖远网络
 */
@Controller
@ResponseBody
@RequestMapping("/globalSetting")
public class GlobalSettingController{

    @Resource(name = "globalSettingService")
    private IGlobalSettingService globalSettingService;


    /**
     * 根据propertyValue查询
     *
     * @param propertyName
     * @return com.jiuy.rb.model.common.GlobalSettingRb
     * @auther Charlie(唐静)
     * @date 2018/6/19 18:03
     */
    @RequestMapping( "findByPropName" )
    public ResponseResult findByPropName(String propertyName) {
        return ResponseResult.instance().success(globalSettingService.findByPropName(propertyName));
    }

}
