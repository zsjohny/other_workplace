package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;

/**
 * Create by hyf on 2018/8/20
 */
public interface OperatorBusinessIMService {
    /**
     * 店铺信息查询
     * @param name
     * @param phone
     * @param time

     * @return
     */
    Page<BusinessInformation> findInformationAll(String name, String phone,String startTime,String endTime);
    /**
     *  根据用户id查询 商家信息
     * @param id userDetail
     * @return com.jiuyuan.web.help.JsonResponse
     * @author hyf
     * @date 2018/8/15 17:49
     */
    JsonResponse findInformationByUserId(Long id);

    /**
     *  根据商品信息id 修改商品信息
     * @return
     * @author hyf
     * @date 2018/8/20 17:49
     */
    JsonResponse updateInformationById(BusinessInformation businessInformation);
}
