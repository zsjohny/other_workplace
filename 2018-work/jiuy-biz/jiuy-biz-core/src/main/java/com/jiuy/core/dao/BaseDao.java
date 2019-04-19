/**
 * @Title: BaseDao.java
 * @Package com.sas.core.dao
 * @Description: Interface for Base Dao
 * @author yunshang_734@163.com
 * @date Dec 2, 2014 8:58:08 PM
 * @version V1.0
 */
package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.BaseMeta;

/**
 * @ClassName: BaseDao
 * @Description: Base Dao
 * @author yunshang_734@163.com
 * @date Dec 2, 2014 8:58:08 PM
 */
public interface BaseDao<TObject extends BaseMeta<TId>, TId extends Object> {

	/***************
	 * 获取表名
	 * @return
	 */
	public String getTableName();
	
	/*************
	 * 添加记录，成功返回对象， 否则返回null
	 * @param obj
	 * @return
	 */
	public TObject add(TObject obj);
	
	/***************
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	public TObject getById(TId id);
	
	/**************
	 * 根据id列表获取批量对象
	 * @param ids
	 * @return
	 */
	public List<TObject> listByIds(TId[] ids);
	
	/*************
	 * 根据id列表批量删除对象，返回删除的行数
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int deleteByIds(TId... ids);

}
