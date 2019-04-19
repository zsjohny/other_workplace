package com.e_commerce.miscroservice.product.controller.live;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.product.service.LiveService;
import com.e_commerce.miscroservice.product.vo.LiveUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hyf
 * @Date 2019/1/16 14:05
 */
@RestController
@RequestMapping("live")
public class LiveController {

    @Autowired
    private LiveService liveService;

    /**
     * 开启直播间
     * @param title 标题
     * @param userId 用户id
     * @param storeId 店铺id
     * @return
     */
    @RequestMapping("open/live")
    public Response openLive(String title,Long userId,Long storeId){
        return liveService.openLive(title,userId,storeId);
    }

    /**
     * 销毁直播间
     * @param userId 用户id
     * @param storeId 店铺id
     * @param liveType 直播间类型
     * @param roomId 房间号
     * @return
     */
    @RequestMapping("destroy/live")
    public Response destroyLive(Long userId, Long storeId, Integer liveType, Long roomId){
        return liveService.destroyLive(userId,storeId,liveType,roomId);
    }
    /**
     * 直播间观看人数以及缩略图
     * @param userId 用户id
     * @param storeId 店铺id
     * @param liveType 直播间类型
     * @param roomId 房间号
     * @param pageNumber 页码
     * @param pageSize 数量
     * @return
     */
    @RequestMapping("find/audience")
    public Response findAudience(Long userId, Long storeId, Integer liveType, Long roomId,
                                 Long pageNumber, Long pageSize){
        return liveService.findAudience(userId,storeId,liveType,roomId,pageNumber,pageSize);
    }

    /**
     * 直播间观看人数
     * @param userId 用户id
     * @param roomId 房间号
     * @return
     */
    @RequestMapping("live/tag")
    public Response findLiveDataTag(Long userId,Long roomId){
        return liveService.findLiveDataTag(userId,roomId);
    }


    /**
     * 直播列表
     *
     * @param platformType 0自有直播.1平台直播
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/17 16:17
     */
    @RequestMapping( "onPlayRoomList" )
    public Response onPlayRoomList(
            @RequestParam(value = "platformType", defaultValue = "0")Integer platformType,
            @RequestParam(value = "storeId")Long storeId,
            @RequestParam( value = "pageSize", defaultValue = "1" ) Integer pageSize,
            @RequestParam( value = "pageNumber", defaultValue = "10" ) Integer pageNumber
    ) {
        LiveUserVO liveRoomVO = new LiveUserVO();
        liveRoomVO.setPageNumber(pageNumber);
        liveRoomVO.setPageSize(pageSize);
        liveRoomVO.setPlatformType(platformType);
        liveRoomVO.setStoreId(storeId);
        return ResponseHelper.shouldLogin()
                .invokeHasReturnVal(userId -> {
                    liveRoomVO.setMemberId(userId);
                    return liveService.onPlayRoomList(liveRoomVO);
                })
                .returnResponse();
    }

    /**
     * 分享直播间返回信息
     * @param storeId
     * @param roomId
     * @return
     */
    @RequestMapping("/share/live")
    public Response shareLive(Long storeId,Long roomId,Integer liveType){
        return liveService.shareLive(storeId,roomId,liveType);
    }


    /**
     * 根据房间号返回主播信息
     * @param roomId
     * @return
     */
    @RequestMapping("/show/anchor/information")
    public Response showAnchorInformation(Long roomId){
        return liveService.showAnchorInformation(roomId);
    }

    /**
     * 进入直播间
     * @param storeId
     * @param roomId
     * @param userId
     * @return
     */
    @RequestMapping("/into/room")
    public Response intoRoom(Long storeId,Long roomId,Long userId){
        return liveService.intoRoom(storeId,roomId,userId);

    }
}
