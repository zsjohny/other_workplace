package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.beust.jcommander.internal.Lists;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.application.order.Product;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.live.LiveUserTypeEnum;
import com.e_commerce.miscroservice.commons.enums.product.LiveProductTypeEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.commons.utils.StringUtil;
import com.e_commerce.miscroservice.product.dao.*;
import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.product.mapper.LiveProductMapper;
import com.e_commerce.miscroservice.product.rpc.LiveUserRpcService;
import com.e_commerce.miscroservice.product.service.LiveProductService;
import com.e_commerce.miscroservice.product.vo.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.e_commerce.miscroservice.commons.enums.product.LiveProductTypeEnums.PLATFORM;
import static com.e_commerce.miscroservice.commons.enums.product.LiveProductTypeEnums.SUPPLIER_PRODUCT;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 9:28
 * @Copyright 玖远网络
 */
@Service
public class LiveProductServiceImpl implements LiveProductService {

    private Log logger = Log.getInstance(LiveProductServiceImpl.class);


    private static final String OPER_TOP = "TOP";
    private static final String OPER_SORT = "SORT";
    private static final String OPER_SIMPLE = "SIMPLE";


    @Autowired
    private LiveUserRpcService liveUserRpcService;
    @Autowired
    private DynamicPropertyCategoryDao dynamicPropertyCategoryDao;
    @Autowired
    private ProductPropertyDao productPropertyDao;
    @Autowired
    private ProductSkuDao productSkuDao;
    @Autowired
    private ShopProductDao shopProductDao;
    @Autowired
    private LiveProductMapper liveProductMapper;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private LiveProductDao liveProductDao;


    /**
     * 直播商品选择列表
     *
     * @param vo vo
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/14 15:52
     */
    @Override
    public Map<String, Object> productSelectList(LiveProductVO vo) {
        LiveUserDTO anchor = liveUserRpcService.findLiveUserByAnchorId(vo.getMemberId());
        ErrorHelper.declareNull(anchor, "没找到播主");
        ErrorHelper.declare(anchor.getStatus().equals(0), "播主账户已被禁用");

        Long storeId = anchor.getStoreId();
        Integer anchorType = anchor.getLiveType();
        Long anchorId = anchor.getId();

        LiveUserTypeEnum anchorTypeEnum = LiveUserTypeEnum.create(anchorType);
        ErrorHelper.declareNull(anchorType, "没有找到用户的主播类型");

        List<Map<String, Object>> productList = Collections.emptyList();
        switch (anchorTypeEnum) {
            case PLATFORM:
                //查询平台
                ProductVO productVO = new ProductVO();
                productVO.setPageNumber(vo.getPageNumber());
                productVO.setPageSize(vo.getPageSize());
                productVO.setAnchorId(anchorId);
                productVO.setName(vo.getProductName());
                productList = productDao.listPlatformLiveSelectProducts(productVO);
                break;
            case SHOP:
            case COMMON:
                //小程序
                ShopProductQuery query = new ShopProductQuery();
                query.setPageNumber(vo.getPageNumber());
                query.setPageSize(vo.getPageSize());
                query.setName(vo.getProductName());
                query.setStoreId(storeId);
                query.setAnchorId(anchorId);
                productList = shopProductDao.listWxaLiveSelectProducts(query);
                break;
            default:
                ErrorHelper.declare(false, "未知的用户主播类型");
        }

        //格式化图片
        productList.stream().forEach(pdc -> {
            String summaryImgJsonArr = pdc.get("summaryImgJsonArr").toString();
//            pdc.put("summaryImgJsonArr", StringUtil.subJsonArray(summaryImgJsonArr, 1));
            pdc.put("summaryImg", StringUtil.firstElemOfJsonArray(summaryImgJsonArr));
            pdc.remove("supplierProductSummaryImgJsonArr");
            pdc.remove("supplierProductStyleNo");
        });

        return MapHelper.me(1).put("dataList", new SimplePage<>(productList));
    }


    /**
     * 添加到直播商品
     *
     * @param vo shopProductIds
     * @author Charlie
     * @date 2019/1/14 17:53
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void batchInsertByProductIds(LiveProductVO vo) {
        Long memberId = vo.getMemberId();
        List<Long> shopProductIds = vo.getShopProductIds();
        logger.info("批量添加直播商品 memberId={},shopProductIds={}", memberId, shopProductIds);
        LiveUserDTO anchor = liveUserRpcService.findLiveUserByAnchorId(memberId);
        ErrorHelper.declareNull(anchor, "没找到播主");
        ErrorHelper.declare(anchor.getStatus().equals(0), "播主账户已被禁用");

        Integer anchorType = anchor.getLiveType();
        Long anchorId = anchor.getId();
        Long roomNo = anchor.getRoomNum();
        ErrorHelper.declareNull(anchorType, "没有找到用户的主播类型");

        LiveUserTypeEnum type = LiveUserTypeEnum.create(anchor.getLiveType());

        //如果主播是平台主播==>yjj_product主键,如果是小程序主播==>shop_product主键
        if (ObjectUtils.isEmpty(shopProductIds)) {
            logger.info("没有商品id");
            return;
        }

        //组装直播商品实体类
        List<LiveProduct> liveProducts = Lists.newArrayList(shopProductIds.size());
        switch (type) {
            case PLATFORM: {
                //平台主播
                //查询是否已存在
                List<Long> existSupplierIds = liveProductDao.listPlatformBySupplierProductIds(anchorId, shopProductIds);
                shopProductIds.removeAll(existSupplierIds);
                if (shopProductIds.isEmpty()) {
                    logger.info("商品已加入过直播商品");
                    return;
                }
                List<Product> products = productDao.listByIds4InitLiveProduct(shopProductIds);
                ErrorHelper.declare(products.size() == shopProductIds.size(), "未找到商品信息");

                long curr = System.currentTimeMillis();
                for (Product product : products) {
                    LiveProduct newLp = new LiveProduct();
                    newLp.setAnchorId(anchorId);
                    newLp.setLivePrice(product.getWholeSaleCash());
                    newLp.setRoomNum(roomNo);
                    newLp.setShopProductId(0L);
                    newLp.setSupplierProductId(product.getId());
                    newLp.setType(PLATFORM.getCode());
                    curr += 2;
                    long random = randomSortNo(curr);
                    newLp.setSortNo(random);
                    liveProducts.add(newLp);
                }
                break;
            }
            case SHOP:
            case COMMON:
                //小程序主播
                //查询是否已存在
                List<Long> existShopProductIds = liveProductDao.listSelfLiveByShopProductIds(anchorId, shopProductIds);
                shopProductIds.removeAll(existShopProductIds);
                if (shopProductIds.isEmpty()) {
                    logger.info("商品已加入过直播商品");
                    return;
                }
                List<ShopProduct> shopProducts = shopProductDao.listByIds4InitLiveProduct(shopProductIds);
                ErrorHelper.declare(shopProductIds.size() == shopProducts.size(), "没有找到商品信息");

                long curr = System.currentTimeMillis();
                for (ShopProduct shopProduct : shopProducts) {
                    LiveProduct newLp = new LiveProduct();
                    newLp.setAnchorId(anchorId);
                    newLp.setLivePrice(shopProduct.getPrice().doubleValue());
                    newLp.setRoomNum(roomNo);
                    newLp.setShopProductId(shopProduct.getId());
                    newLp.setSupplierProductId(shopProduct.getProductId());
                    newLp.setType(shopProduct.getOwn());
                    curr += 2;
                    long random = randomSortNo(curr);
                    newLp.setSortNo(random);
                    liveProducts.add(newLp);
                }
                break;
            default:
                throw ErrorHelper.me("未知的用户主播类型");
        }

        //sql insert
        int save = liveProductDao.batchInsertSafe(liveProducts);
        ErrorHelper.declare(save == liveProducts.size(), "加入直播商品失败");
    }

    public long randomSortNo(long curr) {
        return curr * 10000 + new Random().nextInt(10000);
    }

    /**
     * 编辑直播商品
     *
     * @param vo vo
     * @author Charlie
     * @date 2019/1/15 13:41
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void update(LiveProductVO vo) {
        String operType = vo.getOperType();
        Long memberId = vo.getMemberId();
        Double livePrice = vo.getLivePrice();
        Integer liveStatus = vo.getLiveStatus();
        logger.info("编辑商品 memberId={}, operType={},livePrice={},liveStatus={}", memberId, operType, livePrice, liveStatus);
        ErrorHelper.declareNull(operType, "未知的操作类型");

        LiveUserDTO anchor = liveUserRpcService.findLiveUserByAnchorId(memberId);
        ErrorHelper.declareNull(anchor, "没找到播主");
        ErrorHelper.declare(anchor.getStatus().equals(0), "播主账户已被禁用");

        Long anchorId = anchor.getId();
        logger.info("编辑直播商品 operType={},vo={}", operType, vo);

        Long id = vo.getId();
        switch (operType.toUpperCase()) {
            case OPER_TOP: {
                //置顶(按当前时间X2)
                LiveProduct liveProduct = liveProductDao.findById(id, anchorId);
                ErrorHelper.declareNull(liveProduct, "未找到直播商品");
                long random = randomSortNo(System.currentTimeMillis());
                long now = random << 1;
                LiveProduct updVO = new LiveProduct();
                updVO.setId(id);
                updVO.setSortNo(now);

                int upd = liveProductDao.updateById(updVO);
                ErrorHelper.declare(upd == 1, "设置置顶失败");
                break;
            }
            case OPER_SORT:{
                String sortNoJson = vo.getSortNoJson();
                if (StringUtils.isNotBlank(sortNoJson)) {
                    JSONArray jsonArray = JSONObject.parseArray(sortNoJson);
                    if (jsonArray.size() > 1) {
                        //去重
                        JSONObject second = jsonArray.getJSONObject(1);
                        Long secondId = second.getLong("liveProductId");
                        long secondNo = second.getLongValue("sortNo");
                        LiveProduct secondBO = new LiveProduct();
                        secondBO.setId(secondId);
                        secondBO.setSortNo(secondNo);
                        liveProductDao.updateById(secondBO);

                        JSONObject first = jsonArray.getJSONObject(0);
                        Long firstId = first.getLong("liveProductId");
                        long firstNo = first.getLongValue("sortNo");
                        LiveProduct firstBO = new LiveProduct();
                        firstBO.setId(firstId);
                        firstBO.setSortNo(firstNo == secondNo ? (++ firstNo) : firstNo);
                        liveProductDao.updateById(firstBO);
                    }
                }
                break;
            }
            case OPER_SIMPLE: {
                //简单的更新操作
                LiveProduct liveProduct = liveProductDao.findById(id, anchorId);
                ErrorHelper.declareNull(liveProduct, "未找到直播商品");

                LiveProduct updVo = new LiveProduct();
                updVo.setId(id);
                Double newLivePrice = livePrice;
                if (newLivePrice != null && newLivePrice < 0d) {
                    throw ErrorHelper.me("价格错误,不能小于0");
                }
                updVo.setLivePrice(newLivePrice);
                updVo.setLiveStatus(liveStatus);

                int upd = liveProductDao.updateById(updVo);
                ErrorHelper.declare(upd == 1, "更新直播商品失败");
                break;
            }
            default:
                throw ErrorHelper.me("未知的操作类型");
        }
    }


    /**
     * 直播中的商品
     *
     * @param vo userId
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/15 14:28
     */
    @Override
    public Map<String, Object> listLiveProduct(LiveProductVO vo) {
        Long memberId = vo.getMemberId();
        Integer liveStatus = vo.getLiveStatus();
        logger.info("直播列表 memberId={},liveStatus={},productName={}", memberId, liveStatus, vo.getProductName());
        LiveUserDTO anchor = liveUserRpcService.findLiveUserByAnchorId(memberId);
        ErrorHelper.declareNull(anchor, "没找到播主");
        ErrorHelper.declare(anchor.getStatus().equals(0), "播主账户已被禁用");

        vo.setRoomNumList(Stream.of(anchor.getRoomNum()).collect(Collectors.toList()));
        //根据播主类型设置查询参数,平台的是关联yjj_product表,用户自己的是关联shop_product表,用来做商品名称的模糊查询用
        LiveUserTypeEnum type = LiveUserTypeEnum.create(anchor.getLiveType());
        switch (type) {
            case PLATFORM:
                vo.setAnchorType(1);
                break;
            case SHOP:
            case COMMON:
                vo.setAnchorType(2);
                break;
            default:
                throw ErrorHelper.me("未知的直播类型");
        }

        //查询
        List<LiveProductVO> liveProducts = doQueryLiveProductListByRoomNumList(vo, true);
        //只要一张图
        liveProducts.stream().forEach(lp -> {
            lp.setSummaryImg(StringUtil.firstElemOfJsonArray(lp.getSummaryImgJsonArr()));
            if (lp.getInventory() == null) {
                lp.setInventory(0);
            }
        });
        return MapHelper.me(1).put("dataList", new SimplePage<>(liveProducts));
    }


    /**
     * 根据房间号查询所有直播商品信息
     *
     * @param vo LiveProductVO#roomNumList 房间号必填
     * @param vo LiveProductVO#liveStatus 直播商品状态,选填 直播状态:0正常,1取消直播
     * @return java.util.List<com.e_commerce.miscroservice.product.vo.LiveProductVO>
     * @author Charlie
     * @date 2019/1/16 15:57
     */
    @Override
    public List<LiveProductVO> doQueryLiveProductListByRoomNumList(LiveProductVO vo, boolean isQuerySku) {
        Integer pageNumber = vo.getPageNumber();
        Integer pageSize = vo.getPageSize();
        boolean isPage = pageNumber != null && pageSize != null;
        if (isPage) {
            PageHelper.startPage(pageNumber, pageSize);
        }

        if (ObjectUtils.isEmpty(vo.getRoomNumList())) {
            return Collections.emptyList();
        }

        List<LiveProductVO> liveProducts = liveProductMapper.findLiveProductIdsByRoomId(vo);
        if (! liveProducts.isEmpty()) {

            //======================================= 准备填充数据的数据源 =======================================
            int size = liveProducts.size();
            //待查询的供应商商品信息
            List<Long> waitQuerySupplierProductIds = Lists.newArrayList(size);
            //待查询的自有商品信息
            List<Long> waitQuerySelfProductIds = Lists.newArrayList(size);
            liveProducts.stream().forEach(lp -> {
                LiveProductTypeEnums type = LiveProductTypeEnums.create(lp.getType());
                if (type == null) {
                    logger.warn("没有的商品类型 liveProduct={}", lp);
                }
                switch (type) {
                    case SUPPLIER_PRODUCT:
                        waitQuerySupplierProductIds.add(lp.getSupplierProductId());
                        waitQuerySelfProductIds.add(lp.getShopProductId());
                        break;
                    case SELF_CUSTOM:
                    case AS_SAME_AS_SUPPLIER:
                        waitQuerySelfProductIds.add(lp.getShopProductId());
                        break;
                    case PLATFORM:
                        waitQuerySupplierProductIds.add(lp.getSupplierProductId());
                        break;
                    default:
                        throw ErrorHelper.me("未知的商品类型");
                }
            });
            //等待组装的小程序商品信息
            List<ShopProduct> readyQueryShopProducts = shopProductDao.findSimpleInfoByIds(waitQuerySelfProductIds);
            Map<Long, List<ShopProduct>> shopProductPool = readyQueryShopProducts.stream().collect(Collectors.groupingBy(product -> product.getId()));


            //等待组装的供应商商品信息
            List<Product> readyQuerySupplierProducts = productDao.findSimpleInfoByIds(waitQuerySupplierProductIds);
            Map<Long, List<Product>> supplierProductPool = readyQuerySupplierProducts.stream().collect(Collectors.groupingBy(product -> product.getId()));

            //小程序商品的sku信息(供应商商品的sku,在用的时候再查)
            Map<Long, SkuOfProductDTO> skuOfProductOfSelfLive = Collections.emptyMap();
            if (isQuerySku) {
                skuOfProductOfSelfLive = findSkuOfProductOfSelfLive(liveProducts);
            }

            //======================================= 开始填充 =======================================
            //一键上传商品(shop_product+yjj_product)
            fillProductInfoOfSelfLiveOfSupplierProduct(liveProducts, shopProductPool, supplierProductPool, skuOfProductOfSelfLive);
            //自己的商品(shop_product表就行了)
            fillProductInfoOfSelfLiveOfSelfProduct(liveProducts, shopProductPool, skuOfProductOfSelfLive);
            //平台直播商品(yjj_product表就行了)
            fillProductInfoOfPlatformLive(liveProducts, supplierProductPool, isQuerySku);
        }
        return liveProducts;
    }


    /**
     * 正在直播推荐的商品
     *
     * @param vo vo
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/16 14:49
     */
    @Override
    public Map<String, Object> listOnRecommended(LiveProductVO vo) {
        Long roomNum = vo.getRoomNum();
        logger.info("直播间--正在推荐 roomNum={}", roomNum);
        LiveUserDTO anchor = liveUserRpcService.findLiveUserByRoomNum(roomNum);
        ErrorHelper.declareNull(anchor, "没找到播主");
        ErrorHelper.declare(anchor.getStatus().equals(0), "播主账户已被禁用");

        vo.setRoomNumList(Stream.of(roomNum).collect(Collectors.toList()));
        //只查询没有取消的
        vo.setLiveStatus(0);
        List<LiveProductVO> liveProductVOs = doQueryLiveProductListByRoomNumList(vo, false);

        //格式化
        liveProductVOs.stream().forEach(liveProductVO -> {
            //取一张图
//            liveProductVO.setSummaryImgJsonArr(StringUtil.subJsonArray(liveProductVO.getSummaryImgJsonArr(), 1));
            liveProductVO.setSummaryImg(StringUtil.firstElemOfJsonArray(liveProductVO.getSummaryImgJsonArr()));

            //平台不显示价格
            if (LiveProductTypeEnums.PLATFORM.isThis(liveProductVO.getType())) {
                liveProductVO.setLivePrice(Double.MIN_VALUE);
            }
        });

        return MapHelper.me(1).put("dataList", new SimplePage<>(liveProductVOs));
    }


    /**
     * 查询商品信息
     *
     * @param liveProductId liveProductId
     * @return com.e_commerce.miscroservice.commons.utils.MapHelper
     * @author Charlie
     * @date 2019/1/16 16:21
     */
    @Override
    public MapHelper productIntro(Long liveProductId) {
        LiveProduct liveProduct = liveProductDao.findById(liveProductId);
        ErrorHelper.declareNull(liveProduct, "没有找到商品信息");
        ErrorHelper.declare(liveProduct.getDelStatus().equals(StateEnum.NORMAL), "商品不存在");
        ErrorHelper.declare(liveProduct.getLiveStatus().equals(0), "商品已从直播下架");

        MapHelper retVal = MapHelper.me(16);

        Long shopProductId = liveProduct.getShopProductId();
        Long supplierProductId = liveProduct.getSupplierProductId();

        LiveProductTypeEnums type = LiveProductTypeEnums.create(liveProduct.getType());
        //商品信息
        MapHelper productMap = MapHelper.me(10);
        ShopProduct shopProduct;
        //sku
        List<ProductSku> skuList;
        switch (type) {
            case SUPPLIER_PRODUCT: {
                shopProduct = shopProductDao.findLiveProductIntroById(shopProductId);
                ErrorHelper.declareNull(shopProduct, "没有找到小程序商品信息");
                productMap.put("liveProductId", liveProductId);
                productMap.put("productStatus", liveProductSateFromSelfProduct(shopProduct.getSoldOut(), shopProduct.getStatus()));
                productMap.put("name", shopProduct.getName());

                List<Product> products = productDao.findSimpleInfoByIds(Arrays.asList(supplierProductId));
                ErrorHelper.declareNull(products.size() > 0, "没有找到供应商商品信息");
                Product product = products.get(0);
                productMap.put("clothesNumber", product.getClothesNumber());
                productMap.put("summaryImage", StringUtil.firstElemOfJsonArray(product.getDetailImages()));
                productMap.put("shopProductId", shopProduct.getId());
                productMap.put("supplierProductId", product.getId());
                productMap.put("own", shopProduct.getOwn());
                //查询sku
                skuList = productSkuDao.findSimpleOfSupplierProductId(supplierProductId, Arrays.asList(0));
                break;
            }
            case AS_SAME_AS_SUPPLIER:
            case SELF_CUSTOM:
                //查询商品
                shopProduct = shopProductDao.findLiveProductIntroById(shopProductId);
                ErrorHelper.declareNull(shopProduct, "没有找到商品信息");
                productMap.put("liveProductId", liveProductId);
                productMap.put("productStatus", liveProductSateFromSelfProduct(shopProduct.getSoldOut(), shopProduct.getStatus()));
                productMap.put("name", shopProduct.getName());
                productMap.put("own", shopProduct.getOwn());
                productMap.put("shopProductId", shopProduct.getId());
                productMap.put("clothesNumber", shopProduct.getClothesNumber());
                productMap.put("summaryImage", StringUtil.firstElemOfJsonArray(shopProduct.getSummaryImages()));
                //查询sku
                skuList = productSkuDao.findSimpleOfShopProductId(shopProductId, Arrays.asList(0));
                break;
            case PLATFORM:
                List<Product> products = productDao.findSimpleInfoByIds(Arrays.asList(supplierProductId));
                ErrorHelper.declareNull(products.size() > 0, "没有找到供应商商品信息");
                Product product = products.get(0);
                ErrorHelper.declareNull(product, "没有找到平台商品信息");

                productMap.put("liveProductId", liveProductId);
                productMap.put("supplierProductId", product.getId());
                productMap.put("productStatus", liveProductSateFromSupplierProduct(product.getState(), product.getDelState()));
                productMap.put("own", "");
                productMap.put("name", product.getName());
                productMap.put("clothesNumber", product.getClothesNumber());
                productMap.put("summaryImage", StringUtil.firstElemOfJsonArray(product.getDetailImages()));
                //查询参数
//                List<Map<String, String>> productPropertyList = productPropertyDao.listByProduct(supplierProductId);
                List<ProductPropertyDTO> productPropertyDTOS = dynamicPropertyCategoryDao.findByProductId(supplierProductId, product.getThreeCategoryId());
                productMap.put("productPropertyList", productPropertyDTOS);

                //查询sku
                skuList = productSkuDao.findSimpleOfSupplierProductId(supplierProductId, Arrays.asList(0));
                break;
            default:
                throw ErrorHelper.me("商品状态错误");
        }

        productMap.put("livePrice", liveProduct.getLivePrice());
        retVal.put("product", productMap);
        retVal.put("skuList", skuList);
        int size = skuList.size();
        if (size != 0) {
            Map<Long, String> colorMap = new HashMap<>(size);
            Map<Long, String> sizeMap = new HashMap<>(size);
            skuList.forEach(sku -> {
                Long colorId = sku.getColorId();
                String colorName = sku.getColorName();
                colorMap.put(colorId, colorName);
                Long sizeId = sku.getSizeId();
                String sizeName = sku.getSizeName();
                sizeMap.put(sizeId, sizeName);
            });

            //再封装一下,老接口兼容
            List<Object> sizeList = Lists.newArrayList(sizeMap.size());
            sizeMap.forEach((id, value)->{
                Map<String, Object> smap = new HashMap<>(2);
                smap.put("sizeId", id);
                smap.put("value", value);
                sizeList.add(smap);
            });
            List<Object> colorList = Lists.newArrayList(colorMap.size());
            colorMap.forEach((id, value)->{
                Map<String, Object> cmap = new HashMap<>(2);
                cmap.put("colorId", id);
                cmap.put("value", value);
                colorList.add(cmap);
            });
            retVal.put("colorList", colorList).put("sizeList", sizeList);
        } else {
            retVal.put("colorList", Collections.EMPTY_MAP).put("sizeList", Collections.EMPTY_MAP);
        }
        return retVal;
    }


    @Override
    public MapHelper productDetail(Long liveProductId) {
        LiveProduct liveProduct = liveProductDao.findById(liveProductId);
        ErrorHelper.declareNull(liveProduct, "没有找到商品信息");
        ErrorHelper.declare(liveProduct.getDelStatus().equals(StateEnum.NORMAL), "商品不存在");
        ErrorHelper.declare(liveProduct.getLiveStatus().equals(0), "商品已从直播下架");

        MapHelper retVal = MapHelper.me(8);

        Long shopProductId = liveProduct.getShopProductId();
        Long supplierProductId = liveProduct.getSupplierProductId();
        switch (LiveProductTypeEnums.create(liveProduct.getType())) {
            case SUPPLIER_PRODUCT: {
                //一键上传APP商品
                Product product = productDao.findImg(supplierProductId);
                String sizeTableImages = product.getSizeTableImage();
                retVal.put("sizeTableImages", JSONObject.parseArray(sizeTableImages));
                String detailImages = product.getSummaryImages();
                if (JSONObject.parseArray(detailImages).isEmpty()) {
                    detailImages = product.getDetailImages();
                }
                retVal.put("detailImages", JSONObject.parseArray(detailImages));
                //自定义的详情,平台的商品详情都要给
                ShopProduct shopProduct = shopProductDao.findImg(shopProductId);
                retVal.put("shopOwnDetail", shopProduct.getShopOwnDetail());
                retVal.put("name", shopProduct.getName());
                break;
            }
            case PLATFORM: {
                //平台直播商品
                Product product = productDao.findImg(supplierProductId);
                String sizeTableImages = product.getSizeTableImage();
                retVal.put("sizeTableImages", JSONObject.parseArray(sizeTableImages));
                String detailImages = product.getSummaryImages();
                if (JSONObject.parseArray(detailImages).isEmpty()) {
                    detailImages = product.getDetailImages();
                }
                retVal.put("detailImages", JSONObject.parseArray(detailImages));
                retVal.put("summaryImages", JSONObject.parseArray(product.getDetailImages()));
                retVal.put("shopOwnDetail", EmptyEnum.string);
                retVal.put("name", product.getName());
                break;
            }
            case SELF_CUSTOM:
            case AS_SAME_AS_SUPPLIER:
                ShopProduct shopProduct = shopProductDao.findImg(shopProductId);
                retVal.put("sizeTableImages", EmptyEnum.string);
                retVal.put("detailImages", EmptyEnum.string);
                retVal.put("name", shopProduct.getName());
                String shopOwnDetail = shopProduct.getShopOwnDetail();
                retVal.put("shopOwnDetail", sortDetailJson(shopOwnDetail));
                break;
            default:
                throw ErrorHelper.me("未知的商品类型");
        }
        return retVal;
    }




    /**
     * 排序
     *
     * @param shopOwnDetail shopOwnDetail
     * @return java.lang.String
     * @author Charlie
     * @date 2019/1/17 10:54
     */
    private static String sortDetailJson(String shopOwnDetail) {
        List<Map<String, String>> detailList = JSONObject.parseObject(shopOwnDetail, new TypeReference<List<Map<String, String>>>() {});
        if (detailList.isEmpty()) {
            return shopOwnDetail;
        }

        detailList = detailList.stream().sorted((m1, m2) -> Integer.parseInt(m2.get("type")) - Integer.parseInt(m1.get("type"))).collect(Collectors.toList());
        return JSONObject.toJSONString(detailList);
    }


    /**
     * 填充自己直播间的一键上传商品
     *
     * @param liveProducts 待填充数据
     * @param shopProductPool 准备的小程序商品数据
     * @param supplierProductPool 准备的供应商商品数据
     * @param skuOfProductOfSelfLive 准备的sku信息
     * @author Charlie
     * @date 2019/1/16 11:48
     */
    private void fillProductInfoOfSelfLiveOfSupplierProduct(List<LiveProductVO> liveProducts, Map<Long, List<ShopProduct>> shopProductPool, Map<Long, List<Product>> supplierProductPool, Map<Long, SkuOfProductDTO> skuOfProductOfSelfLive) {
        //填充名称,价格
        liveProducts.stream().forEach(liveProductVO -> {
            Integer type = liveProductVO.getType();
            if (SUPPLIER_PRODUCT.isThis(type)) {
                Long shopProductId = liveProductVO.getShopProductId();
                List<ShopProduct> subShopProducts = shopProductPool.get(shopProductId);
                if (ObjectUtils.isEmpty(subShopProducts)) {
                    logger.warn("没有找到商品信息 shopProductId={}", shopProductId);
                } else {
                    //填充
                    ShopProduct product = subShopProducts.get(0);
                    liveProductVO.setProductName(product.getName());
                    liveProductVO.setOriginalPrice(product.getPrice().doubleValue());
                }
            }
        });

        //填充款号,橱窗图,状态
        liveProducts.stream().forEach(liveProductVO -> {
            Integer type = liveProductVO.getType();
            if (SUPPLIER_PRODUCT.isThis(type)) {
                Long supplierProductId = liveProductVO.getSupplierProductId();
                List<Product> subProducts = supplierProductPool.get(supplierProductId);
                if (ObjectUtils.isEmpty(subProducts)) {
                    logger.warn("没有找到商品信息 supplierProductId={}", supplierProductId);
                } else {
                    //填充
                    Product product = subProducts.get(0);
                    liveProductVO.setStyleNo(product.getClothesNumber());
                    liveProductVO.setSummaryImgJsonArr(product.getDetailImages());
                    Integer delState = product.getDelState();
                    Integer productStatus = liveProductSateFromSelfProduct(product.getState(), delState);
                    liveProductVO.setProductStatus(productStatus);
                }
            }
        });

        //填充sku
        if (! skuOfProductOfSelfLive.isEmpty()) {
            liveProducts.stream().forEach(liveProductVO -> {
                if (SUPPLIER_PRODUCT.isThis(liveProductVO.getType())) {
                    int ivt = extractInventory(skuOfProductOfSelfLive, liveProductVO);
                    liveProductVO.setInventory(ivt);
                }
            });
        }
    }


    /**
     * 填充自己直播间的自有商品(自定义商品+供应商同款)
     *
     * @param liveProducts 待填充
     * @param shopProductPool 准备的商品数据
     * @param skuOfProductOfSelfLive 准备的sku数据
     * @author Charlie
     * @date 2019/1/16 11:45
     */
    private void fillProductInfoOfSelfLiveOfSelfProduct(List<LiveProductVO> liveProducts, Map<Long, List<ShopProduct>> shopProductPool, Map<Long, SkuOfProductDTO> skuOfProductOfSelfLive) {
        liveProducts.stream().forEach(liveProductVO -> {
            Integer type = liveProductVO.getType();
            if (LiveProductTypeEnums.SELF_CUSTOM.isThis(type) || LiveProductTypeEnums.AS_SAME_AS_SUPPLIER.isThis(type)) {
                Long shopProductId = liveProductVO.getShopProductId();
                List<ShopProduct> subShopProducts = shopProductPool.get(shopProductId);
                if (ObjectUtils.isEmpty(subShopProducts)) {
                    logger.warn("没有找到商品信息 shopProductId={}", shopProductId);
                } else {
                    //填充
                    ShopProduct product = subShopProducts.get(0);
                    liveProductVO.setProductName(product.getName());
                    liveProductVO.setStyleNo(product.getClothesNumber());
                    liveProductVO.setOriginalPrice(product.getPrice().doubleValue());
                    liveProductVO.setSummaryImgJsonArr(product.getSummaryImages());
                    Integer productStatus = liveProductSateFromSelfProduct(product.getSoldOut(), product.getStatus());
                    liveProductVO.setProductStatus(productStatus);
                }
            }
        });

        //填充sku
        if (! skuOfProductOfSelfLive.isEmpty()) {
            liveProducts.stream().forEach(liveProductVO -> {
                if (LiveProductTypeEnums.SELF_CUSTOM.isThis(liveProductVO.getType()) || LiveProductTypeEnums.AS_SAME_AS_SUPPLIER.isThis(liveProductVO.getType())) {
                    int ivt = extractInventory(skuOfProductOfSelfLive, liveProductVO);
                    liveProductVO.setInventory(ivt);
                }
            });
        }
    }

    private int extractInventory(Map<Long, SkuOfProductDTO> skuOfProductOfSelfLive, LiveProductVO liveProductVO) {
        Long shopProductId = liveProductVO.getShopProductId();
        SkuOfProductDTO dto = skuOfProductOfSelfLive.get(shopProductId);
        int ivt = 0;
        if (dto == null) {
            logger.info("没有找到sku信息 shopProductId={}", shopProductId);
        } else {
            ivt = dto.getInventory();
        }
        return ivt;
    }


    /**
     * 填充平台直播商品信息
     *
     * @param liveProducts 待填充
     * @param supplierProductPool 准备的数据
     * @param isQuerySku 是否查询sku
     * @author Charlie
     * @date 2019/1/16 11:38
     */
    private void fillProductInfoOfPlatformLive(List<LiveProductVO> liveProducts, Map<Long, List<Product>> supplierProductPool, boolean isQuerySku) {
        liveProducts.stream().forEach(liveProductVO -> {
            if (PLATFORM.isThis(liveProductVO.getType())) {
                Long supplierProductId = liveProductVO.getSupplierProductId();
                List<Product> subProducts = supplierProductPool.get(supplierProductId);
                if (ObjectUtils.isEmpty(subProducts)) {
                    logger.warn("没有找到商品信息 supplierProductId={}", supplierProductId);
                } else {
                    //填充
                    Product product = subProducts.get(0);
                    liveProductVO.setProductName(product.getName());
                    liveProductVO.setStyleNo(product.getClothesNumber());
                    liveProductVO.setOriginalPrice(product.getWholeSaleCash());
                    liveProductVO.setSummaryImgJsonArr(product.getDetailImages());
                    Integer productStatus = liveProductSateFromSupplierProduct(product.getState(), product.getDelState());
                    liveProductVO.setProductStatus(productStatus);
                }
            }
        });

        //填充库存
        if (isQuerySku) {
            List<Long> productIds = liveProducts.stream()
                    .filter(lp -> LiveProductTypeEnums.PLATFORM.isThis(lp.getType()))
                    .mapToLong(value -> value.getSupplierProductId())
                    .boxed()
                    .collect(Collectors.toList());
            Map<Long, SkuOfProductDTO> skuOfProductDTOMap = productDao.listSkuBySupplierProductIds(productIds);
            liveProducts.stream().forEach(liveProductVO -> {
                if (PLATFORM.isThis(liveProductVO.getType())) {
                    Long supplierProductId = liveProductVO.getSupplierProductId();
                    SkuOfProductDTO dto = skuOfProductDTOMap.get(supplierProductId);
                    int ivt = 0;
                    if (dto == null) {
                        logger.info("没有找到sku信息 supplierProductId={}", supplierProductId);
                    } else {
                        ivt = dto.getInventory();
                    }
                    liveProductVO.setInventory(ivt);
                }
            });
        }
    }


    /**
     * 查询自有直播的小程序商品的sku信息
     *
     * @param liveProducts 直播商品
     * @return java.util.Map
     * @author Charlie
     * @date 2019/1/16 11:24
     */
    private Map<Long, SkuOfProductDTO> findSkuOfProductOfSelfLive(List<LiveProductVO> liveProducts) {
        //过滤平台直播商品
        List<LiveProductVO> waitQueryShopSkuFilter = liveProducts.stream().filter(vo -> LiveProductTypeEnums.isSelfLiveProduct(vo.getType())).collect(Collectors.toList());
        //查询商品sku
        List<ShopProductVO> shopProductVOList = Lists.newArrayList(waitQueryShopSkuFilter.size());
        waitQueryShopSkuFilter.stream().forEach(action -> {
            ShopProductVO skuQuery = new ShopProductVO();
            skuQuery.setId(action.getShopProductId());
            skuQuery.setProductId(action.getSupplierProductId());
            skuQuery.setOwn(action.getType());
            shopProductVOList.add(skuQuery);
        });
        return shopProductDao.listOnSaleWxaSkuByShopProductIds(shopProductVOList);
    }


    private static Integer liveProductSateFromSelfProduct(Integer soldOut, Integer delState) {
        Integer productStatus;
        if (delState.equals(0)) {
            if (soldOut.equals(1)) {
                //上架
                productStatus = 1;
            } else if (soldOut.equals(2)) {
                //下架
                productStatus = 2;
            } else {
                //失效
                productStatus = 10;
            }
        } else {
            //失效
            productStatus = 10;
        }
        return productStatus;
    }


    private static Integer liveProductSateFromSupplierProduct(Integer state, Integer delState) {
        Integer productStatus;
        if (delState.equals(0)) {
            if (state.equals(6)) {
                //上架
                productStatus = 1;
            } else if (state.equals(7)) {
                //下架
                productStatus = 2;
            } else {
                //失效
                productStatus = 10;
            }
        } else {
            //失效
            productStatus = 10;
        }
        return productStatus;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
        int j = new Random().nextInt(1000);
        System.out.println("i = " + j);

        }
    }


}

