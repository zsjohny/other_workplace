package com.jiuy.rb.service.impl.product;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.base.annotation.NoNull;
import com.jiuy.base.annotation.Nullable;
import com.jiuy.base.enums.EMPTY;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.Query;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ConstMy;
import com.jiuy.base.util.HttpUtils;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.product.*;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.product.*;
import com.jiuy.rb.model.user.StoreBusinessRb;
import com.jiuy.rb.service.account.IShareService;
import com.jiuy.rb.service.common.ICacheService;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.product.ISalesVolumeService;
import com.jiuy.rb.service.product.IShopGoodsCarService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuy.rb.service.user.IUserService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.Empty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 小程序商品的业务相关
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 17:52
 * @Copyright 玖远网络
 */
@Service( "shopProductServiceRb" )
public class ShopProductServiceImpl implements IShopProductService{

    /**
     * sku所属商品类型,门店小程序用户的sku
     */
    private static final int STORE_SKU = 2;
    static Logger logger = LoggerFactory.getLogger (ShopProductServiceImpl.class);


    /**
     * 团购活动成团:件数成团
     */
    private static final int TEAM_CONDITION_PRODUCT = 2;
    /**
     * 团购活动库存存活时间
     */
    private static final int TEAM_INVENTORY_SURVIVAL_TIME = 60 * 60 * 24 * 15;
    /**
     * 秒杀活动库存存活时间
     */
    private static final int SECOND_INVENTORY_SURVIVAL_TIME = 60 * 60 * 24 * 15;


    /**
     * 活动状态  正常
     */
    private static final int STATUS_NORMAL = 0;

    @Autowired
    private ShopProductRbMapper shopProductRbMapper;
    @Autowired
    private TeamBuyActivityRbMapper teamBuyActivityRbMapper;
    @Autowired
    private SecondBuyActivityRbMapper secondBuyActivityRbMapper;
    @Autowired
    private PropertyValueRbMapper propertyValueMapper;
    @Autowired
    private ProductSkuRbNewMapper productSkuMapper;
    @Autowired
    private ShopTagRbMapper shopTagRbMapper;
    @Autowired
    private ShopTagProductRbMapper shopTagProductMapper;
    @Autowired
    private ShopProductRbTempInfoMapper shopProductRbTempInfoMapper;

    @Resource( name = "productService" )
    private IProductService productService;

    @Resource( name = "shareService" )
    private IShareService shareService;

    @Resource( name = "dataDictionaryServiceRb" )
    private IDataDictionaryService dataDictionaryService;
    @Resource( name = "userService" )
    private IUserService userService;
    @Resource( name = "cacheService" )
    private ICacheService cacheService;
    @Resource( name = "shopGoodsCarServiceImpl" )
    private IShopGoodsCarService shopGoodsCarService;
    @Resource( name = "salesVolumeService" )
    private ISalesVolumeService salesVolumeService;



    /**
     * 通过id获取某个小程序商品
     *
     * @param productId productId
     * @return com.jiuy.rb.model.product.ShopProductRb
     * @author Aison
     * @date 2018/7/5 17:52
     */
    @Override
    public ShopProductRb getById(Long productId) {

        return shopProductRbMapper.selectByPrimaryKey (productId);
    }

    /**
     *描述 通过ids获取小程序商品列表
     ** @param ids  productId
     * @author hyq
     * @date 2018/8/14 9:07
     * @return java.util.List<com.jiuy.rb.model.product.ShopProductRb>
     */
    @Override
    public List<ShopProductRb> selectByIds(List<String> ids) {
        return shopProductRbMapper.selectByIds(ids);
        //return new MyPage<> (shopProductRbMapper.selectByIds(ids));
    }

    /**
     *描述 通过ids获取小程序商品列表
     ** @param query  query
     * @author hyq
     * @date 2018/8/14 9:07
     * @return java.util.List<com.jiuy.rb.model.product.ShopProductRb>
     */
    @Override
    public List<ShopProductRb> selectByIds(ShopProductRbQuery query) {
        return shopProductRbMapper.selectByIds(query);
        //return new MyPage<> (shopProductRbMapper.selectByIds(ids));
    }


    /**
     * 查询小程序商品列表
     *
     * @param query query
     * @return com.jiuy.base.model.MyPage<com.jiuy.rb.model.product.ShopProductRb>
     * @author Aison
     * @date 2018/7/6 13:58
     */
    @Override
    public MyPage<ShopProductRb> getShopProductList(ShopProductRbQuery query) {

        return new MyPage<> (shopProductRbMapper.selectList (query));
    }

    /**
     * 分享
     *
     * @param page query
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author Aison
     * @date 2018/7/6 15:12
     */
    @Override
    public Page<Map<String, Object>> getShareProduct(Page<Map<String, Object>> page) {


        List<Map<String, Object>> datas = page.getRecords ();

        List<Map<String, Object>> products = new ArrayList<> ();

        String empty = "[]";
        String priceKey = "price";
        List<String> ids = new ArrayList<> ();
        Map<String,String>  storeIdMap = new HashMap<>();
        datas.forEach (action -> {
            Object price = action.get ("price");
            Map<String, Object> map = new HashMap<> (8);
            Object img = action.get ("summaryImages");
            map.put ("id", action.get ("id"));
            map.put ("name", action.get ("name"));
            map.put ("price", price == null ? null : new BigDecimal (price.toString ()));
            map.put ("summaryImages", img);
            map.put ("productId", action.get ("productId"));
            map.put ("activityType", action.get ("activityType"));
            if (img == null || empty.equals (img) || price == null) {
                ids.add (action.get ("productId").toString ());
                map.put ("failed", 1);
            }
            //获取storeId
            storeIdMap .put("storeId",action.get("storeId").toString());
            products.add (map);
        });
        ProductRbQuery query1 = new ProductRbQuery ();
        query1.setIds (ids);
        List<ProductRb> product = productService.getList (query1);
        Map<Long, ProductRb> productRbMap = new HashMap<> (20);
        product.forEach (action -> productRbMap.put (action.getId (), action));
        products.forEach (action -> {
            Integer failed = (Integer) action.get ("failed");
            if (failed != null && failed == 1) {
                Long productId = (Long) action.get ("productId");
                ProductRb productRb = productRbMap.get (productId);
                if (productRb != null) {
                    if (action.get (priceKey) == null) {
                        action.put ("price", new BigDecimal (productRb.getPrice ()));
                    }
                    String summaryImages = (String) action.get ("summaryImages");
                    if (summaryImages == null || empty.equals (summaryImages)) {
                        action.put ("summaryImages", productRb.getDetailImages ());
                    }
                }
            }
        });
        String storeId = storeIdMap.get("storeId");
        //俞姐姐优选 比率
        DataDictionaryRb storeIdValue =  dataDictionaryService.getByCode (ConstMy.YJJ_SHARE_STORE_ID, ConstMy.YJJ_SHARE_STORE_ID);
        DataDictionaryRb dd = dataDictionaryService.getByCode (ConstMy.SHARE_POINT_CODE, ConstMy.SHARE_POINT_GROUP);
        if (Long.parseLong(storeIdValue.getVal())==Long.parseLong(storeId)){
            dd = dataDictionaryService.getByCode (ConstMy.YJJ_SHARE_POINT_CODE, ConstMy.YJJ_SHARE_POINT_CODE);
        }
        Double bl = Double.valueOf (dd.getVal ());
        products.forEach (action -> {
            try {
                String summaryImages = (String) action.get ("summaryImages");
                JSONArray array = JSONArray.parseArray (summaryImages);
                action.put ("summaryImages", array.get (0));
                BigDecimal price = (BigDecimal) action.get ("price");
                BigDecimal commission = new BigDecimal (bl).multiply (price).setScale (2, BigDecimal.ROUND_HALF_DOWN);
                action.put ("commission", commission);
                action.remove ("failed");
            } catch (Exception e) {
                e.printStackTrace ();
            }
        });

        page.setRecords (products);
        return page;
    }


    /**
     * 获取排序后的团购活动列表
     * <p>
     * 排序优先顺序依次按条件：
     * 活动状态为进行中的，
     * 活动状态为待开始的，
     * 活动状态为已结束的，
     * 距离活动即将结束时间最近的，
     * 距离活动开始时间最近的，
     * 距离活动已结束最近的
     * </p>
     *
     * @param query query
     *              storeId, 分页参数必填
     * @author Charlie
     * @date 2018/7/29 19:49
     */
    @Override
    public MyPage<TeamBuyActivityRbQuery> listTeamBuyActivityWithOrder(TeamBuyActivityRbQuery query) {
//        Declare.noNullParams (query.getStoreId ());
//        StoreBusinessRb store = userService.getStoreBusinessById (query.getStoreId ());
//        Declare.notNull (store, GlobalsEnums.USER_NOT_FOUND);
//        if (! ObjectUtils.nullSafeEquals (store.getWxaType (), 1)) {
//            return MyPage.EMPTY;
//        }

        query.setGtActivityEndTime (System.currentTimeMillis ());
        return MyPage.copy2Child (
                teamBuyActivityRbMapper.listTeamBuyActivity (query),
                TeamBuyActivityRbQuery.class,
                (source, target) -> target.formatReadable (source)
        );
    }


    /**
     * 获取排序后的秒杀活动列表
     * <p>
     * 排序优先顺序依次按条件：
     * 活动状态为进行中的，
     * 活动状态为待开始的，
     * 活动状态为已结束的，
     * 距离活动即将结束时间最近的，
     * 距离活动开始时间最近的，
     * 距离活动已结束最近的
     * </p>
     *
     * @param query query
     * @author Charlie
     * @date 2018/7/29 19:49
     */
    @Override
    public MyPage<SecondBuyActivityRbQuery> listSecondBuyActivityWithOrder(SecondBuyActivityRbQuery query) {
        //企业版小程序才有秒杀和团购
//        Declare.noNullParams (query.getStoreId ());
//        StoreBusinessRb store = userService.getStoreBusinessById (query.getStoreId ());
//        Declare.notNull (store, GlobalsEnums.USER_NOT_FOUND);
//        if (! ObjectUtils.nullSafeEquals (store.getWxaType (), 1)) {
//            return MyPage.EMPTY;
//        }

        query.setGtActivityEndTime (System.currentTimeMillis ());
        return MyPage.copy2Child (
                secondBuyActivityRbMapper.listSecondBuyActivity (query),
                SecondBuyActivityRbQuery.class,
                (source, target) -> target.formatReadable (source)
        );
    }


    /**
     * 活动库存回滚
     * <p>
     * 取消订单
     * </p>
     *
     * @param activityId 活动id
     * @param kind       活动种类 {@link ShopActivityKindEnum}
     * @param count      释放库存数量
     * @author Charlie
     * @date 2018/8/3 18:02
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void releaseInventory(Long activityId, ShopActivityKindEnum kind, Integer count) {
        //当前做统一调度,以后活动可能用一张表
        Declare.noNullParams (activityId, kind, count);
        Declare.state (count > 0, GlobalsEnums.PARAM_ERROR);
        logger.info ("释放小程序活动库存 activityId[{}].activityName[{}].count[{}]", activityId, kind.getName (), count);
        switch (kind) {
            case TEAM:
                releaseTeamInventory (activityId, count);
                break;
            case SECOND:
                releaseSecondInventory (activityId, count);
                break;
            default:
                throw BizException.instance (GlobalsEnums.PARAM_ERROR);
        }
    }


    /**
     * 将活动库存放入缓存
     *
     * @param storeId    门店id
     * @param activityId 活动id
     * @param kind       {@link ShopActivityKindEnum}
     * @author Charlie
     * @date 2018/8/3 19:40
     */
    @Override
    public void putActivityInCacheIfNoExist(Long storeId, Long activityId, ShopActivityKindEnum kind) {
        Declare.noNullParams (storeId, activityId, kind);
        switch (kind) {
            case SECOND:
                putSecondInCacheIfNoExist (storeId, activityId);
                break;
            case TEAM:
                putTeamInCacheIfNoExist (storeId, activityId);
                break;
            default:
                Declare.state (false, GlobalsEnums.PARAM_ERROR);
        }
    }


    /**
     * 查询
     *
     * @param teamQuery teamQuery
     * @return com.jiuy.rb.model.product.TeamBuyActivityRb
     * @author Charlie
     * @date 2018/8/6 14:08
     */
    @Override
    public TeamBuyActivityRb selectOne(TeamBuyActivityRbQuery teamQuery) {
        return teamBuyActivityRbMapper.selectOne (teamQuery);
    }


    /**
     * 查询
     *
     * @param secondQuery secondQuery
     * @return com.jiuy.rb.model.product.TeamBuyActivityRb
     * @author Charlie
     * @date 2018/8/6 14:08
     */
    @Override
    public SecondBuyActivityRb selectOne(SecondBuyActivityRbQuery secondQuery) {
        return secondBuyActivityRbMapper.selectOne (secondQuery);
    }


    /**
     * 首页活动
     *
     * @param wxVersion 小程序版本号
     * @param storeId   门店id
     * @return 团购和秒杀活动
     * @author Charlie
     * @date 2018/8/7 10:23
     */
    @Override
    public Map<String, Object> homeActivity(String wxVersion, Long storeId) {
        Declare.noNullParams (storeId);
//        StoreBusinessRb store = userService.getStoreBusinessById (storeId);
//        Declare.existResource (store);

        Map<String, Object> result = new HashMap<> (2);
        //只有企业版才有活动
//        注释：默认所有都为企业 2018年12月25日 hyf
//        if (ObjectUtils.nullSafeEquals (0, store.getWxaType ())) {
//            result.put ("teamBuyActivity", EMPTY.map ());
//            result.put ("secondBuyActivity", EMPTY.map ());
//            return result;
//        }
        //团购活动
        result.put ("teamBuyActivity", homeFirstTeamActivity (storeId, wxVersion));
        //秒杀活动
        result.put ("secondBuyActivity", homeFirstSecondActivity (storeId, wxVersion));
        return result;
    }




    /**
     * 创建一个商品
     *
     * @param param param
     * @return com.jiuy.rb.model.product.ShopProductRb
     * @author Charlie
     * @date 2018/9/3 17:56
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopProductRb createShopProduct(ShopProductRbQuery param) {
        logger.info ("--创建小程序商品");
        Declare.notNull (param.getName (), GlobalsEnums.REQUIRED_PRODUCT_NAME);
        Declare.notNull (param.getPrice ()==null || param.getPrice ().compareTo (new BigDecimal ("0"))>0, GlobalsEnums.REQUIRED_PRODUCT_PRICE );
        //价格必填
        Declare.notNull (param.getPrice (), GlobalsEnums.REQUIRED_PRODUCT_PRICE);
        Declare.notNull (param.getPrice ().compareTo (BigDecimal.ZERO) > 0, GlobalsEnums.REQUIRED_PRODUCT_PRICE);
        //sku必填
        List<ProductSkuRbNew> skuList = json2Sku (param.getSkuJsonArray ());
        Declare.notNull (skuList.size () > 0, GlobalsEnums.REQUIRED_PRODUCT_SKU);
        verifySkuExist (skuList);
        //其他信息
        Declare.notNull (param.getStoreId (), GlobalsEnums.ACCOUNT_USER_ID_IS_NULL);
        Declare.notNull (param.getOwn (), GlobalsEnums.UNKNOWN_PRODUCT_TYEP);
        Declare.notNull (param.getName (), GlobalsEnums.REQUIRED_PRODUCT_NAME);
        Declare.notNull (param.getClothesNumber (), GlobalsEnums.REQUIRED_PRODUCT_CLOTHES_NUMBER);

        //标签信息(验证标签是否存在)
        List<ShopTagRb> shopTagList = findShopTagByIds (param.getTagIds ());
        //创建商品
        ShopProductRb newProduct = doInsertShopProduct (param);

        //记录sku信息,在最后上架时才创建sku
        ShopProductRbTempInfo tempInfo = ShopProductRbTempInfoQuery.build (newProduct.getId (), param.getSkuJsonArray ());
        shopProductRbTempInfoMapper.insert (tempInfo);

        //初始化标签
        doInsertShopTag (newProduct, shopTagList);

        return newProduct;
    }

    private void verifySkuExist(List<ProductSkuRbNew> skuList) {
        if (ObjectUtils.isEmpty (skuList)) {
            Declare.state (false, GlobalsEnums.AT_LEAST_ONE_SKU_ON_SALE);
        }
        //至少一个上架
        boolean hasOneOnSale = atLeastOnSkuOnSale (skuList);
        Declare.state (hasOneOnSale, GlobalsEnums.AT_LEAST_ONE_SKU_ON_SALE);

        Set<Long> colorIds = new HashSet<> (skuList.size ());
        Set<Long> sizeIds = new HashSet<> (skuList.size ());
        skuList.forEach (sku->{
            colorIds.add (sku.getColorId ());
            sizeIds.add (sku.getSizeId ());
        });
        //查询颜色
        logger.info ("查询颜色 colorIds[{}]", colorIds);
        PropertyValueRbQuery pvQuery = new PropertyValueRbQuery ();
        pvQuery.setIds (new ArrayList<> (colorIds));
        List<PropertyValueRb> colorList = propertyValueMapper.selectList (pvQuery);
        Declare.state (colorIds.size () == colorList.size (), GlobalsEnums.NO_FOUND_COLOR);

        //查询尺码
        logger.info ("查询尺码 sizeIds[{}]", sizeIds);
        pvQuery.setIds (new ArrayList<> (sizeIds));
        List<PropertyValueRb> sizeList = propertyValueMapper.selectList (pvQuery);
        Declare.state (sizeIds.size () == sizeList.size (), GlobalsEnums.NO_FOUND_SIZE);
    }


    /**
     * 校验sku至少一个上架
     *
     * @param skuList skuList
     * @return boolean
     * @author Charlie
     * @date 2018/9/11 9:15
     */
    private boolean atLeastOnSkuOnSale(List<ProductSkuRbNew> skuList) {
        int count = 0;
        boolean hasOneOnSale = false;
        for (ProductSkuRbNew sku : skuList) {
            if (ObjectUtils.nullSafeEquals (sku.getStatus (), 0)) {
                int c = sku.getRemainCount () == null ? 0 : sku.getRemainCount ();
                count += c;
                hasOneOnSale = true;
            }
        }
        return hasOneOnSale && count > 0 ;
    }


    /**
     * json转换sku对象
     *
     * @param skuJsonArray skuJsonArray
     * @return java.util.List<com.jiuy.rb.model.product.ProductSkuRbNew>
     * @author Charlie
     * @date 2018/9/4 18:28
     */
    private List<ProductSkuRbNew> json2Sku(String skuJsonArray) {
        if (StringUtils.isBlank (skuJsonArray)) {
            return Empty.list ();
        }

        List<ProductSkuRbNew> skuList;
        try {
            skuList = Biz.jsonStrToListObject (skuJsonArray, List.class, ProductSkuRbNew.class);
        } catch (Exception e) {
            logger.error ("sku1 json转化异常 skuJsonArray[{}]", skuJsonArray);
            throw BizException.instance (GlobalsEnums.UNKNOWN_SKU);
        }
        Declare.state (!ObjectUtils.isEmpty (skuList), GlobalsEnums.REQUIRED_PRODUCT_SKU);
        return skuList;
    }

    /**
     * 编辑
     *
     * @param param param
     * @author Charlie
     * @date 2018/9/4 14:59
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editShopProduct(ShopProductRbQuery param) {
        //校验价格
        if (param.getPrice () != null) {
            Declare.state (param.getPrice ().compareTo (BigDecimal.ZERO) > 0, GlobalsEnums.REQUIRED_PRODUCT_PRICE);
            param.setPrice (param.getPrice ());
        }

        ShopProductRb history = shopProductRbMapper.selectByPrimaryKey (param.getId ());
        Declare.notNull (history, GlobalsEnums.NO_PRODUCT_PRODUCT);

        //校验商品类型
        Integer own = param.getOwn ();
        if (own != null) {
            Declare.state (ShopProductOwnEnum.SELF_SAMPLE_STYLE.isThis (own) || ShopProductOwnEnum.SELF_CUSTOM.isThis (own),
                    GlobalsEnums.UNKNOWN_PRODUCT_TYEP);
            //上架过的商品,不允许更改商品类型
            if (history.getFirstTimeOnSale () > 0 && !history.getOwn ().equals (own)) {
                Declare.state (false, GlobalsEnums.CANNOT_UPDATE_SHOP_TYPE);
            }
        }


        //校验sku
        if (StringUtils.isNotBlank (param.getSkuJsonArray ())) {
            List<ProductSkuRbNew> skuList = json2Sku (param.getSkuJsonArray ());
            Declare.notNull (skuList.size () > 0, GlobalsEnums.REQUIRED_PRODUCT_SKU);
            verifySkuExist (skuList);
        }


        if (history.getFirstTimeOnSale () == 0 && ObjectUtils.nullSafeEquals (param.getSoldOut (), 1)) {
            logger.info ("更新商品信息, 商品首次上架,初始化sku,初始化标签, 批量新增sku");
            //初始化sku
            List<ProductSkuRbNew> skuList = extractSuk(history, param.getSkuJsonArray ());
            boolean hasOneOnSale = atLeastOnSkuOnSale (skuList);
            Declare.state (hasOneOnSale, GlobalsEnums.AT_LEAST_ONE_SKU_ON_SALE);
            doBatchInsertSku (history, skuList);
            deleteShopProductTempSku (history.getId ());
            //商品首次上架时间
            param.setFirstTimeOnSale (System.currentTimeMillis ());
        }
        else if (history.getFirstTimeOnSale () == 0) {
            //商品不是首次,草稿状态 ignore
            logger.info ("更新商品信息, 商品未上架过");
            if (StringUtils.isNotBlank (param.getSkuJsonArray ())) {
                ShopProductRbTempInfoQuery query = new ShopProductRbTempInfoQuery ();
                query.setStatus (1);
                query.setShopProductId (history.getId ());
                ShopProductRbTempInfo tempInfo = shopProductRbTempInfoMapper.selectOne (query);
                logger.info ("更新商品tempInfoId[{}]", tempInfo.getId ());
                Declare.existResource (tempInfo);
                if (!ObjectUtils.nullSafeEquals (tempInfo.getSkuJson (), param.getSkuJsonArray ())) {
                    tempInfo.setSkuJson (param.getSkuJsonArray ());
                    shopProductRbTempInfoMapper.updateByPrimaryKeySelective (tempInfo);
                }
            }
        }
        else {
            logger.info ("更新商品信息, 商品上过架, 需要更新sku, 更新标签");
            //非首次上架, 更新sku
            if (StringUtils.isNotBlank (param.getSkuJsonArray ())) {
                List<ProductSkuRbNew> newSkuList = json2Sku (param.getSkuJsonArray ());
                synchronizationSampleProductSku (history, newSkuList);
            }
        }
        //更新标签
        synchronizationShopProductTagIds (history, param.getTagIds ());

        //每次上架,刷新上架时间
        if (ObjectUtils.nullSafeEquals (param.getSoldOut (), 1)) {
            param.setGroundTime (System.currentTimeMillis ());
        }
        int rec = shopProductRbMapper.updateByPrimaryKeySelective (param);
        logger.info ("更新商品信息 shopProductId[{}].rec[{}]", history.getId (), rec);
    }




    /**
     * 查询商品详情
     *
     * @param shopProductId shopProductId
     * @param storeId storeId
     * @return com.jiuy.rb.model.product.ShopProductRbQuery
     * @author Charlie
     * @date 2018/9/8 22:19
     */
    @Override
    public ShopProductRbQuery queryProductDetail(Long shopProductId, Long storeId) {
        Declare.noNullParams (shopProductId, storeId);
        ShopProductRbQuery productQuery = new ShopProductRbQuery ();
        productQuery.setId (shopProductId);
        productQuery.setStoreId (storeId);
        ShopProductRbQuery shopProduct = shopProductRbMapper.selectQuery (productQuery);
        Declare.notNull (shopProduct, GlobalsEnums.NO_PRODUCT_PRODUCT);

        //组装标签
        ShopTagProductRbQuery tagProductQuery = new ShopTagProductRbQuery ();
        tagProductQuery.setShopProductId (shopProductId);
        tagProductQuery.setStoreId (storeId);
        List<ShopTagProductRb> shopTagProductRbList = shopTagProductMapper.selectList (tagProductQuery);
        if (ObjectUtils.isEmpty (shopTagProductRbList)) {
            shopProduct.setShopTagRbList (EMPTY.list ());
        }
        else {
            ShopTagRbQuery tagQuery = new ShopTagRbQuery ();
            List<Long> tagIds = new ArrayList<> (shopTagProductRbList.size ());
            for (ShopTagProductRb tagProductRb : shopTagProductRbList) {
                tagIds.add (tagProductRb.getTagId ());
            }
            tagQuery.setIds (tagIds);
            tagQuery.setStatus (0);
            List<ShopTagRb> shopTagRbs = shopTagRbMapper.selectList (tagQuery);
            for (ShopTagRb tagRb : shopTagRbs) {
                tagRb.setStoreId (null);
            }
            shopProduct.setShopTagRbList (shopTagRbs);
        }

        //组装skuJson
        Long firstTimeOnSale = shopProduct.getFirstTimeOnSale ();
        if (firstTimeOnSale == null || firstTimeOnSale == 0) {
            //如果商品没有上架过,在临时表中获取信息
            ShopProductRbTempInfoQuery tempInfoQuery = new ShopProductRbTempInfoQuery ();
            tempInfoQuery.setShopProductId (shopProductId);
            tempInfoQuery.setStatus (1);
            ShopProductRbTempInfo tempInfo = shopProductRbTempInfoMapper.selectOne (tempInfoQuery);
            if (Biz.isEmpty (tempInfo)) {
                shopProduct.setSkuList (EMPTY.list ());
            }
            else {
                shopProduct.setSkuList (Biz.jsonStrToListObject (tempInfo.getSkuJson (), List.class, ProductSkuRbSimpleVo.class));
            }
        }
        else {
            //如果商品上架过,直接查
            List<ProductSkuRbNew> skuList = listSkuByShopProductId (shopProductId);
            if (ObjectUtils.isEmpty (skuList)) {
                shopProduct.setSkuList (EMPTY.list ());
            }
            else {
                List<ProductSkuRbSimpleVo> simpleVoList = new ArrayList<> (skuList.size ());
                for (ProductSkuRbNew sku : skuList) {
                    ProductSkuRbSimpleVo vo = ProductSkuRbSimpleVo.build (sku);
                    simpleVoList.add (vo);
                }
                shopProduct.setSkuList (simpleVoList);
            }
        }

        //颜色尺码
        List<ProductSkuRbSimpleVo> skuList = shopProduct.getSkuList ();
        if (skuList.isEmpty ()) {
            shopProduct.setColorIdName (EMPTY.map ());
            shopProduct.setSizeIdName (EMPTY.map ());
        }
        else {
            Map<Long, String> colorMap = new HashMap<> ();
            Map<Long, String> sizeMap = new HashMap<> ();
            for (ProductSkuRbSimpleVo sku : skuList) {
                colorMap.put (sku.getColorId (), sku.getColorName ());
                sizeMap.put (sku.getSizeId (), sku.getSizeName ());
            }
            shopProduct.setColorIdName (colorMap);
            shopProduct.setSizeIdName (sizeMap);
        }
        shopProduct.setStoreId (null);

        //兼容
        shopProduct.setVideoUrl (shopProduct.getVideoDisplayUrl ());
        shopProduct.setVideoFileId (shopProduct.getVideoDisplayFileid ());
        shopProduct.setVideoImage (shopProduct.getVideoDisplayImage ());
        return shopProduct;
    }


    /**
     * 查询供应商商品信息,已小程序商品信息的格式返回
     *
     * @param productId productId
     * @return ShopProductRbQuery
     * @author Charlie
     * @date 2018/9/12 15:33
     */
    @Override
    public ShopProductRbQuery supplierProduct2ShopProduct(Long productId) {
        ProductRb product = productService.getById (productId);
        Declare.notNull (product, GlobalsEnums.NO_PRODUCT_PRODUCT);
        Declare.state (ProductStateEnum.UP_SOLD.isThis (product.getState ()), GlobalsEnums.PRODUCT_STATE_ERROR);
        Declare.state (product.getStatus () == 0, GlobalsEnums.PRODUCT_STATE_ERROR);
        Declare.state (product.getDelState () == 0, GlobalsEnums.PRODUCT_STATE_ERROR);
        //组装商品信息
        ShopProductRbQuery pAdpt = new ShopProductRbQuery ();
        pAdpt.setProductId (productId);
        String firstImg = acquireFirstImg (product);
        pAdpt.setSummaryImages (firstImg);
        pAdpt.setShopTagRbList (EMPTY.list ());
        pAdpt.setFirstTimeOnSale (0L);
        pAdpt.setVideoImage (product.getVedioMain ());
        pAdpt.setPrice (new BigDecimal (String.valueOf (product.getWholeSaleCash ())));
        pAdpt.setVideoUrl (product.getVideoUrl ());
        pAdpt.setVideoFileId (product.getVideoFileId ());
        pAdpt.setOwn (ShopProductOwnEnum.SELF_SAMPLE_STYLE.getCode ());
        pAdpt.setClothesNumber (product.getClothesNumber ());
        pAdpt.setName (product.getName ());
        //适配商品描述
        List<String> imgs =  Biz.jsonStrToListObject (product.getSummaryImages (), List.class, String.class);
        List<Map<String, Object>> imgAdaptor = new ArrayList<> (imgs.size ());
        for (String img : imgs) {
            HashMap<String, Object> map = new HashMap<> ();
            map.put ("content", img);
            map.put ("type", 0);
            map.put ("width", 0);
            map.put ("height", 0);
            imgAdaptor.add (map);
        }
        pAdpt.setShopOwnDetail (Biz.obToJson (imgAdaptor));
        //组装sku信息
        ProductSkuRbNewQuery skuQuery = new ProductSkuRbNewQuery ();
        skuQuery.setProductId (productId);
        skuQuery.setStatus (0);
        skuQuery.setOwnType (1);
        List<ProductSkuRbNew> skuList = productSkuMapper.selectList (skuQuery);
        if (skuList.isEmpty ()) {
            pAdpt.setColorIdName (EMPTY.map ());
            pAdpt.setSizeIdName (EMPTY.map ());
            pAdpt.setSkuList (EMPTY.list ());
        }
        else {
            Map<Long, String> colorMap = new HashMap<> ();
            Map<Long, String> sizeMap = new HashMap<> ();
            List<ProductSkuRbSimpleVo> skuVoList = new ArrayList<> (skuList.size ());
            for (ProductSkuRbNew sku : skuList) {
                colorMap.put (sku.getColorId (), sku.getColorName ());
                sizeMap.put (sku.getSizeId (), sku.getSizeName ());

                ProductSkuRbSimpleVo vo = new ProductSkuRbSimpleVo ();
                //默认是0,给前端展示0
                vo.setRemainCount (0);
                //默认下架,不选中
                vo.setStatus (-1);
                vo.setSizeName (sku.getSizeName ());
                vo.setSizeId (sku.getSizeId ());
                vo.setColorName (sku.getColorName ());
                vo.setColorId (sku.getColorId ());
                skuVoList.add (vo);
            }
            pAdpt.setColorIdName (colorMap);
            pAdpt.setSizeIdName (sizeMap);
            pAdpt.setSkuList (skuVoList);
        }
        return pAdpt;
    }


    /**
     * 获取商品主图
     *
     * @param product product
     * @return java.lang.String
     * @author Charlie
     * @date 2018/9/13 16:35
     */
    private String acquireFirstImg(ProductRb product) {
        if (StringUtils.isNotBlank (product.getDetailImages ())) {
            List<String> imgs = Biz.jsonStrToListObject (product.getDetailImages (), List.class, String.class);
            if (! ObjectUtils.isEmpty (imgs)) {
                if (imgs.size () > 4) {
                    ArrayList<String> splitImgs = new ArrayList<> (4);
                    for (int i = 0; i < imgs.size (); i++) {
                        if (i >= 4) {
                            break;
                        }
                        splitImgs.add (imgs.get (i));
                    }
                    return Biz.obToJson (splitImgs);
                }
                return Biz.obToJson (imgs);
            }
        }
        return null;
    }

    @Override
    public List<ShopProductRb> selectList(ShopProductRbQuery shopProductRbQuery) {
        return shopProductRbMapper.selectList (shopProductRbQuery);
    }


    /**
     * 删除商品临时表的sku记录
     *
     * @param shopProductId shopProductId
     * @author Charlie
     * @date 2018/9/6 13:35
     */
    private void deleteShopProductTempSku(Long shopProductId) {
        int rec = shopProductRbTempInfoMapper.deleteByShopProductId (shopProductId);
        logger.info ("删除商品sku临时表的sku记录 shopProductId[{}].rec[{}]", shopProductId, rec);
    }

    /**
     * 提取sku
     *
     * @param history history
     * @param skuJsonArray sku,如果有,则以skuJsonArray为主, 没有则通过商品去查
     * @return java.util.List<com.jiuy.rb.model.product.ProductSkuRbNew>
     * @author Charlie
     * @date 2018/9/6 13:29
     */
    private List<ProductSkuRbNew> extractSuk(ShopProductRb history, String skuJsonArray) {
        logger.info ("提取sku shopProduct = [" + history.getId () + "], skuJsonArray = [" + skuJsonArray + "]");
        String json;
        if (StringUtils.isNotBlank (skuJsonArray)) {
            json = skuJsonArray;
        }
        else {
            ShopProductRbTempInfoQuery query = new ShopProductRbTempInfoQuery ();
            query.setShopProductId (history.getId ());
            query.setStatus (1);
            ShopProductRbTempInfo tempInfo = shopProductRbTempInfoMapper.selectOne (query);
            Declare.notNull (tempInfo, GlobalsEnums.NO_FOUND_SKU);
            json = tempInfo.getSkuJson ();
        }
        return json2Sku (json);
    }


    /**
     * 同步商品的标签新
     *
     * @param history history
     * @param tagIds tagIds
     * @author Charlie
     * @date 2018/9/4 17:49
     */
    private void synchronizationShopProductTagIds(ShopProductRb history, String tagIds) {
        //新的 标签信息
        List<ShopTagRb> newStList = StringUtils.isEmpty (tagIds)? EMPTY.list () : findShopTagByIds (tagIds);
        Map<Long, ShopTagRb> newStMap = new HashMap<> (newStList.size ());
        for (ShopTagRb newSt : newStList) {
            newStMap.put (newSt.getId (), newSt);
        }
        //已有的标签信息
        ShopTagProductRbQuery query = new ShopTagProductRbQuery ();
        query.setStoreId (history.getStoreId ());
        query.setShopProductId (history.getId ());
        List<ShopTagProductRb> hisStList = shopTagProductMapper.selectList (query);
        //遍历集合,老的在新的map有的->remove,老的在新的没有的删除
        HashSet<Long> needDelStIds = new HashSet<> ();
        for (ShopTagProductRb hisSt : hisStList) {
            ShopTagRb shopTagRb = newStMap.remove (hisSt.getTagId ());
            if (Biz.isEmpty (shopTagRb)) {
                needDelStIds.add (hisSt.getId ());
            }
        }
        //删除
        for (Long id : needDelStIds) {
            shopTagProductMapper.deleteByPrimaryKey (id);
        }

        //剩下的是需要新增的
        Collection<ShopTagRb> needAddSt = newStMap.values ();
        doInsertShopTag (history, needAddSt);
    }



    /**
     * 根据标签id获取标签
     *
     * @param tagIds tagIds
     * @return java.util.List<com.jiuy.rb.model.product.ShopTagRb>
     * @author Charlie
     * @date 2018/9/4 17:44
     */
    private List<ShopTagRb> findShopTagByIds(String tagIds) {
        if (StringUtils.isBlank (tagIds)) {
            return EMPTY.list ();
        }

        List<Long> tagIdList = Biz.split2List (tagIds, ",");
        List<ShopTagRb> shopTagList;
        if (!ObjectUtils.isEmpty (tagIdList)) {
            ShopTagRbQuery tagQuery = new ShopTagRbQuery ();
            tagQuery.setIds (tagIdList);
            shopTagList = shopTagRbMapper.selectList (tagQuery);
            Declare.state (shopTagList.size () == tagIdList.size (), GlobalsEnums.NO_FOUND_TAG);
            logger.info ("--创建小程序商品:标签信息合法 tagIdList[{}]", tagIdList);
        }
        else {
            logger.info ("--创建小程序商品:标签信息为空");
            shopTagList = EMPTY.list ();
        }
        return shopTagList;
    }








    /**
     * 供应商同款商品,同一个商品,更新sku信息
     *
     * @param history history
     * @param newSkuList newSkuList
     * @author Charlie
     * @date 2018/9/4 20:03
     */
    private void synchronizationSampleProductSku(ShopProductRb history, List<ProductSkuRbNew> newSkuList) {
        //已有的sku
        List<ProductSkuRbNew> hisSkuList = listSkuByShopProductId (history.getId ());
        Map<String, ProductSkuRbNew> hisMap = new HashMap<> (hisSkuList.size ());
        for (ProductSkuRbNew sku : hisSkuList) {
            hisMap.put (sku.getPropertyIds (), sku);
        }
        //新的sku信息
        Map<String, ProductSkuRbNew> newMap = new HashMap<> (hisSkuList.size ());
        for (ProductSkuRbNew sku : newSkuList) {
            newMap.put ("7:" + sku.getColorId () + ";8:" + sku.getSizeId (), sku);
        }

        //待删除的sku
        List<Long> needDeleteSkuIds = new ArrayList<> ();
        //待更新的sku
        List<ProductSkuRbNew> needUpdSkus = new ArrayList<> ();
        //将上架修改为下架的sku
        List<Long> skuLostSaleUp = new ArrayList<> ();
        long current = System.currentTimeMillis ();
        for (Map.Entry<String, ProductSkuRbNew> his : hisMap.entrySet ()) {
            ProductSkuRbNew newSku = newMap.remove (his.getKey ());
            ProductSkuRbNew hisSku = his.getValue ();
            if (newSku == null) {
                //待删除
                needDeleteSkuIds.add (hisSku.getId ());
            }
            else {
                //待修改
                if (! ObjectUtils.nullSafeEquals (newSku.getStatus (), hisSku.getStatus ()) || ! ObjectUtils.nullSafeEquals (newSku.getRemainCount (), hisSku.getRemainCount ())) {
                    hisSku.setStatus (newSku.getStatus ());
                    hisSku.setRemainCount (newSku.getRemainCount ());
                    hisSku.setUpdateTime (current);
                    needUpdSkus.add (hisSku);
                    if (hisSku.getStatus ().equals (0)) {
                        skuLostSaleUp.add (hisSku.getId ());
                    }

                }
            }
        }
        //待添加的sku
        List<ProductSkuRbNew> needAddSkus = new ArrayList<> (newMap.values ());

        if (! needDeleteSkuIds.isEmpty ()) {
            doBatchDeleteSku (needDeleteSkuIds, history.getStoreId ());
        }
        if (! needUpdSkus.isEmpty ()) {
            for (ProductSkuRbNew updSkus : needUpdSkus) {
                productSkuMapper.updateByPrimaryKey (updSkus);
            }
            logger.info ("更新sku信息");
            //设为下架状sku的购物车通知失效
            shopGoodsCarService.adviceGoodsCarSkuHasDisabled (skuLostSaleUp, history.getStoreId ());
        }
        if (! needAddSkus.isEmpty ()) {
            doBatchInsertSku (history, needAddSkus);
        }
    }




    /**
     * 批量删除sku
     *
     * @param skuIds skuIds
     * @param storeId storeId
     * @author Charlie
     * @date 2018/9/4 21:37
     */
    private void doBatchDeleteSku(List<Long> skuIds, Long storeId) {
        int rec = productSkuMapper.deleteShopProductSkuByIds (skuIds);
        logger.info ("批量删除sku skuIds[{}].rec[{}]", skuIds, rec);

        shopGoodsCarService.adviceGoodsCarSkuHasDisabled (skuIds, storeId);
    }


    /**
     * 删除商品的sku
     *
     * @param shopProduct shopProduct
     * @author Charlie
     * @date 2018/9/4 18:32
     */
    private void deleteSkuByShopProduct(ShopProductRb shopProduct) {
        Long shopProductId = shopProduct.getId ();
        List<ProductSkuRbNew> skuList = listSkuByShopProductId (shopProductId);
        if (skuList.isEmpty ()) {
            logger.info ("删除小程序商品sku, 商品没有sku");
            return;
        }
        List<Long> skuIds = new ArrayList<> (skuList.size ());
        for (ProductSkuRbNew sku : skuList) {
            skuIds.add (sku.getId ());
        }


        int rec = productSkuMapper.deleteShopProductSkuByShopProductId (shopProductId);
        logger.info ("删除小程序商品的sku shopProductId[{}].rec[{}]", shopProductId, rec);

        //通知购物车,sku失效
        shopGoodsCarService.adviceGoodsCarSkuHasDisabled (skuIds, shopProduct.getStoreId ());
    }



    /**
     * 查询小程序商品未删除的sku
     *
     * @param shopProductId shopProductId
     * @return java.util.List<com.jiuy.rb.model.product.ProductSkuRbNew>
     * @author Charlie
     * @date 2018/9/4 20:06
     */
    private List<ProductSkuRbNew> listSkuByShopProductId(Long shopProductId) {
        ProductSkuRbNewQuery query = new ProductSkuRbNewQuery ();
        query.setStatusList (Arrays.asList (-1,0));
        query.setOwnType (2);
        query.setWxaProductId (shopProductId);
        return productSkuMapper.selectList (query);
    }


    /**
     * 创建商品
     *
     * @param param param
     * @return com.jiuy.rb.model.product.ShopProductRb
     * @author Charlie
     * @date 2018/9/4 11:38
     */
    private ShopProductRb doInsertShopProduct(ShopProductRbQuery param) {
        logger.info ("--创建小程序商品:name[{}].own[{}]", param.getName (), param.getOwn ());
        ShopProductRb newProduct;
        if (ShopProductOwnEnum.SELF_SAMPLE_STYLE.isThis (param.getOwn ())) {
            //供应商同款,校验商品是否是否存在,是否上架
            Declare.notNull (param.getProductId (), GlobalsEnums.REQUIRED_SUPPLIER_PRODUCT);
            ProductRb product = productService.getById (param.getProductId ());
            Declare.notNull (product, GlobalsEnums.REQUIRED_SUPPLIER_PRODUCT);
            Declare.state (ProductStateEnum.UP_SOLD.isThis (product.getState ()), GlobalsEnums.PRODUCT_STATE_ERROR);
        }
        else if (ShopProductOwnEnum.SELF_CUSTOM.isThis (param.getOwn ())) {
           //ignore...
        }
        else {
            throw BizException.def ().msg ("不支持的新增小程序商品类型");
        }
        //创建商品
        newProduct = ShopProductRbQuery.createShopProduct (param);
        int rec = shopProductRbMapper.insertSelective (newProduct);
        logger.info ("创建小程序商品:insert shopProductId[{}].rec[{}]", newProduct.getId (), rec);
        Declare.state (rec == 1, GlobalsEnums.FAILED_CREATE_SHOP_PRODUCT);
        return newProduct;
    }





    /**
     * 创建商品标签信息
     *
     * @param shopProduct shopProduct
     * @param shopTagList shopTagList
     * @author Charlie
     * @date 2018/9/4 9:09
     */
    private void doInsertShopTag(ShopProductRb shopProduct, Collection<ShopTagRb> shopTagList) {
        if (shopTagList.isEmpty ()) {
            logger.info ("插入商品的标签信息 没有标签信息");
            return;
        }
        List<ShopTagProductRb> shopTagProductList = new ArrayList<> (shopTagList.size ());
        long current = System.currentTimeMillis ();
        shopTagList.forEach (tag->{
            ShopTagProductRb shopTagProduct = new ShopTagProductRb ();
            shopTagProduct.setCreateTime(current);
            shopTagProduct.setUpdateTime(current);
            shopTagProduct.setShopProductId(shopProduct.getId ());
            shopTagProduct.setStoreId(shopProduct.getStoreId ());
            shopTagProduct.setTagId(tag.getId ());
            shopTagProduct.setTagName (tag.getTagName ());
            shopTagProductList.add (shopTagProduct);
        });
        int rec = shopTagProductMapper.insertBach (shopTagProductList);
        logger.info ("--创建小程序商品:创建标签信息 shopProductId[{}].rec[{}]", shopProduct.getId (), rec);
    }

    /**
     * 批量插入小程序商品sku
     *
     * @param shopProduct shopProduct
     * @param skuList sku信息
     * @author Charlie
     * @date 2018/9/3 21:03
     */
    private void doBatchInsertSku(ShopProductRb shopProduct, List<ProductSkuRbNew> skuList) {
        logger.info ("新增小程序用户的sku信息 shopProductId[{}]", shopProduct.getId ());

/*        List<Long> colorIds = new ArrayList<> (skuList.size ());
        List<Long> sizeIds = new ArrayList<> (skuList.size ());
        skuList.forEach (sku->{
            colorIds.add (sku.getColorId ());
            sizeIds.add (sku.getSizeId ());
        });
        //查询颜色
        logger.info ("查询颜色 colorIds[{}]", colorIds);
        PropertyValueRbQuery pvQuery = new PropertyValueRbQuery ();
        pvQuery.setIds (colorIds);
        List<PropertyValueRb> colorList = propertyValueMapper.selectList (pvQuery);
        Declare.state (colorIds.size () == colorList.size (), GlobalsEnums.NO_FOUND_COLOR);
        Map<Long, PropertyValueRb> colorMap = new HashMap<> (colorIds.size ());
        colorList.forEach (color-> colorMap.put (color.getId (), color));

        //查询尺码
        logger.info ("查询尺码 sizeIds[{}]", sizeIds);
        pvQuery.setIds (sizeIds);
        List<PropertyValueRb> sizeList = propertyValueMapper.selectList (pvQuery);
        Declare.state (sizeIds.size () == sizeList.size (), GlobalsEnums.NO_FOUND_SIZE);
        Map<Long, PropertyValueRb> sizeMap = new HashMap<> (sizeIds.size ());
        sizeList.forEach (size-> sizeMap.put (size.getId (), size));*/

        long current = System.currentTimeMillis ();
        for (ProductSkuRbNew sku : skuList) {
            sku.setPropertyIds ("7:" + sku.getColorId () + ";8:" + sku.getSizeId ());
            sku.setOwnType (STORE_SKU);
            sku.setWxaProductId (shopProduct.getId ());
            sku.setCreateTime (current);
            sku.setUpdateTime (current);
            sku.setName (shopProduct.getName ());
            sku.setClothesNumber (shopProduct.getClothesNumber ());
            sku.setUpdateTime (current);
            sku.setCreateTime (current);
            int rec = productSkuMapper.insertSelective (sku);
            Declare.state (rec == 1, GlobalsEnums.FAILED_CREATE_SHOP_PRODUCT_SKU);
        }
        logger.info ("--新建小程序商品:创建sku size[{}]", skuList.size ());

        //设置编码
        skuList.forEach (sku->{
            ProductSkuRbNew param = new ProductSkuRbNew ();
            param.setId (sku.getId ());
            param.setSkuNo (sku.getId ()+ 2000000);
            int updRec = productSkuMapper.updateByPrimaryKeySelective (param);
            Declare.state (updRec == 1, GlobalsEnums.FAILED_CREATE_SHOP_PRODUCT_SKU);
        });
        logger.info ("--新建小程序商品:更新sku编码成功");
    }


    /**
     * 活动首页秒杀活动
     *
     * @param storeId   门店id
     * @param wxVersion 微信小程序版本
     * @return com.jiuy.rb.model.product.TeamBuyActivityRb
     * @author Charlie
     * @date 2018/8/7 10:54
     */
    private Map<String, String> homeFirstSecondActivity(Long storeId, String wxVersion) {
        //新版本
        int newVersion = 140;
        Integer version = HttpUtils.wxVersion (wxVersion, newVersion);
        SecondBuyActivityRb second;
        if (version < newVersion) {
            //老版本
            SecondBuyActivityRbQuery secondQuery = new SecondBuyActivityRbQuery ();
            secondQuery.setStoreId (storeId);
            secondQuery.setGtActivityEndTime (System.currentTimeMillis ());
            secondQuery.setOffset (0);
            secondQuery.setLimit (1);
            List<SecondBuyActivityRb> seconds = secondBuyActivityRbMapper.selectAvailableSecondOrderByEndTime (secondQuery);
            second = seconds.isEmpty () ? null : seconds.get (0);
        }
        else {
            //新版本
            SecondBuyActivityRbQuery secondQuery = new SecondBuyActivityRbQuery ();
            secondQuery.setStoreId (storeId);
            secondQuery.setOffset (0);
            secondQuery.setLimit (1);
            List<SecondBuyActivityRbQuery> seconds = listSecondBuyActivityWithOrder (secondQuery).getRows ();
            second = seconds.isEmpty () ? null : seconds.get (0);
        }
        if (second == null) {
            return Empty.<String, String> map ();
        }

        return buildSecondResponse (second);
    }


    /**
     * 组装返回值
     *
     * @param second second
     * @return java.util.Map<java.lang.String   ,   java.lang.String>
     * @author Charlie
     * @date 2018/8/7 13:03
     */
    private Map<String, String> buildSecondResponse(SecondBuyActivityRb second) {
        Map<String, String> result = new HashMap<> (16);

        result.put ("activityId", String.valueOf (second.getId ()));
        result.put ("activityTitle", String.valueOf (second.getActivityTitle ()));
        result.put ("shopProductId", String.valueOf (second.getShopProductId ()));
        result.put ("activityPrice", String.valueOf (second.getActivityPrice ()));
        result.put ("activityProductPrice", String.valueOf (second.getActivityProductPrice ()));
        result.put ("shopProductShowcaseImgs", second.getShopProductShowcaseImgs ());
        String fullName = second.getShopProductName ();
        result.put ("shopProductName", fullName);
        result.put ("simpleProductName", fullName.length () > 20 ? fullName.substring (0, 20) : fullName);
        //活动状态：1待开始，2进行中，3已结束
        int stateCode = SecondBuyActivityRbQuery.stateCode (second);
        result.put ("activityStatus", String.valueOf (stateCode));
        //还剩活动商品件数
        int surplusProductCount = second.getActivityProductCount () - second.getOrderedProductCount ();
        surplusProductCount = surplusProductCount < 0 ? 0 : surplusProductCount;
        result.put ("surplusProductCount", String.valueOf (surplusProductCount));
        //倒计时
        if (stateCode == 1) {
            long surplusStartTime = second.getActivityStartTime () - System.currentTimeMillis ();
            surplusStartTime = surplusStartTime < 0 ? 0 : surplusStartTime;
            result.put ("surplusStartTime", String.valueOf (surplusStartTime));
        }
        else if (stateCode == 2) {
            long surplusEndTime = second.getActivityEndTime () - System.currentTimeMillis ();
            surplusEndTime = surplusEndTime < 0 ? 0 : surplusEndTime;
            result.put ("surplusEndTime", String.valueOf (surplusEndTime));
        }
        else {
            //ignore...
        }
        return result;
    }


    /**
     * 活动首页团购活动
     *
     * @param storeId   门店id
     * @param wxVersion 微信小程序版本
     * @return com.jiuy.rb.model.product.TeamBuyActivityRb
     * @author Charlie
     * @date 2018/8/7 10:54
     */
    private Map<String, String> homeFirstTeamActivity(Long storeId, String wxVersion) {
        //新版本
        int newVersion = 140;
//        wxVersion = "1.3.9";
        Integer version = HttpUtils.wxVersion (wxVersion, newVersion);
        TeamBuyActivityRb team;
        if (version < newVersion) {
            //老版本:
            TeamBuyActivityRbQuery teamQuery = new TeamBuyActivityRbQuery ();
            teamQuery.setStoreId (storeId);
            teamQuery.setGtActivityEndTime (System.currentTimeMillis ());
            teamQuery.setOffset (0);
            teamQuery.setLimit (1);
            List<TeamBuyActivityRb> teams = teamBuyActivityRbMapper.selectAvailableTeamOrderByEndTime (teamQuery);
            team = teams.isEmpty () ? null : teams.get (0);
        }
        else {
            //新版本
            TeamBuyActivityRbQuery teamQuery = new TeamBuyActivityRbQuery ();
            teamQuery.setStoreId (storeId);
            teamQuery.setOffset (0);
            teamQuery.setLimit (1);
            List<TeamBuyActivityRbQuery> teams = listTeamBuyActivityWithOrder (teamQuery).getRows ();
            team = teams.isEmpty () ? null : teams.get (0);
        }
        if (team == null) {
            return Empty.map ();
        }
        return buildTeamResponse (team);
    }


    /**
     * 组装返回值
     *
     * @param team team
     * @return java.util.Map<java.lang.String   ,   java.lang.String>
     * @author Charlie
     * @date 2018/8/7 13:03
     */
    private Map<String, String> buildTeamResponse(TeamBuyActivityRb team) {
        Map<String, String> result = new HashMap<> (16);

        result.put ("userCount", String.valueOf (team.getUserCount ()));
        result.put ("activityMemberCount", String.valueOf (team.getActivityMemberCount ()));
        result.put ("activityId", String.valueOf (team.getId ()));
        result.put ("activityTitle", team.getActivityTitle ());
        result.put ("shopProductId", String.valueOf (team.getShopProductId ()));
        result.put ("activityPrice", String.valueOf (team.getActivityPrice ()));
        result.put ("activityProductPrice", String.valueOf (team.getActivityProductPrice ()));
        result.put ("shopProductShowcaseImgs", team.getShopProductShowcaseImgs ());
        result.put ("conditionType", String.valueOf (team.getConditionType ()));
        result.put ("orderedProductCount", String.valueOf (team.getOrderedProductCount ()));
        result.put ("meetProductCount", String.valueOf (team.getMeetProductCount ()));
        String fullName = team.getShopProductName ();
        result.put ("shopProductName", fullName);
        //简称
        result.put ("simpleProductName", fullName.length () > 20 ? fullName.substring (0, 20) : fullName);
        //还差多少拼团成功
        if (ObjectUtils.nullSafeEquals (team.getConditionType (), TeamBuyActivityRbQuery.CONDITION_TYPE_PRODUCT)) {
            int temp = team.getMeetProductCount () - team.getOrderedProductCount ();
            temp = temp < 0 ? 0 : temp;
            result.put ("differMemberCount", EMPTY.string);
            result.put ("differProductCount", temp + "");
        }
        else {
            int temp = team.getUserCount () - team.getActivityMemberCount ();
            temp = temp < 0 ? 0 : temp;
            result.put ("differMemberCount", temp + "");
            result.put ("differProductCount", EMPTY.string);
        }
        //活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
        int stateCode = TeamBuyActivityRbQuery.stateCode (team);
        result.put ("activityStatus", String.valueOf (stateCode));
        //活动倒计时
        if (ShopActivityStatusEnum.WAITING_2_START.getCode () == stateCode) {
            long surplusStartTime = team.getActivityStartTime () - System.currentTimeMillis ();
            result.put ("surplusStartTime", String.valueOf (surplusStartTime < 0 ? 0 : surplusStartTime));
        }
        else if (ShopActivityStatusEnum.UNDERWAY.getCode () == stateCode) {
            long surplusEndTime = team.getActivityEndTime () - System.currentTimeMillis ();
            result.put ("surplusEndTime", String.valueOf (surplusEndTime < 0 ? 0 : surplusEndTime));
        }
        else {
            //ignore...
        }
        return result;
    }


    /**
     * 将活动库存放入缓存
     * <p>缓存有则不处理</p>
     *
     * @param storeId  storeId
     * @param secondId secondId
     * @author Charlie
     * @date 2018/8/3 23:24
     */
    private void putSecondInCacheIfNoExist(Long storeId, Long secondId) {
        logger.info ("准备将秒杀活动库存放入缓存.... storeId[{}].secondId[{}]", storeId, secondId);
        SecondBuyActivityRbQuery query = new SecondBuyActivityRbQuery ();
        query.setStoreId (storeId);
        query.setId (secondId);
        SecondBuyActivityRb second = secondBuyActivityRbMapper.selectOne (query);
        Declare.existResource (second);

        Long handEndTime = second.getActivityHandEndTime ();
        Long endTime = second.getActivityEndTime ();
        boolean isActivityOver = isActivityOver (handEndTime, endTime);
        if (isActivityOver) {
            logger.info ("活动已结束,不需要再放入缓存 activityId[{}].activityHandEndTime[{}].activityEndTime[{}]", secondId, handEndTime, endTime);
            return;
        }

        String cacheKey = acquireSecondCacheKey (secondId);
        String cacheCount = cacheService.get (cacheKey);
        boolean isCanPutInCache = second.getActivityStartTime ()>System.currentTimeMillis () || StringUtils.isBlank (cacheCount);
        logger.info ("将小程序秒杀库存放入缓存 cacheKey[{}].cacheCount[{}].isCanPutInCache[{}]", cacheKey, cacheCount, isCanPutInCache);

        if (isCanPutInCache) {
            int inventory = second.getActivityProductCount () - second.getOrderedProductCount ();
            Boolean isOk = cacheService.setSimple (cacheKey, inventory, SECOND_INVENTORY_SURVIVAL_TIME);
            if (isOk) {
                logger.info ("将小程序秒杀库存放入缓存成功 cacheKey[{}].inventory[{}].", cacheKey, inventory);
            }
            else {
                logger.error ("将小程序秒杀库存放入缓存失败!!!! cacheKey[{}].inventory[{}].", cacheKey, inventory);
            }
        }
    }


    /**
     * 活动是否已结束
     *
     * @param handEndTime 手动关闭时间
     * @param endTime     结束时间
     * @return boolean
     * @author Charlie
     * @date 2018/8/5 20:26
     */
    private boolean isActivityOver(@Nullable Long handEndTime, Long endTime) {
        boolean isActivityOver = handEndTime != null && handEndTime > 0;
        isActivityOver = isActivityOver || endTime <= System.currentTimeMillis ();
        return isActivityOver;
    }


    /**
     * 将活动库存放入缓存
     * <p>缓存有则不处理</p>
     *
     * @param storeId storeId
     * @param teamId  teamId
     * @author Charlie
     * @date 2018/8/3 23:24
     */
    private void putTeamInCacheIfNoExist(Long storeId, Long teamId) {
        logger.info ("准备将团购活动库存放入缓存.... storeId[{}].teamId[{}]", storeId, teamId);
        TeamBuyActivityRbQuery query = new TeamBuyActivityRbQuery ();
        query.setStoreId (storeId);
        query.setId (teamId);
        TeamBuyActivityRb team = teamBuyActivityRbMapper.selectOne (query);
        Declare.existResource (team);

        Long handEndTime = team.getActivityHandEndTime ();
        Long endTime = team.getActivityEndTime ();
        boolean isActivityOver = isActivityOver (handEndTime, endTime);
        if (isActivityOver) {
            logger.info ("活动已结束,不需要再放入缓存 activityId[{}].activityHandEndTime[{}].activityEndTime[{}]", teamId, handEndTime, endTime);
            return;
        }

        String cacheKey = acquireTeamCacheKey (teamId);
        String cacheCount = cacheService.get (cacheKey);
        boolean isCanPutInCache = team.getActivityStartTime ()>System.currentTimeMillis () ||  StringUtils.isBlank (cacheCount);
        logger.info ("将小程序团购库存放入缓存 cacheKey[{}].cacheCount[{}].isCanPutInCache[{}]", cacheKey, cacheCount, isCanPutInCache);

        if (isCanPutInCache) {
            //实际库存
            Integer inventory = team.getActivityProductCount () - team.getOrderedProductCount ();
            Boolean isOk = cacheService.setSimple (cacheKey, inventory, TEAM_INVENTORY_SURVIVAL_TIME);
            if (isOk) {
                logger.info ("将小程序团购库存放入缓存成功  cacheKey[{}].inventory[{}].", cacheKey, inventory);
            }
            else {
                logger.error ("将小程序团购库存放入缓存失败!!!! cacheKey[{}].inventory[{}].", cacheKey, inventory);
            }
        }
    }


    /**
     * 释放秒杀活动库存
     *
     * @param secondId secondId
     * @param count    count
     * @author Charlie
     * @date 2018/8/3 18:14
     */
    private void releaseSecondInventory(@NoNull Long secondId, @NoNull Integer count) {
        SecondBuyActivityRb second = secondBuyActivityRbMapper.selectByPrimaryKey (secondId);
        Declare.notNull (second, GlobalsEnums.ACTIVITY_NO_SECOND);

        //活动已结束, 就不用释放库存, 活动未结束则释放锁定库存
        int state = SecondBuyActivityRbQuery.stateCode (second);
        if (ShopActivityStatusEnum.TERMINATE.getCode () == state) {
            //不用释放库存
            return;
        }

        //回滚参加人数
        int dRec = secondBuyActivityRbMapper.updateUserAndProductCount (secondId, 0 - count, - 1);
        Declare.state (dRec == 1, GlobalsEnums.ACTIVITY_UPDATE_FAILED);

        //回滚缓存
        String key = acquireSecondCacheKey (second.getId ());
        String response = cacheService.get (key);
        if (StringUtils.isNotBlank (response)) {
            Long backData = cacheService.incr (key, count);
            logger.info ("释放秒杀活动库存成功 secondId[{}].释放前库存[{}].回滚库存数[{}].释放后库存[{}]", secondId, response, count, backData);
        }
        else {
            //失败 ignore...
            logger.warn ("释放秒杀活动库存,没有缓存信息 secondId[{}].count[{}]", secondId, count);
        }
    }


    /**
     * 获取缓存key
     *
     * @param secondId secondId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/3 19:54
     */
    private String acquireSecondCacheKey(Long secondId) {
        return CacheKeyEnum.SHOP_SECOND_ACTIVITY_INVENTORY.getCache () + secondId;
    }


    /**
     * 释放团购活动库存
     *
     * @param teamId teamId
     * @param count  count
     * @author Charlie
     * @date 2018/8/3 18:15
     */
    private void releaseTeamInventory(@NoNull Long teamId, @NoNull Integer count) {

        TeamBuyActivityRb team = teamBuyActivityRbMapper.selectByPrimaryKey (teamId);
        Declare.notNull (team, GlobalsEnums.ACTIVITY_NO_TEAM);

        //活动已结束, 就不用释放库存, 活动未结束则释放锁定库存
        int state = TeamBuyActivityRbQuery.stateCode (team);
        if (ShopActivityStatusEnum.TERMINATE.getCode () == state) {
            //不用释放库存
            return;
        }

        int dRec = teamBuyActivityRbMapper.updateJoinUserOrProduct (teamId, team.getConditionType (), 0 - count, - 1);
        Declare.state (dRec == 1, GlobalsEnums.ACTIVITY_UPDATE_FAILED);

        //回滚缓存
        String key = acquireTeamCacheKey (team.getId ());
        String response = cacheService.get (key);
        if (StringUtils.isNotBlank (response)) {
            Long backData = cacheService.incr (key, count);
            logger.info ("释放团购活动库存成功 secondId[{}].释放前库存[{}].回滚库存数[{}].释放后库存[{}]", teamId, response, count, backData);
        }
        else {
            //失败 ignore...
            logger.warn ("释放团购活动库存,没有缓存信息 secondId[{}].count[{}]", teamId, count);
        }
    }


    /**
     * 获取缓存key
     *
     * @param teamId teamId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/3 19:54
     */
    private String acquireTeamCacheKey(Long teamId) {
        return CacheKeyEnum.SHOP_TEAM_ACTIVITY_INVENTORY.getCache () + teamId;
    }


}
