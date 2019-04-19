package com.store.service;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.*;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.*;
import com.jiuyuan.service.common.area.JedisUtils;
import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.store.entity.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.constant.Client;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductProp;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.store.dao.mapper.ProductSKUMapper;
import com.store.service.brand.ShopBrandService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.util.ObjectUtils;


@Service
public class ProductFacadeShop{

    public final long DAY_MILLION_SECOND = 24 * 60 * 60 * 1000L;

    private static final String BRAND_HOT_SALE = "brand:hostSale:page:";
    private static final int BRAND_HOT_SALE_TYPE = 0;
    private static final String BRAND_RECOMMEND = "brand:recommend:page:";
    private static final int BRAND_RECOMMEND_TYPE = 1;

    private static final Log logger = LogFactory.get();
    @Autowired
    private ProductPropAssemblerShop productPropAssembler;
    @Autowired
    private ProductSKUMapper productSKUMapper;
    @Autowired
    private ProductServiceShop productService;
    @Autowired
    private IUserNewService userNewService;
    @Autowired
    private ISupplierCustomer supplierCustomerService;
//	    @Autowired
//	    private DataminingAdapter dataminingAdapter;

//	    @Autowired
//	    private FavoriteService favoriteService;

    @Autowired
    private ProductSKUService productSKUService;
    @Autowired
    private IProductNewService productNewService;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private ShopBrandService shopBrandService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private VisitService visitService;

//	    @Autowired
//	    private CommentFacade commentFacade;

    @Autowired
    private StoreFavoriteService storeFavoriteService;


    @Autowired
    private StoreBusinessServiceShop storeBusinessService;

    @Autowired
    private PropertyServiceShop propertyService;

    @Autowired
    private IProductMonitorService productMonitorService;

//	    @Autowired
//	    private BrandBusinessService brandBusinessService;

    @Autowired
    private ShoppingCartService shoppingCartService;

//	    @Autowired
//	    private VisitService visitService;

    @Autowired
    private IBrandNewService brandService;

    @Autowired
    private StoreCouponTemplateNewMapper storeCouponTemplateNewMapper;

    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;

    @Autowired
    private RestrictionActivityProductSkuMapper restrictionActivityProductSkuMapper;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private BrandNewMapper brandNewMapper;

    @Autowired
    private ProductNewMapper productNewMapper;

    /**
     * 获取当前商品在限购时间段内的购买件数记录
     *
     * @param productsItems
     * @return
     */
    private Map<String, Integer> getBuyCountLog(List<RestrictProductVO> productsItems) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int restrictCycle = productsItems.get(0).getRestrictCycle();

        long restrictDaySetBuyTime = (long) productsItems.get(0).getRestrictDayBuyTime();
        long restrictHistorySetBuyTime = (long) productsItems.get(0).getRestrictHistoryBuyTime();

        //获取历史记录的开始时间：取MAX(限购的设置时间, 按规定的周期开始时间) 离当天最近的时间
        long restrictDayTime = restrictDaySetBuyTime > getStartTime() ? restrictDaySetBuyTime : getStartTime();
        long restrictHistoryTime = restrictHistorySetBuyTime > (getStartTime() - restrictCycle * DAY_MILLION_SECOND) ?
                restrictHistorySetBuyTime : (getStartTime() - (restrictCycle * DAY_MILLION_SECOND));

        //今日已购买
        int daySum = 0;
        //历史周期内已购买
        int historySum = 0;
        for (RestrictProductVO product : productsItems) {
            if (product.getCreateTime() >= restrictDayTime) {
                daySum += product.getBuyCount();
            }
            else {
                break;
            }
        }

        for (RestrictProductVO product : productsItems) {
            if (product.getCreateTime() >= restrictHistoryTime) {
                historySum += product.getBuyCount();
            }
            else {
                break;
            }
        }

        map.put("daySum", daySum);
        map.put("historySum", historySum);

        return map;
    }


    /**
     * 获取符合限购条件的商品
     *
     * @param
     * @param productCountMap 参数:Map<productId, buyCount>
     * @return
     */
    public List<RestrictProductVO> getRestrictInfo(UserDetail userDetail, Map<Long, Integer> productCountMap) {
        List<RestrictProductVO> list = new ArrayList<RestrictProductVO>();
        //存在购买记录的商品id
        Set<Long> existProductIds = new HashSet<Long>();
        //商品按id分组的组集合
        Map<Long, List<RestrictProductVO>> productGroup = new HashMap<Long, List<RestrictProductVO>>();

        //获取用户购买这些商品的记录
        if (productCountMap.keySet().size() == 0) {
            return new ArrayList<RestrictProductVO>();
        }
        List<RestrictProductVO> products = productService.getBuyerLogByStoreOrder(userDetail.getId(), productCountMap.keySet());

        //对获取的商品集记录进行按id分组,组内按CreateTime降序排序
        sortProducts(products, productGroup, existProductIds);


        for (Long productId : productCountMap.keySet()) {
            //各自productId的List
            List<RestrictProductVO> productsItems = productGroup.get(productId);

            if (productsItems != null && productsItems.size() > 0) {
                int restrictDayBuy = productsItems.get(0).getRestrictDayBuy();
                int restrictHistoryBuy = productsItems.get(0).getRestrictHistoryBuy();
                int restrictCycle = productsItems.get(0).getRestrictCycle();

                //商品既不今日限购也不历史限购,不记录
                if (restrictDayBuy == - 1 && restrictHistoryBuy == - 1)
                    continue;

                Map<String, Integer> buyCountLog = new HashMap<String, Integer>();
                buyCountLog = getBuyCountLog(productsItems);

                int daySum = buyCountLog.get("daySum");
                int historySum = buyCountLog.get("historySum");
                int buyCount = productCountMap.get(productId);
                //如果 限购 && (历史购买+当前欲购买数  > 限购上限)
                if (((daySum + buyCount > restrictDayBuy) && restrictDayBuy != - 1) ||
                        (historySum + buyCount > restrictHistoryBuy) && restrictHistoryBuy != - 1) {
                    RestrictProductVO restrictProductVO = new RestrictProductVO();
                    restrictProductVO.setRestrictCycle(restrictCycle);
                    restrictProductVO.setRestrictHistoryBuy(restrictHistoryBuy);
                    restrictProductVO.setRestrictDayBuy(restrictDayBuy);
                    restrictProductVO.setProductId(productId);
                    restrictProductVO.setDaySum(daySum);
                    restrictProductVO.setHistorySum(historySum);
                    restrictProductVO.setBuyCount(buyCount);

                    list.add(restrictProductVO);
                }
            }
            else {
                Product product = productService.getProductById(productId);

                int restrictDayBuy = product.getRestrictDayBuy();
                int restrictHistoryBuy = product.getRestrictHistoryBuy();
                int restrictCycle = product.getRestrictCycle();

                int buyCount = productCountMap.get(productId);
                //如果 限购 && (历史购买+当前欲购买数  > 限购上限)
                if (((buyCount > restrictDayBuy) && restrictDayBuy != - 1) ||
                        (buyCount > restrictHistoryBuy) && restrictHistoryBuy != - 1) {
                    RestrictProductVO restrictProductVO = new RestrictProductVO();
                    restrictProductVO.setRestrictCycle(restrictCycle);
                    restrictProductVO.setRestrictHistoryBuy(restrictHistoryBuy);
                    restrictProductVO.setRestrictDayBuy(restrictDayBuy);
                    restrictProductVO.setProductId(productId);
                    restrictProductVO.setDaySum(0);
                    restrictProductVO.setHistorySum(0);
                    restrictProductVO.setBuyCount(buyCount);

                    list.add(restrictProductVO);
                }
            }
        }

        return list;
    }

    private Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    private void sortProducts(List<RestrictProductVO> products, Map<Long, List<RestrictProductVO>> productGroup,
                              Set<Long> existProductIds) {
        long lastProductId = 0;
        List<RestrictProductVO> buyProducts = null;

        //对获取到的商品记录按照id分组,内部集合商品成员按照时间排序
        for (RestrictProductVO product : products) {
            long productId = product.getProductId();

            if (productId != lastProductId) {
                if (buyProducts != null) {
                    Collections.sort(buyProducts,
                            new Comparator<RestrictProductVO>(){
                                public int compare(RestrictProductVO o1, RestrictProductVO o2) {
                                    return (int) (o2.getCreateTime() - o1.getCreateTime());
                                }
                            });
                }
                buyProducts = new ArrayList<RestrictProductVO>();
                productGroup.put(productId, buyProducts);
                lastProductId = productId;
                //获取存在记录的商品id,优化下面的循环
                existProductIds.add(productId);
            }

            buyProducts.add(product);
        }
        //对最后一个商品排序
        if (productGroup.size() > 0) {
            List<RestrictProductVO> bps = productGroup.get(lastProductId);
            if (bps != null) {
                Collections.sort(bps,
                        new Comparator<RestrictProductVO>(){
                            public int compare(RestrictProductVO o1, RestrictProductVO o2) {
                                return (int) (o2.getCreateTime() - o1.getCreateTime());
                            }
                        });
            }
        }
    }

    public String restrictDetail(RestrictProductVO rProductVO) {
        String description;
        if (rProductVO.getHistorySum() + rProductVO.getBuyCount() > rProductVO.getRestrictHistoryBuy()) {
            description = "本商品" + rProductVO.getRestrictCycle() + "天内限购" + rProductVO.getRestrictHistoryBuy() + "件，小朋友不要太调皮啦，给其他小伙伴一点机会吧！";
        }
        else {
            description = "本商品今日限购" + rProductVO.getRestrictDayBuy() + "件，明天再买吧～赶紧先收藏一下";
        }

        return description;
    }

    public Map<String, Object> getProduct18(Product product, UserDetail userDetail, int guideFlag, RestrictionActivityProduct restrictionActivityProductInfo) {
        //添加浏览记录
        visitService.addVisitHistory(userDetail.getId(), guideFlag, new Long[]{product.getId()}, 0);

        Map<String, Object> data = new HashMap<String, Object>();

        // get product main information

        /* 获取商品的属性名和属性值 */
        List<ProductProp> baseProps = productPropertyService.getOrderedProductProperties(product.getId());
        List<ProductPropVO> basePropVOs = this.loadProductPropVOs(baseProps);
        data.put("baseProps", basePropVOs);

        List<ProductSKU> skus = null;
        if (restrictionActivityProductInfo != null) {
            skus = productSKUService.getProductSKUsByRestrictionActivityProductId(restrictionActivityProductInfo.getId());
        }
        else {
            skus = productSKUService.getProductSKUsOfProduct(product.getId());
        }
        Map<String, Map<String, Object>> skuMap = new HashMap<>();
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        int remainCounts = 0;

        //如果该商品参加了限购活动,取出限购活动sku对应的库存
        Map<Long, Integer> restrictionActivityProductSkuRemainCount = new HashMap<Long, Integer>();
        if (restrictionActivityProductInfo != null) {
            long restrictionActivityProductId = restrictionActivityProductInfo.getId();
            Wrapper<RestrictionActivityProductSku> wrapper = new EntityWrapper<RestrictionActivityProductSku>()
                    .eq("activity_product_id", restrictionActivityProductId);
            List<RestrictionActivityProductSku> restrictionActivityProductSkuList = restrictionActivityProductSkuMapper.selectList(wrapper);
            for (RestrictionActivityProductSku restrictionActivityProductSku : restrictionActivityProductSkuList) {
                restrictionActivityProductSkuRemainCount.put(restrictionActivityProductSku.getProductSkuId(), restrictionActivityProductSku.getRemainCount());
                //初始化活动剩余商品数量到缓存
                String groupKey = MemcachedKey.GROUP_KEY_restrictionActivityProductId;
                String key = "_restrictionActivityProductId_" + String.valueOf(restrictionActivityProductId) + "_skuId_"
                        + String.valueOf(restrictionActivityProductSku.getProductSkuId());
                Object obj = memcachedService.getCommon(groupKey, key);
                if (obj == null) {
//					将剩余活动商品数量放入缓存
                    int surplusActivityProductCount = restrictionActivityProductSku.getRemainCount();//剩余活动商品数量
                    int time = 7 * 24 * 60 * 60;//有效期1周，注意这里有效期不能大于30天
                    memcachedService.setCommon(groupKey, key, time, String.valueOf(surplusActivityProductCount).trim());//注意这里必须转为string否则不能加减
                    logger.info("从数据库中获取剩余活动商品数量surplusActivityProductCount：" + surplusActivityProductCount
                            + ",restrictionActivityProductSkuId：" + restrictionActivityProductSku.getId());
                }
                else {
                    logger.info("从缓存中中获取剩余活动商品数量obj：" + obj + ",restrictionActivityProductSkuId：" + restrictionActivityProductSku.getId());
                }
            }
            long activityEndTime = restrictionActivityProductInfo.getActivityEndTime();
            long nowTime = System.currentTimeMillis();
            if (activityEndTime < nowTime) {//活动已结束设置为已下架
                logger.error("当前活动已经结束restrictionActivityProductId:" + restrictionActivityProductId);
                product.setState(7);
            }
            else {
                int productStatus = restrictionActivityProductInfo.getProductStatus();
                //0:待上架;1:已上架;2:已下架;3:已删除
                switch (productStatus) {
                    case 0://0:待上架
                        product.setState(5);
                        break;
                    case 1://1:已上架
                        product.setState(6);
                        break;
                    case 2://2:已下架
                        product.setState(7);
                        break;
                    case 3://3:已删除
                        product.setState(8);
                        break;
                    default:
                        break;
                }
            }
        }
        for (ProductSKU sku : skus) {
            Map<String, Object> item = new HashMap<>();

            //如果该商品参加了限购活动,将对应的库存覆盖原库存
            if (restrictionActivityProductInfo != null) {
                int remainCountNew = restrictionActivityProductSkuRemainCount.get(sku.getId());
                if (remainCountNew < 1) {
                    continue;
                }
                sku.setRemainCount(remainCountNew);
            }

            item.put("sku", sku);

            String propertyIds = sku.getPropertyIds();
            String[] parts = propertyIds.split(";");
            long colorId = Long.parseLong(parts[0].split(":")[1]);
            long sizeId = Long.parseLong(parts[1].split(":")[1]);
            item.put("color", propertyService.getPropertyValueById(colorId));
            item.put("size", propertyService.getPropertyValueById(sizeId));

            skuMap.put(sku.getPropertyIds(), item);
            productPropVOs.addAll(sku.getProductProps());

            remainCounts += sku.getRemainCount();
        }
        data.put("skuMap", skuMap);
        data.put("remainCounts", remainCounts);


        Set<Long> productIdSet = new HashSet<Long>();

        productIdSet.add(product.getId());

        List<ShopProduct> shopProductList = shopProductService.getShopProductListByProIds(userDetail.getId(), productIdSet);
        if (shopProductList != null && shopProductList.size() > 0) {
            data.put("uploadNum", shopProductList.size());
        }
        else {
            data.put("uploadNum", 0);
        }


        Map<String, Map<String, Object>> propertyValueMap = new HashMap<>();
        data.put("propertyValueMap", propertyValueMap);
        for (ProductSKU sku : skus) {
            Map<String, Object> map = new HashMap<>();
            propertyValueMap.put(sku.getPropertyIds(), map);


        }

        productPropAssembler.assemble(productPropVOs);

        List<ProductPropNameValuesPair> skuProps = new ArrayList<ProductPropNameValuesPair>();
        Map<Long, ProductPropNameValuesPair> skuPropMap = new HashMap<Long, ProductPropNameValuesPair>();
        for (ProductPropVO propVO : productPropVOs) {
            ProductPropName propName = propVO.getPropName();
            ProductPropNameValuesPair skuProp = skuPropMap.get(propName.getId());
            if (skuProp == null) {
                skuProp = new ProductPropNameValuesPair(propName);
                skuPropMap.put(propName.getId(), skuProp);
                skuProps.add(skuProp);
            }
            skuProp.add(propVO.getPropValue());
        }
        //按照orderIndex排序
        for (ProductPropNameValuesPair productPropNameValuesPair : skuProps) {

            Collections.sort(productPropNameValuesPair.getPropValues(), new Comparator<ProductPropValue>(){
                @Override
                public int compare(ProductPropValue o1, ProductPropValue o2) {
                    if (o1.getOrderIndex() > o2.getOrderIndex()) {
                        return 1;
                    }
                    if (o1.getOrderIndex() == o2.getOrderIndex()) {
                        return 0;
                    }
                    return - 1;
                }
            });
        }

        data.put("skuProps", skuProps);
        //商品状态(删除,上架,未删除非上架)
        data.put("platformProductState", productNewService.getPlatformProductState(product.getId()));
        data.put("product", product);

        StoreBusiness storeBusiness = storeBusinessService.getById(userDetail.getId());
        if (storeBusiness != null) {
            //提成金额 = 消费金额 * 提成百分比
            data.put("commission", String.format("%.2f", product.getCash() * storeBusiness.getCommissionPercentage() / 100));
        }

        String remark = product.getRemark();
        String[] summaryImages = product.getSummaryImageArray();
        String[] size = product.getSizeTableImageArray();

        String description = product.getDescription();

        String togetherBuyStr = product.getTogether();
        productIdSet = new HashSet<Long>();
        if (togetherBuyStr != null && togetherBuyStr.length() > 0) {
            String[] togetherBuyArr = togetherBuyStr.split(",");
            for (String togetherBuy : togetherBuyArr) {
                try {
                    productIdSet.add(Long.parseLong(togetherBuy));

                } catch (Exception e) {
                    System.out.println("getProduct18:togetherBuy:" + togetherBuy);
                    continue;
                }
            }
        }
        // 过滤未上架商品, 库存0的商品(补丁)
        if (! productIdSet.isEmpty()) {
            List<Long> productIdList = productService.checkProduct(productIdSet);
            if (productIdList != null && ! productIdList.isEmpty()) {
                List<ProductVOShop> tgProductList = productService.getProductByIds(productIdList, false, userDetail);
                data.put("togetherProductList", tgProductList);
            }
        }

        List<String> imgList = new ArrayList<String>();

        String imgHead = Client.OSS_IMG_SERVICE;
        String regEx = "(" + imgHead + "[^>^http:]*" + ".[a-z_A-Z_]*)";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat;

        if (size != null && size.length > 0) {

            for (int i = 0; i < size.length; i++) {
                imgList.add(size[i]);
            }
        }

        if (remark != null) {
            mat = pat.matcher(remark);
            while (mat.find()) {
                imgList.add(mat.group());
            }
            remark = remark.replaceAll("(src=\")(" + imgHead + "[^>^http:]*" + ".[a-z_A-Z_]*)", "onclick=\"javascript:window.open('yjj://skuImg/$2')\"; $1$2");

        }

        if (description != null) {
            mat = pat.matcher(description);
            while (mat.find()) {
                imgList.add(mat.group());
            }

            description = description.replaceAll("(src=\")(" + imgHead + "[^>^http:]*" + ".[a-z_A-Z_]*)", "onclick=\"javascript:window.open('yjj://skuImg/$2')\"; $1$2");
        }

        if (summaryImages != null && summaryImages.length > 0) {
            for (int i = 0; i < summaryImages.length; i++) {
                imgList.add(summaryImages[i]);
            }
        }
        data.put("imgList", imgList);
        product.setRemark(remark);
        product.setDescription(description);

        long userId = userDetail.getId();
        data.put("userId", userId);
        if (userId > 0) {
            data.put("userConfig", buildUserMap(userDetail.getId(), product.getId()));
            data.put("restrictInfo", this.restrictInfoOfProduct(userDetail, CollectionUtil.createSet(product.getId())).get(product.getId()));
            data.put("restrictionCombinationList", this.restrictionCombinationInfoOfProduct(userDetail, CollectionUtil.createSet(product.getId())));
        }

        Brand brand = shopBrandService.getBrand(product.getBrandId());
        data.put("brand", brand);


        //获取商品对应的搭配商品
        List<Map<String, String>> matchProductList = new ArrayList<Map<String, String>>();
        String togetherIds = product.getTogether();
        if (! StringUtils.isEmpty(togetherIds)) {
            String[] matchIds = togetherIds.split(",");
            for (String matchId : matchIds) {
                Map<String, String> matchProductMap = new HashMap<String, String>();
                long id = Long.parseLong(matchId);
                Product matchProduct = productService.getProduct(id);
                matchProductMap.put("matchProductId", matchProduct.getId() + "");
                matchProductMap.put("firstImage", matchProduct.getFirstDetailImage());
                matchProductMap.put("wholeSaleCash", matchProduct.getWholeSaleCash() + "");
                //最小阶梯价格
                matchProductMap.put("minLadderPrice", matchProduct.getMinLadderPrice() + "");
                //最大阶梯价格
                matchProductMap.put("maxLadderPrice", matchProduct.getMaxLadderPrice() + "");
                //阶梯价格JSON
                matchProductMap.put("ladderPriceJson", matchProduct.getLadderPriceJson() + "");
                //供应商ID
                matchProductMap.put("supplierId", matchProduct.getSupplierId() + "");
                //角标
                matchProductMap.put("badgeImage", matchProduct.getBadgeImage() + "");
                matchProductList.add(matchProductMap);
            }
        }
        data.put("matchProductList", matchProductList);

        //放入参加限购活动的商品的数据
        if (restrictionActivityProductInfo != null) {
            int remainCount = restrictionActivityProductInfo.getRemainCount();
            long activityBeginTime = restrictionActivityProductInfo.getActivityBeginTime();
            long activityEndTime = restrictionActivityProductInfo.getActivityEndTime();
            long beginTimeMillis = activityBeginTime - System.currentTimeMillis();//限购活动开始剩余毫秒
            long endTimeMillis = activityEndTime - System.currentTimeMillis();//限购活动结束剩余毫秒
//    		if(remainCount<1){//已售罄
            data.put("beginTimeMillis", beginTimeMillis > 0 ? beginTimeMillis : 0);
            data.put("endTimeMillis", endTimeMillis > 0 ? endTimeMillis : 0);
//    		}else if(activityBeginTime<currentTimeMillis && activityEndTime>currentTimeMillis){//正进行中
//    			data.put("beginTimeMillis", activityEndTime-currentTimeMillis);//限购活动结束剩余毫秒
//    			data.put("endTimeMillis", activityEndTime-currentTimeMillis);
//    		}else if(activityBeginTime>currentTimeMillis){//即将开始
//    			data.put("beginTimeMillis", activityBeginTime-currentTimeMillis);
//    			data.put("endTimeMillis", activityEndTime-currentTimeMillis);
//    		}
            data.put("activityProductPrice", restrictionActivityProductInfo.getActivityProductPrice());//活动价格
            data.put("productPrice", restrictionActivityProductInfo.getProductPrice());//商品原价格
            data.put("remainCount", remainCount);//限购活动商品剩余库存
            data.put("restrictionCount", restrictionActivityProductInfo.getRestrictionCount());//限购数量
            //起订量
            data.put("miniPurchaseCount", restrictionActivityProductInfo.getMiniPurchaseCount());
            data.put("activityProductName", restrictionActivityProductInfo.getProductName());//限购活动商品名称
        }

        return data;
    }


    private Map<String, Object> buildUserMap(long storeId, long productId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("favorite", storeFavoriteService.getFavorite(storeId, productId, 0) != null);
        map.put("cart_count", shoppingCartService.getTotalCountByStoreId(storeId));
        return map;
    }

    /**
     * 获取相关商品的限购信息
     *
     * @param
     * @param productIds
     * @return
     */
    public Map<Long, RestrictProductVO> restrictInfoOfProduct(UserDetail userDetail, Set<Long> productIds) {
        Map<Long, RestrictProductVO> map = new HashMap<Long, RestrictProductVO>();

        //获取用户购买的sku信息
        List<ProductVOShop> pros = productService.getProductByIds(productIds, false, userDetail);
        for (ProductVOShop product : pros) {
            if (product.getRestrictDayBuy() != - 1 && product.getRestrictHistoryBuy() != - 1) {
                RestrictProductVO rVo = new RestrictProductVO();

                rVo.setProductId(product.getId());
                rVo.setRestrictDayBuy(product.getRestrictDayBuy());
                rVo.setRestrictHistoryBuy(product.getRestrictHistoryBuy());
                rVo.setRestrictCycle(product.getRestrictCycle());
                rVo.setRestrictHistoryBuyTime(product.getRestrictHistoryBuyTime());
                rVo.setRestrictDayBuyTime(product.getRestrictDayBuyTime());

                map.put(product.getId(), rVo);
            }
        }

        //存在购买记录的商品id
        Set<Long> existProductIds = new HashSet<Long>();
        //商品按id分组的组集合
        Map<Long, List<RestrictProductVO>> productGroup = new HashMap<Long, List<RestrictProductVO>>();

        //获取用户购买这些商品的记录
        List<RestrictProductVO> products = productService.getBuyerLogByStoreOrder(userDetail.getId(), productIds);
        if (products == null) {
            return map;
        }
        //对获取的商品集记录进行按id分组,组内按CreateTime降序排序
        sortProducts(products, productGroup, existProductIds);

        for (Long productId : existProductIds) {
            RestrictProductVO rVo = map.get(productId);

            if (rVo == null)
                continue;

            //各自productId的List
            List<RestrictProductVO> productsItems = productGroup.get(productId);

            if (productsItems != null && productsItems.size() > 0) {
                int restrictDayBuy = productsItems.get(0).getRestrictDayBuy();
                int restrictHistoryBuy = productsItems.get(0).getRestrictHistoryBuy();
                //商品既不今日限购也不历史限购,不记录
                if (restrictDayBuy == - 1 && restrictHistoryBuy == - 1)
                    continue;

                Map<String, Integer> buyCountLog = new HashMap<String, Integer>();
                buyCountLog = getBuyCountLog(productsItems);

                BeanUtils.copyProperties(productsItems.get(0), rVo);
                rVo.setDaySum(buyCountLog.get("daySum"));
                rVo.setHistorySum(buyCountLog.get("historySum"));

                map.put(rVo.getProductId(), rVo);
            }
        }

        return map;
    }

    /**
     * 获取用户历史和单日组合限购数量
     *
     * @param userDetail
     * @return
     */
    public List<Map<String, String>> restrictionCombinationInfoOfProduct(UserDetail userDetail, Set<Long> productIdSet) {
        List<Map<String, String>> restrictionCombinationList = new ArrayList<Map<String, String>>();
        Map<Long, ProductVOShop> productMap = productService.getProductMap(productIdSet);
        //获取商品所参加的限购活动
        List<Long> restrictIds = new ArrayList<Long>();
        for (Long productId : productMap.keySet()) {
            Product product = productMap.get(productId);
            Long restrictId = product.getRestrictId();
            if (! restrictIds.contains(restrictId)) {
                restrictIds.add(restrictId);
            }
        }
        if (restrictIds.size() <= 0) {
            return restrictionCombinationList;
        }
        Map<Long, RestrictionCombination> restrictMap = productService.getRestrictByIdSet(restrictIds);

//    	//获取用户购买过的并且有参加相应限购活动的商品Id
//    	List<Long> productIdList = productService.getAllProductIdsByUser(userDetail.getId(),restrictIds);

        if (restrictMap != null && restrictMap.size() > 0) {
            for (Long restrictionCombinationId : restrictMap.keySet()) {
                Map<String, String> restrictionCombinationMap = new HashMap<String, String>();
                RestrictionCombination restrictionCombination = restrictMap.get(restrictionCombinationId);
                int daySum = 0;
                int historySum = 0;
                //获取用户单日组合限购数量
                if (restrictionCombination.getDaySetting() == 1 && getStartTime() > restrictionCombination.getDayStartTime() && restrictionCombination.getDayBuy() > 0) {
                    long restrictDayTime = restrictionCombination.getDayStartTime() > (getStartTime() - DAY_MILLION_SECOND) ?
                            restrictionCombination.getDayStartTime() : (getStartTime() - DAY_MILLION_SECOND);
                    daySum = productService.getUserRestrictBuyByStoreOrder(userDetail.getId(), restrictionCombinationId, restrictDayTime, System.currentTimeMillis());
                }
                //获取用户历史组合限购数量
                if (restrictionCombination.getHistorySetting() == 1 && getStartTime() > restrictionCombination.getHistoryStartTime() && restrictionCombination.getHistoryBuy() > 0) {
                    long restrictHistoryTime = restrictionCombination.getHistoryStartTime() > (getStartTime() - restrictionCombination.getHistoryCycle() * DAY_MILLION_SECOND) ?
                            restrictionCombination.getHistoryStartTime() : (getStartTime() - (restrictionCombination.getHistoryCycle() * DAY_MILLION_SECOND));
                    historySum = productService.getUserRestrictBuyByStoreOrder(userDetail.getId(), restrictionCombinationId, restrictHistoryTime, System.currentTimeMillis());
                }
                restrictionCombinationMap.put("historyCycle", restrictionCombination.getHistoryCycle() + "");
                restrictionCombinationMap.put("name", restrictionCombination.getName());
                restrictionCombinationMap.put("historyBuy", restrictionCombination.getHistoryBuy() + "");
                restrictionCombinationMap.put("dayBuy", restrictionCombination.getDayBuy() + "");
                restrictionCombinationMap.put("historySetting", restrictionCombination.getHistorySetting() + "");
                restrictionCombinationMap.put("historyCount", historySum + "");
                restrictionCombinationMap.put("daySetting", restrictionCombination.getDaySetting() + "");
                restrictionCombinationMap.put("dayCount", daySum + "");
                restrictionCombinationMap.put("restrictionCombinationId", restrictionCombinationId + "");
                restrictionCombinationList.add(restrictionCombinationMap);
            }
        }

        return restrictionCombinationList;
    }

    public List<ProductPropVO> loadProductPropVOs(List<ProductProp> productProps) {
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        for (ProductProp productProp : productProps) {
            ProductPropVO vo = new ProductPropVO();
            BeanUtil.copyProperties(vo, productProp);
            productPropVOs.add(vo);
        }
        productPropAssembler.assemble(productPropVOs);
        return productPropVOs;
    }



    /**
     * 将品牌列表刷到redis缓存中
     *
     * @param token
     * @param type 0：热卖 1：推荐
     * @param pageQuery 查询分页参数
     * @param seconds 数据存活时间 0永久存活
     * @return void
     * @author Charlie(唐静)
     * @date 2018/6/22 18:06
     */
    public Map<String, Object> brushBrandListCache(String token, Integer type, PageQuery pageQuery, int seconds) {
        // token 验证

        // 查询列表
        Map<String, Object> brandInfoMap = getBrandList("", new DefaultStoreUserDetail(), pageQuery, type, - 1, false);

        // 放入缓存
        if (brandInfoMap != null && ! brandInfoMap.isEmpty()) {
            String cacheKey = splicingBrandListCacheKey(type, pageQuery.getPage(), pageQuery.getPageSize());
            JedisUtils.setObject(cacheKey, brandInfoMap, seconds);
        }
        return brandInfoMap;
    }


    /**
     * 查询条件
     *
     * @author Aison
     * @date 2018/6/25 17:28
     */
    public List<Map<String,Object>> brandList(String brandName, UserDetail<StoreBusiness> userDetail, PageQuery pageQuery, int type, int brandType, boolean openCache) {


        Map<String, Object> map = new HashMap<>();
        Set<Long> brands = new HashSet<>();
        String phoneNumber = "";
        if (userDetail.getId() != 0) {
            phoneNumber = userDetail.getUserDetail().getPhoneNumber();
        }
        //获取品牌列表
        List<BrandVO> brandList = brandService.getBrandListShow(brandName, pageQuery, type, userDetail.getId(), brandType);
        Long storeId = userDetail.getId();
        Set<Long> ids = new HashSet<>();
        brandList.forEach(action->{
            ids.add(action.getBrandId());
        });
        Map<Long,UserNew> userNewMap = userNewService.getSupplierUsers(ids);
        List<Long> userIds = new ArrayList<>();
        userNewMap.forEach((key,val)->{
            userIds.add(val.getId());
        });
        Map<Long,StoreCouponTemplateNew> storeCouponTemplateNewMap = storeCouponTemplateNewMapper.selectCouponGroup(userIds);

        for (BrandVO brandVO : brandList) {
            long brandId = brandVO.getBrandId();
            ids.add(brandId);
            String productPermission = brandVO.getProductPermission();
            // 判断用户权限
            UserNew userNew = userNewMap.get(brandId);
            //批发限制状态（即混批）（0不混批、1混批）
            int wholesaleLimitState = userNew.buildWholesaleLimitState();
            //批发限制提示文本1, 例子：10件、￥1000元 起订
            String wholesaleLimitTip1 = userNew.buildWholesaleLimitTip1();
            //批发限制提示文本2, 例子：全店满50件且满50.00元订单总价可混批采购、全店满50件可混批采购、全店满50.00元订单总价可混批采购
            String wholesaleLimitTip2 = userNew.buildWholesaleLimitTip2();
            brandVO.setWholesaleLimitState(wholesaleLimitState);
            brandVO.setWholesaleLimitTip1(wholesaleLimitTip1);
            brandVO.setWholesaleLimitTip2(wholesaleLimitTip2);

            Long supplierId = userNew.getId();
            //客户权限设置
            if (userNew.getProductPermission().equals("0")) {
                //首先判断品牌是否公开
                //若公开
                //是否有客户权限 0:没有权限  1:有权限
                brandVO.setHasPermission(1);
                brandVO.setProductPermission("");
            }
            else {
                //若不公开或部分公开
                SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByStoreIdOrPhoneNumber(phoneNumber, storeId, supplierId);
                if (supplierCustomer == null) {
                    //该客户不能看
                    //是否有客户权限 0:没有权限  1:有权限
                    brandVO.setHasPermission(0);
                    brandVO.setProductPermission("");
                }
                else {
                    Long groupId = supplierCustomer.getGroupId();
                    boolean result = productPermission.contains(String.valueOf(groupId));
                    if (result) {
                        //是否有客户权限 0:没有权限  1:有权限
                        brandVO.setHasPermission(1);
                        brandVO.setProductPermission("专供");
                    }
                    else {
                        //是否有客户权限 0:没有权限  1:有权限
                        brandVO.setHasPermission(0);
                        brandVO.setProductPermission("");
                    }
                }
            }
            //是否有优惠券  hasCoupon 0:没有  1:有
            int hasCoupon = 0;
            //List<Map<String, Object>> brandCouponList = storeCouponTemplateNewMapper.getSupplierCouponTemplate(brandId);
            StoreCouponTemplateNew storeCouponTemplateNew =  storeCouponTemplateNewMap.get(userNew.getId());
            if (storeCouponTemplateNew!= null) {
                hasCoupon = 1;
            }
            brandVO.setHasCoupon(hasCoupon);

        }
        if (brandName != null && brandName.trim().length() > 0) {
            for (BrandVO brandVO : brandList) {
                long brandId = brandVO.getBrandId();
                brands.add(brandId);
            }
        }

        Map<String,Object> brandMap = new HashMap<>();
        brandMap.put("brandIds",ids);
        List<ProductNew> productNews = productNewMapper.selectByBrandIds(brandMap);

        Map<BigInteger,Map<String,Long>> countMap = productNewMapper.selectProductCount(ids);

        Map<Long,List<ProductNew>> mapProduct = new HashMap<>(20);

        // 转换成map 方便使用 id get出来
        productNews.forEach(action->{
            Long brandId = action.getBrandId();
            List<ProductNew> products  = mapProduct.computeIfAbsent(brandId,k -> new ArrayList<>());
            products.add(action);
        });

        List<Map<String,Object>> brandVOS = new ArrayList<>();
        brandList.forEach(action->{
            Map<String,Object> mapItem  = BizUtil.bean2MapAllField(action,true,null);
            Long brandId = action.getBrandId();
            List<ProductNew> products = mapProduct.get(brandId);
            Map<String,Long> countMapItem = countMap.get(new BigInteger(brandId.toString()));
            mapItem.put("productNum",countMapItem!=null ? countMapItem.get("count") : 0);
            mapItem.put("products",products == null ? "[]" : BizUtil.listBean2listMap(products,true,(jobMap,key)->{
                if("detailImages".equals(key)) {
                    String detailImages = (String) jobMap.get(key);
                    if(detailImages !=null) {
                        JSONArray array = JSON.parseArray(detailImages);
                        jobMap.put("image",array==null ? new ArrayList<>() : array.get(0));
                    }
                }
            }));
            brandVOS.add(mapItem);
        });
        return brandVOS;
    }

    /**
     * 获取品牌列表
     *
     * @param brandName
     * @param userDetail
     * @param pageQuery
     * @param type
     * @param brandType
     * @param openCache  是否开启redis查询缓存, true优先在redis中获取
     * @return Map
     * update by Charlie(唐静)
     * @date 2018/6/22 17:58
     */
    public Map<String, Object> getBrandList(String brandName, UserDetail<StoreBusiness> userDetail, PageQuery pageQuery, int type, int brandType, boolean openCache) {
        // 开启查询缓存,并且是缓存中存有的品牌查询类型, 从缓存中查询 0：热卖 1：推荐
        if (openCache && BizUtil.isEmpty(brandName)) {
            if (type == BRAND_RECOMMEND_TYPE || type == BRAND_HOT_SALE_TYPE) {
                Map<String, Object> result = getBrandInfoFromCache(type, pageQuery);
                if (result != null && ! result.isEmpty()) {
                    return result;
                }
            }
        }


        Map<String, Object> map = new HashMap<>();
        Set<Long> brands = new HashSet<>();
        String phoneNumber = "";
        if (userDetail.getId() != 0) {
            phoneNumber = userDetail.getUserDetail().getPhoneNumber();
        }
        List<BrandVO> brandList=new ArrayList<>();
        //获取品牌列表
        if(type==2){
            brandList=brandService.getHsitory(userDetail.getId(),pageQuery);
        }else{
           brandList = brandService.getBrandListShow(brandName, pageQuery, type, userDetail.getId(), brandType);
        }

        Long storeId = userDetail.getId();
        for (BrandVO brandVO : brandList) {
            long brandId = brandVO.getBrandId();
            String productPermission = brandVO.getProductPermission();
            // 判断用户权限
            UserNew userNew = userNewService.getSupplierByBrandId(brandId);
            int wholesaleLimitState = userNew.buildWholesaleLimitState();//批发限制状态（即混批）（0不混批、1混批）
            String wholesaleLimitTip1 = userNew.buildWholesaleLimitTip1();//批发限制提示文本1, 例子：10件、￥1000元 起订
            String wholesaleLimitTip2 = userNew.buildWholesaleLimitTip2();//批发限制提示文本2, 例子：全店满50件且满50.00元订单总价可混批采购、全店满50件可混批采购、全店满50.00元订单总价可混批采购
            brandVO.setWholesaleLimitState(wholesaleLimitState);
            brandVO.setWholesaleLimitTip1(wholesaleLimitTip1);
            brandVO.setWholesaleLimitTip2(wholesaleLimitTip2);

            Long supplierId = userNew.getId();
            //客户权限设置
            if (userNew.getProductPermission().equals("0")) {
                //首先判断品牌是否公开
                //若公开
                brandVO.setHasPermission(1);//是否有客户权限 0:没有权限  1:有权限
                brandVO.setProductPermission("");
            }
            else {
                //若不公开或部分公开
                SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByStoreIdOrPhoneNumber(phoneNumber, storeId, supplierId);
                if (supplierCustomer == null) {
                    //该客户不能看
                    brandVO.setHasPermission(0);//是否有客户权限 0:没有权限  1:有权限
                    brandVO.setProductPermission("");
                }
                else {
                    Long groupId = supplierCustomer.getGroupId();
                    boolean result = productPermission.contains(String.valueOf(groupId));
                    if (result) {
                        brandVO.setHasPermission(1);//是否有客户权限 0:没有权限  1:有权限
                        brandVO.setProductPermission("专供");
                    }
                    else {
                        brandVO.setHasPermission(0);//是否有客户权限 0:没有权限  1:有权限
                        brandVO.setProductPermission("");
                    }
                }
            }

            brandVO.setSupplierId(supplierId);
//            //是否有优惠券  hasCoupon 0:没有  1:有
//            int hasCoupon = 0;
//            List<Map<String, Object>> brandCouponList = storeCouponTemplateNewMapper.getSupplierCouponTemplate(brandId);
//            if (brandCouponList.size() > 0) {
//                hasCoupon = 1;
//            }
//            brandVO.setHasCoupon(hasCoupon);

        }
        if (brandName != null && brandName.trim().length() > 0) {
            for (BrandVO brandVO : brandList) {
                long brandId = brandVO.getBrandId();
                brands.add(brandId);
            }
        }
        //获取对应品牌的商品
        Map<Long, List<ProductVOShop>> productMaps = productService.getProductGroupsByBrandIds(brands, userDetail, type);
        Iterator<BrandVO> stuIter = brandList.iterator();
        while (stuIter.hasNext()) {
            BrandVO brandVO = stuIter.next();
            Page<ProductNew> page = new Page<>();
            page.setSize(3);
            page.setCurrent(0);

            List<ProductNew> productNews = productNewService.getBrandProductList(userDetail.getId(), brandVO.getBrandId(), 1, "", page);
            List<Map<String, Object>> productMap = new ArrayList<>(3);
            for (ProductNew productNew : productNews) {
                productMap.add(toSimpleMap15(false, productNew));
            }
            brandVO.setProducts(productMap);
            if (brandVO.getProducts() == null || brandVO.getProducts().size() == 0) {
                stuIter.remove();
                continue;
            }
            if (productMaps.get(brandVO.getBrandId()) != null) {
                Long count = productNewService.getBrandProducCount(brandVO.getBrandId(), "");
                brandVO.setProductNum(count.intValue());
            }
        }
        map.put("brandList", brandList);


        //放到缓存中
        if (openCache && BizUtil.isEmpty(brandName)) {
            if (type == BRAND_RECOMMEND_TYPE || type == BRAND_HOT_SALE_TYPE) {
                // 放入缓存
                String cacheKey = splicingBrandListCacheKey(type, pageQuery.getPage(), pageQuery.getPageSize());
                JedisUtils.setObject(cacheKey, map, 600);
            }
        }
        return map;
    }



    /**
     * 从缓存中获取品牌列表
     *
     * @param type
     * @param pageQuery
     * @return java.util.Map
     * @author Charlie(唐静)
     * @date 2018/6/23 0:13
     */
    private Map<String, Object> getBrandInfoFromCache(int type, PageQuery pageQuery) {
        // 拼接查询key
        String cacheKey = splicingBrandListCacheKey(type, pageQuery.getPage(), pageQuery.getPageSize());
        try {
            // 查询数据
            Map<String, Object> object = (Map<String, Object>) JedisUtils.getObject(cacheKey);
            return object;
        } catch (Exception e) {
            logger.error("查询品牌商品缓存失败 {cacheKey:" + cacheKey + "}");
        }
        return null;
    }


    /**
     * 拼接redis查询品牌商品的key
     *
     * @param type     品牌商品类型 0：热卖 1：推荐
     * @param page     查询页
     * @param pageSize 每页记录数
     * @return 查询key
     * @author Charlie(唐静)
     * @date 2018/6/22 18:33
     */
    private String splicingBrandListCacheKey(int type, int page, int pageSize) {
        String queryKey;
        if (type == BRAND_RECOMMEND_TYPE) {
            queryKey = BRAND_RECOMMEND;
        }
        else if (type == BRAND_HOT_SALE_TYPE) {
            queryKey = BRAND_HOT_SALE;
        }
        else {
            throw BizException.defulat().msg("拼接查询商品列表key,不支持的商品查询类型 type:" + type);
        }
        return queryKey + page + "_" + pageSize;
    }


    public Map<String, Object> toSimpleMap15(boolean promotionImage, ProductNew productNew) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", productNew.getId());
        map.put("name", productNew.getName());
        String image = promotionImage ? org.apache.commons.lang3.StringUtils.defaultString(productNew.getPromotionImage()) : "";
        if (org.apache.commons.lang3.StringUtils.isBlank(image)) {
            String[] detailImageArray = productNew.getDetailImageArray();
            if (detailImageArray.length > 0) {
                image = detailImageArray[0];
            }
        }
        map.put("image", image);
        map.put("price", productNew.getPrice());
        map.put("marketPrice", productNew.getMarketPrice());
        map.put("bottomPrice", productNew.getBottomPrice());
        map.put("marketPriceMin", productNew.getMarketPriceMin());
        map.put("marketPriceMax", productNew.getMarketPriceMax());
        map.put("saleTotalCount", productNew.getSaleTotalCount());
        // 根据sku 来计算的..目前没有
        map.put("onSaling", false);
        map.put("weight", productNew.getWeight());
        map.put("promotionSaleCount", productNew.getPromotionSaleCount());
        map.put("promotionSaleCountStr", "销量：" + productNew.getPromotionSaleCount() + "件");
        map.put("jiuCoin", 0);
        map.put("subscriptLogo", null);
        map.put("currenCash", productNew.getWholeSaleCash());
        map.put("wholeSaleCash", productNew.getWholeSaleCash());
        map.put("badgeImage", productNew.getBadgeImage());
        //最小阶梯价格
        map.put("minLadderPrice", productNew.getMinLadderPrice());
        //最大阶梯价格
        map.put("maxLadderPrice", productNew.getMaxLadderPrice());
        //阶梯价格JSON
        map.put("ladderPriceJson", productNew.getLadderPriceJson());
        //供应商ID
        map.put("supplierId", productNew.getSupplierId());
        map.put("cash", productNew.getCash());
        map.put("loWarehouseId", productNew.getLOWarehouseId());
        map.put("vedioMain", productNew.getVedioMain());


        productMonitorService.fillProductMonitorProductMap(map, "id");
        return map;
    }


    private boolean isPromotion(ProductNew productNew) {
        return productNew.getPromotionSetting() == 1 && productNew.getPromotionStartTime() < System.currentTimeMillis()
                && productNew.getPromotionEndTime() > System.currentTimeMillis();
    }
}