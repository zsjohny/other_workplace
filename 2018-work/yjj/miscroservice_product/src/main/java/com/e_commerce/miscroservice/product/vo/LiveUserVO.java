package com.e_commerce.miscroservice.product.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/17 17:25
 * @Copyright 玖远网络
 */
@Data
public class LiveUserVO {

    private Long id;
    private Long anchorId;
    private Long roomNum;

    private Integer pageSize;
    private Integer pageNumber;
    /**
     * platformType 0自有直播.1平台直播
     */
    private Integer platformType;
    private Long storeId;
    private Long memberId;
    /**
     * 直播商品数量
     */
    private Long liveProductCount;
    private String icon;
    private String liveRoomName;
    private Long onlineUserCount;
    private String nickName;
    private List<Map<String, Object>> productList;

    private List<String> imgList;
    private String payUrl;

    public static LiveUserVO me() {
        return new LiveUserVO();
    }

}
