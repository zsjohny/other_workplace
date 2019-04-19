package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.yujj.entity.member.MemberOperatorRequest;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;

import java.util.List;
import java.util.Map;


public interface IStoreBusinessNewService {

    List<StoreBusiness> getStoreBusinessByPhones(List<String> phones);

    StoreBusiness findStoreDisplayImagesAndWxaAppIdById(Long storeId);

    int findWxaTypeById(Long storeId);

    int updateFirstLoginStatus(long storeId, int noFirstLogin);

    StoreBusiness getById(Long storeId);

    public StoreBusiness getStoreBusinessByPhone(String phone);

    public StoreBusiness getStoreBusinessByBusinessNumber(Long businessNumber);

    public StoreBusiness getStoreBusinessById(Long storeId);

    void updateSupplierIdById(Long id, Long id2);

    Map<String, Object> getStoreBusinessDetail(long storeId, long supplierId);

    List<StoreBusiness> getStoreBusinessBySupplierId(long id);

    int addShopGreetingMessage(long storeId, String greetingImage, String greetingWords, int greetingSendType);

    int delShopGreetingImage(long storeId);

    StoreBusiness getStoreBusinessByWeiXinNum(String toUserName);

    /**
     * 开启/关闭预约试穿功能
     *
     * @param storeId
     * @param status
     * @return
     */
    int switchShopReservations(long storeId, int status);

    List<Map<String, Object>> exportUserData(long beginTime, long endTime);

    public void openWxa(String phone, long proxyProductId, int proxyProductCount);

    /**
     * 新增
     *
     * @param user
     */
    void add(StoreBusiness user);

    /**
     * 更新
     *
     * @param user
     */
    void update(StoreBusiness user);

    /**
     * 根据微信id获取用户
     *
     * @param platformIndependentId
     * @return
     */
    List<StoreBusiness> getStoreBusinessByWeixinId(String platformIndependentId);

    /**
     * 更新用户 WxaArticleShow
     *
     * @param storeId
     * @param i
     */
    void updateWxaArticleShowById(Long storeId, Integer i);

    void updateById(StoreBusiness storeBusiness);

    /**
     * 更新微信小程序关闭时间
     *
     * @param storeId             门店id
     * @param newWxaCloseTime     新的时间
     * @param historyWxaCloseTime 老的时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 14:24
     */
    int updateWxaCloseTime(Long storeId, Long newWxaCloseTime, Long historyWxaCloseTime);

    List<MemberOperatorResponse> selectMyPageLists(Page<MemberOperatorResponse> page, MembersFindRequest membersFindRequest);

    void updateInformation(MemberOperatorRequest memberOperatorRequest);

    /**
     * 根据认证id查询
     *
     * @param authId
     * @return
     */
    StoreBusiness getStoreBusinessByAuthId(long authId);

	/**
	 * 查询首页相关
	 * @param id
	 * @return
	 */
	StoreBusiness findHomeStoreById(Long id);

    /**
     * 查询用户信息
     * @param storeId
     * @return
     */
    StoreBusiness findStoreBusinessById(Long storeId);
}