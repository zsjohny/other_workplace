package com.jiuy.core.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.MemberStatisticsDao;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsBean;
import com.jiuy.core.meta.memberstatistics.ChannelStatisticsSearchBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsBean;
import com.jiuy.core.meta.memberstatistics.TemplateStatisticsSeniorBean;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.MemberStatistics;
import com.jiuyuan.entity.query.PageQuery;

@Service
public class MemberStatisticsServiceImpl implements MemberStatisticsService {

	@Autowired
	private MemberStatisticsDao memberStatisticsDao;

	@Override
	public int getProductSaleCount(MemberStatistics memberStatistics) {

		return memberStatisticsDao.getProductSaleCount(memberStatistics);
	}

	@Override

	public List<Map<String, String>> searchProductSale(PageQuery pageQuery, MemberStatistics memberStatistics,
			int sort) {

		return memberStatisticsDao.getProductSale(pageQuery, memberStatistics, sort);
	}

	@Override
	public Map<String, String> getProductSaleTotal(MemberStatistics memberStatistics) {

		return memberStatisticsDao.getProductSaleTotalCount(memberStatistics);
	}

	@Override
	public List<ChannelStatisticsBean> searchChannelStatistics(ChannelStatisticsSearchBean searchBean,
			PageQuery pageQuery) {
		List<ChannelStatisticsBean> list = memberStatisticsDao.searchChannelStatistics(searchBean, pageQuery);
		for(ChannelStatisticsBean bean : list){
			bean.setLastTimeVist(memberStatisticsDao.getUserLastTimeVist(bean.getUserId()));
		}
		return list;
	}

	@Override
	public int getChannelStatisticsCount(ChannelStatisticsSearchBean searchBean) {
		return memberStatisticsDao.getChannelStatisticsCount(searchBean);
	}

	@Override
	public Map<String, Object> sumCurrentChannelTotal(ChannelStatisticsSearchBean searchBean) {
		return memberStatisticsDao.sumCurrentChannelTotal(searchBean);
	}

	@Override
	public Map<String, Object> sumCurrentChannelTotalMoney(Collection<Long> userIds) {
		return memberStatisticsDao.sumCurrentChannelTotalMoney(userIds);
	}

	@Override
	public List<Map<String, Object>> searchCategoryStatistics(PageQuery pageQuery, String categoryName, long startTime,
			long endTime, int sort) {
		return memberStatisticsDao.searchCategoryStatistics(pageQuery, categoryName, startTime, endTime, sort);
	}

	@Override
	public int getCategoryStatisticsCount(String categoryName, long startTime, long endTime) {
		return memberStatisticsDao.getCategoryStatisticsCount(categoryName, startTime, endTime);
	}

	@Override
	public int getProductDetailSaleCount(MemberStatistics memberStatistics) {

		return memberStatisticsDao.getProductDetailSaleCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchProductDetailSale(PageQuery pageQuery, MemberStatistics memberStatistics) {

		return memberStatisticsDao.getProductDetailSaleCount(pageQuery, memberStatistics);
	}

	@Override
	public int getProvinceSaleCount(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getProvinceSaleCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchProvinceSale(PageQuery pageQuery, MemberStatistics memberStatistics,
			int sort) {
		return memberStatisticsDao.searchProvinceSale(pageQuery, memberStatistics, sort);
	}

	@Override
	public Map<String, String> getProvinceSaleTotal(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getProvinceSaleTotal(memberStatistics);
	}

	@Override
	public int getCitySaleCount(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getCitySaleCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchCitySale(PageQuery pageQuery, MemberStatistics memberStatistics, int sort) {
		return memberStatisticsDao.searchCitySale(pageQuery, memberStatistics, sort);
	}

	@Override
	public Map<String, String> getCitySaleTotal(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getCitySaleTotal(memberStatistics);
	}

	@Override
	public List<Map<String, Object>> searchBrandStatistics(PageQuery pageQuery, String brandName, long startTime,
			long endTime, int sort) {
		return memberStatisticsDao.searchBrandStatistics(pageQuery, brandName, startTime, endTime, sort);
	}

	@Override
	public int getBrandStatisticsCount(String brandName, long startTime, long endTime) {
		return memberStatisticsDao.getBrandStatisticsCount(brandName, startTime, endTime);
	}

	@Override
	public List<ChannelStatisticsBean> searchChannelStatisticsAllUser(ChannelStatisticsSearchBean searchBean) {
		return memberStatisticsDao.searchChannelStatisticsAllUser(searchBean);
	}

	@Override
	public int getPageCategoryCount(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getPageCategoryCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchPageCategory(PageQuery pageQuery, MemberStatistics memberStatistics) {
		return memberStatisticsDao.searchPageCategory(pageQuery, memberStatistics);
	}

	@Override
	public int getPageProductDetailCount(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getPageProductDetailCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchPageProductDetail(PageQuery pageQuery, MemberStatistics memberStatistics) {
		return memberStatisticsDao.searchPageProductDetail(pageQuery, memberStatistics);
	}

	@Override
	public Map<String, Object> searchUserStatistics(MemberStatistics memberStatistics) {
		return memberStatisticsDao.searchUserStatistics(memberStatistics);
	}

	@Override
	public Map<String, Object> sumCurrentBrandStatisticsTotal(String brandName, long startTime, long endTime) {
		return memberStatisticsDao.sumCurrentBrandStatisticsTotal(brandName, startTime, endTime);
	}

	@Override
	public List<TemplateStatisticsBean> searchTemplateStatistics(PageQuery pageQuery, long startTime, long endTime) {
		return memberStatisticsDao.searchTemplateStatistics(pageQuery, startTime, endTime);
	}

	@Override
	public List<TemplateStatisticsSeniorBean> searchTemplateStatisticsSenior(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime, int sort, PageQuery pageQuery) {
		return memberStatisticsDao.searchTemplateStatisticsSenior(type, floorName, code, templateId, serialNumber,
				clickStartTime, clickEndTime, promoteStartTime, promoteEndTime, sort, pageQuery);
	}

	@Override
	public Map<String, Object> sumTemplateStatisticsSenior(int type, String floorName, String code, String templateId,
			String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime, long promoteEndTime) {
		return memberStatisticsDao.sumTemplateStatisticsSenior(type, floorName, code, templateId, serialNumber,
				clickStartTime, clickEndTime, promoteStartTime, promoteEndTime);
	}

	@Override
	public Map<String, Object> lookPvPerDay(long startTime, long endTime) {
		List<Map<String, Object>> list = memberStatisticsDao.lookPvPerDay(startTime, endTime);

		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> subMap : list) {
				map.put((String) subMap.get("day"), subMap.get("count"));
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> lookClickPerDay(long startTime, long endTime) {
		List<Map<String, Object>> list = memberStatisticsDao.lookClickPerDay(startTime, endTime);

		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> subMap : list) {
				map.put((String) subMap.get("day"), subMap.get("count"));
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> lookOrderPerDay(long startTime, long endTime) {
		List<Map<String, Object>> list = memberStatisticsDao.lookOrderPerDay(startTime, endTime);

		Map<String, Object> map = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> subMap : list) {
				long count = (long) subMap.get("count");
				BigDecimal money = (BigDecimal) subMap.get("totalMoney");
				map.put((String) subMap.get("day"), new MemberStatistics(count, money));
			}
		}
		return map;
	}

	@Override
	public int getLookMoreCount(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getLookMoreCount(memberStatistics);
	}

	@Override
	public List<Map<String, String>> searchLookMore(PageQuery pageQuery, MemberStatistics memberStatistics) {
		return memberStatisticsDao.searchLookMore(pageQuery, memberStatistics);
	}

	@Override
	public int getLookMorePv(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getLookMorePv(memberStatistics);
	}

	@Override
	public Map<String, Object> getSummaryPageProductDetail(MemberStatistics memberStatistics) {
		return memberStatisticsDao.getSummaryPageProductDetail(memberStatistics);
	}

	@Override
	public int getTemplateStatisticsSeniorCount(int type, String floorName, String code, String templateId,
			String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime, long promoteEndTime,
			int sort) {
		return memberStatisticsDao.getTemplateStatisticsSeniorCount(type, floorName, code, templateId, serialNumber,
				clickStartTime, clickEndTime, promoteStartTime, promoteEndTime, sort);
	}

	@Override
	public Map<String, Object> sumTemplateStatisticsSeniorOrder(int type, String floorName, String code,
			String templateId, String serialNumber, long clickStartTime, long clickEndTime, long promoteStartTime,
			long promoteEndTime) {
		return memberStatisticsDao.sumTemplateStatisticsSeniorOrder(type, floorName, code, templateId, serialNumber,
				clickStartTime, clickEndTime, promoteStartTime, promoteEndTime);
	}

	@Override
	public int searchTemplateStatisticsCount(long startTime, long endTime) {
		return memberStatisticsDao.searchTemplateStatisticsCount(startTime, endTime);
	}

	@Override
	public List<Map<String, Object>> searchPageStatistics(PageQuery pageQuery, int type, long startTime, long endTime) {
		long totayZeroTime = DateUtil.getDayZeroTime(System.currentTimeMillis());// 当前0点
		List<Map<String, Object>> totayMaps = null;
		if (endTime > totayZeroTime) {// 需要计算今天的数据
			if (startTime >= totayZeroTime) {
				totayZeroTime = startTime;
			}
			totayMaps = memberStatisticsDao.getTotayPageData(type, totayZeroTime, endTime);
			endTime = totayZeroTime - 1;
		}
		List<Map<String, Object>> maps = memberStatisticsDao.searchPageStatistics(pageQuery, type, startTime, endTime);
		if (totayMaps != null && totayMaps.size() > 1 && maps != null && maps.size() > 1) {
			for (Map<String, Object> map : maps) {
				for (Map<String, Object> totayMap : totayMaps) {
					if (map.get("typeId") == totayMap.get("srcId")) {
						map.put("pv", ((BigDecimal) map.get("pv")).longValue() + (Long) totayMap.get("pv"));
						map.put("uv", ((BigDecimal) map.get("uv")).longValue() + (Long) totayMap.get("uv"));
						map.put("ip", ((BigDecimal) map.get("ip")).longValue() + (Long) totayMap.get("ip"));
						map.put("userCount",
								((BigDecimal) map.get("userCount")).longValue() + (Long) totayMap.get("userCount"));
						map.put("lose", ((BigDecimal) map.get("lose")).longValue() + (Long) totayMap.get("lose"));
						long duration = 0;
						long i = ((BigDecimal) map.get("count")).longValue()+((long) totayMap.get("count"));
						if (i != 0) {
							duration = (((BigDecimal) map.get("totalDuration")).longValue()
									+((BigDecimal) totayMap.get("totalDuration")).longValue()) / i;
						}
						map.put("duration", duration);
					}
				}
			}
		} else if ((maps == null || maps.size() < 1) && totayMaps != null && totayMaps.size() > 1) {
			maps = totayMaps;
		}
		return maps;
	}

	@Override
	public int getPageStatisticsCount(int type, long startTime, long endTime) {
		return memberStatisticsDao.getPageStatisticsCount(type, startTime, endTime);
	}

	@Override
	public Map<String, Object> sumPageStatisticsTotal(int type, long startTime, long endTime) {
		long totayZeroTime = DateUtil.getDayZeroTime(System.currentTimeMillis());// 当前0点
		Map<String, Object> totayMap = null;
		if (endTime > totayZeroTime) {// 需要计算今天的数据
			if (startTime >= totayZeroTime) {
				totayZeroTime = startTime;
			}
			totayMap = memberStatisticsDao.sumTotayPageData(type, totayZeroTime, endTime);
			endTime = totayZeroTime - 1;
		}
		Map<String, Object> map = memberStatisticsDao.sumPageStatisticsTotal(type, startTime, endTime);
		if (totayMap != null && totayMap.size() > 1 && map != null && map.size() > 1) {
			map.put("pvCount", ((BigDecimal) map.get("pvCount")).add((BigDecimal) totayMap.get("pvCount")));
			map.put("uvCount", ((BigDecimal) map.get("uvCount")).add((BigDecimal) totayMap.get("uvCount")));
			map.put("ipCount", ((BigDecimal) map.get("ipCount")).add((BigDecimal) totayMap.get("ipCount")));
			map.put("userCount", ((BigDecimal) map.get("userCount")).add((BigDecimal) totayMap.get("userCount")));
			map.put("loseCount", ((BigDecimal) map.get("loseCount")).add((BigDecimal) totayMap.get("loseCount")));
			long duration = 0;
			long i = ((BigDecimal) map.get("count")).add(((BigDecimal) totayMap.get("count"))).longValue();
			if (i != 0) {
				duration = (((BigDecimal) map.get("totalDuration")).add(((BigDecimal) totayMap.get("totalDuration"))))
						.longValue() / i;
			}
			map.put("duration", duration);
			map.put("deepPv", ((BigDecimal) map.get("deepPv")).add((BigDecimal) totayMap.get("deepPv")));
			map.put("deepUv", ((BigDecimal) map.get("deepUv")).add((BigDecimal) totayMap.get("deepUv")));
		} else if ((map == null || map.size() < 1) && totayMap != null && totayMap.size() > 1) {
			map = totayMap;
		}
		return map;
	}
}
