package com.e_commerce.miscroservice.operate.controller.commonconfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.entity.distribution.EarningsStrategyVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.commons.utils.IOUtils;
import com.e_commerce.miscroservice.commons.utils.OssKit;
import com.e_commerce.miscroservice.operate.mapper.SqlMapper;
import com.e_commerce.miscroservice.operate.rpc.dstb.DstbEarningsRpcService;
import com.e_commerce.miscroservice.operate.service.system.DataDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 通用设置
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 11:47
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "/operate/commonConfig" )
public class CommonConfigController {

    private Log logger = Log.getInstance(CommonConfigController.class);

    @Autowired
    private OssKit ossKit;
    @Autowired
    private DstbEarningsRpcService dstbEarningsRpcService;
    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Value( "#{'${oss.upload.bucket.operateH5SaveImg}'.split(',')}" )
    private List<String> operateH5SaveImg;


    /**
     * 更新策略
     *
     * @param strategyList userRoleType : 1 店长 2分销商 3合伙人 selfCommissionEarningsRatio : 自分佣比例
     * fans1CommissionEarningsRatio : 一级粉丝分佣比例 fans2CommissionEarningsRatio : 二级粉丝分佣比例 commissionCurrencyRatio :
     * 分佣金币,RMB比例 managerEarningsRatio : 团队管理提成比例 managerCurrencyRatio : 团队管理提成 金币,RMB比例
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:38
     */
    @RequestMapping( "dstb/earningsRatio/upd" )
    public String dstbEarningsRatioAddOrUpd(String strategyList) {
        if (StringUtils.isBlank(strategyList)) {
            return "";
        }
        return dstbEarningsRpcService.earningsRatioUpd(JSONObject.parseObject(strategyList, new TypeReference<List<EarningsStrategyVo>>() {}));
    }


    /**
     * 角色的所有分销收益策略
     *
     * @return { "code": 200, "data": { "1": { //用户角色类型 0:无等级,1:店长,2:分销商,3:合伙人 "commissionCurrencyRatio": 0.20, //佣金
     * 金币,RMB收益比列 "fans1CommissionEarningsRatio": 0.16, //1级粉丝收益比例 "fans2CommissionEarningsRatio": 0.08, //2级粉丝收益比例
     * "selfCommissionEarningsRatio": 0.16, //自分佣金收益比列 "userRoleType": 1 //用户角色类型 0:无等级,1:店长,2:分销商,3:合伙人 }, "2": {
     * "commissionCurrencyRatio": 0.20, "fans1CommissionEarningsRatio": 0.16, "fans2CommissionEarningsRatio": 0.08,
     * "managerCurrencyRatio": 0.20, //团队管理金 金币,RMB收益比列 "managerEarningsRatio": 0.10, //团队管理金 收益比例
     * "selfCommissionEarningsRatio": 0.16, "userRoleType": 2 }, "3": { "commissionCurrencyRatio": 0.20,
     * "fans1CommissionEarningsRatio": 0.16, "fans2CommissionEarningsRatio": 0.08, "managerCurrencyRatio": 0.20,
     * "managerEarningsRatio": 0.06, "selfCommissionEarningsRatio": 0.16, "userRoleType": 3 } }, "msg": "" }
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @RequestMapping( "dstb/earningsRatio/allStrategy" )
    public String dstbEarningsRatioAllStrategy() {
        return dstbEarningsRpcService.dstbEarningsRatioAllStrategy();
    }


    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @param groupCode groupCode
     * @param code code
     * @param val 更新的值
     * @param name 更新的值
     * @param comment 更新的值
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/10/17 16:12
     */
    @RequestMapping( "dataDictionary/upd" )
    public Response updDataDictionaryByCodeAndGroupCode(
            @RequestParam( "groupCode" ) String groupCode,
            @RequestParam( "code" ) String code,
            @RequestParam( value = "val", required = false ) String val,
            @RequestParam( value = "name", required = false ) String name,
            @RequestParam( value = "comment", required = false ) String comment
    ) {
        DataDictionary dictionary = new DataDictionary();
        dictionary.setCode(code);
        dictionary.setGroupCode(groupCode);
        dictionary.setVal(val);
        dictionary.setName(name);
        dictionary.setComment(comment);
        try {
            dataDictionaryService.updDataDictionaryByCodeAndGroupCode(dictionary);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();

        }
        return Response.success();
    }


    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @param groupCode groupCode
     * @param code code 请求示例: 1.查询金币人民币兑换率(1块钱兑换多少个金币) { 'code': 'exchangeRate', 'groupCode':'goldCoin' } 2.查询连续签到奖励 {
     * 'code': 'signDateCoin', 'groupCode':'sign' } 3.查询分享商品收益设置 { 'code': 'productConfig', 'groupCode':'share' }
     * 999.查询...(以此类推)
     * @return "data": { "id": 69, "code": "exchangeRate", "groupCode": "goldCoin", "val": "10", "name":
     * "金币人民币兑换率(1块钱兑换多少个金币)", "status": 1 }
     * @author Charlie
     * @date 2018/10/17 16:12
     */
    @RequestMapping( "dataDictionary/find" )
    public Response findDictionaryByCodeAndGroupCode(
            @RequestParam( "groupCode" ) String groupCode,
            @RequestParam( "code" ) String code) {
        DataDictionary dictionary = new DataDictionary();
        dictionary.setCode(code);
        dictionary.setGroupCode(groupCode);
        try {
            return Response.success(dataDictionaryService.findDictionaryByCodeAndGroupCode(dictionary));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();

        }
    }


    /**
     * 上传文件(post请求)
     *
     * @param newFile 新上传的文件
     * @param path 上传的目录路径(前端可不传,默认icon)
     * @param newFileName 新上传文件名称(在OSS上的名称,保证唯一)
     * @param olderFileName 老的文件名称(需要删除的文件名,非必填)
     * @return 上传后的文件链接(url)
     * @author Charlie
     * @date 2018/11/5 17:55
     */
    @PostMapping( "uploadFile" )
    public Response uploadFile(@RequestPart( "newFile" ) @NotNull( message = "上传文件不能为空" ) MultipartFile newFile,
                               @RequestParam( value = "path", required = false ) String path,
                               String newFileName,
                               @RequestParam( value = "olderFileName", required = false ) String olderFileName,
                               HttpServletRequest request
    ) {
        logger.info("上传图片 newFileName={},olderFileName={},path={},size={},IP={}", newFileName, olderFileName, path, newFile.getSize(), HttpUtils.getIpAddress(request));
        //可变路径
        List<String> ossCfg = operateH5SaveImg;
        if (StringUtils.isNotBlank(path)) {
            ossCfg = new ArrayList<>(operateH5SaveImg);
            ossCfg.set(1, path);
        }
        InputStream is = null;
        try {
            //上传
            is = newFile.getInputStream();
            String result = ossKit.simpleUpload(is, ossCfg, newFileName);
            logger.info("上传图片成功");

            //删除 先不删
//            if (StringUtils.isNotBlank (olderFileName)) {
//                ossKit.remove (oss, olderFileName);
//            }
            return Response.success(result);
        } catch (IOException e) {
            //ignore
        } finally {
            IOUtils.close(is);
        }
        return Response.error();
    }


    /**
     * 上传文件(post请求)
     *
     * @param newFile       新上传的文件
     * @param path          上传的目录路径(前端可不传,默认icon)
     * @param newFileName   新上传文件名称(在OSS上的名称,保证唯一)
     * @param olderFileName 老的文件名称(需要删除的文件名,非必填)
     * @return 上传后的文件链接(url)
     * @author Charlie
     * @date 2018/11/5 17:55
     */
//    @PostMapping( "uploadImg" )
//    public Response uploadImg(@RequestPart( "refundPhoto" ) @NotNull( message = "上传文件不能为空" ) MultipartFile newFile,
//                               @RequestParam( value = "path", required = false) String path,
//                              @RequestParam( value = "newFileName",defaultValue = "Img") String newFileName,
//                               @RequestParam( value = "olderFileName", required = false ) String olderFileName,
//                               HttpServletRequest request
//    ) {
//
//        //UUID uuid = UUID.randomUUID();
//        //long timeMillis = System.currentTimeMillis();
//        //newFileName=uuid+newFileName;
//        logger.info ("上传图片 newFileName={},olderFileName={},path={},size={},IP={}", newFileName, olderFileName, path, newFile.getSize (), HttpUtils.getIpAddress (request));
//        //可变路径
//        List<String> ossCfg = refundImgs;
//        if (StringUtils.isNotBlank (path)) {
//            ossCfg = new ArrayList<> (refundImgs);
//            ossCfg.set (1, path);
//        }
//        String fileName = newFile.getOriginalFilename();
//        String[] split = StringUtils.split(fileName, ".");
//        int length = split.length;
//        String suffix = StringUtils.split(fileName, ".")[length-1];
//        suffix="."+suffix;
//        newFileName = String.valueOf(System.currentTimeMillis ()) + String.valueOf(System.currentTimeMillis()) +suffix ;
//        InputStream is = null;
//        try {
//            //上传
//            is = newFile.getInputStream ();
//            String result = ossKit.simpleUpload (is, ossCfg, newFileName);
//           // map.put("result",request);
//            logger.info ("上传图片成功");
//
//            //删除 先不删
////            if (StringUtils.isNotBlank (olderFileName)) {
////                ossKit.remove (oss, olderFileName);
////            }
//            return Response.success (result);
//        } catch (IOException e) {
//            //ignore
//        } finally {
//            IOUtils.close (is);
//        }
//        return Response.error ();
//    }


    /**
     * 分销角色升级条件
     *
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2018/11/6 10:33
     */
    @RequestMapping( "dstb/role/upgrade/find" )
    public Response dstbRoleUpgradeFind() {
        return Response.success(dataDictionaryService.dstbRoleUpGradeFind());
    }


    /**
     * 签到阶段奖励--查询
     *
     * @return "data": [ { "day": 5, "prize": 0.05, "condition": 5 }, { "day": 15, "prize": 0.08, "condition": 10 }, {
     * "day": 25, "prize": 0.1, "condition": 15 } ]
     * @author Charlie
     * @date 2018/11/14 10:18
     */
    @RequestMapping( "sign/specialDay/find" )
    public Response signSpecialDayFind() {
        DataDictionary dictionary = new DataDictionary();
        dictionary.setCode(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getCode());
        dictionary.setGroupCode(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getGroupCode());
        try {
            DataDictionary dict = dataDictionaryService.findDictionaryByCodeAndGroupCode(dictionary);
            //组装数据
            List<Map<String, Object>> result = new ArrayList<>(5);

            String config = dict.getComment();
            if (StringUtils.isBlank(config)) {
                logger.warn("没有任何配置");
                return Response.success(EmptyEnum.string);
            }
            JSONObject json = JSONObject.parseObject(config);
            JSONArray conditions = json.getJSONArray("condition");
            JSONArray days = json.getJSONArray("day");
            JSONArray prizes = json.getJSONArray("prize");
            for (int i = 0; i < conditions.size(); i++) {
                Map<String, Object> line = new HashMap<>(4);
                line.put("condition", conditions.get(i));
                line.put("day", days.get(i));
                line.put("prize", prizes.get(i));
                result.add(line);
            }

            return Response.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();

        }
    }


    /**
     * 签到阶段奖励--更新
     *
     * @param updInfo
     * @return 请求示例 json=[{"day":5,"prize":0.05,"condition":5},{"day":15,"prize":0.08,"condition":10},{"day":25,"prize":0.1,"condition":15}]
     * 返回实例 { "msg": "", "code": 200, "data": {} }
     * @author Charlie
     * @date 2018/11/14 10:18
     */
    @RequestMapping( "sign/specialDay/upd" )
    public Response signSpecialDayUpd(@RequestParam( value = "updInfo", required = false, defaultValue = "" ) String updInfo) {
        logger.info("更新阶段签到配置 json={}", updInfo);
        DataDictionary dictionary = new DataDictionary();
        dictionary.setComment(updInfo);
        dictionary.setGroupCode(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getGroupCode());
        dictionary.setCode(DataDictionaryEnums.SIGN_PERIODICAL_PRIZE.getCode());
        if (StringUtils.isNotBlank(updInfo)) {
            //数据组装
            JSONArray jsonArray = JSONObject.parseArray(updInfo);
            int size = jsonArray.size();
            List condition = new ArrayList<>(size);
            List day = new ArrayList<>(size);
            List prize = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                JSONObject elem = jsonArray.getJSONObject(i);
                condition.add(elem.get("condition"));
                day.add(elem.get("day"));
                prize.add(elem.get("prize"));
            }

            Map<String, Object> resMap = new HashMap<>(3);
            resMap.put("condition", condition);
            resMap.put("day", day);
            resMap.put("prize", prize);
            updInfo = JSONObject.toJSONString(resMap);
        }

        dictionary.setComment(updInfo);
        logger.info("更新 json={}", updInfo);
        dataDictionaryService.updDataDictionaryByCodeAndGroupCode(dictionary);
        return Response.success();
    }


    //=================================== 与业务无关,随时可删 ===================================
    @Autowired
    private SqlMapper sqlMapper;

    /**
     * 临时用来刷线上的sql
     *
     * @param
     * @param storeId
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/9 10:39
     */
    @Deprecated
    @RequestMapping( "doSql" )
    public Response doSql(@RequestParam( value = "storeId", required = false ) Long storeId,
@RequestParam( value = "code", required = false ) String gCode,
                          @RequestParam( value = "gCode", required = false ) String code

    ) {
//        int rec = sqlMapper.updShareShopQrImg(storeId);
        dataDictionaryService.test(gCode, code);
        dataDictionaryService.testUpd(gCode, code);
        return Response.success(1);
    }
    //=================================== 与业务无关,随时可删 ===================================


}
