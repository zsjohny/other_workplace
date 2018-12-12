package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.borrow.utils.EyeResponse;

/**
 * 提供给网贷天眼的查询接口service
 */
public interface EyeInfoService {


	/**
	 *
	 * @param start_time
	 * @param end_time
	 * @param mobile
	 * @param order_id
	 * @return
	 */
	public EyeResponse getEyeInfo(String start_time, String end_time, String mobile, String order_id);


}


















