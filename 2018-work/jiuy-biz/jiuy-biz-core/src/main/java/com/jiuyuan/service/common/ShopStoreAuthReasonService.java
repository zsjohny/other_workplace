package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.ShopStoreAuthReasonMapper;
import com.jiuyuan.entity.newentity.ShopStoreAuthReason;

@Service
public class ShopStoreAuthReasonService {
	
	private static final int NO_DELETE = 0;
	@Autowired
	private ShopStoreAuthReasonMapper shopStoreAuthReasonMapper;

	public Map<String, Object> getRefuseReasonList(int refundReasonType) {
		Wrapper<ShopStoreAuthReason> wrapper = new EntityWrapper<ShopStoreAuthReason>().eq("type", refundReasonType)
				                                                                       .eq("is_delete", NO_DELETE)
				                                                                       .orderBy("weight", false);
		List<ShopStoreAuthReason> list = shopStoreAuthReasonMapper.selectList(wrapper);
		List<Map<String,Object>> infoList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		for(ShopStoreAuthReason s:list){
			Map<String,Object> infomap = new HashMap<String,Object>();
			infomap.put("id", s.getId());//拒绝原因ID
			infomap.put("noPassReason", s.getNoPassReason());//拒绝原因
			infoList.add(infomap);
		}
		map.put("RefuseReasonList", infoList);//拒绝原因
		return map;
	}

}
