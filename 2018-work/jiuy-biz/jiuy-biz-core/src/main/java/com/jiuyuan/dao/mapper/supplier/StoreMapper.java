package com.jiuyuan.dao.mapper.supplier;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yujj.entity.member.MemberOperatorResponse;
import com.yujj.entity.member.MembersFindRequest;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreBusiness;
import org.apache.ibatis.session.RowBounds;

/**
 * <p>
  * 门店商家 Mapper 接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@DBMaster
public interface StoreMapper extends BaseMapper<StoreBusiness> {

	Map<String, Object> getStoreBusinessDetail(@Param("storeId")long storeId, @Param("supplierId")long supplierId);

	List<Map<String, Object>> exportUserData(@Param("beginTime")long beginTime, @Param("endTime")long endTime);


	StoreBusiness findStoreDisplayImagesAndWxaAppIdById(@Param("storeId")Long storeId);

	int findWxaTypeById(@Param("id")Long storeId);
	StoreBusiness selectStoreBusiness(Long storeId);

	/**
	 * 更新用户 WxaArticleShow
	 * @param storeId
	 * @param wxaArticleShow
	 * @return void
	 * @auther Charlie(唐静)
	 * @date 2018/6/1 15:46
	 */
    void updateWxaArticleShowById(@Param("storeId")Long storeId, @Param("wxaArticleShow")Integer wxaArticleShow);


	/**
	 * 更新微信小程序关闭时间
	 *
	 * @param storeId 门店id
	 * @param newWxaCloseTime 新的时间
	 * @param historyWxaCloseTime 老的时间
	 * @return int
	 * @author Charlie
	 * @date 2018/8/16 14:24
	 */
    int updateWxaCloseTime(@Param ("storeId") Long storeId, @Param ("newWxaCloseTime") Long newWxaCloseTime, @Param ("historyWxaCloseTime") Long historyWxaCloseTime, @Param ("updTime") Long updTime);

	List<MemberOperatorResponse> selectMyPageLists(Page<MemberOperatorResponse> page,@Param ("membersFindRequest") MembersFindRequest membersFindRequest);


    StoreBusiness findHomeStoreById(Long id);

	/**
	 * 查询用户信息
	 * @param storeId
	 * @return
	 */
    StoreBusiness findStoreBusinessById(@Param("storeId") Long storeId);
}