package com.jiuy.core.dao.mapper;

import java.util.List;

import com.jiuy.core.meta.SearchKeyword;
import com.jiuyuan.entity.SearchMatchObject;
import com.jiuyuan.entity.query.PageQuery;

public interface SearchDao {

	List<SearchMatchObject> loadMatchObject();

	int searchCount(String keyword, Integer weightType, Integer minCount, Integer maxCount);

	List<SearchKeyword> search(PageQuery pageQuery, String keyword, Integer weightType, Integer minCount,
			Integer maxCount, Integer sortType);

	int update(Long id, Integer weightType, Integer weight);

	int addKeywords(SearchKeyword searchKeyword);

	List<SearchKeyword> search(Integer type);

	int batchAddKeywords(List<SearchKeyword> searchKeywords);

}
