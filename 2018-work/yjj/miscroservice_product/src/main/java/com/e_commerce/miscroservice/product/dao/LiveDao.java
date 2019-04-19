package com.e_commerce.miscroservice.product.dao;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/16 14:13
 */
public interface LiveDao {
    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    LiveUser findAnchorByUserId(Long userId);

    List<LiveUserDTO> findLiveUserByStoreAndType(Long storeId, Integer liveType);

    /**
     * 更新
     * @param user
     */
    void upLiveUser(LiveUser user);
}
