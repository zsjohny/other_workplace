package com.e_commerce.miscroservice.publicaccount.controller;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomer;
import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerAudit;
import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.rpc.proxy.ProxyOrderRpcService;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.HttpUtils;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.publicaccount.entity.vo.MyProxyCustomerInfo;
import com.e_commerce.miscroservice.publicaccount.entity.vo.ProxyRefereeUserInfo;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyCustomerService;
import com.e_commerce.miscroservice.publicaccount.service.proxy.ProxyRefereeService;
import com.e_commerce.miscroservice.publicaccount.service.user.PublicAccountUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理商
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:25
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "public/proxyCustomer" )
public class ProxyCustomerController{

    @Autowired
    private ProxyCustomerService proxyCustomerService;

    @Autowired
    private ProxyRefereeService proxyRefereeService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PublicAccountUserService publicAccountUserService;
    @Autowired
    ProxyOrderRpcService proxyOrderRpcService;


    /**
     * 注册申请供应商
     *
     * @param refereeUserId 推荐人id
     * @param name          注册名
     * @param phone         手机号
     * @param authCode      手机验证码
     * @param province      省
     * @param city          市
     * @param county        区/县
     * @param addressDetail 详细地址
     * @param idCardNo      身份证号
     * @param type          1:市代理,2:县代理
     * @param openToken     public/yOpen/publicAccountUser/loginByWeiXin 获取
     * @param openId        public/yOpen/publicAccountUser/loginByWeiXin 获取
     * @return "data": {
     * "idCardNo": "asdfa",
     * "recommonUserId": 40,
     * "name": "直接注册县代理5",
     * "phone": "666-B21",
     * "type": 2,
     * "auditStatus": 0, //审核状态 0 成功 1 处理中 2失败
     * "province": "565",
     * "city": "6255",
     * "county": "1",
     * "addressDetail": "dongguan"
     * }
     * @author Charlie
     * @date 2018/9/20 14:04
     */
    @RequestMapping( "register" )
    public Response register(
            @RequestParam( value = "refereeUserId", required = false ) Long refereeUserId,
            @RequestParam( "name" ) String name,
            @RequestParam( "phone" ) String phone,
            @RequestParam( "authCode" ) String authCode,
            @RequestParam( "province" ) String province,
            @RequestParam( "city" ) String city,
            @RequestParam( "county" ) String county,
            @RequestParam( "addressDetail" ) String addressDetail,
            @RequestParam( "idCardNo" ) String idCardNo,
            @RequestParam( "type" ) Integer type,
            @RequestParam( "openToken" ) String openToken,
            @RequestParam( "openId" ) String openId,
            HttpServletResponse response
    ) {
        ProxyCustomerAudit param = new ProxyCustomerAudit ();
        param.setName (name);
        param.setPhone (phone);
        param.setProvince (province);
        param.setCity (city);
        param.setCounty (county);
        param.setAddressDetail (addressDetail);
        param.setIdCardNo (idCardNo);
        param.setType (type);
        param.setRefereeUserId (refereeUserId);
        try {

            Map<String, Object> result = new HashMap<> ();

            ProxyCustomerAudit audit = proxyCustomerService.doRegister (param, authCode, response, openToken, openId);
            //审核状态 0 成功 1 处理中 2失败
            result.put ("auditStatus", audit.getAuditStatus ());

            //用户信息
            PublicAccountUser user = publicAccountUserService.findByPhone (audit.getPhone ());
            result.put ("wxUserIcon", user.getWxUserIcon ());
            result.put ("wxName", user.getWxName ());

            //代理信息
            ProxyCustomer proxyCustomer = proxyCustomerService.selectByUserId (audit.getRecommonUserId ());
            //0:不是代理
            int proxyCustomerType = BeanKit.hasNull (proxyCustomer) ? 0 : proxyCustomer.getType ();
            //代理姓名
            String proxyCustomerName = BeanKit.hasNull (proxyCustomer) ? "" : proxyCustomer.getName ();
            result.put ("proxyCustomerType", proxyCustomerType);
            result.put ("proxyCustomerName", proxyCustomerName);

            return Response.success (result);
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 解绑代理的客户/下级代理
     *
     * @param refereeId 关系id
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/22 16:39
     */
    @RequestMapping( "unbind" )
    public Response unbind(@RequestParam( "refereeId" ) Long refereeId) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }
        Long userId = user.getId ();
        proxyRefereeService.unBindSafeById (refereeId, userId, userId);
        return Response.success ();
    }


    /**
     * 生成海报
     *
     * @param type 1邀请登录,2邀请注册代理商
     * @return "data": "http://charlie:8089/shareRegisterProxyQcImg/201809280020073073810000.jpg" //二维码地址
     * @author Charlie
     * @date 2018/9/27 18:46
     */
    @RequestMapping( "posterQrcode" )
    public Response posterQrcode(@RequestParam( "type" ) Integer type, HttpServletRequest request) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }
        Long userId = user.getId ();
        try {
            String result = proxyCustomerService.posterQrcode (type, userId, HttpUtils.webBaseUrl (request));
            return Response.success (result);
        } catch (ErrorHelper e) {
            return ResponseHelper.errorHandler (e);
        }
    }


    /**
     * 今日代理/客户数量
     *
     * @param type 1客户,2代理
     * @return Response
     * @author Charlie
     * @date 2018/9/26 7:10
     */
    @RequestMapping( "todayCreateCount" )
    public Response todayCreateCount(@RequestParam( "type" ) Integer type) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }
        Long userId = user.getId ();
        return Response.success (proxyRefereeService.todayCreateCount (type, userId));
    }


    /**
     * 用户所有的下级代理/客户数量
     *
     * @param type 1客户,2代理
     * @return long
     * @author Charlie
     * @date 2018/9/26 7:12
     */
    @RequestMapping( "allRefereeCount" )
    public Response allRefereeCount(@RequestParam( "type" ) Integer type) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }
        Long userId = user.getId ();
        return Response.success (proxyRefereeService.allRefereeCount (type, userId));
    }


    /**
     * 用户所有的下级代理/客户
     *
     * @param name       名称(模糊查询)
     * @param phone      电话号码
     * @param type       1.查询我的客户,2.查询我的县级代理
     * @param pageSize   分页
     * @param pageNumber 分页
     * @return todayCreateCount 今日新增
     * "list": [
     * {
     * "refereeId": 26, //关系id, 解绑关系时传递此值
     * "userId": 41, 用户名称
     * "wxName": "AAA", //微信昵称
     * "phone": "18842680139"
     * "userName": "真实名字" //真实名称
     * "createTimeReadable": "2018-09-29 18:47:45" //注册时间
     * }
     * ]
     * @author Charlie
     * @date 2018/9/27 17:24
     */
    @RequestMapping( "myProxyCustomer" )
    public Response myProxyCustomer(
            @RequestParam( value = "name", required = false ) String name,
            @RequestParam( value = "phone", required = false ) String phone,
            @RequestParam( value = "phoneOrName", required = false ) String phoneOrName,
            @RequestParam( value = "type" ) Integer type,
            @RequestParam( "pageSize" ) Integer pageSize,
            @RequestParam( "pageNumber" ) Integer pageNumber
    ) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }
        MyProxyQueryVo vo = new MyProxyQueryVo ();
        vo.setName (name);
        vo.setPhone (phone);
        vo.setPublicAccountUserId (user.getId ());
        vo.setType (type);
        vo.setPageSize (pageSize);
        vo.setPhoneOrName (phoneOrName);
        vo.setPageNumber (pageNumber);
        MyProxyCustomerInfo info = proxyRefereeService.myProxyCustomer (vo);
        //删除敏感信息
        if (! BeanKit.hasNull (info, info.getUserList ())) {
            PageInfo<ProxyRefereeUserInfo> userList = info.getUserList ();
            if (! ObjectUtils.isEmpty (userList.getList ())) {
                for (ProxyRefereeUserInfo userInfo : userList.getList ()) {
                    userInfo.setUserId (null);
                    userInfo.setIdCardNo (null);
                }
            }
        }
        return Response.success (info);
    }

    /**
     * 描述 1 市代理  0 客户 2 县代理
     *
     * @param userId
     * @return int
     * @author hyq
     * @date 2018/10/10 10:39
     */
    @RequestMapping( "judeCustomerRole" )
    public int judeCustomerRole(Long userId) {
        return proxyCustomerService.judeCustomerRole (userId);
    }

    /**
     * 描述
     *
     * @param userId
     * @return int -1, 没有申请中的代理, 0 成功 1 处理中 2失败'
     * @author hyq
     * @date 2018/10/11 11:46
     */
    public int getAuditStatus(long userId) {
        ProxyCustomerAudit auditStatus = proxyCustomerService.getAuditStatus (userId);
        if (auditStatus == null) {
            return -1;
        }
        else {
            return auditStatus.getAuditStatus ();
        }
    }

    /**
     * 描述 获取个人资料
     *
     * @param
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author hyq
     * @date 2018/10/9 16:24
     */
    @RequestMapping( "getCustomerInfo" )
    public Response getCustomerInfo() {

        PublicAccountUser user = publicAccountUserService.isLogin (request);
        if (user == null) {
            return ResponseHelper.noLogin ();
        }

        long userId = user.getId ();

        PublicAccountUser byUserId = publicAccountUserService.findByUserId (userId);

        ProxyCustomer proxyCustomer = proxyCustomerService.selectByUserId (user.getId ());

        Map<String, String> maps = new HashMap<> ();
        if (proxyCustomer != null) {
            //type 1 市代理  0 客户 2 县代理
            maps.put ("type", proxyCustomer.getType () + "");

            maps.put ("name", proxyCustomer.getName ());
            maps.put ("idCardNo", proxyCustomer.getIdCardNo ());
        }
        else {
            //type 0 客户
            maps.put ("type", "0");
            maps.put ("name", byUserId.getWxName ());
            maps.put ("phone", byUserId.getPhone ());

            maps.put ("status", getAuditStatus (userId) + "");
        }

        //1客户,2代理
        maps.put ("customerNum", proxyRefereeService.todayCreateCount (1, userId) + "");
        maps.put ("agentNum", proxyRefereeService.todayCreateCount (2, userId) + "");

        maps.put ("todayReward", proxyOrderRpcService.getTodayCollectReward (userId, 2));
        maps.put ("waitReward", proxyOrderRpcService.collectReward (userId, 0));

        return Response.success (maps);
    }


    /**
     * 找最近的一个审核未通过或者失败的申请
     *
     * @param  request 消息头:openToken
     *                      :openId
     *                  /public/yOpen/publicAccountUser/loginByWeiXin 获取
     * @return  "data": {
     *         "auditStatus": 1, //1 处理中 2失败( 当hasUnAuditProxy==1时判断 )
     *         "hasUnAuditProxy": 1,  //是否有申请 0没有,1有
     *         "auditMsg": "" //拒绝信息
     *         "audit": { //审核详情,json对象
     *
     *         }
     *     }
     * @author Charlie
     * @date 2018/10/22 19:27
     */
    @RequestMapping( "recentlyUnCheckOrFailedAudit" )
    public Response recentlyUnCheckOrFailedAudit(HttpServletRequest request) {
        PublicAccountUser user = publicAccountUserService.isLogin (request);
        return Response.success (proxyCustomerService.recentlyUnCheckOrFailedAudit (user));
    }
}
