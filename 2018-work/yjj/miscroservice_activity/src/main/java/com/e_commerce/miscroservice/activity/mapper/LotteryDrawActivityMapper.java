package com.e_commerce.miscroservice.activity.mapper;


import com.e_commerce.miscroservice.activity.PO.LotteryDrawActivityPo;
import com.e_commerce.miscroservice.activity.entity.PicturesCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Create by hyf on 2018/10/8
 */
@Mapper
public interface LotteryDrawActivityMapper {

    List<LotteryDrawActivityPo> findAllProductList(@Param("code") Integer code);


    List<PicturesCollection> findPictureCollectionByIdType(@Param("id") Long id, @Param("code") Integer code);
}
