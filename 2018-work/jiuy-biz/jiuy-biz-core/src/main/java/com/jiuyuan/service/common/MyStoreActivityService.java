package com.jiuyuan.service.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.*;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.entity.order.ShopGoodsCar;
import com.jiuyuan.service.common.area.ActivityInfoCache;
import com.jiuyuan.service.common.area.BizCacheKey;
import com.jiuyuan.service.common.area.IBizCacheService;
import com.jiuyuan.util.BizException;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.util.TipsMessageException;
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

@Service
public class MyStoreActivityService implements IMyStoreActivityService {
    private static final Logger logger = LoggerFactory.getLogger(MyStoreActivityService.class);

    /**
     *
     * 购物车状态:正常
     */
    private static final int NORMAL = 1;
    /**
     * 购物车状态:失效
     */
    private static final int DISABLED = 2;
    /**
     * 团购活动
     */
    public static final int TEAM_ACTIVITY = 1;
    /**
     * 秒杀活动
     */
    public static final int SECOND_ACTIVITY = 2;

    /**
     * 人数成团(3.7.9以前版本),
     */
    public static final int TEAM_TYPE_USER = 1;
    /**
     * 件数成团
     */
    public static final int TEAM_TYPE_PRODUCT = 2;
    /**
     * 团购活动库存存活时间
     */
    private static final int TEAM_INVENTORY_SURVIVAL_TIME = 60 * 60 * 24 * 15;
    /**
     * 秒杀活动库存存活时间
     */
    private static final int SECOND_INVENTORY_SURVIVAL_TIME = 60 * 60 * 24 * 15;


    @Autowired
    private ShopMemberOrderNewMapper shopMemberOrderNewMapper;
    @Autowired
    private ShopGoodsCarNewMapper shopGoodsCarMapper;
    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Autowired
    private TeamBuyActivityMapper teamBuyActivityMapper;

    @Autowired
    private IBizCacheService bizCacheService;

    @Autowired
    private SecondBuyActivityMapper secondBuyActivityMapper;

    @Autowired
    private ShopProductNewService shopProductNewService;
    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;
    @Autowired
    private StoreMapper storeMapper;



    /**
     * 获取商家商家活动状态
     *
     * @param shopProductId
     * @param storeId
     * @return 商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
     */
    @Override
    public int getShopProductActivityState2(long shopProductId, long storeId) {

//        int wxaType = storeBusinessNewService.findWxaTypeById(storeId);//小程序类型：0个人、1企业小程序
        int intoActivity = 0;
        //企业版才可有秒杀和团购活动
        if (existTeamBuyActivityActivity(shopProductId, storeId) != 0) {
            intoActivity = 1;
        } else {

            if (existCurrentSecondBuyActivity(shopProductId, storeId) != 0) {
                intoActivity = 2;
            }
        }
        return intoActivity;
    }


    /**
     * 获取商家商家活动状态
     *
     * @param shopProductId
     * @param storeId
     * @return 商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
     */
    @Override
    public int getShopProductActivityState(long shopProductId, long storeId) {
        StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(storeId);
        int wxaType = storeBusiness.getWxaType();//小程序类型：0个人、1企业小程序
        int intoActivity = 0;
        //企业版才可有秒杀和团购活动
        if (wxaType == 1) {
            TeamBuyActivity teamBuyActivity = getCurrentTeamBuyActivity(shopProductId, storeId);
            if (teamBuyActivity != null) {
                intoActivity = 1;
            } else {
                SecondBuyActivity secondBuyActivity = getCurrentSecondBuyActivity(shopProductId, storeId);
                if (secondBuyActivity != null) {
                    intoActivity = 2;
                }
            }
        }
        return intoActivity;
    }


    @Override
    public long existTeamBuyActivityActivity(long shopProductId, long storeId) {

        TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.existOrderByTime(storeId, shopProductId, System.currentTimeMillis());

        return teamBuyActivity == null ? 0 : teamBuyActivity.getId();
    }


    @Override
    public long existCurrentSecondBuyActivity(long shopProductId, long storeId) {
        SecondBuyActivity exist = secondBuyActivityMapper.existOrderByTime(storeId, shopProductId, System.currentTimeMillis());
        return exist == null ? 0 : exist.getId();

    }


    /**
     * 抢购秒杀商品
     *
     * @param activityId 活动id
     * @param buyCount   购买数量,或参团人数
     * @param storeId    storeId
     * @return true 抢到了，false未抢到
     * updateBy Charlie
     * @date 2018/8/1 14:33
     */
    @Override
    public boolean rushSecondBuyProduct(Long activityId, Integer buyCount, Long storeId) {
        logger.info("抢购秒杀商品 ==>activityId = [" + activityId + "], buyCount = [" + buyCount + "], storeId = [" + storeId + "]");

        String cacheKey = secondCacheKey(activityId);
        String inventory = bizCacheService.get(cacheKey);
        if (StringUtils.isBlank(inventory)) {
            logger.error("抢购秒杀, 缓存中没有库存信息. cacheKey[{}].", cacheKey);
            return false;
        }

        boolean isInventoryEnough = isActivityInventoryEnough(Integer.parseInt(inventory), buyCount, SECOND_ACTIVITY);
        if (!isInventoryEnough) {
            logger.info("团购抢购,库存不够 activityId:" + activityId + ",count:" + inventory + ",buyCount:" + buyCount);
            return false;
        }


        int rec = secondActIncreaseJoinUser(activityId, buyCount);
        if (rec == 1) {
            logger.info("抢购商品成功, 更新参团人数/购买件数成功 activityId:" + activityId);
        } else {
            logger.info("抢购商品成功, 更新参团人数/购买件数失败 activityId:" + activityId);
            return false;
        }

        //抢购
        Long decr = bizCacheService.decr(cacheKey, buyCount);
        if (decr >= 0) {
            logger.info("====抢购商品rushSecondBuyProduct 成功 ===>" + "key:" + cacheKey + ",原来数量:" + inventory + ",购买数量:" + buyCount + ",减库存后数量" + decr);
            return true;
        } else {
            //抢购失败回滚库存
            logger.info("====抢购商品rushSecondBuyProduct 失败 ===>" + "key:" + cacheKey + ",原来数量:" + inventory + ",购买数量:" + buyCount + ",减库存后数量" + decr);
            logger.info("====库存回滚中....");
            Long afterReturn = bizCacheService.incr(cacheKey, buyCount);
            logger.info("====抢购商品rushSecondBuyProduct,回滚库存 activityId" + activityId + ",抢购前:" + inventory + ",抢购后:" + decr + ",库存回滚后:" + afterReturn);
            return false;
        }

    }


    /**
     * 抢购团购商品
     *
     * @param activityId    活动id
     * @param conditionType 活动类别
     * @param buyCount      购买数量,或参团人数
     * @param storeId       storeId
     * @return true 抢到了，false未抢到
     * updateBy Charlie
     * @date 2018/8/1 14:33
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rushBuyTeamBuyProduct(Long activityId, Integer conditionType, Integer buyCount, Long storeId) {
        logger.info("抢购团购商品 ===> activityId = [" + activityId + "], conditionType = [" + conditionType + "], buyCount = [" + buyCount + "], storeId = [" + storeId + "]");


        //查询库存
        String key = teamCacheKey(activityId);
        String inventory = bizCacheService.get(key);
        if (StringUtils.isBlank(inventory)) {
            logger.error("未找到库存信息,团购下单失败 key:" + key + ",inventory:" + inventory);
            return false;
        }

        Integer count = Integer.parseInt(inventory);
        boolean isInventoryEnough = isActivityInventoryEnough(count, buyCount, TEAM_ACTIVITY);
        if (!isInventoryEnough) {
            logger.info("团购抢购,库存不够 activityId:" + activityId + ",count:" + count + ",buyCount:" + buyCount);
            return false;
        }

        //记录参加人数/商品件数
        int rec = teamActIncreaseUserOrProduct(activityId, conditionType, buyCount);
        if (rec != 1) {
            String msg = "抢购团购, 更新参团人数/购买件数失败 activityId:" + activityId + ",conditionType:" + conditionType + ",buyCount:" + buyCount;
            logger.error(msg);
            return false;
        } else {
            logger.info("抢购团购, 更新参团人数/购买件数成功 activityId:" + activityId + ",conditionType:" + conditionType + ",buyCount:" + buyCount);
        }

        //抢购
        Long afterDecr = bizCacheService.decr(key, buyCount);
        boolean isRushOk = afterDecr != null && afterDecr >= 0;

        if (isRushOk) {
            logger.info("====抢购商品rushBuyTeamBuyProduct成功 ===>" + "key:" + key + ",原来数量:" + count + ",购买数量" + buyCount + ",减库存后数量" + afterDecr);
            return true;
        } else {
            //失败库存回滚
            logger.info("====抢购商品rushBuyTeamBuyProduct失败 ===>" + "key:" + key + ",原来数量:" + count + ",购买数量" + buyCount + ",减库存后数量" + afterDecr);
            logger.info("====库存回滚中....");
            Long rollBackCount = bizCacheService.incr(key, buyCount + 10);
            logger.info("====抢购商品rushBuyTeamBuyProduct,回滚库存 activityId" + activityId + ",抢购前:" + count + ",抢购后:" + afterDecr + ",库存回滚后:" + rollBackCount);
            return false;
        }
    }

    /**
     * 团购活动库存是否足够
     *
     * @param inventory     剩余库存
     * @param buyCount      购买库存
     * @param whichActivity 活动种类(保留逻辑)
     *                      {@link MyStoreActivityService#TEAM_ACTIVITY, MyStoreActivityService#SECOND_ACTIVITY}
     * @return true 足够
     * @author Charlie
     * @date 2018/8/1 17:57
     */
    private static boolean isActivityInventoryEnough(Integer inventory, Integer buyCount, Integer whichActivity) {
        //暂时两种活动判断逻辑一致,老版本人数成团一次只能购买一件,所以不会存在商品卖完了,但是人数还没成团的情况)
        return inventory >= buyCount;
    }


    /**
     * 获取当前秒杀活动
     *
     * @param shopProductId
     * @return
     */
    @Override
    public SecondBuyActivity getCurrentSecondBuyActivity(long shopProductId, long storeId) {
        ActivityInfoCache actCache = ActivityInfoCache.get();
        if (actCache != null) {
            logger.info("获取当前商品的活动 wxVersion[{}].ActivityType[{}].ActivityId[{}]", actCache.getWxVersion(), actCache.getActivityType(), actCache.getActivityId());
            if (actCache.getWxVersion() >= ActivityInfoCache.VERSION_140) {
                //如果新版本
                if (ObjectUtils.nullSafeEquals(actCache.getActivityType(), 2)) {
                    if (actCache.getActivityId() == null) {
                        throw BizException.defulat().msg("活动id不可为空");
                    }
                    //秒杀活动
                    SecondBuyActivity query = new SecondBuyActivity();
                    query.setId(actCache.getActivityId());
                    query.setStoreId(storeId);
                    query.setShopProductId(shopProductId);
                    query.setDelState(SecondBuyActivity.NORMAL_STATE);
                    return secondBuyActivityMapper.selectOne(query);
                } else if (ObjectUtils.nullSafeEquals(actCache.getActivityType(), 1)) {
                    //没有秒杀活动
                    return null;
                } else {
                    //找商品优先级活动,ignore
                }
            }
        }
        //老版本
        //查找进行中,待开始的,待开始优先, 然后是结束时间最近的一个活动
        List<SecondBuyActivity> list = secondBuyActivityMapper.selectOrderByTime(storeId, shopProductId, System.currentTimeMillis());
        return list.isEmpty() ? null : list.get(0);

    }


    /**
     * 获取当前团购活动
     *
     * @param shopProductId update by Charlie
     * @return
     */
    @Override
    public TeamBuyActivity getCurrentTeamBuyActivity(long shopProductId, long storeId) {
        ActivityInfoCache actCache = ActivityInfoCache.get();
        if (actCache != null) {
            logger.info("获取当前商品的活动 wxVersion[{}].ActivityType[{}].ActivityId[{}]", actCache.getWxVersion(), actCache.getActivityType(), actCache.getActivityId());
            if (actCache.getWxVersion() >= ActivityInfoCache.VERSION_140) {
                //如果新版本
                if (ObjectUtils.nullSafeEquals(1, actCache.getActivityType())) {
                    if (actCache.getActivityId() == null) {
                        throw BizException.defulat().msg("活动id不可为空");
                    }
                    //团购活动
                    TeamBuyActivity query = new TeamBuyActivity();
                    query.setId(actCache.getActivityId());
                    query.setStoreId(storeId);
                    query.setShopProductId(shopProductId);
                    query.setDelState(TeamBuyActivity.NORMAL_STATE);
                    return teamBuyActivityMapper.selectOne(query);
                } else if (ObjectUtils.nullSafeEquals(2, actCache.getActivityType())) {
                    //没有活动
                    return null;
                } else {
                    //找商品优先级活动,ignore
                }
            }
        }

        //老版本
        //查找进行中,待开始的,待开始优先, 然后是结束时间最近的一个活动
        List<TeamBuyActivity> list = teamBuyActivityMapper.selectOrderByTime(storeId, shopProductId, System.currentTimeMillis());
        return list.isEmpty() ? null : list.get(0);

    }


    @Override
    public List<Map<String, Object>> getShopProductList(String keyWords, long storeId, Page<Map<String, Object>> page) {
        List<Map<String, Object>> shopProductList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list = shopProductMapper.searchValidShopProductList(keyWords, storeId, page);
        //获取上架时间降序排列的已上架商品
        for (Map<String, Object> map : list) {
            Map<String, Object> shopProductMap = new HashMap<String, Object>();
            if (((int) map.get("own")) == 0) {
                shopProductMap.put("id", map.get("id"));//门店商品Id
                shopProductMap.put("productId", map.get("product_id"));//平台商品Id
                shopProductMap.put("productName", map.get("productName"));//商品名称
                shopProductMap.put("mainImg", map.get("mainImg"));//商品主图
                shopProductMap.put("productPrice", map.get("price") == null ? "" : map.get("price"));//商品价格
            } else if (((int) map.get("own")) == 1 || ((int) map.get("own")) == 2) {
                shopProductMap.put("id", map.get("id"));//门店商品Id
                shopProductMap.put("productId", map.get("product_id"));//平台商品Id
                shopProductMap.put("productName", map.get("name"));//商品名称
                String summaryImages = (String) map.get("summary_images");
                JSONArray array = JSON.parseArray(summaryImages);
                String mainImg = "";
                if (array == null) {

                } else {
                    String[] images = array.toArray(new String[]{});
                    if (images != null && images.length > 0) {
                        mainImg = images[0];
                    }
                }
                shopProductMap.put("mainImg", mainImg);//商品主图

                shopProductMap.put("productPrice", map.get("price") == null ? "" : map.get("price"));//商品价格
            }
            shopProductList.add(shopProductMap);

        }


        return shopProductList;
    }

    /**
     * 创建团购活动
     *
     * @param conditionType;    成团条件类型 1:人数成团(3.7.9以前版本),2:件数成团
     * @param meetProductCount; 成团件数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTeamActivity(String activityTitle, int userCount, long activityStartTime, long activityEndTime,
                                double activityPrice, int activityProductCount, long shopProductId, Integer conditionType, Integer
                                        meetProductCount, long storeId) {
        long currentTime = System.currentTimeMillis();
        //校验数据
        checkActivityParam(activityTitle, userCount, activityStartTime, activityEndTime, activityPrice, activityProductCount,
                shopProductId, storeId, currentTime, 1, conditionType, meetProductCount);

        //判断是否该活动能够创建
        boolean ok = checkTeamBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
        if (!ok) {
            logger.info("本商品该时间段内已有团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
            throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
        } else {
            ok = checkSecondBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
            if (!ok) {
                logger.info("本商品该时间段内已有秒杀活动，请重新选择开始时间和截止时间！storeId:" + storeId);
                throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
            }
        }

        //校验当前活动商品是否符合创建活动
        checkProductChoice(0L, shopProductId);

        //判断当前活动该用户是否有未结束的，如果有就不能创建
//	    List<TeamBuyActivity> list = searchRunningTeamBuyActivity(storeId);
//	    if(list.size() >0){
//	    	logger.info("尚有未结束的团购活动，无法再次重建！storeId:"+storeId);
//	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
//	    }
        //创建活动
        ShopProduct shopProduct2 = shopProductNewService.getShopProductInfoNoStock(shopProductId);
        //创建团购
        TeamBuyActivity teamBuyActivity = new TeamBuyActivity();
        teamBuyActivity.setActivityTitle(activityTitle);//标题
        teamBuyActivity.setStoreId(storeId);//商家Id
        teamBuyActivity.setShopProductId(shopProduct2.getId());//活动商品Id
        teamBuyActivity.setShopProductName(shopProduct2.getName());//活动商品名称
        teamBuyActivity.setClothesNumber(shopProduct2.getClothesNumber() == null ? "" : shopProduct2.getClothesNumber());//活动商品款号
        teamBuyActivity.setShopProductMainimg(shopProduct2.getFirstImage());//活动商品主图
        /*
         * 活动商品原价格
         * 历史遗留有price为空的情况,是没有price的,这里将那些为null的脏数据设为0,那些脏数据也不会用的
         */
        teamBuyActivity.setActivityProductPrice(shopProduct2.getPrice() == null ? 0 : shopProduct2.getPrice());
        teamBuyActivity.setActivityPrice(activityPrice);//活动价格
        teamBuyActivity.setUserCount(userCount);//成团人数
        teamBuyActivity.setActivityProductCount(activityProductCount);//活动商品数量
        teamBuyActivity.setActivityStartTime(activityStartTime);//活动有效开始时间
        teamBuyActivity.setActivityEndTime(activityEndTime);//活动有效截止时间
        teamBuyActivity.setConditionType(conditionType);
        teamBuyActivity.setMeetProductCount(meetProductCount);
        teamBuyActivity.setCreateTime(currentTime);//创建时间
        teamBuyActivity.setUpdateTime(currentTime);//更新时间
        teamBuyActivity.setDelState(TeamBuyActivity.NORMAL_STATE);//正常状态
        teamBuyActivity.setShopProductShowcaseImgs(shopProduct2.getSummaryImages());//商品活动橱窗图
        teamBuyActivityMapper.insert(teamBuyActivity);//保存建立信息

        //通知购物车该商品已失效
        adviceGoodsCarThisProductHasDisabled(shopProductId, storeId);
    }


    /**
     * 将正常状态的购物车状态设为失效
     *
     * @param shopProductId 门店用户商品id
     * @param storeId       门店用户id
     * @author Charlie(唐静)
     * @date 2018/7/11 17:29
     */
    @Override
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


    /**
     * 创建秒杀活动
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSecondActivity(String activityTitle, long activityStartTime, long activityEndTime,
                                  double activityPrice, int activityProductCount, long shopProductId, long storeId) {
        long currentTime = System.currentTimeMillis();

        //校验数据
        checkActivityParam(activityTitle, null, activityStartTime, activityEndTime, activityPrice, activityProductCount,
                shopProductId, storeId, currentTime, 2, null, null);

        //判断是否该活动能够创建
        boolean ok = checkSecondBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
        if (!ok) {
            logger.info("该时间段内已有秒杀团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
            throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
        } else {
            //活动能被新建表明没有团购
            ok = checkTeamBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
            if (!ok) {
                logger.info("本商品该时间段内已有团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
                throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
            }
        }

        //校验当前活动商品是否符合创建活动
        checkProductChoice(0L, shopProductId);

        //判断当前活动该用户是否有未结束的，如果有就不能创建
//	    List<SecondBuyActivity> list = searchRunningSecondBuyActivity(storeId);
//	    if(list.size() >0){
//	    	logger.info("尚有未结束的秒杀活动，无法再次重建！storeId:"+storeId);
//	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
//	    }

        //创建活动
        ShopProduct shopProduct2 = shopProductNewService.getShopProductInfoNoStock(shopProductId);
        //创建秒杀活动
        SecondBuyActivity secondBuyActivity = new SecondBuyActivity();
        secondBuyActivity.setActivityTitle(activityTitle);//标题
        secondBuyActivity.setStoreId(storeId);//商家Id
        secondBuyActivity.setShopProductId(shopProduct2.getId());//活动商品Id
        secondBuyActivity.setShopProductName(shopProduct2.getName());//活动商品名称
        secondBuyActivity.setClothesNumber(shopProduct2.getClothesNumber() == null ? "" : shopProduct2.getClothesNumber());//活动商品款号
        secondBuyActivity.setShopProductMainimg(shopProduct2.getFirstImage());//活动商品主图
        secondBuyActivity.setActivityProductPrice(shopProduct2.getPrice() == null ? null : shopProduct2.getPrice());//活动商品原价格
        secondBuyActivity.setActivityPrice(activityPrice);//活动价格
        secondBuyActivity.setActivityProductCount(activityProductCount);//活动商品数量
        secondBuyActivity.setActivityStartTime(activityStartTime);//活动有效开始时间
        secondBuyActivity.setActivityEndTime(activityEndTime);//活动有效截止时间
        secondBuyActivity.setCreateTime(currentTime);//创建时间
        secondBuyActivity.setUpdateTime(currentTime);//更新时间
        secondBuyActivity.setDelState(TeamBuyActivity.NORMAL_STATE);//正常状态
        secondBuyActivity.setShopProductShowcaseImgs(shopProduct2.getSummaryImages());//商品活动橱窗图
        secondBuyActivityMapper.insert(secondBuyActivity);//保存建立信息

        adviceGoodsCarThisProductHasDisabled(shopProductId, storeId);
    }

    /**
     * 编辑团购活动
     */
    @Override
    public void editTeamActivity(String activityTitle, int userCount, long activityStartTime, long activityEndTime,
                                 double activityPrice, int activityProductCount, long shopProductId, long teamActivityId, long storeId,
                                 Integer conditionType, Integer meetProductCount) {
        long currentTime = System.currentTimeMillis();
        TeamBuyActivity historyTeam = teamBuyActivityMapper.selectById(teamActivityId);
        long startTime = historyTeam.getActivityStartTime();
        if (currentTime > startTime) {
            logger.info("该团购活动已经开始，无法编辑！teamActivityId:" + teamActivityId);
            throw new ResultCodeException(ResultCode.CANT_EDIT_ACTIVITY_AFTER_START);
        }
        //校验数据
        checkActivityParam(activityTitle, userCount, activityStartTime, activityEndTime, activityPrice, activityProductCount,
                shopProductId, storeId, currentTime, 1, conditionType, meetProductCount);
        //判断是否该活动能够创建
        boolean ok = checkTeamBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, teamActivityId);
        if (!ok) {
            logger.info("本商品该时间段内已有团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
            throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
        } else {
            //活动能被新建表名没有秒杀
            ok = checkSecondBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
            if (!ok) {
                logger.info("该时间段内已有秒杀活动，请重新选择开始时间和截止时间！storeId:" + storeId);
                throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
            }
        }
        //校验当前活动商品是否符合创建活动
        checkProductChoice(historyTeam.getShopProductId(), shopProductId);

        //判断当前活动该用户是否有未结束的，如果有就不能创建
/*	    List<TeamBuyActivity> list = searchRunningTeamBuyActivity(storeId);
	    if(list.size() >1){
	    	logger.info("尚有未结束的团购活动，无法再次重建！storeId:"+storeId);
	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
	    }
	    
	    if(list.size() == 1 && list.get(0).getId() != teamActivityId){
	    	logger.info("尚有未结束的团购活动，无法再次重建！storeId:"+storeId);
	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
	    }*/
	    
	    //编辑活动
	    ShopProduct shopProduct2 = shopProductNewService.getShopProductInfoNoStock(shopProductId);
	    //更新团购
		TeamBuyActivity teamBuyActivity = new TeamBuyActivity();
		teamBuyActivity.setId(teamActivityId);//活动Id
		teamBuyActivity.setActivityTitle(activityTitle);//标题
		teamBuyActivity.setStoreId(storeId);//商家Id
		teamBuyActivity.setShopProductId(shopProduct2.getId());//活动商品Id
		teamBuyActivity.setShopProductName(shopProduct2.getName());//活动商品名称
		teamBuyActivity.setClothesNumber(shopProduct2.getClothesNumber()==null?"":shopProduct2.getClothesNumber());//活动商品款号
		teamBuyActivity.setShopProductMainimg(shopProduct2.getFirstImage());//活动商品主图
		teamBuyActivity.setActivityProductPrice(shopProduct2.getPrice ());//活动商品原价格
		teamBuyActivity.setActivityPrice(activityPrice);//活动价格
		teamBuyActivity.setUserCount(userCount);//成团人数
		teamBuyActivity.setActivityProductCount(activityProductCount);//活动商品数量
		teamBuyActivity.setActivityStartTime(activityStartTime);//活动有效开始时间
		teamBuyActivity.setActivityEndTime(activityEndTime);//活动有效截止时间
		teamBuyActivity.setCreateTime(currentTime);//创建时间
		teamBuyActivity.setUpdateTime(currentTime);//更新时间
		teamBuyActivity.setConditionType (conditionType);//更新时间
		teamBuyActivity.setDelState(TeamBuyActivity.NORMAL_STATE);//正常状态
		teamBuyActivity.setShopProductShowcaseImgs(shopProduct2.getSummaryImages());//商品活动橱窗图
		teamBuyActivity.setMeetProductCount (meetProductCount);
		teamBuyActivityMapper.updateById(teamBuyActivity);//更新
		//更新价格
		teamBuyActivityMapper.updatePrice(teamActivityId, shopProduct2.getPrice ());

        if (historyTeam.getShopProductId() != shopProductId) {
            adviceGoodsCarThisProductHasDisabled(shopProductId, storeId);
        }
    }

    /**
     * 编辑秒杀活动
     */
    @Override
    public void editSecondActivity(String activityTitle, long activityStartTime, long activityEndTime,
                                   double activityPrice, int activityProductCount, long shopProductId, long secondActivityId, long storeId) {
        long currentTime = System.currentTimeMillis();
        SecondBuyActivity historySecond = secondBuyActivityMapper.selectById(secondActivityId);
        long startTime = historySecond.getActivityStartTime();
        if (currentTime > startTime) {
            logger.info("该团购活动已经开始，无法编辑！secondActivityId:" + secondActivityId);
            throw new ResultCodeException(ResultCode.CANT_EDIT_ACTIVITY_AFTER_START);
        }
        //校验数据
        checkActivityParam(activityTitle, null, activityStartTime, activityEndTime, activityPrice, activityProductCount,
                shopProductId, storeId, currentTime, 2, null, null);
        //判断是否该活动能够创建
        boolean ok = checkSecondBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, secondActivityId);
        if (!ok) {
            logger.info("该时间段内已有秒杀团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
            throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
        } else {
            //活动能被新建表明没有团购
            ok = checkTeamBuyActivity(storeId, activityStartTime, activityEndTime, shopProductId, 0L);
            if (!ok) {
                logger.info("本商品该时间段内已有团购活动，请重新选择开始时间和截止时间！storeId:" + storeId);
                throw new ResultCodeException(ResultCode.RESTART_CHOOSE_START_END_TIME);
            }
        }

        //校验当前活动商品是否符合创建活动
        checkProductChoice(historySecond.getShopProductId(), shopProductId);
	    
	    /*//判断当前活动该用户是否有未结束的，如果有就不能创建
	    List<SecondBuyActivity> list = searchRunningSecondBuyActivity(storeId);
	    if(list.size() >1){
	    	logger.info("尚有未结束的团购活动，无法再次重建！storeId:"+storeId);
	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
	    }
	    
	    if(list.size() == 1 && list.get(0).getId() != secondActivityId){
	    	logger.info("尚有未结束的团购活动，无法再次重建！storeId:"+storeId);
	    	throw new ResultCodeException(ResultCode.CANT_CREATE_ACTIVITY_IN_ACTIVITY_PERIOD);
	    }*/

        //编辑活动
        ShopProduct shopProduct2 = shopProductNewService.getShopProductInfoNoStock(shopProductId);
        //更新团购
        SecondBuyActivity secondBuyActivity = new SecondBuyActivity();
        secondBuyActivity.setId(secondActivityId);//活动Id
        secondBuyActivity.setActivityTitle(activityTitle);//标题
        secondBuyActivity.setStoreId(storeId);//商家Id
        secondBuyActivity.setShopProductId(shopProduct2.getId());//活动商品Id
        secondBuyActivity.setShopProductName(shopProduct2.getName());//活动商品名称
        secondBuyActivity.setClothesNumber(shopProduct2.getClothesNumber() == null ? "" : shopProduct2.getClothesNumber());//活动商品款号
        secondBuyActivity.setShopProductMainimg(shopProduct2.getFirstImage());//活动商品主图
        secondBuyActivity.setActivityProductPrice(shopProduct2.getPrice() == null ? null : shopProduct2.getPrice());//活动商品原价格
        secondBuyActivity.setActivityPrice(activityPrice);//活动价格
        secondBuyActivity.setActivityProductCount(activityProductCount);//活动商品数量
        secondBuyActivity.setActivityStartTime(activityStartTime);//活动有效开始时间
        secondBuyActivity.setActivityEndTime(activityEndTime);//活动有效截止时间
        secondBuyActivity.setCreateTime(currentTime);//创建时间
        secondBuyActivity.setUpdateTime(currentTime);//更新时间
        secondBuyActivity.setDelState(TeamBuyActivity.NORMAL_STATE);//正常状态
        secondBuyActivity.setShopProductShowcaseImgs(shopProduct2.getSummaryImages());//商品活动橱窗图
        secondBuyActivityMapper.updateById(secondBuyActivity);//更新
        //更新商品价格
        secondBuyActivityMapper.updatePrice(secondActivityId, shopProduct2.getPrice() == null ? null : shopProduct2.getPrice());

        if (historySecond.getShopProductId() != shopProductId) {
            adviceGoodsCarThisProductHasDisabled(shopProductId, storeId);
        }
    }

    /**
     * 同一商品在同一时间段只能有一个活动, 多个商品可以在同一时间有多个活动, 同一商品可以在多个时间段有多个活动
     *
     * @param storeId
     * @param activityStartTime
     * @param activityEndTime
     * @param shopProductId
     * @param teamActivityId
     * @return
     */
    private boolean checkTeamBuyActivity(long storeId, long activityStartTime, long activityEndTime,
                                         long shopProductId, long teamActivityId) {
        List<TeamBuyActivity> teams = teamBuyActivityMapper.findBetweenSameTimeSliceWithProduct(storeId, shopProductId, activityStartTime, activityEndTime);
        if (teamActivityId == 0) {
            //新增
            return teams == null || teams.isEmpty();
        } else {
            //编辑
            if (teams == null || teams.isEmpty()) {
                return true;
            } else if (teams.size() > 1) {
                return false;
            } else {
                return teams.get(0).getId().equals(teamActivityId);
            }
        }
		/*//搜索该门店未结束的活动
		List<TeamBuyActivity> list =  searchRunningTeamBuyActivity(storeId);
		//遍历判断当前活动是否能够建立
		for(TeamBuyActivity teamBuyActivity:list){
			long startTime = teamBuyActivity.getActivityStartTime();
			long endTime = teamBuyActivity.getActivityEndTime();
			if(teamBuyActivity.getId() != teamActivityId){
				if(activityStartTime >= startTime && activityStartTime <= endTime){
					logger.info("团购活动创建失败，创建的开始时间在已创建的活动中！storeId:"+storeId);
					return false;
				}
				if(activityEndTime >= startTime && activityEndTime <= endTime){
					logger.info("团购活动创建失败，创建的截止时间在已创建的活动中！storeId:"+storeId);
					return false;
				}
				if(activityStartTime <= startTime && activityEndTime >= endTime){
					logger.info("团购活动创建失败，创建的活动时间内包含了已创建的活动！storeId:"+storeId);
					return false;
				}
			}

		}*/
//		return true;
    }

    /**
     * 校验该秒杀活动是否能够创建，在时间上
     *
     * @param storeId
     * @param activityStartTime
     * @param activityEndTime
     * @param shopProductId
     * @param secondActivityId
     * @return
     */
    private boolean checkSecondBuyActivity(long storeId, long activityStartTime, long activityEndTime,
                                           long shopProductId, long secondActivityId) {
        List<SecondBuyActivity> seconds = secondBuyActivityMapper.findBetweenSameTimeSliceWithProduct(storeId, shopProductId, activityStartTime, activityEndTime);
        if (secondActivityId == 0) {
            //新增
            return seconds == null || seconds.isEmpty();
        } else {
            //编辑
            if (seconds == null || seconds.isEmpty()) {
                return true;
            } else if (seconds.size() > 1) {
                return false;
            } else {
                return seconds.get(0).getId().equals(secondActivityId);
            }
        }

    }


    /**
     * 校验当前productId是否是可选商品
     */
    @Override
    public void checkProductChoice(long editProductId, long currentProductId) {
        long currentTime = System.currentTimeMillis();
        //校验该商品是否被删除或者下架
        ShopProduct shopProduct = shopProductMapper.selectById(currentProductId);
        //商品不在上架状态
        if (shopProduct.getSoldOut() != ShopProduct.sold_out_up) {
            logger.info("该商品不在上架状态，请选择其他商品！shopProductId:" + currentProductId);
            throw new ResultCodeException(ResultCode.CANT_SET_PRODUCT_AFTER_SOLD_DOWN);
        }
        //商品被删除
        if (shopProduct.getStatus() != ShopProduct.normal_status) {
            logger.info("该商品被删除，请选择其他商品！shopProductId:" + currentProductId);
            throw new ResultCodeException(ResultCode.CANT_SET_PRODUCT_AFTER_DEL);
        }
    }

    /**
     * 校验设置活动参数
     * @param activityTitle
     * @param userCount
     * @param activityStartTime
     * @param activityEndTime
     * @param activityPrice
     * @param activityProductCount
     * @param shopProductId
     * @param storeId
     * @param currentTime
     * @param activityType         活动类型 1: 团购活动, 2:秒杀活动
     * @param conditionType;       成团条件类型 1:人数成团(3.7.9以前版本),2:件数成团
     * @param meetProductCount;    成团件数
     */
    private void checkActivityParam(String activityTitle, Integer userCount, long activityStartTime,
                                    long activityEndTime,
                                    double activityPrice, int activityProductCount, long shopProductId, long storeId, long currentTime,
                                    int activityType, Integer conditionType, Integer meetProductCount
    ) {
        //标题字数
        if (activityTitle.length() > 50) {
            logger.info("团购活动，活动标题字数超标！storeId:" + storeId);
            throw new ResultCodeException(ResultCode.TITEL_LENGTH_TOO_LONG);
        }
        //开始时间
        if (currentTime >= activityStartTime) {
            logger.info("团购活动，活动开始时间无效！");
            throw new ResultCodeException(ResultCode.ACTIVITY_START_TIME_INVALID);
        }
        //截止时间
        if (currentTime >= activityEndTime || activityEndTime <= activityStartTime) {
            logger.info("团购活动，活动截止时间无效！");
            throw new ResultCodeException(ResultCode.ACTIVITY_END_TIME_INVALID);
        }
        //活动价格
        if (activityPrice <= 0) {
            logger.info("团购活动，活动价格设置不合理！");
            throw new ResultCodeException(ResultCode.AVTIVITY_PRICE_UNREASONABLE);
        }
        //活动商品数量
        if (activityProductCount <= 0) {
            logger.info("团购活动，活动商品数量设置不合理！");
            throw new ResultCodeException(ResultCode.AVTIVITY_COUNT_UNREASONABLE);
        }
        //活动商品Id
        ShopProduct shopProduct = shopProductMapper.selectById(shopProductId);
        if (shopProduct.getStoreId() != storeId) {
//			logger.info("团购活动，该商品无法设置活动！storeId:"+storeId+"shopProduct:"+shopProduct);
            throw new ResultCodeException(ResultCode.CANT_SET_PRODUCT);
        }


        if (activityType == 1) {
            //团购活动成团人数,团购件数校验
            if (conditionType == TEAM_TYPE_USER) {
                //成团人数
                if (userCount <= 0) {
                    logger.info("团购活动，成团人数填写非法！storeId:" + storeId);
                    throw new ResultCodeException(ResultCode.USER_COUNT_UNREASONABLE);
                }
                if (userCount > activityProductCount) {
                    logger.info("团购活动，成团人数填写非法！storeId:" + storeId);
                    throw new ResultCodeException(ResultCode.USER_COUNT_UNREASONABLE);
                }
            } else if (conditionType == TEAM_TYPE_PRODUCT) {
                //成团件数
                if (meetProductCount == null || meetProductCount <= 0 || meetProductCount > activityProductCount) {
                    logger.info("团购活动，成团件数填写非法！storeId:" + storeId);
                    throw new ResultCodeException(ResultCode.NO_SUPPORT_MEET_PRODUCT_COUNT);
                }
            } else {
                throw new ResultCodeException(ResultCode.UNKNOWN_TEAM_ACTIVITY_TYPE);
            }
        }

    }


    /**
     * 删除活动
     */
    @Override
    public void deleteAvtivity(long activityId, int type, long storeId) {
        long currentTime = System.currentTimeMillis();
        //搜索当前活动
        long startTime = 0L;
        int delState = 0;
        Object activity = null;
        long activityStoreId = 0L;
        if (type == TEAM_ACTIVITY) {
            activity = teamBuyActivityMapper.selectById(activityId);
            startTime = ((TeamBuyActivity) activity).getActivityStartTime();
            delState = ((TeamBuyActivity) activity).getDelState();
            activityStoreId = ((TeamBuyActivity) activity).getStoreId();
        }
        if (type == SECOND_ACTIVITY) {
            activity = secondBuyActivityMapper.selectById(activityId);
            startTime = ((SecondBuyActivity) activity).getActivityStartTime();
            delState = ((SecondBuyActivity) activity).getDelState();
            activityStoreId = ((SecondBuyActivity) activity).getStoreId();
        }

        if (storeId != activityStoreId) {
            logger.info("该活动您无权删除！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.CANT_DELETE_ACTIVITY);

        }

        //检测活动是否开始已经开始
        if (currentTime > startTime) {
            logger.info("当前活动已经开始，无法删除该活动！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.CANT_DELETE_ACTIVITY_AFTER_START);
        }

        if (delState == TeamBuyActivity.DEL_STATE) {
            logger.info("当前活动已经删除，请勿重复操作！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.DONT_REPEAT_OPERATION_AFTER_DELETE);
        }
        //开始删除该活动
        if (type == TEAM_ACTIVITY) {
            TeamBuyActivity teamBuyActivity = new TeamBuyActivity();
            teamBuyActivity.setId(activityId);
            teamBuyActivity.setDelState(TeamBuyActivity.DEL_STATE);
            teamBuyActivity.setUpdateTime(currentTime);
            teamBuyActivityMapper.updateById(teamBuyActivity);
        }
        if (type == SECOND_ACTIVITY) {
            SecondBuyActivity secondBuyActivity = new SecondBuyActivity();
            secondBuyActivity.setId(activityId);
            secondBuyActivity.setDelState(SecondBuyActivity.DEL_STATE);
            secondBuyActivity.setUpdateTime(currentTime);
            secondBuyActivityMapper.updateById(secondBuyActivity);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handCloseActivity(long activityId, int type, long storeId) {
        long currentTime = System.currentTimeMillis();
        long startTime = 0L;
        long endTime = 0L;
        int delState = 0;
        Object activity = null;
        long activityStoreId = 0L;
        if (type == TEAM_ACTIVITY) {
            activity = teamBuyActivityMapper.selectById(activityId);
            startTime = ((TeamBuyActivity) activity).getActivityStartTime();
            endTime = ((TeamBuyActivity) activity).getActivityEndTime();
            delState = ((TeamBuyActivity) activity).getDelState();
            activityStoreId = ((TeamBuyActivity) activity).getStoreId();
        }
        if (type == SECOND_ACTIVITY) {
            activity = secondBuyActivityMapper.selectById(activityId);
            startTime = ((SecondBuyActivity) activity).getActivityStartTime();
            endTime = ((SecondBuyActivity) activity).getActivityEndTime();
            delState = ((SecondBuyActivity) activity).getDelState();
            activityStoreId = ((SecondBuyActivity) activity).getStoreId();
        }
        if (storeId != activityStoreId) {
            logger.info("该活动您无权结束！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.CANT_HAND_CLOSE_ACTIVITY);

        }
        if (delState == TeamBuyActivity.DEL_STATE) {
            logger.info("当前活动已经删除，无需手动结束！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.NOT_NEED_HAND_CLOSE_AFTER_DELETE);
        }

        if (currentTime >= endTime) {
            logger.info("当前活动已经结束，无需手动结束！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.NOT_NEED_HAND_CLOSE_AFTER_CLOSE);
        }

        if (currentTime < startTime) {
            logger.info("只有正在进行中的活动才能进行手动结束！activityId：" + activityId + ",type:" + type + ",类型1:团购 2:秒杀");
            throw new ResultCodeException(ResultCode.CANT_HAND_CLOSE_ACTIVITY_NOT_IN_ACTIVITY);
        }

        //开始手动结束该活动
        if (type == TEAM_ACTIVITY) {
            TeamBuyActivity teamBuyActivity = new TeamBuyActivity();
            teamBuyActivity.setId(activityId);
            teamBuyActivity.setActivityHandEndTime(currentTime);
            teamBuyActivity.setUpdateTime(currentTime);
            teamBuyActivityMapper.updateById(teamBuyActivity);
        }
        if (type == SECOND_ACTIVITY) {
            SecondBuyActivity secondBuyActivity = new SecondBuyActivity();
            secondBuyActivity.setId(activityId);
            secondBuyActivity.setActivityHandEndTime(currentTime);
            secondBuyActivity.setUpdateTime(currentTime);
            secondBuyActivityMapper.updateById(secondBuyActivity);
        }

        //关闭未付款订单
        List<ShopMemberOrder> list = new ArrayList<ShopMemberOrder>();
        if (type == TEAM_ACTIVITY) {
            list = getUnpaidActivityOrders(storeId, type, activityId);
        }
        if (type == SECOND_ACTIVITY) {
            list = getUnpaidActivityOrders(storeId, type, activityId);
        }
        for (ShopMemberOrder shopMemberOrder : list) {
            //关闭订单
            ShopMemberOrder order = new ShopMemberOrder();
            order.setId(shopMemberOrder.getId());
            order.setOrderStatus(ShopMemberOrder.order_status_order_closed);
            order.setUpdateTime(System.currentTimeMillis());
            order.setCancelReasonType(ShopMemberOrder.CANCEL_REASON_TYPE_STORE_ACTIVITY_CANCEL);//明明是手动结束活动，但是需求2018-2-5产品经理说是设置为活动取消
            order.setOrderStopTime(System.currentTimeMillis());
            order.setCancelReason("商家手动结束活动，关闭订单！");
            shopMemberOrderNewMapper.updateById(order);
        }


    }

    /**
     * 获取当前活动，未付款的活动订单
     *
     * @param storeId
     * @param type
     * @param activityId
     * @return
     */
    public List<ShopMemberOrder> getUnpaidActivityOrders(long storeId, int type, long activityId) {
        Wrapper<ShopMemberOrder> wrapper = new EntityWrapper<ShopMemberOrder>();
        wrapper.eq("store_id", storeId);
        if (type == TEAM_ACTIVITY) {
            wrapper.eq("team_id", activityId);
        } else if (type == SECOND_ACTIVITY) {
            wrapper.eq("second_id", activityId);
        } else {
            return new ArrayList<ShopMemberOrder>();
        }
        wrapper.eq("order_status", ShopMemberOrder.order_status_pending_payment);
        List<ShopMemberOrder> list = shopMemberOrderNewMapper.selectList(wrapper);
        return list;

    }

    /**
     * 搜索未结束的团购活动
     *
     * @param storeId
     * @return
     */
    @Override
    public List<SecondBuyActivity> searchRunningSecondBuyActivity(Long storeId) {
        long currentTime = System.currentTimeMillis();
        Wrapper<SecondBuyActivity> wrapper = new EntityWrapper<SecondBuyActivity>();
        wrapper.eq("store_id", storeId)
                .eq("del_state", SecondBuyActivity.NORMAL_STATE)
                .eq("activity_hand_end_time", 0)
                .gt("activity_end_time", currentTime);
        return secondBuyActivityMapper.selectList(wrapper);
    }


    /**
     * 开启或关闭热线按钮
     */
    @Override
    public void updateHotlineStatus(int hasHotonline, long storeId) {
        //开启或者关闭客服热线
        StoreBusiness storeBusiness = storeMapper.selectById(storeId);
        if (hasHotonline == storeBusiness.getHasHotonline()) {
            logger.info("请勿重复操作！storeId：" + storeId);
            throw new TipsMessageException("请勿重复操作！");
        }
        StoreBusiness storeBusiness2 = new StoreBusiness();
        storeBusiness2.setId(storeId);
        storeBusiness2.setHasHotonline(hasHotonline);
        storeMapper.updateById(storeBusiness2);
    }

    /**
     * 保存热线电话
     */
    @Override
    public void saveHotonline(String hotonline, long storeId) {
        //保存客服热线
        StoreBusiness storeBusiness = storeMapper.selectById(storeId);
        if (hotonline.equals(storeBusiness.getHotOnline())) {
            logger.info("请勿重复操作！storeId：" + storeId);
            throw new TipsMessageException("请勿重复操作！");
        }
        StoreBusiness storeBusiness2 = new StoreBusiness();
        storeBusiness2.setId(storeId);
        storeBusiness2.setHotOnline(hotonline);
        storeMapper.updateById(storeBusiness2);
    }


    /**
     * 团购活动件 减 参与团购人或者购买件数
     * <p>
     * 用户取消订单时
     * </p>
     *
     * @param id            团购活动id
     * @param conditionType 活动成团条件类型
     * @param count         新增的数量(人数或商品件数)
     * @return int 操作记录数 success: 1
     * @author Charlie
     * @date 2018/7/31 18:22
     */
    @Override
    public int teamActDecreaseUserOrProduct(Long id, Integer conditionType, Integer count) {
        return teamBuyActivityMapper.increaseBuyUserOrPdcCount(id, conditionType, 0 - count, -1);
    }

    /**
     * 团购活动件 加 参与团购人或者购买件数
     * <p>
     * 用户下订单
     * </p>
     *
     * @param id            团购活动id
     * @param conditionType 活动成团条件类型
     * @param count         新增的数量(人数或商品件数)
     * @return int 操作记录数 success: 1
     * @author Charlie
     * @date 2018/7/31 18:22
     */
    @Override
    public int teamActIncreaseUserOrProduct(Long id, Integer conditionType, Integer count) {
        return teamBuyActivityMapper.increaseBuyUserOrPdcCount(id, conditionType, count, 1);
    }


    /**
     * 秒杀活动, 增加参与人数
     *
     * @param secondId secondId
     * @param buyCount 增加的商品件数
     * @return int
     * @author Charlie
     * @date 2018/8/1 17:16
     */
    @Override
    public int secondActIncreaseJoinUser(Long secondId, Integer buyCount) {
        return secondBuyActivityMapper.updateJoinUser(secondId, 1, buyCount);
    }


    /**
     * 秒杀活动, 减少参与人数
     *
     * @param secondId secondId
     * @param buyCount 减少的商品数量
     * @return int
     * @author Charlie
     * @date 2018/8/1 17:16
     */
    @Override
    public int secondActDecreaseJoinUser(Long secondId, Integer buyCount) {
        return secondBuyActivityMapper.updateJoinUser(secondId, -1, 0 - buyCount);
    }



	/**
	 * 获取秒杀活动对应的缓存key
	 *
	 * @param secondId secondId
	 * @return java.lang.String
	 * @author Charlie
	 * @date 2018/8/2 17:56
	 */
	private String secondCacheKey(Long secondId) {
		return BizCacheKey.SHOP_SECOND_ACTIVITY_INVENTORY + secondId;
	}


    /**
     * 获取团购活动对应的缓存key
     *
     * @param teamId teamId
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/2 17:56
     */
    private static String teamCacheKey(Long teamId) {
        return BizCacheKey.SHOP_TEAM_ACTIVITY_INVENTORY + teamId;
    }


}