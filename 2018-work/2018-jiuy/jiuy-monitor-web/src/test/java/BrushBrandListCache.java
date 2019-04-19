import com.jiuy.base.util.Biz;
import com.jiuy.base.util.HttpRequest;
import com.jiuy.timer.model.QrtzJobsAcceptQuery;
import lombok.AllArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @date 2018/6/25 17:40
 * @Copyright 玖远网络
 */
public class BrushBrandListCache{

    /**
     * 1 新增
     * 5 删除
     */
    static int switcher = 1;

    public static void main(String[] args) throws UnsupportedEncodingException {

        brushHotSale(ENVIRONMENT.开发);
        brushRecommend(ENVIRONMENT.开发);
    }



    /**
     * 刷热卖
     *
     * @param environment
     * @return void
     * @author Charlie(唐静)
     * @date 2018/6/25 18:32
     */
    static void brushHotSale(ENVIRONMENT environment) throws UnsupportedEncodingException {

        List<QrtzJobsAcceptQuery> params = hotSaleTask(environment);
        String json = Biz.obToJson(params);

        Map<String, Object> map = new HashMap<>(2);
        map.put("jobDetailVos", URLEncoder.encode(json,"utf-8"));

        String response = HttpRequest.sendPostJson(environment.getJobServerUrl(), map);

        System.out.println("=====================================================");
        System.out.println("热卖 @response: " + response);
        System.out.println("=====================================================");
    }



    /**
     * 刷推荐
     *
     * @param environment
     * @return void
     * @author Charlie(唐静)
     * @date 2018/6/25 18:32
     */
    static void brushRecommend(ENVIRONMENT environment) throws UnsupportedEncodingException {

        List<QrtzJobsAcceptQuery> params = recommendTask(environment);
        String json = Biz.obToJson(params);

        Map<String, Object> map = new HashMap<>(2);
        map.put("jobDetailVos", URLEncoder.encode(json,"utf-8"));

        String response = HttpRequest.sendPostJson(environment.getJobServerUrl(), map);

        System.out.println("=====================================================");
        System.out.println("推荐 @response: " + response);
        System.out.println("=====================================================");
    }




    /**
     * 热卖请求参数构建
     *
     * @param env 请求服务的环境
     * @return java.util.List<com.jiuy.timer.model.QrtzJobsAcceptQuery>
     * @author Charlie(唐静)
     * @date 2018/6/25 18:03
     */
    private static List<QrtzJobsAcceptQuery> hotSaleTask(ENVIRONMENT env) {
        List<QrtzJobsAcceptQuery> list = new ArrayList<>(8);
        for (int pageSize = 1; pageSize <= 6; pageSize++) {
            QrtzJobsAcceptQuery query = new QrtzJobsAcceptQuery();
            query.setJobType(switcher);
            query.setCronExpression("0 0/1 * * * ?");
            query.setJobName("HOT_SALE_" + pageSize + "_20");
            query.setJobGroup("BRAND_LIST_BRUSH_CACHE");
            query.setFeedbackData("{\"seconds\":600,\"pageSize\":20,\"page\":"+pageSize+",\"type\":0}");
            query.setFeedbackUrl(env.getCallBackUrl());
            query.setJobComment("品牌热卖刷缓存");
            list.add(query);
        }
        return list;
    }


    /**
     * 推荐请求参数构建
     *
     * @param env 请求服务的环境
     * @return java.util.List<com.jiuy.timer.model.QrtzJobsAcceptQuery>
     * @author Charlie(唐静)
     * @date 2018/6/25 18:03
     */
    private static List<QrtzJobsAcceptQuery> recommendTask(ENVIRONMENT env) {
        List<QrtzJobsAcceptQuery> list = new ArrayList<>(8);
        for (int pageSize = 1; pageSize <= 6; pageSize++) {
            QrtzJobsAcceptQuery query = new QrtzJobsAcceptQuery();
            query.setJobType(switcher);
            query.setCronExpression("0 0/1 * * * ?");
            query.setJobName("RECOMMEND_" + pageSize + "_20");
            query.setJobGroup("BRAND_LIST_BRUSH_CACHE");
            query.setFeedbackData("{\"seconds\":600,\"pageSize\":20,\"page\":"+pageSize+",\"type\":1}");
            query.setFeedbackUrl(env.getCallBackUrl());
            query.setJobComment("品牌推荐刷缓存");
            list.add(query);
        }
        return list;
    }



    @AllArgsConstructor
    enum ENVIRONMENT{
        开发("http://monitoringlocal.yujiejie.com/acceptJobs","http://storelocal.yujiejie.com/mobile/product/brushBrandListCache.json"),
        测试("http://monitoringtest.yujiejie.com/acceptJobs","http://storetest.yujiejie.com/mobile/product/brushBrandListCache.json"),
        生产("http://monitoringonline.yujiejie.com/acceptJobs","http://storeonline.yujiejie.com/mobile/product/brushBrandListCache.json");

        private String jobServerUrl;
        private String callBackUrl;

        public String getJobServerUrl() {
            return jobServerUrl;
        }

        public String getCallBackUrl() {
            return callBackUrl;
        }
    }
}
