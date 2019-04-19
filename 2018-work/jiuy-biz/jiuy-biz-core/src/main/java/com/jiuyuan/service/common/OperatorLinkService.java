package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.core.util.BeetlUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.Status;
import com.jiuyuan.dao.mapper.supplier.OperatorRelationUrlMapper;
import com.jiuyuan.entity.newentity.OperatorRelationUrl;
@Service
public class OperatorLinkService implements IOperatorLinkService {
	private static final Logger logger = LoggerFactory.getLogger(OperatorLinkService.class);
	
	@Autowired
	private OperatorRelationUrlMapper operatorRelationUrlMapper;

	@Override
	public void add(long operatorUserId, String title, String replaceImageTitle, String imageUrl, String linkUrl,
			int sort) {
		long currentTime = System.currentTimeMillis();
		//友情链接添加
		OperatorRelationUrl operatorRelationUrl = new OperatorRelationUrl();
		operatorRelationUrl.setTitle(title);//友情链接标题
		operatorRelationUrl.setImageTitle(replaceImageTitle);//图片替代标题
		operatorRelationUrl.setImageUrl(imageUrl);//图片链接
		operatorRelationUrl.setJumpUrl(linkUrl);//跳跃链接
		operatorRelationUrl.setSort(sort);//排序
		operatorRelationUrl.setStatus(Status.NORMAL.getIntValue());//状态
		operatorRelationUrl.setCreateTime(currentTime);//创建时间
		operatorRelationUrl.setUpdateTime(currentTime);//更新时间
		operatorRelationUrl.setCreateOperatorId(operatorUserId);//创建管理员ID
		operatorRelationUrlMapper.insert(operatorRelationUrl);
		logger.info("添加友情链接！operatorUserId："+operatorUserId+",operatorRelationUrlId:"+operatorRelationUrl.getId());
		
		//生成友情链接静态页
		generateHtmlFile();
		
		
	}

	private void generateHtmlFile() {
		Wrapper<OperatorRelationUrl> wrapper = new EntityWrapper<OperatorRelationUrl>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .orderBy("id", false);
		List<OperatorRelationUrl> list = operatorRelationUrlMapper.selectList(wrapper);
		BeetlUtil.generateFooterFile(list);
	}

	@Override
	public void update(long operatorUserId, String title, String replaceImageTitle, String imageUrl, String linkUrl,
			int sort, long operatorRelationUrlId) {
		long currentTime = System.currentTimeMillis();
		OperatorRelationUrl operatorRelationUrl = new OperatorRelationUrl();
		operatorRelationUrl.setId(operatorRelationUrlId);//id
		operatorRelationUrl.setTitle(title);//友情链接标题
		operatorRelationUrl.setImageTitle(replaceImageTitle);//图片替代标题
		operatorRelationUrl.setImageUrl(imageUrl);//图片链接
		operatorRelationUrl.setJumpUrl(linkUrl);//跳跃链接
		operatorRelationUrl.setSort(sort);//排序
		operatorRelationUrl.setStatus(Status.NORMAL.getIntValue());//状态 0:正常 -1:删除
		operatorRelationUrl.setUpdateTime(currentTime);//更新时间
		operatorRelationUrlMapper.updateById(operatorRelationUrl);
		logger.info("更新友情链接！operatorUserId："+operatorUserId+",operatorRelationUrlId:"+operatorRelationUrlId);
		
		//生成静态图
		generateHtmlFile();
		
	}

	@Override
	public void delete(long operatorUserId, long operatorRelationUrlId) {
		long currentTime = System.currentTimeMillis();
		//删除友情链接
		OperatorRelationUrl operatorRelationUrl = new OperatorRelationUrl();
		operatorRelationUrl.setId(operatorRelationUrlId);//友情链接
		operatorRelationUrl.setStatus(Status.DELETE.getIntValue());//状态
		operatorRelationUrl.setDeleteTime(currentTime);
		operatorRelationUrl.setDeleteOperatorId(operatorUserId);
		operatorRelationUrlMapper.updateById(operatorRelationUrl);
		logger.info("删除友情链接！operatorUserId："+operatorUserId+",operatorRelationUrlId:"+operatorRelationUrlId);
		
		//生成静态图
		generateHtmlFile();
	}

	@Override
	public Map<String,Object> detail(long operatorRelationUrlId) {
		Map<String,Object> map = new HashMap<>();
		OperatorRelationUrl operatorRelationUrl = operatorRelationUrlMapper.selectById(operatorRelationUrlId);
		
		map.put("id", operatorRelationUrl.getId());//友情链接id
		map.put("title", operatorRelationUrl.getTitle());//友情链接标题
		map.put("imageTitle", operatorRelationUrl.getImageTitle());//友情链接图片替代标题
		map.put("imageUrl", operatorRelationUrl.getImageUrl());//友情链接URL
		map.put("sort", operatorRelationUrl.getSort());//排序
		map.put("jumpUrl", operatorRelationUrl.getJumpUrl());//跳转URL
		return map;
		
	}

	@Override
	public List<Map<String, Object>> list(Page<Map<String, Object>> page) {
		Wrapper<OperatorRelationUrl> wrapper = new EntityWrapper<OperatorRelationUrl>();
		wrapper.eq("status", Status.NORMAL.getIntValue())
		       .orderBy("id", false);
		List<OperatorRelationUrl> list = operatorRelationUrlMapper.selectPage(page, wrapper);
		List<Map<String,Object>> result = new ArrayList<>();
		for(OperatorRelationUrl operatorRelationUrl:list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", operatorRelationUrl.getId());//id
			map.put("title", operatorRelationUrl.getTitle());//标题
			map.put("imageUrl", operatorRelationUrl.getImageUrl());//图片链接
			map.put("imageTitle", operatorRelationUrl.getImageTitle());//图片替代标题
			map.put("sort", operatorRelationUrl.getSort());//排序
			result.add(map);
		}
		return result;
	}
	

}
