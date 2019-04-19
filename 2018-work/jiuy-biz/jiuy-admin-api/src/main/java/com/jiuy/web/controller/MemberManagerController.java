package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.dao.MemberDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.meta.member.MemberSearch;
import com.jiuy.core.meta.member.MemberVO;
import com.jiuy.core.service.member.MemberSercive;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;


@Controller
@RequestMapping("/member")
public class MemberManagerController {
	
	@Resource 
	private MemberSercive memberSerciveImpl;
	
	@Resource
	private MemberDao memberDaoSqlImpl;
	
	@Resource
	private StoreBusinessDao storeBusinessDao;

    @AdminOperationLog
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String loadPage() {
		return "page/backend/memberManagement";
	}
	
	@RequestMapping(value="/search")
	@ResponseBody
	public JsonResponse search(@RequestParam(value="page", required=false, defaultValue="1")int page,
			@RequestParam(value="page_size", required=false, defaultValue="10")int pageSize,
			@RequestParam(value="yjj_number", required=false, defaultValue="-1")long yjjNumber,
			@RequestParam(value="phone", required=false, defaultValue="-1")long phone,
			@RequestParam(value="parent_id", required=false, defaultValue="-1")long parentYjjjNumber,
			@RequestParam(value="status", required=false, defaultValue="-2")int status,
			@RequestParam(value="partner_count_min", required=false, defaultValue="0")int partnerMin,
			@RequestParam(value="partner_count_max", required=false, defaultValue="-1")int partnerMax,
			@RequestParam(value="distribution_status", required=false, defaultValue="-2")int distributionStatus,
			@RequestParam(value="create_time_min", required=false, defaultValue="1970-1-1 00:00:00")String createTimeMinStr,
			@RequestParam(value="create_time_max", required=false, defaultValue="")String createTimeMaxStr,
			@RequestParam(value="jiucoin_min", required=false, defaultValue="0")int jiuCoinMin,
			@RequestParam(value="jiucoin_max", required=false, defaultValue="-1")int jiuCoinMax,
			@RequestParam(value="belong_store_id", required=false, defaultValue="-1")long belongStoreNumber
			) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
		MemberSearch meSearch = new MemberSearch();
		
		//转换时间
		long createTimeMinL = 0L;
		long createTimeMaxL = -1L;
		try {
			createTimeMinL = DateUtil.convertToMSEL(createTimeMinStr);
			if(!StringUtils.equals(createTimeMaxStr, "")){
				createTimeMaxL = DateUtil.convertToMSEL(createTimeMaxStr);
			}
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + createTimeMinL + " endTime:" + createTimeMaxL);
		}
		
		long parentUserId = -1;
		//将 上级俞姐姐号 找到对应的userId
		if(parentYjjjNumber!=-1){
			parentUserId = memberDaoSqlImpl.userIdOfYjjNumber(parentYjjjNumber);
		}
		
		long belongStoreId = -1;
		//找到对应门店商家id
		if(belongStoreNumber!= -1){
			belongStoreId = storeBusinessDao.getIdByBusinessNumber(belongStoreNumber);
		}
		
		meSearch.setYjjNumber(yjjNumber);
		meSearch.setBindPhone(phone);
		meSearch.setParentDistribution(parentUserId);
		meSearch.setStatus(status);
		meSearch.setPartnerCountMin(partnerMin);
		meSearch.setPartnerCountMax(partnerMax);
		meSearch.setDistributionStatus(distributionStatus);
		meSearch.setCreateTimeMin(createTimeMinL);
		meSearch.setCreateTimeMax(createTimeMaxL);
		meSearch.setJiuCoinMin(jiuCoinMin);
		meSearch.setJiuCoinMax(jiuCoinMax);
		meSearch.setBelongStoreId(belongStoreId);
		
		List<MemberVO> list = memberSerciveImpl.search(meSearch, pageQueryResult);
		int count = memberSerciveImpl.searchCount(meSearch);
		pageQueryResult.setRecordCount(count);
		
		//根据分销上级UserId查询分销上级俞姐姐号
		if(list.size()>0){
			ArrayList<Long> userIds = new ArrayList<>();
			for(MemberVO memberVO : list){
				userIds.add(memberVO.getParentDistribution());
			}
			Map<Long, HashMap<String, Long>> usersMap = memberDaoSqlImpl.getYJJNumberOfParentUserId(userIds);
			for(MemberVO memberVO : list){
				memberVO.setParentYJJNumber(usersMap.get(memberVO.getParentDistribution())==null?0:usersMap.get(memberVO.getParentDistribution()).get("YJJNumber"));
			}
		}
		
		data.put("authority", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}

    @AdminOperationLog
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse edit(@RequestParam(value="user_id")long userId, @RequestParam(value="status")int status, 
			@RequestParam(value="distribution_status")int distributionStatus) {
		JsonResponse jsonResponse = new JsonResponse();
		MemberVO mVo = new MemberVO();
		
		mVo.setUserId(userId);
		mVo.setStatus(status);
		mVo.setDistributionStatus(distributionStatus);
		
		int row = memberSerciveImpl.edit(mVo);
		
		return jsonResponse.setSuccessful().setData(row);
	}
    
    @AdminOperationLog
	@RequestMapping(value = "/unbundlingstore", method = RequestMethod.POST)
	@ResponseBody
    public JsonResponse unbundlingStore(@RequestParam(value="yjj_number")long yjjNumber,
    		@RequestParam(value="business_number")long businessNumber){
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	if(yjjNumber==0 || businessNumber ==0){
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("俞姐姐号或者所属门店ID不能为0");
    	}
    	
    	/*try {*/
    		long userId = memberSerciveImpl.userIdOfYjjNumber(yjjNumber);
        	//解绑
        	memberSerciveImpl.unbundlingStore(userId,businessNumber);
		/*} catch (Exception e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("解绑失败");
		}*/
    	
    	
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }

    @AdminOperationLog
	@RequestMapping(value = "/resetpwd", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse logOut(@RequestParam(value="yjj_number")long yjjCode) {
		JsonResponse jsonResponse = new JsonResponse();
		
		int row = memberSerciveImpl.resetpwd(yjjCode);
		
		return jsonResponse.setSuccessful().setData(row);
	}
	
}
