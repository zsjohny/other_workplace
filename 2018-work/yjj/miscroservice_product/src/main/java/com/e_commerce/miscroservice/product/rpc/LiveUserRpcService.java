package com.e_commerce.miscroservice.product.rpc;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/17 12:55
 * @Copyright 玖远网络
 */
@FeignClient(value = "USER", path = "system/rpc/liveUserRpcController")
public interface LiveUserRpcService {

    /**
     * 查询liveUser
     *
     * @param anchorId memberId
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:58
     */
    @RequestMapping("findLiveUserByAnchorId")
    LiveUserDTO findLiveUserByAnchorId(@RequestParam( "anchorId" ) Long anchorId);


    /**
     * 查询liveUser
     *
     * @param roomNum roomNum
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:58
     */
    @RequestMapping("findLiveUserByRoomNum")
    LiveUserDTO findLiveUserByRoomNum(@RequestParam( "roomNum" ) Long roomNum);


}
