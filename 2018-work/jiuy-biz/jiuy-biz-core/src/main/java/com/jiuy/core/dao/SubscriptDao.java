package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;

public interface SubscriptDao extends DomainDao<Subscript,Long> {

	public Subscript addSubscript(Subscript subscript);
	
	public List<Subscript> getSubscripts();
	
	public int getSubscriptIdByName(String name);
	
	public int remove(Collection<Long> subscriptIds);
	
	public Subscript getSubscriptById(int id);
	
	public int updateSubscript(Subscript subscript);
	
	public int updateProductSum(Long id, Integer productSum);
	
	public int deleteByIds(Collection<Long> subscriptIds);
	
	public List<Subscript> search(String name,String description,Long productId,PageQuery pageQuery);
	
	public int searchCount(String name,String description,Long productId);

}
