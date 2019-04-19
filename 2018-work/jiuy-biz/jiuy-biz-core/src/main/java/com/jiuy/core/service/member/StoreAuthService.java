package com.jiuy.core.service.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.impl.sql.StoreAuthMapper;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.StoreAuth;

/**
 * @author qiuyuefan
 */
@Service
public class StoreAuthService{
	
	@Autowired
	private StoreAuthMapper storeAuthMapper;
	
	/**
	 * 获取新门店审核列表
	 * @param status		状态:’0：提交审核 1：审核通过 -1:审核不通过’
	 * @param pageQuery
	 * @param keyWord 
	 * @param isVip 
	 * @return
	 */
	public List<StoreAuth>  getAuthList(Integer authState, PageQuery pageQuery, Integer authType, String keyWord) {
		return storeAuthMapper.selectAuthList(authState,pageQuery,authType,keyWord);
	}
	
	/**
	 * 获取新门店审核列表
	 * @param status		状态:’0：提交审核 1：审核通过 -1:审核不通过’
	 * @param pageQuery
	 * @param keyWord 
	 * @param isVip 
	 * @return
	 */
	public int getAuthListCount(Integer authState, Integer authType, String keyWord) {
		return storeAuthMapper.selectAuthListCount(authState,authType,keyWord);
	}

	
	/**
	 * 审核通过
	 * @param storeAuthId	门店认证信息Id
	 * @return
	 */
	public int setAuthPass(long storeAuthId) {
		return storeAuthMapper.setAuthPass(storeAuthId);
	}
	/**
	 * 审核不通过
	 * @param storeAuthId	门店认证信息Id
	 * @return
	 */
	public int setAuthNoPass(long storeAuthId,String noPassReason) {
		return storeAuthMapper.setAuthNoPass(storeAuthId,noPassReason);
	}

	/**
	 * 获取预设拒绝原因列表
	 * @param pageQuery 
	 * @return
	 */
	public List<Map<String, String>> getPreinstallNoPassReasonList(PageQuery pageQuery,int type) {
		return storeAuthMapper.getPreinstallNoPassReasonList(pageQuery,type);
	}

	/**
	 * 删除预设理由
	 * @param authReasonId
	 * @return
	 */
	public int delPreinstallNoPassReason(long authReasonId) {
		return storeAuthMapper.delPreinstallNoPassReason(authReasonId);
	}

	/**
	 * 添加预设拒绝理由
	 * @param authReason
	 * @return
	 */
	public int setPreinstallNoPassReason(String authReason,int type) {
		return storeAuthMapper.setPreinstallNoPassReason(authReason,type);
	}

	/**
	 * 获取预设拒绝原因列表数量
	 * @return
	 */
	public int getPreinstallNoPassReasonListCount() {
		return storeAuthMapper.getPreinstallNoPassReasonListCount();
	}
	
	
}