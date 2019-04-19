package com.store.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.store.entity.ShopMemberFavorite;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author QiuYuefan
 *
 */
public interface WxaMemberFavoriteMapper extends BaseMapper<ShopMemberFavorite>{


    /**
     * 查询收藏数量
     * @param memberId
     * @param storeId
     * @return
     */
    Integer findMyFavoriteCount(@Param("memberId") long memberId, @Param("storeId") long storeId);
}