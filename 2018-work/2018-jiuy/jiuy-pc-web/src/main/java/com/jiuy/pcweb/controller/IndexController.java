package com.jiuy.pcweb.controller;

import com.jiuy.base.model.MyPage;
import com.jiuy.model.web.article.OperatorArticle;
import com.jiuy.model.web.article.OperatorArticleQuery;
import com.jiuy.model.web.article.OperatorSeo;
import com.jiuy.model.web.article.OperatorSeoQuery;
import com.jiuy.service.web.IArticleService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 此类是测试首页功能的类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/23 21:54
 * @Copyright 玖远网络
 */
@Controller
public class IndexController {


    @Resource(name = "articleService")
    private IArticleService articleService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 首页
     *
     * @date 2018/5/23 22:18
     * @author Aison
     */
    @RequestMapping("/index.html")
    public String indexHtml(Model model, OperatorArticleQuery query) {

        query.setLimit(3);
        queryArticle(1, model, query);
        model.addAttribute("seo", getSeo(1));
        if (isMobileDevice(request)) {
            return "wap/index";
        }
        return "pc/index";
    }


    public static boolean isMobileDevice(HttpServletRequest request) {
//        /**
//         * android : 所有android设备
//         * mac os : iphone ipad
//         * windows phone:Nokia等windows系统的手机
//         */
//        String requestHeader = request.getHeader("user-agent");
//        String[] deviceArray = new String[]{"android","mac os","windows phone"};
//        if(requestHeader == null) {
//            return false;
//        }
//        requestHeader = requestHeader.toLowerCase();
//        for(int i=0;i<deviceArray.length;i++){
//            if(requestHeader.indexOf(deviceArray[i])>0){
//                return true;
//            }
//        }
//        return false;

        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (null == userAgent) {
            userAgent = "";
        }

        boolean isFromMobile = false;
        isFromMobile = CheckMobile.check(userAgent);
        //判断是否为移动端访问
        if (isFromMobile) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 首页
     *
     * @date 2018/5/23 22:18
     * @author Aison
     */
    @RequestMapping("/")
    public String index(Model model, OperatorArticleQuery query) {

        query.setLimit(3);
        queryArticle(1, model, query);
        model.addAttribute("seo", getSeo(1));
        if (isMobileDevice(request)) {
            return "wap/index";
        }
        return "pc/index";
    }

    /**
     * 关于公司
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/aboutCompany.html")
    public String aboutCompany(Model model) {
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/aboutCompany";
        }
        return "pc/aboutCompany";
    }

    /**
     * 小程序
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/wxa.html")
    public String joinIn(Model model) {

        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/wxa";
        }
        return "pc/wxa";
    }

    /**
     * 加入我们
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/joinUs.html")
    public String joinUs(Model model) {
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/joinUs";
        }
        return "pc/joinUs";
    }

    /**
     * 新闻
     *
     * @param query 查询参数
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/news/list{pageNum}.html")
    public String newsList(OperatorArticleQuery query, Model model, @PathVariable("pageNum") Integer pageNum) {
        query.setLimit(10);
        queryArticle(pageNum, model, query);
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/news/list";
        }
        return "pc/news/list";
    }


    /**
     * 获取seo信息
     *
     * @param type 1是首页 2是默认的
     * @return com.jiuy.model.web.article.OperatorSeo
     * @author Aison
     * @date 2018/7/31 12:00
     */
    private OperatorSeo getSeo(Integer type) {
        return articleService.getSeo(type);
    }

    /**
     * 查询新闻列表
     *
     * @param pageNum pageNum
     * @param model   model
     * @param query   query
     * @author Aison
     * @date 2018/5/24 16:41
     */
    private void queryArticle(Integer pageNum, Model model, OperatorArticleQuery query) {
        query = query == null ? new OperatorArticleQuery() : query;
        query.setOffset(pageNum);
        query.setOrderBy("create_time desc");
        MyPage<OperatorArticle> operatorArticleList = articleService.getArticleList(query);
        model.addAttribute("articles", operatorArticleList);
    }

    /**
     * 新闻
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/articleDetail/{id}.html")
    public String newsDetail(@PathVariable("id") Long id, Model model) {

        OperatorArticle article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/news/newsDetail";
        }
        return "pc/news/newsDetail";
    }

    /**
     * 小程序加盟
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/wxaAgent.html")
    public String wxaAgent(Model model) {
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/wxaAgent";
        }
        return "pc/wxaAgent";
    }


    /**
     * 服务条款
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/server.html")
    public String server(Model model) {
        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/server";
        }
        return "pc/server";
    }

    /**
     * 隐私条款
     *
     * @date 2018/5/24 9:31
     * @author Aison
     */
    @RequestMapping("/privacy.html")
    public String privacy(Model model) {

        model.addAttribute("seo", getSeo(2));
        if (isMobileDevice(request)) {
            return "wap/privacy";
        }
        return "pc/privacy";
    }


}
