/**
 * 
 */
package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.UserAliPay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 
 * 
 * @author xiaoluo
 * @version $Id: UserAliPayRepository.java, 2016年2月25日 下午9:16:25
 */
@RepositoryDefinition(domainClass = UserAliPay.class, idClass = Long.class)
public interface UserAliPayRepository {
	@Modifying
	@Query("update UserAliPay set payCount=:payCount,userModifyTime=now() where  isDeleted='0'  and payId =:payId and payInfo=:payInfo")
	int updateUserAlipayCountByPayId(@Param("payCount") Double payCount, @Param("payId") Integer payId,
									 @Param("payInfo") String payInfo);

	@Modifying
	@Query("update UserAliPay set  isDeleted=:isDeleted where   UserAliPayId=:UserAliPayId")
	int deleteUserAlipayByUserAliPayId(@Param("isDeleted") String isDeleted, @Param("UserAliPayId") Long UserAliPayId);

	@Modifying
	@Query("update UserAliPay set isAuthor=:isAuthor ,sign=:sign ,payCount=:payCount ,userAliAccount=:userAliAccount ,userModifyTime=now() where  isDeleted='0'  and payInfo =:payInfo")
	int updateUserAlipayIsAuthorByPayId(@Param("isAuthor") String isAuthor, @Param("sign") String sign,
										@Param("payCount") Double payCount, @Param("payInfo") String payInfo,
										@Param("userAliAccount") String userAliAccount);

	@Modifying
	@Query("update UserAliPay set isAuthor=:isAuthor ,sign=:sign ,payCount=:payCount ,userAliAccount=:userAliAccount  where  isDeleted='0'  and payInfo =:payInfo")
	int updateUserAlipayIsAuthorAliPayByPayId(@Param("isAuthor") String isAuthor, @Param("sign") String sign,
											  @Param("payCount") Double payCount, @Param("payInfo") String payInfo,
											  @Param("userAliAccount") String userAliAccount);

	@Query("from UserAliPay  where  isDeleted='0'  and payId =:payId and payInfo=:payInfo ")
	UserAliPay queryUserAlipayByIdAndPayInfo(@Param("payId") Integer payId, @Param("payInfo") String payInfo);

	@Query(value = "select * from useralipay  where payId =:payId and userModifyTime is null  ", nativeQuery = true)
	List<UserAliPay> queryUserAlipayPayRecordByPayId(@Param("payId") Integer payId);

	@Query(value = "select * from useralipay  where payId =:payId and userModifyTime is null and payCount is not null  ", nativeQuery = true)
	List<UserAliPay> queryEmptyUserAliPayRecordByPayId(@Param("payId") Integer payId);

//	@Query("select count(userAliPayId) from UserAliPay  where payId =:payId")
//	Integer queryUserAliPayRecordCountByPayId(@Param("payId") Integer payId);

	@Query(value = "select count(userAliPayId) from useralipay  where payId =:payId",nativeQuery = true)
	Integer queryUserAliPayRecordCountByPayId(@Param("payId") Integer payId);

	@Query("from UserAliPay  where payId =:payId")
	List<UserAliPay> queryUserAlipayAllByPayId(@Param("payId") Integer payId);
}
