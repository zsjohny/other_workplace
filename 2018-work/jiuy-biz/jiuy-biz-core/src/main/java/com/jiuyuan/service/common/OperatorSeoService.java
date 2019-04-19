package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.admin.core.template.config.OfficialContextConfig;
import com.admin.core.template.config.OfficialPageConfig;
import com.admin.core.template.engine.OfficialCommonTemplateEngine;
import com.admin.core.template.engine.base.OfficialTemplateEngine;
import com.admin.core.util.BeetlUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.OperatorArticleMapper;
import com.jiuyuan.dao.mapper.supplier.OperatorSeoMapper;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.entity.newentity.OperatorSeo;

@Service
public class OperatorSeoService implements IOperatorSeoService {
	private static final Logger logger = LoggerFactory.getLogger(OperatorSeoService.class);
	
	private static final int LATEST_ARITICLE_SIZE = 8;
	
	@Autowired
	private OperatorArticleMapper operatorArticleMapper;
   
	@Autowired
	private OperatorSeoMapper operatorSeoMapper;
	
	@Autowired
	private IOperatorArticleService operatorArticleService;
	@Override
	public String getDefaultSeoKeywords() {
		Wrapper<OperatorSeo> wrapper = new EntityWrapper<OperatorSeo>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .eq("seo_type", OperatorSeo.SEO_TYPE_DEFAULT);
		List<OperatorSeo> list = operatorSeoMapper.selectList(wrapper);
		return list.get(0).getKeywords();
	}
	
	@Override
	public List<Map<String, Object>> detail() {
		List<Map<String,Object>> result = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(OperatorSeo.SEO_TYPE_HOME).append(",").append(OperatorSeo.SEO_TYPE_DEFAULT);
		Wrapper<OperatorSeo> wrapper = new EntityWrapper<OperatorSeo>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .in("seo_type", stringBuffer.toString());
		List<OperatorSeo> list = operatorSeoMapper.selectList(wrapper);
		for(OperatorSeo operatorSeo :list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("title", operatorSeo.getTitle());//seo标题
			map.put("descriptor", operatorSeo.getDescriptor());//seo描述
			map.put("keywords", operatorSeo.getKeywords());//seo关键词
			map.put("seoType", operatorSeo.getSeoType());//seo类型:1:首页 2:页面默认seo
			map.put("id", operatorSeo.getId());//seoId
			result.add(map);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(long operatorUserId, String seoHomeTitle, String seoHomeDescriptor, String seoHomeKeywords,
			String seoDefaultTitle, String seoDefaultDescriptor, String seoDefaultKeywords) {
		//删除以前seo信息
		logger.info("更改seo,operatorUserId:"+operatorUserId);
		Wrapper<OperatorSeo> wrapper = new EntityWrapper<OperatorSeo>();
		wrapper.eq("status", Status.NORMAL.getIntValue());
		List<OperatorSeo> list = operatorSeoMapper.selectList(wrapper);
		for(OperatorSeo operatorSeo : list){
			OperatorSeo operatorSeo2 = new OperatorSeo();
			operatorSeo2.setId(operatorSeo.getId());
			operatorSeo2.setStatus(Status.DELETE.getIntValue());
			operatorSeo2.setDeleteTime(System.currentTimeMillis());
			operatorSeo2.setUpdateTime(System.currentTimeMillis());
			operatorSeoMapper.updateById(operatorSeo2);
		}
		long currentTime  = System.currentTimeMillis();
		//插入seo信息
		OperatorSeo homeSeo = new OperatorSeo();
		homeSeo.setTitle(seoHomeTitle);//首页标题
		homeSeo.setDescriptor(seoHomeDescriptor);//首页描述
		homeSeo.setKeywords(seoHomeKeywords);//首页关键字
		homeSeo.setSeoType(OperatorSeo.SEO_TYPE_HOME);//首页类型
		homeSeo.setStatus(Status.NORMAL.getIntValue());//首页状态
		homeSeo.setCreateTime(currentTime);//创建时间
		homeSeo.setUpdateTime(currentTime);//更新时间
		homeSeo.setCreateOperatorId(operatorUserId);//创建管理员ID
		operatorSeoMapper.insert(homeSeo);
		//默认
		OperatorSeo defaultSeo = new OperatorSeo();
		defaultSeo.setTitle(seoDefaultTitle);//默认标题
		defaultSeo.setDescriptor(seoDefaultDescriptor);//默认描述
		defaultSeo.setKeywords(seoDefaultKeywords);//默认关键字
		defaultSeo.setSeoType(OperatorSeo.SEO_TYPE_DEFAULT);//默认类型
		defaultSeo.setStatus(Status.NORMAL.getIntValue());//默认状态
		defaultSeo.setCreateTime(currentTime);//创建时间
		defaultSeo.setUpdateTime(currentTime);//更新时间
		defaultSeo.setCreateOperatorId(operatorUserId);//创建管理员ID
		operatorSeoMapper.insert(defaultSeo);//修改默认seo
		
		
		//更改seo首页
		generateHomeFile(homeSeo);
		
		//更改相关页面
		BeetlUtil.generateRelativeFile(defaultSeo);

		BeetlUtil.generatePages(defaultSeo,OfficialPageConfig.PAGE_TYPE_WXA);
		BeetlUtil.generatePages(defaultSeo,OfficialPageConfig.PAGE_TYPE_WXA_AGENT);
		
		//更改新闻列表
		operatorArticleService.generateArticleList(defaultSeo);



		
		
		
	}


	private void generateHomeFile(OperatorSeo homeSeo) {
		int pageSize = LATEST_ARITICLE_SIZE;
		int current =1;
		Page<OperatorArticle> page = new Page<OperatorArticle>(current,pageSize);
		Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .orderBy("create_time", false);
		List<OperatorArticle> list = operatorArticleMapper.selectPage(page, wrapper);
		
		BeetlUtil.generateHomeFile(homeSeo,list);
		
	}

	@Override
	public OperatorSeo getDefaultSeo() {
		Wrapper<OperatorSeo> wrapper = new EntityWrapper<OperatorSeo>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .eq("seo_type", OperatorSeo.SEO_TYPE_DEFAULT);
		List<OperatorSeo> list = operatorSeoMapper.selectList(wrapper);
		return list.get(0);
	}

}
