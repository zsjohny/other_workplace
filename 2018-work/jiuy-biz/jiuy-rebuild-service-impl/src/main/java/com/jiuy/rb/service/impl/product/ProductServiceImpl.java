package com.jiuy.rb.service.impl.product;

import com.alibaba.fastjson.JSON;
import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.enums.EMPTY;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.ProductAuditEnum;
import com.jiuy.rb.enums.ProductStateEnum;
import com.jiuy.rb.mapper.product.ProductAuditRbMapper;
import com.jiuy.rb.mapper.product.ProductRbMapper;
import com.jiuy.rb.mapper.product.RestrictionActivityProductRbMapper;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.product.*;
import com.jiuy.rb.service.product.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  商品相关业务的实现类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/13 9:44
 * @Copyright 玖远网络
 */
@Service("productService")
public class ProductServiceImpl implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductAuditRbMapper productAuditRbMapper;

    @Autowired
    private ProductRbMapper productRbMapper;

    @Autowired
    private RestrictionActivityProductRbMapper restrictionActivityProductRbMapper;

    /**
     * 通过id获取一个商品
     *
     * @param productId productId
     * @author Aison
     * @date 2018/6/21 13:52
     */
    @Override
    public ProductRb getById(Long productId) {
        return productRbMapper.selectByPrimaryKey(productId);
    }

    /**
     *  查询一个 list
     *
     * @param query query
     * @author Aison
     * @date 2018/7/6 15:24
     * @return java.util.List<com.jiuy.rb.model.product.ProductRb>
     */
    @Override
    public List<ProductRb> getList(ProductRbQuery query) {

        return productRbMapper.selectList(query);
    }

    /**
     * 获取活动商品
     *
     * @param activityId activityId
     * @author Aison
     * @date 2018/6/21 13:58
     */
    @Override
    public RestrictionActivityProductRb getActivityProductById(Long activityId) {

        return restrictionActivityProductRbMapper.selectByPrimaryKey(activityId);
    }

    /**
     * 获取供应商id
     *
     * @param brandId brandId
     * @author Aison
     * @date 2018/8/6 10:41
     * @return java.lang.Long
     */
    @Override
    public Long getSupplierId(Long brandId) {
        return productRbMapper.getSupplierId(brandId);
    }


    /**
     * 审核通过商品
     *
     * @param productAuditId 审核主键
     * @param userSession 用户
     * @author Aison
     * @date 2018/6/13 9:46
     * @return MyLog 返回日志
     */
    @Override
    @MyLogs(logInfo = "审核商品",model = ModelType.PRODUCT_MODEL)
    @Transactional(rollbackFor = Exception.class)
    public MyLog<Long> auditProduct(Long productAuditId, UserSession userSession) {

        // 判断当前审核记录的状态
       ProductAuditRb productAuditRb =  productAuditRbMapper.selectByPrimaryKey(productAuditId);
       if(productAuditRb == null) {
            throw BizException.paramError();
       }
       ProductRb old = productRbMapper.selectByPrimaryKey(productAuditRb.getProductId());
       if(old == null) {
           throw BizException.paramError();
       }

       Integer code = productAuditRb.getAuditState();
       boolean isServerWait = ProductAuditEnum.SERVER_WAIT.isThis(code);
       boolean isBuyerWait = ProductAuditEnum.BUYER_WAIT.isThis(code);
       // 如果即不是客服待审核 也不是买手待审核则抛出异常
       if(!isServerWait && !isBuyerWait) {
          throw BizException.instance(GlobalsEnums.PRODUCT_STATE_ERROR);
       }

        MyLog<Long> myLog = new MyLog<>(userSession);
        Long time = System.currentTimeMillis();
        ProductAuditRb productAuditRbNew = new ProductAuditRb();
        productAuditRbNew.setId(productAuditId);
        productAuditRbNew.setAuditTime(time);
        productAuditRbNew.setUpdateTime(time);
       // 客服待审核
       if(isServerWait) {
           // 生成买手审核记录
           addBuyerProductAudit(productAuditRb);
           // 修改本条记录
           productAuditRbNew.setAuditState(ProductAuditEnum.SERVER_PASS.getCode());
       }
       // 买手待审核
       if(isBuyerWait) {
           // 修改商品的状态到待上架
           ProductRb product = new ProductRb();
           product.setId(productAuditRb.getProductId());
           product.setState(ProductStateEnum.WAIT_UP_SOLD.getCode());
           product.setAuditTime(System.currentTimeMillis());
           productRbMapper.updateByPrimaryKeySelective(product);
           //修改本审核记录状态
           productAuditRbNew.setAuditState(ProductAuditEnum.BUYER_PASS.getCode());
           //自动上架
           autoPutaway(productAuditRb.getProductId(), productAuditRb.getSupplierId());
           // 添加商品的修改记录
           myLog.moreLog(old,product,MyLog.Type.up);
       }
        // 添加日志
        myLog.moreLog(productAuditRb,productAuditRbNew,MyLog.Type.up);
        productAuditRbMapper.updateByPrimaryKeySelective(productAuditRbNew);
        return myLog;
    }




    /**
     * 自动上架
     * @param productId 商品id
     * @param supplierId 供应商id
     * @author Aison
     * @date 2018/6/13 10:47
     */
    private void autoPutaway(Long productId,Long supplierId) {


    }

    /**
     * 生成买手审核记录
     *
     * @param productAudit 客服审核记录
     */
    private void addBuyerProductAudit(ProductAuditRb productAudit) {

        ProductAuditRb newProductAudit = new ProductAuditRb();
        //审核状态：0待审核、1已通过、2已拒绝
        newProductAudit.setAuditState(ProductAuditEnum.BUYER_WAIT.getCode());
        //商家ID
        newProductAudit.setSupplierId( productAudit.getSupplierId());
        //品牌ID
        newProductAudit.setBrandId( productAudit.getBrandId());
        //品牌名称
        newProductAudit.setBrandName(productAudit.getBrandName());
        //品牌Logo
        newProductAudit.setBrandLogo(productAudit.getBrandLogo());
        //商品ID
        newProductAudit.setProductId(productAudit.getProductId());
        //商商品名称
        newProductAudit.setProductName(productAudit.getProductName());
        //商品款号
        newProductAudit.setClothesNumber(productAudit.getClothesNumber());
        //橱窗图片集合，英文逗号分隔
        newProductAudit.setShowcaseImgs(productAudit.getShowcaseImgs());
        //详情图片集合，英文逗号分隔
        newProductAudit.setDetailImgs(productAudit.getDetailImgs());
        //商品视频url
        newProductAudit.setVideoUrl(productAudit.getVideoUrl());
        //商品主图
        newProductAudit.setMainImg(productAudit.getMainImg());
        //商品品类名称，只用于显示，格式例：裙装 -> 连衣裙
        newProductAudit.setCategoryName(productAudit.getCategoryName());
        //商品SKUJSON
        newProductAudit.setSkuJSON(productAudit.getSkuJSON());
        //搭配商品ID集合，英文逗号分隔
        newProductAudit.setMatchProductIds(productAudit.getMatchProductIds());
        //阶梯价格JSON
        newProductAudit.setLadderPriceJson(productAudit.getLadderPriceJson());
        // 最大阶梯价格
        newProductAudit.setMaxLadderPrice(productAudit.getMaxLadderPrice());
        //最小阶梯价格
        newProductAudit.setMinLadderPrice(productAudit.getMinLadderPrice());
        //提交审核时间
        newProductAudit.setSubmitAuditTime(productAudit.getSubmitAuditTime());
        newProductAudit.setAuditTime(0L);
        long time = System.currentTimeMillis();
        //创建时间
        newProductAudit.setCreateTime(time);
        //更新时间
        newProductAudit.setUpdateTime(time);
        productAuditRbMapper.insertSelective(newProductAudit);
        logger.info("生成买手审核记录完成");
    }



    /**
     * 审核不通过
     *
     * @param productAuditId 审核id
     * @param noPassReason 审核原因
     * @author Aison
     * @date 2018/6/13 13:29
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @MyLogs(logInfo = "审核商品",model = ModelType.PRODUCT_MODEL)
    public MyLog<Long> productAuditNoPass(Long productAuditId, String noPassReason,UserSession userSession) {

        ProductAuditRb audit = productAuditRbMapper.selectByPrimaryKey(productAuditId);
        if(audit == null) {
            throw BizException.paramError();
        }
        ProductRb productRb = productRbMapper.selectByPrimaryKey(audit.getProductId());
        if(productRb == null) {
            throw BizException.paramError();
        }
        // 审核状
        int auditState = audit.getAuditState();
        // 判断状态 不是待审核的都是错误的
        if(!ProductAuditEnum.SERVER_WAIT.isThis(auditState) && !ProductAuditEnum.BUYER_WAIT.isThis(auditState)) {
            throw BizException.instance(GlobalsEnums.PRODUCT_STATE_ERROR);
        }

        //1、修改本审核记录状态
        ProductAuditRb productAudit = new ProductAuditRb();
        productAudit.setId(productAuditId);
        productAudit.setAuditTime(System.currentTimeMillis());
        noPassReason = Biz.hasEmpty(noPassReason) ? "不通过" : noPassReason;
        productAudit.setNoPassReason(noPassReason);

        //客服待审核
        if(ProductAuditEnum.SERVER_WAIT.isThis(auditState)){
            productAudit.setAuditState(ProductAuditEnum.SERVER_NOT_PASS.getCode());
        }else if(ProductAuditEnum.BUYER_WAIT.isThis(auditState)){
            // 买手审核
            productAudit.setAuditState(ProductAuditEnum.BUYER_NOT_PASS.getCode());
        } else {
            throw BizException.instance(GlobalsEnums.PRODUCT_STATE_ERROR);
        }
        productAuditRbMapper.updateByPrimaryKeySelective(productAudit);

        //2、修改商品状态为拒绝
        Long productId = audit.getProductId();
        ProductRb product = new ProductRb();
        product.setId(productId);
        product.setState(ProductStateEnum.AUDIT_NO_PASS.getCode());
        product.setAuditTime(System.currentTimeMillis());
        product.setAuditNoPassReason(noPassReason);
        logger.info("商品审核不通过修改商品信息product:" + JSON.toJSONString(product));
        productRbMapper.updateByPrimaryKeySelective(product);

        return new MyLog<Long>(userSession).moreLog(productRb,product,MyLog.Type.up).moreLog(audit,productAudit,MyLog.Type.up);
    }


    /**
     * 获取审核详情（展示详情）
     *
     * @param productAuditId productAuditId
     * @author Aison
     * @date 2018/6/13 15:06
     */
    @Override
    public ProductAuditRbQuery getProductAuditInfo(Long productAuditId) {

        ProductAuditRb audit = productAuditRbMapper.selectByPrimaryKey(productAuditId);

        ProductAuditRbQuery productAuditRbQuery = new ProductAuditRbQuery();
        Biz.copyBean(audit,productAuditRbQuery);

        List<Map<String,String>> matchProductList = new ArrayList<Map<String,String>>();
        //搭配推荐商品ID集合，英文逗号分隔
        String matchProductIds = audit.getMatchProductIds();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(matchProductIds)){
            String[] productIdArr = matchProductIds.split(",");
            for(String togetherProductId : productIdArr){
                ProductRb togetherProduct = productRbMapper.selectByPrimaryKey(Long.parseLong(togetherProductId));
                Map<String,String> togetherProductMap = new HashMap<String,String>();
                //商品ID
                togetherProductMap.put("productId", String.valueOf(togetherProduct.getId()));
                //商品款号
                togetherProductMap.put("clothesNumber", togetherProduct.getClothesNumber());
                //商品名称
                togetherProductMap.put("name", togetherProduct.getName());
                //商品主图
                togetherProductMap.put("mainImg", togetherProduct.getMainImg());
                //阶梯价格JSON
                togetherProductMap.put("ladderPriceJson", togetherProduct.getLadderPriceJson());
                //最大阶梯价格
                togetherProductMap.put("maxLadderPrice", String.valueOf(togetherProduct.getMaxLadderPrice()));
                //最小阶梯价格
                togetherProductMap.put("minLadderPrice", String.valueOf(togetherProduct.getMinLadderPrice()));
                togetherProductMap.put("vedioMain",togetherProduct.getVedioMain());
                matchProductList.add(togetherProductMap);
            }
        }
        //搭配推荐商品列表
        productAuditRbQuery.setMatchProductList(matchProductList);

        return productAuditRbQuery;
    }

    /**
     * 审核数据列表
     *
     * @param productAuditRbQuery productAuditRbQuery
     * @author Aison
     * @date 2018/6/13 15:33
     */
    @Override
    public MyPage<ProductAuditRbQuery> productAuditList(ProductAuditRbQuery productAuditRbQuery) {

       List<ProductAuditRb> productAuditRbs =  productAuditRbMapper.selectList(productAuditRbQuery);
       return MyPage.copy2Child(productAuditRbs,ProductAuditRbQuery.class,null);
    }


    /**
     * 增加商品销量
     *
     * @param productId productId
     * @param count 增加的销量
     * @return int
     * @author Charlie
     * @date 2018/8/6 9:20
     */
    @Override
    public int addSaleTotalCount(Long productId, Integer count) {
        Declare.noNullParams (productId, count);
        ProductRb product = productRbMapper.selectByPrimaryKey (productId);
        Declare.existResource (product);
        return productRbMapper.addSaleTotalCount (productId, count);
    }

    /**
     *描述 根据供应商id获取供应商下面的产品
     * @author hyq
     * @date 2018/8/13 17:14
     * @return java.util.List<com.jiuy.rb.model.product.ProductRbQuery>
     */
    @Override
    public Map<String,Object> getProductByBrandId(ProductRbQuery query) {


        //MyPage<ProductRbQuery> productRbQueryMyPage = new MyPage<>(productRbMapper.getProductByBranIds(query));
        MyPage<ProductRbQuery> productRbQueryMyPage = new MyPage<>(productRbMapper.getProductByProductIds(query));

        return getCounponProductList(productRbQueryMyPage);

    }

    /**
     *描述 封装参数
     ** @param productRbQueryMyPage
     * @author hyq
     * @date 2018/8/14 18:00
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String,Object> getCounponProductList(MyPage<ProductRbQuery> productRbQueryMyPage){

        List<Map<String, Object>> retMap = new ArrayList<>();

        for(ProductRbQuery product : productRbQueryMyPage.getRows()){

            Map<String, Object> map = new HashMap<>();
            map.put("campaignImage", product.getMainImg());
            map.put("salesVolume", product.getSalesVolume());
            map.put("name", product.getName());
            map.put("saleTotalCount", product.getSaleTotalCount());
            map.put("minLadderPrice", product.getMinLadderPrice());
            map.put("brandId", product.getBrandId());
            map.put("productId", product.getId());
            retMap.add(map);
        }


        MyPage<Map<String, Object>> retPage = new MyPage<>();
        retPage.setTotal(productRbQueryMyPage.getTotal());
        retPage.setCPage(productRbQueryMyPage.getCPage());
        retPage.setPageSize(productRbQueryMyPage.getPageSize());
        retPage.setPageNum(productRbQueryMyPage.getPageNum());
        retPage.setRows(retMap);


        Map<String,Object> resMap = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();


        Integer pageNum = productRbQueryMyPage.getPageNum();
        pageNum = pageNum/productRbQueryMyPage.getPageSize()  + 1;
        queryMap.put("page",pageNum);
        queryMap.put("pageSize",productRbQueryMyPage.getPageSize());
        queryMap.put("recordCount",productRbQueryMyPage.getTotal());
        queryMap.put("maxRecordCount",20000);
        queryMap.put("pageCount",productRbQueryMyPage.getPages());
        queryMap.put("more", pageNum<productRbQueryMyPage.getPages());
        resMap.put("pageQuery",queryMap);

        resMap.put("productList",retMap);

        return resMap;
    }

    /**
     *描述 通过关键字查询优惠券里面的指定商品
     ** @param query
     * @author hyq
     * @date 2018/8/14 18:02
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> getProductByKeyWords(ProductRbQuery query) {

        //查询品牌和关键字下面的商品id
        List<ProductRbQuery> productByKeyWord = productRbMapper.getProductByKeyWord(query);

        MyPage<ProductRbQuery> productRbQueryMyPage = new MyPage<>(productByKeyWord);

        if(productByKeyWord ==null || productByKeyWord.size()==0){
            Map<String,Object> resMap = new HashMap<>();
            Map<String,Object> queryMap = new HashMap<>();


            Integer pageNum = productRbQueryMyPage.getPageNum();
            pageNum = pageNum/productRbQueryMyPage.getPageSize()  + 1;
            queryMap.put("page",pageNum);
            queryMap.put("pageSize",productRbQueryMyPage.getPageSize());
            queryMap.put("recordCount",productRbQueryMyPage.getTotal());
            queryMap.put("maxRecordCount",20000);
            queryMap.put("pageCount",productRbQueryMyPage.getPages());
            queryMap.put("more", pageNum<productRbQueryMyPage.getPages());
            resMap.put("pageQuery",queryMap);

            List<Map<String, Object>> retMap = new ArrayList<>();
            resMap.put("productList",retMap);
            return resMap;
        }

        return getCounponProductList(productRbQueryMyPage);

//        List<String> ids = new ArrayList<>();
//        productRbQueryMyPage.getRows().forEach(productRbQuery -> {
//            ids.add(productRbQuery.getId()+"");
//        });
//
//        ProductRbQuery newQuery = new ProductRbQuery();
//        newQuery.setIds(ids);
//        newQuery.setLimit(query.getLimit());
//        newQuery.setOffset(query.getOffset());
//
//        //通过商品id查询商品
//        MyPage<ProductRbQuery> productRbQueryMyPageNew = new MyPage<>(productRbMapper.getProductByProductIds(newQuery));

//        return getCounponProductList(productRbQueryMyPageNew);
    }


    /**
     * 验证商品是否合法
     *
     * @param productIds productIds
     * @author Charlie
     * @date 2018/9/4 9:58
     */
    @Override
    public Map<String, Object> verifyProductIds(String productIds) {
        Map<String, Object> result = new HashMap<> (2);
        result.put ("isFull", true);
        result.put ("allUpSold", false);

        List<String> productIdList = extractProductIds (productIds);
        if (ObjectUtils.isEmpty (productIdList)) {
            return result;
        }
        ProductRbQuery query = new ProductRbQuery ();
        query.setIds (productIdList);
        List<ProductRb> productList = productRbMapper.selectList (query);
        boolean isFull = productIdList.size () == productList.size ();
        if (!isFull) {
            //有未找到商品
            result.put ("isFull", false);
            return result;
        }
        for (ProductRb product : productList) {
            if (!ProductStateEnum.UP_SOLD.isThis (product.getState ())) {
                //有商品未上架
                result.put ("allUpSold", false);
                return result;
            }
        }
        result.put ("allUpSold", true);
        return result;
    }

    @Override
    public Map<String, Object> verifyActivityProductIds(String productIds) {
        Map<String, Object> result = new HashMap<> (2);
        result.put ("isFull", true);
        result.put ("allUpSold", false);

        List<String> productIdList = extractProductIds (productIds);
        if (ObjectUtils.isEmpty (productIdList)) {
            return result;
        }

        RestrictionActivityProductRbQuery query = new RestrictionActivityProductRbQuery ();
        query.setIds(productIdList);
        List<RestrictionActivityProductRb> activityList = restrictionActivityProductRbMapper.selectList (query);
        boolean isFull = productIdList.size () == activityList.size ();
        if (!isFull) {
            //有未找到商品
            result.put ("isFull", false);
            return result;
        }
        for (RestrictionActivityProductRb activity : activityList) {
            if (!ObjectUtils.nullSafeEquals (activity.getProductStatus (), 1)) {
                //有活动商品未上架
                result.put ("allUpSold", false);
                return result;
            }
        }
        result.put ("allUpSold", true);
        return result;
    }

    @Override
    public String findSummaryImages(Long productId) {
        String images = productRbMapper.findSummaryImages(productId);
        return images;
    }


    /**
     * 提取商品id
     *
     * @return java.util.List<java.lang.String>
     * @author Charlie
     * @date 2018/9/4 9:33
     */
    private List<String> extractProductIds(String idArray) {
        if (StringUtils.isBlank (idArray)) {
            return EMPTY.list ();
        }
        //兼容','分隔

        String[] idArr = idArray.split (",");
        List<String> temp = new ArrayList<> ();
        if (!ObjectUtils.isEmpty (idArr)) {
            for (String i : idArr) {
                if (StringUtils.isNotBlank (i)) {
                    temp.add(i.trim ());
                }
            }
        }

        //现在改成';'分隔
        ArrayList<String> ids = new ArrayList<> ();
        for (String id : temp) {
            idArr = id.split (";");
            if (!ObjectUtils.isEmpty (idArr)) {
                for (String i : idArr) {
                    if (StringUtils.isNotBlank (i)) {
                        ids.add(i.trim ());
                    }
                }
            }
        }
        return ids;
    }

}
