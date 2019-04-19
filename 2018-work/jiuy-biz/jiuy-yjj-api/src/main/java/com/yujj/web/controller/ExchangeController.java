package com.yujj.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.BatchNumber;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.QrCode;
import com.jiuyuan.entity.account.UserMember;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ExchangeFacade;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.BatchNumberService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.QrCodeService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserMemberService;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserFavoriteMapper;
import com.yujj.entity.StoreBusiness;
import com.yujj.entity.account.UserDetail;

@Controller
@RequestMapping("/exchange")
public class ExchangeController {
    
    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private BatchNumberService batchNumberService;

    @Autowired
    private ExchangeFacade exchangeFacade;
    
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;
    
    @Autowired
    private UserMemberService userMemberService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private UserCoinService userCoinService;
    
	@Autowired
	private UserFavoriteMapper userFavoriteMapper;
	
	@Autowired
	private GlobalSettingService globalSettingService;
	

    @RequestMapping("/{code}")
    public String index(@PathVariable String code, Map<String, Object> model) {
        QrCode qrCode = qrCodeService.getQrCode(code);
        if (qrCode == null) {
            return Constants.ERROR_PAGE_NOT_FOUND;
        }
        
        String[] parts = StringUtils.split(code, "-");
        String supplierCode = parts[0];
        int innerCode = Integer.parseInt(parts[1]);
        BatchNumber batchNumber = batchNumberService.getBatchNumber(qrCode.getBatch_no(), supplierCode, innerCode);
        // 增加二维码无法扫描问题临时规避代码，待问题处理解决后去掉
        // 处理方法：如果batchNumber为null，增加一次获取批次号的逻辑
        // add by LiuWeisheng
        if(null == batchNumber){
            batchNumber = batchNumberService.getBatchNumberPatch(qrCode.getBatch_no(), supplierCode, innerCode);
        }
        model.put("batchNumber", batchNumber);

        return "exchange/index";
    }
    
    @RequestMapping("/coin/{code}")
    @ResponseBody
    public JsonResponse getCoin(@PathVariable String code) {
        JsonResponse jsonResponse = new JsonResponse();

        QrCode qrCode = qrCodeService.getQrCode(code);
        if (qrCode == null) {
            return jsonResponse.setResultCode(ResultCode.EXCHANGE_ERROR_BATCH_NUMBER);
        }

        String[] parts = StringUtils.split(code, "-");
        String supplierCode = parts[0];
        int innerCode = Integer.parseInt(parts[1]);
        BatchNumber batchNumber = batchNumberService.getBatchNumber(qrCode.getBatch_no(), supplierCode, innerCode);
        if(batchNumber == null) {
            // 增加二维码无法扫描问题临时规避代码，待问题处理解决后去掉
            // 处理方法：如果batchNumber为null，增加一次获取批次号的逻辑
            // add by LiuWeisheng
            batchNumber = batchNumberService.getBatchNumberPatch(qrCode.getBatch_no(), supplierCode, innerCode);
            if (null == batchNumber){
                return jsonResponse.setResultCode(ResultCode.EXCHANGE_ERROR_BATCH_NUMBER);
            }
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("coins", batchNumber.getToken_ratio());

        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping("/exchange/{code}")
    @Login
    @ResponseBody
    public JsonResponse exchange(@PathVariable("code") String code, UserDetail userDetail, ClientPlatform clientPlatform) {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, String> regSucessImgMap = new HashMap<String, String>();

        if (code.contains("type=") && code.contains("id=")) {
        	String type_str = getParamByRegEx("type", code);
        	String id_str = getParamByRegEx("id", code);
        	
			if (!isInteger(type_str) || !isInteger(id_str)) {
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("二维码中的type或id参数错误,不是整数");
			}
			Integer type = Integer.parseInt(type_str);
			Long id = Long.parseLong(id_str);

			String img2208 = null;
			String img1136 = null;
			String img960 = null;
			String img1334 = null;
			int grant_coins = 0;
			
			if (type.equals(2)) {
				StoreBusiness sb = storeBusinessMapper.getById(id);
				if (sb == null) {
					return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARTIAL_FAILURE).setError("门店不存在");
				}
				
				/**
				 * 扫码得玖币
				 */
				JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
				JSONObject storeScanReward = jiucoin_global_setting.getJSONObject("storeScanReward");
				//周期
				int obtainCycle = storeScanReward.getInteger("obtainCycle");
				//周期内最多次数
				int maxCountEachCycle = storeScanReward.getInteger("maxCountEachCycle");
				//同一门店扫码获得积分不得超过
				int maxScanCount = storeScanReward.getInteger("maxScanCount");
				
		        long time = System.currentTimeMillis();
		        long startTime = DateUtil.getTodayEnd() - obtainCycle * DateUtils.MILLIS_PER_DAY;
		        long endTime = DateUtil.getTodayEnd();
		        
		        UserCoinOperation operation = UserCoinOperation.SCAN_STORE_QRCODE;
		        
		        //周期内扫码次数
		        int cycle_count = userCoinService.getUserCoinLogCount(userDetail.getUserId(), startTime, endTime, operation);
				//扫码统一门店次数
		        int cycle_same_count = userCoinService.getUserCoinSameLogCount(userDetail.getUserId(), null, null, operation, sb.getId());
		        if (cycle_count < maxCountEachCycle && cycle_same_count < maxScanCount) {
		        	int eachObtain = storeScanReward.getInteger("eachObtain");
		        	userCoinService.updateUserCoin(userDetail.getUserId(), 0, eachObtain, sb.getId() + "", System.currentTimeMillis(), operation, null, clientPlatform.getVersion());
				}
		        
		        
				List<UserFavorite> userFavorites = userFavoriteMapper.getUserFavorite(CollectionUtil.createList(id), FavoriteType.STORE_BUSINESS.getIntValue(), userDetail.getUserId());
				boolean already_follow = false;
				if (userFavorites.size() > 0) {
					already_follow = true;
					
					grant_coins = 0;
					img2208 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/1242x2208_cared.png";
					img1136 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x1136_cared.png";
					img960 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x960_cared.png";
					img1334 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/750x1334_cared.png";
					
				} else {
					already_follow = false;
					Activity activity = activityService.getActivity("collectstore");
					if (activity != null ) {		
						grant_coins = activity.getGrantAmountRandom();
					}
//					userCoinService.updateUserCoin(userDetail.getUserId(), 0, grant_coins, activity.getActivityCode(), System.currentTimeMillis(), UserCoinOperation.ACTIVITY);
					
					img2208 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/1242x2208_success.png";
					img1136 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x1136_success.png";
					img960 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x960_success.png";
					img1334 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/750x1334_success.png";
				}
				
				regSucessImgMap.put("img2208", img2208);
				regSucessImgMap.put("img1136", img1136);
				regSucessImgMap.put("img960", img960);
				regSucessImgMap.put("img1334", img1334);
				data.put("coins", grant_coins);
				
				StoreBusiness storeBusiness = storeBusinessMapper.getById(id);
				
				UserMember userMember = new UserMember(userDetail.getUserId(), time, time, 0, null, 0, storeBusiness.getBusinessName(), id, "");
				userMemberService.add(userMember, time);
				
				data.put("link_url", "/static/app/scanstore.html?already_follow=" + already_follow + "&store_id=" + id);
			}
		} else {
			data.put("link_url", "/static/app/scan.html");
			
//			QrCode qrCode = qrCodeService.getQrCode(code);
//			if (qrCode == null) {
//				return jsonResponse.setResultCode(ResultCode.EXCHANGE_ERROR_BATCH_NUMBER);
//			} else if (qrCode.getStatus() != 1) {
//				return jsonResponse.setResultCode(ResultCode.EXCHANGE_ERROR_EXCHANGED);
//			}
//			
//			String[] parts = StringUtils.split(code, "-");
//			String supplierCode = parts[0];
//			int innerCode = Integer.parseInt(parts[1]);
//			BatchNumber batchNumber = batchNumberService.getBatchNumber(qrCode.getBatch_no(), supplierCode, innerCode);
//			if (batchNumber == null) {
//				// 临时增加代码：修复二维码无法扫描的问题，待二维码生成规则错误问题解决后去掉
//				// 修改逻辑：将二位码的生成规则与批次号的匹配关系改为  供应商代码 + 批次ID + 随机串
//				// begin
//				batchNumber = batchNumberService.getBatchNumberPatch(qrCode.getBatch_no(), supplierCode, innerCode);
//				// end
//				if(null == batchNumber){
//					return jsonResponse.setResultCode(ResultCode.EXCHANGE_ERROR_BATCH_NUMBER);
//				}
//			}
//			
//			exchangeFacade.exchange(userDetail.getUserId(), qrCode, batchNumber);
//			
//			data.put("coins", batchNumber.getToken_ratio());
//			String img2208 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/1242x2208.jpg";
//			String img1136 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x1136.jpg";
//			String img960 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/640x960.jpg";
//			String img1334 = "http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/scanner/750x1334.jpg";
//			regSucessImgMap.put("img2208", img2208);
//			regSucessImgMap.put("img1136", img1136);
//			regSucessImgMap.put("img960", img960);
//			regSucessImgMap.put("img1334", img1334);
		}
        
    	data.put("regSucessImgMap", regSucessImgMap); 

        return jsonResponse.setSuccessful().setData(data);
    }

	private String getParamByRegEx(String param, String code) {
		Pattern pat_type = Pattern.compile(param + "=([0-9]*)");
    	Matcher mat;
    	mat = pat_type.matcher(code);
    	String result = null;
		while (mat.find()) {
			result = mat.group(1);
		}
		return result;
	}
	
	private boolean isInteger(String str) {    
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");    
	    return pattern.matcher(str).matches();    
	}  

}
