package com.jiuy.rb.service.impl.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.mapper.ReflectHelper;
import com.jiuy.base.model.Model;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.mapper.product.*;
import com.jiuy.rb.model.product.*;
import com.jiuy.rb.service.product.ISalesVolumeFeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 回调的时候的业逻辑
 * 拆分开来是为了做事务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/15 19:16
 * @Copyright 玖远网络
 */
@Service("salesVolumeFeedbackService")
public class SalesVolumeFeedbackImpl implements ISalesVolumeFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesVolumeFeedbackImpl.class);


    @Resource(name = "salesVolumePlainDetailRbMapper")
    private SalesVolumePlainDetailRbMapper salesVolumePlainDetailRbMapper;

    @Resource(name = "productRbMapper")
    private ProductRbMapper productRbMapper;

    @Resource(name = "restrictionActivityProductRbMapper")
    private RestrictionActivityProductRbMapper restrictionActivityProductRbMapper;

    @Resource(name = "salesVolumeProductRbMapper")
    private SalesVolumeProductRbMapper salesVolumeProductRbMapper;

    @Resource(name="salesVolumePlainExceptionRbMapper")
    private SalesVolumePlainExceptionRbMapper salesVolumePlainExceptionRbMapper;

    @Resource(name = "salesVolumePlainRbMapper")
    private SalesVolumePlainRbMapper salesVolumePlainRbMapper;


    /**
     * 初始化明细
     *
     * @param plain plain
     * @author Aison
     * @date 2018/6/15 16:58
     */
    @Override
    @Transactional(rollbackFor = Exception.class )
    public List<SalesVolumePlainDetailRb> initExecute(SalesVolumePlainRb plain) {


        // 查询批次详情列表
        SalesVolumePlainDetailRbQuery query = new SalesVolumePlainDetailRbQuery();
        query.setPlanId(plain.getId());
        List<SalesVolumePlainDetailRb> salesVolumePlainDetailRbs = salesVolumePlainDetailRbMapper.selectList(query);

        // 如果没有初始化 则初始化
        if(salesVolumePlainDetailRbs == null || salesVolumePlainDetailRbs.size()==0) {
            doInit(plain);
        } else {
            // 如果已经初始化了  则判断是否是循环的调用 如果是循环调用则判断初始化时间是否是昨天..
            // 如果是昨天的则 初始化数据..添加累计刷量
            reInit(plain);
        }
        return salesVolumePlainDetailRbMapper.selectList(query);
    }


    /**
     * 重新初始化
     * <P>
     *     如果是一次执行,不做任何操作,如果是轮询,更新初始化日期,如果是随机刷数量,还会更新预刷数量
     * </P>
     * @param plain plain
     * @author Aison
     * @date 2018/6/25 10:27
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reInit(SalesVolumePlainRb plain) {
        // 是轮询且已经初始化过了
        // 如果初始化时间不不一致则更新
        if(isTodayTaskNotYetExecute (plain)){
            /*
             * 随机增长和固定增长的预刷量不一样的,随机增长的在每次轮询的时候新生成随机种子初始化每个plainDetail
             */
            if (plain.getCountType ().equals (SalesVolumeServiceImpl.RANDOM_ADD)) {
                //刷随机销量
                SalesVolumePlainDetailRbQuery query = new SalesVolumePlainDetailRbQuery ();
                query.setPlanId (plain.getId ());
                List<SalesVolumePlainDetailRb> plainDetailList = salesVolumePlainDetailRbMapper.selectList (query);
                if (! ObjectUtils.isEmpty (plainDetailList)) {
                    for (SalesVolumePlainDetailRb detail : plainDetailList) {
                        long randomExceptAddCount = SalesVolumeServiceImpl.doGetExceptAddCount (plain, Boolean.TRUE);
                        Double avg = countAvg(plain, Boolean.TRUE, randomExceptAddCount);
                        //一个一个更新
                        int rs = salesVolumePlainDetailRbMapper.reInitDetailByDetailId(detail.getId (),avg,randomExceptAddCount);
                        if(rs == 0) {
                            throw BizException.def().msg("重新初始化失败");
                        }
                    }
                }
            }
            else if (plain.getCountType ().equals (SalesVolumeServiceImpl.FIX_ADD)) {
                //刷固定销量
                Double avg = countAvg(plain,true, null);
                //批量更新
                int rs = salesVolumePlainDetailRbMapper.reInitDetail(plain.getId(),avg,plain.getEachAddCount());
                if(rs == 0) {
                    throw BizException.def().msg("重新初始化失败");
                }
            }

            //记录这次轮询刷量时间
            SalesVolumePlainRb plainRb = new SalesVolumePlainRb();
            plainRb.setInitDate(new Date());
            plainRb.setId(plain.getId());
            salesVolumePlainRbMapper.updateByPrimaryKeySelective(plainRb);
        }
    }



    /**
     * 重新初始化
     * <P>
     *     如果是一次执行,不做任何操作,如果是轮询,更新初始化日期,如果是随机刷数量,还会更新预刷数量
     * </P>
     * @param plain plain
     * @see SalesVolumeFeedbackImpl#reInit(com.jiuy.rb.model.product.SalesVolumePlainRb)
     * @author Aison
     * @date 2018/6/25 10:27
     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
/*    @Deprecated
    public void reInit_历史文件(SalesVolumePlainRb plain) {
        // 是轮询且已经初始化过了
        Date date = plain.getInitDate();
        if(plain.getExecuteType() == 0 && date!=null) {
            String initDay = Biz.formatDate(date,"yyyy-MM-dd");
            String today = Biz.formatDate(new Date(),"yyyy-MM-dd");
            // 如果初始化时间不不一致则更新
            if(!today.equals(initDay)){
                int type = plain.getCountType();
                // 如果是随机数 则重新计算量
                if(type == 1 ) {
                    Random random = new Random();
                    int max = plain.getCountRandomMax().intValue()+1;
                    int min = plain.getCountRandomMini().intValue();
                    Integer exceptAddCount = min+random.nextInt(max-min);
                    plain.setEachAddCount(exceptAddCount.longValue());
                }

                Double avg = countAvg(plain,true, null);
                int rs = salesVolumePlainDetailRbMapper.reInitDetail(plain.getId(),avg,plain.getEachAddCount());
                SalesVolumePlainRb plainRb = new SalesVolumePlainRb();
                plainRb.setInitDate(new Date());
                plainRb.setId(plain.getId());
                plainRb.setEachAddCount(plain.getEachAddCount());
                salesVolumePlainRbMapper.updateByPrimaryKeySelective(plainRb);
                if(rs == 0) {
                    throw BizException.def().msg("重新初始化失败");
                }

            }
        }
    }*/






    /**
     * 轮询的刷量,今天的刷量任务是否还没执行
     *
     * @param plain 刷量任务
     * @return true 还没执行
     * @author Charlie
     * @date 2018/8/29 22:47
     */
    private boolean isTodayTaskNotYetExecute(SalesVolumePlainRb plain) {
        Date date = plain.getInitDate ();
        if (plain.getExecuteType () == 0 && date != null) {
            String initDay = Biz.formatDate(date,"yyyy-MM-dd");
            String today = Biz.formatDate(new Date(),"yyyy-MM-dd");
            return ! today.equals (initDay);
        }
        return false;
    }


    /**
     * 初始化
     *
     * @param plain plain
     * @author Aison
     * @date 2018/6/19 10:34
     */
    private void doInit(SalesVolumePlainRb plain) {
        LOGGER.info ("刷销量,初始化doInit plainId[{}]", plain.getId ());
        Integer queryType = plain.getProductQueryType();
        String queryDetail = plain.getProductQueryDetail();
        JSONObject queryArgs = JSONObject.parseObject(queryDetail);
        List<Model> target = new ArrayList<>();
        // 指定商品
        if (queryType == 1) {
            Integer productType =  queryArgs.getInteger("type");
            List<String> ids = extractProductIds (queryArgs.getJSONArray("ids"));
            LOGGER.info ("指定商品刷销量 productIds[{}]", ids);
            switch (productType) {
                case 1:
                    RestrictionActivityProductRbQuery query = new RestrictionActivityProductRbQuery();
                    query.needKeys("id");
                    query.setIds(ids);
                    List<RestrictionActivityProductRb> restrictionActivityProductRbs = restrictionActivityProductRbMapper.selectList(query);
                    target.addAll(restrictionActivityProductRbs);
                    break;
                case 2:
                    ProductRbQuery queryProduct = new ProductRbQuery();
                    queryProduct.needKeys("id");
                    queryProduct.setIds(ids);
                    List<ProductRb> productRbs = productRbMapper.selectList(queryProduct);
                    target.addAll(productRbs);
                    break;
                default:
                    break;
            }
        } else if (queryType == 2) {
            if(queryArgs.size()>0){
                // 查询条件
                List<ProductRb> productRbs =productRbMapper.selectByMap(queryArgs);
                List<RestrictionActivityProductRb> activityProductRbs = restrictionActivityProductRbMapper.selectFilterProduct(queryArgs);
                target.addAll(productRbs);
                target.addAll(activityProductRbs);
            }
        }

        if(target.size()==0) {
            throw BizException.def().msg("查询不到商品信息");
        }
        // 初始化 每一个商品是一条数据
        List<SalesVolumePlainDetailRb> finalSalesVolumePlainDetailRbs = new ArrayList<>();
        target.forEach(action->{
            SalesVolumePlainDetailRb plainDetail = new SalesVolumePlainDetailRb();
            plainDetail.setCreateTime(new Date());
            plainDetail.setPlanId(plain.getId());
            plainDetail.setProductId(Long.valueOf(ReflectHelper.getFieldValue(action,"id").toString()));
            plainDetail.setAddedCount(0L);
            long exceptAddCount = SalesVolumeServiceImpl.doGetExceptAddCount (plain, Boolean.TRUE);
            plainDetail.setExceptCount (exceptAddCount);
            plainDetail.setLeftCount(exceptAddCount);
            Double finalAverage = countAvg(plain,false, exceptAddCount);
            plainDetail.setAverageCount(finalAverage.longValue());
            if(action instanceof ProductRb){
                plainDetail.setProductType(1);
            } else if(action instanceof RestrictionActivityProductRb){
                plainDetail.setProductType(2);
            }
            finalSalesVolumePlainDetailRbs.add(plainDetail);
        });

        Integer size = finalSalesVolumePlainDetailRbs.size();
        // 更新初始化时间
        SalesVolumePlainRb plainNew = new SalesVolumePlainRb();
        plainNew.setInitDate(new Date());
        plainNew.setId(plain.getId());
        plainNew.setProductCount(size.longValue());
        salesVolumePlainRbMapper.updateByPrimaryKeySelective(plainNew);

        salesVolumePlainDetailRbMapper.insertBach(finalSalesVolumePlainDetailRbs);
    }

    /**
     * 提取商品id
     *
     * @param jsonArray jsonArray
     * @return java.util.List<java.lang.String>
     * @author Charlie
     * @date 2018/9/4 9:33
     */
    private List<String> extractProductIds(JSONArray jsonArray) {
        //组装商品id
        //兼容','分隔
        List<String> temp = new ArrayList<> ();
        jsonArray.forEach(action->{
            String idsStr = action.toString ();
            String[] idArr = idsStr.split (",");
            if (!ObjectUtils.isEmpty (idArr)) {
                for (String i : idArr) {
                    if (StringUtils.isNotBlank (i)) {
                        temp.add(i.trim ());
                    }
                }
            }
        });

        //现在改成';'分隔
        ArrayList<String> ids = new ArrayList<> ();
        temp.forEach (id->{
            String[] idArr = id.split (";");
            if (!ObjectUtils.isEmpty (idArr)) {
                for (String i : idArr) {
                    if (StringUtils.isNotBlank (i)) {
                        ids.add(i.trim ());
                    }
                }
            }
        });
        return ids;
    }

    /**
     * 计算平均值
     * @param plain plain
     * @param isReInit isReInit 是否是重新计算
     * @param exceptAddCount 策略详情中记录的每个商品需要加的销量数量,当plain.getCountType ()==1时,必填
     * @author Aison
     * @date 2018/6/23 15:51
     */
    private Double countAvg(SalesVolumePlainRb plain, boolean isReInit, Long exceptAddCount) {
        String strBegin = plain.getExecuteTimeBegin();
        String strEnd = plain.getExecuteTimeEnd();
        Date now = new Date();
        if(plain.getExecuteType() == 0){
            String datePix = Biz.formatDate(now,"yyyy-MM-dd");
            strBegin = datePix +" "+ plain.getExecuteTimeBegin();
            strEnd = datePix +" "+ plain.getExecuteTimeEnd();
        }

        Date begin = Biz.dateStr2Date(strBegin,"yyyy-MM-dd HH:mm:ss");
        if(!isReInit) {
            // 如果是当前时间之前的一个时间
            if(begin.before(now)) {
                begin = now;
            }
        }
        Date end = Biz.dateStr2Date(strEnd,"yyyy-MM-dd HH:mm:ss");
        // 计算平均值
        Long subTime = Biz.subDate(begin,end);
        Long mini = subTime/1000/60;
        Double count = Math.floor(mini/plain.getEachTime());
        //获取预刷量: 如果是固定刷量,则直接取plain.getEachAddCount(),如果是随机刷量,每个plainDetail的随机增长销量是不一样的,需要自己指定
        Long eachAddCount = null;
        if (plain.getCountType ().equals (SalesVolumeServiceImpl.FIX_ADD)) {
            eachAddCount = plain.getEachAddCount ();
        }
        else if (plain.getCountType ().equals (SalesVolumeServiceImpl.RANDOM_ADD)) {
            eachAddCount = exceptAddCount;
        }
        if (eachAddCount == null) {
            throw BizException.def().msg("计算刷量平均值,预刷量是空");
        }

        Double average =  (eachAddCount/count);
        // 如果平均值小于 1 默认为 1
        if(average<1) {
            average = 1D;
        }
        return average;
    }

    /**
     * 处理回调
     *
     * @param detailRb plainRb
     * @author Aison
     * @date 2018/6/15 19:22
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MyLog<Long> doFeedback(SalesVolumePlainDetailRb detailRb, Long count) {

       // 查询目标销量记录数据
       Long productId = detailRb.getProductId();
       Integer type =  detailRb.getProductType();
        // 更新数据
        SalesVolumeProductRbQuery query = new SalesVolumeProductRbQuery();
        query.setProductId(productId);
        query.setProductType(type);
        SalesVolumeProductRb salesVolumeProductRb = salesVolumeProductRbMapper.selectOne(query);

        int rs ,upRs;
        if(salesVolumeProductRb==null) {
            salesVolumeProductRb = new SalesVolumeProductRb();
            salesVolumeProductRb.setSalesVolume(count);
            salesVolumeProductRb.setProductId(productId);
            salesVolumeProductRb.setProductType(type);
            rs = salesVolumeProductRbMapper.insertSelective(salesVolumeProductRb);
        } else {
            rs =  salesVolumeProductRbMapper.addSalesVolume(count,detailRb.getProductType(),detailRb.getProductId());
        }
        // 修改最后一次修改数量
        upRs = salesVolumePlainDetailRbMapper.subLeftCount(count,count,detailRb.getId());
        if(upRs==0 || rs ==0 ) {
            throw BizException.instance(GlobalsEnums.UP_LEFT_COUNT_FAILED);
        }
        SalesVolumeProductRb newProduct = salesVolumeProductRbMapper.selectByPrimaryKey(salesVolumeProductRb.getId());
        return new MyLog<>(salesVolumeProductRb,newProduct,null);
    }



}
