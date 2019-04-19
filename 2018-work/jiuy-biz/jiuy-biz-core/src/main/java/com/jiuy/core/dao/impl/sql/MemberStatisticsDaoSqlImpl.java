package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.MemberStatisticsDao;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsBean;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsSearchBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsSeniorBean;
import com.jiuyuan.entity.MemberStatistics;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class MemberStatisticsDaoSqlImpl implements MemberStatisticsDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int getProductSaleCount(MemberStatistics memberStatistics) {

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchProductSaleCount", params);
	}

	@Override

	public List<Map<String, String>> getProductSale(PageQuery pageQuery, MemberStatistics memberStatistics,int sort) {
		 	Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("pageQuery", pageQuery);
			params.put("params", memberStatistics);
			params.put("sort", sort);
			return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchProductSale", params);
	}

	@Override
	public Map<String, String> getProductSaleTotalCount(MemberStatistics memberStatistics) {

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getProductSaleTotalCount", params);
	}

	@Override
	public List<ChannelStatisticsBean> searchChannelStatistics(ChannelStatisticsSearchBean searchBean,PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("searchBean", searchBean);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchChannelStatistics", params);
	}

	@Override
	public int getChannelStatisticsCount(ChannelStatisticsSearchBean searchBean) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("searchBean", searchBean);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getChannelStatisticsCount", params);
	}

	@Override
	public Map<String, Object> sumCurrentChannelTotal(ChannelStatisticsSearchBean searchBean) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("searchBean", searchBean);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumCurrentChannelTotal", params);
	}

	@Override
	public Map<String, Object> sumCurrentChannelTotalMoney(Collection<Long> userIds) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("userIds", userIds);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumCurrentChannelTotalMoney", params);
	}

	@Override

	public List<Map<String, Object>> searchCategoryStatistics(PageQuery pageQuery, String categoryName, long startTime,
			long endTime,int sort) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("pageQuery", pageQuery);
		params.put("categoryName", categoryName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("sort", sort);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchCategoryStatistics", params);
	}

	@Override
	public int getCategoryStatisticsCount(String categoryName, long startTime, long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("categoryName", categoryName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getCategoryStatisticsCount", params);
	}

	public int getProductDetailSaleCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchProductDetailSaleCount", params);
	}

	@Override
	public List<Map<String, String>> getProductDetailSaleCount(PageQuery pageQuery, MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchProductDetailSale", params);

	}

	@Override
	public int getProvinceSaleCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getProvinceSaleCount", params);
	}

	@Override
	public List<Map<String, String>> searchProvinceSale(PageQuery pageQuery, MemberStatistics memberStatistics,int sort) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		params.put("sort", sort);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchProvinceSale", params);
	}

	@Override
	public Map<String, String> getProvinceSaleTotal(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getProvinceSaleTotal", params);
	}

	@Override
	public int getCitySaleCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getCitySaleCount", params);
	}

	@Override
	public List<Map<String, String>> searchCitySale(PageQuery pageQuery,MemberStatistics memberStatistics,int sort) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		params.put("sort", sort);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchCitySale", params);
	}

	@Override
	public Map<String, String> getCitySaleTotal(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getCitySaleTotal", params);
	}

	@Override
	public List<Map<String, Object>> searchBrandStatistics(PageQuery pageQuery, String brandName, long startTime,
			long endTime,int sort) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("pageQuery", pageQuery);
		params.put("brandName", brandName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("sort", sort);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchBrandStatistics", params);
	}

	@Override
	public int getBrandStatisticsCount(String brandName, long startTime, long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("brandName", brandName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getBrandStatisticsCount", params);
	}

	@Override
	public List<ChannelStatisticsBean> searchChannelStatisticsAllUser(ChannelStatisticsSearchBean searchBean) {
		HashMap<String, Object> params = new HashMap<String,Object>();
		params.put("searchBean", searchBean);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchChannelStatisticsAllUser", params);
	}

	@Override
	public int getPageCategoryCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getPageCategoryCount", params);
	}

	@Override
	public List<Map<String, String>> searchPageCategory(PageQuery pageQuery, MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchPageCategory", params);
	}

	@Override
	public int getPageProductDetailCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getPageProductDetailCount", params);
	}

	@Override
	public List<Map<String, String>> searchPageProductDetail(PageQuery pageQuery, MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchPageProductDetail", params);
	}

	@Override
	public Map<String, Object> searchUserStatistics(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchUserStatistics", params);
	}

	@Override
	public Map<String, Object> sumCurrentBrandStatisticsTotal(String brandName, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brandName", brandName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumCurrentBrandStatisticsTotal", params);
	}

	@Override
	public List<TemplateStatisticsBean> searchTemplateStatistics(PageQuery pageQuery,long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchTemplateStatistics", params);
	}

	@Override
	public List<TemplateStatisticsSeniorBean> searchTemplateStatisticsSenior(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime, int sort,PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("floorName", floorName);
		params.put("code", code);
		params.put("templateId", templateId);
		params.put("serialNumber", serialNumber);
		params.put("clickStartTime", clickStartTime);
		params.put("clickEndTime", clickEndTime);
		params.put("promoteStartTime", promoteStartTime);
		params.put("promoteEndTime", promoteEndTime);
		params.put("sort", sort);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchTemplateStatisticsSenior", params);
	}

	@Override
	public Map<String, Object> sumTemplateStatisticsSenior(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("floorName", floorName);
		params.put("code", code);
		params.put("templateId", templateId);
		params.put("serialNumber", serialNumber);
		params.put("clickStartTime", clickStartTime);
		params.put("clickEndTime", clickEndTime);
		params.put("promoteStartTime", promoteStartTime);
		params.put("promoteEndTime", promoteEndTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumTemplateStatisticsSenior", params);
	}

	@Override
	public List<Map<String, Object>> lookPvPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.lookPvPerDay", params);
	}

	@Override
	public List<Map<String, Object>> lookClickPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.lookClickPerDay", params);
	}

	@Override
	public List<Map<String, Object>> lookOrderPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.lookOrderPerDay", params);
	}

	@Override
	public int getLookMoreCount(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getLookMoreCount", params);
	}

	@Override
	public List<Map<String, String>> searchLookMore(PageQuery pageQuery, MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchLookMore", params);
	}

	@Override
	public int getLookMorePv(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getLookMorePv", params);
	}

	@Override
	public Map<String, Object> getSummaryPageProductDetail(MemberStatistics memberStatistics) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("params", memberStatistics);
		Map<String, Object> map = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getSummaryPageProductDetail", params);
		return map;
	}

	@Override
	public int getTemplateStatisticsSeniorCount(int type, String floorName, String code, String templateId,
			String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime, long promoteEndTime,
			int sort) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("floorName", floorName);
		params.put("code", code);
		params.put("templateId", templateId);
		params.put("serialNumber", serialNumber);
		params.put("clickStartTime", clickStartTime);
		params.put("clickEndTime", clickEndTime);
		params.put("promoteStartTime", promoteStartTime);
		params.put("promoteEndTime", promoteEndTime);
		params.put("sort", sort);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getTemplateStatisticsSeniorCount", params);
	}

	@Override
	public Map<String, Object> sumTemplateStatisticsSeniorOrder(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("floorName", floorName);
		params.put("code", code);
		params.put("templateId", templateId);
		params.put("serialNumber", serialNumber);
		params.put("clickStartTime", clickStartTime);
		params.put("clickEndTime", clickEndTime);
		params.put("promoteStartTime", promoteStartTime);
		params.put("promoteEndTime", promoteEndTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumTemplateStatisticsSeniorOrder", params);
	}

	@Override
	public int searchTemplateStatisticsCount(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchTemplateStatisticsCount", params);
	}

	@Override
	public List<Map<String, Object>> searchPageStatistics(PageQuery pageQuery, int type, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		params.put("type", type);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.searchPageStatistics", params);
	}

	@Override
	public int getPageStatisticsCount(int type, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("type", type);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getPageStatisticsCount", params);
	}

	@Override
	public Map<String, Object> sumPageStatisticsTotal(int type, long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("type", type);
		Map<String, Object> map = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumPageStatisticsTotal", params);
		Map<String, Object> deepMap = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumDeepStatisticsTotal", params);
		map.put("deepPv", deepMap.get("deepPv"));
		map.put("deepUv", deepMap.get("deepUv"));
		return map;
	}

	@Override
	public List<Map<String, Object>> getTotayPageData(int type, long totayZeroTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", totayZeroTime);
		params.put("endTime", endTime);
		params.put("type", type);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getTotayPageData", params);
	}

	@Override
	public Map<String, Object> sumTotayPageData(int type, long totayZeroTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", totayZeroTime);
		params.put("endTime", endTime);
		params.put("type", type);
		Map<String, Object> map = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumTotayPageData", params);
		Map<String, Object> deepMap = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.sumDeepTotayPageData", params);
		map.put("deepPv", deepMap.get("deepPv"));
		map.put("deepUv", deepMap.get("deepUv"));
		return map;
	}

	@Override
	public long getUserLastTimeVist(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		Object result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.MemberStatisticsDaoSqlImpl.getUserLastTimeVist",params);
		return result==null?0:(long)result;
	}

}
