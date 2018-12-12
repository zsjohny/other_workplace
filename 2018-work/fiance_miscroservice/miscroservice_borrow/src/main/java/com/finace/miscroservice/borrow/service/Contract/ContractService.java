package com.finace.miscroservice.borrow.service.Contract;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.commons.utils.tools.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成合同工具类
 */
@Service
@RefreshScope
public class ContractService {


    private Log logger = Log.getInstance(ContractService.class);
    public final static  String SUCCESS_CODE = "200";
    public final static  String EXIST_USER = "10207";
    public final static  String EXIST_COMPANY = "10216";
    public final static  String NOT_EXIST_USER = "10004";

    public final static  String PLATFORM_COMPANY = "etongjin88888";  //平台id

    @Value("${borrow.yun.contract.host}")
    private String host;  //云合同地址 生产地址--https://sdk.yunhetong.com  测试地址--https://sdklink.yunhetong.com

    @Value("${borrow.yun.contract.appid}")
    private String appid;  //appid

    @Value("${borrow.yun.contract.appkey}")
    private String appkey;  //应用密码 AppKey

    @Value("${borrow.yun.contract.templateid}")
    private String templateId;  //合同模板编号

    @Value("${borrow.yun.contract.locationname1}")
    private String locationname1;  //合同模板位置1  --用户

    @Value("${borrow.yun.contract.locationname2}")
    private String locationname2;  //合同模板位置2  --借款人

    @Value("${borrow.yun.contract.locationname3}")
    private String locationname3;  //合同模板位置3 --平台


    /**
     * 将用户信息导入应用中，为后续签署服务做准备。
     * @param sendMap
     * @return
     */
    public String addUser(Map<String, String> sendMap){
        sendMap.put("appId", appid);
        sendMap.put("password", appkey);
        sendMap.put("createSignature", "1"); //1为自动创建签名，0为不自动创建签名
        String reqMsg = HttpUtil.doPost(host+"/sdk/userInfo/addUser", sendMap, "UTF-8");

        logger.info("云合同，用户信息导入应用中,返回结果：reqMsg={}",reqMsg);
        JSONObject req = JSONObject.parseObject(reqMsg);
        return req.getString("subCode");
    }

    /**
     * 判断用户是否存在
     * @param userId
     * @return
     */
    public Boolean isExistUser(String userId){
        String token = getToken(userId);
        if( null != token ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     *获取用户登录凭证Token
     * @param userId
     * @return
     */
    public String getToken(String userId) {
        Map<String, String> sendMap = new HashMap<>();
        sendMap.put("appId", appid);
        sendMap.put("password", appkey);
        sendMap.put("appUserId", userId);
        String reqMsg = HttpUtil.doPost(host + "/sdk/token/getToken", sendMap, "UTF-8");

        logger.info("云合同，获取用户{}登录凭证Token,返回结果：reqMsg={}", userId, reqMsg);
        JSONObject req = JSONObject.parseObject(reqMsg);
        if (ContractService.SUCCESS_CODE.equals(req.getString("code"))) {
            JSONObject jsonObject = JSONObject.parseObject(req.getString("value"));
            return jsonObject.getString("token");
        }
        return null;
    }


    /**
     * 校验Token有效性
     * @param token
     * @return
     */
    public String checkToken(String token){
        String reqMsg = HttpUtil.doGet(host+"/sdk/token/checkToken?token="+token,"UTF-8");
        JSONObject req = JSONObject.parseObject(reqMsg);
        return req.getString("code");
    }

    /**
     * 根据模版生成合同
     * @param sendMap
     * @return
     */
    public String templateContract(Map<String, String> sendMap, String creatToken){
        sendMap.put("title", "借款协议");
        sendMap.put("templateId", templateId);
        sendMap.put("useCer", "1");
        String reqMsg = HttpUtil.doPost(host+"/sdk/contract/templateContract?token="+creatToken, sendMap, "UTF-8");

        logger.info("云合同，根据模版{}生成合同,合同编号={},返回结果：reqMsg={}", templateId,sendMap.get("defContractNo"), reqMsg);
        JSONObject req = JSONObject.parseObject(reqMsg);
        if(ContractService.SUCCESS_CODE.equals(req.getString("code"))){
            JSONObject jsonObject = JSONObject.parseObject(req.getString("value"));
            return jsonObject.getString("contractId");
        }
        return null;
    }

    /**
     * 上传文件生成合同
     * @param sendMap
     * @return
     */
    public String fileContract(Map<String, String> sendMap){
        String reqMsg = HttpUtil.doPost(host+"/sdk/contract/getToken", sendMap, "UTF-8");
        return reqMsg;
    }

    /**
     * 添加参与者
     * @param token
     * @param contractId  合同id
     * @param puserId 平台id
     * @param tuserId 出借人id
     * @param juserId 借款人id
     * @return
     */
    public Boolean addPartner(String token,String contractId,String puserId, String tuserId, String juserId){
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> pmap1 = new HashMap<>();
        pmap1.put("appUserId", puserId);
        pmap1.put("locationName", locationname3);
        list.add(pmap1);
        Map<String, String> pmap2 = new HashMap<>();
        pmap2.put("appUserId", tuserId);
        pmap2.put("locationName", locationname1);
        list.add(pmap2);
        /*Map<String, String> pmap3 = new HashMap<>();
        pmap3.put("appUserId", juserId);
        pmap3.put("locationName", locationname2);
        list.add(pmap3);*/
        map.put("contractId", contractId);
        map.put("partners", JSONObject.toJSONString(list));
        String reqMsg = HttpUtil.doPost(host+"/sdk/contract/addPartner?token="+token, map, "UTF-8");

        logger.info("添加合同参与者,返回结果：contractId={}, puserId={}, tuserId={}, juserId={}, reqMsg={}", contractId, puserId, tuserId, juserId, reqMsg);
        JSONObject req = JSONObject.parseObject(reqMsg);
        if(ContractService.SUCCESS_CODE.equals(req.getString("code"))){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 合同自动签署
     * @param token
     * @param contractId
     * @param puserId
     * @param tuserId
     * @param juserId
     * @return
     */
    public Boolean signContract(String token,String contractId,String puserId, String tuserId, String juserId){
        Map<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(puserId);
        list.add(tuserId);
        //list.add(juserId);
        map.put("contractId", contractId);
        map.put("signer", JSONObject.toJSONString(list));
        String reqMsg = HttpUtil.doPost(host+"/sdk/contract/signContract?token="+token, map, "UTF-8");

        logger.info("合同自动签署,返回结果：contractId={}, puserId={}, tuserId={}, juserId={}, reqMsg={}", contractId, puserId, tuserId, juserId, reqMsg);
        JSONObject req = JSONObject.parseObject(reqMsg);
        if(req != null && ContractService.SUCCESS_CODE.equals(req.getString("code"))){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 合同签署状态详情
     * @param sendMap
     * @return
     */
    public String detail(Map<String, String> sendMap){
        String reqMsg = HttpUtil.doPost(host+"/sdk/contract/detail", sendMap, "UTF-8");
        return reqMsg;
    }

    /**
     * 合同下载
     * @param token
     * @return
     */
    public InputStream download(String token,String contractId){
        Map<String, String> map = new HashMap<>();
        map.put("contractId", contractId);
        InputStream reqMsg = HttpUtil.doGetContract(host+"/sdk/contract/download?token="+token, map, "UTF-8");
        /*JSONObject req = JSONObject.parseObject(reqMsg);
        if(req.getString("subCode") != null ){
            logger.info("查詢合同,返回结果：contractId={}, reqMsg={}", contractId, reqMsg);
            return null;
        }*/

        return reqMsg;
    }


}
