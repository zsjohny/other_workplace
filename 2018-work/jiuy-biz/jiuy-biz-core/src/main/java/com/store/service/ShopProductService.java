package com.store.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.SystemPlatform;
import com.jiuyuan.dao.mapper.shop.PropertyValueNewMapper;
import com.jiuyuan.dao.mapper.supplier.OrderItemNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductTempInfoMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.IYjjMemberService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpsClientUtil;
import com.jiuyuan.util.VideoSignatureUtil;
import com.store.dao.mapper.*;
import com.store.entity.*;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author QiuYuefan
 */

@Service
public class ShopProductService {
    private static final Log logger = LogFactory.get("ShopProductService");

    /**
     * 商品:草稿
     */
    private static final int SHOP_SALE_DRAFT = 0;
    /**
     * 商品:上架
     */
    private static final int SHOP_SALE_ON = 1;
    /**
     * 商品:下架
     */
    private static final int SHOP_SALE_OUT = 2;
    /**
     * 商品:正常
     */
    private static final int SHOP_STATUS_NORMAL = 0;
    /**
     * 商品:删除
     */
    private static final int SHOP_STATUS_DELETE = -1;
    /**
     * sku状态:停用
     */
    private static final int SKU_STATUS_BLOCK_UP = -2;
    /**
     * sku状态:下架
     */
    private static final int SKU_STATUS_SALE_OUT = -1;
    /**
     * sku状态:正常
     */
    private static final int SKU_STATUS_NORMAL = 0;

    private static final int OWN_TYPE_STORE = 2;


    @Autowired
    private ShopGoodsCarService shopGoodsCarService;
    @Autowired
    private ShopTagService shopTagService;
    @Autowired
    private MemcachedService memcachedService;
    @Autowired
    private ShopProductMapper shopProductMapper;
    @Autowired
    private ShopProductLogMapper shopProductLogMapper;
    @Autowired
    private PropertyValueNewMapper propertyValueNewMapper;
    @Autowired
    private ShopPropertyValueMapper shopPropertyValueMapper;
    @Autowired
    private WxaMemberFavoriteMapper wxaMemberFavoriteMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductNewMapper productNewMapper;
    @Autowired
    private ProductSKUMapper productSKUMapper;
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private IMyStoreActivityService myStoreActivityService;
    @Autowired
    private IYjjMemberService memberService;
    @Autowired
    private ShopTagProductService shopTagProductService;
    @Autowired
    private OrderItemNewMapper orderItemNewMapper;
    @Autowired
    private ShopProductTempInfoMapper shopProductTempInfoMapper;

    /**
     * 获取商品库存数
     */
    private int getStockByProductIdCache(long productId) {
        String groupKey = MemcachedKey.GROUP_KEY_shop_product_stock;
        String key = String.valueOf(productId);
        int time = DateConstants.SECONDS_TEN_MINUTES;
        Object obj = memcachedService.get(groupKey, key);
//		logger.info("开始获取商家所有商品列表");
        int stock = 0;
        if (obj != null) {
            stock = (int) obj;
//			logger.info("----从缓存中获取商品库存stock："+stock+",缓存时间time："+time); 
        } else {
            List<ProductSKU> productSKUList = productSKUMapper.getAllProductSKUsOfProduct(productId);

            for (ProductSKU productSKU : productSKUList) {
                if (productSKU == null) {
                    continue;
                }
                if (stock == 0) {
                    stock = productSKU.getRemainCount();
                } else if (productSKU.getRemainCount() < stock) {
                    stock = productSKU.getRemainCount();
                }
            }
            memcachedService.set(groupKey, key, time, stock);
//			logger.info("----从数据库中获取商品库存stock完成并存入缓存完成,缓存时间为time"+time+",stock："+stock); 
        }


        return stock;
    }

    private void fillPlatformShopProductInfo(List<ShopProduct> shopProductList,
                                             List<ProductNew> platformProductNewList, Map<Long, Integer> productIdIndexMap) {

        for (ProductNew product : platformProductNewList) {
            long productId = product.getId();
            int index = productIdIndexMap.get(productId);
            ShopProduct shopProduct = shopProductList.get(index);
            String shopProductName = shopProduct.getName();
            if (StringUtils.isEmpty(shopProductName)) {
                shopProduct.setName(product.getName());
            }
            shopProduct.setXprice(product.getWholeSaleCash());
//			shopProduct.setMarketPrice((double) product.getMarketPrice());
            shopProduct.setClothesNumber(product.getClothesNumber());
            shopProduct.setVideoUrl(product.getVideoUrl());

//			int stock = getStockByProductIdCache(productId);
//			shopProduct.setStock((long) stock);
            shopProduct.setSummaryImages(product.getDetailImages());

            String sizeTableImage = product.getSizeTableImage();
            String detailImages = product.getSummaryImages();
            if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
                detailImages = product.getDetailImages();
            }
            String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
            shopProduct.setRemark(remark);
//			if(shopProduct.getStock()>0){
//				shopProduct.setStockTime(System.currentTimeMillis());
//			}


//			shopProduct.setPlatformProductState(productSKUService.getPlatformProductState(productId));
            String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
            int state = product.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            if (state == ProductNewStateEnum.up_sold.getIntValue()) {
                platformProductState = "0";
            }
            shopProduct.setPlatformProductState(platformProductState);
        }
    }


    /**
     * 封装平台商品数据进ShopProduct
     *
     * @param shopProduct
     */
    private void getPlatformShopProductInfo(ShopProduct shopProduct) {
        long productId = shopProduct.getProductId();
//		Product product = productNewMapper.getProduct(productId);
        ProductNew product = productNewMapper.selectById(productId);

        String shopProductName = shopProduct.getName();
        if (StringUtils.isEmpty(shopProductName)) {
            shopProduct.setName(product.getName());
        }
        shopProduct.setXprice(product.getWholeSaleCash());
//		shopProduct.setMarketPrice((double) product.getMarketPrice());
        shopProduct.setClothesNumber(product.getClothesNumber());
        shopProduct.setVideoUrl(product.getVideoUrl());

        int stock = getStockByProductIdCache(productId);
        shopProduct.setStock((long) stock);
        shopProduct.setSummaryImages(product.getDetailImages());

        String sizeTableImage = product.getSizeTableImage();
        String detailImages = product.getSummaryImages();
        if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
            detailImages = product.getDetailImages();
        }
        String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
        shopProduct.setRemark(remark);
        if (shopProduct.getStock() > 0) {
            shopProduct.setStockTime(System.currentTimeMillis());
        }

        String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
        int state = product.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
        if (state == ProductNewStateEnum.up_sold.getIntValue()) {
            platformProductState = "0";
        }
        shopProduct.setPlatformProductState(platformProductState);
    }


    public ShopProduct getShopProductInfoNoStock4App(long shopProductId) {
        ShopProduct shopProduct = shopProductMapper.selectById (shopProductId);
        if (shopProduct == null) {
            return shopProduct;
        }

        if (shopProduct.getOwn() == 0) {
            long productId = shopProduct.getProductId();
            ProductNew product = productNewMapper.findById(productId);
//            ProductNew product = productNewMapper.selectById (productId);
            if (product == null) {
                logger.error("小程序商品查询,没有找的对应的供应商商品 productId={},shopProductId={}", productId, shopProductId);
                return shopProduct;
            }
            shopProduct.setXprice(product.getWholeSaleCash());

            shopProduct.setClothesNumber(product.getClothesNumber());
            shopProduct.setVideoUrl(product.getVideoUrl());

            //获取库存需要查询sku信息非常慢，所有不进行填充，如果发小其他问题请单独获取
            shopProduct.setSummaryImages(product.getDetailImages());

            String sizeTableImage = product.getSizeTableImage();
            String detailImages = product.getSummaryImages();
            if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
                detailImages = product.getDetailImages();
            }
            String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
            shopProduct.setRemark(remark);

            String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
            int state = product.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            if (state == ProductNewStateEnum.up_sold.getIntValue()) {
                platformProductState = "0";
            }
            shopProduct.setPlatformProductState(platformProductState);
        }
        return shopProduct;
    }



    /**
     * 获得商家商品列表（不填充平台商品库存）
     */
    public ShopProduct getShopProductInfoNoStock(long shopProductId) {
        ShopProduct shopProduct = shopProductMapper.findProductIdAndOwnById(shopProductId);
//        ShopProduct shopProduct = shopProductMapper.selectById (shopProductId);
        if (shopProduct == null) {
            return shopProduct;
        }

        if (shopProduct.getOwn() == 0) {
            long productId = shopProduct.getProductId();
            ProductNew product = productNewMapper.findById(productId);
//            ProductNew product = productNewMapper.selectById (productId);
            if (product == null) {
                logger.error("小程序商品查询,没有找的对应的供应商商品 productId={},shopProductId={}", productId, shopProductId);
                return shopProduct;
            }
            shopProduct.setXprice(product.getWholeSaleCash());

            shopProduct.setClothesNumber(product.getClothesNumber());
            shopProduct.setVideoUrl(product.getVideoUrl());

            //获取库存需要查询sku信息非常慢，所有不进行填充，如果发小其他问题请单独获取
            shopProduct.setSummaryImages(product.getDetailImages());

            String sizeTableImage = product.getSizeTableImage();
            String detailImages = product.getSummaryImages();
            if (!StringUtils.isEmpty(detailImages) && JSON.parseArray(detailImages).size() <= 0) {
                detailImages = product.getDetailImages();
            }
            String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
            shopProduct.setRemark(remark);

            String platformProductState = "1";// 商品状态:0已上架、1已下架、2已删除
            int state = product.getState();//商品状态： 0（编辑中，未完成编辑）、 1（新建，编辑完成、待提审）、2（待提审）、3（待审核，审核中）、4（审核不通过）、 5（待上架，审核通过、待上架）、 6（上架，审核通过、已上架）、 7（下架，审核通过、已下架）
            if (state == ProductNewStateEnum.up_sold.getIntValue()) {
                platformProductState = "0";
            }
            shopProduct.setPlatformProductState(platformProductState);
        }
        return shopProduct;
    }

    /**
     * 获取商家商品列表
     *
     * @param tabType 状态:1:在售商品,2:已下架,0:草稿’
     * @param owns    '是否是自有商品：0平台供应商商品, 1是用户自定义款，2用户自营平台同款'
     */
    public Map<String, Object> getShopProductList(int tabType, int sortType, Page<ShopProduct> page, StoreBusiness storeBusiness, int isOrder, int own, int isStock, String keyWords, long tagId, String owns) {
        long storeId = storeBusiness.getId();
        logger.info("获取商家商品列表:tabType:" + tabType + ",sortType:" + sortType + ",storeId:" + storeId + ",keyWords" + keyWords);
        Map<String, Object> data = new HashMap<String, Object>();
        Wrapper<ShopProduct> wrapper = null;
        // 1、获取商家商品列表
        if (isOrder == -1) {
            wrapper = getShopProductListWrapper(tabType, sortType, storeId, keyWords);
        } else {
            wrapper = getShopProductListWrapper(tabType, sortType, storeId, isOrder, own, isStock, keyWords);
        }
        /*if (storeBusiness.getVip () == 0) {
            wrapper.and (" product_id NOT IN ( SELECT id FROM yjj_Product WHERE vip=1 ) ");
        }*/
        if (StringUtils.isNotBlank(owns)) {
            wrapper.and(" own IN (" + owns + ")");
        }

        if (tagId != 0) {
            wrapper.and("id IN ( SELECT shop_product_id FROM shop_tag_product WHERE tag_id = " + tagId + " )");
        }
        //1、获取所有商家商品列表
        long a = System.currentTimeMillis();
        List<ShopProduct> shopProductList = shopProductMapper.selectList(wrapper);

        long b = System.currentTimeMillis();
        long c = b - a;
        logger.info("1、获取所有商家商品列表总耗时：" + c + "毫秒，shopProductList.size():" + shopProductList.size());

        //2、获取平台商品ID Map集合，key：商品Id，value:原始位置角标
        Map<Long, Integer> productIdIndexMap = new HashMap<Long, Integer>();
        for (int i = 0; i < shopProductList.size(); i++) {
            ShopProduct shopProduct = shopProductList.get(i);
            if (shopProduct.getOwn() == 0) {
                long productId = shopProduct.getProductId();
                productIdIndexMap.put(productId, i);
            }
        }
        logger.info("2、获取平台商品ID Map集合productIdIndexMap.size():" + productIdIndexMap.size());
        //3、根据平台商品ID集合获取平台商品列表列表
        long d = System.currentTimeMillis();
        List<Long> productIdList = new ArrayList<Long>(productIdIndexMap.keySet());
        List<ProductNew> platformProductNewList = new ArrayList<ProductNew>();
        if (productIdList.size() > 0) {
            platformProductNewList = productNewMapper.selectBatchIds(productIdList);
        }

        long e = System.currentTimeMillis();
        long f = e - d;
        logger.info("3、根据平台商品ID集合获取平台商品列表列表，总耗时：" + f + "毫秒,platformProductNewList.size():" + platformProductNewList.size());

        //4、填充平台商品数据
        long g = System.currentTimeMillis();
        fillPlatformShopProductInfo(shopProductList, platformProductNewList, productIdIndexMap);
        long h = System.currentTimeMillis();
        long l = h - g;
        logger.info("4、填充平台商品数据，总耗时：" + l + "毫秒");

        //5、获取分页数据
        List<ShopProduct> pageList;
        boolean isMore = false;
        if (page == null) {
            pageList = shopProductList;
        } else {
            // 3、分页,注意程序中翻页要分装成工具类使用，避免角标计算错误
            // 每页开始数量
            int fromIndex = page.getOffset();
//			logger.info("获取商家商品列表，fromIndex："+fromIndex);
            // 每页结束数量
            int toIndex = fromIndex + page.getLimit();
//			logger.info("获取商家商品列表，toIndex："+toIndex);
            // 剩余最大数量
            int maxIndex = shopProductList.size();
//			logger.info("获取商家商品列表，maxIndex："+maxIndex);
            if (maxIndex <= toIndex) {
                toIndex = maxIndex;
            }
//			logger.info("获取商家商品列表，toIndex："+toIndex);
            // list截取含头不含尾
            pageList = shopProductList.subList(fromIndex, toIndex);
            if (toIndex < maxIndex) {
                isMore = true;
            }
        }

        //6、转换商家商品数据和按照置顶分组后合并
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> topProductList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> unTopProductList = new ArrayList<Map<String, Object>>();
        for (ShopProduct shopProduct : pageList) {
            Map<String, Object> shopProductMap = buildShopProduct(shopProduct, storeId);
            if (shopProduct.getTopTime() == 0) {
                topProductList.add(shopProductMap);
            } else {
                unTopProductList.add(shopProductMap);
            }
        }
        productList.addAll(unTopProductList);
        productList.addAll(topProductList);

        data.put("productList", productList);
        data.put("isMore", isMore);
//		logger.info("获取商家商品列表结束:isMore"+isMore+",data:"+JSON.toJSONString(data));

        return data;
    }

//	/**
//	 * 排序
//	 * @param list
//	 * @param sortStr
//	 * @return
//	 */
//	private List<Map<String, Object>> sortList(List<Map<String, Object>> list, String sortStr) {
//		list.sort(new Comparator<Map<String, Object>>() {
//			@Override
//			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//				return (long) o1.get(sortStr) > (long) o2.get(sortStr) ? -1 : 1;
//			}
//		});
//		return list;
//	}

    private Map<String, Object> buildShopProduct(ShopProduct shopProduct, long storeId) {
        long shopProductId = shopProduct.getId();
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("id", shopProductId + "");
        productMap.put("productId", shopProduct.getProductId());
        productMap.put("name", shopProduct.getName());
        productMap.put("price", shopProduct.getPrice());
        productMap.put("xprice", shopProduct.getXprice() + "");
        productMap.put("clothesNumber", shopProduct.getClothesNumber());
        productMap.put("firstImage", shopProduct.getFirstImage());
        productMap.put("own", shopProduct.getOwn());// 是否是自有商品：1是自有商品，0平台商品

        //商家状态  0：草稿，1：上架， 2：下架
        int soldOut = shopProduct.getSoldOut();
        if (soldOut != 0) {
            productMap.put("wantMemberCount", shopProduct.getWantMemberCount());// 想要会员数量
            productMap.put("showCount", shopProduct.getShowCount());// 浏览数量
            //商品VIP属性
            long productId = shopProduct.getProductId();
            Product product = null;
            if (productId > 0) {
                product = productMapper.getProduct(productId);
                if (product != null) {
                    productMap.put("vip", product.getVip());
                } else {
                    logger.info("获取商家商品信息为空，请尽快排查问题，shopProduct.getProductId():" + shopProduct.getProductId() + ",shopProductId:" + shopProduct.getId());
                }
            }

            // 现货时间为0表示没有现货，0没现货、1有现货
            String isStock = "0";
            // 0：购买过，1：未购买过
            int tabType = shopProduct.getTabType();
            if (tabType == ShopProduct.tab_type_shop_sift) {// 购买过时一直是1为有现货
                isStock = "1";
            } else if (tabType == ShopProduct.tab_type_buyer_recommend) {// 未未购买过根据设置现货时间判断
                if (shopProduct.getStockTime() != 0) {
                    isStock = "1";
                } else {
                    isStock = "0";
                }
            }
            productMap.put("isStock", isStock);

            // 推荐时间为0表示没有推荐
            productMap.put("isTop", shopProduct.getTopTime() == 0 ? "0" : "1");
//			平台商品状态:0已上架、1已下架、2已删除
            productMap.put("platformProductState", shopProduct.getPlatformProductState());


            //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
            int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
            productMap.put("intoActivity", intoActivity);//商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动


        }

        //库存
        int inventory = 0;
        if (ObjectUtils.nullSafeEquals(shopProduct.getOwn(), 0) && shopProduct.getProductId() > 0) {
            //平台供应商商品
            inventory = productSKUMapper.supplierProductInventory(shopProduct.getProductId());
            logger.info("查询供应商商品库存 shopProductId[{}].inventory[{}]", shopProductId, inventory);
        } else if (ObjectUtils.nullSafeEquals(shopProduct.getOwn(), 1) || ObjectUtils.nullSafeEquals(shopProduct.getOwn(), 2)) {
            if (shopProduct.getFirstTimeOnSale() == 0) {
                //查询草稿中的库存
                ShopProductTempInfo query = new ShopProductTempInfo();
                query.setShopProductId(shopProductId);
                query.setStatus(1);
                ShopProductTempInfo shopProductTempInfo = shopProductTempInfoMapper.selectOne(query);
                if (shopProductTempInfo != null) {
                    String skuJson = shopProductTempInfo.getSkuJson();
                    List<ProductSkuRbSimpleVoDepreate> skuList = BizUtil.jsonStrToListObject(skuJson, List.class, ProductSkuRbSimpleVoDepreate.class);
                    if (!ObjectUtils.isEmpty(skuList)) {
                        for (ProductSkuRbSimpleVoDepreate sku : skuList) {
                            inventory += sku.getRemainCount();
                        }
                    }
                }
                logger.info("查询自营商品未初始化库存 shopProductId[{}].inventory[{}]", shopProductId, inventory);
            } else {
                //查找直接库存
                inventory = productSKUMapper.storeProductInventory(shopProductId);
                logger.info("查询自营商品真实库存 shopProductId[{}].inventory[{}]", shopProductId, inventory);
            }
        } else {
            logger.error("未知的商品类型! shopProductId[{}]", shopProductId);
        }
        productMap.put("inventory", inventory);
        return productMap;
    }


    /**
     * 从2.2版本开始的新商品列表
     * <p>
     * tabType 状态:1：在售商品，2：已下架、0草稿’
     * sortType 排序方式 ：0上架时间、1想要、浏览量
     * isOrder:0已下单、1未下单
     * own 是否是自有商品 1自有商品  0平台商品
     *
     * @param tabType
     * @param sortType
     * @param storeId
     * @return
     */
    private Wrapper<ShopProduct> getShopProductListWrapper(int tabType, int sortType, long storeId, int isOrder, int own, int isStock, String keyWords) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId);
        wrapper.eq("sold_out", tabType);
        // 拼接条件
        if (tabType == 1 || tabType == 2) {//已上架或已下架
            //是否下单  0已下单、1未下单
            if (isOrder == 0) {// 已下单
                wrapper.eq("tab_type", 0);//0：已下单，1：未下单
            }

            //是否自有商品
            if (own == 1) {//自有商品
                wrapper.eq("own", 1); //是否是自有商品：1是自有商品，0平台商品
            }

            //是否有现货
            if (isStock == 1) {
                wrapper.gt("stock_time", 0);//大于0表示有现货
            }
            //有关键字搜索
            if (StringUtils.isNotBlank(keyWords)) {
//				wrapper.like("name", keyWords);
                wrapper.and(" ((name LIKE '%" + keyWords + "%') or (product_id in (SELECT id FROM yjj_Product WHERE NAME LIKE '%" + keyWords + "%')))");
            }

            //排序方式
            if (tabType == 2) {// 上架时间排序
                wrapper.orderBy(" top_time desc , update_time desc ");
            } else if (sortType == 0) {// 上架时间排序
                wrapper.orderBy(" top_time desc , ground_time desc ");
            } else if (sortType == 1) {// 想要排序
                // wrapper.orderBy("update_time",false);
                wrapper.orderBy(" top_time desc , want_member_count desc, ground_time desc ");
            } else if (sortType == 2) {// 浏览量
                // wrapper.orderBy("update_time",false);
                wrapper.orderBy(" top_time desc , show_count desc, ground_time desc");
            } else {
                logger.info("获取商家商品列表参数错误,tabType：" + tabType + ",sortType：" + sortType + ",isOrder：" + isOrder);
            }
        } else if (tabType == 0) { //草稿
            wrapper.orderBy(" top_time desc , update_time  desc ");
        } else {
            logger.info("获取商家商品列表参数错误,tabType：" + tabType + ",sortType：" + sortType + ",isOrder：" + isOrder);
        }
        return wrapper;
    }

    /**
     * 推荐/取消推荐商品
     *
     * @param shopProductId
     * @param isTop
     * @param ip
     * @param client
     * @return
     */
    public void updateTop(Long shopProductId, Integer isTop, @ClientIp String ip, ClientPlatform client, Long storeId) {
		/*if(isTop == 1){
			Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("store_id", storeId).eq("status", 0).gt("top_time", 0);
			int count = shopProductMapper.selectCount(wrapper);
			if(count>=ShopProduct.max_product_recommend_count){//V3.3时该限制去掉了
				throw new RuntimeException(ShopProduct.max_product_recommend_count+"个商品推荐位已用完");
			}
		}*/

        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setProductId(shopProductId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setCreateTime(System.currentTimeMillis());
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        // shopProductLog.setNet(2);
        shopProductLog.setType(5);
        shopProductLog.setStoreId(storeId);

        if (isTop == 1) {
            shopProductLog.setContent("推荐");
        } else if (isTop == 0) {
            shopProductLog.setContent("取消推荐");
        } else {
            throw new RuntimeException("请确认商品状态");
        }

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setStoreId(storeId);
        shopProduct.setUpdateTime(System.currentTimeMillis());
        if (isTop == 0) {
            shopProduct.setTopTime(0L);
        } else if (isTop == 1) {
            shopProduct.setTopTime(shopProduct.getUpdateTime());
        } else {
            throw new RuntimeException("商品无法更改成该状态");
        }

        int result = shopProductMapper.updateById(shopProduct);
        if (result != 1) {
            throw new RuntimeException("推荐/取消推荐商品失败");
        }

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("推荐/取消推荐商品记录失败");
        }
    }

    /**
     * 商品现货/取消现货
     *
     * @param shopProductId
     * @param isStock
     * @param ip
     * @param client
     * @return
     */
    public void updateStock(Long shopProductId, Integer isStock, @ClientIp String ip, ClientPlatform client,
                            Long storeId) {
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setStoreId(storeId);
        shopProduct.setUpdateTime(System.currentTimeMillis());
        if (isStock == 0) {
            shopProduct.setStockTime(0L);
        } else if (isStock == 1) {
            shopProduct.setStockTime(shopProduct.getUpdateTime());
        } else {
            throw new RuntimeException("商品无法更改成该状态");
        }

        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setStoreId(storeId);
        shopProductLog.setProductId(shopProductId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setCreateTime(System.currentTimeMillis());
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        // shopProductLog.setNet(2);
        shopProductLog.setType(6);

        if (isStock == 1) {
            shopProductLog.setContent("设为现货");
        } else if (isStock == 0) {
            shopProductLog.setContent("取消现货");
        } else {
            throw new RuntimeException("请确认商品状态");
        }

        int result = shopProductMapper.updateById(shopProduct);
        if (result != 1) {
            throw new RuntimeException("商品设为现货/取消现货失败");
        }

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("商品设为现货/取消现货记录失败");
        }
    }

    /**
     * 上架/下架商品
     *
     * @param shopProductId
     * @param soldOut
     * @param ip
     * @param client
     * @return
     */
    public void updateSoldOut(long shopProductId, Integer soldOut, @ClientIp String ip, ClientPlatform client, Long storeId) {

        //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
        if (intoActivity != 0) {
            throw new RuntimeException("不可操作！该商品正在活动中");
        }

        ShopProduct shopProductOld = getShopProductInfoNoStock4App(shopProductId);
        if (shopProductOld.getOwn() == 0 && soldOut == 1 && !("0".equals(shopProductOld.getPlatformProductState()))) {
            throw new RuntimeException("平台未上架此商品");
        }
        long currentTime = System.currentTimeMillis();

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setStoreId(storeId);
        shopProduct.setUpdateTime(currentTime);
        shopProduct.setSoldOut(soldOut);
        if (soldOut == 2) {//下架
            shopProduct.setTopTime(0L);
            shopProduct.setGroundTime(0L);
        } else if (soldOut == 1) {//上架
            shopProduct.setGroundTime(shopProduct.getUpdateTime());
        }

        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setProductId(shopProductId);
        shopProductLog.setStoreId(storeId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setCreateTime(currentTime);
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        // shopProductLog.setNet(2);
        shopProductLog.setType(4);

        if (soldOut == 1) {
            shopProductLog.setContent("上架");
        } else if (soldOut == 2) {
            shopProductLog.setContent("下架");
        } else {
            throw new RuntimeException("请确认商品状态");
        }

        int result = shopProductMapper.updateById(shopProduct);
        if (result != 1) {
            throw new RuntimeException("上架/下架商品失败");
        }

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("上架/下架商品记录失败");
        }
    }


    /**
     * 商品改零售价
     *
     * @param shopProductId
     * @param price
     * @param ip
     * @param client
     * @return
     */
    public void updatePrice(long shopProductId, double price, @ClientIp String ip, ClientPlatform client, long storeId) {

        //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
        if (intoActivity != 0) {
            throw new RuntimeException("不可操作！该商品正在活动中");
        }

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setUpdateTime(System.currentTimeMillis());
        shopProduct.setPrice(price);
        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setProductId(shopProductId);
        shopProductLog.setStoreId(storeId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setType(3);
        shopProductLog.setCreateTime(System.currentTimeMillis());
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        ShopProduct shopProductOld = getShopProductInfoNoStock4App(shopProductId);
        shopProductLog.setContent(JSONObject.toJSONString(shopProductOld));

        int result = shopProductMapper.updateById(shopProduct);
        if (result != 1) {
            throw new RuntimeException("商品改零售价失败");
        }

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("商品改零售价记录失败");
        }
    }


    /**
     * 商品删除
     *
     * @param shopProductId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long shopProductId, @ClientIp String ip, ClientPlatform client, Long storeId) {
        //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
        if (intoActivity != 0) {
            throw new RuntimeException("不可操作！该商品正在活动中");
        }

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setStoreId(storeId);
        shopProduct.setStatus(-1);

        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setProductId(shopProductId);
        shopProductLog.setStoreId(storeId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setCreateTime(System.currentTimeMillis());
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        // shopProductLog.setNet(2);
        shopProductLog.setType(2);
        shopProductLog.setContent("删除");

        int result = shopProductMapper.updateById(shopProduct);
        if (result != 1) {
            throw new RuntimeException("删除商品失败");
        }

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("删除商品记录失败");
        }

        //通知购物车该商品已失效
        shopGoodsCarService.adviceGoodsCarThisProductHasDisabled(shopProductId, storeId);
    }


    /**
     * 保存
     *
     * @param tagIds  标签的属性值id,字符串以逗号拼接
     * @param colorId 颜色的属性值id
     * @param sizeId  尺码的属性值id
     */
    @Transactional(rollbackFor = Exception.class)
    public long save(long id, String name, double price, String clothesNumber, long categoryId, int isStock,
                     String summaryImages, String shopOwnDetail, int soldOut, String videoDisplayUrl, Long videoFileId, String videoImage,
                     @ClientIp String ip, ClientPlatform client, long storeId, String tagIds, Long colorId, Long sizeId) {
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setUpdateTime(System.currentTimeMillis());

        //设置默认值
        allocateDefaultValue(id, name, price, clothesNumber, categoryId, isStock, summaryImages, shopOwnDetail, soldOut, videoDisplayUrl, videoFileId, videoImage, shopProduct);

        // 获取标签id,并校验tagIds
        List<Long> tagIdList = split2List(tagIds, ",");
        if (!tagIdList.isEmpty()) {
            List<ShopTag> shopTagList = shopTagService.listByIds(storeId, tagIdList);
            if (shopTagList.size() < tagIdList.size()) {
                throw new RuntimeException("未找到标签信息");
            }
        }

        //文本长度校验 校验放在前端做,老版本是不定长的, 以后如果强制更新了, 这段code可以放开
//        if (StringUtils.isNotBlank (shopOwnDetail)) {
//            List<Map<String, String>> details = new Gson ().fromJson (shopOwnDetail, new TypeToken<List<Map<String, String>>> (){}.getType ());
//            for (Map<String, String> detailMap : details) {
//                if (ObjectUtils.nullSafeEquals ("1", detailMap.get ("type"))) {
//                    if (StringUtils.isNotBlank (detailMap.get ("content")) && detailMap.get ("content").length () > 500) {
//                        throw new RuntimeException ("文本长度不可大于500");
//                    }
//                }
//            }
//        }

        //3.7.7版本之前color和size没有,不需要设置颜色尺码
        //如果颜色尺码信息没提交,也不需要更新颜色尺码
        boolean isVersionAfter377 = colorId.longValue() != 0 || sizeId.longValue() != 0;
        if (isVersionAfter377) {
            //只要有一个不为空,其他的都不能是空
            if (colorId.equals(0)) {
                throw new RuntimeException("颜色必填");
            }
            if (sizeId.equals(0)) {
                throw new RuntimeException("尺码必填");
            }

            List<Long> colorSizeIds = Arrays.asList(colorId, sizeId);
            List<PropertyValueNew> colorSizeEntityList = propertyValueNewMapper.selectBatchIds(colorSizeIds);
            //校验是否参数合法
            verifyPropValPermission(storeId, colorSizeIds, colorSizeEntityList);
            colorSizeEntityList.forEach(cs -> {
                if (cs.getId().equals(colorId)) {
                    shopProduct.setColorId(cs.getId());
                    shopProduct.setColorName(cs.getPropertyValue());
                } else {
                    shopProduct.setSizeId(cs.getId());
                    shopProduct.setSizeName(cs.getPropertyValue());
                }
            });
        }


        shopProduct.setTagIds(tagIds);
        shopProduct.setStoreId(storeId);
        shopProduct.setOwn(ShopProduct.own_product);
        // 保存/编辑商品
        int result = 0;
        if (id <= 0) {
            shopProduct.setCreateTime(shopProduct.getUpdateTime());
            result = shopProductMapper.insert(shopProduct);
            // 同步tagIds信息
            shopTagProductService.synchronizePduTagInfo(storeId, shopProduct.getId(), tagIdList);
        } else {
            //校验是否是用户商品
            ShopProduct query = new ShopProduct();
            query.setId(id);
            query.setStoreId(storeId);
            ShopProduct history = shopProductMapper.selectOne(query);
            if (history == null) {
                throw new RuntimeException("未找到商品信息");
            }
            //更新商品
            result = shopProductMapper.updateById(shopProduct);


            //如果更新了sku信息,通知购物车将该商品设为失效
            boolean isSkuHasChange = isSkuHasChangeAfterV377(history, shopProduct);
            if (isSkuHasChange) {
                shopGoodsCarService.adviceGoodsCarThisProductHasDisabled(history.getId(), storeId);
            }

            // 同步tagIds信息
            if (!ObjectUtils.nullSafeEquals(history.getTagIds(), tagIds)) {
                shopTagProductService.synchronizePduTagInfo(storeId, shopProduct.getId(), tagIdList);
            }
        }
        if (result != 1) {
            throw new RuntimeException("保存商品失败");
        }

        // 记录日志
        addShopProductLog(id, shopProduct, soldOut, ip, client);
        return id <= 0 ? shopProduct.getId() : id;
    }


    /**
     * 商品sku信息是否更改
     * <p>3.7.7版本之前sku信息是0或null,版本迭代不认为是sku消息更改了
     *
     * @param history     history
     * @param shopProduct shopProduct
     * @return boolean 用户有sku信息.又更改了sku信息:true
     * @author Charlie(唐静)
     * @date 2018/7/11 17:14
     */
    private boolean isSkuHasChangeAfterV377(ShopProduct history, ShopProduct shopProduct) {
        //用户是否提交了sku信息
        boolean userHasSubmitSkuInfo = isColorAndSizeNotNull(shopProduct);
        //该商品有sku历史信息,没有(377以前版本)则不管
        boolean userHistorySkuNotNull = isColorAndSizeNotNull(history);

        if (userHasSubmitSkuInfo && userHistorySkuNotNull) {
            return !history.getColorId().equals(shopProduct.getColorId()) || !history.getSizeId().equals(shopProduct.getSizeId());
        }
        return false;
    }


    /**
     * sku信息不是空
     * <p>3.7.7之前的版本会是空或者0
     *
     * @param shopProduct shopProduct
     * @return boolean
     * @author Charlie(唐静)
     * @date 2018/7/11 17:21
     */
    private boolean isColorAndSizeNotNull(ShopProduct shopProduct) {
        return shopProduct.getColorId() != null
                && shopProduct.getSizeId() != null
                && shopProduct.getColorId() != 0
                && shopProduct.getSizeId() != 0;
    }


    /**
     * 校验用户是否拥有属性的权限
     *
     * @param storeId           storeId
     * @param propValIds        用户请求的属性值id
     * @param propValEntityList 数据库查出的属性值实体类
     * @return void
     * @author Charlie(唐静)
     * @date 2018/7/9 16:07
     */
    private void verifyPropValPermission(long storeId, List<Long> propValIds, List<PropertyValueNew> propValEntityList) {
        if (propValEntityList.size() != propValIds.size()) {
            throw new RuntimeException("不存在的属性值");
        }
        for (PropertyValueNew pv : propValEntityList) {
            if (!pv.getStatus().equals(0)) {
                throw new RuntimeException("不存在的属性值");
            }

            if (pv.getSupplierId() == -1) {
                //门店用户私有的属性值
                if (!pv.getStoreId().equals(storeId)) {
                    throw new RuntimeException("非法访问,不是用户自有的属性");
                }
            }
        }

    }


    /**
     * 切割字符串
     *
     * @param str 需要切割的字符串
     * @param reg 切割符号
     * @return java.util.List<java.lang.Long>
     * @author Charlie(唐静)
     * @date 2018/7/9 15:19
     */
    private List<Long> split2List(String str, String reg) {
        StringTokenizer st = new StringTokenizer(str, reg);
        int count = st.countTokens();
        List<Long> result = new ArrayList<>(count);
        if (count > 0) {
            while (st.hasMoreTokens()) {
                String idStr = st.nextToken();
                result.add(Long.parseLong(idStr));
            }
        }
        return result;
    }


    /**
     * 设置默认值
     *
     * @param id              id
     * @param name            name
     * @param price           price
     * @param clothesNumber   clothesNumber
     * @param categoryId      categoryId
     * @param isStock         isStock
     * @param summaryImages   summaryImages
     * @param shopOwnDetail   shopOwnDetail
     * @param soldOut         soldOut
     * @param videoDisplayUrl videoDisplayUrl
     * @param videoFileId     videoFileId
     * @param videoImage      videoImage
     * @param shopProduct     shopProduct
     */
    private void allocateDefaultValue(long id, String name, double price, String clothesNumber, long categoryId, int isStock, String summaryImages, String shopOwnDetail, int soldOut, String videoDisplayUrl, Long videoFileId, String videoImage, ShopProduct shopProduct) {
        if (id > 0) {
            shopProduct.setId(id);
        }
        if (!(StringUtils.isEmpty(name.trim()))) {
            shopProduct.setName(name);
        }
        if (price > 0) {
            shopProduct.setPrice(price);
        }
        if (!(StringUtils.isEmpty(clothesNumber.trim()))) {
            shopProduct.setClothesNumber(clothesNumber);
        }
        if (categoryId > 0) {
            shopProduct.setCategoryId(categoryId);
        }
        shopProduct.setTabType(ShopProduct.tab_type_buyer_recommend);
        if (!(StringUtils.isEmpty(summaryImages.trim()))) {
            String[] imageArray = summaryImages.split(",");
            JSONArray jsonArray = (JSONArray) JSON.toJSON(imageArray);
            shopProduct.setSummaryImages(jsonArray.toJSONString());
        }
        if (!(StringUtils.isEmpty(shopOwnDetail.trim()))) {
            shopProduct.setShopOwnDetail(shopOwnDetail);
        }
        shopProduct.setSoldOut(soldOut);
        if (soldOut == 1) {
            shopProduct.setGroundTime(shopProduct.getUpdateTime());
        }
        if (isStock > 0) {
            shopProduct.setStockTime(shopProduct.getUpdateTime());
        }
        if (!StringUtils.isEmpty(videoDisplayUrl)) {
            shopProduct.setVideoDisplayUrl(videoDisplayUrl);
        }
        if (videoFileId > 0) {
            shopProduct.setVideoDisplayFileId(videoFileId);
        }
        if (!StringUtils.isEmpty(videoImage)) {
            shopProduct.setVideoDisplayImage(videoImage);
        }
    }


    /**
     * 添加log日志
     *
     * @param shopProduct
     * @param ip
     * @param client
     */
    private void addShopProductLog(long id, ShopProduct shopProduct, int soldOut, @ClientIp String ip, ClientPlatform client) {
        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setProductId(shopProduct.getId());
        shopProductLog.setAdminId(1L);
        if (id <= 0) {
            shopProductLog.setType(1);
        } else {
            shopProductLog.setType(3);
        }
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        //shopProductLog.setNet(2);

        String productJson = JSONObject.toJSONString(shopProduct);
        shopProductLog.setContent(productJson);

        shopProductLog.setCreateTime(shopProduct.getCreateTime());

        int logResult = shopProductLogMapper.insert(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("保存商品记录失败");
        }
    }


    /**
     * 编辑前获取数据并回显
     *
     * @param shopProductId
     * @return
     */
    public Map<String, Object> toUpdate(Long shopProductId) {
        ShopProduct shopProduct = getShopProductInfoNoStock4App(shopProductId);
        if (shopProduct == null) {
            throw new RuntimeException("没有这个商品");
        }

        Map<String, Object> product = new HashMap<String, Object>();
        product.put("id", shopProduct.getId() + "");
        product.put("name", shopProduct.getName());
        product.put("price", shopProduct.getPrice() + "");
        product.put("clothesNumber", shopProduct.getClothesNumber());

        product.put("isStock", shopProduct.getStockTime() > 0 ? 1 : 0);
        product.put("categoryId", shopProduct.getCategoryId());
        product.put("summaryImages", shopProduct.getSummaryImages());
        product.put("shopOwnDetail", shopProduct.getShopOwnDetail());
        product.put("soldOut", shopProduct.getSoldOut());
        product.put("videoUrl", shopProduct.getVideoDisplayUrl());
        product.put("videoFileId", shopProduct.getVideoDisplayFileId());
        product.put("videoImage", shopProduct.getVideoDisplayImage());
        product.put("colorId", shopProduct.getColorId());
        product.put("color", shopProduct.getColorName());
        product.put("sizeId", shopProduct.getSizeId());
        product.put("size", shopProduct.getSizeName());

        List<ShopTagProduct> tagList = shopTagProductService.getBindProductTagList(shopProduct.getId(), shopProduct.getStoreId());
        product.put("tagInfos", tagList);
        return product;
    }


    /**
     * 获取门店商品详情
     *
     * @param
     * @return
     */
    public Map<String, Object> getProductItem(Long shopProductId, Long storeId) {
        if (storeId == null || storeId < 1) {
            throw new RuntimeException("会员信息有误,请确认");
        }
        if (shopProductId == null || shopProductId < 1) {
            throw new RuntimeException("商品Id错误");
        }
        ShopProduct shopProduct = getShopProductInfoNoStock4App(shopProductId);
        if (shopProduct == null) {
            throw new RuntimeException("没有找到商家商品");
        }
        long productId = shopProduct.getProductId();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> productMap = new HashMap<String, Object>();


        productMap.put("id", shopProduct.getId() + "");
        productMap.put("productId", String.valueOf(productId));
        String platformProductState = shopProduct.getPlatformProductState();

        productMap.put("platformProductState", platformProductState);
        productMap.put("name", shopProduct.getName());
        productMap.put("price", String.valueOf(shopProduct.getPrice()));
        productMap.put("xprice", String.valueOf(shopProduct.getXprice()));
        productMap.put("videoUrl", shopProduct.getVideoDisplayUrl());
        String videoImage = shopProduct.getVideoDisplayImage();
        if (StringUtils.isEmpty(videoImage)) {
            productMap.put("videoImage", "");
        } else {
            productMap.put("videoImage", videoImage);
        }

        if (shopProduct.getStockTime() == 0) {
            productMap.put("isStock", "0");
        } else {
            productMap.put("isStock", "1");
        }

        if (shopProduct.getTopTime() == 0) {
            productMap.put("isTop", "0");
        } else {
            productMap.put("isTop", "1");
        }
        productMap.put("own", String.valueOf(shopProduct.getOwn()));
        productMap.put("soldOut", String.valueOf(shopProduct.getSoldOut()));
        productMap.put("tabType", String.valueOf(shopProduct.getTabType()));
        productMap.put("sizes", getPropertyValue(shopProduct.getSizeIds()));
        productMap.put("colors", getPropertyValue(shopProduct.getColorIds()));

        String summaryImages = shopProduct.getSummaryImages();
        if (StringUtils.isNotEmpty(summaryImages)) {
            JSONArray jsonArrayImages = JSONArray.parseArray(summaryImages);
            productMap.put("summaryImages", jsonArrayImages.toArray());
        } else {
            productMap.put("summaryImages", null);
        }
        String remark = shopProduct.getRemark();

        JSONArray detailImages = null;
        JSONArray sizeTableImage = null;
        if (!(StringUtils.isEmpty(remark))) {
            JSONObject jsonObject = JSONObject.parseObject(remark);
            // JSONArray jsonArrayRemark = jsonObject.getJSONArray("DetailImages");
            // org.json.JSONArray a = jsonObject.getJSONArray("SizeTableImage");
            // System.out.println(JSONArray.parseArray(jsonObject.getString("DetailImages")).toArray());

            productMap.put("detailImages", jsonObject.getJSONArray("DetailImages"));
            productMap.put("sizeTableImage", jsonObject.getJSONArray("SizeTableImage"));

            detailImages = jsonObject.getJSONArray("DetailImages");
            sizeTableImage = jsonObject.getJSONArray("SizeTableImage");
        }

        // 商家商品自定义详情
        productMap.put("shopOwnDetail", shopProduct.getShopOwnDetail());

        List<Map<String, String>> images = new ArrayList<Map<String, String>>();
        if (sizeTableImage != null && sizeTableImage.size() > 0) {
            for (Object object : sizeTableImage) {
                String sizeImage = (String) object;
                Map<String, String> image = new HashMap<String, String>();
                image.put("content", "");
                image.put("image", sizeImage);
                images.add(image);
            }
        }
        if (detailImages != null && detailImages.size() > 0) {
            for (Object object : detailImages) {
                String detailImage = (String) object;
                Map<String, String> image = new HashMap<String, String>();
                image.put("content", "");
                image.put("image", detailImage);
                images.add(image);
            }
        }
        productMap.put("remark", images);

        Wrapper<ShopMemberFavorite> wrapper = new EntityWrapper<ShopMemberFavorite>().eq("related_id", shopProductId)
                .eq("member_id", storeId).eq("type", 0);
        List<ShopMemberFavorite> wxaMemberFavoritenewList = wxaMemberFavoriteMapper.selectList(wrapper);
        if (wxaMemberFavoritenewList.size() != 1) {
            data.put("isFavorite", -1);
        } else {
            ShopMemberFavorite wxaMemberFavorite = wxaMemberFavoritenewList.get(0);
            if (wxaMemberFavorite == null || wxaMemberFavorite.getStatus() == -1) {
                data.put("isFavorite", -1);
            } else {
                data.put("isFavorite", 0);
            }
        }

        int totalInventory = 0;
        if (ObjectUtils.nullSafeEquals(shopProduct.getOwn(), 0)) {
            //平台供应商商品
            Product supplierProduct = productMapper.getProduct(productId);
            if (supplierProduct != null) {
                productMap.put("platformRetailPrice", supplierProduct.getCash());//平台零售价
                productMap.put("platformWholesalePrice", supplierProduct.getWholeSaleCash());//平台批发价
                productMap.put("minLadderPrice", supplierProduct.getMinLadderPrice() + "");//最小阶梯价格
                productMap.put("maxLadderPrice", supplierProduct.getMaxLadderPrice() + "");//最大阶梯价格
                productMap.put("ladderPriceJson", supplierProduct.getLadderPriceJson() + "");//阶梯价格JSON
                productMap.put("vip", supplierProduct.getVip());//
                productMap.put("clothesNumber", supplierProduct.getClothesNumber());//款号
                productMap.put("memberLevel", supplierProduct.getMemberLevel());//会员等级
                productMap.put("memberLadderPriceMin", supplierProduct.getMemberLadderPriceMin());//会员最小阶梯价格
                productMap.put("memberLadderPriceMax", supplierProduct.getMemberLadderPriceMax());//会员最大阶梯价格
                productMap.put("salTotalCount", supplierProduct.getSaleTotalCount());//总销量
                //查询供应商库存
                totalInventory = productSKUMapper.supplierProductInventory(supplierProduct.getId());
            }
        } else if (ObjectUtils.nullSafeEquals(shopProduct.getOwn(), 2)) {
            //自营同款
            Product productObj = productMapper.getProduct(productId);
            if (productObj != null) {
                //平台批发价?
                productMap.put("platformWholesalePrice", productObj.getWholeSaleCash());
                //最小阶梯价格(平台批发价)?
                productMap.put("minLadderPrice", productObj.getMinLadderPrice() + "");
            }
            //查询自营库存(这里不考虑草稿状态sku,草稿状态还真正创建商品,没有sku)
            totalInventory = productSKUMapper.storeProductInventory(shopProduct.getId());
            productMap.put("clothesNumber", shopProduct.getClothesNumber());
        } else {
            //查询自营库存(这里不考虑草稿状态sku,草稿状态还真正创建商品,没有sku)
            totalInventory = productSKUMapper.storeProductInventory(shopProduct.getId());
            productMap.put("clothesNumber", shopProduct.getClothesNumber());
        }
        //总库存
        productMap.put("totalInventory", totalInventory);

        //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        int intoActivity = myStoreActivityService.getShopProductActivityState(shopProductId, storeId);
        productMap.put("intoActivity", intoActivity);//商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        //商品绑定的标签列表
        List<ShopTagProduct> tagList = shopTagProductService.getBindProductTagList(shopProductId, storeId);
        productMap.put("tagList", tagList);

        data.put("product", productMap);
        return data;
    }


    public List<ShopProduct> getShopProductListByProIds(long storeId, Collection<Long> proIds) {
        return shopProductMapper.getShopProductListByProIds(storeId, proIds);
    }

    /**
     * 根据属性值Id获取对应的属性值
     *
     * @param propertyValueIds
     * @return
     */
    public List<String> getPropertyValue(String propertyValueIds) {
        if (propertyValueIds == null) {
            return new ArrayList<String>();
        }
        String[] propertyValueIdArray = propertyValueIds.split(",");
        List<String> propertyValueList = new ArrayList<String>();
        for (String propertyValueId : propertyValueIdArray) {
            ShopPropertyValue shopPropertyValue = shopPropertyValueMapper.selectById(propertyValueId);
            if (shopPropertyValue == null) {
                continue;
            }
            propertyValueList.add(shopPropertyValue.getPropertyValue());
        }
        return propertyValueList;
    }


    /**
     * 商品上传
     *
     * @param productId
     * @param storeId
     * @param price
     * @param shopProductName
     */
    public void uploadProduct(long productId, long storeId, @ClientIp String ip, ClientPlatform client, Double price, String shopProductName) {
        if (storeId <= 0) {
            throw new RuntimeException("没有该门店:storeId--------" + storeId);
        }
        Product product = productMapper.getProduct(productId);
        if (product == null) {
            throw new RuntimeException("没有该商品");
        }
//		ShopProduct entity = new ShopProduct();
//		entity.setProductId(productId);
//		entity.setStoreId(storeId);
//		entity.setStatus(0);
//		
//		ShopProduct shopProductOld = shopProductMapper.selectOne(entity);
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("product_id", productId);
        List<ShopProduct> shopProductList = shopProductMapper.selectList(wrapper);
        ShopProduct shopProductOld = null;
        if (shopProductList.size() > 1) {
            throw new RuntimeException("该门店该商品有多个");
        } else if (shopProductList.size() == 1) {
            throw new RuntimeException("该商品已经上传");
        } else {
            shopProductOld = new ShopProduct();
            shopProductOld.setStoreId(storeId);
            if (!StringUtils.isEmpty(shopProductName)) {
                shopProductOld.setName(shopProductName);
            }
            shopProductOld.setProductId(product.getId());
//			shopProductOld.setName(product.getName());
//			shopProductOld.setXprice(product.getWholeSaleCash());
//			shopProductOld.setMarketPrice((double) product.getMarketPrice());
//			shopProductOld.setClothesNumber(product.getClothesNumber());
//			shopProductOld.setVideoUrl(product.getVideoUrl());
            if (price != null && price > 0) {
                shopProductOld.setPrice(price);
            }
//
//			List<ProductSKU> productSKUList = productSKUMapper.getAllProductSKUsOfProduct(productId);
//			int stock = 0;
//			for (ProductSKU productSKU : productSKUList) {
//				if (productSKU == null) {
//					continue;
//				}
//				if (stock == 0) {
//					stock = productSKU.getRemainCount();
//				} else if (productSKU.getRemainCount() < stock) {
//					stock = productSKU.getRemainCount();
//				}
//			}
//			shopProductOld.setStock((long) stock);

            // shopProductOld.setCategoryId(product.getCategoryId());
            // shopProduct.setTagIds(tagIds);
            // shopProduct.setSizeIds(sizeIds);
            // shopProduct.setColorIds(colorIds);
            // String[] images = product.getDetailImageArray();
            // String image = "[";
            // for (int i=0;i<images.length;i++) {
            // if(i==images.length-1){
            // image = image+images[i];
            // }else{
            // image = image+images[i]+",";
            // }
            // }
            // image+="]";
//			shopProductOld.setSummaryImages(product.getDetailImages());

//			String sizeTableImage = product.getSizeTableImage();
//			String detailImages = product.getSummaryImages();
//			if (JSON.parseArray(detailImages).size() <= 0) {
//				detailImages = product.getDetailImages();
//			}
//			String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
//			shopProductOld.setRemark(remark);

            long time = System.currentTimeMillis();
            // if(shopProductOld.getStock()>0){
            // shopProductOld.setStockTime(time);
            // }

            shopProductOld.setTabType(1);
            shopProductOld.setSoldOut(1);
            shopProductOld.setStatus(0);
            shopProductOld.setCreateTime(time);
            shopProductOld.setUpdateTime(time);
            shopProductOld.setGroundTime(time);
            int record = shopProductMapper.insert(shopProductOld);
            if (record != 1) {
                throw new RuntimeException("商品上传失败:productId---" + shopProductOld.getProductId());
            }
        }

        ShopProductLog shopProductLog = new ShopProductLog();
        shopProductLog.setType(1);
        shopProductLog.setProductId(shopProductOld.getId());
        shopProductLog.setStoreId(storeId);
        shopProductLog.setAdminId(1L);
        shopProductLog.setCreateTime(System.currentTimeMillis());
        shopProductLog.setIp(ip);

        if (client != null) {
            String value = client.getPlatform().getValue();
            if ("pc".equals(value)) {
                shopProductLog.setPlatform(0);
            } else if ("android".equals(value)) {
                shopProductLog.setPlatform(1);
            } else if ("iphone".equals(value)) {
                shopProductLog.setPlatform(2);
            }
        }

        shopProductLog.setVersion(client.getVersion());
        // shopProductLog.setNet(2);

        shopProductLog.setContent(JSONObject.toJSONString(shopProductOld));

        int logResult = shopProductLogMapper.insertAllColumn(shopProductLog);
        if (logResult != 1) {
            throw new RuntimeException("商品改零售价记录失败:shopProductId---" + shopProductOld.getId());
        }
    }

    /**
     * 支付成功后将商品改为店家精选
     *
     * @param orderNo
     */
    public void updateTabTypeAfterPaySuccess(String orderNo) {
        // logger.error("updateTabTypeAfterPaySuccess:"+orderNo);
        ShopStoreOrder order = storeOrderMapper.getUserOrderByNoOnly(orderNo);
        long storeId = order.getStoreId();

        //如果买的是小程序服务
        if (order.getClassify() == 2) {
            logger.info("购买会员服务,支付后回调 orderNo[{}]", orderNo);
            Wrapper<StoreOrderItemNew> wrapper = new EntityWrapper<>();
            wrapper.eq("OrderNo", orderNo);
            wrapper.eq("StoreId", storeId);
            List<StoreOrderItemNew> shopOrderItemList = orderItemNewMapper.selectList(wrapper);
            if (shopOrderItemList.isEmpty()) {
                logger.error("购买会员服务, 未找到ShopStoreOrderItem storeId[{}].orderNo[{}]", storeId, orderNo);
                throw new RuntimeException("购买会员服务, 未找到ShopStoreOrderItem ");
            }
            if (shopOrderItemList.size() > 1) {
                logger.error("购买会员服务, 找到多条ShopStoreOrderItem记录 storeId[{}].orderNo[{}]", storeId, orderNo);
                throw new RuntimeException("购买会员服务, 找到多条ShopStoreOrderItem记录 ");
            }
            StoreOrderItemNew item = shopOrderItemList.get(0);
            memberService.buyMemberPackageOK(SystemPlatform.STORE.getCode(), storeId, item.getTotalMoney(), item.getMemberPackageType(), orderNo);
            return;
        }

        List<ShopStoreOrderItem> shopOrderItemList = orderItemMapper.getOrderNewItemsByOrderNO(storeId,
                Long.parseLong(orderNo));

//		logger.info("1-shopOrderItemList:" + shopOrderItemList);
        // logger.error("updateTabTypeAfterPaySuccess:"+shopOrderItemList);
        //购买的是商品
        List<Long> productIds = new ArrayList<Long>();
        for (ShopStoreOrderItem shopStoreOrderItem : shopOrderItemList) {

            long productId = shopStoreOrderItem.getProductId();
//			logger.info("1-productId:" + productId);
            if (productIds.contains(productId)) {
                continue;
            }
            ShopProductLog shopProductLog = new ShopProductLog();

            ShopProduct shopProductOld = getShopProduct(productId, storeId);//
//			logger.info("1-shopProductOld:" + shopProductOld);
            // logger.error("updateTabTypeAfterPaySuccess:"+shopOrderItemList);
            shopProductLog.setProductId(productId);
            shopProductLog.setStoreId(storeId);
            shopProductLog.setAdminId(1L);
            shopProductLog.setCreateTime(System.currentTimeMillis());

            if (shopProductOld != null) {//与存在则修改
                long time = System.currentTimeMillis();
                ShopProduct shopProduct = new ShopProduct();
                shopProduct.setId(shopProductOld.getId());
                shopProduct.setUpdateTime(time);
                shopProduct.setTabType(0);
                shopProduct.setSoldOut(1);
                shopProduct.setStockTime(time);
                int record = shopProductMapper.updateById(shopProduct);
//				logger.info("1-update:" + record);
                // logger.error("updateTabTypeAfterPaySuccess-update"+i+":"+record);
                if (record != 1) {
                    throw new RuntimeException("商品上传失败:shopProductId---" + shopProductOld.getId());
                }
                shopProductLog.setType(3);
            } else {
                Product product = productMapper.getProduct(productId);
                if (product == null) {
                    throw new RuntimeException("没有该商品");
                }
                shopProductOld = new ShopProduct();
                shopProductOld.setStoreId(storeId);
                shopProductOld.setProductId(product.getId());
                shopProductOld.setName(product.getName());
//				shopProductOld.setXprice(product.getWholeSaleCash());
//				shopProductOld.setMarketPrice((double) product.getMarketPrice());
//				shopProductOld.setClothesNumber(product.getClothesNumber());

                List<ProductSKU> productSKUList = productSKUMapper.getAllProductSKUsOfProduct(productId);
//				logger.info("1-productSKUList:" + productSKUList);
                int stock = 0;
                for (ProductSKU productSKU : productSKUList) {
                    if (productSKU == null) {
                        continue;
                    }
                    if (stock == 0) {
                        stock = productSKU.getRemainCount();
                    } else if (productSKU.getRemainCount() < stock) {
                        stock = productSKU.getRemainCount();
                    }
                }
                shopProductOld.setStock((long) stock);

                shopProductOld.setCategoryId(product.getCategoryId());
                // shopProduct.setTagIds(tagIds);
                // shopProduct.setSizeIds(sizeIds);
                // shopProduct.setColorIds(colorIds);
//				shopProductOld.setSummaryImages(product.getDetailImages());

//				String sizeTableImage = product.getSizeTableImage();
//				String detailImages = product.getDetailImages();

//				String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
//				shopProductOld.setRemark(remark);

                long time = System.currentTimeMillis();
                if (shopProductOld.getStock() > 0) {
                    shopProductOld.setStockTime(time);
                }

                shopProductOld.setTabType(0);
                shopProductOld.setSoldOut(1);
                shopProductOld.setStatus(0);
                shopProductOld.setCreateTime(time);
                shopProductOld.setUpdateTime(time);
                shopProductOld.setGroundTime(time);
                int record = shopProductMapper.insert(shopProductOld);
                logger.info("1-insert:" + record);
                // logger.error("updateTabTypeAfterPaySuccess-insert"+i+":"+record);
                if (record != 1) {
                    throw new RuntimeException("商品上传失败:productId---" + shopProductOld.getProductId());
                }
                shopProductLog.setType(1);
            }

            shopProductLog.setContent(JSON.toJSONString(shopProductOld));
            int logResult = shopProductLogMapper.insertAllColumn(shopProductLog);
            // logger.error("updateTabTypeAfterPaySuccess-log"+i+":"+logResult);
            if (logResult != 1) {
                throw new RuntimeException("商品改零售价记录失败:shopProductId---" + shopProductOld.getId());
            }

            productIds.add(productId);
        }

        // if(client!=null){
        // String value = client.getPlatform().getValue();
        // if("pc".equals(value)){
        // shopProductLog.setPlatform(0);
        // }else if("android".equals(value)){
        // shopProductLog.setPlatform(1);
        // }else if("iphone".equals(value)){
        // shopProductLog.setPlatform(2);
        // }
        // }
        //
        // shopProductLog.setVersion(client.getVersion());
        // //shopProductLog.setNet(2);
    }

    /**
     * 获取商家商品
     *
     * @param productId
     * @param storeId
     * @return
     */
    private ShopProduct getShopProduct(long productId, long storeId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>();
        wrapper.eq("product_id", productId);
        wrapper.eq("store_id", storeId);
        wrapper.eq("status", 0);
        List<ShopProduct> list = shopProductMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

//		ShopProduct entity = new ShopProduct();
//		entity.setProductId(productId);
//		entity.setStoreId(storeId);
//		entity.setStatus(0);
//		return shopProductMapper.selectOne(entity);

    }

    /**
     * 修改商品商家自定义描述
     *
     * @param shopOwnDetail
     * @param name          商品标题
     * @param shopProductId
     */
    public void updateShopOwnDetail(String shopOwnDetail, String name, long shopProductId) {
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setShopOwnDetail(shopOwnDetail);
        if (StringUtils.isNotEmpty(name)) {
            shopProduct.setName(name);
        }
        shopProductMapper.updateById(shopProduct);
    }

    /**
     * 获取商家商品
     *
     * @param shopProductId
     * @param
     */
    public ShopProduct selectById(long shopProductId) {
        return getShopProductInfoNoStock4App(shopProductId);
    }

    /**
     * 增加收藏数量
     *
     * @param
     */
    public void increaseFavoriteCount(Long shopProductId) {
        shopProductMapper.increaseFavoriteCount(shopProductId);
    }

    /**
     * 减少收藏数量
     *
     * @param shopProductId
     */
    public void reduceFavoriteCount(Long shopProductId) {
        ShopProduct shopProduct = selectById(shopProductId);
        long wantMemberCount = shopProduct.getWantMemberCount();
        if (wantMemberCount > 0) {
            shopProductMapper.reduceFavoriteCount(shopProductId);
        } else {
            logger.info("请排查问题:该商品收藏数量不大于0不能减1，shopProductId：" + shopProductId + ",当前数量是wantMemberCount:" + wantMemberCount);
        }
    }

    /**
     * 增加浏览数量
     */
    public void increaseShowCount(long shopProductId) {
        shopProductMapper.increaseShowCount(shopProductId);
    }

//===================================================================================================================================================================

    /**
     * 2.2版本之前的商品列表，从2.2版本之后不再使用
     * tabType ’板块 ：1:在售商品,2:已下架,0:草稿 sortType 排序方式 ：0上架时间、1想要、浏览量
     *
     * @param tabType
     * @param sortType
     * @param storeId
     * @return
     */
    private Wrapper<ShopProduct> getShopProductListWrapper(int tabType, int sortType, long storeId, String keyWords) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId);
        // 拼接条件
        if (tabType == 2) {// 已下架
            wrapper.eq("sold_out", 2);
        } else if (tabType == 0 || tabType == 1) {// 上架的店主精选和买手推荐
            wrapper.eq("tab_type", tabType).eq("sold_out", 1);
        } else {
            logger.info("获取商家商品列表参数错误,tabType：" + tabType + ",sortType：" + sortType);
        }
        //有关键字搜索
        if (keyWords != "" && keyWords != null) {
//			wrapper.like("name", keyWords);
            wrapper.and(" ((name LIKE '%" + keyWords + "%') or (product_id in (SELECT id FROM yjj_Product WHERE NAME LIKE '%" + keyWords + "%')))");
        }
        if (tabType == 2) {// 上架时间排序
            wrapper.orderBy(" top_time desc, update_time desc ");
        } else if (sortType == 0) {// 上架时间排序
            wrapper.orderBy(" top_time desc, ground_time desc ");
        } else if (sortType == 1) {// 想要排序
            // wrapper.orderBy("update_time",false);
            wrapper.orderBy(" top_time desc, want_member_count desc, ground_time desc ");
        } else if (sortType == 2) {// 浏览量
            // wrapper.orderBy("update_time",false);
            wrapper.orderBy(" top_time desc, show_count desc, ground_time desc");
        } else {
            logger.info("获取商家商品列表参数错误,tabType：" + tabType + ",sortType：" + sortType);
        }
        return wrapper;
    }

    /**
     * 获取平台商品数量
     *
     * @param storeId
     * @return
     */
    public int getPlatformProductCount(long storeId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("sold_out", ShopProduct.sold_out_up)
                .eq("tab_type", ShopProduct.tab_type_buyer_recommend).eq("own", ShopProduct.platform_product);
        return shopProductMapper.selectCount(wrapper);
    }

    /**
     * 获取自有商品
     *
     * @param storeId
     * @return
     */
    public int getOwnProductCount(long storeId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("own", ShopProduct.own_product).eq("sold_out", ShopProduct.sold_out_up);
        return shopProductMapper.selectCount(wrapper);
    }

    /**
     * 获取商品
     *
     * @param shopProductId
     * @return
     */
    public ShopProduct getShopProductById(long shopProductId) {
        return getShopProductInfoNoStock(shopProductId);
    }

    /**
     * 获取商家在售商品数量
     *
     * @param storeId
     * @return
     */
    public int getZaiShouShangPinCount(long storeId) {
        Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("store_id", storeId).eq("sold_out", ShopProduct.sold_out_up);
        return shopProductMapper.selectCount(wrapper);
    }


    /**
     * 获取商家商品状态信息
     * 商家商品状态:0已上架、1已下架、2已删除
     */
    public String getShopProductState(long shopProductId) {
        ShopProduct shopProduct = getShopProductInfoNoStock(shopProductId);
//		logger.info("获取商家商品状态信息shopProduct:"+JSON.toJSONString(shopProduct));
        int status = shopProduct.getStatus();//状态:-1：删除，0：正常
        int soldOut = shopProduct.getSoldOut(); //上架状态：0草稿，1上架， 2下架
//			商家商品状态:0已上架、1已下架、2已删除
        String shopProductState = "";
        if (status == -1) {
            shopProductState = "2";
        } else if (soldOut == ShopProduct.sold_out_up) {
            shopProductState = "0";
        } else {
            shopProductState = "1";
        }
        return shopProductState;
    }

    /**
     * 商家同步上传平台的上架商品
     */
    public void synchronousUpdateProduct(StoreBusiness storeBusiness, double rate, int synchronousButtonStatus) {
        long storeId = storeBusiness.getId();
        //判断同步按钮是否开启
        if (synchronousButtonStatus == 0) {
            logger.info("storeId:" + storeId + ",同步按钮关闭，不用同步上新！");
            throw new RuntimeException("同步按钮关闭，不用同步上新！");
        }

        //插入条数

        //需要插入的商品总数
//		int productSum = productSKUMapper.getSaleStartProductNums(storeId);
        Long startTime = System.currentTimeMillis();
        //1、获取需要同步的平台商品Id集合，不包含已经同步的商品
        List<Long> productIdList = productSKUMapper.getAllSynchronousUpdateProductIds(storeId);
        //2、将平台商品批量同步到商家商品中
        insertShopProductsBatch(productIdList, rate, storeId);

        long endTime = System.currentTimeMillis();
        logger.info("同步上新完成,耗时:" + (endTime - startTime) + "毫秒，同步商品数量：" + productIdList.size() + ",storeId：" + storeId);

        //商品同步后发送系统通知

        //type//跳转类型0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
//		List<String> CIDList = new ArrayList<String>();
//		CIDList.add(storeBusiness.getUserCID());
        //			GetuiUtil.pushGeTui(CIDList, "同步商品库提醒", "已完成"+batchSize+"30款商品同步。", "", "","0",System.currentTimeMillis()+"");
        ShopNotification shopNotification = new ShopNotification();
        shopNotification.setTitle("同步商品库提醒");
        shopNotification.setAbstracts("已完成" + productIdList.size() + "款商品同步。");
        shopNotification.setPushStatus(1);
        shopNotification.setImage("");
        shopNotification.setType(0);
        shopNotification.setStatus(0);
        shopNotification.setCreateTime(System.currentTimeMillis());
        shopNotification.setPushTime(shopNotification.getCreateTime());
        shopNotification.setUpdateTime(shopNotification.getCreateTime());
        notificationMapper.addNotification(shopNotification);
        logger.info("发送同步上新系统通知完成，title：" + shopNotification.getTitle() + ",Abstracts:" + shopNotification.getAbstracts());

    }


    private void insertShopProductsBatch(List<Long> productIdList, double rate, long storeId) {
        //在yjj_product表中获取对应的商品，并封装再重新插入在shop_product表中
        List<Product> productList = productMapper.getProductsByIds(productIdList);
        List<ShopProduct> shopProductList = new ArrayList<ShopProduct>();
        for (Product product : productList) {
            shopProductList.add(getShopProductFromProduct(product, rate, storeId));
        }
        //批量插入添加进shop_product表
        int i = shopProductMapper.insertShopProductsBatch(shopProductList);
        if (i == -1) {
            logger.info("同步上新失败！请排查问题！！");
            throw new RuntimeException("同步上新失败！请排查问题！！");
        }
    }

    private ShopProduct getShopProductFromProduct(Product product, double rate, long storeId) {
        long time = System.currentTimeMillis();
        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setProductId(product.getId());
        //状态:-1：删除，0：正常
        shopProduct.setStatus(0);
        //0：已下单，1：未下单
        shopProduct.setTabType(1);
        //0：草稿，1：上架， 2：下架
        shopProduct.setSoldOut(1);
        //1是自有商品，0平台商品
        shopProduct.setOwn(0);
        shopProduct.setUpdateTime(time);
        shopProduct.setCreateTime(time);
        shopProduct.setGroundTime(time);
        shopProduct.setStoreId(storeId);
        //计算倍率价格
        if (rate > 0) {
//			BigDecimal wholeSaleCash = new BigDecimal(product.getWholeSaleCash());
            BigDecimal maxLadderPrice = new BigDecimal(product.getMaxLadderPrice());
            BigDecimal bigRate = new BigDecimal(rate);
            maxLadderPrice = maxLadderPrice.multiply(bigRate);
            shopProduct.setPrice(maxLadderPrice.doubleValue());
        }

        return shopProduct;

    }

    /**
     * 删除视频
     *
     * @param shopProductId
     * @param fileId
     * @param priority
     */
    public void deleteVideo(long shopProductId, long fileId, int priority) {
        VideoSignatureUtil sign = new VideoSignatureUtil();
        sign.m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
        sign.m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("url", sign.getDeleteSignatureG(fileId, priority));
        String result = HttpsClientUtil.httpsRequest(sign.getDeleteSignatureG(fileId, priority), "GET", null);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String codeDesc = (String) jsonObject.get("codeDesc");
        if (!"Success".equals(codeDesc)) {
            logger.error("删除视频失败:" + result);
            throw new RuntimeException("删除视频失败");
        }

        ShopProduct shopProduct = new ShopProduct();
        shopProduct.setId(shopProductId);
        shopProduct.setVideoDisplayFileId(0L);
        shopProduct.setVideoDisplayImage("");
        shopProduct.setVideoDisplayUrl("");
        shopProductMapper.updateById(shopProduct);
    }


    /**
     * 随机获取门店的一件上架商品,并且库存>0
     *
     * @param query 请求参数封装
     * @return com.jiuyuan.entity.newentity.ShopProduct
     * @author Charlie(唐静)
     * @date 2018/7/3 15:58
     */
    public ShopProduct randomProduct(ShopProduct query) {
        return shopProductMapper.randomProduct(query);
    }


    public Page<Map<String, Object>> getShopPage(Page page, Long storeId) {


        EntityWrapper warp = new EntityWrapper();
        warp.eq("store_id", storeId);
        warp.eq("status", 0);
        warp.eq("sold_out", 1);
        warp.orderBy("top_time", false);
        warp.orderBy("ground_time", false);
        List<ShopProduct> list = shopProductMapper.selectPage(page, warp);
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(action -> {
            Map<String, Object> map = BizUtil.bean2Map(action);
            maps.add(map);
        });
        page.setRecords(maps);
        return page;

    }

    public ShopProduct findProductForFavoriteById(long shopProductId) {
       return shopProductMapper.findProductForFavoriteById(shopProductId);
    }


    /*  *//**
     * 获取商品,活动商品置顶
     *
     * @param page    page
     * @param storeId storeId
     * @return com.baomidou.mybatisplus.plugins.Page
     * @author Charlie
     * @date 2018/7/13 18:47
     *//*
    public Page<Map<String, Object>> getShopProductAndActivityPage(Page page, Long storeId) {
        SecondBuyActivity secondBuyActivity = myStoreActivityService.getStoreCurrentSecondBuyActivity (storeId);
        TeamBuyActivity teamBuyActivity = myStoreActivityService.getStoreCurrentTeamBuyActivity (storeId);
        Long secondBuyShopProductId = secondBuyActivity == null ? 0 : (secondBuyActivity.getShopProductId () == null) ? 0 : secondBuyActivity.getShopProductId ();
        Long teamBuyShopProductId = teamBuyActivity == null ? 0 : (teamBuyActivity.getShopProductId () == null) ? 0 : teamBuyActivity.getShopProductId ();

        int status = 0;
        int soldOut = 1;
        List<Map<String, Object>> result = shopProductMapper.getShopProductAndActivityPage (page, storeId, status, soldOut, secondBuyShopProductId, teamBuyShopProductId);
        page.setRecords (result);
        return page;

    }*/

}