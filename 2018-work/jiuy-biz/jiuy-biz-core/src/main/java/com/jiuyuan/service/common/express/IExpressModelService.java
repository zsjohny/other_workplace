package com.jiuyuan.service.common.express;

import com.jiuyuan.entity.express.ExpressModel;
import com.jiuyuan.entity.express.ExpressModelDetail;
import com.jiuyuan.entity.express.ExpressUtilVo;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.StoreOrderNewVo;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.express
 * @Description:
 * @author: Aison
 * @date: 2018/5/2 11:58
 * @Copyright: 玖远网络
 */
public interface IExpressModelService {

    /**
     * 批量添加明细
     * @param supplierId
     * @param expressModelDetails
     * @date:   2018/5/2 10:24
     * @author: Aison
     */
    List<Long> addExpressModelDetails(Long supplierId, List<ExpressModelDetail> expressModelDetails);

    /**
     * 添加运费模板
     * @param expressModel
     * @date:   2018/4/28 11:19
     * @author: Aison
     */
    void addExpressModel(ExpressModel expressModel);


    /**
     * 添加模板详情
     * @param expressModelDetail
     * @param supplierId 供应上id
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    Long addExpressModelDetail(Long supplierId, ExpressModelDetail expressModelDetail);


    /**
     * 删除运费详情
     * @param supplierId
     * @param detailId
     * @date:   2018/4/28 11:52
     * @author: Aison
     */
    void deleteExpressModelDetail(Long supplierId,Long detailId);

    /**
     * 删除运费详情
     *
     * @param supplierId
     * @param detailIds ids
     * @date: 2018/4/28 11:52
     * @author: Aison
     */
    void deleteExpressModelDetails(Long supplierId, String detailIds);

    /**
     * @param supplierId 供应商id
     * @param deleteIds 需要删除的详情
     * @param updates  需要更新的详情
     * @param adds 需要添加的详情
     * @date:   2018/5/8 15:31
     * @author: Aison
     */
    void updateDetailBatch(Long supplierId,String deleteIds,String updates,String adds);


    /**
     *  更新模板
     * @param expressModel
     * @date:   2018/4/28 13:59
     * @author: Aison
     */
    void updateModel(ExpressModel expressModel,Long supplierId);

    /**
     *  更新模板
     * @param expressModelDetail
     * @param supplierId
     * @date:   2018/4/28 13:59
     * @author: Aison
     */
    void updateModelDetail(ExpressModelDetail expressModelDetail,Long supplierId);


    /**
     * 获取最小的运费
     * @param supplierId
     * @date:   2018/4/28 13:38
     * @author: Aison
     */
    BigDecimal mininExpress(Long supplierId);

    /**
     * 计算快递费用  供应商的商品都是有sku的  不考虑没有sku的情况
     *
     * @param provinceCode 目标省份行政区划
     * @param skuinfos    sku的ids  3_1,2_1,3_1
     * @param supplierId  供应商id
     * @date: 2018/5/2 14:00
     * @author: Aison
     */
    BigDecimal countOrderExpressMoney(String provinceCode, String skuinfos, Long supplierId);

       /**
     * 通过省份名称获取运费
     * @param provinceName
     * @param skuinfos
     * @param supplierId
     * @date:   2018/5/3 17:51
     * @author: Aison
     */
    BigDecimal countOrderExpressMoneyPovinceName(String provinceName, String skuinfos, Long supplierId);

    /**
     * 通过省份名称获取运费
     * @param expressUtilVo 参数的封装
     * @date:   2018/5/3 17:51
     * @author: Aison
     */
    Map<String,BigDecimal>  countOrderExpressMoneyPovinceNames(ExpressUtilVo expressUtilVo);



    /**
     * 
     * 获取某个供应商的运费模板
     * @param supplireId  供应商id
     * @date: 2018/5/2 14:00
     * @author: Aison
     */
    ExpressModel supplierExpress(Long supplireId);
    
    /**
     * 
     * 获取某个供应商的运费模板明细
     * @param supplireId  供应商id
     * @date: 2018/5/2 14:00
     * @author: Aison
     */
    List<ExpressModelDetail> supplierExpressDetail(Long supplireId);


    /**
     * 计算某个订单的运费
     * @param storeOrderNewVo
     * @param storeOrderNew
     * @param addressId
     * @date:   2018/5/8 9:54
     * @author: Aison
     */
    void countOrderExpress(StoreOrderNewVo storeOrderNewVo, StoreOrderNew storeOrderNew, Long addressId);

    /**
     * 获取某个商品的运费
     * @param productId
     * @date:   2018/5/9 14:08
     * @author: Aison
     */
    BigDecimal countProductExpress(Long productId);


    void countOrderExpress4InstantOfSendGoods(StoreOrderNewVo storeOrderNewVo, StoreOrderNew storeOrderNew, String province);
}
