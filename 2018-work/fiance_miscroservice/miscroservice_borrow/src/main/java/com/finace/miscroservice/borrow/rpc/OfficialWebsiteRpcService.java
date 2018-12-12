package com.finace.miscroservice.borrow.rpc;

import com.finace.miscroservice.commons.utils.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
@FeignClient(value = "OFFICIAL-WEBSITE")
public interface OfficialWebsiteRpcService {
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
    @RequestMapping("/sys/website/getDataCollection")
    Response getDataCollection();

}
