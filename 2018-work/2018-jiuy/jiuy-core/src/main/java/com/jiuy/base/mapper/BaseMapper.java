package com.jiuy.base.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jiuy.base.model.Query;

public interface BaseMapper<T> {
	/**
	 * 通过Id查询
	 **/
	public T selectByPrimaryKey(@Param("id") Object id);

	/**
	 * deleteByPrimaryKey
	 **/
	public int deleteByPrimaryKey(@Param("id") Object id);

	/**
	 * insert
	 **/
	public int insert(T t);
	/**
	 * insertSelective
	 **/
	public int insertSelective(T t);

	/**
	 * updateByPrimaryKeySelective
	 **/
	public int updateByPrimaryKeySelective(T t);

	/**
	 * updateByPrimaryKey
	 **/
	public int updateByPrimaryKey(T t);

	/**
	 * selectOne
	 **/
	public T selectOne(Query query);

	/**
	 * selectList
	 **/
	public List<T> selectList(Query param);

	/**
	 * selectCount
	 **/
	public int selectCount(Query param);

	public int insertBach(List<T> list);
}
