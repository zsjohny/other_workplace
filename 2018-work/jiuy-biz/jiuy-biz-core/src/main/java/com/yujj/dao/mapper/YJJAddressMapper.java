/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
//import com.yujj.entity.account.Address;
import com.jiuyuan.entity.account.Address;

/**
 * @author LWS
 *
 */
@DBMaster
public interface YJJAddressMapper {

    public List<Address> loadAddressList(@Param("userId") long userId);
    
    public int addAddress(Address address);

    public Address getUserAddress(@Param("userId") long userId, @Param("addressId") long addressId);

    public int removeAddress(@Param("userId") long userId, @Param("addressId") long addressId);

    public int removeDefaultAddress(@Param("userId") long userId);

    public int setDefaultAddress(@Param("userId") long userId, @Param("addressId") long addressId);

    public int updateAddress(@Param("userId") long userId, @Param("address") Address address);
}
