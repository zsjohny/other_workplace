package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.SecondBuyActivity;
import com.jiuyuan.entity.newentity.TeamBuyActivity;

import java.util.List;
import java.util.Map;

public interface IMyStoreActivityService {

    /**
     * 获取商家商家活动状态
     *
     * @param shopProductId
     * @param storeId
     * @return 商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
     */
    int getShopProductActivityState2(long shopProductId, long storeId);

    /**
     * 获取门店用户的商品列表
     *
     * @param keyWords
     * @param storeId
     * @param page
     * @return
     */
    List<Map<String, Object>> getShopProductList(String keyWords, long storeId, Page<Map<String, Object>> page);

    void addTeamActivity(String activityTitle, int userCount, long activityStartTime, long activityEndTime,
                         double activityPrice, int activityProductCount, long shopProductId, Integer conditionType, Integer meetProductCount, long storeId);

    long existTeamBuyActivityActivity(long shopProductId, long storeId);

    long existCurrentSecondBuyActivity(long shopProductId, long storeId);

    /**
     * 获取当前团购活动
     *
     * @param shopProductId
     * @return
     */
    public TeamBuyActivity getCurrentTeamBuyActivity(long shopProductId, long storeId);

    /**
     * 获取当前秒杀活动
     *
     * @param shopProductId
     * @return
     */
    public SecondBuyActivity getCurrentSecondBuyActivity(long shopProductId, long storeId);


    void editTeamActivity(String activityTitle, int userCount, long activityStartTime, long activityEndTime,
                          double activityPrice, int activityProductCount, long shopProductId, long teamActivityId, long storeId, Integer conditionType, Integer meetProductCount);

    void checkProductChoice(long editProductId, long currentProductId);

    void addSecondActivity(String activityTitle, long activityStartTime, long activityEndTime,
                           double activityPrice, int activityProductCount, long shopProductId, long storeId);

    void editSecondActivity(String activityTitle, long activityStartTime, long activityEndTime, double activityPrice,
                            int activityProductCount, long shopProductId, long secondActivityId, long storeId);

    void deleteAvtivity(long activityId, int type, long storeId);

    void handCloseActivity(long activityId, int type, long storeId);

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
    boolean rushBuyTeamBuyProduct(Long activityId, Integer conditionType, Integer buyCount, Long storeId);

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
    boolean rushSecondBuyProduct(Long activityId, Integer buyCount, Long storeId);

    /**
     * 获取商家商家活动状态
     *
     * @param shopProductId
     * @param storeId
     * @return 商品当前参与活动：0未参与活动、1表示参与团购活动、2表示参与秒杀活动
     */
    int getShopProductActivityState(long shopProductId, long storeId);


    List<SecondBuyActivity> searchRunningSecondBuyActivity(Long storeId);

    void updateHotlineStatus(int hasHotonline, long storeId);

    void saveHotonline(String hotonline, long storeId);


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
    int teamActDecreaseUserOrProduct(Long id, Integer conditionType, Integer count);


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
    int teamActIncreaseUserOrProduct(Long id, Integer conditionType, Integer count);


    /**
     * 秒杀活动,添加参与人数
     *
     * @param activityId activityId
     * @param count      count
     * @return int
     * @author Charlie
     * @date 2018/8/1 17:16
     */
    int secondActIncreaseJoinUser(Long activityId, Integer count);

    /**
     * 秒杀活动,减少参与人数
     *
     * @param activityId activityId
     * @param count      count
     * @return int
     * @author Charlie
     * @date 2018/8/1 17:16
     */
    int secondActDecreaseJoinUser(Long activityId, Integer count);

    /**
     * 将正常状态的购物车状态设为失效
     *
     * @param shopProductId 门店用户商品id
     * @param storeId       门店用户id
     * @author Charlie(唐静)
     * @date 2018/7/11 17:29
     */
    void adviceGoodsCarThisProductHasDisabled(Long shopProductId, Long storeId);
}