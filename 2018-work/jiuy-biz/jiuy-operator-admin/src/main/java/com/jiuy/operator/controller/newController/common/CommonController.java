package com.jiuy.operator.controller.newController.common;

import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.common.ShopStoreAuthReasonRb;
import com.jiuy.rb.service.common.ICommonService;
import com.jiuyuan.entity.newentity.ShopStoreAuthReason;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 一些通用的接口
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 14:23
 * @Copyright 玖远网络
 */
@Component
@ResponseBody
@RequestMapping("/admin/")
public class CommonController {


    @Resource(name = "commonService")
    private ICommonService commonService;

    /**
     * 获取拒绝原因
     *
     * @param shopStoreAuthReasonRb 拒绝原因
     * @author Aison
     * @date 2018/6/13 14:25
     */
    @RequestMapping("getPreinstallNoPassReasonList")
    public ResponseResult getPreinstallNoPassReasonList(ShopStoreAuthReasonRb shopStoreAuthReasonRb) {

        return ResponseResult.instance().success(commonService.authReasonList(shopStoreAuthReasonRb));
    }
}
