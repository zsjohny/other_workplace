/**
 * 
 */
package com.ouliao.dao.versionfirst.impl;

import com.ouliao.dao.versionfirst.HuanXinDao;
import com.ouliao.domain.versionfirst.HuanXin;
import com.ouliao.repository.versionfirst.HuanXinCrudRepository;
import com.ouliao.repository.versionfirst.HuanXinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: HuanXinDaoImpl.java, 2016年3月16日 下午9:44:14
 */
@Repository
public class HuanXinDaoImpl implements HuanXinDao {
	@Autowired
	private HuanXinCrudRepository huanXinCrudRepository;

	@Autowired
	private HuanXinRepository huanXinRepository;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void saveHuanXin(HuanXin huanXin) {
		huanXinCrudRepository.save(huanXin);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public HuanXin queryIsExist(Integer ownerId) {
		return huanXinRepository.queryIsExist(ownerId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public HuanXin queryHuanXinByName(String huaXinName) {
		return huanXinRepository.queryHuanXinByName(huaXinName);
	}

}
