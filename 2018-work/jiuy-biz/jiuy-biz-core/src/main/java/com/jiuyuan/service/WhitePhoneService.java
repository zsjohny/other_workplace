/**
 * 
 */
package com.jiuyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.supplier.WhitePhoneMapper;

//import com.yujj.dao.mapper.WhitePhoneMapper;

/**
* @author DongZhong
* @version 创建时间: 2016年12月28日 下午2:57:27
*/

@Service
public class WhitePhoneService {

	@Autowired
	WhitePhoneMapper whitePhoneMapper;
//	
	public int getWhitePhone(String phone) {
		return whitePhoneMapper.getWhitePhone(phone);
	}
	
	
}
