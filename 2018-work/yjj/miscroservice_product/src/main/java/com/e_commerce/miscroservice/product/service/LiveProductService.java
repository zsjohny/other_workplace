package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 9:27
 * @Copyright 玖远网络
 */
public interface LiveProductService {


    /**
     * 直播商品选择列表
     *
     * @param vo vo
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/14 15:52
     */
    Map<String, Object> productSelectList(LiveProductVO vo);


    /**
     * 添加到直播商品
     *
     * @param vo shopProductIds
     * @author Charlie
     * @date 2019/1/14 17:53
     */
    void batchInsertByProductIds(LiveProductVO vo);



    /**
     * 编辑直播商品
     *
     * @param vo vo
     * @author Charlie
     * @date 2019/1/15 13:41
     */
    void update(LiveProductVO vo);


    /**
     * 直播中的商品
     *
     * @param vo userId
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/15 14:28
     */
    Map<String, Object> listLiveProduct(LiveProductVO vo);



    /**
     * 根据房间号查询所有直播商品信息
     *
     * @param vo LiveProductVO#roomNumList 房间号必填
     * @param vo LiveProductVO#liveStatus 直播商品状态,选填 直播状态:0正常,1取消直播
     * @return java.util.List<com.e_commerce.miscroservice.product.vo.LiveProductVO>
     * @author Charlie
     * @date 2019/1/16 15:57
     */
    List<LiveProductVO> doQueryLiveProductListByRoomNumList(LiveProductVO vo, boolean isQuerySku);

    /**
     * 正在直播推荐的商品
     *
     * @param vo vo
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/16 14:49
     */
    Map<String, Object> listOnRecommended(LiveProductVO vo);


    /**
     * 商品简介
     *
     * @param liveProductId liveProductId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/16 16:15
     */
    MapHelper productIntro(Long liveProductId);


    /**
     * 商品详情
     *
     * @param liveProductId liveProductId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/16 16:15
     */
    MapHelper productDetail(Long liveProductId);




}
