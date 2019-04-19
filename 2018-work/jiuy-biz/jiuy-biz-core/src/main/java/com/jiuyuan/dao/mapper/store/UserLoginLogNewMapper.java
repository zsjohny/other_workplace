package com.jiuyuan.dao.mapper.store;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.log.UserLoginLogNew;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 用户登陆表 Mapper 接口
 * </p>
 *
 * @author Aison
 * @since 2018-05-15
 */
@DBMaster
public interface UserLoginLogNewMapper extends BaseMapper<UserLoginLogNew> {


    /**
     * 登出的时候设置某个设备的用户全部为登出状态
     * @param phone
     * @param deviceId
     * @date:   2018/5/16 12:18
     * @author: Aison
     */
    Integer logout(@Param("deviceId") String deviceId,@Param("phone") String phone);
}