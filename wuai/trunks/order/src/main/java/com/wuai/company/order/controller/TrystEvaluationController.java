package com.wuai.company.order.controller;

import static com.wuai.company.util.JwtToken.ID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.service.TrystEvaluationService;
import com.wuai.company.util.Response;

/**
 * 用户评价的controller
 */
@RestController
@RequestMapping("tryst")
public class TrystEvaluationController {
	
	@Autowired
    private TrystEvaluationService trystEvaluationService;
	
	//@GetMapping("evaluation/create")
	@PostMapping("evaluation/create")
    public Response seekOtherOrders(Integer ownUserId,Integer targetUserId,Integer type,String serviceAttitude,String serviceLabel,
			String similarity,String similarityLabel) {

        return trystEvaluationService.createEvaluation(ownUserId, targetUserId, type, serviceAttitude, serviceLabel, similarity, similarityLabel);
    }
	 
}
