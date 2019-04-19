package com.jiuy.core.dao.support;


import java.util.List;

import com.jiuyuan.entity.BaseMeta;

/**
 * @ClassName: DomainDao
 * @Description: 单主键dao 接口
 * @author Zhan Jinwu
 */
public interface DomainDao<TObject extends BaseMeta<TId>, TId extends Object> {
	
	/*************
	 * 添加记录，成功返回对象， 否则返回null
	 * @param obj
	 * @return
	 */
	public TObject add(TObject obj);

    /**
     * 更新对象
     * */
    public boolean update(TObject newObject);

    /**
     * 修改计数
     * */
    public boolean changeCount(TId id, String field, int deltaCount);
	
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
