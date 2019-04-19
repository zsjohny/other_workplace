package com.e_commerce.miscroservice.operate.service.live;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.entity.request.AddAnchorRequest;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;

/**
 * @Author hyf
 * @Date 2019/1/15 15:39
 */
public interface LiveService {
    /**
     * 找官方主播
     * @param liveRequest
     * @return
     */
    Response findOfficialAnchor(FindLiveRequest liveRequest);

    /**
     * 查询门店主播
     * @param obj
     * @return
     */
    Response findStoreAnchor(FindLiveRequest obj);

    /**
     * 查询普通主播
     * @param obj
     * @return
     */
    Response findCommonAnchor(FindLiveRequest obj);

    /**
     * 添加官方主播
     * @param obj
     * @return
     */
    Response addAnchor(AddAnchorRequest obj);

    /**
     * 删除官方主播
     * @param id
     * @return
     */
    Response delOfficialAnchor(Long id);
}
