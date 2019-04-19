package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.newentity.OperatorSeo;

public interface IOperatorSeoService {

	String getDefaultSeoKeywords();

	List<Map<String, Object>> detail();

	void update(long operatorUserId, String seoHomeTitle, String seoHomeDescriptor, String seoHomeKeywords,
			String seoDefaultTitle, String seoDefaultDescriptor, String seoDefaultKeywords);

	OperatorSeo getDefaultSeo();

}