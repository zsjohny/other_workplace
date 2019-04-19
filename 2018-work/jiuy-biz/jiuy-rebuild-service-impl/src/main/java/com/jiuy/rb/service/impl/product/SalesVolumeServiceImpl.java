package com.jiuy.rb.service.impl.product;


import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.HttpRequest;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.mapper.product.SalesVolumePlainDetailRbMapper;
import com.jiuy.rb.mapper.product.SalesVolumePlainExceptionRbMapper;
import com.jiuy.rb.mapper.product.SalesVolumePlainRbMapper;
import com.jiuy.rb.mapper.product.SalesVolumeProductRbMapper;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.common.QrtzJobsAccept;
import com.jiuy.rb.model.product.*;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.product.ISalesVolumeFeedbackService;
import com.jiuy.rb.service.product.ISalesVolumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * 商品销量相关的业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 11:00
 * @Copyright 玖远网络
 */
@Service( "salesVolumeService" )
public class SalesVolumeServiceImpl implements ISalesVolumeService{

    private static final Logger LOGGER = LoggerFactory.getLogger (SalesVolumeServiceImpl.class);

    /**
     * 刷量增长类型:随机增长
     */
    static final int RANDOM_ADD = 1;
    /**
     * 刷量增长类型:固定增长
     */
    static final int FIX_ADD = 0;

    @Resource( name = "salesVolumePlainRbMapper" )
    private SalesVolumePlainRbMapper salesVolumePlainRbMapper;

    @Resource( name = "salesVolumePlainDetailRbMapper" )
    private SalesVolumePlainDetailRbMapper salesVolumePlainDetailRbMapper;

    @Resource( name = "dataDictionaryServiceRb" )
    private IDataDictionaryService dataDictionaryServiceRb;

    @Resource( name = "salesVolumeFeedbackService" )
    private ISalesVolumeFeedbackService salesVolumeFeedbackService;

    @Resource( name = "salesVolumePlainExceptionRbMapper" )
    private SalesVolumePlainExceptionRbMapper salesVolumePlainExceptionRbMapper;
    @Resource( name = "salesVolumeProductRbMapper" )
    private SalesVolumeProductRbMapper salesVolumeProductRbMapper;


    /**
     * 添加商品刷量策略
     *
     * @param plain       plain
     * @param userSession userSession
     * @author Aison
     * @date 2018/6/15 11:03
     */
    @Transactional( rollbackFor = Exception.class )
    @Override
    public MyLog<SalesVolumePlainRb> addSalesVolumePlain(SalesVolumePlainRb plain, UserSession userSession) {

        //必要参数验证
        if (Biz.hasEmpty (plain.getCountType (),
                plain.getName (),
                plain.getExecuteType (),
                plain.getEachTime (),
                plain.getExecuteTimeBegin (),
                plain.getExecuteTimeEnd (),
                plain.getProductQueryType (),
                plain.getProductQueryDetail ())
                ) {
            throw BizException.paramError ();
        }
        // 验证重名的
        SalesVolumePlainRbQuery query = new SalesVolumePlainRbQuery ();
        query.setName (plain.getName ());
        int count = salesVolumePlainRbMapper.selectCount (query);
        if (count > 0) {
            throw BizException.def ().msg ("重复的策略名称");
        }
        // 固定的
        if (plain.getCountType () == 0 && Biz.nullOrZero (plain.getEachAddCount ())) {
            throw BizException.instance (GlobalsEnums.PLAIN_EACH_COUNT_ERROR);
        }
        boolean flag = plain.getCountRandomMini () == null || Biz.nullOrZero (plain.getCountRandomMax ());
        // 随机的
        if (plain.getCountType () == 1 && flag) {
            throw BizException.instance (GlobalsEnums.PLAIN_EACH_COUNT_ERROR);
        }
        plain.setStatus (1);
        // 生成jobName 供回调使用
        String jobName = "plain_" + Biz.getUuid ();
        plain.setJobName (jobName);
        // 验证查询参数是否是个json
        try {
            Biz.jsonStrToMap (plain.getProductQueryDetail ());
        } catch (Exception e) {
            e.printStackTrace ();
            throw BizException.def ().msg ("查询参数格式错误 不是有效的json格式");
        }
        plain.setCreateTime (new Date ());
        plain.setUpdateTime (new Date ());
        int rs = salesVolumePlainRbMapper.insertSelective (plain);
        if (rs == 0) {
            throw BizException.instance (GlobalsEnums.FAILED);
        }
        DataDictionaryRb dataDictionaryRb = dataDictionaryServiceRb.getByCode ("simple", "quartz_server");
        if (dataDictionaryRb == null) {
            throw BizException.instance (GlobalsEnums.TIMER_URL_NOT_SET);
        }
        // 组装参数
        QrtzJobsAccept accept = postAccept (plain);
        String url = dataDictionaryRb.getVal ();
        String param = Biz.obToJson (accept);
        //test
//        url = url.replaceAll ("monitoringlocal.yujiejie.com", "192.168.10.105:8088");
//        param = param.replaceAll ("operatorlocal.yujiejie.com", "192.168.10.105:8084");
        LOGGER.info ("注册job信息 url:{}", url);
        LOGGER.info ("注册job信息 param:{}", param);
        ResponseResult responseResult = HttpRequest.sendPostJson (url, param);
        LOGGER.info ("注册job responseResult[{}]", responseResult == null ? null : responseResult.toString ());
        if (! responseResult.isSuccessful ()) {
            throw BizException.instance (GlobalsEnums.ADD_TIMER_FAILED).msg (responseResult.getMsg ());
        }
        return new MyLog<SalesVolumePlainRb> (plain, MyLog.Type.add, userSession).moreLog (accept, MyLog.Type.add).setData (plain);
    }

    /**
     * 组装一个请求数据
     *
     * @param plain plain
     * @author Aison
     * @date 2018/6/15 15:05
     */
    private QrtzJobsAccept postAccept(SalesVolumePlainRb plain) {

        DataDictionaryRb dataDictionaryRb = dataDictionaryServiceRb.getByCode ("salesVolumeFeedback", "salesVolumeFeedback");

        QrtzJobsAccept accept = new QrtzJobsAccept ();
        accept.setJobName (plain.getJobName ());
        accept.setJobGroup ("salesVolume");
        accept.setJobComment ("定时添加销量");
        accept.setJobType (1);
        accept.setFeedbackUrl (dataDictionaryRb.getVal ());
        String loop = "false";
        // 仅此一次
        if (plain.getExecuteType () == 1) {
            accept.setBeginDate (plain.getExecuteTimeBegin ());
            accept.setEndDate (plain.getExecuteTimeEnd ());
            accept.setEachTime (plain.getEachTime ());
        }
        else {
            //轮询
            Date now = new Date ();
            String datePix = Biz.formatDate (now, "yyyy-MM-dd");
            String dBegin = datePix + " " + plain.getExecuteTimeBegin ();
            // 一百年后
            String dEnd = Biz.formatDate (Biz.addDate (now, 24 * 365 * 100), "yyyy-MM-dd HH:mm:ss");
            accept.setBeginDate (dBegin);
            accept.setEndDate (dEnd);
            accept.setEachTime (plain.getEachTime ());
            loop = "true";
        }

        Map<String, Object> feedBack = new HashMap<> ();
        feedBack.put ("id", plain.getId ());
        feedBack.put ("comment", plain.getComment ());
        feedBack.put ("name", plain.getName ());
        feedBack.put ("eachTime", plain.getEachTime ());
        feedBack.put ("loop", loop);
        feedBack.put ("beginTime", plain.getExecuteTimeBegin ());
        feedBack.put ("endTime", plain.getExecuteTimeEnd ());

        StringBuilder stringBuilder = new StringBuilder ("{");
        StringBuilder finalStringBuilder = stringBuilder;
        feedBack.forEach ((key, val) -> {
            finalStringBuilder.append ("\"").append (key).append ("\":").append ("\"").append (val).append ("\",");
        });
        stringBuilder = stringBuilder.delete (stringBuilder.length () - 1, stringBuilder.length ());
        stringBuilder.append ("}");
        accept.setFeedbackData (stringBuilder.toString ());
        return accept;
    }


    /**
     * 修改策略
     *
     * @param plain       plain
     * @param userSession userSession
     * @return MyLog
     * @author Aison
     * @date 2018/6/15 11:03
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public MyLog<SalesVolumePlainRb> updateSalesVolumePlain(SalesVolumePlainRb plain, UserSession userSession) {

        if (Biz.hasEmpty (plain.getId (), plain.getStatus ())) {
            throw BizException.paramError ();
        }
        SalesVolumePlainRb oldPlan = salesVolumePlainRbMapper.selectByPrimaryKey (plain.getId ());
        if (oldPlan == null) {
            throw BizException.paramError ();
        }
        if (plain.getStatus () != 0) {
            throw BizException.def ().msg ("目前只有停止功能");
        }
        SalesVolumePlainRb plainNew = new SalesVolumePlainRb ();
        plainNew.setId (plain.getId ());
        plainNew.setStatus (plain.getStatus ());
        plainNew.setUpdateTime (new Date ());
        salesVolumePlainRbMapper.updateByPrimaryKeySelective (plainNew);


        DataDictionaryRb dataDictionaryRb = dataDictionaryServiceRb.getByCode ("simple", "quartz_server");
        QrtzJobsAccept accept = new QrtzJobsAccept ();
        accept.setJobName (oldPlan.getJobName ());
        accept.setJobGroup ("salesVolume");
        accept.setJobType (5);
        ResponseResult responseResult = HttpRequest.sendPostJson (dataDictionaryRb.getVal (), Biz.obToJson (accept));

        if (! responseResult.isSuccessful ()) {
            throw BizException.def ().msg ("停止定时任务失败 :" + responseResult.getMsg ());
        }
        return new MyLog<SalesVolumePlainRb> (oldPlan, plainNew, userSession).setData (plainNew);
    }

    /**
     * 分页
     *
     * @param query query
     * @return MyLog
     * @author Aison
     * @date 2018/6/19 11:05
     */
    @Override
    public MyPage<SalesVolumePlainRbQuery> plainPage(SalesVolumePlainRbQuery query) {

        List<SalesVolumePlainRb> salesVolumePlainRbs = salesVolumePlainRbMapper.selectList (query);
        // 计算lei'ji累积的
        return MyPage.copy2Child (salesVolumePlainRbs, SalesVolumePlainRbQuery.class, ((source, target) -> {
            //商品数量总数
            SalesVolumePlainDetailRbQuery detailQuery = new SalesVolumePlainDetailRbQuery ();
            detailQuery.setPlanId (source.getId ());
            Integer count = salesVolumePlainDetailRbMapper.selectCount (detailQuery);
            target.setProductCount (count.longValue ());
            // 累计销量
            Long allCount = salesVolumePlainDetailRbMapper.selectAllAddedCount (source.getId ());
            target.setAddedAllCount (allCount == null ? 0 : allCount);

            Map<String, Object> todayExpectCountAndDoneCount = salesVolumePlainDetailRbMapper.todayExpectCountAndDoneCount (source.getId ());
/*            // 今日预计刷量 只有是轮询的 且数量固定的才有
            target.setTodayExpectAllCount(count*source.getEachAddCount());
            // 今日已刷
            Long todayCount = salesVolumePlainDetailRbMapper.selectPlainAddedCount(source.getId());
            target.setTodayAddedAllCount(todayCount == null ? 0 : todayCount);*/

            //今日已刷
            target.setTodayAddedAllCount (Long.parseLong (todayExpectCountAndDoneCount.get ("doneCount").toString ()));
            //进入预刷量
            target.setTodayExpectAllCount (Long.parseLong (todayExpectCountAndDoneCount.get ("expectCount").toString ()));

            //如果没有计算出来数量 则先计算出来
            initEachAddCount (source);
        }));
    }

    /**
     * 初始化数量
     *
     * @param source source
     * @author Aison
     * @date 2018/6/19 14:15
     */
    @Override
    public void initEachAddCount(SalesVolumePlainRb source) {

        SalesVolumePlainRb plain = new SalesVolumePlainRb ();

        int type = source.getCountType ();
        // 如果是随机数量 且总数为空的话则先计算出来
        if (type == 1 && Biz.nullOrZero (source.getEachAddCount ())) {
            long exceptAddCount = doGetExceptAddCount (source, Boolean.FALSE);
            plain.setEachAddCount (exceptAddCount);
            plain.setId (source.getId ());
            salesVolumePlainRbMapper.updateByPrimaryKeySelective (plain);

            source.setEachAddCount (exceptAddCount);
        }
        else {
            salesVolumeFeedbackService.reInit (source);
        }
    }


    /**
     * 获取需要增长的销量数量
     *
     * @param source 销量策略
     * @param real   是否获取实际的增加刷量值
     *               当是随机刷销量时,实时计算随机值,否则返回最大值作为当天预刷销量
     * @return long
     * @author Charlie
     * @date 2018/8/29 21:51
     */
    static long doGetExceptAddCount(SalesVolumePlainRb source, boolean real) {
        if (source.getCountType ().equals (RANDOM_ADD)) {
            //刷量为随机量
            if (real) {
                Random random = new Random ();
                int max = source.getCountRandomMax ().intValue () + 1;
                int min = source.getCountRandomMini ().intValue ();
                return (long) (min + random.nextInt (max - min));
            }
            return source.getCountRandomMax ();
        }
        //刷量为固定量
        return source.getEachAddCount ();
    }


    /**
     * 增加销量,收藏数,评论数,浏览数...
     * <p>null则不操作</p>
     *
     * @param salesPdvInfo 需要 productId,productType
     * @param addInfo      如果不为null,则在原来的基础上加(而不是直接替换),是null则忽视
     * @return int
     * @author Charlie
     * @date 2018/8/5 23:40
     */
    @Override
    public int updOrAddSalesVolumeCount(SalesVolumeProductRb salesPdvInfo, SalesVolumeProductRb addInfo) {
        Declare.noNullParams (salesPdvInfo, salesPdvInfo.getProductId (), salesPdvInfo.getProductType (), addInfo);
        SalesVolumeProductRbQuery query = new SalesVolumeProductRbQuery ();
        query.setProductId (salesPdvInfo.getProductId ());
        query.setProductType (salesPdvInfo.getProductType ());
        SalesVolumeProductRb history = salesVolumeProductRbMapper.selectOne (query);

        //组装需要修改的信息
        SalesVolumeProductRb entity = SalesVolumeProductRbQuery.Builder.newInstance (
                salesPdvInfo.getProductId (),
                salesPdvInfo.getProductType (),
                addInfo.getSalesVolume (),
                addInfo.getCollectionCount (),
                addInfo.getStarCount (),
                addInfo.getOrderSuccessCount (),
                addInfo.getRefundCount (),
                addInfo.getOrderCount (),
                addInfo.getViewCount ()
        );
        if (Biz.isEmpty (history)) {
            //新增
            return salesVolumeProductRbMapper.insertSelective (entity);
        }
        else {
            //修改
            return salesVolumeProductRbMapper.safeAddCount (entity);
        }
    }


    /**
     * 回调修改商品销量
     *
     * @param id      id
     * @param comment comment
     * @author Aison
     * @date 2018/6/15 16:52
     */
    @Override
    @MyLogs( logInfo = "商品数量", model = ModelType.PRODUCT_MODEL )
    public MyLog<Long> feedbackPlain(Long id, String comment, UserSession optUser) {
        LOGGER.info ("回调修改商品销量start id[{}]", id);
        SalesVolumePlainRb plain = salesVolumePlainRbMapper.selectByPrimaryKey (id);
        if (plain == null) {
            throw BizException.paramError ();
        }
        if (plain.getStatus () != 1) {
            throw BizException.def ().msg ("此策略已经停止不能调用");
        }
        // 初始化要添加多少数量
        initEachAddCount (plain);

        List<SalesVolumePlainDetailRb> salesVolumePlainDetailRbs = salesVolumeFeedbackService.initExecute (plain);
        // 调用回调
        MyLog<Long> myLogAll = doFeedback (plain, salesVolumePlainDetailRbs);
        // 处理异常
        myLogAll.addAll (doException (plain));
        LOGGER.info ("回调修改商品销量success id[{}]", id);
        return myLogAll;
    }


    /**
     * 异常处理 每次回调的时候都处理一下异常
     *
     * @author Aison
     * @date 2018/6/19 10:24
     */
    private MyLog<Long> doException(SalesVolumePlainRb plain) {

        // 查询出未处理成功的 且处理次数小于10的
        SalesVolumePlainExceptionRbQuery query = new SalesVolumePlainExceptionRbQuery ();
        query.setDataStatus (0);
        query.setLastExecuteCount (10);
        // 查询出此条异常
        List<SalesVolumePlainExceptionRb> exceptionRbs = salesVolumePlainExceptionRbMapper.selectList (query);

        MyLog<Long> myLogAll = new MyLog<> ();
        exceptionRbs.forEach (action -> {
            // 查询出对应的detail
            SalesVolumePlainDetailRb detailRb = salesVolumePlainDetailRbMapper.selectByPrimaryKey (action.getDetailId ());
            Long count = getCount (plain, detailRb);
            try {
                if (count > 0) {
                    MyLog<Long> myLog = salesVolumeFeedbackService.doFeedback (detailRb, count);
                    myLogAll.addAll (myLog);
                }
            } catch (Exception e) {
                e.printStackTrace ();
                action.setExecuteResult (Biz.getFullException (e));
                action.setExceptCount (action.getExceptCount () + 1);
                salesVolumePlainExceptionRbMapper.updateByPrimaryKeySelective (action);
                myLogAll.moreLog (action.getId ().toString (), action, "处理异常失败 预计添加量为：" + count);
            }
        });
        return myLogAll;
    }

    /**
     * 调用回调
     *
     * @param salesVolumePlainDetailRbs salesVolumePlainDetailRbs
     * @author Aison
     * @date 2018/6/19 9:15
     */
    private MyLog<Long> doFeedback(SalesVolumePlainRb plain, List<SalesVolumePlainDetailRb> salesVolumePlainDetailRbs) {
        plain = salesVolumePlainRbMapper.selectByPrimaryKey (plain.getId ());
        // 调用处理逻辑
        MyLog<Long> myLogAll = new MyLog<> ();
        SalesVolumePlainRb finalPlain = plain;
        salesVolumePlainDetailRbs.forEach (action -> {
            // 记录日志
            Long count = getCount (finalPlain, action);
            try {
                if (count > 0) {
                    MyLog<Long> myLog = salesVolumeFeedbackService.doFeedback (action, count);
                    myLogAll.addAll (myLog);
                }
            } catch (Exception e) {
                e.printStackTrace ();
                SalesVolumePlainExceptionRb exceptionRb = new SalesVolumePlainExceptionRb ();
                exceptionRb.setCreateTime (new Date ());
                exceptionRb.setDataStatus (0);
                exceptionRb.setDetailId (action.getId ());
                exceptionRb.setExceptCount (count);
                exceptionRb.setPlanId (action.getPlanId ());
                salesVolumePlainExceptionRbMapper.insertSelective (exceptionRb);
                myLogAll.moreLog (action.getId ().toString (), action, "添加销量失败：预计添加量为:" + count + " 商品id:" + action.getProductId () + ": 商品类型:" + action.getProductType ());
            }
        });
        return myLogAll;
    }

    /**
     * 获取此次要添加多少量
     *
     * @param detail detail
     * @author Aison
     * @date 2018/6/15 20:23
     */
    private Long getCount(SalesVolumePlainRb plain, SalesVolumePlainDetailRb detail) {

        // 判断是否是最后一次
        String endTime = plain.getExecuteTimeEnd ();
        Integer eachTime = plain.getEachTime ();
        Date now = new Date ();
        Date end;
        if (plain.getExecuteType () == 0) {
            String datePix = Biz.formatDate (now, "yyyy-MM-dd");
            endTime = datePix + " " + endTime;
            end = Biz.dateStr2Date (endTime, "yyyy-MM-dd HH:mm:ss");
        }
        else {
            end = Biz.dateStr2Date (endTime, "yyyy-MM-dd HH:mm:ss");
        }
        // 如果结束时间和当前时间间隔小于每次调用间隔 则说明此次为最后一次调用了
        Long sub = Biz.subDate (now, end);
        Integer minute = Math.toIntExact (sub / 1000 / 60);
        if (minute < eachTime) {
            return detail.getLeftCount ();
        }

        Long count = detail.getAverageCount ();
        Long leftCount = detail.getLeftCount ();
        // 如果剩余的数量是平均值的 10% 则视为 零头.. 偏差了百分之10
        double point = 0.8;
        Double bl = (leftCount.doubleValue () / count.doubleValue ());
        if (bl < point) {
            return leftCount;
        }

        Long lastCount = detail.getLastCount ();
        Random rand = new Random ();
        double offset = ((double) (rand.nextInt (9) + 2)) / 100;
        if (count != 1) {
            if (lastCount != null) {
                count = Double.valueOf (lastCount > count ? count * (1 - offset) : count * (1 + offset)).longValue ();
            }
            else {
                count = Double.valueOf (count * (1 - offset)).longValue ();
            }
        }

        // 还剩下多少
        leftCount = leftCount - count;
        bl = (leftCount.doubleValue () / count.doubleValue ());
        // 如果剩余的数量是平均值的 10% 则视为 零头.. 偏差了百分之10
        if (bl < point) {
            return detail.getLeftCount ();
        }
        return count;
    }


}




