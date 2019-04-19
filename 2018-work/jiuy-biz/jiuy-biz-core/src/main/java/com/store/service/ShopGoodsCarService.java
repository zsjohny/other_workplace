package com.store.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.ProductSkuNew;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.order.ShopGoodsCar;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.BizUtil;
import com.store.dao.mapper.ProductSKUMapper;
import com.store.dao.mapper.ShopGoodsCarMapper;
import com.store.dao.mapper.StoreBusinessMapper;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.member.ShopMember;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * 小程序商城购物车 服务实现类
 * </p>
 *
 * @author Aison
 * @since 2018-04-19
 */
@Service
public class ShopGoodsCarService {
    private static final Log logger = LogFactory.get();

    /**
     * 购物车状态:正常
     */
    private static final int NORMAL = 1;
    /**
     * 购物车状态:失效
     */
    private static final int DISABLED = 2;

    @Autowired
    private ShopGoodsCarMapper shopGoodsCarMapper;
    @Autowired
    private ProductSKUMapper productSKUMapper;
    @Autowired
    private ShopProductMapper shopProductMapper;
    @Autowired
    private ShopMemberOrderFacade shopMemberOrderFacade;
    @Autowired
    private StoreBusinessMapper storeBusinessMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    private ProductSkuNewMapper productSkuNewMapper;

    /**
     * 判断直接付款，库存量
     * @return
     */
    public List<Map<String, Object>> judgePaySNum(List<Map<String,String>> paySkuList){

        List<Map<String, Object>> rsDataList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        for(Map<String,String> action :paySkuList) {

            String productId = action.get("productId");
            String skuId = action.get("skuId");
            int count = Integer.parseInt(action.get("count"));

            ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
//            ProductSkuNew productSkuNew = productSkuNewMapper.findColorSizeRemainById(skuId);
            ShopProduct shopProduct = shopProductMapper.selectById(productId);


            //0平台供应商商品, 1是用户自定义款，2用户自营平台同款
            Integer own = shopProduct.getOwn();
            int remainCount = productSkuNew.getRemainCount().intValue();

            if (own.intValue() == 1 || own.intValue() == 0) {
                //不是同款自营直接比较库存
                if (count > remainCount) {

                    data.put("isOk", false);
                    data.put("productId", productId);
                    rsDataList.add(data);
                    return rsDataList;
                }
            } else {

                //同款自营 加上供应商的库存在比较
                Wrapper<ProductSkuNew> wrapperApp = new EntityWrapper<ProductSkuNew>();
                wrapperApp.eq("ProductId", shopProduct.getProductId());
                wrapperApp.eq("colorId", productSkuNew.getColorId());
                wrapperApp.eq("sizeId", productSkuNew.getSizeId());
                wrapperApp.ge("Status", - 1);
                List<ProductSkuNew> skuAppList = productSkuNewMapper.selectList(wrapperApp);

                int appC=remainCount;
                if(skuAppList !=null && skuAppList.size() >0){
                    ProductSkuNew productSkuNew1 = skuAppList.get(0);
                    appC += productSkuNew1.getRemainCount().intValue();
                }

                if (count > appC) {
                    data.put("isOk", false);
                    data.put("productId", productId);
                    rsDataList.add(data);
                    return rsDataList;
                }
            }
        };


        if (rsDataList.size()<1){
            data.put("isOk", true);
            data.put("cardId", "");
            rsDataList.add(data);
        }

        return rsDataList;
    }

    private List<Map<String, Object>> judgeCardSNum(ShopGoodsCar action){

        List<Map<String, Object>> rsDataList = new ArrayList<>();

        logger.info("判断购物车商品库存 car=", action);

        Long skuId = action.getProductSkuId();
        if (skuId == null || skuId < 1L) {
            throw BizException.defulat().msg("购物车商品已失效");
        }
        ProductSkuNew productSkuNew = productSkuNewMapper.selectById(skuId);
        ShopProduct shopProduct = shopProductMapper.selectById(action.getShopProductId());

        long skuNumber = action.getSkuNumber().longValue();

        Map<String, Object> data = new HashMap<>();
        //0平台供应商商品, 1是用户自定义款，2用户自营平台同款
        Integer own = shopProduct.getOwn();

        Integer skuCount = productSkuNew.getRemainCount();
        int remainCount = 0;
        if (skuCount == null) {
            logger.error("库存数量为空 skuId={}", skuId);
        }
        else {
            remainCount = skuCount.intValue();
        }

        if (own.intValue() == 1 || own.intValue() == 0) {
            //不是同款自营直接比较库存
            if (skuNumber > remainCount) {

                data.put("isOk", false);
                data.put("cardId", action.getId());
                rsDataList.add(data);
                return rsDataList;
            }
        } else {

            //同款自营 加上供应商的库存在比较
            Wrapper<ProductSkuNew> wrapperApp = new EntityWrapper<ProductSkuNew>();
            wrapperApp.eq("ProductId", shopProduct.getProductId());
            wrapperApp.eq("colorId", productSkuNew.getColorId());
            wrapperApp.eq("sizeId", productSkuNew.getSizeId());
            wrapperApp.ge("Status", - 1);
            List<ProductSkuNew> skuAppList = productSkuNewMapper.selectList(wrapperApp);

            int appC=remainCount;
            if(skuAppList !=null && skuAppList.size() >0){
                ProductSkuNew productSkuNew1 = skuAppList.get(0);
                appC += productSkuNew1.getRemainCount().intValue();
            }

            if (skuNumber > appC) {
                data.put("isOk", false);
                data.put("cardId", action.getId());
                rsDataList.add(data);
                return rsDataList;
            }
        }

        if (rsDataList.size()<1){
            data.put("isOk", true);
            data.put("cardId", action.getId());
            rsDataList.add(data);
        }

        return rsDataList;
    }

    /**
     *描述 购物车点击结算的时候，进行库存的判断
     *
     * @author hyq
     * @date 2018/9/6 15:40
     * @return com.jiuyuan.web.help.JsonResponse
     */
    public List<Map<String, Object>> judgeCardsInventory(Long memberId, Object[] carId) {
//        List<Long> list=new ArrayList<>();
//        for (int i = 0; i <carId.length ; i++) {
//            list.add((Long)carId[i]);
//        }
//        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectShopGoodsCarList(memberId, list);
        //查询出正常的可以生成订单的购物车清单
        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectList(Condition.create().eq("member_id", memberId).eq("car_suk_status",1)
                .in("id", carId).orderBy("store_id"));
        if(cars.size()==0) {
            throw BizException.defulat().msg("购物车中没有商品了");
        }

        List<Map<String, Object>> rsDataList = new ArrayList<>();

        for(ShopGoodsCar action:cars) {

            List<Map<String, Object>> maps = judgeCardSNum(action);

            Map<String, Object> stringObjectMap = maps.get(0);

            Object isOk = stringObjectMap.get("isOk");

            if(!(Boolean) isOk){
                return maps;
            }
        }

        return rsDataList;
    }

    /**
     * @throws
     * @Title: joinShopCar
     * @Description: 加入购物车
     * @param: shopGoodsCar  memberId skuId number 必须传递
     * @return: void
     */
    public void joinShopCar(ShopGoodsCar shopGoodsCar) {

        //判断参数合法性
        if (shopGoodsCar.getMemberId() == null) {
            throw BizException.defulat().paramError();
        }
        // 如果sku为空 且productid也为空则是错误的
        if(BizUtil.hasEmpty(shopGoodsCar.getProductSkuId()) && BizUtil.hasEmpty(shopGoodsCar.getProductId())) {
            throw BizException.defulat().paramError();
        }

        ShopProduct shopProduct =  shopProductMapper.selectById(shopGoodsCar.getProductId());
        if(shopProduct== null) {
            throw BizException.defulat().msg("获取不到商品信息");
        }
        // 当前商城下面此商品是否在售
        if(shopProduct.getSoldOut()!=1) {
            throw BizException.defulat().msg("此商品已经下架,加入购物车失败");
        }

        Long productId = null;

        //一键上传商品  和 供应商同款
        if(shopProduct.getOwn().intValue()==0||shopProduct.getOwn().intValue()==2){
            productId = shopProduct.getProductId();
        }

        Long skuNumber = shopGoodsCar.getSkuNumber();
        //如果少于 1 则默认为1
        if (skuNumber == null || skuNumber <= 0) {
            shopGoodsCar.setSkuNumber(1L);
        }
        // 判断是否已经在购物车中了 如果已经存在购物车中 则数量+1;
        // 查询是否有此条记录

        ShopGoodsCar existCar = null;

        // 没有sku的情况 则判断shop_product_id是存在
        ShopGoodsCar param = new ShopGoodsCar();
        param.setMemberId(shopGoodsCar.getMemberId());
        param.setStoreId(shopGoodsCar.getStoreId());
        if(shopGoodsCar.getProductSkuId()==0) {
            ShopGoodsCar history = shopGoodsCarMapper.selectShopGoodsCar(
                    shopGoodsCar.getMemberId(),
                    shopGoodsCar.getStoreId(),
                    shopGoodsCar.getProductId(),
                    null
            );
//            param.setShopProductId(shopGoodsCar.getProductId());
//            ShopGoodsCar history = shopGoodsCarMapper.selectOne(param);
            //已失效的不算
            if (history != null && !ObjectUtils.nullSafeEquals (history.getCarSukStatus(), 2)) {
                existCar = history;
            }
        } else {
//            param.setProductSkuId(shopGoodsCar.getProductSkuId());
//            existCar = shopGoodsCarMapper.selectOne(param);
            existCar=shopGoodsCarMapper.selectShopGoodsCarNew(shopGoodsCar.getMemberId(), shopGoodsCar.getStoreId(),shopGoodsCar.getProductSkuId(), null);
        }
        // 已经添加过一次了
        int rs;
        if (existCar != null) {
            existCar.setSkuNumber(existCar.getSkuNumber() + shopGoodsCar.getSkuNumber());
            existCar.setLastUpdateTime(System.currentTimeMillis());
            existCar.setProductId(productId);
            existCar.setStoreId(shopGoodsCar.getStoreId());
            existCar.setShopProductId(shopProduct.getId());

            rs = shopGoodsCarMapper.updateById(existCar);
        } else {
            shopGoodsCar.setProductId(productId);
            shopGoodsCar.setCreateTime(System.currentTimeMillis());
            shopGoodsCar.setLastUpdateTime(shopGoodsCar.getCreateTime());
            shopGoodsCar.setShopProductId(shopProduct.getId());

            rs = shopGoodsCarMapper.insert(shopGoodsCar);
        }
        if (rs == 0) {
            //添加失败抛出异常
            throw BizException.defulat().msg("加入购物车失败");
        }
    }

    /**
     * @throws
     * @Title: updateShopCar
     * @Description: 修改购物车的某条记录
     * @param: shopGoodsCa id 必选  number/status 选填
     * @return: void
     */
    public void updateShopCar(ShopGoodsCar shopGoodsCar) {
        // 每次修改都必须提供memberid 否则参数错误  id 与meberId只要有一个为空则不允许修改
        if (BizUtil.hasEmpty(shopGoodsCar.getMemberId(), shopGoodsCar.getId())) {
            throw BizException.defulat().paramError();
        }

        //将老的查询出来是否存在老的
        ShopGoodsCar param = new ShopGoodsCar();
        param.setId(shopGoodsCar.getId());
        param.setMemberId(shopGoodsCar.getMemberId());
        ShopGoodsCar oldCar = shopGoodsCarMapper.selectOne(param);
//        ShopGoodsCar oldCar = shopGoodsCarMapper.selectByMemberId(shopGoodsCar.getId(), shopGoodsCar.getMemberId());
        if (oldCar == null) {
            throw BizException.defulat().paramError();
        }
        oldCar.setSkuNumber(shopGoodsCar.getSkuNumber());

        List<Map<String, Object>> maps = judgeCardSNum(oldCar);

        Map<String, Object> objectMap = maps.get(0);

        Object isOK = objectMap.get("isOk");

        if( isOK!=null && !(Boolean) isOK){
            throw BizException.defulat().msg("库存不足,无法添加");
        }

        shopGoodsCar.setLastUpdateTime(System.currentTimeMillis());
        shopGoodsCarMapper.updateById(shopGoodsCar);
    }

    /**
     * 删除购物车中的某条记录
     *
     * @date: 2018/4/20 10:36
     * @author: Aison
     */
    public void deleteCarGoods(Long memberId, Long carId) {

        // 验证参数
        if (BizUtil.hasEmpty(memberId, carId)) {
            throw BizException.defulat().paramError();
        }
        // 判断是否存在此记录
        ShopGoodsCar param = new ShopGoodsCar();
        param.setId(carId);
        param.setMemberId(memberId);
        ShopGoodsCar oldCar = shopGoodsCarMapper.selectOne(param);
//        ShopGoodsCar oldCar = shopGoodsCarMapper.selectByMemberId(carId, memberId);

        if (oldCar == null) {
            throw BizException.defulat().paramError();
        }
        //逻辑删除此记录
        int rs = shopGoodsCarMapper.delete(Condition.create().eq("id",carId).eq("member_id",memberId));
        if(rs==0) {
            throw BizException.defulat().msg("删除失败");
        }
    }

    /**
     * 删除购物车中的某条记录
     *
     * @date: 2018/4/20 10:36
     * @author: Aison
     */
    public void deleteCarGoodsByIds(Long memberId, String carIds) {

        // 判断是否存在此记录
        if(BizUtil.hasEmpty(carIds)) {
            return ;
        }
        String [] ids = carIds.split(",");
        if(ids.length==0){
            return ;
        }
        ShopGoodsCar cart = new ShopGoodsCar();
        cart.setCarSukStatus(-1);
        int rs = shopGoodsCarMapper.delete(Condition.create().in("id",ids).eq("member_id",memberId));
        if(rs!=ids.length) {
            throw BizException.defulat().msg("删除购物车失败");
        }
    }

    /**
     * 从购物车跳转到订单确认页面
     *
     * @param memberId
     * @param carId
     * @date: 2018/4/20 10:42
     * @author: Aison
     */
    public Map<Long, Map<String, Object>> confirmOrderFromCar(Long memberId, Long[] carId) {
//        List<Long>list=new ArrayList<>();
//        for (int i = 0; i <carId.length ; i++) {
//            list.add(carId[i]);
//        }
//        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectListNew(memberId, list);
        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectList(Condition.create().eq("member_id", memberId).in("id", carId).orderBy("store_id"));
        Map<Long, Map<String, Object>> resultMap = new HashMap<>();
        cars.forEach(action -> {
            Long storeId = action.getStoreId();
            Map<String, Object> map = resultMap.get(storeId);
            if (map == null) {
                map = new HashMap<>();
                map.put("storeId", storeId);
                map.put("memberId", memberId);
                map.put("shopMemberDeliveryAddressId", "");
                String productIdSkuIdCount = action.getShopProductId() + "_" + action.getProductSkuId() + "_" + action.getSkuNumber();
                List<String> products = new ArrayList<>();
                products.add(productIdSkuIdCount);
                map.put("productIdSkuIdCount", products);
                resultMap.put(storeId, map);
            } else {
                String productIdSkuIdCount = action.getShopProductId() + "_" + action.getProductSkuId() + "_" + action.getSkuNumber();
                List<String> products = (List<String>) map.get("productIdSkuIdCount");
                products.add(productIdSkuIdCount);
                map.put("productIdSkuIdCount", products);
            }
        });
        return resultMap;
    }

    /**
     * 从购物车到订单确认
     * @param memberId
     * @param carIds
     * @param version
     * @param orderType
     * @param shopMemberDeliveryAddressId
     * @date:   2018/4/20 15:20
     * @author: Aison
     */
    public List<Map<String, Object>> confirmDataFromCar(Long memberId, String[] carIds, String version, Integer orderType, Long shopMemberDeliveryAddressId) {

        if(carIds == null || carIds.length==0) {
            throw BizException.defulat().msg("参数错误");
        }
//        List<Long>list=new ArrayList<>();
//        for (int i = 0; i <carIds.length ; i++) {
//            list.add(Long.parseLong(carIds[i]));
//        }
//        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectListT(memberId, list);
        //查询出正常的可以生成订单的购物车清单
        List<ShopGoodsCar> cars = shopGoodsCarMapper.selectList(Condition.create().eq("member_id", memberId).eq("car_suk_status",1)
                .in("id", carIds).orderBy("store_id"));
        if(cars.size()==0) {
            throw BizException.defulat().msg("购物车中没有商品了");
        }


        List<Map<String, Object>> rsDataList = new ArrayList<>();

        // 对选中的购物车进行分组 分组成不同的商家   //其实永远只有一个商家  hyq
        Map<Long, List<ShopGoodsCar>> carGroup = new HashMap<>();
        cars.forEach(action -> {
            Long storeId = action.getStoreId();
            List<ShopGoodsCar> carItem = carGroup.get(storeId);
            if (carItem == null) {
                carItem = new ArrayList<>();
                carItem.add(action);
            } else {
                carItem.add(action);
            }
            carGroup.put(storeId, carItem);
        });


        for (Map.Entry<Long, List<ShopGoodsCar>> entry : carGroup.entrySet()) {
            StoreBusiness storeBusiness = storeBusinessMapper.getByIdNew(entry.getKey());
            //StoreBusiness storeBusiness = storeBusinessMapper.getById(entry.getKey());
            ShopDetail shopDetail = new ShopDetail();
            shopDetail.setStoreBusiness(storeBusiness);
            List<ShopGoodsCar> goodsCars = entry.getValue();
            List<Map<String, String>> orderProductAndSkuInfo = new ArrayList<Map<String, String>>();
            for (ShopGoodsCar car : goodsCars) {
                Map<String, String> param = new HashMap<String, String>();
                param.put("productId", car.getShopProductId().toString());
                param.put("skuId", car.getProductSkuId().toString());
                param.put("count", car.getSkuNumber().toString());
                Long liveProductId = car.getLiveProductId();
                param.put("liveProductId", liveProductId == null ? "0": liveProductId.toString());
                orderProductAndSkuInfo.add(param);
            }
            //获取商品详情和优惠券个数
            Map<String, Object> data = shopMemberOrderFacade.confirmOrder(orderProductAndSkuInfo, storeBusiness.getId(), memberId, version);


            ShopMember shopMember =  memberService.getMemberById(memberId);
            MemberDetail memberDetail = new MemberDetail();
            memberDetail.setMember(shopMember);

            //默认收货方式送货上门
            Map<String, Object> addressInfo = shopMemberOrderFacade.getAddressInfo(orderType, shopMemberDeliveryAddressId, shopDetail, memberDetail);
            //获取该用户的收货信息 //默认到店取货
            addressInfo.put("dorderType", ShopMemberOrder.order_type_get_product_at_store);
            //商家名称
            addressInfo.put("dbusinessName", shopDetail.getStoreBusiness().getBusinessName());
            //到店取货地址
            addressInfo.put("daddress", shopDetail.getStoreBusiness().getBusinessAddress());
            data.put("storeBusiness", shopDetail.getStoreBusiness());
            data.put("addressInfo", addressInfo);

            rsDataList.add(data);
        }
        return rsDataList;
    }

    /**
     * 获取某个用户的购物车详情
     *
     * @param memberId
     * @date: 2018/4/20 9:27
     * @author: Aison
     */
    public List<Map<String, Object>> shopCarList(Long memberId) {

        List<Map<String,Object>> cartList = shopGoodsCarMapper.shopGoodsCarList(memberId, Arrays.asList(1,2));

        for (Map<String, Object> stringObjectMap : cartList) {
           Object mainImg =  stringObjectMap.get("mainImg");
           //测试线空指针报错, 这个code没有同步到生产环境, 暂时是测试线发现问题, updateBy Charlie 18/12/06
            if (mainImg != null) {
               JSONArray array =  JSONArray.parseArray(mainImg.toString());
               if(array!=null && array.size()>0) {
                   stringObjectMap.put("mainImg",array.get(0));
               }
            }
        }
        return cartList;
    }





    /**
     * 将正常状态的购物车状态设为失效
     *
     * @param shopProductId 门店用户商品id
     * @param storeId 门店用户id
     * @author Charlie(唐静)
     * @date 2018/7/11 17:29
     */
    @Transactional(rollbackFor = Exception.class)
    public void adviceGoodsCarThisProductHasDisabled(Long shopProductId, Long storeId) {

        Wrapper<ShopGoodsCar> wrapper = new EntityWrapper<>();
        wrapper.eq("store_id", storeId);
        wrapper.eq("car_suk_status", NORMAL);
        wrapper.eq("shop_product_id", shopProductId);
        List<ShopGoodsCar> shopGoodsCars = shopGoodsCarMapper.selectList(wrapper);

        long current = System.currentTimeMillis();
        for (ShopGoodsCar car : shopGoodsCars) {
            car.setCarSukStatus(DISABLED);
            car.setLastUpdateTime(current);
            if (shopGoodsCarMapper.updateById(car) != 1) {
                throw new RuntimeException("未能更新购物车状态");
            }
        }
    }

    public void joinShopCarWithLive(ShopGoodsCar shopGoodsCar) {

        logger.info("加入购物 car={}" , shopGoodsCar);
        //判断参数合法性
        if (shopGoodsCar.getMemberId() == null) {
            throw BizException.defulat().paramError();
        }
        // 如果sku为空 且productid也为空则是错误的
        if(BizUtil.hasEmpty(shopGoodsCar.getProductSkuId()) && BizUtil.hasEmpty(shopGoodsCar.getProductId())) {
            throw BizException.defulat().paramError();
        }

        ShopProduct shopProduct =  shopProductMapper.selectById(shopGoodsCar.getProductId());
        if(shopProduct== null) {
            throw BizException.defulat().msg("获取不到商品信息");
        }
        // 当前商城下面此商品是否在售
        if(shopProduct.getSoldOut()!=1) {
            throw BizException.defulat().msg("此商品已经下架,加入购物车失败");
        }

        Long productId = null;

        //一键上传商品  和 供应商同款
        if(shopProduct.getOwn().intValue()==0||shopProduct.getOwn().intValue()==2){
            productId = shopProduct.getProductId();
        }

        Long skuNumber = shopGoodsCar.getSkuNumber();
        //如果少于 1 则默认为1
        if (skuNumber == null || skuNumber <= 0) {
            shopGoodsCar.setSkuNumber(1L);
        }
        // 判断是否已经在购物车中了 如果已经存在购物车中 则数量+1;
        // 查询是否有此条记录


        ShopGoodsCar existCar = null;

        // 没有sku的情况 则判断shop_product_id是存在
        ShopGoodsCar param = new ShopGoodsCar();
        param.setMemberId(shopGoodsCar.getMemberId());
        param.setStoreId(shopGoodsCar.getStoreId());
        Long liveProductId = shopGoodsCar.getLiveProductId();
        if(shopGoodsCar.getProductSkuId()==0) {
            ShopGoodsCar history = shopGoodsCarMapper.selectShopGoodsCar(shopGoodsCar.getMemberId(), shopGoodsCar.getStoreId(),shopGoodsCar.getProductId(), liveProductId);
//            param.setShopProductId(shopGoodsCar.getProductId());
//            ShopGoodsCar history = shopGoodsCarMapper.selectOne(param);
            //已失效的不算
            if (history != null && !ObjectUtils.nullSafeEquals (history.getCarSukStatus(), 2)) {
                existCar = history;
            }
        } else {
//            param.setProductSkuId(shopGoodsCar.getProductSkuId());
//            existCar = shopGoodsCarMapper.selectOne(param);
            existCar=shopGoodsCarMapper.selectShopGoodsCarNew(shopGoodsCar.getMemberId(), shopGoodsCar.getStoreId(),shopGoodsCar.getProductSkuId(), liveProductId);
        }
        // 已经添加过一次了
        int rs;
        if (existCar != null) {
            existCar.setSkuNumber(existCar.getSkuNumber() + shopGoodsCar.getSkuNumber());
            existCar.setLastUpdateTime(System.currentTimeMillis());
            existCar.setProductId(productId);
            existCar.setStoreId(shopGoodsCar.getStoreId());
            existCar.setShopProductId(shopProduct.getId());

            rs = shopGoodsCarMapper.updateById(existCar);
        } else {
            shopGoodsCar.setProductId(productId);
            shopGoodsCar.setCreateTime(System.currentTimeMillis());
            shopGoodsCar.setLastUpdateTime(shopGoodsCar.getCreateTime());
            shopGoodsCar.setShopProductId(shopProduct.getId());

            rs = shopGoodsCarMapper.insert(shopGoodsCar);
        }
        if (rs == 0) {
            //添加失败抛出异常
            throw BizException.defulat().msg("加入购物车失败");
        }


    }
}
