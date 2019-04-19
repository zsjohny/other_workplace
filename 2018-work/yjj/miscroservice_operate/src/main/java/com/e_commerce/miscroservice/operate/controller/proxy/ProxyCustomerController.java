package com.e_commerce.miscroservice.operate.controller.proxy;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyCustomerQuery;
import com.e_commerce.miscroservice.commons.entity.proxy.MyProxyQueryVo;
import com.e_commerce.miscroservice.commons.entity.proxy.ProxyCustomerAuditQuery;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.operate.rpc.proxy.ProxyCustomerRpcService;
import com.e_commerce.miscroservice.operate.rpc.user.PublicAccountRpcService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 代理商
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 11:25
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/operate/proxy/customer")
public class ProxyCustomerController{

    @Autowired
    private ProxyCustomerRpcService proxyCustomerRpcService;
    @Autowired
    private PublicAccountRpcService publicAccountRpcService;


    /**
     * 获取登录状态用户的手机号
     *
     * @param auditId 审核id
     * @param msg 审核意见
     * @param isPass 1:通过,0:拒绝
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/20 14:04
     */
    @RequestMapping("audit")
    public String audit(Long auditId,
                          @RequestParam(value = "msg", defaultValue = "", required = false) String msg,
                          Integer isPass) {
//        DebugUtils.todo ("获取用户id");
        Long operUserId = 1L;
        return proxyCustomerRpcService.audit (auditId, msg, operUserId, isPass);
    }


    /**
     * 代理申请列表
     *
     * @param pageNumber pageNumber
     * @param pageSize pageSize
     * @param wxName 微信昵称
     * @param userName 用户姓名
     * @param province 省
     * @param city 市
     * @param county 县/区
     * @param idCardNo 身份证
     * @param createTimeBefore 提交时间查询
     * @param createTimeAfter 提交时间查询
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * {
     *             "id": 8, //代理id
     *             "idCardNo": "asdfa", //身份证
     *             "refereeUserId": 28,
     *             "recommonUserId": 31,
     *             "name": "直接注册县代理3", //姓名
     *             "phone": "666-B3", //电话
     *             "type": 1,
     *             "auditStatus": 1, //'审核状态 0 成功 1 处理中 2失败'
     *             "delStatus": 0, //'状态 0:正常,1:删除'
     *             "province": "565", //省
     *             "city": "629",//市
     *             "county": "15",//区县
     *             "addressDetail": "dongguan",//详细地址
     *             "updateUserId": 0,
     *             "createTime": 1538036643000,
     *             "updateTime": 1538036643000,
     *             "createTimeReadable": "2018-09-27 16:24:03"//提交时间
     *         }
     * @author Charlie
     * @date 2018/10/8 7:58
     */
    @RequestMapping("auditList")
    public Response auditList(
            @RequestParam( value = "pageNumber", required = false, defaultValue = "14" ) Integer pageNumber,
            @RequestParam( value = "pageSize", required = false, defaultValue = "0" ) Integer pageSize,
            @RequestParam(value = "wxName", required = false)String wxName,
            @RequestParam(value = "userName", required = false)String userName,
            @RequestParam(value = "province", required = false)String province,
            @RequestParam(value = "city", required = false)String city,
            @RequestParam(value = "county", required = false)String county,
            @RequestParam(value = "idCardNo", required = false)String idCardNo,
            @RequestParam(value = "createTimeBefore", required = false)String createTimeBefore,
            @RequestParam(value = "createTimeAfter", required = false)String createTimeAfter
    ) {
        ProxyCustomerAuditQuery query = new ProxyCustomerAuditQuery ();
        query.setWxName (wxName);
        query.setName (userName);
        query.setProvince (province);
        query.setCity (city);
        query.setCounty (county);
        query.setIdCardNo (idCardNo);
        query.setCreateTimeBefore (createTimeBefore);
        query.setCreateTimeAfter (createTimeAfter);
        query.setPageSize (pageSize);
        query.setPageNumber (pageNumber);
        List<ProxyCustomerAuditQuery> proxyCustomerAuditQueries = proxyCustomerRpcService.auditList(query);
        return Response.success (new PageInfo<>(proxyCustomerAuditQueries));
    }




    /**
     * 删除一条审核记录
     *
     * @param auditId 审核id
     * @author Charlie
     * @date 2018/9/25 10:57
     */
    @RequestMapping("delete")
    public String delete(Long auditId){
        //DebugUtils.todo ("获取当前用户id");
        Long operUserId = 1L;
        return proxyCustomerRpcService.delete (auditId, operUserId);
    }

    /**
     * 用户所有的下级代理/客户数量
     *
     * @param name 名称(模糊查询)
     * @param phone 电话号码
     * @param publicAccountUserId 用户id
     * @param type 1.查询我的客户,2.查询我的县级代理
     * @param pageSize 分页
     * @param pageNumber 分页
     * @return
     *  todayCreateCount 今日新增
     *  "list": [
     *                 {
     *                     "refereeId": 26, //关系id, 解绑关系时传递此值
     *                     "userId": 41, 用户名称
     *                     "wxName": "AAA", //微信昵称
     *                     "phone": "18842680139"
     *                     "userName": "真实名字" //真实名称
     *                     "createTimeReadable": "2018-09-29 18:47:45" //注册时间
     *                 }
     *           ]
     * @author Charlie
     * @date 2018/9/27 17:24
     */
    @RequestMapping( "myProxyCustomer" )
    public String myProxyCustomer(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            Long publicAccountUserId,
            Integer type,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber
    ) {
        MyProxyQueryVo vo = new MyProxyQueryVo ();
        vo.setName (name);
        vo.setPhone (phone);
        vo.setPublicAccountUserId (publicAccountUserId);
        vo.setType (type);
        vo.setPageSize (pageSize);
        vo.setPageNumber (pageNumber);
        return proxyCustomerRpcService.myProxyCustomer (vo);
    }



    /**
     * 解绑代理的客户/下级代理
     *
     * @param refereeId   关系id
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author Charlie
     * @date 2018/9/22 16:39
     */
    @RequestMapping( "unbind" )
    public Response unbind(Long refereeId) {
//        DebugUtils.todo ("获取用户id");
        Long operUserId = 1L;
        proxyCustomerRpcService.unBindById (refereeId, operUserId);
        return Response.success ();
    }

    /**
     * 描述 代理商管理
     * @param query
     * @author hyq
     * @date 2018/10/15 15:55
     * @return java.lang.String
     */
    @RequestMapping("customerList")
    public String customerList(ProxyCustomerQuery query,Integer pageSize, Integer pageNum){
        query.setPageSize(pageSize);
        query.setPageNum(pageNum);
        return proxyCustomerRpcService.customerList(query);
    }

    /**
     * 描述 禁用代理
     * @param userId
     * @author hyq
     * @date 2018/10/17 11:20
     * @return int
     */
    @RequestMapping( "stopCustomer" )
    public String stopCustomer(long userId,int type){
        return publicAccountRpcService.stopCustomer(userId,type);
    }

}
