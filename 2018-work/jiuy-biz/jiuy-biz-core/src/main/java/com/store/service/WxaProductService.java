package com.store.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enums.PropertiesEnums;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.SecondBuyActivity;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.service.common.IMyStoreActivityService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpClientUtils;
import com.jiuyuan.util.JiuyMultipartFile;
import com.jiuyuan.util.PriceUtil;
import com.store.dao.mapper.ProductSKUMapper;
import com.store.dao.mapper.PropertyValueMapper;
import com.store.dao.mapper.ShopPropertyValueMapper;
import com.store.dao.mapper.WxaMemberFavoriteMapper;
import com.store.entity.ShopMemberFavorite;
import com.store.entity.ShopPropertyValue;
import com.util.ConstantId;
import com.util.PropertiesUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.util.file.OSSFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author QiuYuefan
 */

@Service
public class WxaProductService {
    private static final Log logger = LogFactory.get();

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Autowired
    private ShopPropertyValueMapper shopPropertyValueMapper;

    @Autowired
    private WxaMemberFavoriteMapper wxaMemberFavoriteMapper;

    @Autowired
    private ProductServiceShop productService;

    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;

    @Autowired
    private ProductSKUMapper productSKUMapper;

    @Autowired
    private PropertyValueMapper propertyValueMapper;

    @Autowired
    private IMyStoreActivityService myStoreActivityService;

    @Autowired
    private MemcachedService memcachedService;


    /**
     * 封装平台商品数据进ShopProduct
     *
     * @param shopProduct
     */
    private void getPlatformShopProductInfo(ShopProduct shopProduct) {
        long productId = shopProduct.getProductId();
        Product product = productService.getProduct(productId);

        shopProduct.setXprice(product.getMinLadderPrice());
        shopProduct.setMarketPrice((double) product.getMarketPrice());
        shopProduct.setClothesNumber(product.getClothesNumber());
        shopProduct.setVideoUrl(product.getVideoUrl());

        //最小库存
       /* int stock = 0;
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
        shopProduct.setStock((long) stock);*/
        shopProduct.setStock(1L);
        if (shopProduct.getStock() > 0) {
            shopProduct.setStockTime(System.currentTimeMillis());
        }

        shopProduct.setSummaryImages(product.getDetailImages());

        String sizeTableImage = product.getSizeTableImage();
        String detailImages = product.getSummaryImages();
        if (JSON.parseArray(detailImages).size() <= 0) {
            detailImages = product.getDetailImages();
        }
        String remark = "{\"SizeTableImage\":" + sizeTableImage + ",\"DetailImages\":" + detailImages + "}";
        shopProduct.setRemark(remark);
    }

    /* (non-Javadoc)
     * @see com.store.service.IWxaProductService#getShopProductInfo(long)
     */
    public ShopProduct getShopProductInfo(long shopProductId) {
        ShopProduct shopProduct = shopProductMapper.selectById(shopProductId);
        if (shopProduct.getOwn() == 0) {
            getPlatformShopProductInfo(shopProduct);
        }
        return shopProduct;
    }


    public static String weixinServiceUrl = WeixinPayConfig.getWeiXinServerUrl();
    public static String getProductQrcodeUrl = "/code/getProductQrcodeUrl";
    private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;
    @Autowired
    private OSSFileUtil ossFileUtil;

    @Autowired
    private StoreWxaService storeWxaService;

    /**
     * 1
     * 生成小程序二维码
     *
     * @param shopProduct
     */
    private String createWxaQrUrl(ShopProduct shopProduct) {
        //这个storeId在后面可能会被替换掉,用来查询小程序appId
        long storeId = shopProduct.getStoreId();
        long shopProductId = shopProduct.getId();
        try {
            Integer storeShopType = shopProductMapper.getByStoreId(storeId);
            //s是店中店
            boolean isShareStore = storeShopType == 1;
            if (isShareStore) {
                //storeId = ConstantId.skipShopStoreId();
                //11878
                storeId =Long.parseLong(PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(),PropertiesEnums.CONSTANTS_SHOP_IN_ID.getKey()));
            }
            String wxaqrcodeUrl = isShareStore ? shopProduct.getProductNewImg() : shopProduct.getWxaqrcodeUrl();
            logger.info("是否是店中店 isShareStore={},wxaqrcodeUrl={},storeId={}", isShareStore, wxaqrcodeUrl, storeId);
            if (StringUtils.isEmpty(wxaqrcodeUrl)) {
                StoreWxa storeWxa = storeWxaService.getStoreWxaByStoreId(storeId + "");
                String productQrcodeUrl = "";
                if (storeWxa == null) {
                    logger.info("该商家没有开通小程序storeId：" + storeId + ",获取商品小程序二维码失败");
                } else {
                    String url = weixinServiceUrl + "/third/findAccessTokenByAppId";
                    Map<String, Object> param = new HashMap<>(2);
                    param.put("appId", storeWxa.getAppId());
                    String token = HttpClientUtils.get(url, param);
                    logger.info("获取token = {}", token);
                    Map<String, String> paramMap = new HashMap(6);
                    String fileNameLink;
                    if (isShareStore) {
                        Long productBelongStoreId = shopProduct.getStoreId();
                        //分享进店,兼容
                        paramMap.put("scene", "pid" + shopProductId + "&sid" + productBelongStoreId);
                        fileNameLink = "_pid_" + shopProductId + "_sid_" + productBelongStoreId;
                    } else {
                        paramMap.put("scene", shopProductId + "");
                        fileNameLink = "_pid_" + shopProductId;
                    }
                    //线上的为"pages/component/detail/detail"
                    //线下的为""page/component/detail/detail""
                    paramMap.put("page", "pages/component/detail/detail");
                    paramMap.put("width", "430");


                    String fileName = System.getProperty("java.io.tmpdir") + "/pic/" + storeWxa.getAppId() + fileNameLink + "_pages_component_detail_detail.jpg";
                    String params = JSONObject.toJSONString(paramMap);
                    logger.info("文件名 fileName={},params={}", fileName, params);
                    File file =
                            HttpClientUtils.postInputStreamToFile(String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s",
                                    token),
                                    params, fileName);

                    if (file == null) {
                        logger.error("生小程序二维码={}失败", shopProductId);
                        return null;
                    }

                    logger.info("创建文件");
                    MultipartFile partFile = new JiuyMultipartFile(file);
                    try {
                        file.createNewFile();
                        logger.info("上传文件");
                        productQrcodeUrl = ossFileUtil.uploadFile(DEFAULT_BASEPATH_NAME, partFile);
                        logger.info("生成二维码成功!!! path={}", productQrcodeUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.info("生成二维码失败" + e.getMessage());
                        return null;
                    }
                    file.delete();
                }

                logger.info("生成小程序二维码productQrcodeUrl成功productQrcodeUrl：" + productQrcodeUrl + "storeId:" + storeId + ",wxaqrcodeUrl:" + wxaqrcodeUrl);
                if (StringUtils.isNotEmpty(productQrcodeUrl)) {
                    ShopProduct updShopProduct = new ShopProduct();
                    updShopProduct.setId(shopProductId);
                    if (isShareStore) {
                        updShopProduct.setProductNewImg(productQrcodeUrl);
                    } else {
                        updShopProduct.setWxaqrcodeUrl(productQrcodeUrl);
                    }
                    shopProductMapper.updateById(updShopProduct);
                    logger.info("保存小程序二维码productQrcodeUrl到数据库成功productQrcodeUrl：" + productQrcodeUrl + "storeId:" + storeId + ",wxaqrcodeUrl:" + wxaqrcodeUrl);
                } else {
                    logger.info("生成小程序二维码productQrcodeUrl为空productQrcodeUrl：" + productQrcodeUrl + "storeId:" + storeId + ",wxaqrcodeUrl:" + wxaqrcodeUrl);
                }

                return productQrcodeUrl;
            } else {
                logger.info("商品小程序二维码地址wxaqrcodeUrl:" + wxaqrcodeUrl);
                return wxaqrcodeUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("生成小程序二维码失败，请尽快排除问题，shopProductId:" + shopProductId);
        }

        return "";
    }



    /**
     * 获取商品信息详情
     *
     * @param shopProductId shopProductId
     * @param memberId      memberId
     * @param storeId       storeId
     * @return java.util.Map
     * update by Charlie
     * @date 2018/8/9 17:21
     */
    public Map<String, Object> getProductItem(Long shopProductId, Long memberId, long storeId) {
        if (memberId == null || memberId < 1) {
            throw new RuntimeException("会员信息有误,请确认");
        }
        if (shopProductId == null || shopProductId < 1) {
            throw new RuntimeException("商品Id必填");
        }

        ShopProduct shopProduct = getShopProductInfo(shopProductId);
        if (shopProduct == null) {
            throw new RuntimeException("没有该商品");
        }

        Long productId = shopProduct.getProductId();

        Map<String, Object> data = new HashMap<>(10);
        Map<String, Object> product = new HashMap<>(10);
        //商家商品Id
        product.put("id", shopProduct.getId() + "");
        //商家商品名称
        product.put("name", shopProduct.getName());
        //商品自定义表示JSON
        product.put("shopOwnDetail", sortShopOwnDetail(shopProduct.getShopOwnDetail()));
        //商品ID
        product.put("productId", productId);
        //商品零售价
        product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()));
        //该字段已经废弃，确认后可删除
        product.put("xprice", PriceUtil.getPriceString(shopProduct.getXprice()));
        //是否有现货：1:商品有现货	0:商品没现货,作废写死
        product.put("isStock", "1");
        //是否推荐：1:商品推荐	0:商品不推荐
        product.put("isTop", shopProduct.getTopTime() == 0?"0":"1");

        //商品状态：1：上架	2：下架
        product.put("soldOut", shopProduct.getSoldOut() + "");
        //是否下单：0：已下单，1：未下单(0：店主精选	1：买手推荐)
        product.put("tabType", shopProduct.getTabType() + "");
        //商品尺码
        product.put("sizes", getPropertyValue(shopProduct.getSizeIds()));
        //商品颜色
        product.put("colors", getPropertyValue(shopProduct.getColorIds()));


        String summaryImages = shopProduct.getSummaryImages();
        JSONArray jsonArrayImages = JSONArray.parseArray(summaryImages);
        List<String> summaryImagesList = new ArrayList<>();
        for (int i = 0; i < jsonArrayImages.size(); i++) {
            String image = jsonArrayImages.getString(i);
            if (StringUtils.isNotBlank(image)) {
                summaryImagesList.add(image);
            }
            if (summaryImagesList.size() >= 4) {
                break;
            }
        }
        //橱窗图（最多4张）
        product.put("summaryImages", summaryImagesList);

        String remark = shopProduct.getRemark();
        if (StringUtils.isNotBlank(remark)) {
            JSONObject jsonObject = JSONObject.parseObject(remark);
            product.put("detailImages", jsonObject.getJSONArray("DetailImages"));
            product.put("sizeTableImage", jsonObject.getJSONArray("SizeTableImage"));
        }
        //商品橱窗视频url
        product.put("videoDisplayUrl", shopProduct.getVideoDisplayUrl());
        //商品橱窗视频图片url
        product.put("videoDisplayImage", shopProduct.getVideoDisplayImage());


        //为商品生成小程序二维码图片
        String wxaqrcodeUrl = createWxaQrUrl(shopProduct);
        if (StringUtils.isBlank(wxaqrcodeUrl)) {
            wxaqrcodeUrl = "";
        }
        //商品小程序二维码路径
        product.put("wxaqrcodeUrl", wxaqrcodeUrl);

//        Wrapper<ShopMemberFavorite> wrapper = new EntityWrapper<ShopMemberFavorite>().eq("related_id", shopProductId).eq("member_id", memberId).eq("type", 0)
//                .eq("store_id", storeId);
//        List<ShopMemberFavorite> wxaMemberFavoritenewListOld = wxaMemberFavoriteMapper.selectList(wrapper);
//        if (wxaMemberFavoritenewListOld.size() != 1) {
//            data.put("isFavorite", -1);
//        } else {
//            ShopMemberFavorite wxaMemberFavorite = wxaMemberFavoritenewListOld.get(0);
//            //收藏状态：	0:收藏	-1:未收藏
//            if (wxaMemberFavorite == null || wxaMemberFavorite.getStatus() == -1) {
//                data.put("isFavorite", -1);
//            } else {
//                data.put("isFavorite", 0);
//            }
//        }
        List<Integer> wxaMemberFavoritenewList = shopProductMapper.listFavoriteStatus(shopProductId, memberId, 0, storeId);
        if (wxaMemberFavoritenewList.size() != 1) {
            data.put("isFavorite", -1);
        } else {
            Integer wxaMemberFavorite = wxaMemberFavoritenewList.get(0);
            //收藏状态：	0:收藏	-1:未收藏
            if (wxaMemberFavorite == null || wxaMemberFavorite.equals(-1)) {
                data.put("isFavorite", -1);
            } else {
                data.put("isFavorite", 0);
            }
        }



        //获取商品视频
        Product productObj = null;
        if (productId != null && productId > 0L) {
            productObj = productService.getProduct(productId);
        }
        if (productObj != null) {
            data.put("videoUrl", productObj.getVideoUrl());
        } else {
            data.put("videoUrl", "");
        }

        //如果是平台商品,增加sku信息
        if (shopProduct.getOwn() == 0) {
            //获取门店发布订单的sku
//            List<ProductSKU> productSKUList = productSKUMapper.getProductSKUsByProductId(productId);
//            Map<String, Object> productSKUInfos = getProductSKUInfos(productSKUList);
            List<ProductSkuNew> productSKUList = productSkuNewMapper.getSkuColorSizeAndCountByProductId(productId, System.currentTimeMillis());
            Map<String, Object> productSKUInfos = buildSkuInfo(productSKUList);
            data.put("skuInfos", productSKUInfos);
        } else if (shopProduct.getOwn() == 2) {
            //APP 发布供应商同款 自营

            //获取门店发布订单的sku
        /*    Wrapper<ProductSkuNew> wrapperWxa = new EntityWrapper<>();
            wrapperWxa.eq("wxa_product_id", shopProduct.getId());
            //大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
            wrapperWxa.ge("Status", 0);
            List<ProductSkuNew> skuWxaList = productSkuNewMapper.selectList(wrapperWxa);*/
            List<ProductSkuNew> skuWxaList = productSkuNewMapper.listSkuColorSizeAndCountByWxaProductId(shopProduct.getId());


            /*Wrapper<ProductSkuNew> wrapperApp = new EntityWrapper<>();
            wrapperApp.eq("ProductId", productId);
            wrapperApp.ge("Status", 0);
            List<ProductSkuNew> skuAppList = productSkuNewMapper.selectList(wrapperApp);*/
            List<ProductSkuNew> skuAppList = productSkuNewMapper.listSkuColorSizeAndCountByProductId(productId);

            Map<String, Object> productSKUInfoWxa = getProductSKUInfoWxa(skuWxaList, skuAppList);
            data.put("skuInfos", productSKUInfoWxa);
        } else {

            //app 自定义发布商品
//            Wrapper<ProductSkuNew> wrapperWxa = new EntityWrapper<>();
//            wrapperWxa.eq("wxa_product_id", shopProduct.getId());
//            大于等于-1  状态:-3废弃，-2停用，-1下架，0正常，1定时上架
//            wrapperWxa.ge("Status", 0);
//            List<ProductSkuNew> skuWxaList = productSkuNewMapper.selectList(wrapperWxa);
            List<ProductSkuNew> skuWxaList = productSkuNewMapper.listSkuColorSizeAndCountByWxaProductId(shopProduct.getId());

            Map<String, Object> productSKUInfoWxa = getProductSKUInfoWxa(skuWxaList);
            data.put("skuInfos", productSKUInfoWxa);
        }

        Map<String, Object> productSKUInfos = (Map<String, Object>) data.get("skuInfos");
        List<Map<String, String>> skuList = (List<Map<String, String>>) productSKUInfos.get("skuList");
        boolean isStock = false;

        for (Map<String, String> action : skuList) {
            int count = Integer.parseInt(action.get("count"));
            if (count > 0) {
                isStock = true;
                break;
            }
        }

        data.put("isInventoryEnough", isStock);

        data.put("product", product);

        //活动商品相关逻辑
        //商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
//        int intoActivity = myStoreActivityService.getShopProductActivityState(shopProduct.getId(), storeId);
        Map<String, String> teamBuy = new HashMap<>(10);
        Map<String, String> secondBuy = new HashMap<>(10);

        int intoActivity = 0;
        TeamBuyActivity teamBuyActivity = myStoreActivityService.getCurrentTeamBuyActivity(shopProductId,storeId);
        if (teamBuyActivity != null) {
            intoActivity = 1;
            //参与团购活动
            logger.info("获取商品列表,商品有活动 teamId[{}]", teamBuyActivity.getId());
            //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
            long activityId = teamBuyActivity.getId();
            //活动状态：1待开始，2进行中，3已结束
            int activityStatus = teamBuyActivity.haveActivityStatusInt();
            teamBuy.put("activityStatus", String.valueOf(activityStatus));
            //成团人数
            int userCount = teamBuyActivity.getUserCount();
            //活动商品数量
//                int activityProductCount = teamBuyActivity.getActivityProductCount();
            //参与人数
            int activityMemberCount = teamBuyActivity.getActivityMemberCount();
            int differMemberCount = userCount - activityMemberCount;
            differMemberCount = differMemberCount < 0 ? 0 : differMemberCount;
            //成团条件
            teamBuy.put("conditionType", String.valueOf(teamBuyActivity.getConditionType()));
            //成团人数
            teamBuy.put("userCount", userCount + "");
            //成团件数
            teamBuy.put("meetProductCount", String.valueOf(teamBuyActivity.getMeetProductCount()));
            //还差人数
            teamBuy.put("differMemberCount", String.valueOf(differMemberCount));
            //还差件数
            int differProductCount = 0;
            if (teamBuyActivity.getConditionType() == 2) {
                differProductCount = teamBuyActivity.getMeetProductCount() - teamBuyActivity.getOrderedProductCount();
                differProductCount = differProductCount < 0 ? 0 : differProductCount;
            }
            teamBuy.put("differProductCount", differProductCount + "");
            //剩余库存(先不在redis拿了, 现在缓存与库是保持一致的)
            teamBuy.put("surplusProductCount", String.valueOf(teamBuyActivity.getActivityProductCount() - teamBuyActivity.getOrderedProductCount()));

            //活动价格
            teamBuy.put("activityPrice", String.valueOf(teamBuyActivity.getActivityPrice()));
            if (activityStatus == 1) {
                //1待开始
                long activityStartTime = teamBuyActivity.getActivityStartTime();
                long surplusStartTime = activityStartTime - System.currentTimeMillis();
                if (surplusStartTime < 0) {
                    surplusStartTime = 0;
                }
                //活动开始倒计时剩余毫秒数
                teamBuy.put("surplusStartTime", String.valueOf(surplusStartTime));
            } else if (activityStatus == 2) {
                //2进行中
                long activityEndTime = teamBuyActivity.getActivityEndTime();
                long surplusEndTime = activityEndTime - System.currentTimeMillis();
                if (surplusEndTime < 0) {
                    surplusEndTime = 0;
                }
                //活动结束倒计时剩余毫秒数
                teamBuy.put("surplusEndTime", String.valueOf(surplusEndTime));
            }
            //用于controller将库存放缓存中
            data.put("teamId", activityId);
        } else {
            SecondBuyActivity secondBuyActivity = myStoreActivityService.getCurrentSecondBuyActivity(shopProductId,storeId);
            if (secondBuyActivity != null) {
                intoActivity = 2;
                //2表示参与秒杀活动
                long activityId = secondBuyActivity.getId();
                int activityStatus = secondBuyActivity.haveActivityStatusInt();
                //活动状态：1待开始，2进行中，3已结束
                secondBuy.put("activityStatus", String.valueOf(activityStatus));

                //活动商品数量
                int activityProductCount = secondBuyActivity.getActivityProductCount();
                secondBuy.put("activityProductCount", String.valueOf(activityProductCount));
                //还剩活动商品件数
                int surplusProductCount = activityProductCount - secondBuyActivity.getOrderedProductCount();
                if (surplusProductCount < 0) {
                    surplusProductCount = 0;
                }
                secondBuy.put("surplusProductCount", String.valueOf(surplusProductCount));
                //活动价格
                secondBuy.put("activityPrice", String.valueOf(secondBuyActivity.getActivityPrice()));
                if (activityStatus == 1) {
                    long activityStartTime = secondBuyActivity.getActivityStartTime();
                    long surplusStartTime = activityStartTime - System.currentTimeMillis();
                    if (surplusStartTime < 0) {
                        surplusStartTime = 0;
                    }
                    //活动开始倒计时剩余毫秒数
                    secondBuy.put("surplusStartTime", String.valueOf(surplusStartTime));
                } else if (activityStatus == 2) {
                    long activityEndTime = secondBuyActivity.getActivityEndTime();
                    long surplusEndTime = activityEndTime - System.currentTimeMillis();
                    if (surplusEndTime < 0) {
                        surplusEndTime = 0;
                    }
                    //活动结束倒计时剩余毫秒数
                    secondBuy.put("surplusEndTime", String.valueOf(surplusEndTime));
                }

                //用于controller将库存放缓存中
                data.put("secondId", activityId);
            }
        }
        data.put("intoActivity", intoActivity);//商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
        data.put("teamBuyActivity", teamBuy);//团购活动信息
        data.put("secondBuyActivity", secondBuy);//秒杀活动信息
        return data;
    }



    /**
     * 老接口重构
     * @see {com.store.service.WxaProductService#getProductSKUInfos(java.util.List)}
     * @param productSKUList productSKUList
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/2 19:49
     */
    private Map<String, Object> buildSkuInfo(List<ProductSkuNew> productSKUList) {
        Map<String, Object> result = new HashMap<>(4);

        List<Map<String, String>> skuList;
        List<Map<String, String>> colorList;
        List<Map<String, String>> sizeList;
        if (productSKUList.isEmpty()) {
            skuList = Collections.emptyList();
            colorList = Collections.emptyList();
            sizeList = Collections.emptyList();
        }
        else {
            int size = productSKUList.size();
            skuList = new ArrayList<>(size);
            colorList = new ArrayList<>(size);
            sizeList = new ArrayList<>(size);

            //临时存放,避免颜色尺码列表反复放入
            Set<String> alreadyInColor = new HashSet<>(size);
            Set<String> alreadyInSize = new HashSet<>(size);

            for (ProductSkuNew sku : productSKUList) {
                //skuList
                String sizeId = sku.getSizeId()+"";
                String colorId = sku.getColorId()+"";

                Map<String, String> skuMap = new HashMap(4);
                skuMap.put("sizeId", sizeId);
                skuMap.put("colorId", colorId);
                skuMap.put("count", sku.getRemainCount()+"");
                skuMap.put("skuId", sku.getId()+"");
                skuList.add(skuMap);

                //sizeList
                if (! alreadyInSize.contains(sizeId)) {
                    Map<String, String> sizeMap = new HashMap(2);
                    sizeMap.put("sizeId", sizeId);
                    sizeMap.put("value", sku.getSizeName());
                    sizeList.add(sizeMap);
                }

                //colorList
                if (! alreadyInColor.contains(colorId)) {
                    Map<String, String> colorMap = new HashMap(2);
                    colorMap.put("colorId", colorId);
                    colorMap.put("value", sku.getColorName());
                    colorList.add(colorMap);
                }

                alreadyInColor.add(colorId);
                alreadyInSize.add(sizeId);
            }
        }
        result.put("colorList", colorList);
        result.put("sizeList", sizeList);
        result.put("skuList", skuList);
        return result;

    }


    /**
     * 将文本内容排序
     * <p>文本放前面,图片放后面</p>
     *
     * @param shopOwnDetail shopOwnDetail
     * @return java.lang.String
     * @author Charlie
     * @date 2018/7/16 10:00
     */
    private String sortShopOwnDetail(String shopOwnDetail) {
        if (BizUtil.isEmpty(shopOwnDetail)) {
            return shopOwnDetail;
        }

        List<Map<String, String>> details = new Gson().fromJson(shopOwnDetail,
                new TypeToken<List<Map<String, String>>>() {
        }.getType());
        List<Map<String, String>> afterSort = new ArrayList<>(details.size());

        Iterator<Map<String, String>> iterator = details.iterator();
        while (iterator.hasNext()) {
            Map<String, String> next = iterator.next();
            if (ObjectUtils.nullSafeEquals("1", next.get("type"))) {
                iterator.remove();
                afterSort.add(next);
            }
        }

        afterSort.addAll(details);
        return BizUtil.bean2json(afterSort);
    }


    /**
     * 封装商品的sku信息
     *
     * @param productSKUList
     * @return
     */
    private Map<String, Object> getProductSKUInfos(List<ProductSKU> productSKUList) {
        Map<String, Object> productSKUInfos = new HashMap<String, Object>();
        List<Map<String, String>> skuList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> colorList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> sizeList = new ArrayList<Map<String, String>>();
        List<Long> colorIdList = new ArrayList<Long>();
        List<Long> sizeIdList = new ArrayList<Long>();
        for (ProductSKU productSKU : productSKUList) {
            Map<String, String> sku = new HashMap<String, String>();
            Map<String, String> color = new HashMap<String, String>();
            Map<String, String> size = new HashMap<String, String>();
            String propertyIds = productSKU.getPropertyIds();
            if (StringUtils.isEmpty(propertyIds)) {
                continue;
            }
            String[] split = propertyIds.split(";");
            String[] colorArr = split[0].split(":");
            long colorId = Long.parseLong(colorArr[1]);
            String[] sizeArr = split[1].split(":");
            long sizeId = Long.parseLong(sizeArr[1]);
            if (!colorIdList.contains(colorId)) {
                String colorShopPropertyValue = propertyValueMapper.getPropertyValueById(colorId);
                color.put("colorId", colorId + "");
                color.put("value", colorShopPropertyValue);
                colorIdList.add(colorId);
                colorList.add(color);
            }

            if (!sizeIdList.contains(sizeId)) {
                String sizeShopPropertyValue = propertyValueMapper.getPropertyValueById(sizeId);
                size.put("sizeId", sizeId + "");
                size.put("value", sizeShopPropertyValue);
                sizeIdList.add(sizeId);
                sizeList.add(size);
            }

            sku.put("skuId", productSKU.getId() + "");
            sku.put("colorId", colorId + "");
            sku.put("sizeId", sizeId + "");
            sku.put("count", productSKU.getRemainCount() + "");
            skuList.add(sku);
        }
        productSKUInfos.put("colorList", colorList);
        productSKUInfos.put("sizeList", sizeList);
        productSKUInfos.put("skuList", skuList);
        return productSKUInfos;
    }

    /**
     * 描述 获取小程序订单的sku
     * * @param wxaSkuList
     *
     * @param appSkuList
     * @author hyq
     * @date 2018/9/4 14:59
     */
    private Map<String, Object> getProductSKUInfoWxa(List<ProductSkuNew> wxaSkuList, List<ProductSkuNew> appSkuList) {

        Map<String, Object> productSKUInfos = new HashMap<String, Object>();
        List<Map<String, String>> skuList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> colorList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> sizeList = new ArrayList<Map<String, String>>();

        //对供应商的sku进行分组
        Map<String, ProductSkuNew> productSkuNewMap = new HashMap<>();
        appSkuList.stream().forEach(appSku -> {
            String keyStr = appSku.getColorId() + "" + appSku.getSizeId();
            //只有一对一
            if (productSkuNewMap.get(keyStr) == null) {
                productSkuNewMap.put(keyStr, appSku);
            }

        });

        Map<Long, String> colorListOnly = new HashMap<>();
        Map<Long, String> sizeListOnly = new HashMap<Long, String>();

        wxaSkuList.stream().forEach(productSKU -> {

            Map<String, String> sku = new HashMap<String, String>();
            Map<String, String> color = new HashMap<String, String>();
            Map<String, String> size = new HashMap<String, String>();

            color.put("colorId", productSKU.getColorId() + "");
            color.put("value", productSKU.getColorName());
            color.put("selected", "false");
            if (colorListOnly.get(productSKU.getColorId()) == null) {
                colorListOnly.put(productSKU.getColorId(), "1");
                colorList.add(color);
            }

            size.put("sizeId", productSKU.getSizeId() + "");
            size.put("selected", "false");
            size.put("value", productSKU.getSizeName());
            if (sizeListOnly.get(productSKU.getSizeId()) == null) {
                sizeListOnly.put(productSKU.getSizeId(), "1");
                sizeList.add(size);
            }

            sku.put("skuId", productSKU.getId() + "");
            sku.put("colorId", productSKU.getColorId() + "");
            sku.put("sizeId", productSKU.getSizeId() + "");
            String keyStr = productSKU.getColorId() + "" + productSKU.getSizeId();
            if (productSkuNewMap.get(keyStr) != null) {
                int count = productSKU.getRemainCount() + productSkuNewMap.get(keyStr).getRemainCount();
                sku.put("count", count + "");
            } else {
                sku.put("count", productSKU.getRemainCount() + "");
            }
            skuList.add(sku);

        });

        productSKUInfos.put("colorList", colorList);
        productSKUInfos.put("sizeList", sizeList);
        productSKUInfos.put("skuList", skuList);
        return productSKUInfos;
    }

    /**
     * 描述 获取小程序订单的sku
     * @author hyq
     * @date 2018/9/4 14:59
     */
    private Map<String, Object> getProductSKUInfoWxa(List<ProductSkuNew> wxaSkuList) {

        Map<String, Object> productSKUInfos = new HashMap<String, Object>();
        List<Map<String, String>> skuList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> colorList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> sizeList = new ArrayList<Map<String, String>>();

        Map<Long, String> colorListOnly = new HashMap<Long, String>();
        Map<Long, String> sizeListOnly = new HashMap<Long, String>();

        wxaSkuList.stream().forEach(productSKU -> {

            Map<String, String> sku = new HashMap<String, String>();
            Map<String, String> color = new HashMap<String, String>();
            Map<String, String> size = new HashMap<String, String>();

            color.put("colorId", productSKU.getColorId() + "");
            color.put("value", productSKU.getColorName());
            color.put("selected", "false");
            if (colorListOnly.get(productSKU.getColorId()) == null) {
                colorListOnly.put(productSKU.getColorId(), "1");
                colorList.add(color);
            }

            size.put("sizeId", productSKU.getSizeId() + "");
            size.put("selected", "false");
            size.put("value", productSKU.getSizeName());
            if (sizeListOnly.get(productSKU.getSizeId()) == null) {
                sizeListOnly.put(productSKU.getSizeId(), "1");
                sizeList.add(size);
            }

            sku.put("skuId", productSKU.getId() + "");
            sku.put("colorId", productSKU.getColorId() + "");
            sku.put("sizeId", productSKU.getSizeId() + "");
            String keyStr = productSKU.getColorId() + "" + productSKU.getSizeId();
            sku.put("count", productSKU.getRemainCount() + "");

            skuList.add(sku);

        });
        productSKUInfos.put("colorList", colorList);
        productSKUInfos.put("sizeList", sizeList);
        productSKUInfos.put("skuList", skuList);
        return productSKUInfos;
    }

    /* (non-Javadoc)
     * @see com.store.service.IWxaProductService#getPropertyValue(java.lang.String)
     */
    public List<String> getPropertyValue(String propertyValueIds) {
        if (propertyValueIds == null) {
            return new ArrayList<>();
        }
        String[] propertyValueIdArray = propertyValueIds.split(",");
        List<String> propertyValueList = new ArrayList<>();
        for (String propertyValueId : propertyValueIdArray) {
            //ShopPropertyValue propertyRecord = new ShopPropertyValue();
            //propertyRecord.setId(Long.parseLong(propertyValueId));
            ShopPropertyValue shopPropertyValue = shopPropertyValueMapper.selectById(propertyValueId);
            if (shopPropertyValue == null) {
                continue;
            }
            propertyValueList.add(shopPropertyValue.getPropertyValue());
        }
        return propertyValueList;
    }


}