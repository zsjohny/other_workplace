package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 11:57
 * @Copyright 玖远网络
 */
@Mapper
public interface LiveProductMapper {

    int insertSafe(LiveProduct liveProduct);

    /**
     * 查询直播商品
     *
     * @param vo vo
     * @return java.util.List<com.e_commerce.miscroservice.product.entity.LiveProduct>
     * @author Charlie
     * @date 2019/1/15 18:34
     */
    List<LiveProductVO> findLiveProductIdsByRoomId(LiveProductVO vo);


    /**
     * 查询主播商品个数
     *
     * @param anchorIdList anchorIdList
     * @return java.util.List
     * @author Charlie
     * @date 2019/1/17 18:29
     */
    List<Map<String,Long>> countAnchorProduct(@Param( "anchorIdList" ) List<Long> anchorIdList);

    /**
     * 根据直播产品id 创建时间 结束时间 查询进入数量
     * @param list
     * @param create
     * @param current
     * @return
     */
    Integer findOrderCountByProductIds(@Param("list") List<Long> list, @Param("create") Long create, @Param("current") Long current);
}
