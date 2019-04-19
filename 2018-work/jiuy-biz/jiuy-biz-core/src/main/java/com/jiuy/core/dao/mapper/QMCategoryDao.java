package com.jiuy.core.dao.mapper;

import com.jiuyuan.entity.qianmi.QMCategory;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMCategoryDao {

	QMCategory search(Long categoryId);

	QMCategory add(QMCategory qmCategory);

}
