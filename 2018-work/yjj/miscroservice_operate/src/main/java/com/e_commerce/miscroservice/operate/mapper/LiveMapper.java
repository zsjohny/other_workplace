package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.operate.entity.request.FindLiveRequest;
import com.e_commerce.miscroservice.operate.entity.response.FindAllLiveResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 15:42
 */
@Mapper
public interface LiveMapper {
    /**
     * 查询主播 根据类型
     * @param liveRequest
     * @return
     */
    List<FindAllLiveResponse> findAllAnchorByType(@Param("liveRequest") FindLiveRequest liveRequest, @Param("liveType") Integer liveType);

    /**
     * 根据storId和类型查找店家账号手机号和姓名
     * @param storeId
     * @param type
     * @return
     */
    LiveUser findAnchorPhoneNumberByStoreIdType(@Param("storeId") Long storeId, @Param("type") int type);
}
