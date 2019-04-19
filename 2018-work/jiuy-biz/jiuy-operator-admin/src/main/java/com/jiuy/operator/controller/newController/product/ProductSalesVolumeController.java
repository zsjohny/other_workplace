package com.jiuy.operator.controller.newController.product;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.model.product.SalesVolumePlainRb;
import com.jiuy.rb.model.product.SalesVolumePlainRbQuery;
import com.jiuy.rb.service.product.ISalesVolumeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品销量相关的service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 10:56
 * @Copyright 玖远网络
 */
@Controller
@RequestMapping("/admin/")
@ResponseBody
public class ProductSalesVolumeController {

    @Resource(name = "salesVolumeService")
    private ISalesVolumeService salesVolumeService;
    /**
     * 添加刷量策略
     *
     * @param salesVolumePlainRb salesVolumePlainRb
     * @author Aison
     * @date 2018/6/15 10:58
     */
    @RequestMapping("addSalesVolumePlan")
    public ResponseResult addSalesVolumePlan(SalesVolumePlainRb salesVolumePlainRb) {

        SalesVolumePlainRb salesVolumePlainRb1 =  salesVolumeService.addSalesVolumePlain(salesVolumePlainRb,UserSession.getUserSession()).getData();
        return ResponseResult.instance().success(salesVolumePlainRb1);
    }

    /**
     * 修改策略 只允许修改状态
     *
     * @param salesVolumePlainRb salesVolumePlainRb
     * @author Aison
     * @date 2018/6/19 10:57
     */
    @RequestMapping("updateSalesVolumePlain")
    public ResponseResult updateSalesVolumePlain(SalesVolumePlainRb salesVolumePlainRb) {

        salesVolumeService.updateSalesVolumePlain(salesVolumePlainRb,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }


    /**
     * 任务分页
     *
     * @param query query
     * @author Aison
     * @date 2018/6/19 11:04
     */
    @RequestMapping("plainPage")
    public ResponseResult plainPage(SalesVolumePlainRbQuery query) {

        return ResponseResult.instance().success(salesVolumeService.plainPage(query));
    }



}
