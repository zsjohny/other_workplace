/**
 * 
 */
package com.jiuyuan.dao.mapper.shop;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.account.Address;

/**
 * @author LWS
 *
 */
@DBMaster
public interface AddressMapper {

    public List<Address> loadAddressList(@Param("userId") long userId);

	public Address getUserAddress(@Param("userId") long userId, @Param("addressId") long addressId);

	public int addAddress(Address address);
	
//	public List<AreaProvince> getAreaProvinceList();
//    
//    public List<AreaCity> getAreaCityList(@Param("provinceCode") long provinceCode);
//    
//    public List<AreaDistrict> getAreaDistrictList(@Param("cityCode") long cityCode);

    public int removeAddress(@Param("userId") long userId, @Param("addressId") long addressId);

    public int removeDefaultAddress(@Param("userId") long userId);

    public int setDefaultAddress(@Param("userId") long userId, @Param("addressId") long addressId);

    public int updateAddress(@Param("userId") long userId, @Param("address") Address address);

	public List<Address> AddressOfUserIdsStore(@Param("userIds")Collection<Long> userIds);
    
}
