/**
 * 
 */
package com.jiuyuan.entity.search;

import java.io.Serializable;
import java.util.List;

/**
* @author DongZhong
* @version 创建时间: 2016年9月27日 上午10:09:48
*/
public class SearchWeight implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2844349693695200182L;
	
	private List<SearchObject> search_objects;
	private List<KeywordLevel> keyword_level;
	
	public List<KeywordLevel> getKeyword_level() {
		return keyword_level;
	}
	public void setKeyword_level(List<KeywordLevel> keyword_level) {
		this.keyword_level = keyword_level;
	}
	public List<SearchObject> getSearch_objects() {
		return search_objects;
	}
	public void setSearch_objects(List<SearchObject> search_objects) {
		this.search_objects = search_objects;
	}
}
