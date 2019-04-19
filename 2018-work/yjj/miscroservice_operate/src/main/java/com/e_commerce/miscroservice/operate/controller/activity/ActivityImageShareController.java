package com.e_commerce.miscroservice.operate.controller.activity;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.ActivityImageShareAddRequest;
import com.e_commerce.miscroservice.operate.service.activity.ActivityImageShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hyf
 * @Date 2019/1/8 16:28
 */
@RestController
@RequestMapping("/share/images")
public class ActivityImageShareController {

    @Autowired
    private ActivityImageShareService activityImageShareService;

    /**
     * 根据筛选添加查询所有
     * @param type 类型：默认：0-小程序
     * @param shareType 分享类型：默认：1001-分享至小程序首页
     * @param timeStar 创建时间-开始
     * @param timeEnd 创建时间-结束
     * @param pageNumber 页码
     * @param pageSize 每页数量
     * @return
     */
    @RequestMapping("/condition/filtrate")
    public Response shareImage(Integer type, Integer shareType, Long timeStar, Long timeEnd, @RequestParam(name = "pageNumber",defaultValue = "1")Integer pageNumber,@RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        return activityImageShareService.findAllActivityImageShare(type,shareType,timeStar,timeEnd,pageNumber,pageSize);
    }

    /**
     * 添加或者修改
     * @param id
     * @param type
     * @param shareType
     * @param mainMap
     * @param wxImg
     * @return
     */
    @Consume(ActivityImageShareAddRequest.class)
    @RequestMapping("change")
    public Response shareImageChange(Long id,Integer type, Integer shareType, String mainMap, String wxImg){
        return activityImageShareService.shareImageChange((ActivityImageShareAddRequest) ConsumeHelper.getObj());
    }

}
