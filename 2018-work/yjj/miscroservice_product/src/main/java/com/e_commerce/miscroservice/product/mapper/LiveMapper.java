package com.e_commerce.miscroservice.product.mapper;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/16 14:13
 */
@Mapper
public interface LiveMapper {
    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    LiveUser findAnchorByUserId(@Param("userId") Long userId);

    /**
     * 查找一个直播
     *
     * @param storeId storeId
     * @param liveType liveType
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO
     * @author Charlie
     * @date 2019/1/17 16:38
     */
    List<LiveUserDTO> findLiveUserByStoreAndType(@Param( "storeId" ) Long storeId, @Param( "liveType" ) Integer liveType);



    /**
     * 根据房间号查主播
     *
     * @param roomNumList roomNumList
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.user.LiveUser>
     * @author Charlie
     * @date 2019/1/17 17:45
     */
    List<LiveUser> findLiveUserByRoomNums(@Param( "roomNumList" ) List<Long> roomNumList);
}
