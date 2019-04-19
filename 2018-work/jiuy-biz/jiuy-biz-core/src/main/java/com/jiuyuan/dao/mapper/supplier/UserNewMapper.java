package com.jiuyuan.dao.mapper.supplier;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.store.StoreWxa;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 管理员表 Mapper 接口
 * </p>
 *
 * @author nijin
 * @since 2017-10-16
 */
@DBMaster
public interface UserNewMapper extends BaseMapper<UserNew> {

    Map<String, Object> getShareShopLoginQr(@Param("storeId") Long storeId);


    int updateShareShopLoginQr(@Param("id") Long id, @Param("shareQcCodeUrl") String shareQcCodeUrl);

    List<StoreWxa>selectStoreWxaList(@Param("storeId")Long storeId);


}