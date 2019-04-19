package com.e_commerce.miscroservice.activity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.activity.dao.ChannelUserGatherDao;
import com.e_commerce.miscroservice.activity.entity.channel.*;
import com.e_commerce.miscroservice.activity.entityvo.ChannelRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelResponse;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserRequest;
import com.e_commerce.miscroservice.activity.entityvo.ChannelUserResponse;
import com.e_commerce.miscroservice.activity.mapper.ChannelMapper;
import com.e_commerce.miscroservice.activity.service.ChannelService;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChannelServiceImp implements ChannelService {
    @Value("page.url")
    private String pageUrl;
    private static final String WXA_CODE_URL="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    //public static String weixinServiceUrl ="https://weixinonline.yujiejie.com";
    Log logger = Log.getInstance(ChannelServiceImp.class);
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private ChannelUserGatherDao channelUserGatherDao;
    @Value("${user.jfinal.sys.url}")
    private String weixinServiceUrl;
    @Value("${inShop.storeId}")
    private Long inShopStoreId;


    @Autowired
    private OssKit ossKit;
    @Override
    public void addChannel(String name, String phone) {
        ChannelUser channelUser=new ChannelUser();
        channelUser.setUserName(name);
        channelUser.setPhone(phone);
        ChannelUser channelUser1 = channelMapper.selectUser(phone);
        if (channelUser1!=null){
            ErrorHelper.declare(channelUser1==null, "请勿重复添加");
        }
        int i = MybatisOperaterUtil.getInstance().save(channelUser);
        ErrorHelper.declare(i==1, "添加渠道商失败");
        //
        Long channelUserId = channelUser.getId();
        ChannelUserGather gather = new ChannelUserGather();
        gather.setChannelUserId(channelUserId);
        int save = channelUserGatherDao.save(gather);
        ErrorHelper.declare(save==1, "初始化渠道商统计信息失败");
    }
    @Override
    public Response updateChannel(Long id, String name, String phone, Integer status) {
        int i = channelMapper.updateChannel(id,name,phone,status);
        if (i!=1){
            logger.info("修改渠道商信息失败"+i+"{}");
            return Response.errorMsg("修改渠道商失败");
        }
        return Response.success("修改渠道商成功");
    }
    @Override
    public Response search(ChannelRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        Map<String,Object> map=new HashMap<>();
        List<ChannelResponse> channelResponses = channelMapper.searchAll(request);
        if (channelResponses.size()<=0){
            PageInfo<ChannelResponse> poolResponsePageInfo  = new PageInfo<>(channelResponses);
            Map<String,Object> data=new HashMap<>();
            List<Map<String,Object>> statistics=new ArrayList<>();
            data.put("newUser",0);//所有渠道商下今日新增用户数量
            data.put("allOrderCount",0);//总的订单数
            data.put("newOrderCount",0);//所有渠道商下今日新增订单数量
            data.put("allUser",0);//总用户数量
            statistics.add(data);
            map.put("statistics",statistics);
            data.put("orderConversionRate","0%");//订单转化率
            map.put("dataList",poolResponsePageInfo);
        }
        Long allUser=0L;//总用户数量
        Long allOrderCount=0L;//所有渠道商的总订单数量
        Long newUser=0L;//所有渠道商下今日新增用户数量
        Long newOrderCount=0L;//所有渠道商下今日新增订单数量
        Long actualCount=0L;//下过单的用户数量
        Calendar startOfToday = TimeUtils.startOfToday();
        Calendar endOfToday = TimeUtils.endOfToday();

        //根据时间查询
        for (ChannelResponse channelResponse : channelResponses) {
            Long id = channelResponse.getId();//获取渠道商商id
            request.setChannelId(id);
            ChannelUserGather channelUserGather = channelMapper.selectMyFans(request);
            channelResponse.setFansOrderCount( channelUserGather.getFansOrderCount());//渠道商下用户下单总数量
            channelResponse.setFansCount(channelUserGather.getFansCount());//渠道商下粉丝的总数
            allUser=allUser+channelResponse.getFansCount();//总的用户数量
            allOrderCount=allOrderCount+channelResponse.getFansOrderCount();//总的订单数
            //查找渠道商下下过单的用户
            List<ChannelUserFans> channelUserFans = channelMapper.selectActualFans(request);
            for (ChannelUserFans channelUserFan : channelUserFans) {
               if (channelUserFan.getShopMemberOrderCount()>0){
                   actualCount=actualCount+1;
               }
                System.out.println(channelUserFan.getChannelUserId());
                Long shopMemberId = channelUserFan.getShopMemberId();//获取到会员id
                Long todayNewOrder = channelMapper.selectTodayOrder(TimeUtils.cal2Str(startOfToday), TimeUtils.cal2Str(endOfToday), id, shopMemberId);//在某一时间段内渠道商下用户的订单增加数量
                newOrderCount=newOrderCount+todayNewOrder;//今日新增订单数
            }
            System.out.println(id);
            Long todayNewUser = channelMapper.selectTodayUser(TimeUtils.cal2Str(startOfToday), TimeUtils.cal2Str(endOfToday), id);
            newUser=newUser+todayNewUser;//今日新增用户
            channelResponse.setNewUser(newUser);//所有渠道商下今日新增用户数量
            channelResponse.setNewOrderCount(newOrderCount);//所有渠道商下今日新增订单数量
            channelResponse.setAllUser(allUser);//总用户数量
            channelResponse.setActualCount(actualCount);//实际下过单的用户
            channelResponse.setAllOrderCount(allOrderCount);//总的订单数
        }
        Map<String,Object> data=new HashMap<>();
        if (allUser==0){
            data.put("orderConversionRate","0%");//订单转化率
        }else {
            String accuracy = accuracy(actualCount, allUser, 2);
            data.put("orderConversionRate",accuracy);//订单转化率
        }
        List<Map<String,Object>> statistics=new ArrayList<>();
        data.put("newUser",newUser);//所有渠道商下今日新增用户数量
        data.put("allOrderCount",allOrderCount);//总的订单数
        data.put("newOrderCount",newOrderCount);//所有渠道商下今日新增订单数量
        data.put("allUser",allUser);//总用户数量
        statistics.add(data);
        PageInfo<ChannelResponse> poolResponsePageInfo  = new PageInfo<>(channelResponses);
        map.put("statistics",statistics);
        map.put("dataList",poolResponsePageInfo);
        return Response.success(map);
    }

    @Override
    public Response searchUser(ChannelUserRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        Map<String,Object> map=new HashMap<>();
        String accuracy ="";
        List<ChannelUserResponse> channelUserResponses = channelMapper.selectAllUser(request);
        if (channelUserResponses.size()<=0){
            PageInfo<ChannelUserResponse> poolResponsePageInfo  = new PageInfo<>(channelUserResponses);
            logger.info("获取到的渠道商列表:"+channelUserResponses);
            Map<String,Object> data=new HashMap<>();
            List<Map<String,Object>> statistics=new ArrayList<>();
            data.put("newUser",0);//所有渠道商下今日新增用户数量
            data.put("allOrderCount",0);//总的订单数
            data.put("newOrderCount",0);//所有渠道商下今日新增订单数量
            data.put("allUser",0);//总用户数量
            statistics.add(data);
            map.put("statistics",statistics);
            map.put("dataList",poolResponsePageInfo);
            data.put("orderConversionRate","0%");//订单转化率
            return Response.success(map);
        }
        Calendar startOfToday = TimeUtils.startOfToday();
        Calendar endOfToday = TimeUtils.endOfToday();
        Long allUser=0L;//总用户数量
        Long allOrderCount=0L;//所有渠道商的总订单数量
        Long newUser=0L;//所有渠道商下今日新增用户数量
        Long newOrderCount=0L;//所有渠道商 下今日新增订单数量
        Long actualCount=0L;//下过单的用户数量
        Long shopMemberId=null;
        for (ChannelUserResponse channelUserRespons : channelUserResponses) {
            ChannelUserGather channelUserGather = channelMapper.selectMyFanNew(request);
            allOrderCount=channelUserGather.getFansOrderCount();//渠道商下用户下单总数量
            channelUserRespons.setAllUser(channelUserGather.getFansCount());//渠道商下粉丝的总
        }
        Long todayNewUser = channelMapper.selectTodayUser(TimeUtils.cal2Str(startOfToday), TimeUtils.cal2Str(endOfToday), request.getChannelId());
        newUser=newUser+todayNewUser;//今日新增用户
        List<ChannelUserFans> channelUserFans = channelMapper.selectActualFanNew(request);
        for (ChannelUserFans channelUserFan : channelUserFans) {
            shopMemberId=channelUserFan.getShopMemberId();
            Long todayNewOrder = channelMapper.selectTodayOrder(TimeUtils.cal2Str(startOfToday), TimeUtils.cal2Str(endOfToday), request.getChannelId(), shopMemberId);//在某一时间段内渠道商下用户的订单增加数量 //ChannelOrderRecord
            newOrderCount=newOrderCount+todayNewOrder;//今日新增订单数
        }
        List<ChannelUserFans> channelUserFanNew= channelMapper.selectActualFanCount(request.getChannelId(), request.getStartTime(), request.getOverTime());
        for (ChannelUserFans channelUserFan : channelUserFanNew) {
            if (channelUserFan.getShopMemberOrderCount()>0){
                actualCount=actualCount+1;
            }
        }
        allUser= Long.valueOf(channelUserResponses.size());
        Map<String,Object> data=new HashMap<>();
        PageInfo<ChannelUserResponse> poolResponsePageInfo  = new PageInfo<>(channelUserResponses);
        if (allUser==0){
            data.put("orderConversionRate","0%");//订单转化率
        }else {
            accuracy = accuracy(actualCount, allUser ,2);
            data.put("orderConversionRate",accuracy);//订单转化率
        }
        List<Map<String,Object>> statistics=new ArrayList<>();
        data.put("newUser",newUser);//所有渠道商下今日新增用户数量
        data.put("allOrderCount",allOrderCount);//总的订单数
        data.put("newOrderCount",newOrderCount);//所有渠道商下今日新增订单数量
        data.put("allUser",allUser);//总用户数量
        statistics.add(data);
        map.put("statistics",statistics);
        map.put("dataList",poolResponsePageInfo);
        return Response.success(map);
    }

    @Override
    public Response test(Long memberId, Long storeId) {
        List<Long> list=new ArrayList<>();
        list.add(284L);
        Integer status=null;
        Long toutal = channelMapper.selectAllOrderCount(list, status);
        return Response.success(toutal);
    }

    @Override
    public Response searchQrCode(Long channelId) {
        //先查询有没有
        String path="";
        path=channelMapper.selectQrCode(channelId);
        if (path==null||"".equals(path)){
            logger.info("初始化二维码");
            //path = GreateCode.geneShareQrImg();
            path= geneShareQrImg("channelShareLogin"+channelId+new SimpleDateFormat("yyyyMMddHHmmss")+".jpg",
                    inShopStoreId, channelId);
            //插入二维码
            logger.info("插入图片成功");
            int i = channelMapper.updateImg(channelId, path);
            if (i!=1){
                logger.info("插入图片失败");
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("path",path);
        return Response.success(map);
    }


    @Override
    public List<Map<String, Object>> exUser(ChannelUserRequest request) {
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ChannelUserResponse> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        List<ChannelUserResponse> channelUserResponses = channelMapper.selectAllUser(request);
        for (ChannelUserResponse channelUserRespons : channelUserResponses) {
            ChannelUserGather channelUserGather = channelMapper.selectMyFanNew(request);

            channelUserRespons.setAllOrderCount( channelUserGather.getFansOrderCount());//渠道商下用户下单总数量

            list.add(channelUserRespons);
        }
        List<Map<String, Object>> mapList = this.encapsulatedOutPendingDeliveryOrderExcel(list);

       return mapList;

    }
    private List<Map<String, Object>> encapsulatedOutPendingDeliveryOrderExcel( List<ChannelUserResponse> selectList) {
        List<Map<String, Object>> supplierOrderListPendingDelivery = new ArrayList<Map<String,Object>>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        for (ChannelUserResponse channelUserResponse : selectList) {
            Map<String,Object> supplierOrder = new HashMap<String,Object>();
            supplierOrder.put("wxName",channelUserResponse.getUserNickname());
            supplierOrder.put("wxPhone",channelUserResponse.getWxPhone());
            supplierOrder.put("allOrderCount",channelUserResponse.getAllOrderCount());
            supplierOrder.put("createTime",channelUserResponse.getCreateTime());
            if (channelUserResponse.getSex() == 1) {
                supplierOrder.put("sex","男");
            }
            if (channelUserResponse.getSex()==2){
                supplierOrder.put("sex","女");
            }
            if (channelUserResponse.getSex()==0){
                supplierOrder.put("authority","未授权");
            }else {
                supplierOrder.put("authority","已授权");
            }
            supplierOrderListPendingDelivery.add(supplierOrder);
        }
        return supplierOrderListPendingDelivery;
    }
    private String geneShareQrImg(String fileName, Long storeId, Long channelId) {
        String path="";
        String url =weixinServiceUrl+"/third/findAccessTokenByAppId";
        Map<String, String> param = new HashMap<>(6);
        List<StoreWxa> storeWxas = channelMapper.selectStoreWxaList(storeId);
        StoreWxa storeWxa = null;
        if (storeWxas.size()>0) {
            storeWxa = storeWxas.get(0);
            param.put("appId",storeWxa.getAppId());
            String token = HttpUtils.sendGet(url, param);
            logger.info("token = " + token);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("scene", "channelId="+channelId+"&type=channel");
            paramMap.put("page",pageUrl );
            paramMap.put("width", "430");
            InputStream inputStream = HttpClientUtils.postInputStream(String.format(WXA_CODE_URL, token), JSONObject.toJSONString(paramMap));
            try{
                //上传
                 path = ossKit.simpleUpload (
                         inputStream,
                         Stream.of("yjj-img-main", "customer/").collect(Collectors.toList()),
                         fileName
                 );
            }catch (Exception e){
                logger.info("上传图片失败");
            }finally {
                IOUtils.close (inputStream);
                return path;
            }
        }else {
            return path;
        }
    }
    /**
     * 获取当天时间的开始与结束时间
     */
    public String[] timeLimit(){
        Date dNow = new Date(); //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        String defaultStartDate = sdf.format(dBefore); //格式化当天
        defaultStartDate = defaultStartDate+" 00:00:00";
        String defaultEndDate = defaultStartDate.substring(0,10)+" 23:59:59";
        String[] timeStr={defaultStartDate,defaultEndDate};
        return timeStr;
    }
    /**
     * 判断是否在当天时间内
     */
    public  boolean isToday(Date inputJudgeDate) {
            boolean flag = false;
            //获取当前系统时间
            long longDate = System.currentTimeMillis();
            Date nowDate = new Date(longDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(nowDate);
            String subDate = format.substring(0, 10);
            //定义每天的24h时间范围
            String beginTime = subDate + " 00:00:00";
            String endTime = subDate + " 23:59:59";
            Date paseBeginTime = null;
            Date paseEndTime = null;
            try {
                paseBeginTime = dateFormat.parse(beginTime);
                paseEndTime = dateFormat.parse(endTime);

            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
                flag = true;
            }
            return flag;
        }

    /**
     * 订单转化率的计算
     * @return
     */

    public static String accuracy(double num, double total, int scale){
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();
        //设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式  四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num)+"%";
    }
}
