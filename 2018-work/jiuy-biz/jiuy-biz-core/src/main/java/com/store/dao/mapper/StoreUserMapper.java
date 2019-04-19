package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;


@DBMaster
public interface StoreUserMapper {

	public StoreBusiness getStoreBusinessByWxaAppId(String wxaAppId);

	public StoreBusiness getStoreBusiness4Login(String userName);

	public StoreBusiness getStoreBusinessByBusinessNumber(String businessNumber);

	public StoreBusiness getStoreBusinessByStoreId(long storeId);

	public StoreBusiness getStoreBusinessByPhone(String phone);

	public StoreBusiness getStoreBusinessById(long id);

	public StoreBusiness getStoreBusinessByWeixinId(String weixinId);

	public StoreBusiness getStoreBusinessByWeiXinNum(String weiXinNum);

	public List<StoreBusiness>  getStoreBusinessAuditList(PageQuery pageQuery,int status );

    public int resetUserPassword(@Param("userName") String userName, @Param("password") String password);

    public int updateStoreActiveTime(@Param("storeId") long storeId, @Param("sysTime") long sysTime);


    public int cancelAuth(long storeId);

    public int updateUserCid(@Param("id") long id, @Param("userCid") String userCid);

    public int updateUserBusinessNumber(@Param("id") long id, @Param("businessNumber") long businessNumber);

    public int updateUserProtocolTime(@Param("id") long id, @Param("time") long time);

    public int updateUserNickName(@Param("id") long id, @Param("userNickName") String userNickName);

    public int updateStoreNotice(@Param("id") long id, @Param("storeNotice") String storeNotice);


    public int wxaAuth(Map<String ,Object> map);

    public int addStoreBusiness(StoreBusiness storeBusiness);

    public int fillStoreBusinessData(StoreBusiness storeBusiness);

    public int saveStoreAudit(StoreAudit storeAudit);

    public int selectAuditCountByStoreId(@Param( "storeId" ) long storeId, @Param( "statusArray" ) int[] statusArray);

    public int oldUserBindWeixin(StoreBusiness storeBusiness);

    public int getStoreBusinessCountByBusinessNumber(@Param( "businessName" ) String businessName, @Param( "storeId" ) long storeId);

    public int updateStoreAuditStatus(@Param( "storeId" ) long storeId, @Param( "status" ) int status);

    public void cleanStoreBusinessProvinceCityCounty(@Param( "storeId" ) long storeId);

    public int updateSupplierId(@Param( "supplierId" ) long supplierId, @Param( "storeId" ) long storeId);

    public void addStoreBusinessStoreDisplayImages(@Param( "storeId" ) long storeId, @Param( "storeDisplayImages" ) String storeDisplayImages);

}