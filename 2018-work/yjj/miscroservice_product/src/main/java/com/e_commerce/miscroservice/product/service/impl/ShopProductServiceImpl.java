package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.product.ProductSkuSimpleVo;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.enums.product.ShopProductOwnEnum;
import com.e_commerce.miscroservice.commons.enums.product.ShopProductUpdTypeEnum;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.product.dao.ProductSkuDao;
import com.e_commerce.miscroservice.product.dao.ShopProductDao;
import com.e_commerce.miscroservice.product.dao.ShopProductTempInfoDao;
import com.e_commerce.miscroservice.product.entity.ProductSku;
import com.e_commerce.miscroservice.product.entity.ShopProduct;
import com.e_commerce.miscroservice.commons.entity.product.ShopProductQuery;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.product.entity.ShopProductTempInfo;
import com.e_commerce.miscroservice.product.mapper.ProductSkuMapper;
import com.e_commerce.miscroservice.product.mapper.ShopProductMapper;
import com.e_commerce.miscroservice.product.rpc.ShopActivityRpcService;
import com.e_commerce.miscroservice.product.service.ProductService;
import com.e_commerce.miscroservice.product.service.ShopGoodsCarService;
import com.e_commerce.miscroservice.product.service.ShopProductService;
import com.e_commerce.miscroservice.product.vo.ProductSkuQuery;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.commons.enums.product.ShopProductOwnEnum.SUPPLIER_PRODUCT;
import static java.lang.Boolean.FALSE;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/22 17:33
 * @Copyright 玖远网络
 */
@Service
public class ShopProductServiceImpl implements ShopProductService{


    private Log logger = Log.getInstance (ShopProductServiceImpl.class);

    @Autowired
    private ShopProductDao shopProductDao;
    @Autowired
    private ProductSkuDao productSkuDao;
    @Autowired
    private ShopProductMapper shopProductMapper;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopActivityRpcService shopActivityRpcService;
    @Autowired
    private ShopGoodsCarService shopGoodsCarService;
    @Autowired
    private ShopProductTempInfoDao shopProductTempInfoDao;


    /**
     * 查询一个对象
     *
     * @param query query
     * @return com.e_commerce.miscroservice.product.entity.ShopProduct
     * @author Charlie
     * @date 2018/11/22 17:39
     */
    @Override
    public ShopProduct selectOne(ShopProductQuery query) {
        logger.info ("查询商品 query={}", query);
        return shopProductMapper.selectOne (query);
    }


    /**
     * APP用户的小程序商品管理列表
     * <p>query.store != null </p>
     *
     * @param query query
     * @return java.util.Map
     * @author Charlie
     * @date 2018/11/26 11:09
     */
    @Override
    public Map<String, Object> manageList(ShopProductQuery query) {
        ErrorHelper.declareNull (query.getStoreId (), "没有用户信息");
        PageHelper.startPage (query.getPageNumber (), query.getPageSize ());
        //sql查询
        List<Map<String, Object>> dataList = shopProductMapper.manageList (query);

        //批发价, 如果是供应商商品,查询供应商商品价, 否则'-'

        //查询供应商平台商品, 查询库存,查询
        Set<Long> supplierProductIdList = new HashSet<> ();
        for (Map<String, Object> product : dataList) {
            Integer own = (Integer) product.get ("own");
            ShopProductOwnEnum ownEnum = ShopProductOwnEnum.create (own);
            if (ownEnum == SUPPLIER_PRODUCT) {
                //
                supplierProductIdList.add ((Long) product.get ("productId"));
                //格式化信息
                product.put ("xprice", product.get ("productWholeSaleCash"));
                product.put ("summaryImages", product.get ("productDetailImages"));

            }
            else {
                product.put ("xprice", "-");
            }

            //取第一张图
            Object summaryImages = product.get ("summaryImages");
            product.put ("summaryImage", EmptyEnum.string);
            if (BeanKit.notNull (summaryImages)) {
                JSONArray imgArray = JSONObject.parseArray (String.valueOf (summaryImages));
                if (! imgArray.isEmpty ()) {
                    product.put ("summaryImage", imgArray.get (0));
                }
            }
            product.remove ("summaryImages");

            //是否推荐 1推荐,0非推荐
            product.put ("isTop", Long.valueOf (product.get ("topTime").toString ()) > 0 ? 1 : 0);
        }

        List<Map<String, Object>> productInventory = EmptyEnum.list ();
        if (! supplierProductIdList.isEmpty ()) {
            //平台上传商品库存
            productInventory = productService.findInventoryByProductIds (supplierProductIdList);
        }

        //填充库存
        for (Map<String, Object> product : dataList) {
            Integer own = (Integer) product.get ("own");
            Long productId = (Long) product.get ("productId");
            Long id = (Long) product.get ("id");

            if (SUPPLIER_PRODUCT.isThis (own)) {
                //供应商同款查询供应商sku
                productInventory.forEach (inventory -> {
                    Long supplierProductId = (Long) inventory.get ("productId");
                    if (ObjectUtils.nullSafeEquals (productId, supplierProductId)) {
                        product.put ("inventory", inventory.get ("inventory"));
                    }
                });
            }
            else {
                //自营
                Long firstTimeOnSale = (Long) product.get ("firstTimeOnSale");
                if (firstTimeOnSale == 0) {
                    //未发布过,草稿中获取,查询草稿中的库存,这个表后面最好加个字段
                    Integer inventory = 0;
                    ShopProductTempInfo shopProductTempInfo = shopProductTempInfoDao.findByShopProductId (id);
                    if (shopProductTempInfo != null) {
                        String skuJson = shopProductTempInfo.getSkuJson ();
                        List<ProductSkuSimpleVo> skuList = JSONObject.parseObject (skuJson, new TypeReference<List<ProductSkuSimpleVo>> (){});
                        if (! ObjectUtils.isEmpty (skuList)) {
                            for (ProductSkuSimpleVo sku : skuList) {
                                inventory = sku.getRemainCount ();
                            }
                        }
                    }
                    product.put ("inventory", inventory);
                }
                else {
                    //发布过
                    Integer inventory = productSkuMapper.storeProductInventory (id);
                    product.put ("inventory", inventory);
                }
            }
        }


        Map<String, Object> result = new HashMap<> (2);
        result.put ("dataList", new SimplePage<> (dataList));
        return result;
    }


    /**
     * 批量更新
     * <p>
     * 对商品的更新,编辑的统一接口
     * </p>
     *
     * @param updateType 更新类型
     * @param request    更新的参数
     * @return boolean
     * @author Charlie
     * @date 2018/11/26 20:14
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map<String, Object> update(ShopProductUpdTypeEnum updateType, ShopProductQuery request) {
        Map<String, Object> result = EmptyEnum.map ();
        ErrorHelper.declareNull (request.getStoreId (), "用户未登录");
        switch (updateType) {
            //批量推荐
            case BATCH_DO_RECOMMEND:
                shopProductDao.batchRecommend (request, false);
                break;
            //批量取消推荐
            case BATCH_CANCEL_RECOMMEND:
                shopProductDao.batchRecommend (request, true);
                break;
            //批量上架
            case BATCH_PUT_ON_SALE:
                batchPutOnSale (request);
                break;
            //批量下架
            case BATCH_PUT_OFF_SALE: {
                batchPutOffSale (request);
                break;
            }
            //批量删除架
            case BATCH_DELETE:
                batchDelete (request);
                break;
            //更新sku库存
            case UPD_PRODUCT_SKU_REMAIN:
                updProductAllSku (request);
                break;
            //删除sku
            case DELETE_SUK:
                deleteSku (request);
                break;
            default:
                ErrorHelper.declare (FALSE, "未知的操作类型");
        }
        return result;
    }

    /**
     * 删除sku
     *
     * @param request request
     * @author Charlie
     * @date 2018/12/7 14:00
     */
    private void deleteSku(ShopProductQuery request) {

        Long storeId = request.getStoreId ();
        Long colorId = request.getColorId ();
        Long sizeId = request.getSizeId ();
        Long shopProductId = request.getId ();
        if (BeanKit.hasNull (colorId, sizeId, shopProductId)) {
            ErrorHelper.declare (false, "请求参数为空");
        }
        logger.info ("删除sku shopProductId={},storeId={},colorId={},sizeId={}", shopProductId, storeId, colorId, sizeId);



        ShopProduct shopProduct = shopProductDao.findById (shopProductId, storeId);
        ErrorHelper.declareNull (shopProduct, "没有找到商品信息");


        if (ShopProductOwnEnum.isNoSelfSupport (shopProduct.getOwn ())) {
            logger.info ("删除sku, 商品非自营商品,不能删除");
            return;
        }

        if (BeanKit.gt0 (shopProduct.getFirstTimeOnSale ())) {
            //上架过商品,
            //查找所有sku
            List<ProductSku> allSkuList = productSkuDao.findShopProductSkus (shopProductId);
            //至少有一个有效的的(上架和下架的)
            allSkuList = allSkuList.stream ().filter (productSku -> productSku.getStatus () >= - 1 && productSku.getStatus () <= 1).collect (Collectors.toList ());
            int allSkuSize = allSkuList.size ();
            ErrorHelper.declare (allSkuSize > 1 , "每个商品至少有一个sku,不允许删除sku");

            //过滤查询指定的sku
            List<Long> skuIds = allSkuList.stream ()
                    .filter (productSku -> colorId.equals (productSku.getColorId ()) && sizeId.equals (productSku.getSizeId ()))
                    .mapToLong (ProductSku::getId)
                    .boxed ()
                    .collect (Collectors.toList ());
            logger.info ("待删除的sku ids={}",skuIds);



            //删除
            int rec = productSkuDao.deleteShopProductSkuByIds (skuIds);
            logger.info ("批量删除sku skuIds[{}].rec[{}]", skuIds, rec);

            //通知购物车失效
            shopGoodsCarService.adviceGoodsCarSkuHasDisabled (skuIds, storeId);

        }
        else {
            //未上架过商品,临时表中修改
            ShopProductTempInfo tempInfo = shopProductTempInfoDao.findByShopProductId (shopProductId);
            ErrorHelper.declareNull (tempInfo, "没有找到sku信息");
            String skuJson = tempInfo.getSkuJson ();
            logger.info ("skuJson={}",skuJson);
            if (StringUtils.isNotBlank (skuJson)) {
                JSONArray skuArray = JSONObject.parseArray (skuJson);

                int size = skuArray.size ();
                ErrorHelper.declare(size > 1, "每个商品至少有一个sku,不允许删除sku");

                int index = - 1;
                for (int i = 0; i < size; i++) {
                    JSONObject sku = skuArray.getJSONObject (i);
                    Long tempSizeId = sku.getLong ("sizeId");
                    Long tempColorId = sku.getLong ("colorId");
                    if (sizeId.equals (tempSizeId) && colorId.equals (tempColorId)) {
                        index = i;
                        break;
                    }
                }

                if (index > - 1) {
                    String waitUpdSku = skuArray.toJSONString ();
                    logger.info ("开始删除sku waitUpdSku={}",waitUpdSku);
                    skuArray.remove (index);
                    ShopProductTempInfo updInfo = new ShopProductTempInfo ();
                    updInfo.setId (tempInfo.getId ());
                    updInfo.setSkuJson (waitUpdSku);
                    shopProductTempInfoDao.updateById (updInfo);
                }
            }
        }


 }


    /**
     * 更新商品的sku
     *
     * @param request request
     * @author Charlie
     * @date 2018/11/28 14:50
     */
    private void updProductAllSku(ShopProductQuery request) {
        Long storeId = request.getStoreId ();
        Long id = request.getId ();
        String skuJsonArray = request.getSkuJsonArray ();
        ErrorHelper.declareNull (id, "没有商品");
        ErrorHelper.declareNull (skuJsonArray, "没有修改信息");

        ShopProduct shopProduct = shopProductDao.findById (id, storeId);
        ErrorHelper.declareNull (shopProduct, "没有找到商品信息");

        ErrorHelper.declare (ShopProductOwnEnum.isSelfSupport (shopProduct.getOwn ()), "非自营商品,不能编辑sku");

        //需要更新的sku
        List<ProductSkuSimpleVo> requestSkus = ProductSkuSimpleVo.create (skuJsonArray);
        logger.info ("更新sku={}, size={}", skuJsonArray, requestSkus.size ());
        requestSkus.forEach (reqSku -> {
            if (BeanKit.hasNull (reqSku.getSizeId (), reqSku.getColorId (), reqSku.getRemainCount ())) {
                ErrorHelper.declare (false, "请求参数为空");
            }

            if (reqSku.getRemainCount () < 0) {
                ErrorHelper.declare (false, "请求参数不合法,库存不可小于0");
            }
        });


        if (BeanKit.gt0 (shopProduct.getFirstTimeOnSale ())) {
            //已发布商品,sku表中查
            logger.info ("开始更新商品sku库存");
            List<ProductSku> historySkuList = productSkuDao.findShopProductSku (shopProduct.getId (), Arrays.asList (-1,0));

            //需要更新的sku集合
            List<ProductSku> waitModifySkus = new ArrayList<> (historySkuList.size ());
            //将库存改了的放到待更新集合中
            for (ProductSku historySku : historySkuList) {
                for (ProductSkuSimpleVo reqSku : requestSkus) {
                    if (reqSku.getSizeId ().equals (historySku.getSizeId ())
                            && reqSku.getColorId ().equals (historySku.getColorId ())
                            && ! reqSku.getRemainCount ().equals (historySku.getRemainCount ())) {
                        //颜色,尺码相等,但是库存不同, 放到待更新的集合中
                        ProductSku productSku = new ProductSku ();
                        productSku.setId (historySku.getId ());
                        productSku.setRemainCount (reqSku.getRemainCount ());
                        waitModifySkus.add (productSku);
                        break;
                    }
                }
            }


            //更新
            logger.info ("更新库存 shopProductId={},modifySku={}", id, waitModifySkus);
            if (! waitModifySkus.isEmpty ()) {
                for (ProductSku modifySku : waitModifySkus) {
                    int rec = productSkuDao.updRemainCountById (modifySku);
                    if (rec != 1) {
                        logger.error ("更新库存失败 skuId={}", modifySku.getId ());
                        ErrorHelper.declare (FALSE, "更新库存失败");
                    }
                }
            }

        }
        else {
            //草稿商品, sku信息在草稿中

            ShopProductTempInfo tempInfo = shopProductTempInfoDao.findByShopProductId (id);
            ErrorHelper.declareNull (tempInfo, "没有找到草稿记录");

            shopProductTempInfoDao.updateRemainCount (tempInfo, requestSkus);
        }

    }


    /**
     * 商品sku列表
     *
     * @param storeId       门店id
     * @param shopProductId 商品id
     * @return java.util.List
     * @author Charlie
     * @date 2018/11/27 19:39
     */
    @Override
    public Map<String, Object> listSkuByShopProductId(Long storeId, Long shopProductId) {
        logger.info ("查询商品sku storeId={},shopProductId={}", storeId, shopProductId);
        ShopProduct shopProduct = shopProductDao.findById (shopProductId, storeId);
        ErrorHelper.declareNull (shopProduct, "没有找到商品信息");

        Map<String, Object> result = new HashMap<> (2);

        if (ShopProductOwnEnum.SUPPLIER_PRODUCT.isThis (shopProduct.getOwn ())) {
            //平台供应商商品,不允许编辑sku
            result.put ("isCanEditSku", Boolean.FALSE);
            return result;
        }


        //供应商商品不允许编辑sku
        logger.info ("商品类型 own={}", shopProduct.getOwn ());
        List<ProductSkuSimpleVo> skuResult;
        if (ShopProductOwnEnum.SUPPLIER_PRODUCT.isThis (shopProduct.getOwn ())) {
            //平台供应商商品,不允许查询sku
            result.put ("isCanEditSku", Boolean.FALSE);
            return result;
        }


        //商品是否参加活动, 参加活动, 不允许编辑sku
//        Map<String, Object> activity = shopActivityRpcService.recentlyShopProductActivity (null, null, shopProductId);
//        ErrorHelper.declareNull (activity, "网络异常");
//        Integer activityType = Integer.parseInt (activity.get ("type").toString ());
//        logger.info ("查询商品sku, 商品是否参加活动 activity={}", activityType);
//        if (activityType > 0) {
//            result.put ("isCanEditSku", Boolean.FALSE);
//            return result;
//        }


        result.put ("isCanEditSku", Boolean.TRUE);


        //查询sku 自营商品
        if (ObjectUtils.nullSafeEquals (0L, shopProduct.getFirstTimeOnSale ())) {
            //没有发布过,在临时表获取
            ShopProductTempInfo tempInfo = shopProductTempInfoDao.findByShopProductId (shopProductId);
            if (tempInfo == null || StringUtils.isBlank (tempInfo.getSkuJson ())) {
                skuResult = EmptyEnum.list ();
            }
            else {
                skuResult = JSONObject.parseObject (tempInfo.getSkuJson (), new TypeReference<List<ProductSkuSimpleVo>> (){});
            }
        }
        else {
            //查询sku表
            ProductSkuQuery query = new ProductSkuQuery ();
            query.setStatusList (Arrays.asList (- 1, 0));
            query.setOwnType (2);
            query.setWxaProductId (shopProductId);
            List<ProductSku> skuList = productSkuMapper.selectList (query);
            if (ObjectUtils.isEmpty (skuList)) {
                skuResult = EmptyEnum.list ();
            }
            else {
                //组装vo
                skuResult = new ArrayList<> (skuList.size ());
                for (ProductSku sku : skuList) {
                    ProductSkuSimpleVo vo = new ProductSkuSimpleVo ();
                    vo.setColorId (sku.getColorId ());
                    vo.setColorName (sku.getColorName ());
                    vo.setSizeId (sku.getSizeId ());
                    vo.setSizeName (sku.getSizeName ());
                    vo.setStatus (sku.getStatus ());
                    vo.setId (sku.getId ());
                    vo.setRemainCount (sku.getRemainCount ());
                    skuResult.add (vo);
                }
            }
        }

        result.put ("dataList", skuResult);
        result.put ("shopProductId", shopProductId);
        return result;
    }


    /**
     * 批量删除
     *
     * @param request request
     * @author Charlie
     * @date 2018/11/27 17:05
     */
    private void batchDelete(ShopProductQuery request) {

        Long storeId = request.getStoreId ();

        if (ObjectUtils.isEmpty (request.getIds ())) {
            logger.info ("批量删除, 没有id");
            return;
        }

        List<Long> ids = request.getIds ();
        List<ShopProduct> shopProductList = shopProductDao.findByIds (ids, storeId);
        logger.info ("批量删除 ids={},querySize={}", ids, shopProductList.size ());
        if (shopProductList.isEmpty ()) {
            logger.info ("批量删除, 没有商品信息");
            return;
        }


        //商品参与活动,不能删除
        for (ShopProduct shopProduct : shopProductList) {
            Long id = shopProduct.getId ();
            Map<String, Object> activity = shopActivityRpcService.recentlyShopProductActivity (null, null, id);
            ErrorHelper.declareNull (activity, "网络异常");
            Integer activityType = Integer.parseInt (activity.get ("type").toString ());
            logger.info ("查询商品sku, 商品是否参加活动 activity={}", activityType);
            if (activityType > 0) {
                logger.info ("商品id={],正在参加活动", id);
                ErrorHelper.declare (FALSE, "商品(" + shopProduct.getName () + ")正在参与活动中,不允许删除操作");
            }
        }


        //批量删除
        Long current = System.currentTimeMillis ();
        for (ShopProduct shopProduct : shopProductList) {
            //下架
            ShopProduct updInfo = new ShopProduct ();
            updInfo.setUpdateTime (current);
            updInfo.setId (shopProduct.getId ());
            updInfo.setStatus (- 1);
            int rec = shopProductDao.updateById (updInfo);
            ErrorHelper.declare (rec == 1, "商品上架失败");
        }

        //通知购物车商品已失效
        for (ShopProduct shopProduct : shopProductList) {
            shopGoodsCarService.adviceGoodsCarThisProductHasDisabled (shopProduct.getId (), storeId);
        }

    }


    /**
     * 批量下架
     *
     * @param request request
     * @author Charlie
     * @date 2018/11/27 17:00
     */
    private void batchPutOffSale(ShopProductQuery request) {
        ErrorHelper.declare (request != null && ! ObjectUtils.isEmpty (request.getIds ()), "没有更新的商品信息");
        List<ShopProduct> shopProductList = shopProductDao.findByIds (request.getIds (), request.getStoreId ());

        if (shopProductList.isEmpty ()) {
            //如果没查到 skip
            logger.info ("批量下架, 没有商品信息");
            return;
        }


        for (ShopProduct shopProduct : shopProductList) {
            ErrorHelper.declare (shopProduct.getSoldOut () != 0, "草稿中商品不允许下架操作");
        }


        //商品参与活动,不能下架
        for (ShopProduct shopProduct : shopProductList) {
            Long id = shopProduct.getId ();
            Map<String, Object> activity = shopActivityRpcService.recentlyShopProductActivity (null, null, id);
            ErrorHelper.declareNull (activity, "网络异常");
            Integer activityType = Integer.parseInt (activity.get ("type").toString ());
            logger.info ("查询商品sku, 商品是否参加活动 activity={}", activityType);
            if (activityType > 0) {
                logger.info ("商品id={],正在参加活动", id);
                ErrorHelper.declare (FALSE, "商品(" + shopProduct.getName () + ")正在参与活动中,不允许下架操作");
            }
        }


        //批量下架
        long current = System.currentTimeMillis ();
        for (ShopProduct shopProduct : shopProductList) {
            //下架
            ShopProduct updInfo = new ShopProduct ();
            updInfo.setGroundTime (0L);
            updInfo.setTopTime (0L);
            updInfo.setSoldOut (2);
            updInfo.setUpdateTime (current);
            updInfo.setId (shopProduct.getId ());
            int rec = shopProductDao.updateById (updInfo);
            ErrorHelper.declare (rec == 1, "商品上架失败");
        }

    }


    /**
     * 商品批量上架
     *
     * @param request request
     * @author Charlie
     * @date 2018/11/27 13:45
     */
    private void batchPutOnSale(ShopProductQuery request) {
        ErrorHelper.declare (request != null && ! ObjectUtils.isEmpty (request.getIds ()), "没有更新的商品信息");
        List<Long> ids = request.getIds ();
        List<Long> list = shopProductMapper.selectShopProductId(ids, request.getStoreId());
        if (list.size()>0){
            ErrorHelper.declare (list.size()==0, "上架失败,存在已上架的商品");
        }
        ShopProductQuery query = new ShopProductQuery ();
        query.setIds (request.getIds ());
        query.setStatus (StateEnum.NORMAL);
        query.setStoreId (request.getStoreId ());
        List<Map<String, Object>> shopProductList = shopProductMapper.productDetailList (query);
        logger.info ("查询商品 ids.len={}, shopProductList.len={}", ids.size (), shopProductList.size ());

        //供应商同款的商品,必须是上架状态
        for (Map<String, Object> product : shopProductList) {
            Integer own = (Integer) product.get ("own");
            Integer soldOut = (Integer) product.get ("soldOut");
            ErrorHelper.declare (soldOut != 0, "草稿中商品不允许直接上架操作");
            if (ShopProductOwnEnum.SUPPLIER_PRODUCT.isThis (own)) {
                Integer productState = (Integer) product.get ("productState");
                ErrorHelper.declare (ObjectUtils.nullSafeEquals (6, productState), "平台未上架此商品 " + product.get ("name"));
            }
        }
        //上架
        long current = System.currentTimeMillis ();
        for (Map<String, Object> product : shopProductList) {
            Long id = (Long) product.get ("id");
            ShopProduct updInfo = new ShopProduct ();
            updInfo.setGroundTime (current);
            updInfo.setUpdateTime (current);
            updInfo.setSoldOut (1);
            updInfo.setId (id);
            int rec = shopProductDao.updateById (updInfo);
            ErrorHelper.declare (rec == 1, "商品上架失败");
        }
    }

}
