package com.finace.miscroservice.borrow.controller;


import com.finace.miscroservice.borrow.po.BorrowPO;
import com.finace.miscroservice.borrow.po.FinanceBidPO;
import com.finace.miscroservice.borrow.rpc.ActivityRpcService;
import com.finace.miscroservice.borrow.rpc.OfficialWebsiteRpcService;
import com.finace.miscroservice.borrow.rpc.UserRpcService;
import com.finace.miscroservice.borrow.service.BorrowService;
import com.finace.miscroservice.borrow.service.Contract.ContractService;
import com.finace.miscroservice.borrow.service.FinanceBidService;
import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.entity.ChannelBanner;
import com.finace.miscroservice.commons.entity.InvestRecords;
import com.finace.miscroservice.commons.entity.User;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import com.finace.miscroservice.commons.utils.tools.TextUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 理财模块主要接口:投资记录获取合同信息, 投资记录, 获取理财列表, 标的详情, h5获取项目详情, h5获取标的投资记录, app首页,
 */
@RestController
public class AppBorrowController extends BaseController {

    private Log logger = Log.getInstance(AppBorrowController.class);
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private FinanceBidService financeBidService;

    @Autowired
    private UserRpcService userRpcService;

    @Autowired
    private ActivityRpcService activityRpcService;

    @Autowired
    private ContractService contractService;

    @Value("${borrow.evaluation}")
    private String evaluation;  //标的是否需要测评 0--不需要 1--需要


    /**
     * app首页
     *
     * @param channel 渠道
     * @param isxs    0--不是新手1--是新手
     *                <p>
     *                "data": {
     *                "bmap": {
     *                "cp3": { 标的3
     *                "mostAccount": "0", 最大投资金额 0表示无限额
     *                "apr": 12, 利率
     *                "lowestAccount": "100", 最小投资金额
     *                "accountYes": "339999.00", 已购买金额
     *                "use": "1", 标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *                "scales": "0.34000", 已购买进度
     *                "name": "3月标", 标的名称
     *                "timeLimitDay": 90, 项目期限
     *                "account": "1000000", 项目总金额
     *                "addApr":0.2  加息部分金额
     *                "litpic":0.2  标记
     *                "status":0.2  状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *                "remmoney":0.2  项目剩余金额
     *                },
     *                "cp2": { 标的2
     *                "mostAccount": "0", 最大投资金额 0表示无限额
     *                "apr": 12, 利率
     *                "lowestAccount": "100", 最小投资金额
     *                "accountYes": "339999.00", 已购买金额
     *                "use": "1", 标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *                "scales": "0.34000", 已购买进度
     *                "name": "3月标", 标的名称
     *                "timeLimitDay": 90, 项目期限
     *                "account": "1000000", 项目总金额
     *                "addApr":0.2  加息部分金额
     *                "litpic":0.2  标记
     *                "status":0.2  状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *                "remmoney":0.2  项目剩余金额
     *                },
     *                "cp1": { 标的1
     *                "mostAccount": "0", 最大投资金额 0表示无限额
     *                "apr": 12, 利率
     *                "lowestAccount": "100", 最小投资金额
     *                "accountYes": "339999.00", 已购买金额
     *                "use": "1", 标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *                "scales": "0.34000", 已购买进度
     *                "name": "3月标", 标的名称
     *                "timeLimitDay": 90, 项目期限
     *                "account": "1000000", 项目总金额
     *                "addApr":0.2  加息部分金额
     *                "litpic":0.2  标记
     *                "status":0.2  状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *                "remmoney":0.2  项目剩余金额
     *                },
     *                "xsb": {  新手标
     *                "mostAccount": "0", 最大投资金额 0表示无限额
     *                "apr": 12, 利率
     *                "lowestAccount": "100", 最小投资金额
     *                "accountYes": "339999.00", 已购买金额
     *                "use": "1", 标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *                "scales": "0.34000", 已购买进度
     *                "name": "3月标", 标的名称
     *                "timeLimitDay": 90, 项目期限
     *                "account": "1000000", 项目总金额
     *                "addApr":0.2  加息部分金额
     *                "litpic":0.2  标记
     *                "status":0.2  状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *                "remmoney":0.2  项目剩余金额
     *                }
     *                },
     *                "bannerlist": [
     *                {
     *                "id": 0,
     *                "imgurl": "http://118.31.129.39/ytj/data/banner/2017122610362126c1.png",  图片路径
     *                "url": "http://112.17.92.53:8001/ytj/app/activity/huawei.html",  图片点击跳转链接
     *                "corder": "1"  排序
     *                "scontent": "分享内容",   分享内容
     *                "stitle": "分享标题", 分享标题
     *                "simgurl": "http://112.17.92.53:8001/ytj/data/banner/20180205160212e679.png"  分享图片
     *                },
     *                ],
     *                "indexMsg": [
     *                {
     *                "topic": "消息通知",  图片路径
     *                "msg": "http://112.17.92.53:8001/ytj/app/activity/huawei.html",  图片点击跳转链接
     *                },
     *                ]
     *
     *
     *                }
     * @return
     */
    @RequestMapping("appindex")
    public Response appindex(@RequestParam("channel") String channel,
                             @RequestParam(value = "isxs", required = false) Integer isxs) {
        Map<String, Object> map = new HashMap<>();
        logger.info("{}渠道app获取首页信息", channel);
        //首页banner图片
        List<ChannelBanner> bannerlist = this.activityRpcService.getChannelBanner(channel);
        map.put("bannerlist", bannerlist);

        Map<String, Map<String, Object>> bmap = borrowService.getBorrowByIndex(isxs);
        map.put("bmap", bmap);

        //首页消息通知
        map.put("indexMsg", userRpcService.getAppIndexMsg());

        return Response.successByMap(map);
    }

    /**
     * 投资记录
     *
     * @param page 第几页
     *             <p>
     *             {
     *             "borrowName": null,  标名称
     *             "borrowId": null,  标id
     *             "addtime": "1513068872",  投资时间
     *             "tender_time": null,
     *             "money": "5020.00",  投资金额
     *             "ocinvestment": "5000.00",  投资本金
     *             "interest": "49.31",  预期收益
     *             "dqr": "2018-01-12",  到期时间
     *             "repaymentYesinterest": "49.31",
     *             "account": "20000",  标的金额
     *             "status": "counting",
     *             "release_type": null,
     *             "id": "233",
     *             "apr": "12.00"  利率
     *             "hbmoney": null, 红包金额
     *             "hbtype": null, 红包类型 1-红包2-加息卷
     *             "limitDay": "20", 项目期限
     *             "qxr": "2017-12-21" 起息日
     *             "repaymentType": 到期本息还款  还款类型
     *             }
     * @return
     */
    @RequestMapping("inverstRecords/auth")
    public Response inverstRecords(@RequestParam("page") int page) {
        String userId = getUserId();
        logger.info("用户{}访问投资记录", userId);
        if (page < 1) {
            logger.warn("用户{}访问投资记录,页码错误{}", page);
            return Response.error();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", "1");
        map.put("stype", "1");
        List<InvestRecords> investRecords = financeBidService.getUserInvestRecords(map, page);

        return Response.success(investRecords);
    }


    /**
     * 投资记录获取合同信息
     *
     * @param fbid 投资记录id
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "buyAmt": 100,   购买金额
     *             "tusername": "13300000001", 投资人用户名
     *             "apr": 12,  利率
     *             "dqr": "2018-01-10",  到期日
     *             "contract": "20171220282", 合同编号
     *             "qxr": "2017-12-21",  起息日
     *             "limitDay": 20,  项目期限
     *             "jrealname": null, 借款人姓名
     *             "trealname": "楼下",  投资人姓名
     *             "loanUage": " S",  借款用途
     *             "interest": 0.65,  收益
     *             "tcard": "512501197506045175",  投资人身份证
     *             "tphone": "13300000001", 投资人电话
     *             "jphone": "13100000001", 借款人电话
     *             "account": "500000", 借款总金额
     *             "jusername": "13100000001"  借款人用户名
     *             }
     *             }
     * @return
     */
    @RequestMapping("showHt/auth")
    public Response showHt(@RequestParam("fbid") Integer fbid) {

        if (fbid==null){
            logger.info("投资记录id 参数为空");
        }
        return borrowService.showHt(fbid);
    }


    /**
     * 投资购买界面查看合同
     *
     * @param borrowId 标的id
     * @param buyAmt   购买金额(传值后合同里面显示购买金额和预收利息,不传不显示)
     *                 <p>
     *                 {
     *                 "msg": "",
     *                 "code": 200,
     *                 "data": {
     *                 "buyAmt": "",  购买金额
     *                 "tusername": "15100000001", 投资人用户名
     *                 "apr": 10, 利率
     *                 "dqr": "2018-01-13",  到期日
     *                 "contract": "",  合同编码
     *                 "qxr": "2017-12-29",  起息日
     *                 "limitDay": 15,  项目天数
     *                 "jrealname": null,  借款人真实姓名
     *                 "loanUage": "", 借款用途
     *                 "trealname": "",  投资人真实姓名
     *                 "interest": "",  收益
     *                 "tcard": "", 投资人身份证号
     *                 "tphone": "15100000001",  投资人手机号码
     *                 "jphone": null,  借款人手机号码
     *                 "account": "10000",  借款金额
     *                 "jusername": null  借款人用户名
     *                 }
     *                 }
     * @return
     */
    @RequestMapping("showtzHt/auth")
    public Response showtzHt(@RequestParam("borrowId") int borrowId,
                             @RequestParam(value = "buyAmt", required = false) String buyAmt) {
        String userId = getUserId();
        return borrowService.showTzHt(borrowId,buyAmt,userId);

    }


    /**
     * 理财列表
     *
     * @param page 名称
     * @param isxs 0--不是新手1--是新手
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": [
     *             {
     *             "id": 0,  标的id
     *             "siteId": 0,
     *             "userId": 0,
     *             "name": "3月标",   标的名称
     *             "status": 0,   状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *             "order": 0,
     *             "hits": 0,
     *             "isVouch": 0,
     *             "viewType": 0,
     *             "vouchTimes": 0,
     *             "repaymentUser": 0,
     *             "repaymentYesinterest": 0,
     *             "use": "1",  标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *             "account": "500000",   项目金额
     *             "accountYes": "52000.00",  已购买金额
     *             "apr": 12,  利率
     *             "addApr": 0,
     *             "award": 0,
     *             "partAccount": 0,
     *             "funds": 0,
     *             "isMb": 0,
     *             "isFast": 0,
     *             "isJin": 0,
     *             "isXin": 0,
     *             "isday": 0,
     *             "timeLimitDay": 90,  标的天数
     *             "isArt": 0,
     *             "isCharity": 0,
     *             "isProject": 0,
     *             "isFlow": 0,
     *             "flowStatus": 0,
     *             "flowMoney": 0,
     *             "flowCount": 0,
     *             "flowYescount": 0,
     *             "isStudent": 0,
     *             "isOffvouch": 0,
     *             "hbFlag": 0,
     *             "scales": "0.10400",  购买进度
     *             "borrowTotal": 0,
     *             "borrowInterest": 0,
     *             "borrowType": 0,
     *             "count1": 0,
     *             "count2": 0,
     *             "count3": 0,
     *             "remmoney": 0, 项目剩余金额
     *             "borrowTypes": 0,
     *             "hongbaoTrans": 0,
     *             "releaseType": 0
     *             "litpic":60天标
     *             "lowestAccount": "100", 最小投资金额
     *             "mostAccount": "0", 最大投资金额 0表示无限额/br>
     *             }
     *             ]
     *             }
     * @return
     */
    @RequestMapping("financelist")
    public Response financelist(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "isxs", required = false) Integer isxs) {
        logger.info("理财列表刷新");

        if (page == null || page < 1) {
            logger.warn("理财列表,页面错误{}", page);
            return Response.error("页数错误");
        }
        return borrowService.financeList(page,isxs);
    }

    /**
     * 获取标的详情
     *
     * @param id 标的id
     *           <p>
     *           {
     *           "msg": "",
     *           "code": 200,
     *           "data": {
     *           "mostAccount": "0", 最大投资金额 0表示无限额
     *           "apr": 12, 利率
     *           "lowestAccount": "100", 最小投资金额
     *           "accountYes": "339999.00", 已购买金额
     *           "use": "1", 标的类型 0新手标，1应收账款，2供应链金融；3优质资产转让；4票据业务；5物业宝；6学费宝
     *           "scales": "0.34000", 已购买进度
     *           "name": "3月标", 标的名称
     *           "timeLimitDay": 90, 项目期限
     *           "account": "1000000", 项目总金额
     *           "addApr":0.2  加息部分金额
     *           "litpic":0.2  标记
     *           "status":0.2  状态0：等待初审，1：初审通过，2：初审失败，3：复审通过，4：复审失败，5：撤标，6：还款中，7：等待还款，9:标的冻结（不能购买）  8：已还款,49满标审核未通过，59项目撤回;5的状态页面刷新后，会修改成59;9手动冻结（票务宝未满标复审）
     *           "remmoney":0.2  项目剩余金额
     *           "trustLevel":  标的安全等级  1-保守型2-谨慎型3-稳健型4-积极型5-激进型
     *           "validTime":  募集期限(天)
     *           "isEval":  是否需要风险测评 0--不需要 1--需要
     *           "riskGrade": 风险等级  0低风险 1-中低风险 2中等风险 3中高风险 4高风险
     *           "interestDay":T(成交日)+1  起息方式
     *           "refundTyep":到期本息还款   还款方式
     *           "releaseType":20  20--富有 30--存管
     *           }
     *           }
     * @return
     */
    @RequestMapping("getBorrow")
    public Response getBorrow(@RequestParam("id") Integer id) {
        return borrowService.getBorrow(id);
    }


    /**
     * h5获取标的项目详情
     *
     * @param id 标的id
     *           <p>
     *           {
     *           "msg": "",
     *           "code": 200,
     *           "data": {
     *           "img": [  图片数组
     *           "http://118.31.129.39/ytj/data/borrow/201712251325447fb9.png",
     *           "http://118.31.129.39/ytj/data/borrow/20171225132545c63c.png",
     *           "http://118.31.129.39/ytj/data/borrow/20171225132547517c.png",
     *           "http://118.31.129.39/ytj/data/borrow/20171225132549587b.png",
     *           "http://118.31.129.39/ytj/data/borrow/20171225132551ebe7.jpg",
     *           "http://118.31.129.39/ytj/data/borrow/201712251325535b73.jpg",
     *           "http://118.31.129.39/ytj/data/borrow/201712251325578374.png",
     *           "http://118.31.129.39/ytj/data/borrow/201712251326003031.jpg"
     *           ],
     *           "noimgContent2": "有",
     *           "imagestrlist2": [],
     *           "financeCompany": "刚回家看看",   融资企业
     *           "loanUsage": "购房贷款计划",  借款额度
     *           "ficAccount": "500000",   借款额度
     *           "timeLimitDay": 90,   借款期限
     *           "payment": "航空港已经",  还款来源
     *           "imagestrlist": [],
     *           "use": "1",   标的类型
     *           "noimgContent": "温馨提醒：使用最新手机APP可查看更多详情！",
     *           "borrowFlag": "newBorrow"  是否是老的标的 newBorrow--新标的  oldBorrow--老的标的
     *           "trustLevel":  标的安全等级  1-保守型2-谨慎型3-稳健型4-积极型5-激进型
     *           "validTime":  募集期限(天)
     *           "privacy":  法人
     *           "address":  地址
     *           "realname":  公司名称
     *           "uptime":  成立时间
     *           }
     *           }
     * @return
     */
    @RequestMapping("getBorrowDetail")
    public Response getBorrowDetail(@RequestParam(value = "id", required = false) Integer id) {
        logger.info("访问标的{}详情", id);
        if( null == id ){
            logger.warn("访问标的详情id={}", id);
            return Response.errorMsg("标的id不能为空");
        }

        return borrowService.getBorrowDetail(id);
    }


    /**
     * h5获取标的投资记录
     *
     * @param id   标的id
     * @param page 第几页
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data":{
     *             "totalSize": 21, 总页数
     *             "finance": [
     *             {
     *             "addtime": "2017-12-22 11:11:35", 投资时间
     *             "borrowId": 10633, 标的id
     *             "account": 30000,  投资金额
     *             "username": "151****0001" 投资用户
     *             }
     *             ]
     *             }
     * @return
     */
    @RequestMapping("getBorrowInvestmentRecords")
    public Response getBorrowInvestmentRecords(@RequestParam("id") int id, @RequestParam("page") int page) {

        logger.info("获取标的投资投资记录第{}页", page);
        if (page < 1) {
            logger.warn("获取标的投资投资记录， 页数错误{}", page);
            return Response.errorMsg("页数错误");
        }

        return Response.success(financeBidService.getInvestmentRecordByBorrowId(id, page));
    }


    /**
     * 获取累计成交信息
     * <p>
     * "data": {
     * "cumulativeMoney": 2175296,  累计成交额
     * "cumulativeCount": 220 累计借款笔数
     * }
     *
     * @return
     */
    @RequestMapping("getCumulativeData")
    public Response getCumulativeData() {
        return Response.success(financeBidService.getCumulativeData());
    }
    /**
     * 数据披露
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "ljcjje": 2834746.0, //累计成交金额
     * "ljyhsy": 49803.1, //累计用户收益(元)
     * "ljzcyh": 653, //累计注册用户(人)
     * "ljcjbs": 389, //累计成交笔数(笔)
     * "ljcjrzs": 122, //累计出借人总数(人)
     * "bncjje": 1357751.0, //本年度成交金额(元)
     * "zddhcjyezb": 191611.0, //最大单户出借余额占比
     * "qydhcjyezb": -191610.0, //其余单户出借余额占比
     * "zdshtzcjzb": 743578.0, //最大10户投资出借占比
     * "qyyhcjyezb": -743577.0,//其余用户出借余额占比
     * "xbnan": 0.2462, //性别男 占比
     * "xbnv": 0.7538, //性别女 占比
     * "dhjebs": 293,//待还金额笔数(笔)
     * "ljjkrzs": 4, //累计借款人总数(人)
     * "dhje": 1240201.0, //待还金额(万元)
     * "xmyql": 0.0, //项目逾期率(OP)
     * "yqje": 0.0, //逾期金额(万元)
     * "jeyql": 0.0, //金额逾期率(OP)
     * "jstysyql": 0.0, //90天以上逾期率(OP)
     * "jstysyqje": 0.0, //90天以上逾期金额(万元)
     * "jstysljyql": 0.0 //90天以上累计逾期率(OP)
     * "dqjkrsl": 0.0 //当前借款人数量
     * "dqcjrsl": 0.0 //当前出借人数量
     * }
     * }
     *
     * @return
     */
    @RequestMapping("getDataCollection")
    public Response getDataCollection() {
        return borrowService.getDataCollection();
    }



    /**
     * 获取云合同的token
     *
     * @return
     */
    @RequestMapping("getYunContractToken/auth")
    public Response getYunContractToken() {
        String userId = getUserId();
        if (null == userId) {
            userId = ContractService.PLATFORM_COMPANY;
        }
        return Response.success(contractService.getToken(userId));
    }


    /**
     * 添加消息
     *
     * @param userId  用户id
     * @param msgCode 消息类型
     * @param topic   标题
     * @param msg     内容
     * @return
     */
    @RequestMapping("add/msg")
    public Response addMsg(Integer userId, Integer msgCode, String topic, String msg) {
        userRpcService.addMsg(userId, msgCode, topic, msg);
        return Response.success();
    }
}
