package com.jiuy.core.service.member;

import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.entity.store.StoreWxaCode;


/**
* @author WuWanjian
* @version 创建时间: 2016年10月25日 下午3:35:59
*/
public interface StoreWxaAdminService {
	
	public StoreWxaCode getStoreWxaCodeByWxaId(long wxaId);

	/**
	 * 根据门店ID获取门店小程序信息
	 * @param storeId
	 * @return
	 */
	public StoreWxa getStoreWxaByStoreId(long storeId);
	
	/**
	 * 根据门店ID获取门店小程序代码信息
	 * @param storeId
	 * @return
	 */
	public StoreWxaCode getStoreWxaCodeByStoreId(long storeId);

	/**
	 * 上传代码更新小程序代码信息
	 * @param storeId
	 * @param templateId
	 * @param version
	 * @param desc
	 */
	public void updateWxaCodeUploadCode(long storeId, String templateId, String version, String desc);

	
	public void updateWxaCodeSubmitAudit(long storeId);
	/**
	 * 更改小程序发布版二维码
	 * @param storeId
	 * @param onlineWxaQrcodeUrl
	 */
	public int updateWxaOnlineWxaQrcodeUrl(long storeId, String onlineWxaQrcodeUrl);
	/**
	 * 设置商户号
	 * @param storeId
	 * @param mchId
	 */
	public int setMchId(long storeId, String mchId);
	/**
	 * 设置商户秘钥
	 * @param storeId
	 * @param payKey
	 */
	public int setPayKey(long storeId, String payKey);
	
	/**
	 * 取消小程序授权（解除小程序和门店的关联）
	 * SELECT * FROM yjj_online.yjj_StoreBusiness where PhoneNumber like '%15268876468%';
update yjj_StoreBusiness set WxaAppId='',WeiXinNum='' where Id = 7520;
update jiuy_store_wxa set store_id=8, nick_name='15268876468_7520' WHERE store_id = 7520;
	 */
	void cancelAuth(long storeId);
}
