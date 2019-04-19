package com.e_commerce.miscroservice.operate.service.activity;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.ActivityImageShareAddRequest;

/**
 * 分享活动图片管理
 * @Author hyf
 * @Date 2019/1/8 16:31
 */
public interface ActivityImageShareService {
    /**
     * 根据筛选条件 查找
     * @param type
     * @param shareType
     * @param timeStar
     * @param timeEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Response findAllActivityImageShare(Integer type, Integer shareType, Long timeStar, Long timeEnd, Integer pageNumber, Integer pageSize);

    /**
     * 添加或修改
     * @return
     */
    Response shareImageChange(ActivityImageShareAddRequest activityImageShareAddRequest);
}
