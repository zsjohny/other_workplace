package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.memberstatistics.ChannelStatisticsBean;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsSearchBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsSeniorBean;
import com.jiuyuan.entity.MemberStatistics;
import com.jiuyuan.entity.query.PageQuery;

public interface MemberStatisticsDao {

	int getProductSaleCount(MemberStatistics memberStatistics);

	List<Map<String, String>> getProductSale(PageQuery pageQuery, MemberStatistics memberStatistics,int sort);

	Map<String, String> getProductSaleTotalCount(MemberStatistics memberStatistics);

	List<ChannelStatisticsBean> searchChannelStatistics(ChannelStatisticsSearchBean searchBean,PageQuery pageQuery);
	
	int getChannelStatisticsCount(ChannelStatisticsSearchBean searchBean);
	
	Map<String, Object> sumCurrentChannelTotal(ChannelStatisticsSearchBean searchBean);
	
	Map<String, Object> sumCurrentChannelTotalMoney(Collection<Long> userIds);

	List<Map<String, Object>> searchCategoryStatistics(PageQuery pageQuery,String categoryName,long startTime,long endTime,int sort);
	
	int getCategoryStatisticsCount(String categoryName,long startTime,long endTime);
	
	List<Map<String, Object>> searchBrandStatistics(PageQuery pageQuery,String brandName,long startTime,long endTime,int sort);
	
	int getBrandStatisticsCount(String brandName,long startTime,long endTime);

	int getProductDetailSaleCount(MemberStatistics memberStatistics);

	List<Map<String, String>> getProductDetailSaleCount(PageQuery pageQuery, MemberStatistics memberStatistics);

	int getProvinceSaleCount(MemberStatistics memberStatistics);

	List<Map<String, String>> searchProvinceSale(PageQuery pageQuery, MemberStatistics memberStatistics,int sort);

	Map<String, String> getProvinceSaleTotal(MemberStatistics memberStatistics);

	int getCitySaleCount(MemberStatistics memberStatistics);

	List<Map<String, String>> searchCitySale(PageQuery pageQuery, MemberStatistics memberStatistics,int sort);

	Map<String, String> getCitySaleTotal(MemberStatistics memberStatistics);
	
	List<ChannelStatisticsBean> searchChannelStatisticsAllUser(ChannelStatisticsSearchBean searchBean);

	int getPageCategoryCount(MemberStatistics memberStatistics);

	List<Map<String, String>> searchPageCategory(PageQuery pageQuery, MemberStatistics memberStatistics);

	int getPageProductDetailCount(MemberStatistics memberStatistics);

	List<Map<String, String>> searchPageProductDetail(PageQuery pageQuery, MemberStatistics memberStatistics);

	Map<String, Object> searchUserStatistics(MemberStatistics memberStatistics);

	Map<String, Object> sumCurrentBrandStatisticsTotal(String brandName,long startTime,long endTime);
	
	List<TemplateStatisticsBean> searchTemplateStatistics(PageQuery pageQuery,long startTime,long endTime);
	
	int searchTemplateStatisticsCount(long startTime,long endTime);
	
	List<TemplateStatisticsSeniorBean> searchTemplateStatisticsSenior(int type,String floorName,String code,String templateId,String serialNumber,long clickStartTime,long clickEndTime,long promoteStartTime,long promoteEndTime,int sort, PageQuery pageQuery);
	
	public Map<String, Object> sumTemplateStatisticsSenior(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime);
	
	public Map<String, Object> sumTemplateStatisticsSeniorOrder(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime);
	
	List<Map<String, Object>> lookPvPerDay(long startTime, long endTime);

	List<Map<String, Object>> lookClickPerDay(long startTime, long endTime);

	List<Map<String, Object>> lookOrderPerDay(long startTime, long endTime);

	int getLookMoreCount(MemberStatistics memberStatistics);

	List<Map<String, String>> searchLookMore(PageQuery pageQuery, MemberStatistics memberStatistics);

	int getLookMorePv(MemberStatistics memberStatistics);

	Map<String, Object> getSummaryPageProductDetail(MemberStatistics memberStatistics);

	int getTemplateStatisticsSeniorCount(int type, String floorName, String code, String templateId,
			String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime, long promoteEndTime,
			int sort);

	List<Map<String, Object>> searchPageStatistics(PageQuery pageQuery, int type, long startTime, long endTime);

	int getPageStatisticsCount(int type, long startTime, long endTime);

	Map<String, Object> sumPageStatisticsTotal(int type, long startTime, long endTime);

	List<Map<String, Object>> getTotayPageData(int type, long totayZeroTime, long endTime);

	Map<String, Object> sumTotayPageData(int type, long totayZeroTime, long endTime);
	
	long getUserLastTimeVist(long userId);
}
