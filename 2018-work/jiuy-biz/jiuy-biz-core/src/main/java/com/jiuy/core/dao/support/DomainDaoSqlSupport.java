package com.jiuy.core.dao.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.jiuyuan.entity.BaseMeta;

/***************
 * 所有sql dao实现的基类
 * @author zhuliming
 *
 * @param <TObject>
 * @param <TId>
 */
public class DomainDaoSqlSupport<TObject extends BaseMeta<TId>, TId extends Object>  extends SqlSupport
	implements DomainDao<TObject, TId> {

    /*************
     * 添加记录，成功返回对象， 否则返回null
     * @param obj
     * @return
     */
    public final TObject add(TObject obj){
        if(obj == null){
            return  null;
        }
        if(getSqlSession().insert(this.getMetaClassName() + ".add", obj) > 0){
            return obj;
        }
        return null;
    }

    /***************
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	public TObject getById(TId id){
		return getSqlSession().selectOne(this.getMetaClassName() + ".getById", id);
	}
	
	/**************
	 * 根据id列表获取批量对象
	 * @param ids
	 * @return
	 */
	public final List<TObject> listByIds(TId[] ids){
		if (ArrayUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		List<TObject> tObjectList = this.getSqlSession().selectList(this.getMetaClassName() + ".listByIds", ids);
		return this.assureNotNullList(tObjectList);
	}
	
	/*************
	 * 根据id列表批量删除对象，返回删除的行数
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final int deleteByIds(TId... ids)
	{
		if(ArrayUtils.isEmpty(ids)){
			return 0;
		}
		return getSqlSession().delete(this.getMetaClassName() + ".deleteByIds",ids);
	}

    public boolean update(TObject newObject) {
        return getSqlSession().update(this.getMetaClassName() + ".update", newObject) > 0;
    }

    public boolean changeCount(TId id, String field, int deltaCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("field", field);
        params.put("count", deltaCount);
        return this.getSqlSession().update(this.getMetaClassName() + ".changeCount", params) > 0;
    }
	/***************
	 * 确保返回的list不是null，以免nullpointexception
	 * @param list
	 * @return
	 */
	protected final List<TObject> assureNotNullList(final List<TObject>  list){
		return list == null ?  new ArrayList<TObject>(0) : list;
	}
}