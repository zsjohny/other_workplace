package com.wuai.company.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.Response.UserVideoResponse;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.enums.*;
import com.wuai.company.order.dao.SceneDao;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.entity.DistancePo;
import com.wuai.company.order.entity.TrystScene;
import com.wuai.company.order.entity.TrystSceneExample;
import com.wuai.company.order.mapper.TrystSceneMapper;
import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.order.util.NearbyUtil;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.Arith;
import com.wuai.company.util.Response;
import com.wuai.company.util.SysUtils;
import com.wuai.company.util.UserUtil;
import com.wuai.company.util.comon.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hyf on 2018/1/17.
 */
@Service
@Transactional
public class TrystOrdersServiceImpl implements TrystOrdersService {
    Logger logger = LoggerFactory.getLogger(TrystOrdersServiceImpl.class);

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    @Autowired
    private NearbyUtil nearbyUtil;

    @Autowired
    private SceneDao sceneDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TrystSceneMapper trystSceneMapper;

    @Resource
    private HashOperations<String,String,SnatchUser> snatchUserTemplate;
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse[]> nearBodyTemplate;
    @Resource
    private RedisTemplate removeRedisTemplate;
//    @Resource
//    private ZSetOperations<String,String> msgValueTemplate;

    private final String USER_MSG = "%s:msg"; //用户id--信息列表
    private final String USER_TYPE_CONTEN = "%s:%s:%s"; //用户id--类型--内容
    private final String SNATCH_USER_TRYST = "snatch:user:%s"; //tryst 订单号
    private final String NEARBY_ID_TRYST="nearby:%s";//用户id
    private final String NEARBY_ID_BODY_TRYST="nearby:%s:body:%s";//用户id--约会id
    private final Double fir = 0.5;     //首次取消订单
    private final Double other = 0.15;


    /*
        创建订单
     */
    @Override
    public Response createTrystOrders(Integer id, TrystOrdersRequest trystOrdersRequest) throws Exception{
        if(id == null || SysUtils.checkObjFieldIsNull(trystOrdersRequest)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //当前时间 大于约会时间
        if (TimeUtil.compareTime(TimeUtil.currentTime(),trystOrdersRequest.getTime())>=0){
                logger.warn("当前时间大于约会时间currentTime={}",TimeUtil.currentTime());
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"当前时间大于约会时间");
        }
        String startTime = trystOrdersRequest.getTime();
        String mm = String.valueOf(trystOrdersRequest.getDuration()).split("\\.")[0];
        String dd = String.valueOf(trystOrdersRequest.getDuration()).split("\\.")[1];
        String endTime = TimeUtil.afterTime(trystOrdersRequest.getTime(),Integer.parseInt(mm),Integer.parseInt(dd));
        //今日订单中已存在该时间段订单
        //查询我的所有未完成的订单
        List<TrystOrders> list = trystOrdersDao.findTrystOrdersListById(id);
        Integer exit = 0;
        for (TrystOrders trystOrders : list){
            String start = trystOrders.getTime();
            String m = String.valueOf(trystOrders.getDuration()).split("\\.")[0];
            String d = String.valueOf(trystOrders.getDuration()).split("\\.")[1];
            String end = TimeUtil.afterTime(trystOrders.getTime(),Integer.parseInt(m),Integer.parseInt(d));
            // 订单开始 时间大于开始并小于结束
            if (TimeUtil.compareTime(startTime,start)>=0&&TimeUtil.compareTime(startTime,end)<=0){
                logger.warn("当前订单开始时间在 已有订单的 时间段内  uuid = {}", trystOrders.getUuid());
                exit++;
                break;
            }
            //订单结束时间 大于开始小于结束
            if (TimeUtil.compareTime(endTime,start)>=0&&TimeUtil.compareTime(endTime,end)<=0){
                logger.warn("当前订单结束时间在 已有订单的 时间段内  uuid = {}", trystOrders.getUuid());
                exit++;
                break;
            }
        }
        if (exit>0){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该时间段已存在订单");
        }
        String today = TimeUtil.today();
        //查找今日 取消订单列表
        TrystCancel trystCancel = trystOrdersDao.findCancelTryst(id,CancelEnum.TRYST.getCode(),today);
        if(trystCancel != null){
            if (trystCancel.getTime() == 3 && TimeUtil.compareTime(trystCancel.getDate(),TimeUtil.currentTime()) > 0){
                logger.warn("今日取消订单次数已达三次");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"今日取消订单次数已达三次");
            }
        }
        String uuid = UserUtil.generateUuid();
        //创建 约会订单
        trystOrdersDao.createTrystOrders(id,uuid,trystOrdersRequest,PayTypeEnum.PARTY_WAIT_CONFIRM.toCode(),PayTypeEnum.PARTY_WAIT_CONFIRM.getValue());
        //经匹配的订单人存入redis
        /*TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        nearbyUtil.findTodayTotalNearBodies(trystOrders,trystOrdersRequest.getSex());*/
        Map<String, Object> map = new HashMap<>();
        //map.put("trystId",OrdersTypeEnum.TRYST_ADVANCE_MONEY.getQuote() + uuid);
        map.put("trystId", uuid);
        map.put("money",trystOrdersRequest.getAdvanceMoney());
        map.put("bugInfo",trystOrdersRequest.getScene());
        return Response.success(map);
    }


    /*
        显示可抢单列表
     */
    @Override
    public Response snatchableListAuth(Integer id, Integer pageNum, Double longitude, Double latitude){
        //不显示人数已满足的。。不显示已抢的
        //是否仅显示 推送
        if (null == id || null == pageNum){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = trystOrdersDao.findUserById(id);
//        List<String> list = orderHashRedisTemplate.values(String.format(NEARBY_ID_TRYST,id));
        //根据性别要求 查询 等待抢单的订单
        PageHelper.startPage(pageNum,10);
        List<TrystOrders> trystOrdersList = trystOrdersDao.findTrystOrdersList(user.getGender(),PayTypeEnum.PARTY_WAIT_CONFIRM.toCode());   //按最新发布的排序
        List<Map<String, Object>> res = new ArrayList<>();
        for (TrystOrders trystOrder : trystOrdersList){
            //判断是否已抢单
            if (snatchUserTemplate.get(String.format(SNATCH_USER_TRYST,trystOrder.getUuid()),String.valueOf(id)) != null) continue;
            //判断是否是自己的单
            if (trystOrder.getUserId().equals(id)) continue;

            Map<String, Object> map = new HashMap<>();
            User demander = trystOrdersDao.findUserById(trystOrder.getUserId());
            map.put("uuid",trystOrder.getUuid());
            map.put("demanderName",demander.getNickname());
            map.put("demanderHeadUrl",demander.getIcon());
            map.put("demanderSex",demander.getGender());
            map.put("scenePic",sceneDao.selectScenePicByScene(trystOrder.getScene()));
            map.put("sceneName",trystOrder.getScene());
            map.put("distance",getDistance(latitude,longitude,trystOrder.getLatitude(),trystOrder.getLongitude()));
            map.put("sceneContent",sceneDao.selectSceneContentByScene(trystOrder.getScene()));
            map.put("time",trystOrder.getTime() + "开始  " + trystOrder.getDuration() + "小时");
            map.put("place",trystOrder.getPlace());
            map.put("personSex",trystOrder.getSex());
            map.put("personCount",trystOrder.getPersonCount());
            map.put("reward",trystOrder.getMoney());
            res.add(map);
        }
        return Response.success(res);
    }


    /*
        服务方抢单
    */
    @Override
    public Response snatchOrder(Integer id, String uuid){
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //判断该订单是否有效
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders.getPayCode() != PayTypeEnum.PARTY_WAIT_CONFIRM.toCode()){
            return Response.error("该订单已失效");
        }
        //服务方用户
        User user = trystOrdersDao.findUserById(id);
        //添加到 该订单 抢单列表中
        SnatchUser u = snatchUserTemplate.get(String.format(SNATCH_USER_TRYST,uuid),String.valueOf(id));
        if (u != null){
            logger.warn("已抢订单");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已抢订单");
        }
        //添加到抢单列表
        SnatchUser snatchUser = new SnatchUser();
        snatchUser.setUuid(user.getUuid());
        snatchUser.setHeadUrl(user.getIcon());
        snatchUser.setNickName(user.getNickname());
        snatchUser.setGender(user.getGender());
        snatchUser.setAge(user.getAge());
        snatchUser.setZodiac(user.getZodiac());
        snatchUser.setHeight(user.getHeight());
        snatchUser.setWeight(user.getWeight());
        snatchUser.setSignature(user.getSignature());
        snatchUser.setVideos(userDao.findVideos(user.getId()));
        //功能未完成
        snatchUser.setLocation("杭州");
        snatchUser.setComment("暂无评论");
        snatchUserTemplate.put(String.format(SNATCH_USER_TRYST,uuid),String.valueOf(id),snatchUser);
        return Response.success();
    }


    /*
        需求方显示已抢单人员列表
     */
    @Override
    public Response snatchUserList(Integer id, String uuid) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<SnatchUser> list = snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid));
        return Response.success(list);
    }


    /*
        选择用户，确认订单
     */
    @Override
    public Response sureUser(Integer id, String uuid, String userIds) {
        if (null == id|| StringUtils.isEmpty(uuid) || StringUtils.isEmpty(userIds)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String[] ids =  userIds.split(",");
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        for (String id1 : ids) {
            //添加到 约吧订单 加入订单
            //即认证用户添加到与约吧订单相关联表中
            Integer userId = userDao.findUserByUserId(id).getId();
            String uid = UserUtil.generateUuid();
            //新增反馈表数据      --       tryst_receive           uid对于表uuid  uuid对应表trystId  userId对应表userId(user表的Id主键）
            trystOrdersDao.addTrystOrdersReceive(uid, uuid, userId, trystOrders.getMoney());
            //修改人数  --      tryst_orders
            trystOrdersDao.upTrystPersonCount(uuid, ids.length);
        }
        removeRedisTemplate.delete(String.format(SNATCH_USER_TRYST,uuid));
        Map<String, Object> res = new HashMap<>();
        res.put("money",trystOrders.getMoney() * ids.length);
        res.put("bugInfo",trystOrders.getScene());
        res.put("payId",OrdersTypeEnum.TRYST_ORDERS.getQuote() + trystOrders.getUuid());
        return Response.success(res);
    }


    /*
        聊天室订单简要
     */
    @Override
    public Response roomTrystAuth(Integer id, String uuid){
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders == null){
            return Response.error("未找到订单");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("trystId",trystOrders.getUuid());
        res.put("scene",trystOrders.getScene());
        res.put("place",trystOrders.getPlace());
        res.put("time",trystOrders.getTime() + "开始  " + trystOrders.getDuration() + "小时");
        res.put("money",trystOrders.getMoney());
        return Response.success(res);
    }


    /*
        通过订单id拿到订单详情
     */
    @Override
    public Response findTrystOrders(Integer id, String uuid) throws Exception {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders == null){
            return Response.error("未找到订单");
        }
        TrystSceneExample example = new TrystSceneExample();
        TrystSceneExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(trystOrders.getScene());
        TrystScene trystScene = trystSceneMapper.selectByExample(example).get(0);
        User user = userDao.findUserByUserId(trystOrders.getUserId());
        Map<String, Object> res = new HashMap<>();
        res.put("trystId",trystOrders.getUuid());
        if (trystOrders.getUserId().equals(id)){
            res.put("role",TrystRole.TRYST_ROLE_DEMAND.getValue());
        }else {
            res.put("role",TrystRole.TRYST_ROLE_SERVICE.getValue());
        }
        res.put("scenePic",trystScene.getPic());
        res.put("sceneName",trystScene.getName());
        res.put("state",trystOrders.getPayValue());
        res.put("headUrl",user.getIcon());
        res.put("nickName",user.getNickname());
        res.put("userSex",user.getGender());
        res.put("age",user.getAge());
        res.put("place",trystOrders.getPlace());
        res.put("time",trystOrders.getTime() + "开始  " + trystOrders.getDuration() + "小时");
        res.put("personCount",trystOrders.getPersonCount());
        res.put("personSex",trystOrders.getSex());
        res.put("money",trystOrders.getMoney());
        return Response.success(res);
    }


    /*
        遍历出待消费后的订单
     */
    @Override
    public Response listTrystAuth(Integer id, Integer pageNum){
        if (null == id || pageNum == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<TrystOrders> trystOrdersList = trystOrdersDao.selectTrystOrderByReceiveUserId(id,pageNum);
        if (trystOrdersList == null){
            return Response.success("暂无相关订单");
        }
        List<Map<String, Object>> res = new ArrayList<>();
        String today = TimeUtil.today();
        for (TrystOrders trystOrders : trystOrdersList){
            Map<String, Object> map = new HashMap<>(16);
            map.put("uuid",trystOrders.getUuid());
            map.put("scene",trystOrders.getScene());
            map.put("state",trystOrders.getPayValue());
            map.put("place",trystOrders.getPlace());
            map.put("money",trystOrders.getMoney());
            if (trystOrders.getPayCode() == PayTypeEnum.STORE_WAIT_CONFIRM.toCode()){
                map.put("time",trystOrders.getTime() + "开始，" + "持续" + trystOrders.getDuration() + "小时");
            }else{
                map.put("time","有效期至 " + trystOrders.getTime().substring(0,10));
            }
            if (trystOrders.getTime().substring(0,10).equals(today)){
                map.put("color","red");
            }else {
                map.put("color","normal");
            }
            res.add(map);
        }
        return Response.success(res);
    }

    /*
        需求方取消订单
     */
    @Override
    public Response cancelTrystAuth(Integer id, String uuid, Integer passive, String reason) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uid = UserUtil.generateUuid();
        String date = TimeUtil.afterTime(TimeUtil.currentTime(),2,0);
        String today = TimeUtil.today();
        String afterTime = TimeUtil.afterTime(TimeUtil.currentTime(),1,0);
        TrystCancel trystCancel = trystOrdersDao.findCancelTryst(id,CancelEnum.TRYST.getCode(),today);
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);


        //长时间没有用户抢单     ||      未付款
        if (passive == 1 || trystOrders.getPayCode().equals(PayTypeEnum.PARTY_WAIT_CONFIRM.toCode())){
            if (trystCancel != null){
                trystOrdersDao.addTimeByCancelTryst(trystCancel.getUuid(),uuid,reason);
            }else {
                //取消订单列表 --  create t_tryst_cancel              id 需求方userId         uuid 订单号
                trystOrdersDao.cancelTryst(uid, id, uuid, CancelEnum.TRYST.getCode(), CancelEnum.CHOOSE.getCode(), CancelEnum.CHOOSE.getValue(), today, date, 1,reason);
                //返还预付金额
                //trystOrdersDao.upMoney(id,trystOrders.getAdvanceMoney());
            }
        //已付款
        }else if (trystOrders.getPayCode().equals(PayTypeEnum.STORE_WAIT_CONFIRM.toCode())){
            //当前时间一个小时候大于订单时间 且 当前时间小于订单时间
            if(TimeUtil.compareTime(afterTime,trystOrders.getTime())>0&&TimeUtil.compareTime(trystOrders.getTime(),TimeUtil.currentTime())>0){
                logger.warn("订单开始前一个小时不可取消订单");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单开始前一个小时不可取消订单");
            }
            //判断是否为首次取消订单
            TrystCancel trystCancelFir = trystOrdersDao.findCancelTrystFir(id,CancelEnum.TRYST.getCode(),CancelEnum.DOWN.getCode());
            //若不是首次取消订单
            Double delMoney;
            if (trystCancelFir!=null){
                delMoney=other;
            }else {
                delMoney=fir;
            }
            //每人应付金额*人数
            Double countMoney = Arith.multiplys(2,trystOrders.getMoney(),trystOrders.getPersonCount());
            //需扣除总金额
            Double countPay = Arith.multiplys(2,countMoney,delMoney);
            //返还金额
            Double countBack = Arith.divides(2,countMoney,countPay);
            //每人 获取金额
            Double countGet = Arith.multiplys(2,trystOrders.getMoney(),delMoney);
            //发单用户返还金额
            trystOrdersDao.upMoney(id,countBack);
            //所有接单用户
            List<TrystReceive> trystReceiveList = trystOrdersDao.findTrystReceive(uuid);
            for (TrystReceive trystReceive:trystReceiveList){
                trystOrdersDao.upMoney(trystReceive.getUserId(),countGet);
            }
            //取消订单列表
            trystOrdersDao.cancelTryst(uid,id,uuid,CancelEnum.TRYST.getCode(),CancelEnum.DOWN.getCode(),CancelEnum.DOWN.getValue(),today,date,1,reason);
        }
        //根据订单id（uuid）清除redis中已抢单人员
        removeRedisTemplate.delete(String.format(SNATCH_USER_TRYST,uuid));
        //取消订单
        if (trystOrdersDao.calcelTrystOrders(uuid,PayTypeEnum.STR_CANCEL.toCode(),PayTypeEnum.STR_CANCEL.getValue()) == 0){
            return Response.error("将订单状态修改为”已取消“失败");
        }
        return Response.success("订单已取消");
    }


    /*
        赴约用户 取消订单
     */
    @Override
    public Response cancelTrystUser(Integer id, String uuid, Integer payed, String reason) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (payed == 0){
            snatchUserTemplate.delete(String.format(SNATCH_USER_TRYST,uuid),id);
        }else if (payed == 1){
            //删除反馈表
            trystOrdersDao.delectReceiveByUserId(id,uuid);
            //修改订单表的 person_count - 1
            trystOrdersDao.reduceTrystPersonCount(uuid);
            //TODO  退回一份tryst money给发单方
        }
        return Response.success("取消成功");
    }

    /**
     * 可抢单列表
     * @param attribute
     * @return
     */
    @Override
    public Response grabOrders(Integer attribute, Double longitude, Double latitude) {
        if (attribute == null || longitude == null || latitude == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"参数为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("payCode", PayTypeEnum.PARTY_WAIT_CONFIRM.toCode());
        //按距离由近到远排序
        List<DistancePo> distancePos = trystOrdersDao.selectDistanceAndIdByAsc(params);
        List<Map> res = new ArrayList<>();
        for (DistancePo distancePo : distancePos){
            Map<String, Object> map = new HashMap<>();
            TrystOrders trystOrders = new TrystOrders();
            trystOrders.setId(distancePo.getId());
            map.put("trystOrders",trystOrdersDao.selectTrystOrders(trystOrders));
            map.put("distance",distancePo.getDistance());
            res.add(map);
        }
        return Response.success(res);
    }

    @Override
    public Response choiceSnatchUser(Integer id, String uuid) throws Exception {
        if (null==id|| StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<SnatchUser> listUser =  snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid));
//        List<User> list =listUser.subList(0,2);
        return Response.success(listUser);
    }



    @Override
    public Response noticeSize(Integer id, String uuid) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<NearBodyResponse[]> nearbyBodyList = nearBodyTemplate.values(String.format(NEARBY_ID_BODY_TRYST,String.valueOf(uuid)));
        List<Coupons> coupons = trystOrdersDao.findMyCoupons(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size",nearbyBodyList.size());
        jsonObject.put("coupons",coupons);
        return Response.success(nearbyBodyList.size());
    }

    @Override
    public Response myTrystOrders(Integer attribute, Integer pageNum) {
        return null;
    }

    private static double getDistance(double lat1, double lng1, double lat2, double lng2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        double EARTH_RADIUS = 6371.393;
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
