package com.jiuyuan.service.common;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.ProxyProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.util.DoubleUtil;
import com.yujj.entity.member.MemberOperatorRequest;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreBusinessNewService implements IStoreBusinessNewService {
    private static final Logger logger = LoggerFactory.getLogger(StoreBusinessNewService.class);

    private static final int NORMAL_STATUS = 0;
    @Autowired
    private SupplierOrderMapper supplierOrderMapper;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private ProxyProductService proxyProductService;



	@Override
	public StoreBusiness getById(Long storeId) {
//        storeMapper.selectStoreBusiness(storeId);
		return storeMapper.selectById(storeId);
	}

	@Override
	public StoreBusiness getStoreBusinessById(Long storeId) {
		return storeMapper.selectById(storeId);
	}

    public StoreBusiness getStoreBusinessByWxaAppId(String WxaAppId) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("WxaAppId", WxaAppId);
        List<StoreBusiness> storeBusinessList = storeMapper.selectList(wrapper);
        if (storeBusinessList.size() > 0) {
            return storeBusinessList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public StoreBusiness getStoreBusinessByWeiXinNum(String weiXinNum) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("WeiXinNum", weiXinNum);
        List<StoreBusiness> storeBusinessList = storeMapper.selectList(wrapper);
        if (storeBusinessList.size() > 0) {
            return storeBusinessList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据手机号获取商家信息
     */
    @Override
    public StoreBusiness getStoreBusinessByPhone(String phone) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("PhoneNumber", phone);
        List<StoreBusiness> storeBusinessList = storeMapper.selectList(wrapper);
        if (storeBusinessList.size() > 0) {
            return storeBusinessList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据商家号获取门店信息
     */
    @Override
    public StoreBusiness getStoreBusinessByBusinessNumber(Long businessNumber) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("BusinessNumber", businessNumber);
        List<StoreBusiness> storeBusinessList = storeMapper.selectList(wrapper);
        if (storeBusinessList.size() > 0) {
            return storeBusinessList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int updateFirstLoginStatus(long storeId, int noFirstLogin) {
        StoreBusiness storeBusiness = storeMapper.selectById(storeId);
        storeBusiness.setFirstLoginStatus(noFirstLogin);
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("id", storeId);
        return storeMapper.update(storeBusiness, wrapper);
    }

    @Override
    public List<StoreBusiness> getStoreBusinessByPhones(List<String> phones) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        for (int i = 0; ; i++) {
            wrapper.eq("PhoneNumber", phones.get(i));
            if (i >= phones.size() - 1) {
                break;
            }
            wrapper.or();
        }
        wrapper.andNew("Status = " + NORMAL_STATUS);
        List<StoreBusiness> storeBusinessNewList = storeMapper.selectList(wrapper);

        return storeBusinessNewList;
    }

    @Override
    public StoreBusiness findStoreDisplayImagesAndWxaAppIdById(Long storeId) {
        return storeMapper.findStoreDisplayImagesAndWxaAppIdById(storeId);
    }

    @Override
    public int findWxaTypeById(Long storeId) {
        return storeMapper.findWxaTypeById(storeId);
    }

    @Override
    public void updateSupplierIdById(Long id, Long id2) {
        // 更新供应商信息
        StoreBusiness storeBusiness = new StoreBusiness();
        storeBusiness.setId(id);
        storeBusiness.setSupplierId(id2);
        int i = storeMapper.updateById(storeBusiness);
        if (i == -1) {
            throw new RuntimeException("门店绑定供应商失败！");
        }
    }

    @Override
    public Map<String, Object> getStoreBusinessDetail(long storeId, long supplierId) {

        StoreBusiness storeBusiness = storeMapper.selectById(storeId);
        String province = storeBusiness.getProvince();
        String city = storeBusiness.getCity();
        String businessName = storeBusiness.getBusinessName();
        String businessAddress = storeBusiness.getBusinessAddress();
        Integer status = storeBusiness.getStatus();
        String legalPerson = storeBusiness.getLegalPerson();
        String phoneNumber = storeBusiness.getPhoneNumber();
        Wrapper<StoreOrderNew> wrapper = new EntityWrapper<StoreOrderNew>().eq("storeId", storeId)
                .eq("supplierId", supplierId).eq("status", 0).in("OrderStatus", "70,100");//所有交易成功和交易关闭的订单
        List<StoreOrderNew> storeOrderNewList = supplierOrderMapper.selectList(wrapper);
        double totalMoney = 0.00;
        double totalExpressMoney = 0.00;
        double totalBuyCount = 0.00;
        for (StoreOrderNew storeOrderNew : storeOrderNewList) {
            totalMoney += storeOrderNew.getTotalMoney();
            totalExpressMoney += storeOrderNew.getTotalExpressMoney();
            totalBuyCount += storeOrderNew.getTotalBuyCount();
        }
        Wrapper<StoreOrderNew> wrapper1 = new EntityWrapper<StoreOrderNew>().eq("storeId", storeId)
                .eq("supplierId", supplierId).eq("status", 0).eq("OrderStatus", 70).ge("total_refund_cost", 0.01);//交易完成并且包含退款
        List<StoreOrderNew> storeOrderNewList1 = supplierOrderMapper.selectList(wrapper1);
        double totalBackCount = 0.00;//退货数量
        double totalRefundCost = 0.00;//退款金额
        for (StoreOrderNew storeOrderNew : storeOrderNewList1) {
            totalBackCount += storeOrderNew.getTotalBuyCount();
            totalRefundCost += storeOrderNew.getTotalRefundCost();
        }

        Wrapper<StoreOrderNew> wrapper2 = new EntityWrapper<StoreOrderNew>().eq("storeId", storeId)
                .eq("supplierId", supplierId).eq("status", 0).eq("OrderStatus", 100).ge("total_refund_cost", 0.01);//交易关闭 全额退款的订单
        List<StoreOrderNew> storeOrderNewList2 = supplierOrderMapper.selectList(wrapper2);
        double totalBackCount1 = 0.00;//退货数量
        double totalRefundCost1 = 0.00;//退款金额
        for (StoreOrderNew storeOrderNew : storeOrderNewList2) {
            totalBackCount += storeOrderNew.getTotalBuyCount();
            totalRefundCost += storeOrderNew.getTotalRefundCost();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("businessName", businessName);
        map.put("totalPay", DoubleUtil.round(totalMoney + totalExpressMoney, 2));
        map.put("totalBuyCount", totalBuyCount);
        map.put("legalPerson", legalPerson);
        map.put("phoneNumber", phoneNumber);
        map.put("status", status);
        map.put("province", province);
        map.put("city", city);
        map.put("businessAddress", businessAddress);
        map.put("totalBackCount", totalBackCount + totalBackCount1);
        map.put("totalBackCost", DoubleUtil.round(totalRefundCost + totalRefundCost1, 2));

        return map;
    }

    @Override
    public List<StoreBusiness> getStoreBusinessBySupplierId(long id) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<StoreBusiness>();
        wrapper.eq("supplierId", id);
        List<StoreBusiness> list = storeMapper.selectList(wrapper);
        return list;
    }

    public void closeOrOpenArticle(StoreBusiness newStoreBusiness) {
        storeMapper.updateById(newStoreBusiness);
    }

    /**
     * 设置小程序问候消息
     */
    @Override
    public int addShopGreetingMessage(long storeId, String greetingImage, String greetingWords, int greetingSendType) {
        StoreBusiness storeBusiness = new StoreBusiness();
        storeBusiness.setId(storeId);
        storeBusiness.setGreetingSendType(greetingSendType);
        storeBusiness.setGreetingImage(greetingImage);
        storeBusiness.setGreetingWords(greetingWords);

        return storeMapper.updateById(storeBusiness);
    }

    /**
     * 删除小程序问候语图片
     */
    @Override
    public int delShopGreetingImage(long storeId) {
        StoreBusiness storeBusiness = new StoreBusiness();
        storeBusiness.setId(storeId);
        storeBusiness.setGreetingImage("");
        return storeMapper.updateById(storeBusiness);
    }


    @Override
    public int switchShopReservations(long storeId, int status) {
        StoreBusiness storeBusiness = new StoreBusiness();
        storeBusiness.setId(storeId);
        storeBusiness.setShopReservationsOrderSwitch(status);
        return storeMapper.updateById(storeBusiness);
    }

    @Override
    public List<Map<String, Object>> exportUserData(long beginTime, long endTime) {
        List<Map<String, Object>> list = storeMapper.exportUserData(beginTime, endTime);
        return list;
    }

    @Override
    public void openWxa(String phone, long proxyProductId, int proxyProductCount) {
//		String phone = order.getApplyPhone();
//		long proxyUserId = order.getProxyUserId();
//		long proxyProductId = order.getProxyProductId();
        StoreBusiness storeBusiness = getStoreBusinessByPhone(phone);
        if (storeBusiness == null) {
            logger.info("未找到门店,phone:" + phone);
            throw new RuntimeException("未找到手机号为" + phone + "的门店用户");
        }
        String wxaAppId = storeBusiness.getWxaAppId();//小程序appId
        if (StringUtils.isEmpty(wxaAppId)) {
            logger.info("门店未绑定小程序,wxaAppId:" + wxaAppId);
            throw new RuntimeException("门店未绑定小程序");
        }
        int isOpenWxa = storeBusiness.getIsOpenWxa();//是否开通小程序：0未开通，1已开通
        if (isOpenWxa != 1) {
            logger.info("门店未绑定小程序,wxaAppId:" + wxaAppId);
            throw new RuntimeException("门店未绑定小程序");
        }
        ProxyProduct proxyProduct = proxyProductService.getProxyProduct(proxyProductId);
        if (proxyProduct == null) {
            logger.info("未找到代理产品,proxyProductId:" + proxyProductId);
            throw new RuntimeException("未找到代理产品");
        }

        long wxaOpenTime = proxyProduct.buildWxaOpenTime(storeBusiness.getWxaCloseTime(), storeBusiness.getWxaOpenTime());
        long wxaCloseTime = proxyProduct.buildProductCloseTime(proxyProductCount, storeBusiness.getWxaCloseTime(), storeBusiness.getWxaOpenTime());
        long wxaRenewProtectCloseTime = proxyProduct.buildProductRenewProtectCloseTime(proxyProductCount, storeBusiness.getWxaCloseTime(), storeBusiness.getWxaOpenTime());
        StoreBusiness updStoreBusiness = new StoreBusiness();
        updStoreBusiness.setId(storeBusiness.getId());
        updStoreBusiness.setWxaOpenTime(wxaOpenTime);
        updStoreBusiness.setWxaCloseTime(wxaCloseTime);
        updStoreBusiness.setWxaRenewProtectCloseTime(wxaRenewProtectCloseTime);
        storeMapper.updateById(updStoreBusiness);
    }

    @Override
    public void add(StoreBusiness user) {
        storeMapper.insert(user);
    }

    @Override
    public void update(StoreBusiness user) {
        storeMapper.updateAllColumnById(user);
    }

    @Override
    public List<StoreBusiness> getStoreBusinessByWeixinId(String platformIndependentId) {
        Wrapper<StoreBusiness> wrapper = new EntityWrapper<>();
        wrapper.eq("BindWeixinId", platformIndependentId);
        return storeMapper.selectList(wrapper);
    }


    /**
     * 更新用户 WxaArticleShow
     *
     * @param storeId
     * @param wxaArticleShow
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/1 15:46
     */
    @Override
    public void updateWxaArticleShowById(Long storeId, Integer wxaArticleShow) {
        storeMapper.updateWxaArticleShowById(storeId, wxaArticleShow);
    }

    @Override
    public void updateById(StoreBusiness storeBusiness) {
        storeMapper.updateById(storeBusiness);
    }


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
    @Override
    public int updateWxaCloseTime(Long storeId, Long newWxaCloseTime, Long historyWxaCloseTime) {
        return storeMapper.updateWxaCloseTime(storeId, newWxaCloseTime, historyWxaCloseTime, System.currentTimeMillis());
    }

    @Override
    public List<MemberOperatorResponse> selectMyPageLists(Page<MemberOperatorResponse> page, MembersFindRequest membersFindRequest) {
        return storeMapper.selectMyPageLists(page, membersFindRequest);
    }

    @Override
    public void updateInformation(MemberOperatorRequest memberOperatorRequest) {
//		StoreBusiness user2 = getStoreBusinessByPhone(memberOperatorRequest.getPhone());
        StoreBusiness storeBusiness = new StoreBusiness();
//		storeBusiness.setBusinessName(memberOperatorRequest.getName());
//		storeBusiness.setProvince(memberOperatorRequest.getProvince());
//		storeBusiness.setCity(memberOperatorRequest.getCity());
//		storeBusiness.setCounty(memberOperatorRequest.getDistrict());
        storeBusiness.setPhoneNumber(memberOperatorRequest.getPhone());
        storeBusiness.setId(memberOperatorRequest.getId());
        updateById(storeBusiness);
    }

    /**
     * 根据认证id查询
     *
     * @param authId
     * @return
     */
    @Override
    public StoreBusiness getStoreBusinessByAuthId(long authId) {
        StoreBusiness storeBusiness = new StoreBusiness();
        storeBusiness.setAuthId(authId);
        return storeMapper.selectOne(storeBusiness);
    }

	@Override
	public StoreBusiness findHomeStoreById(Long id) {
    	return storeMapper.findHomeStoreById(id);
	}

    @Override
    public StoreBusiness findStoreBusinessById(Long storeId) {
        return storeMapper.findStoreBusinessById(storeId);
    }


}