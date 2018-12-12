package com.finace.miscroservice.activity.controller;


import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.activity.po.*;
import com.finace.miscroservice.activity.rpc.BorrowRpcService;
import com.finace.miscroservice.activity.rpc.UserRpcService;
import com.finace.miscroservice.activity.service.*;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.Borrow;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 活动主要接口:我的福利券,h5获取可用红包列表和红包数量, 邀请红包金额和个数,邀请列表, 获取用户地址, 新增修改收货地址, 删除收货地址,收货地址列表
 */
@RestController
public class AppActivityController extends BaseController {
    private Log logger = Log.getInstance(AppActivityController.class);

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Resource
    private BorrowRpcService borrowRpcService;

    @Autowired
    private UserRedPacketsService userRedPacketsService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ImagesTypeService imagesTypeService;

    @Autowired
    private UserJiangPinService userJiangPinService;


    /**
     * 获取福利券信息
     *
     * @param hbtype 第几页
     * @param page   福利券类型(例:1-红包2-加息券)
     *               {
     *               "msg": "",
     *               "code": 200,
     *               "data": [
     *               {
     *               "hbid": 74454, 福利券id
     *               "userid": 0,
     *               "hbleixingid": 12,
     *               "hbstartime": "2017-12-19 00:00:00", 福利券开始时间
     *               "hbendtime": "2018-06-19 00:00:00",   福利券结束时间
     *               "hbstatus": 0, 福利券状态 0--未使用2--已使用3--已过期
     *               "hbmoney": 20, 福利券金额
     *               "hbdetail": "投资2000块钱以上可以使用", 福利券使用明细
     *               "hbtype": 1, 福利券蕾西  1--红包2--加息券
     *               "hbname": "新手注册红包", 福利券名称
     *               "smoney": 2000, 福利券使用起投金额
     *               "sday": 0  福利券使用起投天数
     *               }
     *               ]
     *               }
     * @return
     */
    @RequestMapping("hongbao/auth")
    public Response hongbao(@RequestParam("hbtype") String hbtype,
                            @RequestParam("page") int page) {
        String userId = getUserId();

        logger.info("用户访问{}获取福利券信息", userId);
        if (page < 1) {
            logger.warn("用户访问{}获取福利券信息, 页数错误{}", userId, page);
            return Response.error("页数错误");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("hbtype", hbtype);
        map.put("hbstatus", "");
        return userRedPacketsService.hongbao(map, page);
    }


    /**
     * h5获取可用红包列表和红包数量
     *
     * @param borrowId 标的id
     * @param buyAmt   购买金额
     * @param type     类型（hblist--红包列表,hbcount--红包个数）
     * @param page     页码
     *                 <p>
     *                 {
     *                 "msg": "",
     *                 "code": 200,
     *                 "data": {
     *                 "totalSize": 1,
     *                 "hblist": [
     *                 {
     *                 "hbid": 74520, 红包id
     *                 "userid": 0,
     *                 "hbleixingid": 50, 红包类型id
     *                 "hbstartime": "2017-12-22 00:00:00",  红包可使用开始时间
     *                 "hbendtime": "2018-01-21 00:00:00", 红包过去时间
     *                 "hbstatus": 0, 红包状态 0-- 红包未使用  2-- 红包已过期 3-- 红包已使用
     *                 "hbmoney": 0.3, 红包金额
     *                 "hbdetail": "投资期限≥90天可用",  红包详情
     *                 "hbtype": 2, 红包类型 1--红包 2--加息券
     *                 "hbname": "国庆中秋双节活动活动加息卷",  福利券名称
     *                 "smoney": 0, 限制金额
     *                 "sday": 90 限制天数
     *                 }
     *                 ]
     *                 }
     *                 }
     * @return
     */
    @RequestMapping(value = "availableHb/auth")
    public Response availableHb(@RequestParam(value = "borrowId", required = false) int borrowId,
                                @RequestParam(value = "buyAmt", required = false) String buyAmt,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "page", required = false) int page) {

        String userId = getUserId();
        logger.info("用户访问{}可用红包列表和红包数量borrowId={},buyAmt={},type={}", userId, borrowId, buyAmt, type);

        if( StringUtils.isEmpty(buyAmt)){
            logger.warn("用户{}获取可用红包时，输入购买金额为空buyAmt={}", userId, buyAmt);
            return Response.errorMsg("请输入投资金额");
        }

        Borrow borrow = borrowRpcService.getBorrowById(borrowId);
        if( null == borrow ){
            logger.warn("用户{}获取可用红包时，标的不存在", userId, buyAmt);
            return Response.errorMsg("标的不存在，请选择其他标的");
        }

        Map<String, Object> params = new HashMap<>();
        if (borrow.getJx_flag() == null || borrow.getJx_flag() == 0) {
            params.put("ohb", "not"); //判断只能使用红包
        }

        if (borrow.getJxj_flag() == null || borrow.getJxj_flag() == 0) {
            params.put("nhb", "not"); //判断不能使用红包
        }

        if (!"0".equals(borrow.getUse())) {
            params.put("jxqbnsy", "jxqbnsy"); //判断新手注册的时候，送的加息卷，只能新手标使用
        }

        params.put("sday", borrow.getTimeLimitDay());
        params.put("userId", userId);
        params.put("smoney", buyAmt);
        params.put("hbstatus", "0");

        Map<String, Object> map = new HashMap<>();
        //返回红包列表
        if ("hblist".equals(type)) {
            logger.info("用户{}可用红包列表", userId);
            BasePage bpage = new BasePage();
            bpage.setPageNum(page);
            List<UserRedPacketsPO> list = this.userRedPacketsService.getHbByParam(params);
            map.put("hblist", list);
            map.put("totalSize", bpage.getTotal(list));
        } else if ("hbcount".equals(type)) {
            //返回福利劵数
            int hbcount = this.userRedPacketsService.getCountHbByParam(params);
            logger.info("用户{}可用红包数为{}", userId, hbcount);
            map.put("hbcount", hbcount);
        }

        return Response.successByMap(map);
    }


    /**
     * 投标判断可用红包
     *
     * @param borrowId 标的id
     *
     *                 *                 <p>
     *                 {
     *                 "msg": "",
     *                 "code": 200,
     *                 "data": {
     *                 "totalSize": 1,
     *                 "hblist": [
     *                 {
     *                 "hbid": 74520, 红包id
     *                 "userid": 0,
     *                 "hbleixingid": 50, 红包类型id
     *                 "hbstartime": "2017-12-22 00:00:00",  红包可使用开始时间
     *                 "hbendtime": "2018-01-21 00:00:00", 红包过去时间
     *                 "hbstatus": 0, 红包状态 0-- 红包未使用  2-- 红包已过期 3-- 红包已使用
     *                 "hbmoney": 0.3, 红包金额
     *                 "hbdetail": "投资期限≥90天可用",  红包详情
     *                 "hbtype": 2, 红包类型 1--红包 2--加息券
     *                 "hbname": "国庆中秋双节活动活动加息卷",  福利券名称
     *                 "smoney": 0, 限制金额
     *                 "sday": 90 限制天数
     *                 }
     *                 ]
     *                 }
     *                 }
     * @return
     */
    @RequestMapping(value = "getAvailableHb/auth")
    public Response getAvailableHb(@RequestParam(value = "borrowId", required = false) Integer borrowId) {

        String userId = getUserId();
        logger.info("用户访问{}可用红包列表和红包数量borrowId={}", userId, borrowId);
        Borrow borrow = borrowRpcService.getBorrowById(borrowId);
        if( null == borrow ){
            logger.warn("用户{}获取可用红包时，标的不存在", userId);
            return Response.errorMsg("标的不存在，请选择其他标的");
        }

        Map<String, Object> params = new HashMap<>();
        if (borrow.getJx_flag() == null || borrow.getJx_flag() == 0) {
            params.put("ohb", "not"); //判断只能使用红包
        }

        if (borrow.getJxj_flag() == null || borrow.getJxj_flag() == 0) {
            params.put("nhb", "not"); //判断不能使用红包
        }

        if (!"0".equals(borrow.getUse())) {
            params.put("jxqbnsy", "jxqbnsy"); //判断新手注册的时候，送的加息卷，只能新手标使用
        }

        params.put("sday", borrow.getTimeLimitDay());
        params.put("userId", userId);
        params.put("hbstatus", "0");

        Map<String, Object> map = new HashMap<>();
        logger.info("用户{}可用红包列表", userId);
//        BasePage.setPage(10);
        List<UserRedPacketsPO> list = this.userRedPacketsService.getHbByParam(params);

        map.put("hblist", list);
        map.put("rmoney", borrow.getRemmoney());//剩余可投金额

        return Response.successByMap(map);
    }

    /**
     * 邀请好友获取的红包个数和红包总金额
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "money": 0, //累计红包金额
     * "count": 0  //邀请人数
     * }
     * }
     */
    @RequestMapping("getInviterCountSumHb/auth")
    public Response getInviterCountSumHb() {

        String userId = getUserId();
        logger.info("用户{}邀请好友获取的红包个数和红包总金额", userId);

        Map<String, Object> map = userRedPacketsService.getInviterCountSumHb(Integer.valueOf(userId));
        return Response.successByMap(map);
    }

    /**
     * 邀请红包列表
     *
     * @param page 页码
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": [
     *             {
     *             "money": "0", 获得红包
     *             "phone": "158****0612", 被邀请人
     *             "content": "未投资" 进度
     *             }
     *             ]
     *             }
     * @return
     */
    @RequestMapping("getInviterList/auth")
    public Response getInviterList(@RequestParam("page") int page) {
        if (page < 1) {
            logger.warn("邀请红包列表,页面错误{}", page);
            return Response.error("页数错误");
        }
        String userId = getUserId();
        logger.info("用户={}邀请红包列表", userId);
        List<Map<String, Object>> list = userRedPacketsService.getInviterList(Integer.valueOf(userId), page);

        return Response.success(list);
    }


    /**
     * 新增修改收货地址
     *
     * @param name      收件人
     * @param phone     收件人电话
     * @param province  省
     * @param city      市
     * @param county    县
     * @param address   详细地址
     * @param status    状态  1--默认 0--其他地址
     * @param addressId 状态id
     * @return
     */
    @RequestMapping("saveAndUpdateAddress/auth")
    public Response saveAndUpdateAddress(@RequestParam("name") String name,
                                         @RequestParam("phone") String phone,
                                         @RequestParam("province") String province,
                                         @RequestParam("city") String city,
                                         @RequestParam("county") String county,
                                         @RequestParam("address") String address,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "addressId", required = false) String addressId) {

        String userId = getUserId();
        logger.info("用户{}开始新增收货地址", userId);

        if (StringUtils.hasEmpty(name, phone, province, city, county, address)) {
            logger.warn("收货地址参数name={}, phone={}, province={}, city={}, county={}, address={}", name, phone, province, city, county, address);
            return Response.errorMsg("参数错误");
        }

        return addressService.saveAndUpdateAddress(userId, name, phone, province, city, county, address, status, addressId);
    }


    /**
     * 获取用户地址列表
     *
     * @param page 第几页
     *             <p>
     *             <p>
     *             "data": {
     *             "toUserName": "你好", 收件人
     *             "adress": "西湖区", 县
     *             "adressDeatil": "新东方大厦701室",  详细地址
     *             "phone": "18652211223",  收件人电话
     *             "status": "1",  状态 1--默认地址 0--其他地址
     *             "shengfen": "浙江省", 省
     *             "city": "杭州市", 市
     *             "id": 地址id
     *             }
     * @return
     */
    @RequestMapping("userAddress/auth")
    public Response userAddress(@RequestParam(value = "page", required = false) Integer page) {
        String userId = getUserId();
        logger.info("用户{}获取默认收货地址", userId);
        if (page == null || page < 0) {
            logger.warn("用户{}获取默认收货地址列表，页码错误{}", userId, page);
        }

        return addressService.getAddressListByUser(userId, page);
    }


    /**
     * 删除用户地址
     *
     * @param addressId 地址id
     * @return
     */
    @RequestMapping("delAddress/auth")
    public Response delAddress(@RequestParam(value = "addressId", required = false) String addressId) {
        String userId = getUserId();
        logger.info("用户{}删除收货地址", userId);

        if (null == addressId) {
            return Response.errorMsg("删除的地址id为空");
        }

        return addressService.delAddressById(userId, addressId);
    }

    /**
     * 获取图片配置信息
     *
     * @param page 页码
     * @param type 类型(2--活动中心 4--运营报告)
     *             <p>
     *             {
     *             "id": 10,
     *             "name": "name",  名称
     *             "type": 2,  类型 1--app首页banner  2--活动中心  3--PC首页 4--运营报告 5--网贷课堂 6--新闻媒体
     *             "status": 0,
     *             "jumurl": "bgfnbgfnbgf", 跳转url
     *             "imgurl": "bngfngfngf", 图片url
     *             "scontent": "bngfngfngf", 分享内容
     *             "surl": "nbgfngfngf", 分享url
     *             "stitle": "ngfdngfngf", 分享标题
     *             "simgurl": "bgfbngfbgf", 分享图片url
     *             "torder": 0, 排序
     *             }
     * @return
     */
    @RequestMapping("imagesCenter")
    public Response imagesCenter(@RequestParam("page") int page,
                                 @RequestParam("type") Integer type) {
        logger.info("图片配置type={},1--app首页banner  2--活动中心  3--PC首页 4--运营报告 5--网贷课堂 6--新闻媒体", type);
        BasePage basePage = new BasePage();
        basePage.setPageNum(page);
        List<ImagesTypePO> list = imagesTypeService.getImagesType(type, page);
        return Response.successDataMsg(list, String.valueOf(basePage.getTotal(list)));
    }


    /**
     * 我获得的奖励列表
     * <p>
     * {
     * "invitations": 10,  邀请人数
     * "userJiangPinPOList": 20, 用户奖品红包数量
     * "jdCardMoneyAmt": 200, 京东卡金额
     * "list": [{
     * "addTime": "1524016340188", 添加时间
     * "id": 11,
     * "type": 0, 默认 0 未发放 1已发放
     * "code": 1,  奖品类型code "荐面奖",1"佣金奖",2"人脉奖",3
     * "jiangPinName": "奖品名称", 奖品名称
     * "money": "200",  出借金额
     * "phone": "138****5151", 手机号
     * "remark": "100",
     * "underUser": "0",
     * "userId": "1061"
     * }]
     * }
     *
     * @return
     */
    @RequestMapping("gifts/auth")
    public Response getGifts() {
        String userId = getUserId();
//        String userId = "60016";
        logger.info("ip={},userId={}我获得的奖励列表", Iptools.gainRealIp(request),userId);
        JSONObject userJiangPinPO = userJiangPinService.findUserAwards(Integer.parseInt(userId));
        return Response.success(userJiangPinPO);
    }

    /**
     * 我的好友列表
     *
     * @param page 页码
     *             <p>
     *             {
     *             "total": 1,
     *             "list": [{
     *             "phone": "138****3104",
     *             "time": "1452131261",
     *             "type": "已出借"
     *             <p>
     *             }]
     *             }
     * @return
     */
    @RequestMapping("friends/auth")
    public Response findMyFriends(Integer page) {
//        String userId = "60016";
        if (page==null||page<1){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String userId = getUserId();
        logger.info("ip={},userId={}我的好友列表", Iptools.gainRealIp(request),userId);

        JSONObject myFriendsPOS = userJiangPinService.findMyFriends(userId, page);

        return Response.success(myFriendsPOS);
    }

    /**
     * 邀请好友
     * <p>
     * {
     * "invitation": 2134  邀请好友
     * }
     *
     * @return
     */
    @RequestMapping("getInvitation/auth")
    public Response getInvitations() {
        String userId = getUserId();
        logger.info("ip={},userId={}邀请好友", Iptools.gainRealIp(request),userId);
        InvitationPO invitationPO = userJiangPinService.getInvitations(Integer.parseInt(userId));
        return Response.success(invitationPO);
    }

    /**
     * 获取全民 狂欢活动红包
     * @param code
     * @return
     */
    @RequestMapping("carnival/auth")
    public Response addCarnival(Integer code){
        String userId = getUserId();
//        String userId = "60109";
        logger.info("userId={} 获取全民狂欢活动红包 选择 code ={} 号红包" ,userId,code);
        return userRedPacketsService.addCarnival(code,userId);
    }

    /**
     * 获取全民 狂欢活动红包 已选取的红包
     * @return
     */
    @RequestMapping("find/carnival/auth")
    public Response getCarnival(){
        String userId = getUserId();
//        String userId = "60109";
        logger.info("userId={} 进入全民狂欢活动红包 " ,userId);
        String code = userRedPacketsService.getCarnival(userId);
        if (StringUtils.isNotEmpty(code))
        {
            logger.info("用户今日已领取红包code={}",code);
            return Response.success(code);
        }
        return Response.success(0);
    }
}










