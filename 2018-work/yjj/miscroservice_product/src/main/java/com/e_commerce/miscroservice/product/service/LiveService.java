package com.e_commerce.miscroservice.product.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.product.vo.LiveUserVO;

/**
 * @Author hyf
 * @Date 2019/1/16 14:13
 */
public interface LiveService {
    /**
     * 开启直播间
     * @param title
     * @param userId
     * @param storeId
     * @return
     */
    Response openLive(String title, Long userId, Long storeId);

    /**
     * 查找直播间观看人数以及缩略图
     * @param userId
     * @param storeId
     * @param liveType
     * @param roomId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Response findAudience(Long userId, Long storeId, Integer liveType, Long roomId, Long pageNumber, Long pageSize);


    /**
     * 直播列表
     *
     *
     * @param vo
     * @return map
     * @author Charlie
     * @date 2019/1/17 16:17
     */
    MapHelper onPlayRoomList(LiveUserVO vo);

    /**
     * 直播间观看人数
     * @param userId
     * @param roomId
     * @return
     */
    Response findLiveDataTag(Long userId, Long roomId);

    /**
     * 销毁直播间
     * @param userId
     * @param storeId
     * @param liveType
     * @param roomId
     * @return
     */
    Response destroyLive(Long userId, Long storeId, Integer liveType, Long roomId);

    /**
     * 分享直播间返回信息
     * @param storeId
     * @param roomId
     * @param liveType
     * @return
     */
    Response shareLive(Long storeId, Long roomId, Integer liveType);

    /**
     * 根据房间号返回主播信息
     * @param roomId
     * @return
     */
    Response showAnchorInformation(Long roomId);

    /**
     * 进入直播间
     * @param storeId
     * @param roomId
     * @param userId
     * @return
     */
    Response intoRoom(Long storeId, Long roomId, Long userId);
}
