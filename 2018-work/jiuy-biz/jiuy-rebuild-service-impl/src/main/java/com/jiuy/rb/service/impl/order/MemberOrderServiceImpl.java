package com.jiuy.rb.service.impl.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.annotation.Nullable;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.enums.ModelType;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.util.CallBackUtil;
import com.util.ServerPathUtil;
import com.jiuy.base.exception.Declare;
import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.MyPage;
import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.rb.enums.*;
import com.jiuy.rb.mapper.order.*;
import com.jiuy.rb.mapper.product.ProductSkuRbNewMapper;
import com.jiuy.rb.mapper.product.SecondBuyActivityRbMapper;
import com.jiuy.rb.mapper.product.TeamBuyActivityRbMapper;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.coupon.*;
import com.jiuy.rb.model.order.*;
import com.jiuy.rb.model.product.*;
import com.jiuy.rb.service.common.ICacheService;
import com.jiuy.rb.service.common.IDataDictionaryService;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.order.IMemberOrderService;
import com.jiuy.rb.service.product.IProductService;
import com.jiuy.rb.service.product.ISalesVolumeService;
import com.jiuy.rb.service.product.IShopProductService;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpClientUtils;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.jiuy.rb.enums.MemberOrderCancelEnum.MEMBER_DO;
import static com.jiuy.rb.enums.MemberOrderCancelEnum.SYSTEM_DO;
import static com.jiuy.rb.enums.SalesVolumeProductTypeEnum.SHOP_PRODUCT;
import static com.jiuy.rb.enums.SalesVolumeProductTypeEnum.SHOP_SECOND;
import static com.jiuy.rb.enums.SalesVolumeProductTypeEnum.SHOP_TEAM;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/23 9:25
 * @Copyright 玖远网络
 */
@Service( "memberOrderService" )
public class MemberOrderServiceImpl implements IMemberOrderService{


    private static final Log logger = LogFactory.get (MemberOrderServiceImpl.class);

    @Autowired
    private ShopMemberOrderRbMapper shopMemberOrderRbMapper;

    @Autowired
    private ShopMemberOrderItemRbMapper shopMemberOrderItemRbMapper;

    @Autowired
    private ShopMemberOrderLogRbMapper shopMemberOrderLogRbMapper;

    @Autowired
    private ShopMemberCouponRbMapper shopMemberCouponRbMapper;

    @Autowired
    private ProductSkuRbNewMapper productSkuMapper;

    @Autowired
    private TeamBuyActivityRbMapper teamBuyActivityRbMapper;

    @Autowired
    private SecondBuyActivityRbMapper secondBuyActivityRbMapper;

    @Resource( name = "cacheService" )
    private ICacheService cacheService;

    @Resource( name = "productService" )
    private IProductService productService;

    @Resource( name = "couponServerNew" )
    private ICouponServerNew couponService;

    @Resource( name = "shopProductServiceRb" )
    private IShopProductService shopProductService;
    @Resource( name = "salesVolumeService" )
    private ISalesVolumeService salesVolumeService;

    @Resource(name = "dataDictionaryServiceRb")
    private IDataDictionaryService dataDictionaryService;

    /**
     * 通过订单号来查询订单
     *
     * @param orderNo orderNo
     * @return com.jiuy.rb.model.order.ShopMemberOrderRb
     * @author Aison
     * @date 2018/7/23 9:26
     */
    @Override
    public ShopMemberOrderRb getOrderByOrderNo(String orderNo) {

        if (Biz.isEmpty (orderNo)) {
            return null;
        }

        ShopMemberOrderRbQuery query = new ShopMemberOrderRbQuery ();
        query.setOrderNumber (orderNo);
        return shopMemberOrderRbMapper.selectOne (query);
    }

    /**
     * 获取小程序订单列表
     *
     * @return com.jiuy.base.model.MyPage
     * @auther hyq
     * @date 2018/7/27 9:58
     */
    @Override
    public MyPage<Map<String, Object>> orderList(ShopMemberOrderRbQuery query) {
        return new MyPage<> (shopMemberOrderRbMapper.selectMemberOrderList (query));
    }

    @Override
    public MyPage<Map<String, Object>> refundOrderList(ShopMemberOrderRbQuery query) {
        return null;
    }

    /**
     * 描述:  获取C端订单详情 传id
     *
     * @return com.jiuy.base.util.ResponseResult
     * @auther hyq
     * @date 2018/7/29 19:34
     */
    @Override
    public ShopMemberOrderRbQuery orderInfo(Long orderId) {

        ShopMemberOrderRb shopMemberOrderRb = shopMemberOrderRbMapper.selectByPrimaryKey (orderId);

        String expressInfo = shopMemberOrderRb.getExpressInfo ();

        ShopMemberOrderRbQuery query = Biz.copyBean (shopMemberOrderRb, ShopMemberOrderRbQuery.class);
        query.setShopMemberOrderItemList (getOrderItem (shopMemberOrderRb.getOrderNumber ()));

        query.setCreateTimeStr (query.getCreateTime () != null ? Biz.formatDate (new Date (query.getCreateTime ()), "yyyy-MM-dd HH:mm:ss") : null);
        query.setUpdateTimeStr (query.getUpdateTime () != null ? Biz.formatDate (new Date (query.getUpdateTime ()), "yyyy-MM-dd HH:mm:ss") : null);
        query.setDeliveryTimeStr (query.getDeliveryTime () != null ? Biz.formatDate (new Date (query.getDeliveryTime ()), "yyyy-MM-dd HH:mm:ss") : null);
        query.setOrderStopTimeStr (query.getOrderStopTime () != null ? Biz.formatDate (new Date (query.getOrderStopTime ()), "yyyy-MM-dd HH:mm:ss") : null);
        query.setOrderFinishTimeStr (query.getOrderFinishTime () != null ? Biz.formatDate (new Date (query.getOrderFinishTime ()), "yyyy-MM-dd HH:mm:ss") : null);
        query.setTakeDeliveryTimeStr (query.getTakeDeliveryTime () != null ? Biz.formatDate (new Date (query.getTakeDeliveryTime ()), "yyyy-MM-dd HH:mm:ss") : null);

        if (Biz.isNotEmpty (expressInfo)) {
            JSONObject jsonObject = JSON.parseObject (expressInfo);

            JSONObject result = (JSONObject) jsonObject.get ("result");
            query.setExpressInfoCom (result.get ("com").toString ());
            query.setExpressInfoNo (result.get ("no").toString ());
            query.setExpressInfoCompany (result.get ("company").toString ());

            query.setExpressInfoList (parseJson (jsonObject));

            //就不需要物流信息字段的显示
            query.setExpressInfo ("");
        }

        CouponRbNewQuery couponRbNew = new CouponRbNewQuery();
        couponRbNew.setOrderNo(shopMemberOrderRb.getOrderNumber());
        couponRbNew.setStatus(1);
        CouponRbNew oneCoupon = couponService.getOneCoupon(couponRbNew);

        if(oneCoupon!=null){
            CouponTemplateNewQuery couponTemplateNewQuery = new CouponTemplateNewQuery();
            couponTemplateNewQuery.setId(oneCoupon.getTemplateId());
            CouponTemplateNew oneTemp = couponService.getOneTemp(couponTemplateNewQuery);
            if(oneTemp!=null){
                if(oneTemp.getPlatformType().intValue()==0){
                    query.setPlatformCoupon(query.getSaleMoney());
                }else {
                    query.setBusinessCoupon(query.getSaleMoney());
                }
            }

        }

        return query;
    }

    /**
     * 解析物流信息json
     */
    private static List<Map<String, Object>> parseJson(JSONObject jsonObject) {

        JSONObject result = (JSONObject) jsonObject.get ("result");

        JSONArray arrayList = (JSONArray) result.get ("data");

        List<Map<String, Object>> list = new ArrayList<> ();
        //Map<String, Object> data = new HashMap<>();

        if (arrayList != null) {
            DateFormat df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < arrayList.size (); i++) {
                JSONObject obj = (JSONObject) arrayList.get (i);
                Map<String, Object> map = new HashMap<> ();
                String context = obj.get ("context").toString ();
                Date time = null;
                try {
                    time = df.parse (obj.get ("time").toString ());
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                //String date = df.format(time);
                map.put ("context", context);
                map.put ("date", time);
                //map.put("date1", obj.get("time").toString());
                list.add (map);
            }
        }
        /*
         * int compare(Person p1, Person p2) 返回一个基本类型的整型，
         * 返回负数表示：p1 小于p2，
         * 返回0 表示：p1和p2相等，
         * 返回正数表示：p1大于p2
         */
        Collections.sort (list, (p1, p2) -> {
            //按照Person的年龄进行升序排列
            Date p1d = (Date) p1.get ("date");
            Date p2d = (Date) p2.get ("date");
            if (p1d.before (p2d)) {
                return 1;
            }
            return - 1;
        });
        return list;
    }

    public static void main(String[] args) {
//        JSONObject jsonObject = JSON.parseObject("{\"result\":{\"com\":\"shunfeng\",\"no\":\"12345678\",\"ischeck\":true,\"data\":[{\"context\":\"卖家发货，如长时间未更新建议咨询快递公司或卖家。联系电话：[95338]\",\"time\":\"2017-11-25 12:41:17\"},{\"context\":\"商品已经下单\",\"time\":\"2017-11-08 09:39:46\"},{\"context\":\"您的包裹已出库\",\"time\":\"2017-10-21 16:45:55\"},{\"context\":\"您的订单开始处理\",\"time\":\"2017-10-21 16:12:02\"},{\"context\":\"当前订单已确认收货，交易成功。如未收到包裹，请及时联系卖家解决\",\"time\":\"2017-08-30 20:52:57\"},{\"context\":\"您已在成都华润凤凰城二期6栋店完成取件，感谢使用菜鸟驿站，期待再次为您服务。\",\"time\":\"2017-07-26 16:14:28\"},{\"context\":\"您的包裹已打包\",\"time\":\"2017-07-17 09:20:35\"},{\"context\":\"您的包裹已出库\",\"time\":\"2017-07-17 09:20:35\"},{\"context\":\"您的订单信息审核通过\",\"time\":\"2017-07-16 20:55:38\"},{\"context\":\"您的订单开始处理\",\"time\":\"2017-07-16 18:46:14\"},{\"context\":\"当前订单已确认收货，交易成功。如未收到包裹，请及时联系卖家解决\",\"time\":\"2017-07-06 11:00:08\"},{\"context\":\"您的快件已被玛斯兰德格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15852946283\",\"time\":\"2017-05-01 14:00:42\"},{\"context\":\"您的快件已被理想家园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-05-01 13:15:31\"},{\"context\":\"您的快件已被盛城东星速递易【自提柜】代收，请及时取件。有问题请联系派件员18821774027\",\"time\":\"2017-05-01 12:51:09\"},{\"context\":\"您的快件已被荷康花园A05/A06（乐城）速递易【自提柜】代收，请及时取件。有问题请联系派件员13590257377\",\"time\":\"2017-05-01 12:22:25\"},{\"context\":\"您的快件已被新东方龙湾B区【自提柜】代收，请及时取件。有问题请联系派件员15552133078\",\"time\":\"2017-05-01 12:18:22\"},{\"context\":\"您的快件已被竹溪苑【自提柜】代收，请及时取件。有问题请联系派件员18577767689\",\"time\":\"2017-05-01 11:51:07\"},{\"context\":\"您的快件已被锦绣天下速递易【自提柜】代收，请及时取件。有问题请联系派件员13367171777\",\"time\":\"2017-05-01 11:49:01\"},{\"context\":\"您的快件已被新月鸿锦苑【自提柜】代收，请及时取件。有问题请联系派件员13120825218\",\"time\":\"2017-05-01 11:44:07\"},{\"context\":\"您的快件已被麟瑞商务广场速递易【自提柜】代收，请及时取件。有问题请联系派件员18563901120\",\"time\":\"2017-05-01 11:14:55\"},{\"context\":\"您的快件已被绵阳高新区倍特领尚一期【自提柜】代收，请及时取件。有问题请联系派件员18989287030\",\"time\":\"2017-05-01 11:09:55\"},{\"context\":\"您的快件已被蓝钻100格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15015919595\",\"time\":\"2017-05-01 10:56:13\"},{\"context\":\"您的快件已被众安隐龙湾商业广场1幢速递易【自提柜】代收，请及时取件。有问题请联系派件员15958117230\",\"time\":\"2017-05-01 10:40:31\"},{\"context\":\"您的快件已被万邦国际名城速递易【自提柜】代收，请及时取件。有问题请联系派件员18906553378\",\"time\":\"2017-05-01 10:39:31\"},{\"context\":\"您的快件已被新中苑格格货栈【自提柜】代收，请及时取件。有问题请联系派件员15914073024\",\"time\":\"2017-05-01 10:38:30\"},{\"context\":\"您的快件已被东皇先锋北区【自提柜】代收，请及时取件。有问题请联系派件员15543538250\",\"time\":\"2017-05-01 10:12:34\"},{\"context\":\"您的快件已被东山花园B区速递易【自提柜】代收，请及时取件。有问题请联系派件员15571710588\",\"time\":\"2017-05-01 10:04:33\"},{\"context\":\"您的快件已被龙湖三千城一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13981905979\",\"time\":\"2017-05-01 10:04:08\"},{\"context\":\"您的快件已被丝绸小区【自提柜】代收，请及时取件。有问题请联系派件员13656100859\",\"time\":\"2017-05-01 09:37:59\"},{\"context\":\"您的快件已被锦尚春天b区速递易【自提柜】代收，请及时取件。有问题请联系派件员17508140103\",\"time\":\"2017-05-01 09:24:06\"},{\"context\":\"您的快件已被自贡市汇景苑【自提柜】代收，请及时取件。有问题请联系派件员18882016315\",\"time\":\"2017-05-01 09:14:40\"},{\"context\":\"您的快件已被雨花村大门口【自提柜】代收，请及时取件。有问题请联系派件员13770589971\",\"time\":\"2017-05-01 07:43:10\"},{\"context\":\"您的快件已被成都青羊区上河新城九栋【自提柜】代收，请及时取件。有问题请联系派件员13540194034\",\"time\":\"2017-05-01 07:33:43\"},{\"context\":\"您的快件已被东原D7速递易【自提柜】代收，请及时取件。有问题请联系派件员13883423396\",\"time\":\"2017-05-01 00:08:25\"},{\"context\":\"您的快件已被托乐嘉街区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18668075017\",\"time\":\"2017-04-30 22:07:43\"},{\"context\":\"您的快件已被托乐嘉街区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18668075017\",\"time\":\"2017-04-30 22:07:01\"},{\"context\":\"您的快件已被领海国际一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13153631355\",\"time\":\"2017-04-30 21:02:56\"},{\"context\":\"您的快件已被理想家园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 19:27:31\"},{\"context\":\"您的快件已被景明佳园四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13451811474\",\"time\":\"2017-04-30 19:21:30\"},{\"context\":\"您的快件已被景明佳园四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13451811474\",\"time\":\"2017-04-30 19:20:30\"},{\"context\":\"您的快件已被柳州供电段单位宿舍速递易【自提柜】代收，请及时取件。有问题请联系派件员15007728956\",\"time\":\"2017-04-30 18:14:07\"},{\"context\":\"您的快件已被成都金科天籁城10栋架空层1号机【自提柜】代收，请及时取件。有问题请联系派件员15884553228\",\"time\":\"2017-04-30 17:25:57\"},{\"context\":\"您的快件已被上游三区速递易【自提柜】代收，请及时取件。有问题请联系派件员15007728956\",\"time\":\"2017-04-30 16:35:52\"},{\"context\":\"您的快件已被澳门花园东区速递易【自提柜】代收，请及时取件。有问题请联系派件员15153269321\",\"time\":\"2017-04-30 16:28:48\"},{\"context\":\"您的快件已被澳门花园东区速递易【自提柜】代收，请及时取件。有问题请联系派件员15153269321\",\"time\":\"2017-04-30 16:27:23\"},{\"context\":\"您的快件已被广安市世博花园【自提柜】代收，请及时取件。有问题请联系派件员15982606686\",\"time\":\"2017-04-30 16:19:38\"},{\"context\":\"您的快件已被海棠佳苑B速递易【自提柜】代收，请及时取件。有问题请联系派件员17508140103\",\"time\":\"2017-04-30 15:36:57\"},{\"context\":\"您的快件已被理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 14:28:08\"},{\"context\":\"您的快件已被理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员18001223384\",\"time\":\"2017-04-30 14:27:54\"},{\"context\":\"您的快件已被华昱家园速递易【自提柜】代收，请及时取件。有问题请联系派件员18500914002\",\"time\":\"2017-04-30 13:38:11\"},{\"context\":\"您的快件已被龙禧园四区格格货栈【自提柜】代收，请及时取件。有问题请联系派件员13821581300\",\"time\":\"2017-04-30 13:28:57\"},{\"context\":\"您的快件已被青龙鹭苑速递易【自提柜】代收，请及时取件。有问题请联系派件员15983825944\",\"time\":\"2017-04-30 13:13:54\"},{\"context\":\"您的快件已被滨江苑二期速递易【自提柜】代收，请及时取件。有问题请联系派件员15171249240\",\"time\":\"2017-04-30 12:50:44\"},{\"context\":\"您的快件已被云翔·卡纳源筑一期速递易【自提柜】代收，请及时取件。有问题请联系派件员13573893040\",\"time\":\"2017-04-30 12:49:56\"},{\"context\":\"您的快件已被安德利迎海花园速递易【自提柜】代收，请及时取件。有问题请联系派件员13255456577\",\"time\":\"2017-04-30 12:46:30\"},{\"context\":\"您的快件已由成都华润凤凰城二期6栋店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-04-07 18:57:10\"},{\"context\":\"您的快件已由金盛路天地新城天一座01栋19号车库菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-03-29 12:30:07\"},{\"context\":\"您的快件已由福州连江官坂大街糕兴西饼屋店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-03-11 17:04:56\"},{\"context\":\"您的快件已被SX金色丽城A格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-18 15:33:12\"},{\"context\":\"您的快件已由成都东苑C区店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-02-17 14:55:30\"},{\"context\":\"您的快件已被协信阿卡迪亚E2组团格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-13 17:52:17\"},{\"context\":\"您的快件已被锦西人家四期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-02-11 10:41:06\"},{\"context\":\"您的快件已由济南盛福花园46号楼6单元店菜鸟驿站代收。请凭取货码及时取件（查询方式：短信/物流详情页）\",\"time\":\"2017-01-24 16:06:18\"},{\"context\":\"您的快件已被金雅苑三期格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-19 23:09:33\"},{\"context\":\"您的快件已被南源花园南门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-19 09:11:36\"},{\"context\":\"您的快件已被BJ理想家园北门格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-13 10:54:04\"},{\"context\":\"您的快件已被千禧园格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-12 10:10:53\"},{\"context\":\"您的快件已被BJ晓月六里格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-12 08:47:35\"},{\"context\":\"您的快件已被HB国瑞园中区入口西侧格格货栈【自提柜】代收，请及时取件。有问题请联系派件员\",\"time\":\"2017-01-10 09:48:59\"}],\"company\":\"顺丰速运\",\"updatetime\":\"2018-04-04 14:42:09\"},\"reason\":\"成功\",\"error_code\":0}");
//
//        JSONObject result = (JSONObject)jsonObject.get("result");
//        System.out.println(result.get("com").toString());
//        System.out.println(result.get("no").toString());
//        System.out.println(result.get("company").toString());
//        JSONArray arrayList = (JSONArray)result.get("data");
//
//        JSONObject aa =  (JSONObject)arrayList.get(0);
//        System.out.println(aa.get("context"));
//        System.out.println(aa.get("time"));

        System.out.println (ResponseResult.instance ().success (new ShopMemberOrderRb ()));


        //System.out.println(jsonObject.get("result").toString());
        // Map<String, Object> stringObjectMap = parseJson(jsonObject);
    }

    /**
     * C端订单详情
     *
     * @param orderNum
     * @return java.util.List<com.jiuy.rb.model.order.ShopMemberOrderItemRbQuery>
     * @author hyq
     * @date 2018/7/30 10:40
     */
    @Override
    public List<ShopMemberOrderItemRbQuery> getOrderItem(String orderNum) {

        ShopMemberOrderItemRbQuery query = new ShopMemberOrderItemRbQuery ();
        query.setOrderNumber (orderNum);
        return MyPage.copy2Child (shopMemberOrderItemRbMapper.selectList (query), ShopMemberOrderItemRbQuery.class, (source, target) -> {
            ShopProductRb shopProductRb = shopProductService.getById(source.getShopProductId());

            target.setDetailImage (shopProductRb.getSummaryImages());
            target.setProductName (shopProductRb.getName ());
            target.setClothesNumber (shopProductRb.getClothesNumber ());
//            if(shopProductRb.getProductId()>0){
//                ProductRb productRb = productService.getById(shopProductRb.getProductId());
//                target.setDetailImage (productRb.getDetailImages ());
//                target.setProductName (productRb.getName ());
//                target.setClothesNumber (productRb.getClothesNumber ());
//            }else{
//                target.setDetailImage (shopProductRb.getShopOwnDetail());
//                target.setProductName (shopProductRb.getName ());
//                target.setClothesNumber (shopProductRb.getClothesNumber ());
//            }

        }).getRows ();
    }

    /**
     * 描述: 更新C端订单信息
     *
     * @return com.jiuy.base.util.ResponseResult
     * @auther hyq
     * @date 2018/7/29 19:48
     */
    @Override
    @MyLogs( logInfo = "修改订单", model = ModelType.ORDER_MODEL )
    public MyLog<Long> updateOrder(ShopMemberOrderRb query, UserSession userSession) {

        Declare.notNull (query.getOrderNumber (), "订单号为空");
        ShopMemberOrderRb shopMemberOrderRbOld = shopMemberOrderRbMapper.selectByPrimaryKey (query.getId ());
        Declare.notNull (shopMemberOrderRbOld, GlobalsEnums.PARAM_ERROR);

        Integer orderStatus = query.getOrderStatus ();

        shopMemberOrderRbMapper.updateByPrimaryKeySelective (query);

        return new MyLog<> (shopMemberOrderRbOld, query, userSession);

    }

    /**
     * 描述 C端订单发货
     *
     * @param query
     * @param userSession
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     * @author hyq
     * @date 2018/7/30 11:24
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    @MyLogs( logInfo = "订单发货", model = ModelType.ORDER_MODEL )
    public MyLog<Long> deliverOrder(ShopMemberOrderRbQuery query, UserSession userSession) {

        ShopMemberOrderRbQuery newQuery = new ShopMemberOrderRbQuery ();
        newQuery.setOrderNumber (query.getOrderNumber ());
        ShopMemberOrderRb newShopMemberOrderRb = shopMemberOrderRbMapper.selectOne (newQuery);

        Declare.notNull (newShopMemberOrderRb, GlobalsEnums.ORDER_NOT_FOUND);
        Integer orderStatus = newShopMemberOrderRb.getOrderStatus ();

        MyLog<Long> mylog = new MyLog<> (userSession);
        //0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货

        if (! MemberOrderStatusEnum.PENDING_DELIVERY.isThis (orderStatus) && ! MemberOrderStatusEnum.DELIVER.isThis (orderStatus) &&
                ! MemberOrderStatusEnum.SUCCESS.isThis (orderStatus)) {
            logger.info ("该订单不在发货期间不能进行发货！");
            throw new RuntimeException ("该订单不在发货期间不能进行发货！");
        }

        //更新信息
        ShopMemberOrderRb newOrder = new ShopMemberOrderRb ();

        newOrder.setId (newShopMemberOrderRb.getId ());
        newOrder.setOrderNumber (query.getOrderNumber ());
        newOrder.setOrderStatus (MemberOrderStatusEnum.DELIVER.getCode ());
        newOrder.setDeliveryTime(System.currentTimeMillis());

        if (Biz.isNotEmpty (query.getExpreeSupplierCnname ())) {
            newOrder.setExpreeSupplierCnname (query.getExpreeSupplierCnname ());
        }

        if (Biz.isNotEmpty (query.getExpressSupplier ())) {
            newOrder.setExpressSupplier (query.getExpressSupplier ());
        }

        if (Biz.isNotEmpty (query.getExpressNo ())) {
            newOrder.setExpressNo (query.getExpressNo ());
        }

        if (Biz.isNotEmpty (query.getReceiverName ())) {
            newOrder.setReceiverName (query.getReceiverName ());
        }

        if (Biz.isNotEmpty (query.getReceiverPhone ())) {
            newOrder.setReceiverPhone (query.getReceiverPhone ());
        }

        if (Biz.isNotEmpty (query.getReceiverAddress ())) {
            newOrder.setReceiverAddress (query.getReceiverAddress ());
        }

        if (Biz.isNotEmpty (query.getRemark ())) {
            newOrder.setRemark (query.getRemark ());
        }

        if (Biz.isNotEmpty (query.getExpressInfo ())) {
            newOrder.setExpressInfo (query.getExpressInfo ());
        }

        shopMemberOrderRbMapper.updateByPrimaryKeySelective (newOrder);

        //添加日志  已经发过货的就不要记录了
        if (MemberOrderStatusEnum.PENDING_DELIVERY.isThis (orderStatus)) {
            addShopMemberOrderLog (newShopMemberOrderRb.getMemberId (), orderStatus, newOrder, mylog);
        }

        return mylog;
    }

    /**
     * 描述 C端订单取消
     * * @param query
     *
     * @param userSession
     * @return int
     * @author hyq
     * @date 2018/7/30 15:29
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    //@MyLogs(logInfo = "取消订单",model = ModelType.ORDER_MODEL)
    public int stopOrder(ShopMemberOrderRbQuery query, UserSession userSession) {

        ShopMemberOrderRbQuery newQuery = new ShopMemberOrderRbQuery ();
        newQuery.setOrderNumber (query.getOrderNumber ());
        ShopMemberOrderRb newShopMemberOrderRb = shopMemberOrderRbMapper.selectOne (newQuery);

        Declare.notNull (newShopMemberOrderRb, GlobalsEnums.ORDER_NOT_FOUND);
        Integer oldOrderStatus = newShopMemberOrderRb.getOrderStatus ();

        ShopMemberOrderRb newOrder = new ShopMemberOrderRb ();

        newOrder.setId (newShopMemberOrderRb.getId ());
        long time = System.currentTimeMillis ();

        if ((MemberOrderStatusEnum.REFUNDING.isThis (oldOrderStatus)) || MemberOrderStatusEnum.CLOSED.isThis (oldOrderStatus)) {
//            throw new RuntimeException("已经是处理关闭的订单");
            return 0;
        }

        if (MemberOrderStatusEnum.SUCCESS.isThis (oldOrderStatus)) {
            newOrder.setOrderStatus (MemberOrderStatusEnum.REFUNDING.getCode ());
        }
        else if (MemberOrderStatusEnum.UNPAID.isThis (oldOrderStatus)) {
            newOrder.setOrderStatus (MemberOrderStatusEnum.CLOSED.getCode ());
        }
        else {
            newOrder.setOrderStatus (MemberOrderStatusEnum.CLOSED.getCode ());
        }
        //newOrder.setOrderStatus(MemberOrderStatusEnum.CLOSED.getCode());
        //取消类型：0无、1会员取消、2商家取消、3系统自动取消 4商家手动结束活动，关闭订单 5 平台客服关闭订单
        newOrder.setCancelReasonType (5);
        newOrder.setCancelReason ("平台客服关闭订单");
        newOrder.setOrderStopTime (time);
        newOrder.setUpdateTime (time);

        //添加日志
        MyLog<Long> mylog = new MyLog<> (userSession);

        int record = shopMemberOrderRbMapper.updateByPrimaryKeySelective (newOrder);

        if (record == 1) {
            //添加日志
            addShopMemberOrderLog (newShopMemberOrderRb.getMemberId (), oldOrderStatus, newShopMemberOrderRb, mylog);

            //如果有优惠券,将优惠券设置为未使用
            long couponId = newShopMemberOrderRb.getCouponId ();

            if (couponId > 0) {

                ShopMemberCouponRb shopMemberCoupon = new ShopMemberCouponRb ();
                shopMemberCoupon.setId (couponId);
                shopMemberCoupon.setAdminId (0L);
                shopMemberCoupon.setCheckTime (0L);
                shopMemberCoupon.setCheckMoney (BigDecimal.ZERO);
                //状态:-1：删除，0：正常，1：使用
                shopMemberCoupon.setStatus (0);
                shopMemberCoupon.setUpdateTime (time);
                int couponRecord = shopMemberCouponRbMapper.updateByPrimaryKeySelective (shopMemberCoupon);
                if (couponRecord != 1) {
                    logger.error ("小程序取消订单:将优惠券设置为未使用couponId:" + couponId);
                    throw new RuntimeException ("小程序取消订单:将优惠券设置为未使用couponId:" + couponId);
                }
            }

            //秒杀订单，团购订单，取消订单后 库存回滚
            //团购。手动取消
            if (newShopMemberOrderRb.getTeamId () != null) {
                TeamBuyActivityRb teamBuyActivityRb = teamBuyActivityRbMapper.selectByPrimaryKey (newShopMemberOrderRb.getTeamId ());

                TeamBuyActivityRb tb = new TeamBuyActivityRb ();
                tb.setId (teamBuyActivityRb.getId ());
                //参与人数-1
                logger.info ("参与人数减1");
                tb.setActivityMemberCount (teamBuyActivityRb.getActivityMemberCount () - 1);

                teamBuyActivityRbMapper.updateByPrimaryKeySelective (tb);

                // 缓存中相关信息修改
                String groupKey = MemcachedKey.GROUP_KEY_activityTeamBuy;
                String key = "_teamBuyActivityId_" + String.valueOf (teamBuyActivityRb.getId ());
                Object obj = cacheService.getCommon (groupKey, key);
                logger.info ("取消团购订单取查询缓存数据groupKey+key:" + groupKey + key);
                if (obj != null) {
                    logger.info ("取消团购订单前缓存中剩余活动商品数量obj:" + obj);
                    int count = 1;
                    String memcachedcountStr = (String) obj;
                    int memcachedcount = Integer.valueOf (memcachedcountStr.trim ());
                    if (memcachedcount == 0) {
                        count = 2;
                    }
                    logger.info ("自动----取消秒杀订单前缓存中剩余活动商品数量count" + count + ",memcachedcount:" + memcachedcount);
                    cacheService.incrCommon (groupKey, key, count);
                    logger.info ("取消团购订单缓存加1");
                    logger.info ("取消团购订单后缓存中剩余活动商品数量obj:" + cacheService.getCommon (groupKey, key));
                }
                else {
                    logger.info ("取消团购订单增加缓存商品数量失败obj:" + obj);
                }

            }

            //秒杀 手动取消
            if (newShopMemberOrderRb.getSecondId () != null) {

                SecondBuyActivityRb secondBuyActivity = secondBuyActivityRbMapper.selectByPrimaryKey (newShopMemberOrderRb.getSecondId ());

                SecondBuyActivityRb newSecondBuyActivity = new SecondBuyActivityRb ();
                newSecondBuyActivity.setId (secondBuyActivity.getId ());
                //参与人数-1
                newSecondBuyActivity.setActivityMemberCount (secondBuyActivity.getActivityMemberCount () - 1);
                logger.info ("参与人数减1");
                secondBuyActivityRbMapper.updateByPrimaryKeySelective (newSecondBuyActivity);
                //缓存中相关信息修改
                String groupKey = MemcachedKey.GROUP_KEY_activitySecondBuy;
                String key = "_secondBuyActivityId_" + String.valueOf (secondBuyActivity.getId ());
                Object obj = cacheService.getCommon (groupKey, key);
                logger.info ("取消秒杀订单取查询缓存数据groupKey+key:" + groupKey + key);
                if (obj != null) {
                    logger.info ("取消秒杀订单前缓存中剩余活动商品数量obj:" + obj);
                    int count = 1;
                    String memcachedcountStr = (String) obj;
                    int memcachedcount = Integer.valueOf (memcachedcountStr.trim ());
                    if (memcachedcount == 0) {
                        count = 2;
                    }
                    logger.info ("自动----取消秒杀订单前缓存中剩余活动商品数量count" + count + ",memcachedcount:" + memcachedcount);
                    cacheService.incrCommon (groupKey, key, count);
                    logger.info ("取消秒杀订单缓存加1成功！");
                    logger.info ("取消秒杀订单后缓存中剩余活动商品数量obj:" + cacheService.getCommon (groupKey, key));
                }
                else {
                    logger.info ("取消秒杀订单失败obj:" + obj);
                }
            }

        }

        return record;
    }

    /**
     * 描述 订单的退款操作
     * * @param query
     *
     * @param userSession
     * @return int
     * @author hyq
     * @date 2018/7/30 16:23
     */
    @Override
    public int refundMoneyOrder(ShopMemberOrderRb query, UserSession userSession) {

        ShopMemberOrderRb shopMemberOrderRbOld = shopMemberOrderRbMapper.selectByPrimaryKey (query.getId ());
        Declare.notNull (shopMemberOrderRbOld, GlobalsEnums.PARAM_ERROR);

        Integer orderStatus = query.getOrderStatus ();
        Declare.notNull (orderStatus, "状态为空");

        int i = shopMemberOrderRbMapper.updateByPrimaryKeySelective (query);

        MyLog<Long> mylog = new MyLog<> (userSession);

        addShopMemberOrderLog (shopMemberOrderRbOld.getMemberId (), shopMemberOrderRbOld.getOrderStatus (), query, mylog);

        return i;
    }

    /**
     * 添加log
     *
     * @param oldStatus
     * @param shopMemberOrder
     */
    private void addShopMemberOrderLog(long memberId, int oldStatus, ShopMemberOrderRb shopMemberOrder, MyLog<Long> mylog) {

        //生成订单日志
        ShopMemberOrderLogRb orderLog = new ShopMemberOrderLogRb ();

//        MyLog<Long> mylog = new MyLog<>(userSession);

        orderLog.setOldStatus (oldStatus);
        orderLog.setMemberId (memberId);
        orderLog.setNewStatus (shopMemberOrder.getOrderStatus ());
        orderLog.setOrderId (shopMemberOrder.getId ());
        orderLog.setCreateTime (System.currentTimeMillis ());

        orderLog.setOperAccount (mylog.getUserSession ().getAccount ());

        int record = shopMemberOrderLogRbMapper.insertSelective (orderLog);
        mylog.moreLog (orderLog, MyLog.Type.add);

        if (record != 1) {
            throw new RuntimeException ("添加log失败shopMemberOrderId:" + orderLog.getOrderId ());
        }

//        // 发送个推
//        if(record==1){
//            long storeId = order.getStoreId();
//            StoreBusinessRb storeBusiness = userService.getStoreBusinessById(storeId);
//            long supplierId = order.getSupplierId();
//            SupplierUserRb supplierUser = userService.getSupplierUser(supplierId);
//            String userCID = storeBusiness.getUserCID();
//
//            appMsgService.geTui(userCID,new GeTuiVo("[俞姐姐门店宝]您在["+supplierUser.getBusinessName()+"]采购的美丽的衣服[订单号："+orderNo+"]已经发货成功了。",
//                    "",orderNo+"","", GeTuiEnum.PACKAGE_SEND.getCode(),System.currentTimeMillis()+""));
//        }
    }


    // ===> Charlie ***********************************************************************

    /**
     * 用户即将成团的订单
     *
     * @param query query
     *              必填:storeId
     *              必填:memberId
     * @return com.jiuy.base.model.MyPage
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    @Override
    public MyPage<Map<String, Object>> teamBuyActivityUnderwayList(ShopMemberOrderRbQuery query) {
        Declare.noNullParams (query.getStoreId (), query.getMemberId ());
        query.setCurrentTime (System.currentTimeMillis ());
        List<Map<String, Object>> maps = shopMemberOrderRbMapper.teamBuyActivityUnderwayList (query);
        maps.forEach (map -> {
            map.put ("simpleProductName", Biz.replaceStr (map.get ("shopProductName"), 20, "..."));

            //是否成团
            boolean isTeamSuccess = false;
            Integer conditionType = (Integer) map.get ("conditionType");
            if (ObjectUtils.nullSafeEquals (conditionType, 1)) {
                //成团人数成团
                Integer userCount = (Integer) map.get ("userCount");
                Integer activityMemberCount = (Integer) map.get ("activityMemberCount");
                if (userCount.compareTo (activityMemberCount) <= 0) {
                    isTeamSuccess = true;
                }
            }
            if (ObjectUtils.nullSafeEquals (conditionType, 2)) {
                //成团件数成团
                Integer meetProductCount = (Integer) map.get ("meetProductCount");
                Integer orderedProductCount = (Integer) map.get ("orderedProductCount");
                if (meetProductCount.compareTo (orderedProductCount) <= 0) {
                    isTeamSuccess = true;
                }
            }
            map.put ("isTeamSuccess", isTeamSuccess);

            //图片转化
            String imgStr = String.valueOf (map.get ("shopProductShowcaseImgs"));
            if (StringUtils.isNotBlank (imgStr)) {
                List<String> showcaseImgList = Biz.jsonStrToListObject (imgStr, List.class, String.class);
                map.put ("showcaseImgList", showcaseImgList);
            }

            //倒计时
            Long endTime = Long.parseLong (map.get ("activityEndTime").toString ());
            map.put ("countDown", endTime - System.currentTimeMillis ());
        });
        return new MyPage<> (maps);
    }


    /**
     * 用户已经成团的订单
     *
     * @param query query
     *              必填:storeId
     *              必填:memberId
     * @return com.jiuy.base.model.MyPage
     * @author Charlie
     * @date 2018/7/29 22:33
     */
    @Override
    public MyPage<Map<String, Object>> teamBuyActivityOKList(ShopMemberOrderRbQuery query) {
        Declare.noNullParams (query.getStoreId (), query.getMemberId ());
        query.setCurrentTime (System.currentTimeMillis ());
        List<Map<String, Object>> maps = shopMemberOrderRbMapper.teamBuyActivityOKList (query);
        //截取长度
        int limitLen = 20;
        maps.forEach (map -> {
            map.put ("simpleProductName", Biz.replaceStr (map.get ("shopProductName"), limitLen, "..."));

            //图片转化
            String imgStr = String.valueOf (map.get ("shopProductShowcaseImgs"));
            if (StringUtils.isNotBlank (imgStr)) {
                List<String> showcaseImgList = Biz.jsonStrToListObject (imgStr, List.class, String.class);
                map.put ("showcaseImgList", showcaseImgList);
            }

            //倒计时
            Long endTime = Long.parseLong (map.get ("activityEndTime").toString ());
            endTime = endTime - System.currentTimeMillis ();
            endTime = endTime < 0 ? 0 : endTime;
            map.put ("countDown", endTime);
        });
        return new MyPage<> (maps);
    }


    /**
     * 取消原因
     *
     * @param userSession storeId,memberId
     * @param orderId     orderId
     * @param reasonType  取消类型：0无、1会员取消、2商家取消、3系统自动取消 4商家手动结束活动，关闭订单 5 平台客服关闭订单
     * @param reason      取消原因
     * @author Charlie
     * @date 2018/8/3 15:33
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void cancelOrder(UserSession userSession, Long orderId, Integer reasonType, String reason) {

        Declare.noNullParams (orderId, reason);
        ShopMemberOrderRbQuery query = new ShopMemberOrderRbQuery ();
        query.setId (orderId);
        query.setMemberId (userSession.getMemberId ());
        query.setStoreId (userSession.getStoreId ());
        query.setIsSelectForUpd (true);
        ShopMemberOrderRb history = shopMemberOrderRbMapper.selectOne (query);
        Declare.existResource (history);
        //取消订单
        ShopMemberOrderRb newOrder = new ShopMemberOrderRb ();
        long curr = System.currentTimeMillis();
        newOrder.setOrderStatus (MemberOrderStatusEnum.CLOSED.getCode ());
        newOrder.setCancelReasonType (reasonType);
        newOrder.setCancelReason (reason);
        newOrder.setUpdateTime (curr);
        newOrder.setOrderStopTime (curr);
        newOrder.setId (history.getId ());
        int rec = shopMemberOrderRbMapper.updateByPrimaryKeySelective (newOrder);
        Declare.state (rec == 1, GlobalsEnums.ORDER_CLOSE_FAILED);

        //回滚优惠券未使用
        Long couponId = history.getCouponId ();
        if (Biz.isNotEmpty (couponId) && couponId > 0) {
            logger.info ("小程序用户取消订单,将优惠券设为可用 orderNumber[{}].couponId[{}].storeId[{}].memberId[{}]", history.getOrderNumber (), couponId, userSession.getStoreId (), userSession.getMemberId ());

            boolean isOk = couponService.rollbackCoupon2Available (couponId,orderId,history.getOrderNumber());
            if (isOk) {
                logger.info ("小程序用户取消订单,将优惠券设为可用 ==>失败!!! couponId[{}].storeId[{}].memberId[{}]", couponId, userSession.getStoreId (), userSession.getMemberId ());
            }
            Declare.state (isOk, GlobalsEnums.COUPON_ROLLBACK_FAILED);
            logger.info ("小程序用户取消订单,将优惠券设为可用 ==>成功 couponId[{}].storeId[{}].memberId[{}]", couponId, userSession.getStoreId (), userSession.getMemberId ());
        }

        ShopMemberOrderItemRbQuery shopMemberOrderItemRbQuery = new ShopMemberOrderItemRbQuery();
        shopMemberOrderItemRbQuery.setOrderId(history.getId());
        List<ShopMemberOrderItemRb> shopMemberOrderItemRbs = shopMemberOrderItemRbMapper.selectList(shopMemberOrderItemRbQuery);

        //订单库存回滚
        shopMemberOrderItemRbs.forEach(action->{

            //一件上传商品。不回滚库存
            if(action.getOwn().intValue()==0){
                return;
            }

            Integer count = action.getCount();
            Integer selfCount = action.getSelfCount();
            
            ProductSkuRbNew productSkuRbNew = productSkuMapper.selectByPrimaryKey(action.getProductSkuId());
            //库存为0就不更新了
//            if(productSkuRbNew.getRemainCount().intValue()==0){
//                return;
//            }

            if(action.getOwn().intValue()==2){
                productSkuRbNew.setRemainCount(productSkuRbNew.getRemainCount()+selfCount);
            }else {
                productSkuRbNew.setRemainCount(productSkuRbNew.getRemainCount()+count);
            }



            productSkuMapper.updateByPrimaryKey(productSkuRbNew);


        });

        //活动库存回滚
        if (Biz.isNotEmpty (history.getTeamId ()) && MEMBER_DO.getCode () == reasonType) {
            shopProductService.releaseInventory (history.getTeamId (), ShopActivityKindEnum.TEAM, history.getCount ());
        }
        if (Biz.isNotEmpty (history.getSecondId ()) && (MEMBER_DO.getCode () == reasonType || SYSTEM_DO.getCode () == reasonType)) {
            shopProductService.releaseInventory (history.getSecondId (), ShopActivityKindEnum.SECOND, history.getCount ());
        }

        //记录日志
        insertShopOrderOperateLog (history, newOrder);
    }


    /**
     * 记录操作日志
     *
     * @param oldOrder 老订单
     * @param newOrder 新订单
     * @author Charlie
     * @date 2018/8/3 19:19
     */
    private void insertShopOrderOperateLog(ShopMemberOrderRb oldOrder, ShopMemberOrderRb newOrder) {
        ShopMemberOrderLogRb log = new ShopMemberOrderLogRb ();
        log.setOldStatus (oldOrder.getOrderStatus ());
        log.setMemberId (oldOrder.getMemberId ());
        log.setOrderId (oldOrder.getId ());
        log.setNewStatus (newOrder.getOrderStatus ());
        log.setCreateTime (newOrder.getCreateTime ());
        int rec = shopMemberOrderLogRbMapper.insertSelective (log);
        Declare.state (rec == 1, GlobalsEnums.ORDER_ADD_LOG_FAILED);
    }


    /**
     * 确认收货
     *
     * @param user              user
     * @param shopMemberOrderId 小程序订单id
     * @author Charlie
     * @date 2018/8/5 20:48
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void confirmReceipt(UserSession user, Long shopMemberOrderId) {
        Declare.noNullParams (user, user.getMemberId (), user.getStoreId (), shopMemberOrderId);
        ShopMemberOrderRbQuery query = new ShopMemberOrderRbQuery ();
        query.setId (shopMemberOrderId);
        query.setStoreId (user.getStoreId ());
        query.setMemberId (user.getMemberId ());
        query.setIsSelectForUpd (true);
        ShopMemberOrderRb order = shopMemberOrderRbMapper.selectOne (query);
        Declare.existResource (order);

        //修改订单状态
        ShopMemberOrderRb newOrder = new ShopMemberOrderRb ();
        Date date = new Date ();
        newOrder.setId (shopMemberOrderId);
        newOrder.setOrderFinishTime (date.getTime ());
        newOrder.setConfirmSignedTime (date.getTime ());
        newOrder.setConfirmSignedDate (Integer.valueOf (new SimpleDateFormat ("yyyyMMdd").format (date)));
        newOrder.setOrderStatus (MemberOrderStatusEnum.SUCCESS.getCode ());
        int rec = shopMemberOrderRbMapper.updateByPrimaryKeySelective (newOrder);
        Declare.state (rec == 1, GlobalsEnums.ORDER_OPER_FAILED);

        //活动销量增加(普通商品暂时不处理)
        updateProductSalesVolume (order.getTeamId (), order.getSecondId (), order.getCount ());

        //计算分销收益
        BizUtil.todo ("3.8.5--确认收货---分佣返现....");
        String url = "/distribution/distribution/cashOutIn/dstbSuccess";
        JSONObject map = new JSONObject();
        map.put ("orderNumber", order.getOrderNumber ());
        map.put ("wx2DstbSign", new Md5Hash (order.getOrderNumber () + "xiaochengxu2dstb").toString ());
        map.put ("orderSuccessTime", date.getTime ());
        logger.info ("确认收货, 请求记录分销 url={},map={}", url,map);
        CallBackUtil.send(map.toJSONString(),url,"get");

    }


    /**
     * 获取提示语
     *
     * @param route route
     * @return 提示语
     * @author Charlie
     * @date 2018/8/9 16:27
     */
    @Override
    public String tip(String route) {
        TipKeyEnum key = TipKeyEnum.findByRout (route);
        DataDictionaryRb dataDict;
        switch (key) {
            case TEAM_ACTIVITY_OK:
                dataDict = dataDictionaryService.getByCode (key.getCode (), key.getGroupCode ());
                Declare.existResource (dataDict);
                return dataDict.getVal ();
            case TEAM_ACTIVITY_UNDERWAY:
                dataDict = dataDictionaryService.getByCode (key.getCode (), key.getGroupCode ());
                Declare.existResource (dataDict);
                return dataDict.getVal ();
            default:
                logger.error ("查询tip提示语, 未知的route:"+route);
                return "";
        }
    }


    /**
     * 即将成团数量
     *
     * @param query query
     * @return java.lang.Integer
     * @author Charlie
     * @date 2018/8/10 8:33
     */
    @Override
    public Integer teamBuyActivityUnderwayCount(ShopMemberOrderRbQuery query) {
        Declare.noNullParams (query.getStoreId (), query.getMemberId ());
        query.setCurrentTime (System.currentTimeMillis ());
        return shopMemberOrderRbMapper.teamBuyActivityUnderwayCount (query);
    }



    /**
     * 确认提货
     *
     * @param storeId storeId
     * @param shopMemberOrderId orderId
     * @author Charlie
     * @date 2018/8/27 23:14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmDelivery(Long storeId, Long shopMemberOrderId) {
        logger.info ("确认提货 storeId[{}].shopMemberOrderId[{}]", storeId, shopMemberOrderId);
        Declare.noNullParams (storeId, shopMemberOrderId);

        ShopMemberOrderRbQuery query = new ShopMemberOrderRbQuery ();
        query.setId (shopMemberOrderId);
        query.setStoreId (storeId);
        query.setIsSelectForUpd (true);
        ShopMemberOrderRb order = shopMemberOrderRbMapper.selectOne (query);
        Declare.existResource (order);
        if (! ObjectUtils.nullSafeEquals (order.getOrderStatus (), MemberOrderStatusEnum.WAIT_PICK_UP.getCode ())) {
            logger.error ("确认提货,订单不是提货状态 orderId[{}].orderStatus[{}]", order.getId (), order.getOrderStatus ());
            Declare.state (false, "该订单不是待提货状态,不允许提货操作");
        }

        ShopMemberOrderRb newOrder = new ShopMemberOrderRb ();
        long time = System.currentTimeMillis();
        newOrder.setId (shopMemberOrderId);
        newOrder.setTakeDeliveryTime(time);
        newOrder.setOrderFinishTime(time);
        newOrder.setOrderStatus(MemberOrderStatusEnum.SUCCESS.getCode ());
        int record = shopMemberOrderRbMapper.updateByPrimaryKeySelective (newOrder);
        Declare.state (record == 1, GlobalsEnums.ORDER_OPER_FAILED);
        logger.info ("修改订单状态成功");

        //活动销量增加(普通商品暂时不处理)
        updateProductSalesVolume (order.getTeamId (), order.getSecondId (), order.getCount ());


        //计算分销收益
        String url = "/distribution/distribution/cashOutIn/dstbSuccess";
        JSONObject map = new JSONObject();
        map.put ("orderNumber", order.getOrderNumber ());
        map.put ("wx2DstbSign", new Md5Hash (order.getOrderNumber () + "xiaochengxu2dstb").toString ());
        map.put ("orderSuccessTime", time);
        logger.info ("确认提货, 请求记录分销 url={},map={}", url,map);
        CallBackUtil.send(map.toJSONString(),url,"get");
    }


    @Override
    public void syncOrderType(long shopMemberOrderId) {
//        ShopMemberOrderRbQuery query = new ShopMemberOrderRbQuery ();
//        query.setId (shopMemberOrderId);
//        ShopMemberOrderRb order = shopMemberOrderRbMapper.selectOne (query);
//        StoreOrder storeOrder = shopMemberOrderRbMapper.findStoreOrderByMemberOrderId(shopMemberOrderId);
//        if(storeOrder!=null){
            logger.info("确认收货更新店铺订单={}", shopMemberOrderId);
            shopMemberOrderRbMapper.upStoreByShopMemberOrderId(shopMemberOrderId);
//        }

    }


    /**
     * 更新商品销量(暂时只是小程序的活动商品有销量,普通商品没有)
     *
     * @param teamId   teamId
     * @param secondId secondId
     * @param count    必须>0
     * @author Charlie
     * @date 2018/8/6 9:34
     */
    private void updateProductSalesVolume(@Nullable Long teamId, @Nullable Long secondId, Integer count) {
        logger.info ("更新商品销量 teamId[{}].secondId[{}].count[{}]", teamId, secondId, count);
        SalesVolumeProductTypeEnum type;
        if (Biz.isNotEmpty (teamId) && teamId > 0) {
            type = SHOP_TEAM;
        }
        else if (Biz.isNotEmpty (secondId) && secondId > 0) {
            type = SHOP_SECOND;
        }
        else {
            type = SHOP_PRODUCT;
        }


        if (type != SHOP_PRODUCT) {
            //新增/修改小程序活动商品销量信息
            SalesVolumeProductRb salesPdvInfo = new SalesVolumeProductRb ();
            salesPdvInfo.setProductId (type == SHOP_SECOND ? secondId : teamId);
            salesPdvInfo.setProductType (type.getCode ());

            SalesVolumeProductRb addInfo = new SalesVolumeProductRb ();
            addInfo.setOrderSuccessCount (new Long (count));
            int rec = salesVolumeService.updOrAddSalesVolumeCount (salesPdvInfo, addInfo);
            Declare.state (rec == 1., GlobalsEnums.UP_FAILED);
            logger.info ("更新小程序活动销量成功");

            //如果商品是供应商的商品,供应商销量同步+1
            Long shopProductId = null;
            if (type == SHOP_SECOND) {
                SecondBuyActivityRbQuery secondQuery = new SecondBuyActivityRbQuery ();
                secondQuery.setId (salesPdvInfo.getProductId ());
                SecondBuyActivityRb second = shopProductService.selectOne (secondQuery);
                Declare.notNull (second, GlobalsEnums.NO_RESOURCES);
                shopProductId = second.getShopProductId ();
            }
            if (type == SHOP_TEAM) {
                TeamBuyActivityRbQuery teamQuery = new TeamBuyActivityRbQuery ();
                teamQuery.setId (salesPdvInfo.getProductId ());
                TeamBuyActivityRb team = shopProductService.selectOne (teamQuery);
                Declare.notNull (team, GlobalsEnums.NO_RESOURCES);
                shopProductId = team.getShopProductId ();
            }
            logger.info ("获取活动对应的供应商商品id[{}]", shopProductId);

            ShopProductRb shopProduct = shopProductService.getById (shopProductId);
            Declare.existResource (shopProduct);
            if (Biz.isNotEmpty (shopProduct.getProductId ()) && shopProduct.getProductId () > 0) {
                //同步更新供应商商品销量
                rec = productService.addSaleTotalCount (shopProduct.getProductId (), count);
                logger.info ("更新供应商商品销量 rec[{}]", rec);
            }
        }
        else {
            //小程序普通商品....
        }
    }

    // *********************************************************************** <=== Charlie

}
