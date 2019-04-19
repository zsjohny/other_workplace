package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.beust.jcommander.internal.Lists;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.entity.task.DestoryLive;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.LiveUtils;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.commons.utils.MapTrunPojo;
import com.e_commerce.miscroservice.product.dao.LiveDao;
import com.e_commerce.miscroservice.product.dao.LiveProductDao;
import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.mapper.LiveMapper;
import com.e_commerce.miscroservice.product.rpc.TaskRpcService;
import com.e_commerce.miscroservice.product.service.LiveProductService;
import com.e_commerce.miscroservice.product.service.LiveService;
import com.e_commerce.miscroservice.product.vo.LiveProductVO;
import com.e_commerce.miscroservice.product.vo.LiveUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.weekend.Fn;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.*;

/**
 * @Author hyf
 * @Date 2019/1/16 14:14
 */
@Service
public class LiveServiceImpl implements LiveService {

    private Log logger = Log.getInstance(LiveServiceImpl.class);


    //用户人数
    private static String LIVE_MEMBER_LIST = "live:member:list:%s";

    @Resource
    private LiveDao liveDao;
    @Autowired
    private LiveMapper liveMapper;
    @Autowired
    @Qualifier( "userHashRedisTemplate" )
    private HashOperations<String, String, String> userHashRedisTemplate;
    @Autowired
    @Qualifier( "strRedisTemplate" )
    private RedisTemplate<String, String> strRedisTemplate;
    @Autowired
    private LiveProductService liveProductService;
    @Autowired
    private LiveProductDao liveProductDao;
    @Autowired
    private TaskRpcService taskRpcService;

    @Value("${live.push.domain}")
    private String pushDomain;

    @Value("${live.play.domain}")
    private String playDomain;
    @Value("${live.bizid}")
    private String liveBizid;
    @Value("${live.key}")
    private String liveKey;
    @Override
    public Response openLive(String title, Long userId, Long storeId) {
        logger.info("开启直播间title={}，userId={}，storeId={}", title, userId, storeId);
        LiveUser liveUser = liveDao.findAnchorByUserId(userId);
        if (liveUser == null) {
            logger.warn("直播间未开启={}", userId);
            return Response.errorMsg("直播间未开启");
        }
        Long productSize = liveProductDao.countAnchorProduct(userId);

        if (productSize<=0){
            logger.warn("直播商品未设置userId={},storeId={}", userId,storeId);
            return Response.errorMsg("直播商品未设置");

        }
        Long roomId = liveUser.getRoomNum();
        String mainMap = liveUser.getIcon();
        String nickName = liveUser.getNickName();
//        Long roomId = 1000L;
        String streamId = LiveUtils.streamId(8);
        String pushUrl = LiveUtils.getPushUrl(pushDomain,liveBizid,liveKey,streamId,System.currentTimeMillis()/1000+3600*24);
        String playUrl = pushUrl.split("\\?")[0].replace(pushDomain, playDomain);
        Integer liveStatus = null;
        if (liveUser.getLiveType()==0||liveUser.getLiveType()==3){
            liveStatus = LIVE_COMMON_SHOP.getKey();
        }else if(liveUser.getLiveType()==1){
            liveStatus = LIVE_SPECIAL_PLATFORM.getKey();
        }else if(liveUser.getLiveType()==2){
            liveStatus = LIVE_SPECIAL_DISTRIBUTOR.getKey();
        }else {
            logger.warn("主播类型错误");
            return Response.errorMsg("主播类型错误");
        }
        DataBase dataBase = packageDataBase(nickName, mainMap, title, LIVE_SHOP_CREATE.getKey(), userId, storeId, roomId, MODEL_TYPE_LIVE.getKey(), liveStatus,playUrl);
        taskRpcService.disposeDistribute(dataBase);

        LiveUser user = new LiveUser();
        user.setId(liveUser.getId());
        user.setTitle(title);
        liveDao.upLiveUser(user);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", roomId);
        jsonObject.put("pushUrl", pushUrl);
        jsonObject.put("nickName", liveUser.getNickName());
        jsonObject.put("icon", liveUser.getIcon());
        jsonObject.put("liveType", liveStatus);
        return Response.success(jsonObject);
    }

    /**
     * 查找直播间观看人数以及缩略图
     *
     * @param userId
     * @param storeId
     * @param liveType
     * @param roomId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public Response findAudience(Long userId, Long storeId, Integer liveType, Long roomId, Long pageNumber, Long pageSize) {
        logger.info("进入直播间查看直播间人数 userId={}，storeId={}，liveType={}，roomId={}，pageNumber={}，pageSize={}", userId, storeId, liveType, roomId, pageNumber, pageSize);
        if (! TaskTypeEnums.inLiveType(liveType)) {
            logger.warn("直播间类型错误={}", liveType);
            return Response.errorMsg("直播间类型错误");
        }
        DataBase dataBase = packageDataBaseFindAudience(LIVE_ROOM_MEMBER_LIST.getKey(), userId, storeId, roomId, MODEL_TYPE_LIVE.getKey(), liveType, pageNumber, pageSize);
        taskRpcService.disposeDistribute(dataBase);
        return Response.success();
    }


    /**
     * 直播列表
     *
     * @param vo vo
     * @return map
     * @author Charlie
     * @date 2019/1/17 16:17
     */
    @Override
    public MapHelper onPlayRoomList(LiveUserVO vo) {
        Long storeId = vo.getStoreId();
        Integer platformType = vo.getPlatformType();
        //是否开始了平台直播
        List<LiveUserDTO> bossLiveUsers = liveDao.findLiveUserByStoreAndType(storeId, 0);
        if (bossLiveUsers.isEmpty()) {
            return MapHelper.me(4)
                    .put("bossIsOpenPlatformLive", false)
                    .put("dataList", Collections.EMPTY_LIST)
                    .put("totalRoomCount", 0);
        }
        LiveUserDTO boss = bossLiveUsers.get(0);
        //店老板有没有开通直播间
        boolean bossIsOpenPlatformLive = boss.getOpenOfficial().equals(1);
        logger.info("查询直播房间列表 storeId={},platfromType={},boss={}", storeId, platformType, boss);
        //查询热播房间号
        List<LiveData> roomList;
        switch (platformType) {
            case 0: {
                //0自有直播
                ErrorHelper.declare(! ObjectUtils.isEmpty(bossLiveUsers), "没找到店家直播设置");
                roomList = findRoomList(storeId, LIVE_COMMON_SHOP, vo.getPageNumber(), vo.getPageSize());
                break;
            }
            case 1: {
                //1平台直播
                ErrorHelper.declare(bossIsOpenPlatformLive, "店家没有开通直播间");
                roomList = findRoomList(storeId, LIVE_SPECIAL_PLATFORM, vo.getPageNumber(), vo.getPageSize());
                break;
            }
            default:
                throw ErrorHelper.me("未知的直播平台类型");
        }

        //直播间的主播
        List<Long> roomNumList = roomList.stream().mapToLong(lp -> lp.getRoomId()).boxed().collect(Collectors.toList());
        List<LiveUser> liveUserList = findLiveUserByRoomNums(roomNumList);

        /* 查询商品图片 */
        LiveProductVO query = new LiveProductVO();
        query.setRoomNumList(roomNumList);
        query.setLiveStatus(0);
        List<LiveProductVO> liveProductVOS = liveProductService.doQueryLiveProductListByRoomNumList(query, false);
        //根据主播id进行商品分组
        Map<Long, List<LiveProductVO>> productGroupByAnchor = liveProductVOS.stream()
                .collect(Collectors.groupingBy(LiveProduct::getAnchorId));

        /* 查询主播商品个数 */
        List<Long> anchorIdList = liveUserList.stream().mapToLong(LiveUser::getId).boxed().collect(Collectors.toList());
        List<Map<String, Long>> anchorProductCountList = liveProductDao.countAnchorProduct(anchorIdList);
        Map<Long, Long> anchorCountMap = new HashMap<>(anchorProductCountList.size());
        anchorProductCountList.forEach(pCount -> anchorCountMap.put(pCount.get("anchorId"), pCount.get("pCount")));

        //组装
        Map<Long, List<LiveData>> roomOfNum = roomList.stream().collect(Collectors.groupingBy((Fn<LiveData, Long>) liveData -> liveData.getRoomId()));
        Map<Long, List<LiveData>> finalRoomOfNum = roomOfNum == null ? Collections.emptyMap() : roomOfNum;
        List<LiveUserVO> result = Lists.newArrayList(liveUserList.size());
        liveUserList.stream().forEach(anchor -> {
            LiveUserVO retVO = LiveUserVO.me();
            Long anchorId = anchor.getId();
            Long roomNum = anchor.getRoomNum();
            //在线人数
            Long onlineUserCount = findOnlineUserCount(roomNum);
            retVO.setOnlineUserCount(onlineUserCount);
            //主播信息
            retVO.setAnchorId(anchorId);
            retVO.setIcon(anchor.getIcon());
            retVO.setNickName(anchor.getNickName());
            retVO.setRoomNum(roomNum);
            List<LiveData> lds = finalRoomOfNum.get(roomNum);
            String payUrl = "";
            String roomName = "";
            if (! ObjectUtils.isEmpty(lds)) {
                LiveData ld = lds.get(0);
                if (ld != null) {
                    payUrl = ld.getPlayUrl();
                    roomName = ld.getTitle();
                }
                else {
                    logger.warn("没有直播间信息 roomNum={}", roomNum);
                }
            }
            retVO.setPayUrl(payUrl);
            retVO.setLiveRoomName(roomName);

            //最多3个商品图片
            int maxImgCount = 3;
            List<String> newProductList = Lists.newArrayList(maxImgCount);
            List<LiveProductVO> imgList = productGroupByAnchor.getOrDefault(anchorId, Collections.emptyList());
            for (int i = 0; i < imgList.size() && i < maxImgCount; i++) {
                LiveProductVO liveProductVO = imgList.get(i);
                String summaryImgJsonArr = liveProductVO.getSummaryImgJsonArr();
                JSONArray imgJsonArr = JSONObject.parseArray(summaryImgJsonArr);
                if (imgJsonArr.isEmpty()) {
                    //没有就取下一张
                    maxImgCount++;
                } else {
                    //有就取第一张
                    newProductList.add(String.valueOf(imgJsonArr.get(0)));
                }
            }
            retVO.setImgList(newProductList);

            //商品个数
            Long liveProductCount = anchorCountMap.get(anchorId);
            retVO.setLiveProductCount(liveProductCount == null ? 0L : liveProductCount);

            result.add(retVO);
        });
        return MapHelper.me(4)
                .put("bossIsOpenPlatformLive", bossIsOpenPlatformLive)
                .put("dataList", result)
                .put("totalRoomCount", roomNumList.size());
    }

    @Override
    public Response findLiveDataTag(Long userId, Long roomId) {
        logger.info("查询直播间观看人数userId={},roomId={}", userId,roomId);
//        Long size =  strRedisTemplate.opsForZSet().size(String.format(LIVE_MEMBER_LIST, roomId));
        Long size =  userHashRedisTemplate.size(String.format(LIVE_MEMBER_LIST, roomId));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("online", size);
        return Response.success(jsonObject);
    }

    @Override
    public Response destroyLive(Long userId, Long storeId, Integer liveType, Long roomId)
    {
        logger.info("销毁直播间userId={}，storeId={}，liveType={}，roomId={}",userId,storeId,liveType,roomId);
        DataBase dataBase = new DataBase();
        dataBase.setCode(TaskTypeEnums.LIVE_SHOP_DELETE.getKey());
        dataBase.setRoomId(roomId);
        dataBase.setModuleType(MODEL_TYPE_LIVE.getKey());
        dataBase.setStoreId(storeId);
        LiveData liveData = new LiveData();
        liveData.setCode(liveType);
        dataBase.setObj(liveData);
        Response response = taskRpcService.disposeDistribute(dataBase);
        Object obj = ((Response.ResponseData) response).getData();
        if (((Response.ResponseData) response).getCode()==200){
            DestoryLive map = MapTrunPojo.jsonParseObj(JSONObject.toJSONString(obj),DestoryLive.class);
            List<LiveProduct> list = liveProductDao.findByRoomId(roomId);
            List<Long> productIds = new ArrayList<>();
            list.forEach(liveProduct -> {
                productIds.add(liveProduct.getId());
            });
            logger.info("销毁直播间返回信息={}", map);
            Integer countProduct = 0;
            if (list!=null&&list.size()>0){
                countProduct = liveProductDao.findOrderCountByProductIds(productIds,map.getCreate(),map.getCurrent());
            }
            map.setOrderSize(countProduct);
            return  Response.success(map);
        }
        return response;

    }

    @Override
    public Response shareLive(Long storeId, Long roomId, Integer liveType)
    {
        DataBase dataBase = new DataBase();
        dataBase.setStoreId(storeId);
        dataBase.setRoomId(roomId);
        dataBase.setModuleType(MODEL_TYPE_LIVE.getKey());
        dataBase.setCode(LIVE_SHOP_READ.getKey());
        LiveData liveData = new LiveData();
        liveData.setCode(liveType);
        liveData.setRoomId(roomId);
        liveData.setStoreId(storeId);
        dataBase.setObj(liveData);

        return taskRpcService.disposeDistribute(dataBase);
    }

    @Override
    public Response showAnchorInformation(Long roomId) {
        List<LiveUser> liveUsers = findLiveUserByRoomNums(Arrays.asList(roomId));
        if (liveUsers!=null&&liveUsers.size()>0){
            return Response.success(liveUsers.get(0));
        }
        return Response.errorMsg("查询主播信息失败");
    }

    @Override
    public Response intoRoom(Long storeId, Long roomId, Long userId) {
        logger.info("进入直播间={},roomId={}",storeId, roomId);
        DataBase dataBase = new DataBase();
        dataBase.setRoomId(roomId);
        dataBase.setModuleType(MODEL_TYPE_LIVE.getKey());
        dataBase.setStoreId(storeId);
        dataBase.setMemberId(userId);
        dataBase.setCode(TaskTypeEnums.LIVE_ROOM_MEMBER_INTO.getKey());
        LiveData liveData = new LiveData();
        liveData.setRoomId(roomId);
        liveData.setStoreId(storeId);
        liveData.setCode(TaskTypeEnums.LIVE_ROOM_MEMBER_INTO.getKey());
        dataBase.setObj(liveData);

        return taskRpcService.disposeDistribute(dataBase);
    }

    public Long findOnlineUserCount(Long roomNum) {
        return userHashRedisTemplate.size(String.format(LIVE_MEMBER_LIST, roomNum));
    }

    /**
     * 根据房间号查主播
     *
     * @param roomNumList roomNumList
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.LiveUser>
     * @author Charlie
     * @date 2019/1/17 17:45
     */
    private List<LiveUser> findLiveUserByRoomNums(List<Long> roomNumList) {
        if (ObjectUtils.isEmpty(roomNumList)) {
            return Collections.emptyList();
        }
        return liveMapper.findLiveUserByRoomNums(roomNumList);
    }


    /**
     * 查找直播间列表
     *
     * @param storeId storeId
     * @param type type
     * @param pageNumber pageSize
     * @param pageSize pageNumber
     * @return java.util.List<java.lang.Long>
     * @author Charlie
     * @date 2019/1/17 17:15
     */
    private List<LiveData> findRoomList(Long storeId, TaskTypeEnums type, Integer pageNumber, Integer pageSize) {
        DataBase dataBase = DataBase.me();
        dataBase.setStoreId(storeId);
        dataBase.setCode(LIVE_READ_ALL_SHOP.getKey());
        dataBase.setModuleType(MODEL_TYPE_LIVE.getKey());
        LiveData liveData = new LiveData();
        liveData.setCode(type.getKey());
        dataBase.setObj(liveData);
        Response.ResponseData res = taskRpcService.disposeDistribute(dataBase);
        List<LiveData> allRoomNumList;
        if (res.getCode() == 200) {
            List<String> roomJsonList = (List<String>) res.getData();
            if (! ObjectUtils.isEmpty(roomJsonList)) {
                allRoomNumList = Lists.newArrayList(roomJsonList.size());
                roomJsonList.stream().forEach(jsonStr -> {
                    LiveData ld = JSONObject.parseObject(jsonStr, new TypeReference<LiveData>() {});
                    if (ld != null) {
                        allRoomNumList.add(ld);
                    }
                });
            } else {
                allRoomNumList = Collections.emptyList();
                logger.info("没有热播房间");
            }
        } else {
            allRoomNumList = Collections.emptyList();
            logger.warn("向调度中心请求直播房间列表失败 msg={}", res.getMsg());
        }

        //分页
        pageNumber = pageNumber <= 1 ? 1 : pageNumber;
        int offset = (pageNumber - 1) * pageSize;
        int limit = pageSize * pageNumber;
        int roomSize = allRoomNumList.size();
        limit = limit > roomSize ? roomSize : limit;

        List<LiveData> roomNumAfterPage = Lists.newArrayList(limit);
        for (int i = offset; i < limit; i++) {
            roomNumAfterPage.add(allRoomNumList.get(i));
        }
        return roomNumAfterPage;
    }

    /**
     * 组装分发中心数据
     *
     * @param nickName
     * @param mainMap 主图
     * @param title 标题
     * @param code 操作类型
     * @param userId 用户id
     * @param storeId 店铺id
     * @param roomId 房间号
     * @param moduleType 模块类型
     * @param liveStatus
     * @return
     */
    private DataBase packageDataBase(String nickName, String mainMap, String title, Integer code, Long userId, Long storeId, Long roomId, Integer moduleType, Integer liveStatus, String playUrl) {
        DataBase dataBase = new DataBase();
        dataBase.setId(userId);
        dataBase.setStoreId(storeId);
        dataBase.setRoomId(roomId);
        dataBase.setModuleType(moduleType);
        dataBase.setLiveStatus(liveStatus);
        dataBase.setCode(code);
        LiveData liveData = new LiveData();
        liveData.setCreateTime(System.currentTimeMillis());
        liveData.setCode(liveStatus);
        liveData.setStoreId(storeId);
        liveData.setRoomId(roomId);
        liveData.setTitle(title);
        liveData.setNickName(nickName);
        liveData.setMainMap(mainMap);
        liveData.setPlayUrl(playUrl);
        dataBase.setObj(liveData);
        return dataBase;
    }

    /**
     * 组装分发中心数据
     *
     * @param code 操作类型
     * @param userId 用户id
     * @param storeId 店铺id
     * @param roomId 房间号
     * @param moduleType 模块类型
     * @param liveStatus
     * @param pageNumber
     * @param pageSize
     * @return
     */
    private DataBase packageDataBaseFindAudience(Integer code, Long userId, Long storeId, Long roomId, Integer moduleType, Integer liveStatus, Long pageNumber, Long pageSize) {
        DataBase dataBase = new DataBase();
        dataBase.setId(userId);
        dataBase.setStoreId(storeId);
        dataBase.setRoomId(roomId);
        dataBase.setModuleType(moduleType);
        dataBase.setLiveStatus(liveStatus);
        dataBase.setCode(code);
        dataBase.setPageNumber(pageNumber);
        dataBase.setPageSize(pageSize);
        LiveData liveData = new LiveData();
        liveData.setCreateTime(System.currentTimeMillis());
        liveData.setCode(liveStatus);
        liveData.setStoreId(storeId);
        liveData.setRoomId(roomId);
        dataBase.setObj(liveData);
        return dataBase;
    }



}
