package com.finace.miscroservice.borrow.controller;


import com.finace.miscroservice.borrow.service.EyeInfoService;
import com.finace.miscroservice.borrow.utils.EyeResponse;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.tools.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供给网贷天眼的查询接口
 */
@RestController
@RefreshScope
public class AppEyeSkyController extends BaseController {
    private Log logger = Log.getInstance(AppEyeSkyController.class);

//    private static final String EYE_PUBLIC_KEY = "xI9DfytR3BkEXOZpSgQ94IQj";

    @Autowired
    private EyeInfoService eyeInfoService;

    @Value("${borrow.eye.public.key}")
    private String eyePublicKey;  //天眼的key


    /**
     * 提供给网贷天眼的查询接口
     *
     * @return
     */
    @PostMapping(value = "/getEyeInfo")
    public EyeResponse getEyeInfo(@RequestParam(value = "org_code", required = false) String org_code,
                                  @RequestParam(value = "start_time", required = false) String start_time,
                                  @RequestParam(value = "end_time", required = false) String end_time,
                                  @RequestParam(value = "mobile", required = false) String mobile,
                                  @RequestParam(value = "order_id", required = false) String order_id,
                                  @RequestParam(value = "timestamp", required = false) String timestamp,
                                  @RequestParam(value = "signature", required = false) String signature){
        logger.info("天眼接口查询开始,org_code={},start_time={},end_time={},mobile={}," +
                " order_id={},timestamp={},signature={},",org_code,start_time,end_time,mobile,order_id,timestamp,signature);
        String sginData = "end_time="+ (end_time != null ? end_time : "");
        sginData += "&mobile="+ (mobile != null ? mobile : "");
        sginData += "&order_id="+ (order_id != null ? order_id : "");
        sginData += "&org_code="+ (org_code != null ? org_code : "");
        sginData += "&public_key="+eyePublicKey;
        sginData += "&start_time="+ (start_time != null ? start_time : "");
        sginData += "&timestamp="+ (timestamp != null ? timestamp : "");

        logger.info("天眼接口查询,平台签名数据={}",sginData);
        String sgin = MD5Util.getLowercaseMD5(sginData.toString());
        logger.info("天眼接口查询,平台签名={}",sgin);

        if(!sgin.equals(signature)){
           return EyeResponse.error(EyeResponse.CODE_4002);  //签名校验失败
        }

        return eyeInfoService.getEyeInfo(start_time, end_time, mobile, order_id);
    }








}
