package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.ShopMessage;
import com.thoughtworks.xstream.mapper.Mapper;


//import com.jiuyuan.dao.annotation.DBMaster;

/**
* @author DongZhong
* @version 创建时间: 2016年12月28日 下午2:58:27
*/
@DBMaster
public interface PhoneMapper extends BaseMapper<ShopMessage> { 
	int getWhitePhone(@Param("phone") String phone);
}
