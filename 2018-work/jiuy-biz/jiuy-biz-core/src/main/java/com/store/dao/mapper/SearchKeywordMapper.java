/**
 * 
 */
package com.store.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.search.AppSearchKeyword;

/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 上午10:21:27
*/
@DBMaster
public interface SearchKeywordMapper {
    public List<AppSearchKeyword> getSearchKeywords(@Param("pageQuery") PageQuery pageQuery);
    public int addSearchKeyword(@Param("searchKeywords") List<AppSearchKeyword> searchKeywords);
	public int getSearchKeywordCount();
}
