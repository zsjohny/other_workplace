/**
 *
 */
package com.jiuyuan.service.common;

import java.math.BigDecimal;
import java.util.*;

import com.jiuyuan.dao.mapper.store.SalesVolumeProductMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductsMapper;
import com.jiuyuan.service.common.job.ProductJobService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.SalesVolumeProduct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProductDetailMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.ProductDetail;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.ProductNewStateEnum;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.UserNew;
import org.springframework.util.ObjectUtils;

/**
 * 新商品SKU服务
 */

@Service
public class ProductNewService implements IProductNewService{
    private static final Logger logger = LoggerFactory.getLogger(ProductNewService.class);
    /**
     * 商品状态, 正常
     */
    private static final int STATUS_NORMAL = 0;
    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;
    @Autowired
    private ProductNewMapper productNewMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private IUserNewService supplierUserService;
    @Autowired
    private IProductAuditService productAuditService;
    @Autowired
    private IUserNewService userNewService;
    @Autowired
    private IProductSkuNewService productSkuNewService;
    @Autowired
    private ProductJobService productJobService;
    @Autowired
    private ShopProductsMapper shopProductMapper;

    @Autowired
    private SalesVolumeProductMapper salesVolumeProductMapper;

    /**
     * 获取平台商品状态
     *
     * @param productId
     * @return 平台商品状态:0已上架、1已下架、2已删除
     */
    @Override
    public String getPlatformProductState(long productId) {
        String platformProductState = "2";
        long time4_3_3_1 = System.currentTimeMillis();
        ProductNew productNew = productNewMapper.selectById(productId);
        int delState = productNew.getDelState();//删除状态： 0（未删除）、 1（已删除）
        if (delState == 0) {
            //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            int state = productNew.getState();
            if (state == 6) {//6（上架，审核通过、已上架）
                platformProductState = "0";
            }
            else {
                platformProductState = "1";
            }
        }
        else {
            platformProductState = "2";
        }
        long time4_3_3_2 = System.currentTimeMillis();
//		logger.info("获取平台商品状态time4_3_3_2："+(time4_3_3_2-time4_3_3_1));
        return platformProductState;
    }

    /**
     * 获取平台商品状态
     *
     * @param productNew
     * @return 平台商品状态:0已上架、1已下架、2已删除
     */
    @Override
    public String getPlatformProductState(ProductNew productNew) {
        String platformProductState = "2";
        long time4_3_3_1 = System.currentTimeMillis();

        int delState = productNew.getDelState();//删除状态： 0（未删除）、 1（已删除）
        if (delState == 0) {
            //商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            int state = productNew.getState();
            if (state == 6) {//6（上架，审核通过、已上架）
                platformProductState = "0";
            }
            else {
                platformProductState = "1";
            }
        }
        else {
            platformProductState = "2";
        }
        long time4_3_3_2 = System.currentTimeMillis();
//		logger.info("获取平台商品状态time4_3_3_2："+(time4_3_3_2-time4_3_3_1));
        return platformProductState;
    }


    /**
     * 根据主键获取商品
     */
    @Override
    public ProductNew getProductById(long productId) {
        return productNewMapper.selectById(productId);
    }

    /**
     * 商品设置为审核通过
     */
    @Override
    public void productAuditPass(long productId) {
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setState(ProductNewStateEnum.wait_up_sold.getIntValue());
        product.setAuditTime(System.currentTimeMillis());
        productNewMapper.updateById(product);
    }

    /**
     * 验证款号可用
     */
    @Override
    public boolean checkClothesNumberUsable(long supplierId, long productId, String clothesNumber) {
        BrandNew supplierBrand = supplierUserService.getSupplierBrandInfoBySupplierId(supplierId);//获取供应商品牌信息
        String clothesNumberPrefix = supplierBrand.getClothNumberPrefix();//品牌款号前缀
        if (StringUtils.isEmpty(clothesNumberPrefix)) {
            logger.info("当前品牌款号前缀为空，请尽快填充clothesNumberPrefix:" + clothesNumberPrefix + ",supplierBrand:" + JSON.toJSONString(supplierBrand));
        }
//		logger.info("前缀clothesNumberPrefix:"+clothesNumberPrefix);
//		logger.info("添加前缀前clothesNumber:"+clothesNumber);
        clothesNumber = clothesNumberPrefix + clothesNumber;
//		logger.info("添加前缀后clothesNumber:"+clothesNumber);
        ProductNew product = getProductByClothesNumber(clothesNumber);
        if (product == null) {//商品不存在则可用
//			logger.info("product为空");
            return true;
        }
        else if (product.getId() == productId) {//商品ID为自身商品则也为可用
            logger.info("product.getId()" + product.getId() + ",productId:" + productId);
            return true;
        }
        return false;
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getSearchProductList(com.baomidou.mybatisplus.plugins.Page, java.lang.String, java.lang.String, boolean)
     */
    @Override
    public List<ProductNew> getSearchProductList(Page<ProductNew> page, String keyword, String orderByField, boolean asc) {
        List<ProductNew> result = productNewMapper.getSearchProductList(page, keyword, orderByField, asc);
        return result;
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getProductDetail(long)
     */
    @Override
    public ProductDetail getProductDetail(long productId) {
        Wrapper<ProductDetail> wrapper = new EntityWrapper<ProductDetail>();
        wrapper.eq("productId", productId);//
        List<ProductDetail> list = productDetailMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getSearchProductList(long, java.lang.String, java.lang.String, long, long, long, double, double, int, int, com.baomidou.mybatisplus.plugins.Page)
     */
    @Override
    public List<ProductNew> getSearchProductList(long supplierId, String clothesNumber, String productName, long state,
                                                 long upSoldTimeBegin, long upSoldTimeEnd, double priceBegin, double priceEnd, int salesCountBegin,
                                                 int salesCountEnd, Page<Map<String, Object>> page) {

        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("ClothesNumber", clothesNumber);
        }
        if (StringUtils.isNotEmpty(productName)) {
            wrapper.like("Name", productName);
        }

        if (state != - 1) {
            wrapper.eq("state", state);
        }
        if (salesCountBegin > 0) {
            wrapper.ge("SaleTotalCount", salesCountBegin);
        }
        if (salesCountEnd > 0) {
            wrapper.le("SaleTotalCount", salesCountEnd);
        }
        if (upSoldTimeBegin > 0) {
            wrapper.ge("SaleStartTime", upSoldTimeBegin);//上架时间大于商家开始时间
        }
        if (upSoldTimeEnd > 0) {
            wrapper.le("SaleStartTime", upSoldTimeEnd);//上架时间小于上架结束时间
        }
        if (priceBegin > 0) {
            wrapper.ge("minLadderPrice", priceBegin);//小于最大阶梯价
        }
        if (priceEnd > 0) {
            wrapper.le("maxLadderPrice", priceEnd);//大于最小阶梯价
        }


        wrapper.eq("delState", ProductNew.delState_false);
        wrapper.eq("supplierId", supplierId);//
        wrapper.orderBy("UpdateTime", false);
        List<ProductNew> productList = productNewMapper.selectPage(page, wrapper);
        return productList;
    }


    @Override
    @Transactional( rollbackFor = Exception.class )
    public void upSoldProduct(long productId) {
        long time = System.currentTimeMillis();
        //如果待上架或者下架时才能改为待提审
        ProductNew productNew = productNewMapper.selectById(productId);
//		* 商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
        int state = productNew.getState();
        //如果下架或者待上架时才能上架
        if (state == ProductNewStateEnum.wait_up_sold.getIntValue() || state == ProductNewStateEnum.down_sold.getIntValue()) {
            //1、批量下架SKU
            //		获取商品下架SKU列表
            Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
            wrapper.eq("ProductId", productId);//
            wrapper.eq("Status", ProductSkuNew.down_sold);//状态:-3废弃，-2停用，-1下架，0正常，1定时上架
            List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
            //循环修改为上架状态
            for (ProductSkuNew sku : skuList) {
                ProductSkuNew newSku = new ProductSkuNew();
                newSku.setId(sku.getId());
                newSku.setStatus(ProductSkuNew.up_sold);
                newSku.setUpdateTime(time);
                productSkuNewMapper.updateById(newSku);
                logger.info("商品SKU下架完成，productId" + productId + ",sku:" + sku.getId());
            }
            logger.info("商品下架完成，productId" + productId);

            //2、修改商品为上架状态
            ProductNew product = new ProductNew();
            product.setId(productId);
            product.setState(ProductNewStateEnum.up_sold.getIntValue());
            product.setUpSoldTime(time);
            product.setUpdateTime(time);
            Long lastPutOnTime = productNew.getLastPutonTime();
            // 如果首次上架时间为空 则更新上架时间
            boolean isFirstPutOn = BizUtil.hasEmpty(lastPutOnTime) || lastPutOnTime == 0;
            if (isFirstPutOn) {
                product.setLastPutonTime(System.currentTimeMillis());
            }
            productNewMapper.updateById(product);

            // 首次上架时添加定时修改库存任务
            if (isFirstPutOn) {
                List<ProductSkuNew> taskList = new ArrayList<>(skuList.size());
                for (ProductSkuNew sku : skuList) {
                    if (sku.getTimingSetType() == 2) {
                        taskList.add(sku);
                    }
                }
                if (! taskList.isEmpty()) {
                    productNew.setLastPutonTime(product.getLastPutonTime());
                    productSkuNewService.startTimingSetRemainCountAfterOnShelves(productNew, taskList, productNew.getSupplierId());
                }
            }
        }
        else {
            logger.info("该商品状态不能上架，state：" + state + ",productNew.getId()" + productNew.getId());
            throw BizException.defulat().msg("该商品状态不能上架");
        }

    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#downSoldProduct(long)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void downSoldProduct(long productId) {
        long time = System.currentTimeMillis();

        //1、批量下架SKU
//		获取商品上架SKU列表
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);//
        wrapper.eq("Status", ProductSkuNew.up_sold);// 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
        //循环修改为下架状态
        for (ProductSkuNew sku : skuList) {
            ProductSkuNew newSku = new ProductSkuNew();
            newSku.setId(sku.getId());
            newSku.setStatus(ProductSkuNew.down_sold);
            productSkuNewMapper.updateById(newSku);
            logger.info("商品SKU下架完成，productId" + productId + ",skuId:" + sku.getId());
        }
        logger.info("商品下架完成，productId" + productId);

        //2、修改商品状态为下架
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setState(ProductNewStateEnum.down_sold.getIntValue());
        product.setDownSoldTime(time);
        product.setUpdateTime(time);
        productNewMapper.updateById(product);

        // 手动下架后,将自动定时状态修改为商品审核后立刻上架,用于区分下架后的商品选择定时上架任务时是否需要回显
        ProductDetail entity = new ProductDetail();
        entity.setProductId(productId);
        ProductDetail productDetail = productDetailMapper.selectOne(entity);
        if (BizUtil.isNotEmpty(productDetail)) {
            productDetail.setPutawayType(1);
            productDetail.setTimingPutwayTime(0L);
            productDetailMapper.updateById(productDetail);
        }
        /**
         * 小程序 下架
         */
        shopProductMapper.soldOut(productId);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#productSubmitAudit(long)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void productSubmitAudit(long productId) {
        //1、添加客服审核记录
        productAuditService.addServerProductAudit(productId);
        long time = System.currentTimeMillis();
        //2、修改商品状态
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setState(ProductNewStateEnum.audit_ing.getIntValue());
        product.setSubmitAuditTime(time);
        product.setUpdateTime(time);
        productNewMapper.updateById(product);
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#productMapOfIds(java.util.Set)
     */
    @Override
    public Map<Long, ProductNew> productMapOfIds(Set<Long> productIds) {
        if (productIds == null) {
            Map<Long, ProductNew> productMap = new HashMap<>();

            List<ProductNew> products = productNewMapper.getAllProducts();
            for (ProductNew product : products) {
                productMap.put(product.getId(), product);
            }

            return productMap;
        }

        if (productIds.size() < 1) {
            return new HashMap<Long, ProductNew>();
        }

        Map<Long, ProductNew> productMap = new HashMap<>();

        List<ProductNew> products = productNewMapper.productOfIds(productIds);
        for (ProductNew product : products) {
            productMap.put(product.getId(), product);
        }

        return productMap;
    }


//	/**
//	 * 获取商品SKU列表
//	 * @param productId
//	 * @return
//	 */
//	public List<ProductSkuNew> getProductSkuList(long productId) {
//		Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
//		wrapper.eq("ProductId",productId);//
//		List<ProductSkuNew> list = productSkuNewMapper.selectList(wrapper);
//		return list;
//	}


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getProductDatailByProductId(long)
     */
    @Override
    public ProductDetail getProductDatailByProductId(long productId) {
        Wrapper<ProductDetail> wrapper = new EntityWrapper<ProductDetail>();
        wrapper.eq("productId", productId);//
        List<ProductDetail> list = productDetailMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getProductByClothesNumber(java.lang.String)
     */
    @Override
    public ProductNew getProductByClothesNumber(String clothesNumber) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("ClothesNumber", clothesNumber);//
        List<ProductNew> list = productNewMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    /* (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#productAuditNoPass(long, java.lang.String)
     */
    @Override
    public void productAuditNoPass(long productId, String noPassReason) {
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setState(ProductNewStateEnum.audit_no_pass.getIntValue());
        product.setAuditTime(System.currentTimeMillis());
        product.setAuditNoPassReason(noPassReason);
        logger.info("商品审核不通过修改商品信息product:" + JSON.toJSONString(product));
        productNewMapper.updateById(product);
    }

    @Override
    public void updateSaleCount(long productId, int totalBuyCount) {
        ProductNew productNew = productNewMapper.selectById(productId);
        productNew.setSaleTotalCount(productNew.getSaleTotalCount() + totalBuyCount);
        productNewMapper.updateById(productNew);
    }

    @Override
    public List<Map<String, Object>> getSearchProductListNew(long supplierId, Long timingPutawayTimeFloor, Long timingPutawayTimeCeil, String clothesNumber, String productName,
                                                             long state, long upSoldTimeBegin, long upSoldTimeEnd, double priceBegin, double priceEnd,
                                                             int salesCountBegin, int salesCountEnd, Page<Map<String, Object>> page, int totalSkuCountBegin,
                                                             int totalSkuCountEnd, int orderType) {
        List<Map<String, Object>> productList = productNewMapper.selectPageList2(page, supplierId
                , clothesNumber, productName, state, upSoldTimeBegin, upSoldTimeEnd
                , priceBegin, priceEnd, salesCountBegin, salesCountEnd, totalSkuCountBegin
                , totalSkuCountEnd, orderType
                , timingPutawayTimeFloor, timingPutawayTimeCeil
        );
        if (page.getTotal()==0){
            int size = productNewMapper.selectPageListSize(page, supplierId
                    , clothesNumber, productName, state, upSoldTimeBegin, upSoldTimeEnd
                    , priceBegin, priceEnd, salesCountBegin, salesCountEnd, totalSkuCountBegin
                    , totalSkuCountEnd, orderType
                    , timingPutawayTimeFloor, timingPutawayTimeCeil
            );
            logger.info("插件不起效手动插入total={}",size);
            page.setTotal(size);
        }


        if (!productList.isEmpty ()) {
            //查询sku数量,库存总量
            List<Long> pIds = new ArrayList<> (productList.size ());
            for (Map<String, Object> map : productList) {
                long productId = (long) map.get ("productId");
                pIds.add (productId);
            }
            List<Map<String, Object>> countAndRemainList =  productNewMapper.querySkuCountAndRemainCount(pIds);
            // 组装
            for (Map<String, Object> product : productList) {
                // sku总数,sku总库存
                putSkuCountAndRemainCount (countAndRemainList, product, (Long) product.get ("productId"));

                // 定时上架时间
                Long timingPutwayTime = (Long) product.get("timingPutwayTime");
                Integer putawayType = (Integer) product.get("putawayType");
                String time = putawayType == 2 ? DateUtil.parseLongTime2Str(timingPutwayTime) : "";
                product.put("timingPutwayTime", time);
            }

        }

//		long time3 = System.currentTimeMillis();
//		logger.info("SKU数量、库存量productList2："+(time3-time2));
//		List<Map<String,Object>> productList = productNewMapper.selectPageList(page,supplierId,clothesNumber,productName,state,upSoldTimeBegin,upSoldTimeEnd,priceBegin,priceEnd,salesCountBegin,salesCountEnd,totalSkuCountBegin,totalSkuCountEnd,orderType);
//		long time4 = System.currentTimeMillis();
//		logger.info("productList："+(time4-time3));

        return productList;
    }



    /**
     * 放入sku数量, 和sku总库存
     *
     * @param countAndRemainList sku总数,库存信息
     * @param product 商品map
     * @param id 商品id
     * @author Charlie
     * @date 2018/7/30 20:00
     */
    private void putSkuCountAndRemainCount(List<Map<String, Object>> countAndRemainList, Map<String, Object> product, Long id) {
        for (Map<String, Object> skuInfo : countAndRemainList) {
            Long skuCount = (Long) skuInfo.get ("skuCount");
            int skuRemainCount = ((BigDecimal) skuInfo.get ("skuRemainCount")).intValue ();
            Long productId = (Long) skuInfo.get ("productId");

            if (ObjectUtils.nullSafeEquals (productId, id)) {
                product.put ("skuCount", skuCount == null ? 0 : skuCount);
                product.put ("totalSkuCount", skuRemainCount);
                return;
            }
        }
        product.put ("skuCount", 0);
        product.put ("totalSkuCount", 0);
    }


    @Override
    public void waitProduct(long productId) {
        long time = System.currentTimeMillis();

        //1、批量下架SKU
//		获取商品上架SKU列表
        Wrapper<ProductSkuNew> wrapper = new EntityWrapper<ProductSkuNew>();
        wrapper.eq("ProductId", productId);//
        wrapper.eq("Status", ProductSkuNew.up_sold);// 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
        List<ProductSkuNew> skuList = productSkuNewMapper.selectList(wrapper);
        //循环修改为下架状态
        for (ProductSkuNew sku : skuList) {
            ProductSkuNew newSku = new ProductSkuNew();
            newSku.setId(sku.getId());
            newSku.setStatus(ProductSkuNew.down_sold);
            productSkuNewMapper.updateById(newSku);
            logger.info("商品SKU下架完成，productId" + productId + ",skuId:" + sku.getId());
        }
        logger.info("商品下架完成，productId" + productId);
        //2、修改商品状态为下架
        ProductNew product = new ProductNew();
        product.setId(productId);
        product.setState(ProductNewStateEnum.wait_submit_audit.getIntValue());
        product.setDownSoldTime(time);
        product.setUpdateTime(time);
        productNewMapper.updateById(product);

    }

    @Override
    /*
     * (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#selectProductPage(java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, long, com.baomidou.mybatisplus.plugins.Page)
     */
    public List<Map<String, Object>> selectProductPage(BigDecimal memberLadderPriceFloor, BigDecimal memberLadderPriceCeil,String productIds, String brandName, int state, double minLadderPriceStart,
                                                       double minLadderPriceEnd, String name, String categoryIds, String clothesNumber,
                                                       long badgeStatus, Page<Map<String, Object>> page) {

        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("delState", 0);

        //商品id不为空时直接按照商品id 进行查询
        if (StringUtils.isNotEmpty(productIds)) {
            wrapper.in("Id", productIds);
        }
        if (state == ProductNewStateEnum.up_sold.getIntValue()) {
            wrapper.eq("state", state); //上架状态
        }
        if (state != ProductNewStateEnum.up_sold.getIntValue() && state != - 1) {
            wrapper.eq("state", state); //不为-1和上架状态时（下架状态）
        }
        if (state == - 1) {
//            wrapper.in("state", "6,7"); //上架下架两种状态
        }
        if (StringUtils.isNotEmpty(brandName)) {
            wrapper.like("brandName", brandName);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("Name", name);
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            //根据分类进行沙宣
            String[] categoryArr = categoryIds.split(",");
            if (categoryArr.length == 1) {
                wrapper.eq("oneCategoryId", categoryIds);
            }
            if (categoryArr.length == 2) {
                wrapper.eq("twoCategoryId", categoryArr[1]);
            }
            if (categoryArr.length == 3) {
                wrapper.eq("threeCategoryId", categoryArr[2]);
            }
        }
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("ClothesNumber", clothesNumber);
        }
        if (minLadderPriceStart != 0) {
            wrapper.ge("maxLadderPrice", minLadderPriceStart);
        }
        if (minLadderPriceEnd != 0) {
            wrapper.le("maxLadderPrice", minLadderPriceEnd);
        }
        //角标状态0：未打角标  >0已打角标 且为角标id -1全部
        if (badgeStatus == - 2) {//已打标状态
            wrapper.ge("badge_id", 1);
        }
        if (badgeStatus == 0) {//未打标状态
            wrapper.eq("badge_id", 0);
        }
        if (badgeStatus >= 1) {//指定角标时
            wrapper.eq("badge_id", badgeStatus);
        }
//        if (BigDecimal memberLadderPriceFloor, BigDecimal memberLadderPriceCeil;)
        //低
        if (memberLadderPriceFloor != null) {
            wrapper.ge ("member_ladder_price_max", memberLadderPriceFloor);
            wrapper.ge ("memberLevel", 1);
        }
        //高
        if (memberLadderPriceCeil != null) {
            wrapper.le ("member_ladder_price_min", memberLadderPriceCeil);
            wrapper.ge ("memberLevel", 1);
        }

        //商品id为空时按照条件进行查询
        return selectProductList(wrapper, page);
    }

    /**
     * 查询商品
     *
     * @param wrapper
     * @param page
     * @return
     */
    private List<Map<String, Object>> selectProductList(Wrapper<ProductNew> wrapper, Page<Map<String, Object>> page) {
        List<Map<String, Object>> productMapList = new ArrayList<>();
        List<ProductNew> productNewList = productNewMapper.selectPage(page, wrapper);
        for (ProductNew productNew : productNewList) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", productNew.getId());
            map.put("name", productNew.getName());
            map.put("clothesNumber", productNew.getClothesNumber());
            map.put("brandName", productNew.getBrandName());
            map.put("oneCategoryName", productNew.getOneCategoryName());
            map.put("twoCategoryName", productNew.getTwoCategoryName());
            map.put("threeCategoryName", productNew.getThreeCategoryName());
            map.put("ladderPriceJson", productNew.getLadderPriceJson());
            map.put("memberLadderPriceJson", productNew.getMemberLadderPriceJson ());
            map.put("memberLevel", productNew.getMemberLevel ());
            map.put("state", productNew.getState());
            map.put("badgeId", productNew.getBadgeId());
            map.put("badgeName", productNew.getBadgeName());
            map.put("total", page.getTotal());
            map.put("saleTotalCount", productNew.getSaleTotalCount());
            productMapList.add(map);
        }
        return productMapList;
    }

    /**
     * 按照商品id 绑定角标
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int bindBadgeProduct(long badgeId, String badgeImage, String badgeName, String productIds) {
        String[] productIdarr = productIds.split(",");
        Set<Long> set = new HashSet<>();
        for (String string : productIdarr) {
            set.add(Long.valueOf(string));
        }
        return productNewMapper.bindBadgeProduct(badgeId, badgeImage, badgeName, set);
    }


    /**
     * 按照商品id 清除角标
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int clearProductBadge(String productIds) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().in("Id", productIds).ne("badge_id", 0).eq("delState", 0);
        List<ProductNew> productNewList = productNewMapper.selectList(wrapper);
        if (productNewList.size() < 1) {
            return 0;
        }
        Set<Long> set = new HashSet<>();
        for (ProductNew productNew : productNewList) {
            set.add(productNew.getId());
        }
        productNewMapper.clearProductBadge(set);
        return productNewList.size();
    }

    /**
     * 按照条件绑定角标
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int bindProductBadgeCondition(long badgeId, String badgeImage, String badgeName, String productIds, String brandName,
                                         int state, double minLadderPriceStart, double minLadderPriceEnd, String name, String categoryIds,
                                         String clothesNumber, long badgeStatus) {

        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("delState", 0);
        if (StringUtils.isNotEmpty(productIds)) {
            wrapper.in("Id", productIds);
        }
        if (state == ProductNewStateEnum.up_sold.getIntValue()) {
            wrapper.eq("state", state); //上架状态
        }
        if (state == ProductNewStateEnum.down_sold.getIntValue()) {
            wrapper.eq("state", state); //下架状态
        }
        if (state == - 1) {
            wrapper.in("state", "6,7"); //上架下架两种状态
        }
        if (StringUtils.isNotEmpty(brandName)) {
            wrapper.like("brandName", brandName);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("Name", name);
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            //根据分类进行沙宣
            String[] categoryArr = categoryIds.split(",");
            if (categoryArr.length == 1) {
                wrapper.eq("oneCategoryId", categoryIds);
            }
            if (categoryArr.length == 2) {
                wrapper.eq("twoCategoryId", categoryArr[1]);
            }
            if (categoryArr.length == 3) {
                wrapper.eq("threeCategoryId", categoryArr[2]);
            }
        }
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("ClothesNumber", clothesNumber);
        }
        if (minLadderPriceStart != 0) {
            wrapper.ge("maxLadderPrice", minLadderPriceStart);
        }
        if (minLadderPriceEnd != 0) {
            wrapper.le("maxLadderPrice", minLadderPriceEnd);
        }
        //角标状态0：未打角标  >0已打角标 且为角标id -1全部
        if (badgeStatus == - 2) {//已打标状态
            wrapper.ge("badge_id", 1);
        }
        if (badgeStatus == 0) {//未打标状态
            wrapper.eq("badge_id", 0);
        }
        if (badgeStatus >= 1) {//指定角标时
            wrapper.eq("badge_id", badgeStatus);
        }
//		if (badgeStatus == -1) {//全部时只绑定无角标的情况
//			wrapper.eq("badge_id", 1);
//		}
        List<ProductNew> list = productNewMapper.selectList(wrapper);
        for (ProductNew productNew : list) {
            ProductNew newProduct = new ProductNew();
            newProduct.setId(productNew.getId());
            newProduct.setBadgeImage(badgeImage);
            newProduct.setBadgeName(badgeName);
            newProduct.setBadgeId(badgeId);
            productNewMapper.updateById(newProduct);
        }
        return list.size();
    }


    /**
     * 按照条件清除商品绑定的角标
     */
    @Override
    public int clearProductBadgeCondition(String productIds, String brandName, int state, double minLadderPriceStart,
                                          double minLadderPriceEnd, String name, String categoryIds, String clothesNumber, long badgeStatus) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("delState", 0);
        if (StringUtils.isNotEmpty(productIds)) {
            wrapper.in("Id", productIds);
        }
        if (state == ProductNewStateEnum.up_sold.getIntValue()) {
            wrapper.eq("state", state); //上架状态
        }
        if (state == ProductNewStateEnum.down_sold.getIntValue()) {
            wrapper.eq("state", state); //下架状态
        }
        if (state == - 1) {
            wrapper.in("state", "6,7"); //上架下架两种状态
        }
        if (StringUtils.isNotEmpty(brandName)) {
            wrapper.like("brandName", brandName);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("Name", name);
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            //根据分类进行沙宣
            String[] categoryArr = categoryIds.split(",");
            if (categoryArr.length == 1) {
                wrapper.eq("oneCategoryId", categoryIds);
            }
            if (categoryArr.length == 2) {
                wrapper.eq("twoCategoryId", categoryArr[1]);
            }
            if (categoryArr.length == 3) {
                wrapper.eq("threeCategoryId", categoryArr[2]);
            }
        }
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("ClothesNumber", clothesNumber);
        }
        if (minLadderPriceStart != 0) {
            wrapper.ge("maxLadderPrice", minLadderPriceStart);
        }
        if (minLadderPriceEnd != 0) {
            wrapper.le("maxLadderPrice", minLadderPriceEnd);
        }
        //角标状态0：未打角标  >0已打角标 且为角标id -1全部
        if (badgeStatus == - 2) {//已打标状态
            wrapper.ge("badge_id", 1);
        }
        if (badgeStatus == 0) {//未打标状态
            wrapper.eq("badge_id", 0);
        }
        if (badgeStatus >= 1) {//指定角标时
            wrapper.eq("badge_id", badgeStatus);
        }
        if (badgeStatus == - 1) {//全部
            wrapper.ge("badge_id", 1);
        }
        List<ProductNew> list = productNewMapper.selectList(wrapper);
        for (ProductNew productNew : list) {
            ProductNew newProduct = new ProductNew();
            newProduct.setId(productNew.getId());
            newProduct.setBadgeId(0L);
            newProduct.setBadgeImage("");
            newProduct.setBadgeName("");
            productNewMapper.updateById(newProduct);
        }
        return list.size();
    }

    /*
     * (non-Javadoc)
     * @see com.jiuyuan.service.common.IProductNewService#getWillClearProductCount(java.lang.String, java.lang.String, int, double, double, java.lang.String, java.lang.String, java.lang.String, long)
     */
    @Override
    public int getWillClearProductCount(String productIds, String brandName, int state, double minLadderPriceStart,
                                        double minLadderPriceEnd, String name, String categoryIds, String clothesNumber, long badgeStatus) {

        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("delState", 0);
        if (StringUtils.isNotEmpty(productIds)) {
            wrapper.in("Id", productIds);
        }
        if (state == ProductNewStateEnum.up_sold.getIntValue()) {
            wrapper.eq("state", state); //上架状态
        }
        if (state == ProductNewStateEnum.down_sold.getIntValue()) {
            wrapper.eq("state", state); //不为-1和上架状态时（下架状态）
        }
        if (state == - 1) {
            wrapper.in("state", "6,7"); //上架下架两种状态
        }
        if (StringUtils.isNotEmpty(brandName)) {
            wrapper.like("brandName", brandName);
        }
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like("Name", name);
        }
        if (StringUtils.isNotEmpty(categoryIds)) {
            //根据分类进行沙宣
            String[] categoryArr = categoryIds.split(",");
            if (categoryArr.length == 1) {
                wrapper.eq("oneCategoryId", categoryIds);
            }
            if (categoryArr.length == 2) {
                wrapper.eq("twoCategoryId", categoryArr[1]);
            }
            if (categoryArr.length == 3) {
                wrapper.eq("threeCategoryId", categoryArr[2]);
            }
        }
        if (StringUtils.isNotEmpty(clothesNumber)) {
            wrapper.like("ClothesNumber", clothesNumber);
        }
        if (minLadderPriceStart != 0) {
            wrapper.ge("maxLadderPrice", minLadderPriceStart);
        }
        if (minLadderPriceEnd != 0) {
            wrapper.le("maxLadderPrice", minLadderPriceEnd);
        }
        //角标状态0：未打角标  >0已打角标 且为角标id -1全部
        if (badgeStatus == - 2) {//已打标状态
            wrapper.ge("badge_id", 1);
        }
        if (badgeStatus == 0) {//未打标状态
            wrapper.eq("badge_id", 0);
        }
        if (badgeStatus >= 1) {//指定角标时
            wrapper.eq("badge_id", badgeStatus);
        }
        if (badgeStatus == - 1) {//全部
            wrapper.ge("badge_id", 1);
        }
        Integer count = productNewMapper.selectCount(wrapper);
        return count;
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public void updateProductBrandInfo(long brandId, String brandName, String brandLogo) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>();
        wrapper.eq("delState", 0).eq("BrandId", brandId);
        List<ProductNew> list = productNewMapper.selectList(wrapper);
        List<Long> productIdList = new ArrayList<Long>();
        for (ProductNew productNew : list) {

            productIdList.add(productNew.getId());
            ProductNew newProduct = new ProductNew();
            newProduct.setId(productNew.getId());
            newProduct.setBrandLogo(brandLogo);
            newProduct.setBrandName(brandName);
            productNewMapper.updateById(newProduct);
        }
    }

    @Override
    public List<Map<String, Object>> getAllProductIds() {
        List<Map<String, Object>> list = productNewMapper.getAllProductIds();
        return list;
    }

    /**
     * 获取品牌首页商品列表
     */
    @Override
    public List<ProductNew> getBrandProductList(long storeId, long brandId, int type, String keyWord, Page<ProductNew> page) {

        Map<String,Object> param = new HashMap<>();
        param.put("Status", 0);
        param.put("state", 6);
        param.put("delState", 0);
        param.put("BrandId", brandId);
        param.put("keyWord",keyWord);

        String orderBy = "";
        switch (type){
            case 1:
                orderBy = "pro.rank asc";
                break;
            case 2:
                orderBy = "(pro.SaleTotalCount + IFNULL(svp.sales_volume,0)) desc";
                break;
            case 3:
                orderBy = "pro.upSoldTime asc";
                break;
            case 4:
                orderBy = "pro.upSoldTime desc";
                break;
            case 5:
                orderBy = "pro.maxLadderPrice asc";
                break;
            case 6:
                orderBy = "pro.maxLadderPrice desc";
                break;
            default:
                    break;
        }
        if(type!=4) {
            orderBy = orderBy+", upSoldTime desc";
        }
        param.put("orderBy",orderBy);
        return productNewMapper.selectProductWidthMonitor(page,param);
    }

//    /**
//     * 获取品牌首页商品列表
//     */
//    @Override
//    public List<ProductNew> getBrandProductList(long storeId, long brandId, int type, String keyWord, Page<ProductNew> page) {
//        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>()
//                .eq("Status", 0)
//                .eq("state", 6)
//                .eq("delState", 0)
//                .eq("BrandId", brandId);
//        if (! StringUtils.isEmpty(keyWord)) {
//            wrapper.and("(Name like" + " '%" + keyWord + "%' " + "or ClothesNumber like" + " '%" + keyWord + "%' )");
//        }
//        if (type == 4) {
//            wrapper.orderBy("upSoldTime", false);
//        }
//        else {
//            if (type == 1) {
//                wrapper.orderBy("rank");
//            }
//            if (type == 2) {
//                wrapper.orderBy("SaleTotalCount", false).orderBy("upSoldTime", false);
//            }
//            if (type == 3) {
//                wrapper.orderBy("upSoldTime", true);
//            }
//            if (type == 5) {
//                wrapper.orderBy("maxLadderPrice", true);
//            }
//            if (type == 6) {
//                wrapper.orderBy("maxLadderPrice", false);
//            }
//            wrapper.orderBy("upSoldTime", false);
//        }
//        //sku库存>0 && 状态>0(-3废弃，-2停用，-1下架，0正常，1定时上架) && 上架时间<当前时间 && (下架时间=0 || 下架时间大于当前时间)
//        wrapper.and(" Id IN ( SELECT sku.ProductId FROM yjj_ProductSKU sku WHERE "
////                + " sku.RemainCount>0 "
//                + " sku.Status >= 0 AND sku.SaleStartTime < UNIX_TIMESTAMP()*1000 AND ( sku.SaleEndTime = 0 OR sku.SaleEndTime > UNIX_TIMESTAMP()*1000)) ");
//        return productNewMapper.selectPage(page, wrapper);
//    }


    @Override
    public long getBrandProducCount(long brandId, String keyWord) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>()
                .eq("Status", 0)
                .eq("state", 6)
                .eq("delState", 0)
                .eq("BrandId", brandId);
        if (! StringUtils.isEmpty(keyWord)) {
            wrapper.and("(Name like" + " '%" + keyWord + "%' " + "or ClothesNumber like" + " '%" + keyWord + "%' )");
        }
        //sku库存>0 && 状态>0(-3废弃，-2停用，-1下架，0正常，1定时上架) && 上架时间<当前时间 && (下架时间=0 || 下架时间大于当前时间)
        wrapper.and(" Id IN ( SELECT sku.ProductId FROM yjj_ProductSKU sku WHERE "
//                + "sku.RemainCount>0 "
                + " sku.Status >= 0 AND sku.SaleStartTime < UNIX_TIMESTAMP()*1000 AND ( sku.SaleEndTime = 0 OR sku.SaleEndTime > UNIX_TIMESTAMP()*1000)) ");
        return productNewMapper.selectCount(wrapper);
    }


    /**
     * 获取品牌首页商品数量接口
     */
    @Override
    public int getBrandProductListCount(long storeId, long brandId) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("Status", 0).eq("state", 6).eq("delState", 0).eq("BrandId", brandId);
        return productNewMapper.selectCount(wrapper);
    }

    /**
     * 获取搭配商品的列表
     */
    @Override
    public List<Map<String, Object>> collocationList(String productIds, long storeId) {
        //商品IDs
        String[] productIdArray = productIds.split(",");
        //封装
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //获取各个商品的信息
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        Set<Long> brandIdSet = new LinkedHashSet<Long>();
        for (String productId : productIdArray) {
            if (productId.equals("")) {
                continue;
            }
            Long id = Long.parseLong(productId);
            ProductNew productNew = productNewMapper.selectById(id);
            Map<String, Object> skuMap = productSkuNewService.getSkuList(id);
            long brandId = productNew.getBrandId();
            Map<String, Object> productMap = new HashMap<String, Object>();
            //skuMap
            productMap.put("skuList", skuMap);
            //获取ID
            productMap.put("productId", productNew.getId());
            //历史限购
            productMap.put("RestrictHistoryBuy", productNew.getRestrictHistoryBuy());
            //单日限购
            productMap.put("RestrictDayBuy", productNew.getRestrictDayBuy());
            //获取主图
            productMap.put("mainImg", productNew.getMainImg());
            //获取款号
            productMap.put("clothesNumber", productNew.getClothesNumber());
            //获取商品标题
            productMap.put("name", productNew.getName());
            //获取阶梯价
            productMap.put("ladderPriceJson", productNew.getLadderPriceJson());
            //获取最大阶梯价和最低阶梯价
            productMap.put("maxLadderPrice", productNew.getMaxLadderPrice());
            productMap.put("minLadderPrice", productNew.getMinLadderPrice());
            //获取品牌
            int brandIdSizeBeforeAdd = brandIdSet.size();
            brandIdSet.add(brandId);
            int brandIdSizeAfterAdd = brandIdSet.size();
            if (brandIdSizeBeforeAdd < brandIdSizeAfterAdd) {
                Map<String, Object> map = new HashMap<String, Object>();
                UserNew userNew = userNewService.getSupplierByBrandId(brandId);
                map.put("brandId", brandId);//品牌ID
                map.put("brandName", productNew.getBrandName());//品牌名称
                map.put("wholesaleCost", userNew.getWholesaleCost());//混批起批金额设置
                map.put("wholesaleCount", userNew.getWholesaleCount());//混批起批数量设置
                //混批设置
                int wholesaleCount = userNew.getWholesaleCount();//批发起定量
                double wholesaleCost = userNew.getWholesaleCost();//批发起定额
                String wholesaleLimitTip = "";//批发限制提示文本
                if (wholesaleCount > 0) {
                    wholesaleLimitTip = wholesaleLimitTip + wholesaleCount + "件";
                }
                if (wholesaleCost > 0) {
                    if (wholesaleLimitTip.length() > 0) {
                        wholesaleLimitTip = wholesaleLimitTip + "、";
                    }
                    wholesaleLimitTip = wholesaleLimitTip + "￥" + wholesaleCost + "元";
                }
                if (wholesaleLimitTip.length() > 0) {
                    wholesaleLimitTip = wholesaleLimitTip + "起订";
                }

                map.put("wholesaleLimitTip", wholesaleLimitTip);//批发限制提示文本, 例子：10件、￥1000元 起订
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list.add(productMap);
                map.put("productInfo", list);//商品信息
                result.add(map);
            }
            else {
                for (Map<String, Object> map : result) {
                    if ((long) map.get("brandId") == brandId) {
                        if (map.get("productInfo") instanceof List) {
                            ((List<Map<String, Object>>) map.get("productInfo")).add(productMap);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获得sku列表
     */
    @Override
    public Map<String, Object> getSkuList(long productId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int remainCounts = 0;
        //颜色和尺码集合
        List<Map<String, Object>> colorList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> sizeList = new ArrayList<Map<String, Object>>();
        Set<Long> colorSet = new LinkedHashSet<Long>();
        Set<Long> sizeSet = new LinkedHashSet<Long>();
        //获取商品信息
        ProductNew productNew = productNewMapper.selectById(productId);
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("name", productNew.getName());//商品标题
        productMap.put("ladderPriceJson", productNew.getLadderPriceJson());//商品阶梯价
        productMap.put("clothesNumber", productNew.getClothesNumber());//商品款号
        productMap.put("mainImg", productNew.getMainImg());//商品主图
        resultMap.put("productInfo", productMap);//商品信息
        //获取sku相关信息
        List<ProductSkuNew> list = productSkuNewService.getValidSkuListByProductId(productId);
        List<Map<String, Object>> skuList = new ArrayList<Map<String, Object>>();
        for (ProductSkuNew productSkuNew : list) {
            Map<String, Object> skumap = new HashMap<String, Object>();
            skumap.put("skuId", productSkuNew.getId());//skuId
            skumap.put("colorName", productSkuNew.getColorName());//颜色名称
            skumap.put("colorId", productSkuNew.getColorId());//颜色ID
            skumap.put("sizeId", productSkuNew.getSizeId());//尺寸ID
            skumap.put("sizeName", productSkuNew.getSizeName());//尺寸名称
            int remainCount = productSkuNew.getRemainCount();
            skumap.put("remainCount", remainCount);//库存
            skuList.add(skumap);

            //添加商品库存
            remainCounts += remainCount;

            //添加颜色集合
            int colorBeforeAdd = colorSet.size();
            colorSet.add(productSkuNew.getColorId());
            int colorSizeAfterAdd = colorSet.size();
            if (colorBeforeAdd < colorSizeAfterAdd) {
                Map<String, Object> colorMap = new HashMap<String, Object>();
                colorMap.put("colorId", productSkuNew.getColorId());
                colorMap.put("colorName", productSkuNew.getColorName());
                colorList.add(colorMap);
            }

            //添加尺寸集合
            int sizeBeforeAdd = sizeSet.size();
            sizeSet.add(productSkuNew.getSizeId());
            int sizeAfterAdd = sizeSet.size();
            if (sizeBeforeAdd < sizeAfterAdd) {
                Map<String, Object> sizeMap = new HashMap<String, Object>();
                sizeMap.put("sizeId", productSkuNew.getSizeId());
                sizeMap.put("sizeName", productSkuNew.getSizeName());
                sizeList.add(sizeMap);
            }

        }
        //获取颜色
        resultMap.put("colors", colorList);
        //获取尺码
        resultMap.put("sizes", sizeList);
        //获取总库存
        resultMap.put("remainCounts", remainCounts);
        //获取sku
        resultMap.put("skuList", skuList);
        return resultMap;
    }


    @Override
    public boolean validateRank(long supplierId, int rank) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("rank", rank).eq("supplierId", supplierId).eq("delState", 0);
        Integer selectCount = productNewMapper.selectCount(wrapper);
        if (selectCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public void setRank(ProductNew newProduct) {
        productNewMapper.updateById(newProduct);
    }

    @Override
    public int getBrandProductListCount(long storeId, long brandId, int type, String keyWord) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<ProductNew>().eq("Status", 0).eq("state", 6).eq("delState", 0).eq("BrandId", brandId);
        if (! StringUtils.isEmpty(keyWord)) {
            wrapper.like("Name", keyWord);
        }
        if (type == 1) {
            //(T0DO) 仇约帆-按供应商后台设置的商品的推荐权重数字 顺序
            wrapper.orderBy("");
        }
        if (type == 2) {
            wrapper.orderBy("SaleTotalCount", false).orderBy("upSoldTime", false);
        }
        if (type == 3) {
            wrapper.orderBy("upSoldTime", true);
        }
        if (type == 4) {
            wrapper.orderBy("upSoldTime", false);
        }
        if (type == 5) {
            wrapper.orderBy("maxLadderPrice", true);
        }
        if (type == 6) {
            wrapper.orderBy("maxLadderPrice", false);
        }
        return productNewMapper.selectCount(wrapper);
    }

    @Override
    public List<ProductNew> getProductNewListByTagId(int limit, int offset, int tagId) {
        logger.info("getProductNewListByTagId:limit:" + limit + ",offset:" + offset + ",tagId:" + tagId);
        return productNewMapper.getProductNewListByTagId(limit, offset, tagId);
    }

    @Override
    public int getProductNewListByTagIdCount(int tagId) {
        int count = productNewMapper.getProductNewListByTagIdCount(tagId);
        return count;
    }


    /**
     * 获取某一商品的库存总数
     *
     * @param productId
     * @return int
     * @auther Charlie(唐静)
     * @date 2018/5/30 17:03
     */
    @Override
    public int countSkusRemain(Long productId) {
        return productNewMapper.countSkusRemain(productId);
    }


    /**
     * job回调,商品定时上架
     *
     * @param supplierId 供应商id
     * @param productId  商品id
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/5/30 18:28
     */
    @Override
    public void productPutawayFromJob(Long supplierId, Long productId, String token) {

        //todo... 服务调用者认证 token


        /* 校验商品是供应商所属商品 */
        ProductNew entity = new ProductNew();
        entity.setId(productId);
        entity.setSupplierId(supplierId);
        ProductNew product = productNewMapper.selectOne(entity);
        String msg;
        if (! BizUtil.isNotEmpty(product)) {
            msg = "商品定时上架失败, 未找到商品信息, {\"productId\":" + productId + ",\"supplierId\":" + supplierId + "}";
            logger.warn(msg);
            throw BizException.defulat().msg(msg);
        }

        if (product.getStatus() != 0) {
            msg = "商品定时上架失败, 商品已删除 productId:" + productId + ";supplierId:" + supplierId;
            logger.warn(msg);
            throw BizException.defulat().msg(msg);
        }

        // 校验商品详情
        Wrapper<ProductDetail> filter = new EntityWrapper<>();
        filter.eq("productId", productId);
        List<ProductDetail> detailList = productDetailMapper.selectList(filter);
        if (null == detailList || detailList.isEmpty()) {
            throw BizException.defulat().msg("未找到商品详情 state:" + product.getState() + ";productId:" + productId + ";supplierId:" + supplierId);
        }

        if (product.getState() == 5 || product.getState() == 7) {
            try {
                // 上架
                upSoldProduct(productId);
                return;
            } catch (Exception e) {
                msg = "商品定时上架失败, 更新数据库失败 state:" + product.getState() + ";productId:" + productId + ";supplierId:" + supplierId + "@@exception: " + e;
                logger.error(msg);
                throw BizException.defulat().msg(msg);
            }
        }
        msg = "商品定时上架失败, 商品状态不是待上架或者上状态 state:" + product.getState() + ";productId:" + productId + ";supplierId:" + supplierId;
        logger.warn(msg);
        throw BizException.defulat().msg(msg);
    }


    /**
     * 查询根据商品ids查询商品
     *
     * @param ids
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
     * @auther Charlie(唐静)
     * @date 2018/6/10 16:01
     */
    @Override
    public List<ProductNew> selectByIds(List<Long> ids) {
        return productNewMapper.selectBatchIds(ids);
    }


    /**
     * 通过productId 查询product信息
     *
     * @param productIds
     * @return Map key:productId, value object
     * @auther Charlie(唐静)
     * @date 2018/6/11 17:46
     */
    @Override
    public Map<Long, ProductNew> productByIds(Set<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new HashMap<>(0);
        }

        List<ProductNew> productList = productNewMapper.selectBatchIds(new ArrayList<>(productIds));
        if ((null == productList) || productList.isEmpty()) {
            return new HashMap<>(0);
        }

        Map<Long, ProductNew> map = new HashMap<>(productList.size());
        for (ProductNew product : productList) {
            map.put(product.getId(), product);
        }
        return map;
    }



    /**
     * 活动商品专场页
     *
     * @param pageQuery 分页
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
     * @author Charlie
     * @date 2018/8/13 16:03
     */
    @Override
    public List<ProductNew> memberProductSpecial(Page<ProductNew> pageQuery) {
        int memberLevel = 1;
        List<ProductNew> products = findProductByMemberLevel (memberLevel, pageQuery);
        if (products.isEmpty ()) {
            return products;
        }

        List<Long> productIds = new ArrayList<> (products.size ());
        for (ProductNew product : products) {
            productIds.add (product.getId ());
        }
        Wrapper<SalesVolumeProduct> wrapper = new EntityWrapper<> ();
        wrapper.in ("product_id", productIds);
        wrapper.eq ("product_type", 1);
        List<SalesVolumeProduct> svList = salesVolumeProductMapper.selectList (wrapper);

        List<ProductNew> result = new ArrayList<> (products.size ());
        products.forEach (action->{
            ProductNew elem = new ProductNew ();
            elem.setId (action.getId ());
            elem.setDetailImages (action.getDetailImages ());
            elem.setName (action.getName ());
            elem.setMemberLevel (action.getMemberLevel ());
            elem.setMinLadderPrice (action.getMinLadderPrice ());
            elem.setMemberLadderPriceMin (action.getMemberLadderPriceMin ());
            Long virtual = 0L;
            if (null != svList && ! svList.isEmpty ()) {
                for (SalesVolumeProduct sv : svList) {
                    if (sv.getProductId ().equals (action.getId ())) {
                        virtual = sv.getSalesVolume ();
                        break;
                    }
                }
            }
            elem.setSaleTotalCount (action.getSaleTotalCount () + virtual.intValue ());
            result.add (elem);
        });
        return result;
    }



    /**
     * 设置商品会员价
     *
     * @param productId 商品id
     * @param memberPriceJson 会员价json字符串
     * @param memberLevel 会员等级
     * @author Charlie
     * @date 2018/8/14 8:53
     */
    @Override
    public void updateMemberPrice(Long productId, String memberPriceJson, Integer memberLevel) {
        boolean nullParams = BizUtil.isEmpty (productId, memberLevel);
        if (nullParams) {
            throw BizException.defulat ().msg ("请求参数不可为空 productId:" + productId + ",memberLevel:" + memberLevel);
        }

        if (memberLevel > 0 && StringUtils.isBlank (memberPriceJson)) {
            throw BizException.defulat ().msg ("会员商品的会员阶梯价格不可为空");
        }

        ProductNew product = productNewMapper.selectById (productId);
        if (product == null) {
            throw BizException.defulat ().msg ("未找到商品信息");
        }

        //普通商品
        if (memberLevel == 0) {
            //普通商品--->普通商品
            if (memberLevel.equals (product.getMemberLevel ())) {
                return;
            }
            //会员商品--->普通商品
            product.setMemberLevel (memberLevel);
            BigDecimal defaultPrice = new BigDecimal ("0.00");
            product.setMemberLadderPriceMin (defaultPrice);
            product.setMemberLadderPriceMax (defaultPrice);
            product.setMemberLadderPriceJson ("");
            product.setUpdateTime (System.currentTimeMillis ());
            Integer rec = productNewMapper.updateById (product);
            if (rec != 1) {
                throw BizException.defulat ().msg ("更新商品会员信息失败");
            }
        }
        else {
            //设置商品是会员商品
            if (memberLevel.equals (product.getMemberLevel ()) && memberPriceJson.equals (product.getMemberLadderPriceJson ())) {
                //没有修改
                return;
            }
            Map<Integer, LadderPriceVo> memberPriceVo = LadderPriceVo.build (memberPriceJson);
            Map<Integer, LadderPriceVo> priceVo = LadderPriceVo.build (product.getLadderPriceJson ());
            verifyMemberPrice (memberPriceVo, priceVo);

            BigDecimal memMaxPrice = LadderPriceVo.acquireMaxPrice (memberPriceVo);
            BigDecimal memMinPrice = LadderPriceVo.acquireMinPrice (memberPriceVo);

            ProductNew newData = new ProductNew ();
            newData.setId (productId);
            newData.setMemberLevel (memberLevel);
            newData.setMemberLadderPriceMin (memMinPrice);
            newData.setMemberLadderPriceMax (memMaxPrice);
            newData.setMemberLadderPriceJson (memberPriceJson);
            newData.setUpdateTime (System.currentTimeMillis ());
            Integer rec = productNewMapper.updateById (newData);
            if (rec != 1) {
                throw BizException.defulat ().msg ("更新商品会员信息失败");
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResponse resetRank() {
        int reset = productNewMapper.resetRank();
        return JsonResponse.successful("重置完成"+reset+"条");
    }


    /**
     * 校验会员阶梯价格是否合法
     *
     * @param memberPriceVoMap memberPriceVoMap
     * @param priceVoMap priceVoMap
     * @author Charlie
     * @date 2018/8/14 9:51
     */
    private void verifyMemberPrice(Map<Integer,LadderPriceVo> memberPriceVoMap, Map<Integer,LadderPriceVo> priceVoMap) {
        if (memberPriceVoMap.size () != priceVoMap.size ()) {
            throw BizException.defulat ().msg ("阶梯价与会员阶梯价数量不匹配");
        }

        for (Map.Entry<Integer, LadderPriceVo> entry : memberPriceVoMap.entrySet ()) {
            Integer count = entry.getKey ();
            LadderPriceVo memLadderPriceVo = entry.getValue ();
            LadderPriceVo ladderPriceVo = priceVoMap.get (count);
            if (null == ladderPriceVo) {
                throw BizException.defulat ().msg ("未找到阶梯价对应的会员价 购买阶梯商品数量:" + count);
            }

            BigDecimal mPrice = memLadderPriceVo.getPrice ();
            if (mPrice.compareTo (new BigDecimal ("0.00")) <= 0) {
                throw BizException.defulat ().msg ("会员价格不可小于0 会员阶梯价格:" + mPrice);
            }
            if (ladderPriceVo.getPrice ().compareTo (mPrice) < 0) {
                throw BizException.defulat ().msg ("会员价格不可大于阶梯价格 阶梯价格:" + ladderPriceVo.getPrice () + ",会员价:" + mPrice);
            }
        }
    }


    /**
     * 通过商品会员等级获取商品列表
     *
     * <p>
     *     这个sql以后可以优化, 不要把文本信息查出来
     * </p>
     * @param memberLevel 会员等级
     *                    <p>
     *                    如果是1, 则返回等级>=1的所有商品
     *                    </p>
     * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
     * @author Charlie
     * @date 2018/8/13 16:14
     */
    private List<ProductNew> findProductByMemberLevel(Integer memberLevel, Page<ProductNew> pageQuery) {
        Wrapper<ProductNew> wrapper = new EntityWrapper<> ();
        wrapper.ge ("memberLevel", memberLevel)
                .eq ("state", 6)
                .eq ("delState", STATUS_NORMAL);
        wrapper.orderBy ("upSoldTime", false);
        return productNewMapper.selectPage (pageQuery, wrapper);
    }





    /**============================ 价格梯度工具类,因为gson包引的不是common工程,先放这里 =============================
     * 会员阶梯价格/阶梯价格 vo
     *
     * @author Charlie
     * @version V1.0
     * @date 2018/8/14 9:29
     * @Copyright 玖远网络
     */
    public static class LadderPriceVo{
        private Integer count;
        private BigDecimal price;

        private LadderPriceVo(){}

        /**
         * 格式化商品阶梯价格
         *
         * @param json json
         * @return key:阶梯商品数量, value:阶梯价格
         * @author Charlie
         * @date 2018/8/14 9:38
         */
        public static Map<Integer, LadderPriceVo> build(String json){
            List<LadderPriceVo> vos = build2List(json);
            if (vos.isEmpty ()) {
                return new HashMap<> (0);
            }
            else {
                HashMap map = new HashMap<Integer, LadderPriceVo> (vos.size ());
                vos.forEach (action -> {
                    LadderPriceVo vo = new LadderPriceVo ();
                    vo.count = action.getCount ();
                    vo.price = action.getPrice ();
                    map.put (vo.count, vo);
                });

                return map;
            }
        }

        /**
         * 格式化商品阶梯价格
         *
         * @param json json
         * @return list
         * @author Charlie
         * @date 2018/8/14 9:38
         */
        public static List<LadderPriceVo> build2List(String json){
            return BizUtil.jsonStrToListObject (json, List.class, LadderPriceVo.class);
        }


        /**
         * 获取最大阶梯价
         *
         * @param map map
         * @return java.math.BigDecimal
         * @author Charlie
         * @date 2018/8/14 10:11
         */
        public static BigDecimal acquireMinPrice(Map<Integer,LadderPriceVo> map) {
            if (map == null || map.isEmpty ()) {
                throw BizException.defulat ().msg ("没有阶梯价格");
            }
            //批发最大数量的价格是最小阶梯价格
            Iterator<Integer> it = map.keySet ().iterator ();
            Integer maxCount = null;
            while (it.hasNext ()) {
                Integer next = it.next ();
                if (maxCount == null) {
                    maxCount = next;
                }
                else {
                    if (maxCount.compareTo (next) < 0) {
                        maxCount = next;
                    }
                }
            }
            return map.get (maxCount).getPrice ();
        }



        /**
         * 获取最小阶梯价
         *
         * @param map map
         * @return java.math.BigDecimal
         * @author Charlie
         * @date 2018/8/14 10:11
         */
        public static BigDecimal acquireMaxPrice(Map<Integer,LadderPriceVo> map) {
            if (map == null || map.isEmpty ()) {
                throw BizException.defulat ().msg ("没有阶梯价格");
            }
            //批发最小数量的价格是最大阶梯价格
            Iterator<Integer> it = map.keySet ().iterator ();
            Integer minCount = null;
            while (it.hasNext ()) {
                Integer next = it.next ();
                if (minCount == null) {
                    minCount = next;
                }
                else {
                    if (minCount.compareTo (next) > 0) {
                        minCount = next;
                    }
                }
            }
            return map.get (minCount).getPrice ();
        }



        public void setCount(Integer count) {
            this.count = count;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getCount() {
            return count;
        }
        public BigDecimal getPrice() {
            return price;
        }
    }
    //==============================================================================================================
}