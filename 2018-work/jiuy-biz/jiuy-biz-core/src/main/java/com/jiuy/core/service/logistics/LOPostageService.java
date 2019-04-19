package com.jiuy.core.service.logistics;

import java.util.List;

import com.jiuy.core.meta.logistics.LOLPostageVO;

public interface LOPostageService {

	List<LOLPostageVO> srchLogistics(int deliveryLocation);

	int savePostage(int id, double d);

}
