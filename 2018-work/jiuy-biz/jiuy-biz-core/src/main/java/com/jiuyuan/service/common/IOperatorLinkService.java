package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

public interface IOperatorLinkService {

	void add(long operatorUserId, String title, String replaceImageTitle, String imageUrl, String linkUrl, int sort);

	void update(long operatorUserId, String title, String replaceImageTitle, String imageUrl, String linkUrl, int sort,
			long operatorRelationUrlId);

	void delete(long operatorUserId, long operatorRelationUrlId);

	Map<String,Object> detail(long operatorRelationUrlId);

	List<Map<String, Object>> list(Page<Map<String, Object>> page);

}