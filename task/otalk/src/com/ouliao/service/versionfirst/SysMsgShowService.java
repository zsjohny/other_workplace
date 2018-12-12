/**
 * 
 */
package com.ouliao.service.versionfirst;

import com.ouliao.domain.versionfirst.SysMsgShow;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SysMsgShowService.java, 2016年3月13日 下午6:59:27
 */

public interface SysMsgShowService {
	public void createSysMsgShow(List<SysMsgShow> sysMsgShows);

	public Page<SysMsgShow> querySysMsgShowByUserId(Integer starPage, Integer pageNum, Integer UserId);

	void deletAllSysMsgShowByUserIds(Iterable<SysMsgShow> ids);

	Iterable<SysMsgShow> querySysMsgShowAll(Integer userId);

	public long queryCountSysMsgShowByUserId(Integer UserId);

	Long queryCountBySysMsgByUserId(Integer userId);
}
