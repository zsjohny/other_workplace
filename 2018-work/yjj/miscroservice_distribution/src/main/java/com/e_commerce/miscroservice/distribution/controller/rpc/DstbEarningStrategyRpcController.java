package com.e_commerce.miscroservice.distribution.controller.rpc;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.distribution.service.DstbEarningsStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 13:43
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "dstb/rpc/earningStrategy" )
public class DstbEarningStrategyRpcController{

    @Autowired
    private DstbEarningsStrategyService dstbEarningsStrategyService;


    /**
     * 更新分销收益策略
     *
     * @param strategyList strategyList
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @RequestMapping("earningsRatio/upd")
    public String earningsRatioUpd(@RequestBody List<EarningsStrategyVo> strategyList){
        Response response = null;
        try {
            dstbEarningsStrategyService.update (strategyList);
            response = Response.success ();
        } catch (ErrorHelper e) {
            response = Response.errorMsg (e.getMsg ());
        }
        return JSONObject.toJSONString (response);
    }




    /**
     * 角色的所有分销收益策略
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @RequestMapping("earningsRatio/allStrategy")
    public String earningsRatioAllStrategy(){
        Response response;
        try {
            response = Response.success (dstbEarningsStrategyService.allStrategy ());
        } catch (ErrorHelper e) {
            e.printStackTrace ();
            response = Response.errorMsg (e.getMsg ());
        }
        return JSONObject.toJSONString (response);
    }

}
