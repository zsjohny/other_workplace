package com.e_commerce.miscroservice.order.mapper;

import com.e_commerce.miscroservice.commons.entity.application.proxy.ProxyAddress;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述
 * @date 2018/9/18 17:25
 * @return
 */
@Mapper
public interface ProxyAddressMapper extends BaseMapper<ProxyAddress> {

    @Select("SELECT * FROM `yjj_proxy_address` addr WHERE addr.user_id=#{userId} and addr.is_default in (#{isDefault}) and addr.del_status=0 " +
            " order by addr.is_default desc, addr.update_time desc, addr.create_time desc")
    List<ProxyAddress> getAddressByUserId(@Param("userId") Long userId, @Param("isDefault")int isDefault) ;

    @Select("SELECT * FROM `yjj_proxy_address` addr WHERE addr.user_id=#{userId} and addr.del_status=0 " +
            "order by addr.is_default desc, addr.update_time desc, addr.create_time desc")
    List<ProxyAddress> getAddressByUserIdN(@Param("userId") Long userId) ;
}
