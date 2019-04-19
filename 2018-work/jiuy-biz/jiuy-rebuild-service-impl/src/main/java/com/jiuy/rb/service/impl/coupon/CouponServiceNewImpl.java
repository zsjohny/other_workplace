package com.jiuy.rb.service.impl.coupon;


import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.coupon.CouponRbNewMapper;
import com.jiuy.rb.mapper.coupon.CouponTemplateNewMapper;
import com.jiuy.rb.model.coupon.CouponRbNew;
import com.jiuy.rb.model.coupon.CouponRbNewQuery;
import com.jiuy.rb.model.coupon.CouponTemplateNew;
import com.jiuy.rb.model.coupon.CouponTemplateNewQuery;
import com.jiuy.rb.model.order.ShopMemberOrderRb;
import com.jiuy.rb.model.order.StoreOrderRb;
import com.jiuy.rb.model.product.ProductRb;
import com.jiuy.rb.model.product.ProductRbQuery;
import com.jiuy.rb.model.user.ShopMemberRb;
import com.jiuy.rb.model.user.ShopMemberRbQuery;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.order.IMemberOrderService;
import com.jiuy.rb.service.order.IOrderService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.user.IShopMemberService;
import com.jiuy.rb.util.*;
import com.jiuyuan.entity.BrandVO;
import com.jiuyuan.entity.order.ShopGoodsCar;
import com.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 新的优惠券业务
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 13:51
 * @Copyright 玖远网络
 */
@Service("couponServerNew")
public class CouponServiceNewImpl implements ICouponServerNew {


    @Autowired
    private CouponTemplateNewMapper couponTemplateNewMapper;

    @Autowired
    private CouponRbNewMapper couponRbNewMapper;

    @Resource(name = "orderServiceRb")
    private IOrderService orderService;

    @Resource(name = "productService")
    private IProductService productService;

    @Resource(name = "shopMemberService")
    private IShopMemberService shopMemberService;

    @Resource(name = "memberOrderService")
    private IMemberOrderService memberOrderService;

    /**
     * 添加优惠券模板
     *
     * @param couponTemplateNew couponTemplateNew
     * @author Aison
     * @date 2018/8/2 13:53
     */
    @Override
    public void addCouponTemplate(CouponTemplateNew couponTemplateNew) {
        couponTemplateNew.setCreateTime(new Date());
        int i = couponTemplateNewMapper.insertSelective(couponTemplateNew);
        if(i>0){
            if (CouponSendEnum.SEND_NOW.isThis(couponTemplateNew.getSendType())){
                CouponAcceptVo accept = new CouponAcceptVo(null,null,null,
                        null,null,CouponStateEnum.NOT_USE);

                accept.setSendEnum(CouponSendEnum.SEND_NOW);
                accept.setTempId(couponTemplateNew.getId());
                accept.setStatus(0);
                accept.setSysEnum(CouponSysEnum.get(couponTemplateNew.getSysType()));

                grant(accept);
            }
        }
    }

    /**
     * app 订单回调发放优惠券
     *
     * @param orderNo 订单号
     * @author Aison
     * @date 2018/8/3 18:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantOrder(String orderNo) {

        // 先修改订单的优惠券发放状态
//        int rs = orderService.updateSendCoupon(orderNo);
//        if (rs == 0) {
//            // 锁定失败后返回
//            return;
//        }
        StoreOrderRb order = orderService.getByOrderNo(Long.valueOf(orderNo));
        CouponAcceptVo accept = new CouponAcceptVo(null, order.getStoreId(), orderNo, CouponSysEnum.APP, CouponSendEnum.ORDER, CouponStateEnum.NOT_USE);
        grant(accept);
    }

    /**
     * wxa 订单回调发放优惠券
     *
     * @param orderNo 订单号
     * @author hyq
     * @date 2018/8/24 18:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantWxaOrder(String orderNo) {

        ShopMemberOrderRb orderByOrderNo = memberOrderService.getOrderByOrderNo(orderNo);
        CouponAcceptVo accept = new CouponAcceptVo(orderByOrderNo.getMemberId(), null, orderNo, CouponSysEnum.WXA, CouponSendEnum.ORDER, CouponStateEnum.NOT_USE);
        grant(accept);
    }


    /**
     * 品牌列表 判断是否有优惠券
     *
     * @param param param
     * @author Aison
     * @date 2018/8/6 9:29
     */
    @Override
    public void hasCoupon(Map<String, Object> param) {
        List<BrandVO> brandList = (List<BrandVO>) param.get("brandList");
        Set<Long> supplierIds = new HashSet<>();
        for (BrandVO brandVO : brandList) {
            supplierIds.add(brandVO.getSupplierId());
        }
        Map<Long, Map<String, Long>> mapMap = couponTemplateNewMapper.hasCoupon(supplierIds);
        for (BrandVO brandVO : brandList) {
            Map<String, Long> countMap = mapMap.get(brandVO.getSupplierId());
            Long count = countMap == null ? 0L : countMap.get("count");
            brandVO.setHasCoupon(count == null ? 0 : count > 0 ? 1 : 0);
        }
    }

    /**
     * 查询可以领取的优惠券 app
     *
     * @param brandId supplierId
     * @return java.util.List<com.jiuy.rb.model.coupon.CouponTemplateNew>
     * @author Aison
     * @date 2018/8/6 10:30
     */
    @Override
    public List<Map<String, Object>> appCouponList(Long brandId) {

        // 查询供应商
        Long supplierId = productService.getSupplierId(brandId);
        if (supplierId == null) {
            return new ArrayList<>();
        }
        CouponTemplateNewQuery query = new CouponTemplateNewQuery();
        query.setStatus(0);
        query.setSysType(CouponSysEnum.APP.getCode());
        query.setIsAlive(1);
        query.setCanReceive(1);
        query.setPublishUserId(supplierId);
        List<CouponTemplateNew> ctns = couponTemplateNewMapper.selectList(query);
        //转转成map
        List<Map<String, Object>> retMap = new ArrayList<>(10);
        for (CouponTemplateNew ctn : ctns) {
            Map<String, Object> map = new HashMap<>();
            map.put("Money", ctn.getPrice());
            BigDecimal limitMoney = ctn.getLimitMoney();
            String limitMoneyStr = limitMoney.toString();
            int dot = limitMoneyStr.indexOf(".");
            int lastZero = limitMoneyStr.lastIndexOf("0");
            if (dot != -1) {
                if (lastZero > dot) {
                    while (limitMoneyStr.endsWith("0")) {
                        limitMoneyStr = limitMoneyStr.substring(0, limitMoneyStr.length() - 1);
                    }
                    if (limitMoneyStr.endsWith(".")) {
                        limitMoneyStr = limitMoneyStr.substring(0, limitMoneyStr.length() - 1);
                    }
                }
            }
            Date validityEndTime = ctn.getDeadlineEnd();
            Date validityStartTime = ctn.getDeadlineBegin();

            if (Biz.isEmpty(validityStartTime)) {
                validityStartTime = new Date();
            }
            if (Biz.isEmpty(validityEndTime)) {
                validityEndTime = Biz.addDate(validityStartTime, ctn.getDeadlineCount() * 24);
            }
            StringBuffer preferentialCondition = new StringBuffer();

            if (limitMoney.doubleValue() <= 0) {
                preferentialCondition.append("无门槛");
            } else {
                preferentialCondition.append("满")
                        .append(limitMoneyStr)
                        .append("元使用");
            }
            StringBuffer validTime = new StringBuffer("有效期:");
            validTime.append(Biz.formatDate(validityStartTime, "yyyy-MM-dd"))
                    .append("～")
                    .append(Biz.formatDate(validityEndTime, "yyyy-MM-dd"));
            map.put("preferentialCondition", preferentialCondition);
            map.put("validTime", validTime);
            map.put("id", ctn.getId());
            map.put("LimitMoney", limitMoneyStr);
            map.put("name", ctn.getName());
            map.put("publisher", ctn.getPublishUser());
            retMap.add(map);
        }
        return retMap;
    }


    /**
     * 获取优惠券的地方
     *
     * @param accept 参数
     * @author Aison
     * @date 2018/8/2 15:01
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grant(CouponAcceptVo accept) {

        CouponSendEnum sendEnum = accept.getSendEnum();
        switch (sendEnum) {
            // 立即发放
            case SEND_NOW:
                sendNow(accept);
                break;
            // 购买优惠券
            case ORDER:
                buyReceive(accept);
                break;
            // 注册优惠券
            case REGISTER:
                registerReceive(accept);
                break;
            // 自主领取
            case RECEIVE_SELF:
                receiveSelf(accept);
                break;
            default:
                break;
        }
    }


    /**
     * 回滚优惠券,将其设为可用
     *
     * @param couponId 优惠券id
     * @param orderId  订单id
     * @param orderNo  订单编号
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/3 17:07
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rollbackCoupon2Available(Long couponId, Long orderId, String orderNo) {
        if (Biz.isEmpty(couponId, orderId)) {
            throw new BizException(GlobalsEnums.PARAM_ERROR);
        }
        CouponRbNew couponNew = new CouponRbNew();
        couponNew.setId(couponId);
        couponNew.setStatus(0);
        couponNew.setUpdateTime(new Date());
        couponNew.setOrderNo("-1");
        int rs = couponRbNewMapper.updateByPrimaryKeySelective(couponNew);

        ShopMemberOrderRb query = new ShopMemberOrderRb();
        query.setId(orderId);
        query.setCouponLimitMoney(BigDecimal.ZERO);
        query.setCouponId(0L);
        query.setCouponName("");
        query.setOrderNumber(orderNo);
        memberOrderService.updateOrder(query, null);
        return rs == 1;
    }

    @Override
    public Boolean rollbackCoupon(String orderNo) {

        System.out.println("推优惠券0："+orderNo);
        if (Biz.isEmpty(orderNo)) {
            throw new BizException(GlobalsEnums.PARAM_ERROR);
        }

        CouponRbNewQuery couponNew = new CouponRbNewQuery();
        couponNew.setOrderNo(orderNo);
        couponNew.setStatus(1);
        CouponRbNew couponRbNew = couponRbNewMapper.selectOne(couponNew);
        //System.out.println("推优惠券1："+couponRbNew.toString());

        if(couponRbNew==null){
            System.out.println("暂时无可用优惠券!");
            return false;
        }

        CouponRbNew couponNewTRB = new CouponRbNew();
        couponNewTRB.setId(couponRbNew.getId());
        couponNewTRB.setStatus(0);
        couponNewTRB.setUpdateTime(new Date());
        couponNewTRB.setOrderNo("-1");
        System.out.println("推优惠券2："+couponNewTRB.toString());
        int rs = couponRbNewMapper.updateByPrimaryKeySelective(couponNewTRB);
        //System.out.println("推优惠券3："+rs);
        return rs == 1;
    }

    /**
     * 修改优惠券
     *
     * @param couponRbNew couponRbNew
     * @return int
     * @author Aison
     * @date 2018/8/7 17:41
     */
    @Override
    public int updateCoupon(CouponRbNew couponRbNew) {
        return couponRbNewMapper.updateByPrimaryKeySelective(couponRbNew);
    }

    /**
     * 核销优惠券
     *
     * @param id      id
     * @param storeId storeId
     * @return java.util.Map<com.sun.org.apache.xpath.internal.operations.String   ,   com.sun.org.apache.xpath.internal.operations.String>
     * @author Aison
     * @date 2018/8/7 17:46
     */
    @Override
    public Map<String, String> hx(Long id, Long storeId) {

        Map<String, String> result = new HashMap<>();
        CouponRbNew coupon = couponRbNewMapper.selectByPrimaryKey(id);

        CouponTemplateNew couponTemplateNew = couponTemplateNewMapper.selectByPrimaryKey(coupon.getTemplateId());

        if (coupon == null) {
            result.put("result", "核销失败");
            result.put("text", "二维码已失效");
            return result;
        }
        if (!CouponStateEnum.NOT_USE.isThis(coupon.getStatus())) {
            result.put("result", "核销失败");
            result.put("text", "此优惠券已被使用");
            return result;
        }
        Long now = System.currentTimeMillis();
        // 如果不是在这个时间段内的
        if (!(now >= coupon.getUseBeginTime().getTime() && now <= coupon.getUseEndTime().getTime())) {
            result.put("result", "核销失败");
            result.put("text", "优惠券尚未到试用期或者已经过期了");
            return result;
        }


        //如果不是本店的s
        if (!storeId.equals(coupon.getStoreId()) || !storeId.equals(couponTemplateNew.getPublishUserId())) {
            result.put("result", "核销失败");
            result.put("text", "该优惠券不是本店的");
            return result;
        }
        String name = "";
        BigDecimal limitMoney = coupon.getLimitMoney();
        if (limitMoney == null || limitMoney.compareTo(BigDecimal.ZERO) == 0) {
            name += "无门槛";
        } else {
            name += "满" + limitMoney.toString() + "可用";
        }


        CouponRbNew couponRbNew = new CouponRbNew();
        couponRbNew.setId(id);
        couponRbNew.setStatus(1);
        couponRbNew.setUpdateTime(new Date());
        int rs = updateCoupon(couponRbNew);
        if (rs == 0) {
            result.put("result", "核销失败");
            result.put("text", "二维码已失效");
            return result;
        }

        result.put("result", "核销成功");
        result.put("text", "");
        result.put("name", name);
        return result;
    }


    /**
     * 优惠券模板列表 由于分页的问题只能给app用
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Aison
     * @date 2018/8/8 9:04
     */
    @Override
    public Map<String, Object> tempPageApp(CouponTemplateNewQuery query) {


        MyPage<CouponTemplateNew> list = new MyPage<>(couponTemplateNewMapper.selectList(query));
        List<Long> ids = new ArrayList<>();
        for (CouponTemplateNew temp : list.getRows()) {
            ids.add(temp.getId());
        }
        Map<String, Object> param = new HashMap<>(20);
        param.put("tempIds", ids);
        param.put("userType", query.getSysType());
        Map<String, Map<String, Long>> countMaps = couponRbNewMapper.selectCouponGroup(param);

        List<Map<String, Object>> rowMap = new ArrayList<>();
        for (CouponTemplateNew temp : list.getRows()) {

            Map<String, Long> usedCountMap = countMaps.get(CouponStateEnum.USED.getCode() + "_" + temp.getId());
            Long usedCount = usedCountMap == null ? 0L : usedCountMap.get("count");
            Map<String, Long> avaCountMap = countMaps.get(CouponStateEnum.NOT_USE.getCode() + "_" + temp.getId());
            Long availableCount = avaCountMap == null ? 0L : avaCountMap.get("count");

            Map<String, Object> map = new HashMap<>();
            map.put("id", temp.getId());
            map.put("storeId", temp.getPublishUserId());
            String name = temp.getName();
            map.put("name", name);
            map.put("fillinName", Biz.isEmpty(name) ? 0 : 1);
            map.put("money", temp.getPrice());
            map.put("limitMoney", temp.getLimitMoney());
            map.put("getCount", temp.getReceiveCount());
            map.put("usedCount", usedCount == null ? 0 : usedCount);
            map.put("availableCount", availableCount == null ? 0 : availableCount);
            map.put("grantCount", temp.getIssueCount());
            map.put("publishCount", temp.getIssueCount());
            if (temp.getDeadlineType() == 0) {
                map.put("validityStartTimeStr", Biz.formatDate(temp.getDeadlineBegin(), "yyyy-MM-dd"));
                map.put("validityEndTimeStr", Biz.formatDate(temp.getDeadlineEnd(), "yyyy-MM-dd"));
            } else {
                map.put("deadlineCount", temp.getDeadlineCount());
            }
            map.put("status", temp.getStatus());
            map.put("createTime", Biz.formatDate(temp.getCreateTime(), "yyyy-MM-dd"));
            map.put("updateTime", temp.getUpdateTime());
            rowMap.add(map);
        }

        Integer pageNum = list.getPageNum();
        pageNum = pageNum / list.getPageSize() + 1;

        Map<String, Object> restMap = new HashMap<>();
        restMap.put("records", rowMap);
        restMap.put("total", list.getTotal());
        restMap.put("current", pageNum);
        restMap.put("pages", list.getPages());
        restMap.put("size", list.getPageSize());
        restMap.put("isMore", pageNum < list.getPages() ? 1 : 0);

        return restMap;
    }

    /**
     * 停止发放优惠券
     *
     * @param tempId tempId
     * @return int
     * @author Aison
     * @date 2018/8/8 14:26
     */
    @Override
    public int stopTempSend(Long tempId, Long publishId, Integer status) {

        CouponTemplateNew tempOld = couponTemplateNewMapper.selectByPrimaryKey(tempId);
        if (tempOld == null) {
            throw new BizException(GlobalsEnums.PARAM_ERROR);
        }
        if (!publishId.equals(tempOld.getPublishUserId())) {
            throw new RuntimeException("非本店优惠券模板");
        }
        CouponTemplateNew temp = new CouponTemplateNew();
        temp.setId(tempId);
        temp.setStatus(status);
        return couponTemplateNewMapper.updateByPrimaryKeySelective(temp);
    }


    /**
     * 等待领取的优惠券
     *
     * @param param param
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Aison
     * @date 2018/8/8 15:09
     */
    @Override
    public List<CouponTemplateNew> waitGetCoupon(Map<String, Object> param) {

        return couponTemplateNewMapper.selectNotReceivedTemplate(param);
    }

    /**
     * 获取可用优惠券
     *
     * @param query query
     * @return int
     * @author Aison
     * @date 2018/8/8 16:40
     */
    @Override
    public int useAbleCouponCount(CouponRbNewQuery query) {

        return couponRbNewMapper.selectCount(query);
    }

    /**
     * 核销列表
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Aison
     * @date 2018/8/7 17:05
     */
    @Override
    public Map<String, Object> appHxList(CouponRbNewQuery query) {

        MyPage<CouponRbNew> coupons = new MyPage<>(couponRbNewMapper.selectList(query));
        // ids
        List<Long> ids = new ArrayList<>();
        for (CouponRbNew couponRbNew : coupons.getRows()) {
            ids.add(couponRbNew.getMemberId());
        }

        List<Map<String, Object>> maps = new ArrayList<>();
        Map<Long, ShopMemberRb> memberMap = shopMemberService.getMemberMap(ids);
        for (CouponRbNew couponRbNew : coupons.getRows()) {
            Map<String, Object> map = new HashMap<>(3);
            map.put("checkTime", Biz.formatDate(couponRbNew.getUpdateTime(), "yyyy-MM-dd"));
            map.put("money", couponRbNew.getPrice());
            ShopMemberRb member = memberMap.get(couponRbNew.getMemberId());
            if (member != null) {
                map.put("memberNicheng", member.getUserNickname());
            }
            maps.add(map);
        }
        // 查询核销信息
        Map<String, Object> hxMap = couponRbNewMapper.selectHxInfo(query);
        Object personCount = hxMap.get("personCount");
        Object couponCount = hxMap.get("couponCount");
        Object money = hxMap.get("money");


        Map<String, Object> restMap = new HashMap<>();
        Map<String, Object> retMaps = new HashMap<>();
        retMaps.put("offset", coupons.getPageNum());
        Integer pageNum = coupons.getPageNum();
        pageNum = pageNum / coupons.getPageSize() + 1;

        retMaps.put("total", coupons.getTotal());
        retMaps.put("current", pageNum);
        retMaps.put("pages", coupons.getPages());
        retMaps.put("size", coupons.getPageSize());
        retMaps.put("isMore", pageNum < coupons.getPages() ? 1 : 0);
        retMaps.put("records", maps);
        retMaps.put("limit", coupons.getPageSize());


        restMap.put("shopMemberCouponList", retMaps);
        restMap.put("allMoney", money == null ? 0 : money);
        restMap.put("memberSize", personCount == null ? 0 : personCount);
        restMap.put("count", couponCount == null ? 0 : couponCount);
        return restMap;
    }

    /**
     * 查询我的优惠券
     *
     * @param query 查询对象
     * @return java.util.List<java.util.Map               <               java.lang.String               ,               java.lang.Object>>
     * @author Aison
     * @date 2018/8/6 15:15
     */
    @Override
    public Map<String, Object> myCouponList(CouponRbNewQuery query) {

        List<Map<String, Object>> retMap = new ArrayList<>();
        MyPage<CouponRbNew> coupons = new MyPage<>(couponRbNewMapper.selectList(query));

        for (CouponRbNew coupon : coupons.getRows()) {
            Map<String, Object> map = new HashMap<>();
            String publisher = coupon.getPublishUser();
            if (Biz.isNotEmpty(publisher)) {
                map.put("templateName", coupon.getTemplateName() + " - " + publisher);
            } else {
                map.put("templateName", coupon.getTemplateName());
            }
            map.put("couponTemplateId", coupon.getTemplateId());
            map.put("validityEndTimeStr", Biz.formatDate(coupon.getUseEndTime(), "yyyy-MM-dd"));
            map.put("validityStartTimeStr", Biz.formatDate(coupon.getUseBeginTime(), "yyyy-MM-dd"));
            map.put("publisher", publisher);
            map.put("supplierId", coupon.getPublishUserId());
            CouponStateEnum state = CouponStateEnum.get(coupon.getStatus());
            map.put("useStatus", state.getName());
            map.put("id", coupon.getId());
            map.put("limitMoney", coupon.getLimitMoney());
            CouponTemplateNew tempCoupon = couponTemplateNewMapper.selectByPrimaryKey(coupon.getTemplateId());
            map.put("userRange", tempCoupon.getUseRange());

            Integer couponType = tempCoupon.getCouponType();
            if (CouponTpyeEnum.COUPON.isThis(couponType) || CouponTpyeEnum.RED.isThis(couponType)) {
                map.put("money", coupon.getPrice());
            } else if (CouponTpyeEnum.DISCOUNT.isThis(couponType)) {
                map.put("money", coupon.getDiscount());
            } else {
                map.put("money", coupon.getPrice());
            }
            map.put("couponType", couponType.intValue());

            String allStr = "满money元、";
            if (coupon.getLimitMoney().compareTo(BigDecimal.ZERO)==0) {
                allStr = "";
            } else {
                allStr = allStr.replace("money", coupon.getLimitMoney().toString());
            }

            Integer platformType = tempCoupon.getPlatformType();

            String proStr = getProStr(tempCoupon,platformType);


            map.put("userRangeStr", allStr + proStr);
            retMap.add(map);
        }

        MyPage<Map<String, Object>> retPage = new MyPage<>();
        retPage.setTotal(coupons.getTotal());
        retPage.setCPage(coupons.getCPage());
        retPage.setPageSize(coupons.getPageSize());
        retPage.setPageNum(coupons.getPageNum());
        retPage.setRows(retMap);


        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();


        Integer pageNum = coupons.getPageNum();
        pageNum = pageNum / coupons.getPageSize() + 1;
        queryMap.put("page", pageNum);
        queryMap.put("pageSize", coupons.getPageSize());
        queryMap.put("recordCount", coupons.getTotal());
        queryMap.put("maxRecordCount", 20000);
        queryMap.put("pageCount", coupons.getPages());
        queryMap.put("more", pageNum < coupons.getPages());
        resMap.put("pageQuery", queryMap);

        resMap.put("couponList", retMap);

        return resMap;

    }

    /**
     *描述 新运营平台模板查询
     ** @param query 优惠券模板参数
     * @author hyq
     * @date 2018/8/17 15:37
     * @return com.jiuy.base.model.MyPage<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @Override
    public MyPage<Map<String, Object>> selectCouponTemplateList(CouponTemplateNewQuery query) {
        return new MyPage<> (couponTemplateNewMapper.selectCouponTemplateList(query));
    }

    /**
     *描述 优惠券首页
     ** @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    @Override
    public MyPage<CouponSendList> selectSendCouponInfo(CouponRbNewQuery query) {
        //List<CouponSendList> maps = couponTemplateNewMapper.selectSendCouponInfo(query);

        MyPage<CouponSendList> maps = new MyPage<>(couponTemplateNewMapper.selectSendCouponInfo(query));

        for (CouponSendList couponSend : maps.getRows()){

            if(couponSend == null){
                continue;
            }

            String couponUserType = couponSend.getCouponUserType();
            //app优惠券
            if("1".equals(couponUserType)){
                String name =couponSend.getStoreName()==null ? "":couponSend.getStoreName();
                couponSend.setSendObject(name+"(ID:"+couponSend.getStoreId()+")");
                if("1".equals(couponSend.getCouponStatus())){
                    couponSend.setSendOrderMoney(couponSend.getStoreMoney());
                    couponSend.setSendOrderTime(couponSend.getStoreOrderTime());
                }

            }else {
                String name = couponSend.getWxaName()==null ? "":couponSend.getWxaName();
                couponSend.setSendObject(name+"(ID:"+couponSend.getWxaId()+")");
                if("1".equals(couponSend.getCouponStatus())) {
                    couponSend.setSendOrderMoney(couponSend.getWxaMoney());
                    couponSend.setSendOrderTime(couponSend.getWxaTime());
                }
            }

        }
        return maps;
    }

    /**
     *描述 获取优惠券发送记录
     ** @param id
     * @author hyq
     * @date 2018/8/22 17:56
     * @return com.jiuy.base.util.ResponseResult
     */
    @Override
    public Map<String, Object> selectSendCouponInfoCollect(Long id) {
        return couponTemplateNewMapper.selectSendCouponInfoCollect(id);
    }

    /**
     * 获取特定描述
     * @param tempCoupon
     * @param platformType
     * @return
     */
    private  String getProStr(CouponTemplateNew tempCoupon,Integer platformType){
        String proStr="";
        if(CouponPlatEnum.SUPPLIER.isThis(platformType)){
            proStr = tempCoupon.getPublishUser()+"商品可用";
        }else if(CouponPlatEnum.APP.isThis(platformType)){
            proStr = "全场商品可用";
        }else if(CouponPlatEnum.PLAT.isThis(platformType)){
            if (CouponRangeEnum.ALL.isThis(tempCoupon.getUseRange())) {
                proStr = "全场商品可用";
            } else if (CouponRangeEnum.PRODUCT.isThis(tempCoupon.getUseRange())) {
                proStr = "特定商品可用";
            }
        }else {
            proStr = "全场商品可用";
        }

        return proStr;
    }

    /**
     * 查询我的优惠券 微信小程序 . 别怪我没有办法通用
     *
     * @param query 查询对象
     * @return java.util.List<java.util.Map               <               java.lang.String               ,               java.lang.Object>>
     * @author Aison
     * @date 2018/8/6 15:15
     */
    @Override
    public Map<String, Object> myCouponListWxa(CouponRbNewQuery query) {

        List<Map<String, Object>> retMap = new ArrayList<>();
        MyPage<CouponRbNew> coupons = new MyPage<>(couponRbNewMapper.selectList(query));

        for (CouponRbNew coupon : coupons.getRows()) {

            Map<String, Object> map = new HashMap<>();
            BigDecimal limitMoney = coupon.getLimitMoney();

            CouponTemplateNew tempCoupon = couponTemplateNewMapper.selectByPrimaryKey(coupon.getTemplateId());
            map.put("userRange", tempCoupon.getUseRange());

            String allStr = "满money元、";
            if (coupon.getLimitMoney().compareTo(BigDecimal.ZERO)==0) {
                allStr = "";
            } else {
                allStr = allStr.replace("money", coupon.getLimitMoney().toString());
            }

            String proStr = "";
            if (CouponRangeEnum.ALL.isThis(tempCoupon.getUseRange())) {
                proStr = "全场商品可用";
            } else if (CouponRangeEnum.PRODUCT.isThis(tempCoupon.getUseRange())) {
                if (CouponSysEnum.APP.isThis(tempCoupon.getSysType())) {
                    proStr = "某某品牌商品可用";
                } else {
                    proStr = "特定商品可用";
                }
            }
            //}
            map.put("limitText", allStr + proStr);

//            String text;
//            if(limitMoney.compareTo(BigDecimal.ZERO)>0) {
//                text = "满"+limitMoney+"元可用";
//            } else {
//                text = "无金额限制";
//            }
            //map.put("limitText",text);
            map.put("money", coupon.getPrice());
            map.put("discount",coupon.getDiscount());
            map.put("couponType",tempCoupon.getCouponType());
            map.put("validityEndTime", Biz.formatDate(coupon.getUseEndTime(), "yyyy-MM-dd"));
            map.put("name", coupon.getTemplateName());
            map.put("validityStartTime", Biz.formatDate(coupon.getUseBeginTime(), "yyyy-MM-dd"));
            map.put("id", coupon.getId());
            CouponStateEnum stateName = CouponStateEnum.get(coupon.getStatus());
            map.put("statusStr", coupon.getStatus());
            int aliType = query.getAliveType().intValue();
            map.put("status", aliType == 0 ? "已过期" : aliType==1 ? "未使用" : "已使用");
            retMap.add(map);
        }


        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("limit", coupons.getPageSize());
        queryMap.put("offset", coupons.getPageNum());
        Integer pageNum = coupons.getPageNum();
        pageNum = pageNum / coupons.getPageSize() + 1;
        queryMap.put("current", pageNum);
        queryMap.put("size", coupons.getPageSize());
        queryMap.put("total", coupons.getTotal());
        queryMap.put("pages", coupons.getPages());
        queryMap.put("isMore", pageNum < coupons.getPages() ? 1 : 0);
        queryMap.put("records", retMap);

        resMap.put("memberCouponList", queryMap);
        resMap.put("noUseCount", coupons.getTotal());

        return resMap;

    }

    @Override
    public int listCountNumber(CouponRbNewQuery query) {
        return couponRbNewMapper.selectCount(query);
    }


    /**
     * 修改优惠券
     *
     * @param couponId couponId
     * @return int
     * @author Aison
     * @date 2018/8/6 16:27
     */
    @Override
    public int delCoupon(Long couponId) {
        return couponRbNewMapper.deleteCoupon(couponId);
    }


    /**
     * 新的优惠券填充
     *
     * @param data data
     * @author Aison
     * @date 2018/8/6 17:15
     */
    @Override
    public void fillCoupon(Map<String, Object> data, Long storeId, Long memberId, CouponSysEnum sysEnum,Boolean isManyPro,Integer pageCurrent,Integer pageSize) {


        List<Map<String, Object>> coupons = new ArrayList<>();;
        if (sysEnum == CouponSysEnum.APP) {
            List<Map<String, java.lang.Object>> orderList = (List<Map<String, java.lang.Object>>) data.get("orderList");
            for (Map<String, java.lang.Object> order : orderList) {
                String productId = (String) order.get("productId");
                String category = (String) order.get("category");
                String payPrice = order.get("payPrice").toString();
                Long restrictionActivityProductId = (Long) order.get("restrictionActivityProductId");
                if (restrictionActivityProductId < 1) {
                    coupons = canUseCoupon(new BigDecimal(payPrice), storeId, memberId, sysEnum, productId, category,pageCurrent,pageSize);
                } else {
                    coupons = new ArrayList<>();
                }
                order.put("selelctCouponList", coupons);
            }
        } else if (sysEnum == CouponSysEnum.WXA) {
            Integer wxaType = (Integer) data.get("wxaType");
            if (wxaType == null) {
                // 获取可用的优惠券数量
                Map<String, Object> productInfos = (Map<String, Object>) data.get("productInfos");
                if(isManyPro){
                    List<List<Map<String, Object>>> listCouponMap = new ArrayList<>();
                    List<Map<String, String>> productSKUList =(List<Map<String, String>>)productInfos.get("productSKUList");
                    for (Map<String, String> stringStringMap : productSKUList) {
                        Object allProductPrice =stringStringMap.get("price");
                        String productId = (String) stringStringMap.get("productId");
                        BigDecimal price = new BigDecimal(allProductPrice.toString());
                        //查询出每个商品的红包
                        listCouponMap.add(canUseCoupon(price, storeId, memberId, sysEnum, productId, "",pageCurrent,pageSize));
                    }
                    //取出并集
                    Map<Long, List<Map<String, Object>>> couponGroup = new HashMap<>();
                    for (List<Map<String, Object>> list : listCouponMap) {
                        for (Map<String, Object> couponMap : list) {
                            Long couponId = (Long)couponMap.get("couponId");
                            if(couponGroup.get(couponId)==null){
                                List<Map<String, Object>> couponList = new ArrayList<>();
                                couponList.add(couponMap);
                                couponGroup.put(couponId,couponList);
                            }else {
                                couponGroup.get(couponId).add(couponMap);
                            }
                        }
                    }
                    int allNum=0;
                    int size = productSKUList.size();
                    for (Map.Entry<Long, List<Map<String, Object>>> entry:couponGroup.entrySet()){
                        //取出红包的交集
                        allNum+=1;
                    }
                    data.put("shopMemberCouponCount", allNum);
                    return ;
                }
                Object allProductPrice = productInfos.get("allProductPrice");
                String productId = (String) productInfos.get("productId");
                BigDecimal price = new BigDecimal(allProductPrice.toString());
                coupons = canUseCoupon(price, storeId, memberId, sysEnum, productId, "",pageCurrent,pageSize);
                data.put("shopMemberCouponCount", coupons.size());
            } else if (wxaType==1) {
                // 获取可用优惠券列表

                if(isManyPro){
                    List<List<Map<String, Object>>> listCouponMap = new ArrayList<>();

                    String productId = (String) data.get("productId");
                    String[] splitStr = productId.split(",");
                    for (int i = 0; i < splitStr.length; i++) {
                        String price = (String) data.get("price");
                        listCouponMap.add(canUseCoupon(new BigDecimal(price), storeId, memberId, sysEnum, splitStr[i], "",pageCurrent,pageSize));
                    }

                    //取出并集
                    Map<Long, List<Map<String, Object>>> couponGroup = new HashMap<>();
                    listCouponMap.stream().forEach(action -> {
                        for(Map<String, Object> couponMap :action ){
                            Long couponId = (Long)couponMap.get("couponId");
                            if(couponGroup.get(couponId)==null){
                                List<Map<String, Object>> couponList = new ArrayList<>();
                                couponList.add(couponMap);
                                couponGroup.put(couponId,couponList);
                            }else {
                                couponGroup.get(couponId).add(couponMap);
                            }
                        }
                    });

                    for (Map.Entry<Long, List<Map<String, Object>>> entry:couponGroup.entrySet()){
                        //取出红包的交集
                        Map<String, Object> stringObjectMap = entry.getValue().get(0);
                        coupons.add(stringObjectMap);
                    }

                    data.put("coupons", coupons);
                    return;
                }

                String productId = (String) data.get("productId");
                String price = (String) data.get("price");
                coupons = canUseCoupon(new BigDecimal(price), storeId, memberId, sysEnum, productId, "",pageCurrent,pageSize);
                data.put("coupons", coupons);
            }
        }

    }

    /**
     * @param orderPrice 订单价格
     * @param storeId    appId
     * @param memberId   wxaId
     * @param sysEnum    系统枚举
     * @param productId  商品id
     * @param categoryId 商品类目id
     * @return java.util.List<java.util.Map   <   java.lang.String   ,   java.lang.Object>>
     * @author Aison
     * @date 2018/8/6 18:32
     */
    private List<Map<String, Object>> canUseCoupon(BigDecimal orderPrice, Long storeId, Long memberId, CouponSysEnum sysEnum, String productId, String categoryId,Integer pageCurrent,Integer pageSize) {
        // 查询出当前用户所有的的可用的优惠券满足订单价格的..
        long timeMillis = System.currentTimeMillis();
        String string = TimeUtils.longFormatString(timeMillis);
        List<CouponRbNewQuery> coupons = couponRbNewMapper.selectOrderCoupon(orderPrice, storeId, memberId, sysEnum.getCode(),(pageCurrent-1)*pageSize,pageSize,string);

        List<Map<String, Object>> couponMaps = new ArrayList<>();
        for (CouponRbNewQuery coupon : coupons) {
            String ids = coupon.getRangeIds();
            Set<String> setIds = new HashSet<>(Arrays.asList(ids.split(",")));
            Integer range = coupon.getUseRange();
            CouponTemplateNew couponTemplateNew = couponTemplateNewMapper.selectByPrimaryKey(coupon.getTemplateId());
            Integer platformType = couponTemplateNew.getPlatformType();
            coupon.setCouponType(couponTemplateNew.getCouponType());

            //如果是供应商发的。只能用在供应商下面的商品
            if(CouponPlatEnum.SUPPLIER.isThis(platformType)){
                Long publishUserId = couponTemplateNew.getPublishUserId();

                ProductRbQuery query1 = new ProductRbQuery ();
                query1.setId(Long.parseLong(productId));
                query1.setSupplierId(publishUserId);
                List<ProductRb> productRbList = productService.getList (query1);
                if(productRbList==null || productRbList.size()<1){
                    continue;
                }
            }

            // 全场通用的
            if (CouponRangeEnum.ALL.isThis(range)) {
                addCoupon(coupon, couponMaps);
            }
            // 商品类目的
            if (CouponRangeEnum.PRODUCT.isThis(range) && setIds.contains(productId)) {
                addCoupon(coupon, couponMaps);
            }
            // 类目列表
//            if (CouponRangeEnum.CATEGORY.isThis(range) && setIds.contains(categoryId)) {
//                addCoupon(coupon, couponMaps);
//            }


        }
        return couponMaps;
    }

    private void addCoupon(CouponRbNewQuery coupon, List<Map<String, Object>> couponMaps) {

        Map<String, Object> map = new HashMap<>();

        String name = coupon.getTemplateName();
        map.put("couponName", name);
        map.put("couponPrice", coupon.getPrice());
        map.put("couponId", coupon.getId());
        map.put("discount", coupon.getDiscount());
        map.put("couponType",coupon.getCouponType());


        BigDecimal limit = coupon.getLimitMoney();
        map.put("id", coupon.getId());
        map.put("limitText", limit == null || limit.compareTo(BigDecimal.ZERO) == 0 ? "无限制" : "满" + limit + "可用");
        map.put("name", name);
        map.put("money", coupon.getPrice());
        map.put("validityEndTime", Biz.formatDate(coupon.getUseEndTime(), "yyyy-MM-dd"));
        map.put("validityStartTime", Biz.formatDate(coupon.getUseBeginTime(), "yyyy-MM-dd"));
        couponMaps.add(map);
    }

    /**
     * @param tempId tempId
     * @param count  count
     * @author Aison
     * @date 2018/8/3 12:07
     */
    private void updateTemp(Long tempId, Long count) {

        // 修改模板中的领取量
        int rs = couponTemplateNewMapper.addReceiveCount(tempId, count);
        Declare.isFailed(rs == 0, GlobalsEnums.ADD_COUPON_FAILED);
    }


    /**
     * 检验领取次数
     *
     * @param template 模板
     * @param user     用户
     * @author Aison
     * @date 2018/8/6 14:38
     */
    private void checkTemp(CouponTemplateNew template, CouponUser user) {

        CouponRbNewQuery query = new CouponRbNewQuery();
        query.setTemplateId(template.getId());
        query.setMemberId(user.getMemberId());
        query.setStoreId(user.getStoreId());
        int count = couponRbNewMapper.selectCount(query);
        if (count >= template.getEachReceiveCount()) {
            throw BizException.def().msg("该优惠券每人只能领取" + template.getEachReceiveCount() + "张！您已经无法继续领取");
        }
    }

    /**
     * 自主领取 必须有模板id
     *
     * @param accept accept
     * @author Aison
     * @date 2018/8/6 11:20
     */
    private void receiveSelf(CouponAcceptVo accept) {
        // 查询模板
        CouponTemplateNewQuery query = new CouponTemplateNewQuery();
        query.setId(accept.getTempId());
        query.setIsAlive(1);
        query.setStatus(0);
        CouponTemplateNew template = couponTemplateNewMapper.selectOne(query);
        Declare.isFailed(template == null, GlobalsEnums.COUPON_TEMP_IS_NULL);
        Declare.isFailed(!CouponSendEnum.RECEIVE_SELF.isThis(template.getSendType()), GlobalsEnums.ONLY_SELF);


        CouponUser user = new CouponUser();
        user.setMemberId(accept.getMemberId());
        user.setStoreId(accept.getStoreId());
        user.setSysType(accept.getSysEnum().getCode());
        // 领取校验
        checkTemp(template, user);

        CouponRbNew couponNew = genCoupon(template, user, accept);
        Integer rs = couponRbNewMapper.insertSelective(couponNew);
        Declare.isFailed(rs == 0, GlobalsEnums.ADD_COUPON_FAILED);
        updateTemp(template.getId(), rs.longValue());
    }


    /**
     * 立即发放的逻辑
     *
     * @param accept 参数
     * @author Aison
     * @date 2018/8/2 15:14
     */
    private Long sendNow(CouponAcceptVo accept) {

        // 查询模板
        CouponTemplateNew template = couponTemplateNewMapper.selectByPrimaryKey(accept.getTempId());
        Declare.isFailed(template == null, GlobalsEnums.COUPON_TEMP_IS_NULL);
        Declare.isFailed(!CouponSendEnum.SEND_NOW.isThis(template.getSendType()), GlobalsEnums.ONLY_SEND_NOW);

        CouponSysEnum sysEnum = CouponSysEnum.get(template.getSysType());
        accept.setSysEnum(sysEnum);

        CouponVo vo = new CouponVo(template);
        //查询发送给哪些人
        List<CouponUser> users = null;
        Integer sysType = template.getSysType();
        // 如果是app
        if (CouponSysEnum.APP.isThis(sysType)) {
            users = couponTemplateNewMapper.selectTargetUser(vo.getWho());
        } else if (CouponSysEnum.WXA.isThis(sysType)) {
            // 如果是小程序
            users = couponTemplateNewMapper.selectTargetUserWxa(vo.getWho());
        }
        // 没有目标用户
        if (users == null || users.size() == 0) {
            return 0L;
        }
        List<CouponRbNew> couponList = new ArrayList<>();

        int eachReceiveCount = template.getEachReceiveCount().intValue();
        int receiveCount = template.getReceiveCount().intValue();
        int issueCount = template.getIssueCount().intValue();

        int receiveCountSum = 0;
        // 生成优惠券
        for (CouponUser action: users) {
            if(action==null){
                continue;
            }

            //每次用户可以领多少
            int finalEachReceiveCount = (issueCount-receiveCount)>eachReceiveCount ? eachReceiveCount : (issueCount-receiveCount);
            for (int i = 0; i < finalEachReceiveCount; i++) {
                CouponRbNew coupon = genCoupon(template, action, accept);
                couponList.add(coupon);
                receiveCountSum++;
            }
            receiveCount= receiveCountSum;
        }


        // 添加优惠券
        Integer size = couponRbNewMapper.insertBach(couponList);
        Declare.isFailed(size != couponList.size(), GlobalsEnums.ADD_COUPON_FAILED);

        updateTemp(template.getId(), size.longValue());
        return size.longValue();
    }

    /**
     * 购买获取优惠券
     * <p>
     * orderNo storeId/memberId 为必要参数
     *
     * @param accept 参数
     * @return java.lang.Long
     * @author Aison
     * @date 2018/8/2 17:33
     */
    private Long buyReceive(CouponAcceptVo accept) {
        System.out.println(accept.getOrderNo()+"开始发红包!");

        CouponRbNewQuery coupon = new CouponRbNewQuery();
        coupon.setOrderNo(accept.getOrderNo());
        coupon.setStatus(0);

        int noCount = couponRbNewMapper.selectCount(coupon);

        //已经有了的红包就不需要操作了
        if(noCount>0){
            System.out.println(accept.getOrderNo()+"存在红包了！");
            return 0L;
        }

        // 查询出所有的购买进账优惠券的模板.. 且没有被发完的
        CouponTemplateNewQuery query = new CouponTemplateNewQuery();
        query.setIsAlive(1);
        query.setSendType(CouponSendEnum.ORDER.getCode());
        query.setSysType(accept.getSysEnum().getCode());
        query.setStatus(0);
        List<CouponTemplateNew> templates = couponTemplateNewMapper.selectList(query);

        System.out.println("查询模板个数："+templates.toString());
        Integer size = 0;
        // 获取收益最大的一个优惠券模板
        for (CouponTemplateNew action : templates) {
            // 判断当前订单是否符合优惠券领取要求
            if(action==null){
                continue;
            }
            CouponVo vo = new CouponVo(action);
            CouponWho who = vo.getWho();

            List<CouponUser> users = null;
            Integer sysType = action.getSysType();
            List<String> ids = new ArrayList<>();
            if (CouponSysEnum.WXA.isThis(sysType)) {
                ids.add(accept.getMemberId().toString());
                who.setUserIdList(ids);
                users = couponTemplateNewMapper.selectTargetUserWxa(who);
            } else if (CouponSysEnum.APP.isThis(sysType)) {
                ids.add(accept.getStoreId().toString());
                who.setUserIdList(ids);
                users = couponTemplateNewMapper.selectTargetUser(who);
            }
            System.out.println("开发发送优惠券模板："+action.toString()+":"+who.toString()+":"+users.toString());

            if (users != null && users.size() > 0) {

                int receiveCount = action.getReceiveCount().intValue();
                int eachReceiveCount = action.getEachReceiveCount().intValue();

                if (CouponSysEnum.WXA.isThis(sysType)) {

                    CouponRbNewQuery couponWxa = new CouponRbNewQuery();
                    couponWxa.setTemplateId(action.getId());
                    couponWxa.setMemberId(users.get(0).getMemberId());

                    int i = couponRbNewMapper.selectCount(couponWxa);

                    if(eachReceiveCount<=i){
                        //如果已经超过了领取的数量。
                        continue;
                    }

                } else if (CouponSysEnum.APP.isThis(sysType)) {

                    CouponRbNewQuery couponApp = new CouponRbNewQuery();
                    couponApp.setTemplateId(action.getId());
                    couponApp.setStoreId(users.get(0).getStoreId());

                    int i = couponRbNewMapper.selectCount(couponApp);

                    if(eachReceiveCount<=i){
                        //如果已经超过了领取的数量。
                        continue;
                    }
                }


                // 因为用户购买商品肯定只有一个用户

                System.out.println("开发发送优惠券："+who.toString()+":"+users.get(0).toString());

                int issueCount = action.getIssueCount().intValue();
                eachReceiveCount = (issueCount-receiveCount)>eachReceiveCount ? eachReceiveCount : (issueCount-receiveCount);
                for (int i = 0; i < eachReceiveCount; i++) {
                    CouponRbNew mustCoupon = genCoupon(action, users.get(0), accept);
                    Integer rs = couponRbNewMapper.insertSelective(mustCoupon);
                    Declare.isFailed(rs == 0, GlobalsEnums.ADD_COUPON_FAILED);
                    updateTemp(action.getId(), rs.longValue());
                    size += rs;
                }

//                CouponRbNew coupon = genCoupon(action, users.get(0), accept);
//                Integer rs = couponRbNewMapper.insertSelective(coupon);
//                Declare.isFailed(rs == 0, GlobalsEnums.ADD_COUPON_FAILED);
//                updateTemp(action.getId(), rs.longValue());
//                size += rs;
            }
        }
        return size.longValue();
    }


    /**
     * 注册发放优惠券
     * <p>
     * 需要的参数 memberId / storeId  sysType sendType
     *
     * @param accept accept
     * @return java.lang.Long
     * @author Aison
     * @date 2018/8/2 18:44
     */

    private Long registerReceive(CouponAcceptVo accept) {

        // 查询出所有的购买进账优惠券的模板.. 且没有被发完的
        CouponTemplateNewQuery query = new CouponTemplateNewQuery();
        query.setIsAlive(1);
        query.setSendType(CouponSendEnum.REGISTER.getCode());
        query.setSysType(accept.getSysEnum().getCode());
        query.setStatus(0);
        List<CouponTemplateNew> templates = couponTemplateNewMapper.selectList(query);

        Integer size = 0;
        // 获取收益最大的一个优惠券模板
        for (CouponTemplateNew action : templates) {
            if (action == null) {
                continue;
            }
            // 判断当前订单是否符合优惠券领取要求
            CouponVo vo = new CouponVo(action);
            CouponWho who = vo.getWho();
            who.setOrderNo(accept.getOrderNo());
            List<CouponUser> users = null;
            Integer sysType = action.getSysType();
            List<String> ids = new ArrayList<>();
            if (CouponSysEnum.WXA.isThis(sysType)) {
                ids.add(accept.getMemberId().toString());
                who.setUserIdList(ids);
                users = couponTemplateNewMapper.selectTargetUserWxa(who);
            } else if (CouponSysEnum.APP.isThis(sysType)) {
                ids.add(accept.getStoreId().toString());
                who.setUserIdList(ids);
                users = couponTemplateNewMapper.selectTargetUser(who);
            }
            if (users != null && users.size() > 0) {
                // 因为是注册所以只有一个用户
                //CouponRbNew coupon = genCoupon(action, users.get(0), accept);
                int eachReceiveCount = action.getEachReceiveCount().intValue();
                int receiveCount = action.getReceiveCount().intValue();
                int issueCount = action.getIssueCount().intValue();
                eachReceiveCount = (issueCount-receiveCount)>eachReceiveCount ? eachReceiveCount : (issueCount-receiveCount);
                for (int i = 0; i < eachReceiveCount; i++) {
                    CouponRbNew mustCoupon = genCoupon(action, users.get(0), accept);
                    Integer rs = couponRbNewMapper.insertSelective(mustCoupon);
                    Declare.isFailed(rs == 0, GlobalsEnums.ADD_COUPON_FAILED);
                    updateTemp(action.getId(), rs.longValue());
                    size += rs;
                }
            }
        }
        return size.longValue();
    }

    /**
     * @param temp   优惠券模板
     * @param accept 参数
     * @return com.jiuy.rb.model.coupon.CouponNew
     * @author Aison
     * @date 2018/8/3 11:03
     */
    private CouponRbNew genCoupon(CouponTemplateNew temp, CouponUser user, CouponAcceptVo accept) {

        CouponRbNew coupon = new CouponRbNew();
        coupon.setCreateTime(new Date());
        coupon.setDiscount(temp.getDiscount());
        coupon.setMemberId(user.getMemberId());
        coupon.setOrderNo(accept.getOrderNo());
        coupon.setPrice(temp.getPrice());
        // -2删除 -1:作废  0:未用 1:已使用

        //coupon.setStatus(0);
        coupon.setStatus(accept.getStatus());
        coupon.setStoreId(user.getStoreId());
        coupon.setTemplateId(temp.getId());
        coupon.setTemplateName(temp.getName());
        coupon.setUserType(user.getSysType());
        coupon.setPublishUser(temp.getPublishUser());
        coupon.setPublishUserId(temp.getPublishUserId());
        coupon.setLimitMoney(temp.getLimitMoney());
        // 0 开始时间和结束时间 1是领取后多久
        Integer type = temp.getDeadlineType();
        if (type == 0) {
            coupon.setUseBeginTime(temp.getDeadlineBegin());
            coupon.setUseEndTime(temp.getDeadlineEnd());
        }
        if (type == 1) {
            Date now = new Date();
            coupon.setUseBeginTime(now);
            coupon.setUseEndTime(Biz.addDate(now, 24 * temp.getDeadlineCount()));
        }
        return coupon;
    }


    /**
     * 供应商后台的优惠券模板分页
     *
     * @param query query
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @author Aison
     * @date 2018/8/9 13:48
     */
    @Override
    public Map<String, Object> tempPageSupplier(CouponTemplateNewQuery query) {

        MyPage<CouponTemplateNew> page = new MyPage<>(couponTemplateNewMapper.selectList(query));

        List<Map<String, Object>> rows = new ArrayList<>(20);
        for (CouponTemplateNew template : page.getRows()) {
            Map<String, Object> map = new HashMap<>();
            map.put("supplierId", template.getPublishUserId());
            map.put("Name", template.getName());
            map.put("Money", template.getPrice());
            map.put("RangeType", 0);
            map.put("ValidityStartTime", Biz.formatDate(template.getDeadlineBegin(), null));
            map.put("validTotalCount", template.getIssueCount());
            CouponRbNewQuery couponRbNewQuery = new CouponRbNewQuery();
            couponRbNewQuery.setTemplateId(template.getId());
            couponRbNewQuery.setStatus(1);
            map.put("usedCount", couponRbNewMapper.selectCount(couponRbNewQuery));

            // -1：全部 0：未发放 1：发放中  2：已停止  3：已作废
            // -1：删除，0：正常，1：停止发行，2已领完,  3已失效
            Integer status = template.getStatus();
            Integer publishStatus = 0;
            if (status == -1) {
                publishStatus = 3;
            } else if (status == 0) {
                publishStatus = 1;
            } else if (status == 2 || status == 1) {
                publishStatus = 2;
            } else if (status == 3) {
                publishStatus = 3;
            }
            map.put("publishStatus", publishStatus);
            map.put("Status", template.getStatus());
            map.put("ValidityEndTime", Biz.formatDate(template.getDeadlineEnd(), null));
            map.put("isOvertime", 0);
            map.put("drawStatus", 0);
            map.put("CreateTime", Biz.formatDate(template.getCreateTime(), null));
            map.put("validTotalAmount", template.getPrice().multiply(BigDecimal.valueOf(template.getIssueCount())));
            CouponRbNewQuery couponRbNewQueryTime = new CouponRbNewQuery();
            couponRbNewQueryTime.setAliveType(0);
            couponRbNewQueryTime.setTemplateId(template.getId());
            map.put("overtimeCount", couponRbNewMapper.selectCount(couponRbNewQueryTime));
            map.put("limitDraw", template.getEachReceiveCount());
            map.put("Type", 0);
            map.put("cancelCount", 0);
            map.put("drawEndTime", 0);
            map.put("LimitMoney", template.getLimitMoney());
            map.put("drawStartTime", 0);
            map.put("UpdateTime", template.getUpdateTime());
            map.put("canDelOrUpdate", 0);
            map.put("publisher", template.getPublishUser());
            map.put("Id", template.getId());
            rows.add(map);
        }
        Map<String, Object> restMap = new HashMap<>();
        restMap.put("rows", rows);
        restMap.put("total", page.getTotal());
        return restMap;
    }


    /**
     * 通过id获取temp
     *
     * @param query query
     * @return com.jiuy.rb.model.coupon.CouponTemplateNew
     * @author Aison
     * @date 2018/8/9 16:34
     */
    @Override
    public CouponTemplateNew getOneTemp(CouponTemplateNewQuery query) {

        return couponTemplateNewMapper.selectOne(query);
    }


    /**
     * 修改模板
     *
     * @param templateNew templateNew
     * @return int
     * @author Aison
     * @date 2018/8/9 17:34
     */
    @Override
    public int updateTemp(CouponTemplateNew templateNew) {

        return couponTemplateNewMapper.updateByPrimaryKeySelective(templateNew);
    }

    /**
     * 统计每个模板的使用情况
     *
     * @param param param
     * @return java.math.BigDecimal
     * @author Aison
     * @date 2018/8/9 17:57
     */
    @Override
    public BigDecimal sumTempPrice(Map<String, Object> param) {

        return couponTemplateNewMapper.sumPrice(param);
    }

    /**
     * 获取单个优惠券
     *
     * @param query query
     * @return com.jiuy.rb.model.coupon.CouponRbNew
     * @author Aison
     * @date 2018/8/10 14:43
     */
    @Override
    public CouponRbNew getOneCoupon(CouponRbNewQuery query) {

        return couponRbNewMapper.selectOne(query);
    }

}
