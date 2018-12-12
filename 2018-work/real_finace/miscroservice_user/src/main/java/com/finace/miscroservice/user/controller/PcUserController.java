package com.finace.miscroservice.user.controller;

import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.handler.DbHandler;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.credit.DateUtil;
import com.finace.miscroservice.user.entity.response.MyBorrowInfoResponse;
import com.finace.miscroservice.user.rpc.BorrowRpcService;
import com.finace.miscroservice.user.service.OpenAccountService;
import com.finace.miscroservice.user.service.PcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * 用户模块主要接口
 */
@RestController
@RequestMapping("pc")
@RefreshScope
public class PcUserController extends BaseController {
    private Log logger = Log.getInstance(PcUserController.class);

    @Autowired
    private PcUserService pcUserService;

    @Resource
    private BorrowRpcService borrowRpcService;

    @Autowired
    private OpenAccountService openAccountService;

    /**
     * pc 资金记录
     *
     * @param startTime 查询开始时间
     * @param endTime   查询结束时间
     * @param page      页码
     *                  <p>
     *                  {"msg":"","code":200,"data":
     *                  {
     *                  "pageNum":1,
     *                  "pageSize":10,
     *                  "size":10,
     *                  "startRow":1,
     *                  "endRow":10,
     *                  "total":52,
     *                  "pages":6,
     *                  "list":
     *                  [
     *                  {
     *                  "id":0,
     *                  "total":0.0,
     *                  "money":1000.0,
     *                  "useMoney":0.0,
     *                  "noUseMoney":34324.0,
     *                  "collection":0.0,
     *                  "toUser":0,
     *                  "addtime":"1461820565",
     *                  "typeName":"投标",
     *                  "sunMoney":0.0,
     *                  "user_id":0
     *                  }
     *                  ],
     *                  "prePage":0,
     *                  "nextPage":2,
     *                  "isFirstPage":true,
     *                  "isLastPage":false,
     *                  "hasPreviousPage":false,
     *                  "hasNextPage":true,
     *                  "navigatePages":8,
     *                  "navigatepageNums":[1,2,3,4,5,6],
     *                  "navigateFirstPage":1,
     *                  "navigateLastPage":6,
     *                  "firstPage":1,
     *                  "lastPage":6
     *                  }
     *                  }
     * @return
     */
    @RequestMapping("account/log/auth")
    public Response pcAccountLog(@RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 Integer page) {
        //获取userId
        String userId = getUserId();
//        String userId = "60109";
        if (userId == null) {
            logger.warn("ID不存在={}", userId);
            return Response.errorMsg("ID不存在");
        }
        if (page == null || page < 1) {
            logger.warn("页码错误page={}", page);
            return Response.errorMsg("页码错误page=" + page);
        }

        return pcUserService.pcAccountLog(startTime, endTime, page, Integer.parseInt(userId));
    }

    /**
     * pc 我的资产
     * <p>
     * {"msg":"","code":200,"data":
     * {
     * "amountMoney":22000.0,
     * "amountWaitGetMoney":22000.0,
     * "frozenAmount":0.0,
     * "cumulativeInvestmentAmount":22000.0,
     * "waitPrincipal":0.0,
     * "waitProfit":0.0
     * }
     * }
     *
     * @return
     */
    @RequestMapping("my/property/auth")
    public Response pcMyProperty() {
        String userId = getUserId();
//        String userId = "60287";
        if (userId == null) {
            logger.warn("ID不存在={}", userId);
            return Response.errorMsg("ID不存在");
        }
        return pcUserService.pcMyProperty(Integer.valueOf(userId));

    }


    /**
     * 收款日历
     * <p>
     * "msg": "",
     * "code": 200,
     * "data": {
     * "principal": 0,
     * "endProfit": 0.00,
     * "nowProfit": 0,
     * "withdrawPrincipal": 0.00,
     * "withdrawProfit": 0
     * }
     * }
     *
     * @param month 月份
     * @return {
     */
    @RequestMapping("back/money/auth")
    public Response pcBackMoney(String month,String tday) {
        String userId = getUserId();
//        String userId = "10735";

        if (userId == null) {
            logger.warn("ID不存在={}", userId);
            return Response.errorMsg("ID不存在");
        }
        Map<String, Object> map = borrowRpcService.getReturnCalendar(userId, month, tday);
//        return pcUserService.pcBackMoney(Integer.valueOf(userId), month);
        return Response.success(map);
    }


    /**
     * 我的优惠
     *
     * @param type 优惠券类型 1 红包  2  加息券
     * @param page 页码
     *             <p>
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "pageNum": 1, //页码
     *             "pageSize": 10, //每页显示几条
     *             "size": 5, //数量
     *             "startRow": 1, //开始行数
     *             "endRow": 5, //结束行数
     *             "total": 5, //总条数
     *             "pages": 1,//总共几页
     *             "list": [{
     *             "hbmoney": 300.0, //红包金额
     *             "hbname": "新手福利红包",  //红包名称
     *             "hbdetail": "投资25000元90天及以上", //内容
     *             "hbendtime": "2018-04-13 23:59:59", //时间
     *             "hbtype": 1, //红包类型
     *             "hbstatus": 0 //红包状态
     *             }, {
     *             "hbmoney": 150.0,
     *             "hbname": "新手福利红包",
     *             "hbdetail": "投资15000元90天及以上",
     *             "hbendtime": "2018-05-13 23:59:59",
     *             "hbtype": 1,
     *             "hbstatus": 0
     *             }, {
     *             "hbmoney": 80.0,
     *             "hbname": "新手福利红包",
     *             "hbdetail": "投资10000元60天及以上",
     *             "hbendtime": "2018-04-13 23:59:59",
     *             "hbtype": 1,
     *             "hbstatus": 0
     *             }, {
     *             "hbmoney": 38.0,
     *             "hbname": "新手福利红包",
     *             "hbdetail": "投资4500元60天及以上",
     *             "hbendtime": "2018-04-13 23:59:59",
     *             "hbtype": 1,
     *             "hbstatus": 0
     *             }, {
     *             "hbmoney": 20.0,
     *             "hbname": "新手福利红包",
     *             "hbdetail": "投资2000元30天及以上",
     *             "hbendtime": "2018-03-29 23:59:59",
     *             "hbtype": 1,
     *             "hbstatus": 3
     *             }],
     *             "prePage": 0,
     *             "nextPage": 0,
     *             "isFirstPage": true,  //是否第一页
     *             "isLastPage": true,//是否 最后一页
     *             "hasPreviousPage": false,//是否有上一页
     *             "hasNextPage": false, //是否有下一页
     *             "navigatePages": 8,
     *             "navigatepageNums": [1],
     *             "navigateFirstPage": 1,
     *             "navigateLastPage": 1,
     *             "firstPage": 1, //第一页 是第几页
     *             "lastPage": 1 // 最后一页是第几页
     *             }
     *             }
     * @return
     */
    @RequestMapping("my/coupons/auth")
    public Response pcMyCoupons(Integer type, Integer page) {
        String userId = getUserId();
//        String userId = "60109";
        if (userId == null) {
            logger.warn("ID不存在={}", userId);
            return Response.errorMsg("ID不存在");
        }
        if (page == null || page < 1) {
            logger.warn("页码错误page={}", page);
            return Response.errorMsg("页码错误page=" + page);
        }
        if (null == type) {
            logger.warn("参数为空");
            return Response.errorMsg("参数为空");
        }

        return pcUserService.pcMyCoupons(type, page, Integer.parseInt(userId));
    }

    /**
     * 我的邀请
     * <p>
     * {"msg": "",
     * "code": 200,
     * "data": {
     * "invitationSize": 1,
     * "countMoney": 20.0
     * "shareId": "jad324651302631lkhikj"
     * }
     * }
     *
     * @return
     */

    @RequestMapping("my/invitation/auth")
    public Response myInvitation() {
        String userId = getUserId();
//        String userId = "60287";
//        String userId = "20460";
        return pcUserService.myInvitation(Integer.parseInt(userId));
    }

    /**
     * 邀请好友投资奖励记录
     *
     * @param page 页码
     * @return
     */
    @RequestMapping("rewards/record/auth")
    public Response myRewardsRecord(Integer page) {
        String userId = getUserId();
//        String userId = "60287";
        return pcUserService.myRewardsRecord(Integer.parseInt(userId), page);
    }

    /**
     * 我的信息
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "phone": "138****3104",
     * "realName": "真实姓名",
     * "idCard": "身份证",
     * "bankName": "中国建设银行",
     * "bankCard": "6236************097"
     * }
     * }
     *
     * @return
     */
    @RequestMapping("my/information/auth")
    public Response myInformation() {

        String userId = getUserId();
//        String userId = "60109";
        return pcUserService.myInformation(Integer.parseInt(userId));
    }

    /**
     * 我的出借
     *
     * @param page 页码
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "pageNum": 1,
     *             "pageSize": 10,
     *             "size": 10,
     *             "startRow": 1,
     *             "endRow": 10,
     *             "total": 12,
     *             "pages": 2,
     *             "list": [
     *             {
     *             "name": "30天标0011", 标的名称
     *             "gmtCreate": "2018-04-21 16:00:37", 投标时间
     *             "timeLimitDay": 30, 期限
     *             "buyAmt": 10000,  金额
     *             "apr": 10 利息
     *             },
     *             {
     *             "name": "app首页01",
     *             "gmtCreate": "2018-04-21 16:07:15",
     *             "timeLimitDay": 30,
     *             "buyAmt": 1000,
     *             "apr": 10
     *             },
     *             {
     *             "name": "30天标0011", 标的名称
     *             "gmtCreate": "2018-04-21 17:29:39",  投标时间
     *             "endProfit": "2018-05-22 00:00:00", 到期日
     *             "status": "在投", 状态
     *             "timeLimitDay": 30, 期限
     *             "buyAmt": 10000, 金额
     *             "apr": 10, 年化率
     *             "interest": 82.19 利息
     *             },
     *             {
     *             "name": "60天测试标003",
     *             "gmtCreate": "2018-04-21 17:50:36",
     *             "endProfit": "2018-06-21 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 60,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 164.38
     *             },
     *             {
     *             "name": "app首页01",
     *             "gmtCreate": "2018-04-21 17:54:57",
     *             "endProfit": "2018-05-22 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 30,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 82.19
     *             },
     *             {
     *             "name": "30天标0011",
     *             "gmtCreate": "2018-04-22 16:25:52",
     *             "endProfit": "2018-05-23 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 30,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 82.19
     *             },
     *             {
     *             "name": "60天测试标003",
     *             "gmtCreate": "2018-04-22 17:10:41",
     *             "endProfit": "2018-06-22 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 60,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 164.38
     *             },
     *             {
     *             "name": "10红包",
     *             "gmtCreate": "2018-04-22 17:12:53",
     *             "endProfit": "2018-05-23 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 30,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 82.19
     *             },
     *             {
     *             "name": "30天标0011",
     *             "gmtCreate": "2018-04-22 17:39:32",
     *             "endProfit": "2018-05-23 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 30,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 82.19
     *             },
     *             {
     *             "name": "app首页01",
     *             "gmtCreate": "2018-04-22 17:53:13",
     *             "endProfit": "2018-05-23 00:00:00",
     *             "status": "在投",
     *             "timeLimitDay": 30,
     *             "buyAmt": 10000,
     *             "apr": 10,
     *             "interest": 82.19
     *             }
     *             ],
     *             "prePage": 0,
     *             "nextPage": 2,
     *             "isFirstPage": true,
     *             "isLastPage": false,
     *             "hasPreviousPage": false,
     *             "hasNextPage": true,
     *             "navigatePages": 8,
     *             "navigatepageNums": [
     *             1,
     *             2
     *             ],
     *             "navigateFirstPage": 1,
     *             "navigateLastPage": 2,
     *             "firstPage": 1,
     *             "lastPage": 2
     *             }
     *             }
     * @return
     */
    @RequestMapping("my/financeBid/auth")
    public Response financeBid(Integer page){
        String userId = getUserId();
//        String userId = "60109";
        return pcUserService.myFinanceBid(page,userId);
    }

    /**
     * 注册--临时表
     * @param username 用户名
     * @param pass 密码
     * @return
     */
    @RequestMapping("register")
    public Response register(String username,String pass){
        return pcUserService.register(username,pass);
    }

    @RequestMapping("my/borrowInfo/auth")
    public Response myBorrowinfo(Integer page,@RequestParam(value = "type", required = false) Integer type){
        String userId = getUserId();
        logger.warn("参数userId="+userId+"page:"+page+"type:"+type);
        return pcUserService.myBorrowinfoById(userId,type,page);
    }
    @RequestMapping("my/borrowInfo")
    public Response myBorrowinfotest(String userId,Integer page,@RequestParam(value = "type", required = false) Integer type){

        logger.warn("参数userId="+userId+"page:"+page+"type:"+type);
        return pcUserService.myBorrowinfoById(userId,type,page);
    }

}

