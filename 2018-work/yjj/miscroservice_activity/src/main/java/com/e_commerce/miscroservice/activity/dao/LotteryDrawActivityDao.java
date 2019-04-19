package com.e_commerce.miscroservice.activity.dao;


import com.e_commerce.miscroservice.activity.PO.LotteryDrawActivityPo;
import com.e_commerce.miscroservice.activity.entity.PicturesCollection;

import java.util.List;

/**
 * Create by hyf on 2018/10/8
 */
public interface LotteryDrawActivityDao {

    /**
     * 查询所有 活动
     * @return
     * @param code
     */
    List<LotteryDrawActivityPo> findAllProductList(Integer code);

    /**
     * 根据活动id 类型查找内容图片
     * @param id
     * @param code
     * @return
     */
    List<PicturesCollection> findPictureCollectionByIdType(Long id, Integer code);
}
