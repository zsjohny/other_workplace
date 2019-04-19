package com.yujj.web.controller.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.visit.UserVisitHistory;
import com.jiuyuan.entity.visit.UserVisitHistoryVO;

import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.VisitService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Product;

@Controller
@Login
@RequestMapping("/mobile/user/visit")
public class MobileUserVisitController {

    @Autowired
    private VisitService visitService;

	@Autowired
	private ProductService productService;

    @RequestMapping("/add")
    @NoLogin
    @ResponseBody
    public JsonResponse addVisitHistory(@RequestParam("ids") Long[] ids,   UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        if (ids != null && ids.length > 0) {

			Map<Long, Product> producMap = productService.getProductMap(Arrays
					.asList(ids));
			for (Long id : ids) {
				if (!producMap.containsKey(id)) {
					return jsonResponse
							.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
				}
			}
			if(userDetail != null && userDetail.getUserId() > 0){
				visitService.addVisitHistory(userDetail.getUserId(), ids);
				
			}else {
				visitService.addVisitHistory(0, ids);
				
			}
        }

        return jsonResponse.setSuccessful();
    }

    /**
     * 获取用户浏览历史列表
     * 
     * @param userDetail
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResponse getVisitHistorys(UserDetail userDetail, PageQuery pageQuery) {
        JsonResponse jsonResponse = new JsonResponse();

        List<UserVisitHistory> list = visitService.getVisitList(userDetail.getUserId(), pageQuery);

		List<Long> productIds=Lists.transform(list, new Function<UserVisitHistory, Long>() {

			@Override
			public Long apply(UserVisitHistory history) {
				return history.getRelatedId();
			}
		});
		
		Map<Long, Product> producMap=productService.getProductMap(productIds);
		List<UserVisitHistoryVO> voList = new ArrayList<UserVisitHistoryVO>();
		
		for (UserVisitHistory history : list) {
			if (!producMap.containsKey(history.getRelatedId())) {
				continue;
			}

			UserVisitHistoryVO vo = new UserVisitHistoryVO();
			vo.setId(history.getId());
			vo.setCreateTime(history.getCreateTime());
			vo.setProduct(producMap.get(history.getRelatedId()));
			voList.add(vo);
		}
        int count = visitService.getVisitListCount(userDetail.getUserId());
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);

        Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", voList);
        data.put("pageQuery", pageQueryResult);

        return jsonResponse.setSuccessful().setData(data);
    }

    @RequestMapping("/deleteall")
    @ResponseBody
    public JsonResponse clearVisitHistory(UserDetail userDetail) {
        JsonResponse jsonResponse = new JsonResponse();

        visitService.deleteVisitList(userDetail.getUserId());

        return jsonResponse.setSuccessful();
    }

}
