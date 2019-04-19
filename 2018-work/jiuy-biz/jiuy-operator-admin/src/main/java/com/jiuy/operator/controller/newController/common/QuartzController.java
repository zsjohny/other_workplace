package com.jiuy.operator.controller.newController.common;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.service.product.ISalesVolumeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 定时任务回调controller
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/19 15:08
 * @Copyright 玖远网络
 */
@RequestMapping("/quartz/")
@Controller
@ResponseBody
public class QuartzController {

    @Resource(name = "salesVolumeService")
    private ISalesVolumeService salesVolumeService;

    /**
     * 回调修改销量
     *
     * @param id id
     * @param comment comment
     * @author Aison
     * @date 2018/6/15 16:50
     */
    @RequestMapping("feedback")
    public ResponseResult feedback(Long id, String comment) {

        salesVolumeService.feedbackPlain(id,comment,UserSession.getUserSession());
        return ResponseResult.SUCCESS;
    }
}
