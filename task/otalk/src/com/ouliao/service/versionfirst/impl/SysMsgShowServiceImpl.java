/**
 * 
 */
package com.ouliao.service.versionfirst.impl;

import com.ouliao.dao.versionfirst.SysMsgShowDao;
import com.ouliao.domain.versionfirst.SysMsgShow;
import com.ouliao.service.versionfirst.SysMsgShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: SysMsgShowServiceImpl.java, 2016年3月13日 下午6:59:38
 */
@Service
@Transactional
public class SysMsgShowServiceImpl implements SysMsgShowService {
	@Autowired
	private SysMsgShowDao sysMsgShowDao;

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Page<SysMsgShow> querySysMsgShowByUserId(Integer starPage, Integer pageNum, Integer userId) {
		return sysMsgShowDao.querySysMsgShowByUserId(starPage, pageNum, userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void createSysMsgShow(List<SysMsgShow> sysMsgShows) {
		sysMsgShowDao.createSysMsgShow(sysMsgShows);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public void deletAllSysMsgShowByUserIds(Iterable<SysMsgShow> ids) {
		sysMsgShowDao.deletAllSysMsgShowByUserIds(ids);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public Iterable<SysMsgShow> querySysMsgShowAll(Integer userId) {
		return sysMsgShowDao.querySysMsgShowAll(userId);
	}

	/**
	 *
	 * 
	 * @param
	 * @return
	 */

	@Override
	public long queryCountSysMsgShowByUserId(Integer UserId) {
		return sysMsgShowDao.queryCountSysMsgShowByUserId(UserId);
	}

	@Override
	public Long queryCountBySysMsgByUserId(Integer userId) {
		return sysMsgShowDao.queryCountBySysMsgByUserId(userId);
	}

}
