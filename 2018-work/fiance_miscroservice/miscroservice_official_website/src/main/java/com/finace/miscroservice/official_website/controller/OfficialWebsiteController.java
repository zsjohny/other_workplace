package com.finace.miscroservice.official_website.controller;


import com.finace.miscroservice.commons.base.BaseController;

import com.finace.miscroservice.commons.log.Log;

import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.official_website.rpc.BorrowRpcService;
import com.finace.miscroservice.official_website.service.OfficialWebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 理财模块
 */
@RestController
@RequestMapping("pc")
public class OfficialWebsiteController extends BaseController {

    private Log logger = Log.getInstance(OfficialWebsiteController.class);
    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> userStrHashRedisTemplate;

    @Autowired
    private OfficialWebsiteService officialWebsiteService;

    @Autowired
    private BorrowRpcService borrowRpcService;

    /**
     * PC首页-- banner列表-- 新手标&&推荐标
     *
     *             <p>
     *             {
     *             "msg": "",
     *             "code": 200,
     *             "data": {
     *             "addUpMoney":"",  //累计交易
     *             "addUpInterest":"",//累计收益
     *             "sevenGetMoney":"", //7天交易量
     *             "xsProduct":{  //新手标信息
     *             "name": "新手测试005", 标的名称
     *             "account": "20000", 金额
     *             "accountYes": "2000.00", 已购金额
     *             "surplusAccount": "2000.00", 剩余金额
     *             "apr": "12.00", 年利率
     *             "lowestAccount": "1000", 最低购买金额
     *             "mostAccount": "30000", 最高购买金额
     *             "timeLimitDay": "7", 期限
     *             "scales": "0.10000", 购买比例
     *             "use": "0",  0新手标 1 普通标
     *             "addApr": "0.00", 加息券
     *             "id": "10793",
     *             "litpic": "",  缩略图
     *             "status": "1" 标的状态
     *             }
     *             "productResponseList": [{
     *             "name": "30", 标的名称
     *             "account": "200000",金额
     *             "accountYes": "31000.00",已购金额
     *             "apr": "12.00",年利率
     *             "lowestAccount": "1000",最低购买金额
     *             "mostAccount": "30000",最高购买金额
     *             "surplusAccount": "2000.00", 剩余金额
     *             "timeLimitDay": "30",期限
     *             "scales": "0.15500",购买比例
     *             "use": "1",0新手标 1 普通标
     *             "addApr": "2.00",加息券
     *             "id": "10790",
     *             "litpic": "",缩略图
     *             "status": "1"标的状态
     *             }, {
     *             "name": "30天标006",
     *             "account": "20000",
     *             "accountYes": "7000.00",
     *             "apr": "12.00",
     *             "lowestAccount": "1000",
     *             "mostAccount": "30000",
     *             "surplusAccount": "2000.00", 剩余金额
     *             "timeLimitDay": "30",
     *             "scales": "0.35000",
     *             "use": "1",
     *             "addApr": "0.00",
     *             "id": "10796",
     *             "litpic": "",
     *             "status": "1"
     *             }, {
     *             "name": "30天标005",
     *             "account": "20000",
     *             "accountYes": "8000.00",
     *             "apr": "12.00",
     *             "lowestAccount": "1000",
     *             "mostAccount": "30000",
     *             "surplusAccount": "2000.00", 剩余金额
     *             "timeLimitDay": "30",
     *             "scales": "0.40000",
     *             "use": "1",
     *             "addApr": "0.00",
     *             "id": "10795",
     *             "litpic": "",
     *             "status": "1"
     *             }],
     *             "imagesTypePOList": [{
     *             "id": 87,
     *             "name": "vfdsghtsrb", 名称
     *             "type": 3, 类型  1--app首页  2--活动中心  3--PC首页 4--运营报告
     *             "status": 1, 状态 0--隐藏 1--显示
     *             "jumurl": "股份的生日发送人数", 点击跳转url
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/20180312194838ba90.png", 图片地址
     *             "scontent": "", 分享内容
     *             "stitle": "", 分享标题
     *             "torder": 1,
     *             "stime": "2018-03-12 19:48:33",
     *             "etime": "2018-03-30 19:48:34",
     *             "addtime": "2018-03-12 19:48:38"
     *             }, {
     *             "id": 88,
     *             "name": "和认同感的还有他的号当天",
     *             "type": 3,
     *             "status": 1,
     *             "jumurl": "不规范的河南的",
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/201803122004254352.png",
     *             "scontent": "",
     *             "stitle": "",
     *             "torder": 1,
     *             "stime": "2018-03-12 20:04:19",
     *             "etime": "2018-03-11 20:04:21",
     *             "addtime": "2018-03-12 20:04:25"
     *             }, {
     *             "id": 105,
     *             "name": "测60天1",
     *             "type": 3,
     *             "status": 1,
     *             "jumurl": "index",
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/2018032615201672da.png",
     *             "scontent": "",
     *             "stitle": "",
     *             "torder": 1,
     *             "stime": "2018-01-01 15:51:58",
     *             "etime": "2018-05-11 15:21:15",
     *             "addtime": "2018-03-26 15:20:16"
     *             }],
     *             "addUpMoney": 2883776.0,  总计 金额
     *             "addUpInterest": 50292.6, 总计利息
     *             "sevenGetMoney": 118138.0, 7天 赚取金额
     *             "list1": [{  运营
     *             "name": "运营报告1",
     *             "jumurl": "https://www.baidu.com",  跳转链接或者地址
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/20180320145717ff87.jpg", 图片地址
     *             "addtime": "2018-03-20 14:57:17" 添加时间
     *             }],
     *             "list2": [{ 活动
     *             "name": "123131",
     *             "jumurl": "https://www.baidu.com/",
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/20180313101833750b.png",
     *             "addtime": "2018-03-13 10:18:33"
     *             }, {
     *             "name": "1245215124",
     *             "jumurl": "https://www.baidu.com/",
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/20180313101906561f.png",
     *             "addtime": "2018-03-13 10:19:06"
     *             }, {
     *             "name": "123124",
     *             "jumurl": "https://www.baidu.com/",
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/201803131019366e14.png",
     *             "addtime": "2018-03-13 10:19:36"
     *             }],
     *             "list3": [{ 公告
     *             "topic": "公告中心",
     *             "msg": "http://www.etongjin.com.cn/app/activity/huawei.html",
     *             "addTime": "1521686332241"
     *             }, {
     *             "topic": "公告中心002",
     *             "msg": "http://www.etongjin.com.cn/app/activity/huawei.html1",
     *             "addTime": "1521686419185"
     *             }, {
     *             "topic": "公告中心003",
     *             "msg": "http://www.etongjin.com.cn/app/activity/huawei.html",
     *             "addTime": "1521687460298"
     *             }]
     *             }
     *             }
     * @return
     */
    @RequestMapping("index")
    public Response pcIndex() {
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}进入PC首页",ip);
        return officialWebsiteService.pcIndex();
    }

    /**
     * 理财产品列表
     *
     * @param isxs 是否为新手  0--不是新手1--是新手
     * @param page 页码
     * @param time 投资期限
     *             <p>
     *             {"msg":"","code":200,"data":
     *             {
     *             "pageNum":1,
     *             "pageSize":10,
     *             "size":10,
     *             "startRow":1,
     *             "endRow":10,
     *             "total":15,
     *             "pages":2,
     *             "list":
     *             [
     *             {
     *             "id":10749,
     *             "siteId":0,
     *             "userId":0,
     *             "name":"3天标001",
     *             "status":1,
     *             "order":0,
     *             "hits":0,
     *             "litpic":"",
     *             "isVouch":0,
     *             "viewType":0,
     *             "vouchTimes":0,
     *             "repaymentUser":0,
     *             "repaymentYesinterest":0,
     *             "use":"1",
     *             "account":"100000",
     *             "accountYes":"15690.00",
     *             "surplusAccount": "2000.00", 剩余金额
     *             "apr":10.0,
     *             "addApr":0.0,
     *             "lowestAccount":"1000","mostAccount":"30000","award":0.0,"partAccount":0.0,"funds":0.0,"isMb":0,"isFast":0,"isJin":0,"isXin":0,"isday":0,"timeLimitDay":3,"isArt":0,"isCharity":0,"isProject":0,"isFlow":0,"flowStatus":0,"flowMoney":0,"flowCount":0,"flowYescount":0,"isStudent":0,"isOffvouch":0,"hbFlag":0,"scales":"0.15690","borrowTotal":0.0,"borrowInterest":0.0,"borrowType":0,"count1":0,"count2":0,"count3":0,"remmoney":84310.0,"borrowTypes":0,"hongbaoTrans":0,"releaseType":0
     *             }
     *             ],
     *             "prePage":0,
     *             "nextPage":2,
     *             "isFirstPage":true,
     *             "isLastPage":false,
     *             "hasPreviousPage":false,
     *             "hasNextPage":true,
     *             "navigatePages":8,
     *             "navigatepageNums":[1,2],
     *             "navigateFirstPage":1,
     *             "navigateLastPage":2,
     *             "firstPage":1,
     *             "lastPage":2
     *             }
     *             }
     * @return
     */
    @RequestMapping("product/list")
    public Response pcProductList(String isxs,Integer page,Integer time) {
        if (page==(null)||page<1){
            logger.warn("参数不能为空");
            return Response.errorMsg("参数为空或页码小于1");
        }
        String  ip = Iptools.gainRealIp(request);

        logger.info("IP={} 查询理财产品列表",ip);

        return officialWebsiteService.pcProductList(isxs,page,time);
    }


    /**
     * 理财产品详情
     *
     * @param id 理财产品id
     *           <p>
     *           {
     *           "use": "0", //0为新手标 1为普通标
     *           "loanUsage": "用于向核心企业订购一批家具或资金周转",   //借款用途
     *           "financeCompany": "杭州**贸易有限公司", //融资企业
     *           "timeLimitDay": "7",//期限
     *           "litpic": "", //缩略图
     *           "payment": "销售回款", //还款来源
     *           "id": "10793",
     *           "imgurl10": "",
     *           "mostAccount": "30000", 最多投标总额
     *           "apr": "12.00",  年利率
     *           "accountYes": "2000.00",  已购买金额
     *           "surplusAccount": "2000.00", 剩余金额
     *           "lowestAccount": "1000", 最低投标金额
     *           "imgurl1": "",
     *           "imgurl2": "",
     *           "addApr": "0.00",  加息年利率
     *           "imgurl3": "",
     *           "scales": "0.10000",  已购比例
     *           "imgurl4": "",
     *           "name": "新手测试005",    标的名称
     *           "imgurl5": "",
     *           "imgurl6": "",
     *           "imgurl7": "",
     *           "imgurl8": "",
     *           "imgurl9": "",
     *           "account": "20000", 借贷总金额
     *           "status": "1"  状态 区分标的状态 0 待审 1 初审通过 2 初审不通过 3满标复审中 6还款中 8已还款
     *           }
     * @return
     */
    @RequestMapping("product/detail")
    public Response pcProductDetail(Integer id) {
        if (null==id){
            logger.warn("参数为空 id={}",id);
            return Response.errorMsg("参数为空");
        }
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}查询产品详情",ip);
        return officialWebsiteService.pcProductDetail(id);
    }


    /**
     * 理财产品--投资记录
     *
     * @param page 页码
     * @param borrowId 标的 id
     *             <p>
     *             {
     *             "lastPage": 0,
     *             "navigatepageNums": [],
     *             "startRow": 0,
     *             "hasNextPage": false,
     *             "prePage": 0,
     *             "nextPage": 0,
     *             "endRow": 0,
     *             "pageSize": 10,
     *             "list": [
     *             "borrowId":"",
     *             "account":"",  金额
     *             "addtime":"", 添加时间
     *             "phone":"" 手机号
     *             ],
     *             "pageNum": 1,
     *             "navigatePages": 8,
     *             "navigateFirstPage": 0,
     *             "total": 0,
     *             "pages": 0,
     *             "firstPage": 0,
     *             "size": 0,
     *             "isLastPage": true,
     *             "hasPreviousPage": false,
     *             "navigateLastPage": 0,
     *             "isFirstPage": true
     *             }
     * @return
     */
    @RequestMapping("product/records")
    public Response pcProductRecordes(Integer page,Integer borrowId) {
        if (null==page||page<1){
            logger.warn("参数为空 或 页码 小于 1");
            return Response.errorMsg("参数为空 或 页码小于1");
        }
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}查询投资记录",ip);
        return officialWebsiteService.pcProductRecordes(page,borrowId);
    }


    /**
     * 数据披露
     * <p>
     * {
     * "msg": "",
     * "code": 200,
     * "data": {
     * "ljcjje": 2834746.0, //累计交易总额
     * "ljyhsy": 49803.1, //累计用户收益(元)
     * "ljzcyh": 653, //累计注册用户(人)
     * "ljcjbs": 389, //累计交易笔数
     * "ljcjrzs": 122, //累计出借人数量
     * "bncjje": 1357751.0, //本年度成交金额(元)
     * "zddhcjyezb": 191611.0, //最大单一借款人待还金额占比
     * "qydhcjyezb": -191610.0, //其余单户出借余额占比
     * "zdshtzcjzb": 743578.0, //前十大借款人待还金额占比
     * "qyyhcjyezb": -743577.0,//其余用户出借余额占比
     * "xbnan": 0.2462, //性别男 占比
     * "xbnv": 0.7538, //性别女 占比
     * "dhjebs": 293,//待还金额笔数(笔)
     * "ljjkrzs": 4, //累计借款人数量
     * "dhje": 1240201.0, //待还金额(万元)===当前出借金额---当前借款金额
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
//    @RequestMapping("data/collection")
//    public Response pcDataCollection(){
//        String  ip = Iptools.gainRealIp(request);
//        logger.info("IP={}数据披露",ip);
//        return officialWebsiteService.pcDataConllection();
//    }

    /**
     * 数据披露
     * @return
     */
    @RequestMapping(value = "data/collection")
    public Response getDataCollection() {
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}数据披露",ip);
        return Response.success(borrowRpcService.getDataCollection());
    }

}
