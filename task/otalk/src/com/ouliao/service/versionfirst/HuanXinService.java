/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.HuanXin;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: HuanXinService.java, 2016年3月16日 下午9:45:06   
 */

public interface HuanXinService {
	void saveHuanXin(HuanXin huanXin);

	HuanXin queryIsExist(Integer ownerId);

	HuanXin queryHuanXinByName(String huaXinName);
}
