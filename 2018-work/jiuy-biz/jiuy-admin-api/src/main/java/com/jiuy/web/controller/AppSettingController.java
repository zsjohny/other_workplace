/**
 * 
 */
package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.ad.AdType;
import com.jiuyuan.entity.ad.AdVo;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuy.core.service.AdService;
import com.jiuy.web.controller.util.model.CommonResponseObject;

/**
 * @author LWS
 *
 */
@Controller
@RequestMapping("/appsetting")
public class AppSettingController {
	
	@Autowired
	private AdService adService;
	
	/**********************************************************************************
	 *                               加载页面请求                                                                                                                                                *
	 **********************************************************************************/
	/**
	 * 加载轮播设置页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/loadrecycle")
	public String loadRecycle(ModelMap modelMap){
		return "";
	}
	
	
	/**********************************************************************************
	 *                                处理页面参数请求                                                                                                                                     *
	 **********************************************************************************/
	/**
	 * 处理轮播设置请求
	 * 
	 * @param settingList 每个元素以$分割的参数列表串，例如http://www.yujiejie.com/shangyi$1$2$http://www.yujiejie.com/shangyi
	 * @return
	 */
    @AdminOperationLog
	@RequestMapping("/modifycycle")
	public String disposeRecycle(@RequestParam(value="settinglist") String[] settingList){
		CommonResponseObject<String> resp = new CommonResponseObject<String>();
		if(null == settingList || settingList.length == 0){
			resp.setResult(ResultCode.COMMON_ERROR_PARAMETER_MISSING,null);
		}else{
			List<AdVo> advoList = buildAdvertisementVOList(settingList);
			adService.createAd(advoList);
		}
		return "json";
	}
	
	/**
	 * 删除指定轮播页面
	 * 
	 * @param advertisementId
	 * @return
	 */
    @AdminOperationLog
	@RequestMapping("/removecycle")
	public String deleteCycle(@RequestParam("adid") long advertisementId,ModelMap modelMap){
		int ret = adService.deleteAd(advertisementId);
		CommonResponseObject<Long> resp = new CommonResponseObject<Long>();
		if(ret > 0){
			resp.setResult(ResultCode.COMMON_SUCCESS, advertisementId);
		}else{
			resp.setResult(ResultCode.COMMON_NO_SUCH_ID, null);
		}
		modelMap.addAttribute("removecycle", resp);
		return "json";
	}


	
	/****************************************************************************************
	 *                              私有方法定义                                                                                                                                                                        *
	 ****************************************************************************************/
	private List<AdVo> buildAdvertisementVOList(String[] settingList) {
		List<AdVo> settingObjList = new ArrayList<AdVo>();
		for(String adSource : settingList){
			AdVo advo = makeAdVo(adSource);
			if(advo != null){
				settingObjList.add(advo);
			}
		}
		return settingObjList;
	}
	
	private AdVo makeAdVo(String advertisementSource) {
		if(StringUtils.isBlank(advertisementSource)){
			return null;
		}
		else{
			String[] fields = StringUtils.split(advertisementSource, "$");
			if( fields.length != 4 ){
				return null;
			}else{
				AdVo advo = new AdVo();
				advo.setAdOrder(Integer.valueOf(fields[2]));
				advo.setAdTitle("");
				advo.setAdType(AdType.APP_AD_TYPE.getIntValue());
				advo.setImageUrl(fields[0]);
				advo.setLinkUrl(fields[3]);
				advo.setNewPage(Integer.valueOf(fields[1]));
				long currentTime = new Date().getTime();
				advo.setCreateTime(currentTime);
				advo.setUpdateTime(currentTime);
				return advo;
			}
		}
		
	}
}
