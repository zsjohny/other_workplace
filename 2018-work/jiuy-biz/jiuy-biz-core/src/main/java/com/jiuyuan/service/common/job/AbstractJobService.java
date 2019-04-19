package com.jiuyuan.service.common.job;


import com.jiuyuan.constant.JobTaskType;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.service.common.DataDictionaryService;
import com.jiuyuan.service.common.HttpClientService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.util.JobDetailVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 申请job的抽象类
 * <p>
 * 当前访问的所有job服务地址都是一个默认地址
 *
 * @author Charlie(唐静)
 * @version V1.0
 * @title 定时任务
 * @package jiuy-biz
 * @description
 * @date 2018/5/30 12:59
 * @copyright 玖远网络
 */
public abstract class AbstractJobService implements IJobService{

    static Log logger = LogFactory.getLog(AbstractJobService.class);

    /**
     * job服务url 对应的字典表groupCode
     */
    public static final String QUARTZ_SERVER_GROUP_CODE = "quartz_server";
    /**
     * job服务url 对应的字典表code
     */
    public static final String QUARTZ_SERVER_CODE_ADD = "simple";
    /**
     * job服务url 对应的字典表code
     */
    public static final String QUARTZ_SERVER_CODE_MULTI = "multiple";
    /**
     * 定时上架回调地址 对应的字典表groupCode
     * <p>
     * 目前是默认值, 通用
     */
    public static final String CALLBACK_DICT_GROUP_CODE = "quartz_callback_url";

    /**
     * @Autowired
     */
    protected MemcachedService memcachedService;
    @Autowired
    protected DataDictionaryService dataDictionaryService;
    @Autowired
    private HttpClientService httpClientService;


    /**
     * 向job server申请多种类型的job服务
     *
     * @param description 申请服务的描述, 用于记录日志
     * @param sources
     * @return com.jiuyuan.service.common.job.AbstractJobService.JobResponse
     * @auther Charlie(唐静)
     * @date 2018/6/10 10:04
     */
    @Override
    public JobResponse applyMultiJobTask(String description, Collection<JobDetailVo>... sources) throws IOException {
        int len = sources.length;
        if (len == 0) {
            throw new NullPointerException("申请多种类型的job服务任务, jobDetailVo长度未 0");
        }
        List<JobDetailVo> merge = new ArrayList<>(len);
        for (Collection<JobDetailVo> vos : sources) {
            merge.addAll(vos);
        }

        // 获取服务地址
        String serverUrl = getMultiJobTaskServerUrl(QUARTZ_SERVER_CODE_MULTI);
        //String serverUrl = "http://192.168.10.109:8088/acceptJobs";
        // 记录日志
        logRecord(description, serverUrl, merge);
        // 请求服务参数
        List<NameValuePair> list = new ArrayList<>(1);
        String json = BizUtil.bean2json(merge);
        NameValuePair nvp = new BasicNameValuePair("jobDetailVos", URLEncoder.encode(json, "utf-8"));
        list.add(nvp);
        // 发送请求
        CachedHttpResponse response = httpClientService.post(serverUrl, list);
        JobResponse jobResponse = decorateResponse(response);
        if (jobResponse.ok()) {
            return jobResponse;
        }


        // 再次申请服务,容错处理
        JobResponse secondResponse = tryAgain(merge, jobResponse);
        return secondResponse != null? secondResponse: jobResponse;
    }



    /**
     * 再次申请服务,容错处理
     *
     * @param historyDetailList 首次请求错误信息
     * @param jobResponse
     * @return com.jiuyuan.service.common.job.AbstractJobService.JobResponse  二次申请服务成功,返回JobResponse, 失败,返回null
     * @author Charlie(唐静)
     * @date 2018/6/29 10:57
     */
    private JobResponse tryAgain(List<JobDetailVo> historyDetailList, JobResponse jobResponse) throws IOException {
        // 上线补丁(18/06/29): 目前只对单个任务容错处理,多任务暂不考虑
        if (historyDetailList.size() == 1) {
            JobDetailVo historyDetail = historyDetailList.get(0);

            if (jobResponse.isNullPointerException()) {
                // 返回空指针 1.修改(先删再修改),2.删除(忽略)
                // 通常job服务都是一个一个调用,对于这种常见情况进行修补,如果只有一个任务,尝试将任务设置成新增一个任务
                if (historyDetail.getJobType().equals(JobTaskType.UPDATE.getType())) {
                    historyDetail.setJobType(JobTaskType.ADD.getType());
                    JobResponse secondResponse = applyMultiJobTask("申请修改一个job任务失败,返回NullPointerException,尝试二次请求,直接新增一个任务...", Arrays.asList(historyDetail));
                    if (secondResponse.ok()) {
                        logger.info("申请修改一个job任务失败,返回NullPointerException,尝试二次请求,直接新增一个任务成功");
                        return secondResponse;
                    }else {
                        logger.info("申请修改一个job任务失败,返回NullPointerException,尝试二次请求,直接新增一个任务失败!!!");
                    }
                }
            }

            else if (jobResponse.isObjectAlreadyExistsException()) {
                // 返回记录已存在 1.新增
                if (historyDetail.getJobType().equals(JobTaskType.ADD.getType())) {
                    historyDetail.setJobType(JobTaskType.UPDATE.getType());
                    JobResponse secondResponse = applyMultiJobTask("申请新增一个job任务失败,返回ObjectAlreadyExistsException,尝试二次请求,将已存在的任务修改成当前新任务...", Arrays.asList(historyDetail));
                    if (secondResponse.ok()) {
                        logger.info("申请修改一个job任务失败,返回ObjectAlreadyExistsException,尝试二次请求,将已存在的任务修改成当前新任务成功");
                        return secondResponse;
                    }
                    else {
                        logger.info("申请修改一个job任务失败,返回ObjectAlreadyExistsException,尝试二次请求,将已存在的任务修改成当前新任务失败!!!");
                    }
                }
            }
            else {
                // expand 注意:当扩展时产生循环调用死循环
            }
        }
        return null;
    }


    /**
     * 获取服务地址
     *
     * @param dataDictCode 字典表code
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/6/13 9:15
     */
    protected String getMultiJobTaskServerUrl(String dataDictCode) {
        DataDictionary dataDictionary = dataDictionaryService.getByGroupAndCode(QUARTZ_SERVER_GROUP_CODE, dataDictCode);
        if (dataDictionary == null) {
            throw new RuntimeException("查询字典表尾查到值, groupCode:" + QUARTZ_SERVER_GROUP_CODE + ",code:" + dataDictCode);
        }
        return dataDictionary.getComment();
    }


    /**
     * 新增一个job任务
     *
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    @Override
    public JobResponse addJob(String description, JobDetailVo vo) throws IOException {
        // 设置服务内容为新增
        vo.setJobType(JobTaskType.ADD.getType());
        // 获取服务地址
        String serverUrl = getAddServerUrl();
        // 请求服务参数
        List<NameValuePair> list = vo.getNameValueList();
        // 记录日志
        logRecord(description, serverUrl, Arrays.asList(new JobDetailVo[]{vo}));
        // 发送请求
        CachedHttpResponse response = httpClientService.post(serverUrl, list);
        return decorateResponse(response);
    }

    /**
     * 暂停一个job任务
     *
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    @Override
    public JobResponse pauseJob(String description, JobDetailVo vo) {
        throw new IllegalAccessError("方法尚未实现, 请先实现!!!");
    }

    /**
     * 删除一个job任务
     *
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    @Override
    public JobResponse deleteJob(String description, JobDetailVo vo) throws IOException {
        // 设置服务内容为新增
        vo.setJobType(JobTaskType.DELETE.getType());
        // 获取服务地址
        String serverUrl = getDeleteServerUrl();
        // 请求服务参数
        List<NameValuePair> list = vo.getNameValueList();
        // 记录日志
        logRecord(description, serverUrl, Arrays.asList(new JobDetailVo[]{vo}));
        // 发送请求
        CachedHttpResponse response = httpClientService.post(serverUrl, list);
        return decorateResponse(response);
    }

    /**
     * 修改一个job任务
     *
     * @param vo
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    @Override
    public JobResponse updateJob(String description, JobDetailVo vo) {
        throw new IllegalAccessError("方法尚未实现, 请先实现!!!");
    }


    /**
     * 获取默认的回调地址
     * <p>
     * 当前默认字典表 groupCode 都是一样的
     *
     * @param dataDictCode 字典表中回调地址对应的code列
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/6/7 17:46
     */
    protected String getDefaultCallbackURL(String dataDictCode) {
        DataDictionary dataDictionary = dataDictionaryService.getByGroupAndCode(CALLBACK_DICT_GROUP_CODE, dataDictCode);
        return dataDictionary.getComment();
    }


    /**
     * job服务接口地址, 新增一个job任务的接口地址
     *
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    protected String getAddServerUrl() {
        return getDefaultServerUrl();
    }

    /**
     * job服务接口地址, 修改一个job任务的接口地址
     *
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    protected String getUpdateServerUrl() {
        return getDefaultServerUrl();
    }

    /**
     * job服务接口地址, 暂停一个job任务的接口地址
     *
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    protected String getPauseServerUrl() {
        return getDefaultServerUrl();
    }

    /**
     * job服务接口地址, 删除一个job任务的接口地址
     *
     * @return
     * @auther Charlie(唐静)
     * @date
     */
    protected String getDeleteServerUrl() {
        return getDefaultServerUrl();
    }

    /**
     * 查询job服务的地址 默认从数据库字典表中获取
     *
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/5/31 12:51
     */
    private String getDefaultServerUrl() {
        // 开启缓存 return getUrlFromCacheOrDatabase(MemcachedKey.GROUP_KEY_QUARTZ_SERVER, QUARTZ_SERVER_CODE_ADD, QUARTZ_SERVER_GROUP_CODE, QUARTZ_SERVER_CODE_ADD);

        DataDictionary dataDict = dataDictionaryService.getByGroupAndCode(QUARTZ_SERVER_GROUP_CODE, QUARTZ_SERVER_CODE_ADD);
        if (null == dataDict) {
            logger.error("字典表查询记录, 未找到记录 groupCode:" + QUARTZ_SERVER_GROUP_CODE + ";code:" + QUARTZ_SERVER_CODE_ADD);
            throw new RuntimeException("字典表查询记录, 未查到记录");
        }
        return dataDict.getComment();
    }


    /**
     * 查询定时任务服务的地址URL
     * <p>
     * 优先在缓存中查找, 缓存中没有就在数据库中查, 并放入缓存, 在缓存中5min中失效
     *
     * @param cacheGroupKey 在缓存中查找, 查找条件 groupKey
     * @param cacheKey      在缓存中查找, 查找条件 key
     * @param dictGroupCode 在数据库字典表中查找, 查找条件 groupCode
     * @param dictCode      在数据库字典表中查找, 查找条件 code
     * @return java.lang.String
     * @auther Charlie(唐静)
     * @date 2018/5/31 12:51
     */
    @Deprecated
    public String getUrlFromCacheOrDatabase(String cacheGroupKey, String cacheKey, String dictGroupCode, String dictCode) {
        //memcacheService现在没有注入, 要用的时候记得autowire注解放开
        String addServerUrl = memcachedService.getStr(cacheGroupKey, cacheKey);
        if (! BizUtil.isNotEmpty(addServerUrl)) {
            //数据库查询
            DataDictionary dataDict = dataDictionaryService.getByGroupAndCode(dictGroupCode, dictCode);
            addServerUrl = dataDict.getComment();
            //放入缓存 缓存时间5min
            memcachedService.set(cacheGroupKey, cacheKey, 60 * 5, addServerUrl);
        }
        return addServerUrl;
    }


    /**
     * 记录申请job服务日志
     *
     * @param description 申请服务的描述
     * @param serverUrl   job 服务请求地址
     * @param collection  job 服务请求参数封装
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/7 16:38
     */
    protected void logRecord(String description, String serverUrl, Collection<JobDetailVo> collection) {
        String msg = "";
        if (! collection.isEmpty()) {
            StringBuilder builder = new StringBuilder("[");
            for (JobDetailVo vo : collection) {
                builder.append(vo.toString()).append(",");
            }
            msg = builder.substring(0, builder.lastIndexOf(","));
            msg += "]";
        }

        //日志
        StringBuilder logMsg = new StringBuilder(description);
        logMsg.append("@@@发起http请求, 申请job服务--> serverUrl: ")
                .append(serverUrl)
                .append(", jobDetail: {")
                .append(msg)
                .append("}");

        logger.info(logMsg.toString());
    }

    public JobResponse decorateResponse(CachedHttpResponse response) {
        return new JobResponse(response);
    }

    /**
     * @author Charlie(唐静)
     * @version V1.0
     * @title 申请job 返回值封装
     * @package jiuy-biz
     * @description
     * @date 2018/6/7 17:14
     * @copyright 玖远网络
     */
    class JobResponse{
        private CachedHttpResponse response;
        private static final String OK_REGEX = "\"code\":\"200\"";
        private static final String NULL_POINTER_EXCEPTION = "NullPointerException";
        private static final String OBJECT_EXISTS_EXCEPTION = "ObjectAlreadyExistsException";

        private JobResponse(CachedHttpResponse response) {
            this.response = response;
        }

        /**
         * 申请job服务是否成功
         *
         * @return boolean
         * @auther Charlie(唐静)
         * @date 2018/6/7 17:11
         */
        public boolean ok() {
            return response.getResponseText().contains(OK_REGEX);
        }

        /**
         * 申请job服务返回信息
         *
         * @return java.lang.String
         * @auther Charlie(唐静)
         * @date 2018/6/7 17:11
         */
        public String msg() {
            return response.getResponseText();
        }

        public boolean isNullPointerException() {
            return response.getResponseText().contains(NULL_POINTER_EXCEPTION);
        }

        public boolean isObjectAlreadyExistsException() {
            return response.getResponseText().contains(OBJECT_EXISTS_EXCEPTION);
        }
    }
}
