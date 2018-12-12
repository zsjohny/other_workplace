package com.jiuy.pcweb.controller;

import com.jiuy.base.model.MyPage;
import com.jiuy.model.web.article.OperatorArticle;
import com.jiuy.model.web.article.OperatorArticleQuery;
import com.jiuy.model.web.article.OperatorSeo;
import com.jiuy.service.web.IArticleService;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Properties;

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

    //    @PostConstruct
    public void init() throws IOException, MalformedObjectNameException {

        client();
    }

    private DiscoveryClient client() throws IOException, MalformedObjectNameException {


        Properties properties = new Properties();
        String registerUrl = "http://test:test@47.97.174.183:6898/eureka,http://test:test@$47.97.174.183:6899/eureka";

        properties.put("eureka.name", "test");
        //默认
        properties.put("eureka.port", ManagementFactory.getPlatformMBeanServer().queryNames(new ObjectName("*:type=Connector,*"),
                Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"))).iterator().next().getKeyProperty("port"));
        properties.put("eureka.instance.ip", InetAddress.getLocalHost().getHostAddress());
        properties.put("eureka.serviceUrl.default", registerUrl);
        properties.put("eureka.region", "default");
        properties.put("eureka.vipAddress", properties.getProperty("eureka.name"));
//        properties.put("eureka.preferSameZone", "false");
//        properties.put("eureka.shouldUseDns", "false");
        properties.put("eureka.registration.enabled", "true");
//        properties.put("eureka.us-east-1.availabilityZones", "default");

        properties.put("eureka.instanceId", properties.getProperty("eureka.instance.ip") + ":" + properties.getProperty("eureka.port"));
        properties.put("eureka.statusPageUrl", "http://" + properties.getProperty("eureka.instanceId") + "/info");


        ConfigurationManager.loadProperties(properties);

        MyDataCenterInstanceConfig instanceConfig = new MyDataCenterInstanceConfig();
        InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
        instanceInfo.setStatus(InstanceInfo.InstanceStatus.UP);
        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, instanceInfo);

        DiscoveryClient discoveryClient = new DiscoveryClient(applicationInfoManager, new DefaultEurekaClientConfig());


        return discoveryClient;
    }

    public static void main(String[] args) throws IOException, MalformedObjectNameException {

        new IndexController().client();
        while (true) {

        }

    }


    @GetMapping("info")
    @ResponseBody
    public String get() {
        return "HEALTHS";
    }

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
