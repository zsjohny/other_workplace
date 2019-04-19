package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.meta.logistics.LOLPostageVO;

public interface LOPostageDao {

	List<LOLPostageVO> srchLogistics(int deliveryLocation);

	int savePostage(int id, double postage);

}
