package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.product.entity.LiveProduct;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 11:57
 * @Copyright 玖远网络
 */
public interface LiveProductDao {

    /**
     * 平台直播商品
     *
     * @param anchorId 主播id
     * @param productIds 平台商品
     * @return java.util.List<java.lang.Long>
     * @author Charlie
     * @date 2019/1/14 19:06
     */
    List<Long> listPlatformBySupplierProductIds(Long anchorId, List<Long> productIds);


    /**
     * 安全的批量插入
     * <p>一个主播只会拥有唯一一个直播商品</p>
     *
     * @param liveProducts liveProducts
     * @return int
     * @author Charlie
     * @date 2019/1/14 19:24
     */
    int batchInsertSafe(List<LiveProduct> liveProducts);



    /**
     * 查找自己直播商品
     *
     * @param anchorId anchorId
     * @param productIds productIds
     * @return java.util.List<java.lang.Long>
     * @author Charlie
     * @date 2019/1/15 13:59
     */
    List<Long> listSelfLiveByShopProductIds(Long anchorId, List<Long> productIds);


    /**
     * 查找直播商品
     *
     * @param id id
     * @param anchorId 主播id,可以是空
     * @return com.e_commerce.miscroservice.product.entity.LiveProduct
     * @author Charlie
     * @date 2019/1/15 14:03
     */
    LiveProduct findById(Long id, Long anchorId);


    LiveProduct findById(Long id);

    /**
     * 根据id更新
     *
     * @param updVO updVO
     * @return int
     * @author Charlie
     * @date 2019/1/15 14:04
     */
    int updateById(LiveProduct updVO);


    /**
     * 查询主播商品个数
     *
     * @param anchorIdList anchorIdList
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/17 18:29
     */
    List<Map<String,Long>> countAnchorProduct(List<Long> anchorIdList);


    /**
     * 查询主播商品个数
     *
     * @param anchorId 播主
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/17 18:29
     */
    Long countAnchorProduct(Long anchorId);


    /**
     * 查询直播商品
     *
     * @param ids ids
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.LiveProduct>
     * @author Charlie
     * @date 2019/1/21 9:59
     */
    List<LiveProduct> listByIds(List<Long> ids);

    /**
     * 根据房间号查询直播商品
     * @param roomId
     * @return
     */
    List<LiveProduct> findByRoomId(Long roomId);

    /**
     * 根据直播产品id 创建时间 结束时间 查询进入数量
     * @param list
     * @param create
     * @param current
     * @return
     */
    Integer findOrderCountByProductIds(List<Long> list, Long create, Long current);
}


