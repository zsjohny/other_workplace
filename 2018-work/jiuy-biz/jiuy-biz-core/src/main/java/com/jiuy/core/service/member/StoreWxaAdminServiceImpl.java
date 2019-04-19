package com.jiuy.core.service.member;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.business.facade.ProductSkuFacade;
import com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper;
import com.jiuy.core.dao.impl.sql.StoreWxaCodeAdminMapper;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.entity.store.StoreWxaCode;
import com.xiaoleilu.hutool.date.DateUtil;

/**
 */
@Service("storeWxaAdminService")
public class StoreWxaAdminServiceImpl implements StoreWxaAdminService {
	
	
	 private static final Logger logger = LoggerFactory.getLogger(StoreWxaAdminServiceImpl.class);
	 
	@Autowired
	private StoreWxaAdminMapper storeWxaAdminMapper;
	
	
	@Autowired
	private StoreWxaCodeAdminMapper storeWxaCodeAdminMapper;
	@Override
	public StoreWxa getStoreWxaByStoreId(long storeId) {
		return storeWxaAdminMapper.getStoreWxaByStoreId(storeId);
	}
	
	@Override
	public StoreWxaCode getStoreWxaCodeByStoreId(long storeId) {
		StoreWxa storeWxa = getStoreWxaByStoreId(storeId);
		
		StoreWxaCode storeWxaCode = null;
		if(storeWxa != null){
			storeWxaCode = storeWxaCodeAdminMapper.getByWxaId(storeWxa.getId());
		}
		
		return storeWxaCode;
	}
	
	
	@Override
	public StoreWxaCode getStoreWxaCodeByWxaId(long wxaId) {
		return storeWxaCodeAdminMapper.getByWxaId(wxaId);
	}

	
	/**
	 * 上传代码更新小程序代码信息
	 * @param storeId
	 * @param templateId
	 * @param version
	 * @param desc
	 */
	public void updateWxaCodeUploadCode(long storeId, String templateId, String version, String desc){
		StoreWxa storeWxa = getStoreWxaByStoreId(storeId);
		StoreWxaCode storeWxaCode = storeWxaCodeAdminMapper.getByWxaId(storeWxa.getId());
		if(storeWxaCode != null){
			storeWxaCodeAdminMapper.updateWxaCodeUploadCode(storeWxaCode.getId(),templateId,version,desc);
		}else{
			storeWxaCodeAdminMapper.insertWxaCode(storeWxa.getId(),templateId,version,desc);
		}
		
		
	}
	
	/**
	 * 上传代码更新小程序代码信息
	 * @param storeId
	 * @param templateId
	 * @param version
	 * @param desc
	 */
	public void updateWxaCodeSubmitAudit(long storeId){
		StoreWxa storeWxa = getStoreWxaByStoreId(storeId);
		StoreWxaCode storeWxaCode = storeWxaCodeAdminMapper.getByWxaId(storeWxa.getId());
		if(storeWxaCode != null){
			storeWxaCodeAdminMapper.updateWxaCodeSubmitAudit(storeWxa.getId());
		}
	}
	/**
	 * 更改小程序发布版二维码
	 * @param storeId
	 * @param onlineWxaQrcodeUrl
	 */
	public int updateWxaOnlineWxaQrcodeUrl(long storeId, String onlineWxaQrcodeUrl) {
		
		return storeWxaAdminMapper.updateWxaOnlineWxaQrcodeUrl(storeId,onlineWxaQrcodeUrl);
	}
	/**
	 * 设置商户号
	 * @param storeId
	 * @param mchId
	 */
	public int setMchId(long storeId, String mchId) {
		return storeWxaAdminMapper.setMchId(storeId,mchId);
	}
	/**
	 * 设置商户秘钥
	 * @param storeId
	 * @param payKey
	 */
	public int setPayKey(long storeId, String payKey) {
		return storeWxaAdminMapper.setPayKey(storeId,payKey);
	}
	
	


/**
	 * 取消小程序授权（解除小程序和门店的关联）
	 * SELECT * FROM yjj_online.yjj_StoreBusiness where PhoneNumber like '%15268876468%';
update yjj_StoreBusiness set WxaAppId='',WeiXinNum='' where Id = 7520;
update jiuy_store_wxa set store_id=8, nick_name='15268876468_7520' WHERE store_id = 7520;
	 */
	public void cancelAuth(long storeId){
//		StoreBusiness storeBusiness = storeMapper.selectById(storeId);
		//修改门店关联小程序为空串
//		StoreBusiness updStoreBusiness = new StoreBusiness();
//		updStoreBusiness.setId(storeId);
//		updStoreBusiness.setWxaAppId("");
//		updStoreBusiness.setWeiXinNum("");
		storeWxaAdminMapper.cancelAuth(storeId);
		//删除小程序关联
		storeWxaAdminMapper.delStoreWxaByStoreId(storeId);
		logger.info("删除小程序关联完成，storeId："+storeId);
	}

	
	
}
