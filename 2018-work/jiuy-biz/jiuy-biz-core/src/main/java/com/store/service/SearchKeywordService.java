/**
 * 
 */
package com.store.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.search.AppSearchKeyword;
import com.store.dao.mapper.SearchKeywordMapper;


/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 上午10:20:08
*/
@Service
public class SearchKeywordService {

    @Autowired
    private SearchKeywordMapper searchKeywordMapper;
    
    public List<AppSearchKeyword> getSearchKeywords(PageQuery pageQuery) {
        return searchKeywordMapper.getSearchKeywords(pageQuery);
    }    

	public int getSearchKeywordCount() {
    	return searchKeywordMapper.getSearchKeywordCount();
	}	

    public int addSearchKeyword(String[] searchKeywords, int searchResultHits, int guideFlag) {
    	List<AppSearchKeyword> searchKeywordList = new ArrayList<AppSearchKeyword>();
    	long time = System.currentTimeMillis();
    	for (int i = 0; i < searchKeywords.length; i++) {

        	AppSearchKeyword searchKeyword = new AppSearchKeyword();
        	searchKeyword.setKeyword(searchKeywords[i]);
        	searchKeyword.setCreateTime(time);
        	searchKeyword.setUpdateTime(time);
        	searchKeyword.setSearchResultHits(searchResultHits);
        	searchKeyword.setGuideFlag(guideFlag);
        	
        	searchKeywordList.add(searchKeyword);
		}
    	
    	return searchKeywordMapper.addSearchKeyword(searchKeywordList);
    }    
}
