package com.finace.miscroservice.official_website.controller;

import com.finace.miscroservice.commons.base.BaseController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.official_website.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其他
 */
@RestController
@RequestMapping("other")
public class CommonController extends BaseController {
    private Log  logger = Log.getInstance(CommonController.class);


    @Autowired
    private CommonService commonService;

    /**
     * 运营报告
     *
     * @param page
     * <p>
     * {
     *             "lastPage": 1,
     *             "navigatepageNums": [1],
     *             "startRow": 1,
     *             "hasNextPage": false,
     *             "prePage": 0,
     *             "nextPage": 0,
     *             "endRow": 1,
     *             "pageSize": 10,
     *             "list": [{
     *             "imgurl": "http://118.31.129.39/ytj/data/banner/20180320145717ff87.jpg",  图片地址
     *             "addtime": "2018-03-20 14:57:17", 添加时间
     *             "jumurl": "https://www.baidu.com", 跳转链接
     *             "name": "运营报告1" 名称
     *             }],
     *             "pageNum": 1,
     *             "navigatePages": 8,
     *             "navigateFirstPage": 1,
     *             "total": 1,
     *             "pages": 1,
     *             "firstPage": 1,
     *             "size": 1,
     *             "isLastPage": true,
     *             "hasPreviousPage": false,
     *             "navigateLastPage": 1,
     *             "isFirstPage": true
     *             }
     * @return
     */
    @RequestMapping("report")
    public Response runReport(Integer page){
        if (page==null||page<1){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}运营报告",ip);
        return commonService.runReport(page);
    }

    /**
     * 活动中心
     * @param page
     * @return
     */
    @RequestMapping("active")
    public Response activeCenter(Integer page){
        if (page==null||page<1){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}官方通知",ip);
        return commonService.activeCenter(page);
    }
    /**
     * 平台公告
     * @param page
     * @return
     */
    @RequestMapping("news")
    public Response newsCenter(Integer page){
        if (page==null||page<1){
            logger.warn("页码错误");
            return Response.errorMsg("页码错误");
        }
        String  ip = Iptools.gainRealIp(request);
        logger.info("IP={}公告中心",ip);
        return commonService.newsCenter(page);
    }
}
