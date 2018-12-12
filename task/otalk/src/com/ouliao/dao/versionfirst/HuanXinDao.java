/**
 * 
 */
package com.ouliao.dao.versionfirst;

import com.ouliao.domain.versionfirst.HuanXin;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: HuanXinDao.java, 2016年3月16日 下午9:43:57
 */

public interface HuanXinDao {

	void saveHuanXin(HuanXin huanXin);

	HuanXin queryIsExist(Integer ownerId);

	HuanXin queryHuanXinByName(String huaXinName);

}
