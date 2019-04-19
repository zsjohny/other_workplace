package com.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.store.StoreWxa;
import com.store.dao.mapper.StoreWxaMapper;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21 extends ServiceImpl<MessageMapper); Message>
 */
@Service
public class StoreWxaService {
	private static final Log logger = LogFactory.get();
	@Autowired
	private StoreWxaMapper storeWxaMapper;

	/**
	 * 从数据库获取刷新token
	 * @return
	 */
	public String getRefreshToken(String appId) {
		StoreWxa storeWxa = getStoreWxaByAppId(appId);
		return storeWxa.getRefreshToken();
	}

	/**
	 * 保存小程序刷新token
	 * 
	 * @return
	 */
	public void setRefreshToken(String appId, String refreshToken) {
		
		StoreWxa storeWxa = getStoreWxaByAppId(appId);
		if(storeWxa == null ){
			logger.info("开始更新小程序的刷新token到数据库，小程序表记录不能为空请，尽快排查问题refreshToken："+refreshToken+",appId:"+appId+",storeWxa:"+JSONObject.toJSONString(storeWxa));
		}
//		logger.info("开始更新小程序的刷新token到数据库，refreshToken："+refreshToken+",appId:"+appId+",storeWxa:"+JSONObject.toJSONString(storeWxa));
		StoreWxa storeWxaNew = new StoreWxa();
		storeWxaNew.setId(storeWxa.getId());
		storeWxaNew.setRefreshToken(refreshToken);
		int ret = storeWxaMapper.updateById(storeWxaNew);
		logger.info("更新小程序的刷新token到数据库，结果："+ret);
	}

	public StoreWxa add(String storeId, String authorizer_appid, String nick_name, String head_img, String user_name,
			String principal_name, String idc, String signature, String alias, String qrcode_url,
			String authorizer_info_json) {
		StoreWxa storeWxa = new StoreWxa();
		storeWxa.setStoreId(Long.parseLong(storeId));
		storeWxa.setAppId(authorizer_appid);
		storeWxa.setNickName(nick_name);
		storeWxa.setHeadImg(head_img);
		storeWxa.setUserName(user_name);
		storeWxa.setAlias(alias);
		storeWxa.setAuthorizerInfoJson(authorizer_info_json);
		storeWxa.setCreateTime(DateUtil.current(false));
		storeWxa.setIdc(idc);
		storeWxa.setPrincipalName(principal_name);
		storeWxa.setSignature(signature);
		storeWxaMapper.insert(storeWxa);
		logger.info("添加storeWxa成功，storeWxa:" + storeWxa.toString());
		return storeWxa;
	}

	/**
	 * 获取商家绑定的小程序
	 * 
	 * @param storeId
	 * @return
	 */
	public StoreWxa getStoreWxaByStoreId(String storeId) {
		Wrapper<StoreWxa> wrapper = new EntityWrapper<StoreWxa>().eq("store_id", storeId);
//		List<StoreWxa> list = storeWxaMapper.selectStoreWxa(Long.parseLong(storeId));
		List<StoreWxa> list = storeWxaMapper.selectList(wrapper);
		StoreWxa storeWxa = null;
		if (list.size() > 0) {
			storeWxa = list.get(0);
		}
		return storeWxa;
	}

	/**
	 * 获取商家绑定的小程序
	 * 
	 */
	public StoreWxa getStoreWxaByAppId(String appId) {
		//Wrapper<StoreWxa> wrapper = new EntityWrapper<StoreWxa>().eq("app_id", appId);
		List<StoreWxa> list = storeWxaMapper.selectByAppId(appId);
		//List<StoreWxa> list = storeWxaMapper.selectList(wrapper);
		StoreWxa storeWxa = null;
		if (list.size() > 0) {
			storeWxa = list.get(0);
		}
		return storeWxa;
	}
	
	/**
	 * 获取门店小程序二维码
	 * @param StoreId
	 * @return
	 */
	public String getQRCode(long StoreId) {
		String src = "";
		Map<String,Object> selectMap = new HashMap<String,Object>();
		selectMap.put("store_id", StoreId);
		List<StoreWxa> storeWxaList = storeWxaMapper.selectByMap(selectMap);
		if(storeWxaList != null  && storeWxaList.size() > 0){
			StoreWxa storeWxa = storeWxaList.get(0);
			if(storeWxa != null && storeWxa.getQrcodeUrl() != null){
				src = storeWxa.getQrcodeUrl();
			}
		}
		return src;
	}

}