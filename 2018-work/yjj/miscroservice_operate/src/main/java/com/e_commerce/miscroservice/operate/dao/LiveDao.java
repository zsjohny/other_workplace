package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;
import com.e_commerce.miscroservice.operate.entity.response.FindAllLiveResponse;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 15:42
 */
public interface LiveDao {
    /**
     * 查询主播列表
     * @param liveRequest
     * @param type
     * @return
     */
    List<FindAllLiveResponse> findAllAnchorByType(FindLiveRequest liveRequest, Integer type);

    /**
     * 根据storId和类型查找店家账号手机号和姓名
     * @param storeId
     * @param type
     * @return
     */
    LiveUser findAnchorPhoneNumberByStoreIdType(Long storeId, int type);

    /**
     * 添加官方主播
     * @param liveUser
     */
    void addAnchor(LiveUser liveUser);

    /**
     * 删除官方主播
     */
    void upOfficialAnchor(LiveUser liveUser);

    /**
     * 根据手机号查询
     * @param phone
     * @return
     */
    LiveUser findAnchorByPhone(String phone);
}
