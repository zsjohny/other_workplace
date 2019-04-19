package com.store.service;

import com.baomidou.mybatisplus.service.IService;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.BusinessInformation;

/**
 * <p>
 * 商家信息表 服务类
 * </p>
 *
 * @author Hyf
 * @since 2018-08-15
 */
public interface IBusinessInformationService extends IService<BusinessInformation> {

    /**
     * 添加商家信息
     * @param businessInformation
     * @return
     */
    JsonResponse addInformation(BusinessInformation businessInformation);

    /**
     *  根据商家id查询商家信息
     * @param id id
     * @return BusinessInformation
     * @author hyf
     * @date 2018/8/15 17:39
     */
    BusinessInformation findInformationByUserId(Long id);
    /**
     *  根据商品信息id 修改商品信息
     * @param businessInformation null
     * @return
     * @author hyf
     * @date 2018/8/15 17:54
     */
    JsonResponse updateInformationById(BusinessInformation businessInformation, Long id);

    /**
     * 查询店铺信息
     * @return
     */
    JsonResponse findInformationAll();
    /**
     * 查询商家信息
     */
    StoreBusiness selectStoreBusiness(Long storeId);
}
