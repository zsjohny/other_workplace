package com.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.logistics.LOLocation;
import com.store.dao.mapper.LOLocationMapper;

import java.util.List;

@Service
public class LOLocationService {
	
	@Autowired
	private LOLocationMapper loLocationMapper;

	public List<LOLocation> getByName(String cityName) {
		return loLocationMapper.getByName(cityName);
	}



	/**
	 * 查询匹配的第一个地址信息
	 * see com.store.service.LOLocationServiceTest#getFirstCityByName()
	 *
	 * @param cityName cityName
	 * @return com.jiuyuan.entity.logistics.LOLocation nullable
	 * @author Charlie
	 * @date 2018/7/24 11:22
	 */
	public LOLocation getFirstCityByName(String cityName) {
		List<LOLocation> loLocations = getByName (cityName);
		if (loLocations == null || loLocations.isEmpty ()) {
			return null;
		}
		else if (loLocations.size () == 1) {
			return loLocations.get (0);
		}
		else {
			//查询出来多个,返回第一个
			LOLocation minLct = loLocations.get (0);
			int minIndex= cityName.indexOf (minLct.getProvinceName ());
			for (int i = 1; i < loLocations.size (); i++) {
				LOLocation tempLct = loLocations.get (i);
				int tempIndex = cityName.indexOf (tempLct.getProvinceName ());
				if (tempIndex < minIndex) {
					minIndex = tempIndex;
					minLct = tempLct;
				}
			}
			return minLct;
		}
	}

}
