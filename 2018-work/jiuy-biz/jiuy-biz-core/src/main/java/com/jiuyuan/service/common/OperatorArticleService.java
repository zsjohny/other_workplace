package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.core.template.config.OfficialPageConfig;
import com.admin.core.util.BeetlUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.OperatorArticleMapper;
import com.jiuyuan.dao.mapper.supplier.OperatorSeoMapper;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.entity.newentity.OperatorSeo;
import com.jiuyuan.util.UrlUtil;

@Service
public class OperatorArticleService implements IOperatorArticleService {
	private static final Logger logger = LoggerFactory.getLogger(OperatorArticleService.class);
	
	private static final int PAGE_SIZE = 10;
	
	private static final int LATEST_ARITICLE_SIZE = 8;
    
	@Autowired
	private OperatorArticleMapper operatorArticleMapper;
	
	@Autowired
	private OperatorSeoMapper operatorSeoMapper;
	
	@Autowired
	private IOperatorSeoService operatorSeoService;
	
	
	
	@Override
	public void add(String title, String abstracts, String previewImageUrl, String content, String seoTitle,
			String seoDescriptor, String seoKeywords, long operatorId) {
		long currentTime = System.currentTimeMillis();
		//文章创建
		OperatorArticle operatorArticle = new OperatorArticle();
		operatorArticle.setTitle(title);//标题
		operatorArticle.setAbstracts(abstracts);//摘要
		operatorArticle.setPreviewImageUrl(previewImageUrl);//预览图
		operatorArticle.setContent(UrlUtil.getURLDecoderString(content));//内容
		//seo标题
		if(!StringUtils.isEmpty(seoTitle)){
			operatorArticle.setSeoTitle(seoTitle);//seo标题
		}
		//seo描述
		if(!StringUtils.isEmpty(seoDescriptor)){
			operatorArticle.setSeoDescriptor(seoDescriptor);//seo描述
		}
		//seo关键词
		if(!StringUtils.isEmpty(seoKeywords)){
			operatorArticle.setSeoKeywords(seoKeywords);
		}
		operatorArticle.setCreateOperatorId(operatorId);
		operatorArticle.setCreateTime(currentTime);
		operatorArticle.setUpdateTime(currentTime);
		operatorArticleMapper.insert(operatorArticle);
		
		OperatorSeo	operatorSeo = operatorSeoService.getDefaultSeo();
		//添加新闻详情
		BeetlUtil.generateArticleDetailFile(operatorArticle,operatorSeo);
		
		//重新生成新闻列表
		generateArticleList(operatorSeo);
		
		//重新生成首页
		generateHomeFile();
		
		
	}


	private void generateHomeFile() {
		Wrapper<OperatorSeo> seoWrapper = new EntityWrapper<OperatorSeo>();
		seoWrapper.eq("seo_type", OperatorSeo.SEO_TYPE_HOME)
		       .eq("status", Status.NORMAL.getIntValue());
		List<OperatorSeo> seoList = operatorSeoMapper.selectList(seoWrapper);
		OperatorSeo homeSeo = seoList.get(0);
		
		int pageSize = LATEST_ARITICLE_SIZE;
		int current =1;
		Page<OperatorArticle> page = new Page<OperatorArticle>(current,pageSize);
		Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .orderBy("create_time", false);
		List<OperatorArticle> list = operatorArticleMapper.selectPage(page, wrapper);
		//生成官网
		BeetlUtil.generateHomeFile(homeSeo,list);
		
	}

    @Override
	public void generateArticleList(OperatorSeo operatorSeo) {
		int pageSize = PAGE_SIZE;
		int current = 1;
		while(true){
			Page<OperatorArticle> page = new Page<OperatorArticle>(current,pageSize);
			Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
			wrapper.eq("status", Status.NORMAL.getIntValue())
			       .orderBy("create_time", false);
			List<OperatorArticle> list = operatorArticleMapper.selectPage(page, wrapper);
			if(list.size() <= 0){
				break;
			}
			page.setRecords(list);
			//生成官网新闻列表
			BeetlUtil.generateArticleListFile(page,operatorSeo);
			current++;
		}
	}


	@Override
	public Map<String, Object> detail(long operatorArticleId) {
		OperatorArticle operatorArticle = operatorArticleMapper.selectById(operatorArticleId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", operatorArticle.getTitle());//标题
		map.put("abstracts", operatorArticle.getAbstracts());//摘要
		map.put("id", operatorArticle.getId());//id
		map.put("previewImageUrl", operatorArticle.getPreviewImageUrl());//预览图
		map.put("content", operatorArticle.getContent());//正文
		map.put("seoTitle", operatorArticle.getSeoTitle());//seo标题
		map.put("seoDescriptor", operatorArticle.getSeoDescriptor());//seo描述
		map.put("seoKeywords", operatorArticle.getSeoKeywords());//seo关键字
		return map;
	}


	@Override
	public void update(String title, String abstracts, String previewImageUrl, String content, String seoTitle, String seoDescriptor, String seoKeywords, long operatorArticleId) {
		long currentTime = System.currentTimeMillis();
		//文章创建
		OperatorArticle operatorArticle = new OperatorArticle();
		operatorArticle.setId(operatorArticleId);//id
		operatorArticle.setTitle(title);//标题
		operatorArticle.setAbstracts(abstracts);//摘要
		operatorArticle.setPreviewImageUrl(previewImageUrl);//预览图
		operatorArticle.setContent(UrlUtil.getURLDecoderString(content));//内容
		//seo标题
		if(!StringUtils.isEmpty(seoTitle)){
			operatorArticle.setSeoTitle(seoTitle);//seo标题
		}
		//seo描述
		if(!StringUtils.isEmpty(seoDescriptor)){
			operatorArticle.setSeoDescriptor(seoDescriptor);//seo描述
		}
		//seo关键词
		if(!StringUtils.isEmpty(seoKeywords)){
			operatorArticle.setSeoKeywords(seoKeywords);
		}
		operatorArticle.setUpdateTime(currentTime);
		operatorArticleMapper.updateById(operatorArticle);
		
		OperatorSeo	operatorSeo = operatorSeoService.getDefaultSeo();
		//添加新闻详情
		BeetlUtil.generateArticleDetailFile(operatorArticle,operatorSeo);
		
		//重新生成新闻列表
		generateArticleList(operatorSeo);
		
		//重新生成首页
		generateHomeFile();
		
		
	}


	@Override
	public void delete(long operatorArticleId, long operatorUserId) {
		long currentTime = System.currentTimeMillis();
		OperatorArticle operatorArticle = new OperatorArticle();
		operatorArticle.setId(operatorArticleId);
		operatorArticle.setStatus(Status.DELETE.getIntValue());
		operatorArticle.setUpdateTime(currentTime);
		operatorArticle.setDeleteTime(currentTime);
		operatorArticle.setDeleteOperatorId(operatorUserId);
		operatorArticleMapper.updateById(operatorArticle);
		
		OperatorSeo	operatorSeo = operatorSeoService.getDefaultSeo();
		//重新生成新闻列表
		generateArticleList(operatorSeo);
		
		//重新生成首页
		generateHomeFile();
		
		
	}


	@Override
	public int homePage(long operatorUserId) {
		Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
		wrapper.eq("status", Status.NORMAL.getIntValue());
		return operatorArticleMapper.selectCount(wrapper);
	}


	@Override
	public List<Map<String,Object>> list(Page<Map<String,Object>> page, String articleTitle, String articleAbstracts) {
		Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .orderBy("create_time", false);
		if(!StringUtils.isEmpty(articleTitle)){
			wrapper.like("title", articleTitle);
		}
		if(!StringUtils.isEmpty(articleAbstracts)){
			wrapper.like("abstracts", articleAbstracts);
		}
		List<OperatorArticle> list = operatorArticleMapper.selectPage(page, wrapper);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(OperatorArticle operatorArticle:list){
			Long id = operatorArticle.getId();
			String title = operatorArticle.getTitle();
			String previewImageUrl = operatorArticle.getPreviewImageUrl();
			String abstracts = operatorArticle.getAbstracts();
			String content = operatorArticle.getContent();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", id);//文章ID
			map.put("title", title);//文章标题
			map.put("previewImageUrl", previewImageUrl);//预览图的链接
			map.put("abstracts", abstracts);//文章摘要
			map.put("content", content);//文章内容
			result.add(map);
		}
		return result;
	}


	@Override
	public int getArticleCount() {
		Wrapper<OperatorArticle> wrapper = new EntityWrapper<OperatorArticle>();
	    wrapper.eq("status", Status.NORMAL.getIntValue());
		return operatorArticleMapper.selectCount(wrapper);
	}

}
