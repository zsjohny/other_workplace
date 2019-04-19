package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.store.entity.ShopMemberVisit;

/**
 * 
 * @author QiuYuefan
 *
 */
public interface WxaMemberVisitMapper extends BaseMapper<ShopMemberVisit>{

	int increaseShopMemberVisitCount(@Param("id") long id);
	

}