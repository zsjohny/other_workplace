/**
 *
 */
package com.jiuyuan.service.common;

import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.dao.mapper.supplier.GlobalSettingMapper;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductSkuMapper;
import com.jiuyuan.entity.newentity.GlobalSettingNew;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.RestrictionActivityProductSku;
import com.jiuyuan.util.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 新商品SKU服务
 */

@Service
public class ActivityProductService implements IActivityProductService{

    private static final Log logger = LogFactory.get("ActivityProductService");

    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;

    @Autowired
    private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;

    @Autowired
    private ProductNewMapper productNewMapper;

    @Autowired
    private ProductDetailMapper productDetailMapper;

    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;

    @Autowired
    private GlobalSettingMapper globalSettingMapper;

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IActivityProductService#getAllActivityProductList(long, java.lang.String, java.lang.String, int, int, int, int, long, long, double, double, int, int, com.baomidou.mybatisplus.plugins.Page)
     */
    @Override
    public List<RestrictionActivityProduct> getAllActivityProductList(long restrictionActivityProductId,
                                                                      String restrictionActivityProductName, String clothesNumber, int productStatus,
                                                                      int restrictionActivityStatus, int remainCountMin, int remainCountMax, long activityProductShelfTimeBegin,
                                                                      long activityProductShelfTimeEnd, double activityProductPriceMin, double activityProductPriceMax,
                                                                      int saleCountMin, int saleCountMax, Page<Map<String, Object>> page) {
        Wrapper<RestrictionActivityProduct> wrapper = new EntityWrapper<RestrictionActivityProduct>();
        wrapper.lt("product_status", RestrictionActivityProduct.deleted);
        if (restrictionActivityProductId > 0) {//活动ID
            wrapper.eq("id", restrictionActivityProductId);
        }
        if (! StringUtils.isEmpty(restrictionActivityProductName)) {//活动商品名称
            wrapper.like("product_name", restrictionActivityProductName);
        }
        if (! StringUtils.isEmpty(clothesNumber)) {//活动商品款号
            wrapper.like("clothes_number", clothesNumber);
        }
        if (productStatus > - 1) {//活动商品状态
            wrapper.eq("product_status", productStatus);
        }
        if (restrictionActivityStatus == 3) {//活动已售罄
            wrapper.le("remain_count", 0);
        } else if (restrictionActivityStatus == 0) {//活动即将开始
            wrapper.gt("activity_begin_time", System.currentTimeMillis());
        } else if (restrictionActivityStatus == 1) {//活动进行中
            wrapper.and(" (activity_begin_time<" + System.currentTimeMillis() + " and activity_end_time>" + System.currentTimeMillis() + ") ");
        } else if (restrictionActivityStatus == 2) {//活动已结束
            wrapper.le("activity_end_time", System.currentTimeMillis());
        }
        //活动当前库存量
        if (remainCountMin > 0) {
            wrapper.ge("remain_count", remainCountMin);
        }
        if (remainCountMax > 0) {
            wrapper.le("remain_count", remainCountMax);
        }
        //上架时间
        if (activityProductShelfTimeBegin > - 1) {
            wrapper.ge("activity_product_shelf_time", activityProductShelfTimeBegin);
        }
        if (activityProductShelfTimeEnd > - 1) {
            wrapper.le("activity_product_shelf_time", activityProductShelfTimeEnd);
        }
        //活动价
        if (activityProductPriceMin > 0) {
            wrapper.ge("activity_product_price", activityProductPriceMin);
        }
        if (activityProductPriceMax > 0) {
            wrapper.le("activity_product_price", activityProductPriceMax);
        }
        //销量
        if (saleCountMin > 0) {
            wrapper.ge("sale_count", saleCountMin);
        }
        if (saleCountMax > 0) {
            wrapper.le("sale_count", saleCountMax);
        }
        wrapper.orderBy("create_time", false);
        return restrictionActivityProductMapper.selectPage(page, wrapper);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IActivityProductService#getAllActivityProductSkuList(long)
     */
    @Override
    public List<RestrictionActivityProductSku> getAllActivityProductSkuList(long restrictionActivityProductId) {
        Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>()
                .eq("activity_product_id", restrictionActivityProductId);
        return restrictionActivityProductSkuMapper.selectList(wrapper);
    }

    /**
     * 获取活动商品总数量
     */
    @Override
    public int getActivityProductListAllCount() {
        Wrapper<RestrictionActivityProduct> wrapper = new EntityWrapper<RestrictionActivityProduct>()
                .lt("product_status", RestrictionActivityProduct.deleted);
        return restrictionActivityProductMapper.selectCount(wrapper);
    }

    /**
     * 获取活动商品进行中数量
     */
    @Override
    public int getActivityProductListProcessingCount() {
        Wrapper<RestrictionActivityProduct> wrapper = new EntityWrapper<RestrictionActivityProduct>().lt("product_status", RestrictionActivityProduct.deleted)
                .and(" (activity_begin_time<" + System.currentTimeMillis() + " and activity_end_time>" + System.currentTimeMillis() + ") ");
        return restrictionActivityProductMapper.selectCount(wrapper);
    }

    /**
     * 获取活动商品已售罄数量
     */
    @Override
    public int getActivityProductListSoldOutCount() {
        Wrapper<RestrictionActivityProduct> wrapper = new EntityWrapper<RestrictionActivityProduct>()
                .lt("product_status", RestrictionActivityProduct.deleted)
                .le("remain_count", 0);
        return restrictionActivityProductMapper.selectCount(wrapper);
    }

    /**
     * 上架活动商品
     */
    @Override
    public int shelfActivityProduct(long restrictionActivityProductId) {
        RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
        long currentTimeMillis = System.currentTimeMillis();
        if (restrictionActivityProduct.getActivityEndTime() < currentTimeMillis) {
            logger.error("上架活动商品:该活动已结束restrictionActivityProductId:" + restrictionActivityProductId);
            throw new RuntimeException("该活动已结束");
        }
        restrictionActivityProduct = new RestrictionActivityProduct();
        restrictionActivityProduct.setId(restrictionActivityProductId);
        restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.on_the_shelves);
        restrictionActivityProduct.setActivityProductShelfTime(currentTimeMillis);
        restrictionActivityProduct.setUpdateTime(currentTimeMillis);
        return restrictionActivityProductMapper.updateById(restrictionActivityProduct);
    }

    /**
     * 删除活动商品
     */
    @Override
    public int deleteActivityProduct(long restrictionActivityProductId) {
        RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
        if (restrictionActivityProduct.getActivityProductShelfTime() > 0) {
            logger.error("删除活动商品:该商品已上架过restrictionActivityProductId:" + restrictionActivityProductId);
            throw new RuntimeException("该商品已上架过");
        }
        restrictionActivityProduct = new RestrictionActivityProduct();
        restrictionActivityProduct.setId(restrictionActivityProductId);
        restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.deleted);
        return restrictionActivityProductMapper.updateById(restrictionActivityProduct);
    }

    /**
     * 下架活动商品
     */
    @Override
    public int soldOutActivityProduct(long restrictionActivityProductId) {
        RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
        restrictionActivityProduct = new RestrictionActivityProduct();
        restrictionActivityProduct.setId(restrictionActivityProductId);
        restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.has_been_removed);
        return restrictionActivityProductMapper.updateById(restrictionActivityProduct);
    }

    /**
     * 获取活动商品
     */
    @Override
    public RestrictionActivityProduct getActivityProductById(long restrictionActivityProductId) {
        return restrictionActivityProductMapper.selectById(restrictionActivityProductId);
    }

    /**
     * 保存一个活动商品
     * update by Charlie(唐静) 2018-5-28 10:54:33 : version 3.7.5 新增字段 "miniPurchaseCount"
     * @param productIds
     * @param restrictionActivityProductId
     * @param promotionImage
     * @param activityProductName
     * @param activityProductPrice
     * @param activityPricePercentage
     * @param productPrice
     * @param skuInfo
     * @param activityBeginTime
     * @param activityEndTime
     * @param restrictionCount 最大购买量/限购量
     * @param miniPurchaseCount 最小购买量/起订量
     * @return int
     * @auther Charlie(唐静)
     * @date 2018/5/28 10:52
     */
    @Override
    public int saveActivityProductInfo(String productIds, long restrictionActivityProductId, String promotionImage, String activityProductName,
                                       double activityProductPrice, int activityPricePercentage, double productPrice, String skuInfo, long activityBeginTime, long activityEndTime,
                                       int restrictionCount, int miniPurchaseCount) {
        logger.info("保存活动商品信息productIds:" + productIds);
        String[] productIdArr = productIds.split(",");
        if (restrictionActivityProductId > 0) {
            //修改商品
            return updateActivityProduct(restrictionActivityProductId, promotionImage, activityProductName, activityProductPrice, productPrice, skuInfo,
                    activityBeginTime, activityEndTime, restrictionCount, miniPurchaseCount);
        } else if (productIdArr.length < 0) {
            logger.error("保存活动商品信息:productIdArr为空:productIdArr:" + productIdArr);
            throw new RuntimeException("保存活动商品信息:productIdArr为空");
        } else if (productIdArr.length == 1) {
            //保存单个商品
            return saveActivityProduct(Long.parseLong(productIdArr[0]), promotionImage, activityProductName, activityProductPrice, productPrice, skuInfo,
                    activityBeginTime, activityEndTime, restrictionCount, miniPurchaseCount);
        }
        return 0;
    }

    /**
     * 编辑商品修改
     * update by Charlie(唐静) 2018-5-28 11:03:24 version 3.7.5 ---> 添加更新字段 miniPurchaseCount
     * @param restrictionActivityProductId
     * @param promotionImage
     * @param activityProductName
     * @param activityProductPrice
     * @param productPrice
     * @param skuInfo
     * @param activityBeginTime
     * @param activityEndTime
     * @param restrictionCount
     * @param miniPurchaseCount 最小购买量/起订量
     * @return
     */
    @Transactional( rollbackFor = Exception.class )
    protected int updateActivityProduct(long restrictionActivityProductId, String promotionImage,
                                      String activityProductName, double activityProductPrice, double productPrice, String skuInfo,
                                      long activityBeginTime, long activityEndTime, int restrictionCount, int miniPurchaseCount) {
        //封装活动商品信息
        RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
        restrictionActivityProduct.setId(restrictionActivityProductId);
        //推广图
        if (! StringUtils.isEmpty(promotionImage)) {
            restrictionActivityProduct.setPromotionImage(promotionImage);
        }
        //活动商品剩余库存量
        Map<Long, Integer> skuCount = new HashMap<Long, Integer>();
        String[] split = skuInfo.split(",");
        int allCount = 0;
        for (String skuStr : split) {
            String[] sku = skuStr.split("_");
            int count = Integer.parseInt(sku[1]);
            allCount += count;
            skuCount.put(Long.parseLong(sku[0]), count);
        }
        restrictionActivityProduct.setRemainCount(allCount);
        //修改时间
        restrictionActivityProduct.setUpdateTime(restrictionActivityProduct.getCreateTime());

        //商品名称
        if (StringUtils.isEmpty(activityProductName)) {
            logger.error("保存活动商品信息(单个):活动商品名称不能为空:activityProductName:" + activityProductName);
            throw new RuntimeException("保存活动商品信息(单个):活动商品名称不能为空");
        }
        restrictionActivityProduct.setProductName(activityProductName);
        //活动价格
        if (activityProductPrice <= 0) {
            logger.error("保存活动商品信息(单个):活动商品价格不能为0:activityProductPrice:" + activityProductPrice);
            throw new RuntimeException("保存活动商品信息(单个):活动商品价格不能为0");
        }
        restrictionActivityProduct.setActivityProductPrice(activityProductPrice);
        //商品价格
        if (productPrice <= 0) {
            logger.error("保存活动商品信息(单个):商品原价不能为0:productPrice:" + productPrice);
            throw new RuntimeException("保存活动商品信息(单个):商品原价不能为0");
        }
        restrictionActivityProduct.setProductPrice(productPrice);
        //活动开始时间
        if (activityBeginTime <= 0) {
            logger.error("保存活动商品信息(单个):活动开始时间不能为空:activityBeginTime:" + activityBeginTime);
            throw new RuntimeException("保存活动商品信息(单个):活动开始时间不能为空");
        }
        restrictionActivityProduct.setActivityBeginTime(activityBeginTime);
        //活动结束时间
        if (activityEndTime <= 0) {
            logger.error("保存活动商品信息(单个):活动结束时间不能为空:activityEndTime:" + activityEndTime);
            throw new RuntimeException("保存活动商品信息(单个):活动结束时间不能为空");
        }
        if (activityEndTime < activityBeginTime) {
            logger.error("保存活动商品信息(单个):活动结束时间不能小于活动开始时间:activityBeginTime:" + activityBeginTime + ",:activityEndTime:" + activityEndTime);
            throw new RuntimeException("保存活动商品信息(单个):活动结束时间不能小于活动开始时间");
        }
        restrictionActivityProduct.setActivityEndTime(activityEndTime);
        //限购总量
        if (restrictionCount <= 0) {
            logger.error("保存活动商品信息(单个):限购数量不能小于1:restrictionCount:" + restrictionCount);
            throw new RuntimeException("保存活动商品信息(单个):限购数量不能小于1");
        }
        restrictionActivityProduct.setRestrictionCount(restrictionCount);

        //起订量
        if (miniPurchaseCount <= 0) {
            logger.error("保存活动商品信息(单个):起购数量不能小于1:miniPurchaseCount:" + miniPurchaseCount);
            throw new RuntimeException("保存活动商品信息(单个):起购数量不能小于1");
        }
        restrictionActivityProduct.setMiniPurchaseCount(miniPurchaseCount);

        //校验
        if (restrictionCount < miniPurchaseCount) {
            logger.error("限购量("+restrictionCount+")不能小于起订量("+miniPurchaseCount+")");
            throw new RuntimeException("限购量("+restrictionCount+")不能小于起订量("+miniPurchaseCount+")");
        }

        //更新活动商品
        int record = restrictionActivityProductMapper.updateById(restrictionActivityProduct);

        /* 获取活动商品SKU */
        Wrapper<RestrictionActivityProductSku> restrictionActivityProductSkuWrapper = new EntityWrapper<RestrictionActivityProductSku>()
                .eq("activity_product_id", restrictionActivityProductId);
        List<RestrictionActivityProductSku> restrictionActivityProductSkuList = restrictionActivityProductSkuMapper.selectList(restrictionActivityProductSkuWrapper);

        //更新活动商品SKU
        for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductSkuList) {
            RestrictionActivityProductSku restrictionActivityProductSkuNew = new RestrictionActivityProductSku();
            restrictionActivityProductSkuNew.setId(restrictionActivityProductSku.getId());
            //活动剩余库存量
            restrictionActivityProductSkuNew.setRemainCount(skuCount.get(restrictionActivityProductSku.getProductSkuId()));
            //修改时间
            restrictionActivityProductSkuNew.setUpdateTime(restrictionActivityProductSku.getCreateTime());

            restrictionActivityProductSkuMapper.updateById(restrictionActivityProductSkuNew);
        }
        return record;
    }

    /**
     * 保存活动商品信息(单个)
     * update by Charlie(唐静) 2018-5-28 11:03:24 version 3.7.5 ---> 添加更新字段 miniPurchaseCount
     * @param productId
     * @param promotionImage
     * @param activityProductName
     * @param activityProductPrice
     * @param productPrice
     * @param skuInfo
     * @param activityBeginTime
     * @param activityEndTime
     * @param restrictionCount
     * @return
     */
    @Transactional( rollbackFor = Exception.class )
    protected int saveActivityProduct(long productId, String promotionImage, String activityProductName,
                                    double activityProductPrice, double productPrice, String skuInfo, long activityBeginTime, long activityEndTime,
                                    int restrictionCount, int miniPurchaseCount)
    {
        //判断当前商品是否已经参加限购活动
        Wrapper<RestrictionActivityProduct> restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
                .eq("product_id", productId).and(" (activity_end_time>" + System.currentTimeMillis() + " and product_status<=2) ");
        List<RestrictionActivityProduct> restrictionActivityProductList = restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper);
        if (restrictionActivityProductList.size() > 0) {
            logger.error("该商品已经参加限购活动:productId:" + productId);
            throw new RuntimeException("该商品已经参加限购活动");
        }
        //封装活动商品信息
        RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();

        //获取商品信息
        ProductNew productNew = productNewMapper.selectById(productId);
        if (productNew == null) {
            logger.error("新建活动商品时获取商品信息:该款号没有对应的商品:productId:" + productId);
            throw new RuntimeException("新建活动商品时获取商品信息:该款号没有对应的商品");
        }
        //获取商品详情信息
        Wrapper<ProductDetail> productDetailWrapper = new EntityWrapper<ProductDetail>().eq("productId", productId);
        List<ProductDetail> productDetailList = productDetailMapper.selectList(productDetailWrapper);
        if (productDetailList.size() <= 0) {
            logger.error("新建活动商品时获取商品信息:该商品没有对应的商品详情:productId:" + productId);
            throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品详情");
        }
        ProductDetail productDetail = productDetailList.get(0);
        if (productDetail == null) {
            logger.error("新建活动商品时获取商品信息:该商品没有对应的商品:productId:" + productId);
            throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品");
        }

        //商品ID
        restrictionActivityProduct.setProductId(productId);
        //推广图
        if (! StringUtils.isEmpty(promotionImage)) {
            restrictionActivityProduct.setPromotionImage(promotionImage);
        }
        //商品款号
        restrictionActivityProduct.setClothesNumber(productNew.getClothesNumber());
        //商品状态
        restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.to_be_on_the_shelves);
        //获取活动商品对应的sku
        Wrapper<ProductSkuNew> productSkuNewWrapper = new EntityWrapper<ProductSkuNew>().eq("productId", productId);
        List<ProductSkuNew> productSkuNewList = productSkuNewMapper.selectList(productSkuNewWrapper);
        //获取sku个数
        restrictionActivityProduct.setSkuCount(productSkuNewList.size());
        //活动商品预览图
        String showcaseImage = productDetail.getShowcaseImgs();
        if (! StringUtils.isEmpty(showcaseImage)) {
            JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
            if (jsonArray.size() > 0) {
                restrictionActivityProduct.setMainImage(jsonArray.getString(0));
            } else {
                restrictionActivityProduct.setMainImage("");
            }
        } else {
            restrictionActivityProduct.setMainImage("");
        }
        //活动商品推广图
        restrictionActivityProduct.setShowcaseImage(showcaseImage);
        //活动商品剩余库存量
        Map<Long, Integer> skuCount = new HashMap<Long, Integer>();
        if (! StringUtils.isEmpty(skuInfo)) {
            String[] split = skuInfo.split(",");
            int allCount = 0;
            for (String skuStr : split) {
                String[] sku = skuStr.split("_");
                int count = Integer.parseInt(sku[1]);
                allCount += count;
                skuCount.put(Long.parseLong(sku[0]), count);
            }
            restrictionActivityProduct.setRemainCount(allCount);
        }

        //活动商品总库存量
        int allRemainCount = 0;
        for (ProductSkuNew productSkuNew : productSkuNewList) {
            allRemainCount += productSkuNew.getRemainCount();
        }
        restrictionActivityProduct.setTotalRemainCount(allRemainCount);
        //创建时间
        restrictionActivityProduct.setCreateTime(System.currentTimeMillis());
        //修改时间
        restrictionActivityProduct.setUpdateTime(restrictionActivityProduct.getCreateTime());

        //商品名称
        if (StringUtils.isEmpty(activityProductName)) {
            logger.error("保存活动商品信息(单个):活动商品名称不能为空:activityProductName:" + activityProductName);
            throw new RuntimeException("保存活动商品信息(单个):活动商品名称不能为空");
        }
        restrictionActivityProduct.setProductName(activityProductName);
        //活动价格
        if (activityProductPrice <= 0) {
            logger.error("保存活动商品信息(单个):活动商品价格不能为0:activityProductPrice:" + activityProductPrice);
            throw new RuntimeException("保存活动商品信息(单个):活动商品价格不能为0");
        }
        restrictionActivityProduct.setActivityProductPrice(activityProductPrice);
        //商品价格
        if (productPrice <= 0) {
            logger.error("保存活动商品信息(单个):商品原价不能为0:productPrice:" + productPrice);
            throw new RuntimeException("保存活动商品信息(单个):商品原价不能为0");
        }
        restrictionActivityProduct.setProductPrice(productPrice);
        //活动开始时间
        if (activityBeginTime <= 0) {
            logger.error("保存活动商品信息(单个):活动开始时间不能为空:activityBeginTime:" + activityBeginTime);
            throw new RuntimeException("保存活动商品信息(单个):活动开始时间不能为空");
        }
        restrictionActivityProduct.setActivityBeginTime(activityBeginTime);
        //活动结束时间
        if (activityEndTime <= 0) {
            logger.error("保存活动商品信息(单个):活动结束时间不能为空:activityEndTime:" + activityEndTime);
            throw new RuntimeException("保存活动商品信息(单个):活动结束时间不能为空");
        }
        if (activityEndTime < activityBeginTime) {
            logger.error("保存活动商品信息(单个):活动结束时间不能小于活动开始时间:activityBeginTime:" + activityBeginTime + ",:activityEndTime:" + activityEndTime);
            throw new RuntimeException("保存活动商品信息(单个):活动结束时间不能小于活动开始时间");
        }
        restrictionActivityProduct.setActivityEndTime(activityEndTime);
        //限购总量
        if (restrictionCount <= 0) {
            logger.error("保存活动商品信息(单个):限购数量不能小于1:restrictionCount:" + restrictionCount);
            throw new RuntimeException("保存活动商品信息(单个):限购数量不能小于1");
        }
        restrictionActivityProduct.setRestrictionCount(restrictionCount);

        //最小起订量
        if (miniPurchaseCount <= 0) {
            logger.error("保存活动商品信息(单个):限购数量不能小于0:miniPurchaseCount:" + miniPurchaseCount);
            throw new RuntimeException("保存活动商品信息(单个):限购数量不能小于1");
        }
        restrictionActivityProduct.setMiniPurchaseCount(miniPurchaseCount);

        //插入活动商品数据
        int record = restrictionActivityProductMapper.insert(restrictionActivityProduct);
        //获取活动商品id
        restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
                .eq("product_id", productId).orderBy("create_time", false);
        long activityProductId = restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper).get(0).getId();

        //生成活动商品的sku信息
        for (ProductSkuNew productSkuNew : productSkuNewList) {
            RestrictionActivityProductSku restrictionActivityProductSku = new RestrictionActivityProductSku();
            restrictionActivityProductSku.setActivityProductId(activityProductId);//活动商品id
            restrictionActivityProductSku.setProductId(productId);//商品id
            long productSkuId = productSkuNew.getId();
            restrictionActivityProductSku.setProductSkuId(productSkuId);//skuId
            restrictionActivityProductSku.setColorName(productSkuNew.getColorName());//颜色
            restrictionActivityProductSku.setSizeName(productSkuNew.getSizeName());//尺码
            restrictionActivityProductSku.setRemainCount(skuCount.get(productSkuId));//活动剩余库存量
            restrictionActivityProductSku.setTotalRemainCount(productSkuNew.getRemainCount());//活动商品总库存量
            restrictionActivityProductSku.setCreateTime(System.currentTimeMillis());//创建时间
            restrictionActivityProductSku.setUpdateTime(restrictionActivityProductSku.getCreateTime());//修改时间
            restrictionActivityProductSkuMapper.insert(restrictionActivityProductSku);//插入活动商品sku
        }
        return record;
    }

    /**
     * 保存活动商品信息(多个)
     *
     * @return
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int saveActivityProducts(String[] productIds, double activityPrice, int activityPricePercentage,
                                    long activityBeginTime, long activityEndTime, int restrictionCount)
    {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().in("id", productIds);
        List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
        int record = 0;
        for (ProductNew productNew : productNewList) {
            //商品Id
            long productId = productNew.getId();

            //判断当前商品是否已经参加限购活动
            Wrapper<RestrictionActivityProduct> restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
                    .eq("product_id", productId).and(" (activity_end_time>" + System.currentTimeMillis() + " and product_status<=2) ");
            List<RestrictionActivityProduct> restrictionActivityProductList = restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper);
            if (restrictionActivityProductList.size() > 0) {
                logger.error("该商品已经参加限购活动:productId:" + productId);
                throw new RuntimeException("该商品已经参加限购活动");
            }

            //封装活动商品信息
            RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();

            //获取商品详情信息
            Wrapper<ProductDetail> productDetailWrapper = new EntityWrapper<ProductDetail>().eq("productId", productId);
            List<ProductDetail> productDetailList = productDetailMapper.selectList(productDetailWrapper);
            if (productDetailList.size() <= 0) {
                logger.error("保存活动商品信息(多个):该商品没有对应的商品详情:productId:" + productId);
                throw new RuntimeException("保存活动商品信息(多个):该商品没有对应的商品详情");
            }
            ProductDetail productDetail = productDetailList.get(0);
            if (productDetail == null) {
                logger.error("保存活动商品信息(多个):该商品没有对应的商品:productId:" + productId);
                throw new RuntimeException("保存活动商品信息(多个):该商品没有对应的商品");
            }

            //商品ID
            restrictionActivityProduct.setProductId(productId);
            //商品款号
            restrictionActivityProduct.setClothesNumber(productNew.getClothesNumber());
            //商品名称
            restrictionActivityProduct.setProductName(productNew.getName());
            //商品状态
            restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.to_be_on_the_shelves);
            //获取活动商品对应的sku
            Wrapper<ProductSkuNew> productSkuNewWrapper = new EntityWrapper<ProductSkuNew>().eq("productId", productId);
            List<ProductSkuNew> productSkuNewList = productSkuNewMapper.selectList(productSkuNewWrapper);
            //获取sku个数
            restrictionActivityProduct.setSkuCount(productSkuNewList.size());
            //活动商品预览图
            String showcaseImage = productDetail.getShowcaseImgs();
            if (! StringUtils.isEmpty(showcaseImage)) {
                JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
                if (jsonArray.size() > 0) {
                    restrictionActivityProduct.setMainImage(jsonArray.getString(0));
                } else {
                    restrictionActivityProduct.setMainImage("");
                }
            } else {
                restrictionActivityProduct.setMainImage("");
            }
            //活动商品推广图
            restrictionActivityProduct.setShowcaseImage(showcaseImage);
            //活动商品总库存量和剩余库存量
            int allRemainCount = 0;
            for (ProductSkuNew productSkuNew : productSkuNewList) {
                allRemainCount += productSkuNew.getRemainCount();
            }
            restrictionActivityProduct.setRemainCount(allRemainCount);
            restrictionActivityProduct.setTotalRemainCount(allRemainCount);
            //活动价格
            restrictionActivityProduct.setActivityProductPrice(productNew.getMinLadderPrice());
            //活动商品原价
            restrictionActivityProduct.setProductPrice(productNew.getMaxLadderPrice());
            //创建时间
            restrictionActivityProduct.setCreateTime(System.currentTimeMillis());
            //修改时间
            restrictionActivityProduct.setUpdateTime(restrictionActivityProduct.getCreateTime());
            //活动价格
            if (activityPrice < 0 || activityPricePercentage < 0) {
                logger.error("保存活动商品信息(多个):活动价格不能小于等于0:activityPrice:" + activityPrice + ";activityPricePercentage:" + activityPricePercentage);
                throw new RuntimeException("保存活动商品信息(多个):活动价格不能小于等于0");
            }
            if (activityPrice > 0) {//固定价格
                restrictionActivityProduct.setActivityProductPrice(activityPrice);
            } else if (activityPricePercentage > 0) {//百分比
                DecimalFormat df = new DecimalFormat("#.00");
                String price = df.format(restrictionActivityProduct.getActivityProductPrice() * activityPricePercentage / 100);
                restrictionActivityProduct.setActivityProductPrice(Double.parseDouble(price));
            }
            //商品价格
            restrictionActivityProduct.setProductPrice(productNew.getMaxLadderPrice());
            //活动开始时间
            if (activityBeginTime <= 0) {
                logger.error("保存活动商品信息(多个):活动开始时间不能为空:activityBeginTime:" + activityBeginTime);
                throw new RuntimeException("保存活动商品信息(多个):活动开始时间不能为空");
            }
            restrictionActivityProduct.setActivityBeginTime(activityBeginTime);
            //活动结束时间
            if (activityEndTime <= 0) {
                logger.error("保存活动商品信息(多个):活动结束时间不能为空:activityEndTime:" + activityEndTime);
                throw new RuntimeException("保存活动商品信息(多个):活动结束时间不能为空");
            }
            if (activityEndTime < activityBeginTime) {
                logger.error("保存活动商品信息(多个):活动结束时间不能小于活动开始时间:activityBeginTime:" + activityBeginTime + ",:activityEndTime:" + activityEndTime);
                throw new RuntimeException("保存活动商品信息(多个):活动结束时间不能小于活动开始时间");
            }
            restrictionActivityProduct.setActivityEndTime(activityEndTime);

            //限购总量
            if (restrictionCount <= 0) {
                logger.error("保存活动商品信息(多个):限购数量不能小于0:restrictionCount:" + restrictionCount);
                throw new RuntimeException("保存活动商品信息(多个):限购数量不能小于0");
            }
            restrictionActivityProduct.setRestrictionCount(restrictionCount);


            //最小起订量
            int minLadderCount = ProductNew.getMinLadderCount(productNew.getLadderPriceJson());
            restrictionActivityProduct.setMiniPurchaseCount(minLadderCount);


            //插入活动商品数据
            record = record + restrictionActivityProductMapper.insert(restrictionActivityProduct);
            //获取活动商品id
            restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
                    .eq("product_id", productId).orderBy("create_time", false);
            long activityProductId = restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper).get(0).getId();
            //生成活动商品的sku信息
            for (ProductSkuNew productSkuNew : productSkuNewList) {
                RestrictionActivityProductSku restrictionActivityProductSku = new RestrictionActivityProductSku();
                restrictionActivityProductSku.setActivityProductId(activityProductId);//活动商品id
                restrictionActivityProductSku.setProductId(productId);//商品id
                long productSkuId = productSkuNew.getId();
                restrictionActivityProductSku.setProductSkuId(productSkuId);//skuId
                restrictionActivityProductSku.setColorName(productSkuNew.getColorName());//颜色
                restrictionActivityProductSku.setSizeName(productSkuNew.getSizeName());//尺码
                int remainCount = productSkuNew.getRemainCount();
                restrictionActivityProductSku.setRemainCount(remainCount);//活动剩余库存量
                restrictionActivityProductSku.setTotalRemainCount(remainCount);//活动商品总库存量
                restrictionActivityProductSku.setCreateTime(System.currentTimeMillis());//创建时间
                restrictionActivityProductSku.setUpdateTime(restrictionActivityProductSku.getCreateTime());//修改时间
                restrictionActivityProductSkuMapper.insert(restrictionActivityProductSku);//插入活动商品sku
            }
        }
        return record;
    }

//	/**
//	 * 增加活动商品sku
//	 * @param strings
//	 * @param activityProductId
//	 * @param productId
//	 */
//	private void inserRestrictionActivityProductSku(String[] strings, long activityProductId, long productId) {
//		RestrictionActivityProductSku restrictionActivityProductSku = new RestrictionActivityProductSku();
//		restrictionActivityProductSku.setActivityProductId(activityProductId);
//		restrictionActivityProductSku.setProductId(productId);
//		restrictionActivityProductSku.setProductSkuId(Long.parseLong(strings[0]));
//		restrictionActivityProductSku.setColorName(strings[1]);
//		restrictionActivityProductSku.setSizeName(strings[2]);
//		int remainCount = Integer.parseInt(strings[3]);
//		restrictionActivityProductSku.setRemainCount(remainCount);
//		restrictionActivityProductSku.setTotalRemainCount(remainCount);
//		restrictionActivityProductSku.setCreateTime(System.currentTimeMillis());
//		restrictionActivityProductSku.setUpdateTime(restrictionActivityProductSku.getCreateTime());
//		restrictionActivityProductSkuMapper.insert(restrictionActivityProductSku);
//	}
//	/**
//	 * 新建活动商品时获取商品信息
//	 */

//	public ArrayList<Long> getNewActivityProductInfos(String clothesNumbers) {
//		String[] clothesNumberArr = clothesNumbers.split(",");
//		ArrayList<Long> restrictionActivityProductIdList = new ArrayList<Long>();
//		for (String clothesNumber : clothesNumberArr) {
//			//判断该商品是否有未结束的活动
//			Wrapper<RestrictionActivityProduct> restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
//					.eq("clothes_number", clothesNumber).and(" (activity_end_time>"+System.currentTimeMillis()+" or activity_begin_time=0) ");
//			if(restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper).size()>0){
//				logger.error("商品（款号："+clothesNumber+"）已参加活动！");
//				throw new RuntimeException("商品（款号："+clothesNumber+"）已参加活动！");
//			}
//			//获取商品信息
//			Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("ClothesNumber", clothesNumber);
//			List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
//			if(productNewList.size()<=0){
//				logger.error("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:"+clothesNumber);
//				throw new RuntimeException("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:"+clothesNumber);
//			}
//			ProductNew productNew = productNewList.get(0);
//			if(productNew==null){
//				logger.error("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:"+clothesNumber);
//				throw new RuntimeException("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:"+clothesNumber);
//			}
//			if(productNew.getState()<5){
//				logger.error("商品（款号："+clothesNumber+"）未审核，无法创建活动！");
//				throw new RuntimeException("商品（款号："+clothesNumber+"）未审核，无法创建活动！");
//			}
//			long productId = productNew.getId();
//			//获取商品详情信息
//			Wrapper<ProductDetail> productDetailWrapper = new EntityWrapper<ProductDetail>().eq("productId", productId);
//			List<ProductDetail> productDetailList = productDetailMapper.selectList(productDetailWrapper);
//			if(productDetailList.size()<=0){
//				logger.error("新建活动商品时获取商品信息:该商品没有对应的商品详情:productId:"+productId);
//				throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品详情:productId:"+productId);
//			}
//			ProductDetail productDetail = productDetailList.get(0);
//			if(productDetail==null){
//				logger.error("新建活动商品时获取商品信息:该商品没有对应的商品:productId:"+productId);
//				throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品:productId:"+productId);
//			}
//			//封装活动商品信息
//			RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
//			//商品ID
//			restrictionActivityProduct.setProductId(productId);
//			//商品款号
//			restrictionActivityProduct.setClothesNumber(clothesNumber);
//			//商品名称
//			restrictionActivityProduct.setProductName(productNew.getName());
//			//商品状态
//			restrictionActivityProduct.setProductStatus(RestrictionActivityProduct.to_be_on_the_shelves);
//			//获取活动商品对应的sku
//			Wrapper<ProductSkuNew> productSkuNewWrapper = new EntityWrapper<ProductSkuNew>().eq("productId", productId);
//			List<ProductSkuNew> productSkuNewList = productSkuNewMapper.selectList(productSkuNewWrapper);
//			//获取sku个数
//			restrictionActivityProduct.setSkuCount(productSkuNewList.size());
//			//活动商品预览图
//			String showcaseImage = productDetail.getShowcaseImgs();
//			if(!StringUtils.isEmpty(showcaseImage)){
//				JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
//				if(jsonArray.size()>0){
//					restrictionActivityProduct.setMainImage(jsonArray.getString(0));
//				}else{
//					restrictionActivityProduct.setMainImage("");
//				}
//			}else{
//				restrictionActivityProduct.setMainImage("");
//			}
//			//活动商品推广图
//			restrictionActivityProduct.setShowcaseImage(showcaseImage);
//			//活动商品总库存量和剩余库存量
//			int allRemainCount = 0;
//			for (ProductSkuNew productSkuNew : productSkuNewList) {
//				allRemainCount += productSkuNew.getRemainCount();
//			}
//			restrictionActivityProduct.setRemainCount(allRemainCount);
//			restrictionActivityProduct.setTotalRemainCount(allRemainCount);
//			//活动价格
//			restrictionActivityProduct.setActivityProductPrice(productNew.getMinLadderPrice());
//			//活动商品原价
//			restrictionActivityProduct.setProductPrice(productNew.getMaxLadderPrice());
//			//创建时间
//			restrictionActivityProduct.setCreateTime(System.currentTimeMillis());
//			//修改时间
//			restrictionActivityProduct.setUpdateTime(restrictionActivityProduct.getCreateTime());
//			//插入活动商品数据
//			restrictionActivityProductMapper.insert(restrictionActivityProduct);
//			//获取活动商品id
//			restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
//					.eq("product_id", productId).orderBy("create_time",false);
//			long activityProductId = restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper).get(0).getId();
//			restrictionActivityProductIdList.add(activityProductId);
//			for (ProductSkuNew productSkuNew : productSkuNewList) {
//				RestrictionActivityProductSku restrictionActivityProductSku = new RestrictionActivityProductSku();
//				restrictionActivityProductSku.setActivityProductId(activityProductId);//活动商品id
//				restrictionActivityProductSku.setProductId(productId);//商品id
//				restrictionActivityProductSku.setProductSkuId(productSkuNew.getId());//skuId
//				restrictionActivityProductSku.setColorName(productSkuNew.getColorName());//颜色
//				restrictionActivityProductSku.setSizeName(productSkuNew.getSizeName());//尺码
//				int remainCount = productSkuNew.getRemainCount();//活动商品总库存量和剩余库存量
//				restrictionActivityProductSku.setRemainCount(remainCount);
//				restrictionActivityProductSku.setTotalRemainCount(remainCount);
//				restrictionActivityProductSku.setCreateTime(System.currentTimeMillis());//创建时间
//				restrictionActivityProductSku.setUpdateTime(restrictionActivityProductSku.getCreateTime());//修改时间
//				restrictionActivityProductSkuMapper.insert(restrictionActivityProductSku);//插入活动商品sku
//			}
//		}
//		return restrictionActivityProductIdList;
//	}

    @Override
    public Map<String, Object> getNewActivityProductInfo(String clothesNumbers) {
        String[] clothesNumberArr = clothesNumbers.split(",");
        List<ProductNew> productNewInfoList = new ArrayList<ProductNew>();
        for (String clothesNumber : clothesNumberArr) {
            //判断该商品是否有未结束的活动
            Wrapper<RestrictionActivityProduct> restrictionActivityProductWrapper = new EntityWrapper<RestrictionActivityProduct>()
                    .eq("clothes_number", clothesNumber)
                    .and(" (activity_end_time>" + System.currentTimeMillis() + " and product_status<=2) ");
            if (restrictionActivityProductMapper.selectList(restrictionActivityProductWrapper).size() > 0) {
                logger.error("商品（款号：" + clothesNumber + "）已参加活动！");
                throw new RuntimeException("商品（款号：" + clothesNumber + "）已参加活动！");
            }
            //获取商品信息
            Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("ClothesNumber", clothesNumber);
            List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
            if (productNewList.size() <= 0) {
                logger.error("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:" + clothesNumber);
                throw new RuntimeException("新建活动商品时获取商品信息:该款号没有对应的商品");
            }
            ProductNew productNew = productNewList.get(0);
            if (productNew == null) {
                logger.error("新建活动商品时获取商品信息:该款号没有对应的商品:clothesNumber:" + clothesNumber);
                throw new RuntimeException("新建活动商品时获取商品信息:该款号没有对应的商品");
            }
            if (productNew.getState() < 5) {
                logger.error("商品（款号：" + clothesNumber + "）未审核，无法创建活动！");
                throw new RuntimeException("商品（款号：" + clothesNumber + "）未审核，无法创建活动！");
            }
            productNewInfoList.add(productNew);
        }

        if (productNewInfoList.size() == 1) {
            ProductNew product = productNewInfoList.get(0);
            //封装单个商品
            Map<String, Object> result = packagingActivityProductInfo(product);

            /* 取最小起批量 */
            int miniPurchaseCount = ProductNew.getMinLadderCount(product.getLadderPriceJson());
            result.put("miniPurchaseCount",miniPurchaseCount);

            return result;
        } else {//封装多个商品
            return packagingActivityProductInfo(productNewInfoList);
        }
    }

    /**
     * 封装多个商品
     *
     * @param productNewInfoList
     * @return
     */
    private Map<String, Object> packagingActivityProductInfo(List<ProductNew> productNewInfoList) {
        Map<String, Object> data = new HashMap<String, Object>();
        String productId = "";
        double activityProductPrice = 0;
        for (ProductNew productNew : productNewInfoList) {
            if (StringUtils.isEmpty(productId)) {
                productId = productId + productNew.getId();
            } else {
                productId = productId + "," + productNew.getId();
            }
            double minLadderPrice = productNew.getMinLadderPrice();
            if (activityProductPrice < minLadderPrice) {
                activityProductPrice = minLadderPrice;
            }
        }
        data.put("productId", productId);//商品id用英文逗号分隔组成的字符串
        data.put("activityProductPrice", activityProductPrice);//活动价格
        data.put("activityBeginTime", DateUtil.parseLongTime2Str(System.currentTimeMillis()));//活动开始时间
        return data;
    }

    /**
     * 封装单个商品
     *
     * @param productNew
     * @return
     */
    private Map<String, Object> packagingActivityProductInfo(ProductNew productNew) {
        Map<String, Object> data = new HashMap<String, Object>();
        long productId = productNew.getId();
        data.put("productId", productId);//商品id
        //获取商品详情信息
        Wrapper<ProductDetail> productDetailWrapper = new EntityWrapper<ProductDetail>().eq("productId", productId);
        List<ProductDetail> productDetailList = productDetailMapper.selectList(productDetailWrapper);
        if (productDetailList.size() <= 0) {
            logger.error("新建活动商品时获取商品信息:该商品没有对应的商品详情:productId:" + productId);
            throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品详情");
        }
        ProductDetail productDetail = productDetailList.get(0);
        if (productDetail == null) {
            logger.error("新建活动商品时获取商品信息:该商品没有对应的商品:productId:" + productId);
            throw new RuntimeException("新建活动商品时获取商品信息:该商品没有对应的商品");
        }
        //活动商品预览图
        String showcaseImage = productDetail.getShowcaseImgs();
        if (! StringUtils.isEmpty(showcaseImage)) {
            JSONArray jsonArray = JSONArray.parseArray(showcaseImage);
//			if(jsonArray.size()>0){
            data.put("promotionImage", jsonArray);
//			}else{
//				data.put("promotionImage", "");
//			}
        } else {
            data.put("promotionImage", "");
        }
        data.put("activityProductName", productNew.getName());//活动商品名称
        data.put("activityProductPrice", productNew.getMinLadderPrice());//活动价格
        data.put("productPrice", productNew.getMaxLadderPrice());//商品价格
        //获取活动商品对应的sku
        Wrapper<ProductSkuNew> productSkuNewWrapper = new EntityWrapper<ProductSkuNew>().eq("productId", productId);
        List<ProductSkuNew> productSkuNewList = productSkuNewMapper.selectList(productSkuNewWrapper);
        List<Map<String, Object>> skuInfoList = new ArrayList<Map<String, Object>>();
        for (ProductSkuNew productSkuNew : productSkuNewList) {
            Map<String, Object> skuInfo = new HashMap<String, Object>();
            skuInfo.put("productSkuId", productSkuNew.getId());//商品skuId
            skuInfo.put("color", productSkuNew.getColorName());//颜色
            skuInfo.put("size", productSkuNew.getSizeName());//尺码
            skuInfo.put("remainCount", productSkuNew.getRemainCount());//库存
            skuInfoList.add(skuInfo);
        }
        data.put("skuInfoList", skuInfoList);//sku的信息
        data.put("activityBeginTime", DateUtil.parseLongTime2Str(System.currentTimeMillis()));//活动开始时间
        return data;
    }

    /**
     * 更新活动商品sku库存
     */
    @Transactional( rollbackFor = Exception.class )
    @Override
    public void updateActivityProductSkuRemainCount(long restrictionActivityProductSkuId, int remainCount) {
        RestrictionActivityProductSku restrictionActivityProductSku = new RestrictionActivityProductSku();
        restrictionActivityProductSku.setId(restrictionActivityProductSkuId);
        restrictionActivityProductSku.setRemainCount(remainCount);
        restrictionActivityProductSkuMapper.updateById(restrictionActivityProductSku);
        Wrapper<RestrictionActivityProductSku> restrictionActivityProductSkuWrapper = new EntityWrapper<RestrictionActivityProductSku>()
                .and(" activity_product_id=(SELECT activity_product_id FROM yjj_restriction_activity_product_sku WHERE id=" + restrictionActivityProductSkuId + ") ");
        List<RestrictionActivityProductSku> restrictionActivityProductSkuList = restrictionActivityProductSkuMapper.
                selectList(restrictionActivityProductSkuWrapper);
        int allRemainCount = 0;
        for (RestrictionActivityProductSku activityProductSku : restrictionActivityProductSkuList) {
            allRemainCount += activityProductSku.getRemainCount();
        }
        RestrictionActivityProduct restrictionActivityProduct = new RestrictionActivityProduct();
        restrictionActivityProduct.setId(restrictionActivityProductSkuList.get(0).getActivityProductId());
        restrictionActivityProduct.setRemainCount(allRemainCount);
        restrictionActivityProductMapper.updateById(restrictionActivityProduct);
    }

    /**
     * 获取限购活动商品列表
     */
    @Override
    public List<RestrictionActivityProduct> getRestrictionActivityProductListList(int activityStatus, Page page) {
//		logger.info("获取限购活动商品列表开始");
        Wrapper<RestrictionActivityProduct> wrapper = new EntityWrapper<RestrictionActivityProduct>()
                .eq("product_status", RestrictionActivityProduct.on_the_shelves).gt("activity_end_time", System.currentTimeMillis());
        if (activityStatus == 1) {//正进行中
            wrapper.le("activity_begin_time", System.currentTimeMillis()).orderBy("activity_product_shelf_time", false);
        } else if (activityStatus == 2) {//即将开始
            wrapper.gt("activity_begin_time", System.currentTimeMillis()).orderBy("activity_begin_time", true);
        } else {
            logger.error("获取限购活动商品列表活动状态有误:activityStatus:" + activityStatus);
            throw new RuntimeException("获取限购活动商品列表活动状态有误");
        }
        return restrictionActivityProductMapper.selectPage(page, wrapper);
    }

    /**
     * 获取限购活动商品信息
     */
    @Override
    public RestrictionActivityProduct getRestrictionActivityProductInfo(long activityProductId) {
        return restrictionActivityProductMapper.selectById(activityProductId);
    }

    /**
     * 保存限购活动标题
     */
    @Override
    public void saveActivityTitle(String activityTitle) {
        String stringValue = GlobalSettingName.RESTRICTION_ACTIVITY_TITLE.getStringValue();
        Wrapper<GlobalSettingNew> wrapper = new EntityWrapper<GlobalSettingNew>().eq("PropertyName", stringValue).eq("Status", 0);
        List<GlobalSettingNew> globalSettingList = globalSettingMapper.selectList(wrapper);
        if (globalSettingList.size() < 1) {
            GlobalSettingNew globalSetting = new GlobalSettingNew();
            globalSetting.setPropertyName(stringValue);
            globalSetting.setPropertyValue(activityTitle);
            globalSetting.setStatus(0);
            globalSetting.setCreateTime(System.currentTimeMillis());
            globalSetting.setUpdateTime(globalSetting.getCreateTime());
            globalSetting.setGroupId(0);
            globalSetting.setDescription("限购活动标题");
            globalSettingMapper.insert(globalSetting);
        } else {
            GlobalSettingNew globalSetting = new GlobalSettingNew();
            globalSetting.setId(globalSettingList.get(0).getId());
            globalSetting.setPropertyValue(activityTitle);
            globalSetting.setUpdateTime(System.currentTimeMillis());
            globalSettingMapper.updateById(globalSetting);
        }
    }


    /**
     * 获取限购活动标题
     */
    @Override
    public String getActivityTitle() {
        String stringValue = GlobalSettingName.RESTRICTION_ACTIVITY_TITLE.getStringValue();
        Wrapper<GlobalSettingNew> wrapper = new EntityWrapper<GlobalSettingNew>().eq("PropertyName", stringValue).eq("Status", 0);
        List<GlobalSettingNew> globalSettingList = globalSettingMapper.selectList(wrapper);
        if (globalSettingList.size() < 1) {
            return "";
        } else {
            GlobalSettingNew globalSetting = globalSettingList.get(0);
            return globalSetting.getPropertyValue();
        }
    }
}