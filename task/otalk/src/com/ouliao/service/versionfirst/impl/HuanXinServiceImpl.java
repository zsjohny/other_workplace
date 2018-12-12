/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.HuanXinDao;
import com.ouliao.domain.versionfirst.HuanXin;
import com.ouliao.service.versionfirst.HuanXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: HuanXinServiceImpl.java, 2016年3月16日 下午9:45:27
 */
@Transactional
@Service
public class HuanXinServiceImpl implements HuanXinService {
	@Autowired
	private HuanXinDao huanXinDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void saveHuanXin(HuanXin huanXin) {
		huanXinDao.saveHuanXin(huanXin);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public HuanXin queryIsExist(Integer ownerId) {
		return huanXinDao.queryIsExist(ownerId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public HuanXin queryHuanXinByName(String huaXinName) {
		return huanXinDao.queryHuanXinByName(huaXinName);
	}

}
