package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.GlobalSettingDao;
import com.jiuy.core.meta.global.ProductSeasonWeight;
import com.jiuy.core.meta.jiucoinsetting.ContinuousSetting;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.entity.GlobalSetting;
import com.jiuyuan.entity.JiuCoinSetting;
import com.jiuyuan.entity.JiuCoinSetting.InvitationSetting;
import com.jiuyuan.entity.JiuCoinSetting.PromoteShareSetting;
import com.jiuyuan.entity.JiuCoinSetting.SignInSetting;
import com.jiuyuan.entity.JiuCoinSetting.StoreScanReward;
import com.jiuyuan.service.common.MemcachedService;

@Service
public class GlobalSettingService {

	@Resource
	private GlobalSettingDao globalSettingDao;

	@SuppressWarnings("unused")
	@Autowired
	private MemcachedService memcachedService;

	// public List<GlobalSetting> getNavigationSetting() {
	// String groupKey = MemcachedKey.GROUP_KEY_GLOBAL_SETTING;
	// String key = "Navigation";
	// Object obj = memcachedService.get(groupKey, key);
	// if (obj != null) {
	// return (List<GlobalSetting>) obj;
	// }
	//
	// List<GlobalSetting> setting = globalSettingDao.getNavigationSetting();
	// if (setting != null) {
	// memcachedService.set(groupKey, key, 1000, setting);
	// }
	// return setting;
	// }

	public int add(GlobalSetting globalSetting) {
		long currentTime = System.currentTimeMillis();
		globalSetting.setCreateTime(currentTime);
		globalSetting.setUpdateTime(currentTime);

		return globalSettingDao.add(globalSetting);
	}

	public int deleteSetting(String propertyName) {
		return globalSettingDao.deleteSetting(propertyName);
	}

	public int update(GlobalSetting globalSetting) {
		return globalSettingDao.update(globalSetting);
	}

	public String getSetting(GlobalSettingName name) {
		String setting = globalSettingDao.getSetting(name);
		return setting;
	}

	public GlobalSetting getItem(GlobalSettingName globalSettingName) {
		String gss = globalSettingName.getStringValue();

		return globalSettingDao.getSettingByPropertyName(gss);
	}

	public Map<GlobalSettingName, Object> settingOfName(Collection<String> propertyNames) {
		List<GlobalSetting> settings = globalSettingDao.getItems(propertyNames);

		Map<GlobalSettingName, Object> settingMap = new HashMap<GlobalSettingName, Object>();
		for (GlobalSetting setting : settings) {
			String propertyName = setting.getPropertyName();
			GlobalSettingName globalSettingName = GlobalSettingName.getByStringValue(propertyName);
			String propertyValue = setting.getPropertyValue();

			// 由于这个不是json格式的
			if (globalSettingName == GlobalSettingName.PROMOTION_SEASON) {
				settingMap.put(globalSettingName, propertyValue.split(","));
				continue;
			}

			try {
				settingMap.put(globalSettingName, JSONArray.parse(propertyValue));
			} catch (Exception e) {
				settingMap.put(globalSettingName, propertyValue);
			}

		}
		return settingMap;
	}

	public JSONArray getJsonArray(GlobalSettingName name) {
		String value = getSetting(name);
		return StringUtils.isBlank(value) ? new JSONArray() : JSON.parseArray(value);
	}

	public JSONObject getJsonObject(GlobalSettingName name) {
		String value = getSetting(name);
		return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
	}

	public int getInt(GlobalSettingName name) {
		String value = getSetting(name);
		return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
	}

	public double getDouble(GlobalSettingName name) {
		String value = getSetting(name);
		return StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
	}

	public long getLong(GlobalSettingName name) {
		String value = getSetting(name);
		return StringUtils.isBlank(value) ? 0 : Long.parseLong(value);
	}

	public int update(String propertyName, String propertyValue) {
		return globalSettingDao.update(propertyName, propertyValue);
	}

	@Transactional(rollbackFor = Exception.class)
	public int addProductSeasonWeight(String propertyValue) {
		long currentTime = System.currentTimeMillis();
		globalSettingDao.resetProductSeasonWeight();
		JSONObject json = JSON.parseObject(propertyValue);
		if (json.containsKey("seasons")) {
			List<ProductSeasonWeight> seasons = JSON.parseArray(json.getString("seasons"), ProductSeasonWeight.class);
			if (seasons.size() > 0) {

				for (int i = 0; i < seasons.size(); i++) {
					ProductSeasonWeight promotionSeason = seasons.get(i);
					promotionSeason.setCreateTime(currentTime);
					promotionSeason.setUpdateTime(currentTime);
					promotionSeason.setStatus(0);
				}
				return globalSettingDao.addProductSeasonWeight(seasons);
			}
		}

		return 0;
	}

	@Transactional(rollbackFor = Exception.class)
	public int addJiuCoinSetting(String propertyName, String propertyValue, int groupId, String groupName,
			String description) {
		// 拼成需要的value
		String setting = getSetting(
				GlobalSettingName.getByStringValue(GlobalSettingName.JIUCOIN_GLOBAL_SETTING.getStringValue()));
		JSONObject parseObject = null;
		if (StringUtils.isBlank(setting)) {
			parseObject = new JSONObject();
		} else {
			parseObject = JSON.parseObject(setting);
		}
		if (StringUtils.equals(propertyName, "registerJiuCoinSetting") || StringUtils.equals(propertyName, "giftBag")) {
			parseObject.put(propertyName, Integer.parseInt(propertyValue));
		} else {
			JSONObject valueObject = StringUtils.isBlank(propertyValue) ? new JSONObject()
					: JSON.parseObject(propertyValue);
			parseObject.put(propertyName, valueObject);
		}

		String value = parseObject.toJSONString();

		// 计算极限值后添加修改json
		List<Map<String, Integer>> calculateLimitValue = calculateLimitValue(value);
		parseObject.put("sevenLimitValue", calculateLimitValue);

		String newValue = parseObject.toJSONString();

		GlobalSetting newGlobalSetting = new GlobalSetting();
		newGlobalSetting.setPropertyName(GlobalSettingName.JIUCOIN_GLOBAL_SETTING.getStringValue());
		newGlobalSetting.setPropertyValue(newValue);
		newGlobalSetting.setGroupName(groupName);
		newGlobalSetting.setGroupId(groupId);
		newGlobalSetting.setDescription(description);
		newGlobalSetting.setUpdateTime(System.currentTimeMillis());

		return add(newGlobalSetting);
	}

	@Transactional(rollbackFor = Exception.class)
	public int updateStatisticalSetting(String propertyName, String propertyValue, int groupId, String groupName,
			String description) {
		// 拼成需要的value
		String setting = getSetting(
				GlobalSettingName.getByStringValue(GlobalSettingName.STATISTICAL_SETTING.getStringValue()));
		JSONObject parseObject = null;
		if (StringUtils.isBlank(setting)) {
			parseObject = new JSONObject();
		} else {
			parseObject = JSON.parseObject(setting);
		}
		// if (StringUtils.equals(propertyName, "registerJiuCoinSetting") ||
		// StringUtils.equals(propertyName, "giftBag")) {
		// }
		parseObject.put(propertyName, Integer.parseInt(propertyValue));
		// else{
		// JSONObject valueObject = StringUtils.isBlank(propertyValue)? new
		// JSONObject(): JSON.parseObject(propertyValue);
		// parseObject.put(propertyName, valueObject);
		// }

		String value = parseObject.toJSONString();
		GlobalSetting newGlobalSetting = new GlobalSetting();
		newGlobalSetting.setPropertyName(GlobalSettingName.STATISTICAL_SETTING.getStringValue());
		newGlobalSetting.setPropertyValue(value);
		newGlobalSetting.setGroupName(groupName);
		newGlobalSetting.setGroupId(groupId);
		newGlobalSetting.setDescription(description);
		newGlobalSetting.setUpdateTime(System.currentTimeMillis());

		return add(newGlobalSetting);
	}

	/**
	 * 计算玖币7天极限值
	 */
	private List<Map<String, Integer>> calculateLimitValue(String setting) {
		if (StringUtils.isBlank(setting))
			return new ArrayList<>();
		JiuCoinSetting jiuCoinSetting = JSON.parseObject(setting, JiuCoinSetting.class);
		List<Map<String, Integer>> limitValues = new ArrayList<Map<String, Integer>>();

		int totalRegisterJiuCoin = 0;
		int totalScanJiuCoin = 0;
		int totalPromoteShareJiuCoin = 0;
		int totalSignInJiuCoin = 0;
		int totalGiftBag = 0;
		int totalInvitationJiuCoin = 0;
		int totalSubTotal = 0;

		for (int i = 1; i < 8; i++) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			int registerJiuCoin = registerJiuCoin(i, jiuCoinSetting.getRegisterJiuCoinSetting());

			StoreScanReward storeScanReward = jiuCoinSetting.getStoreScanReward();
			int scanJiuCoin = storeScanReward == null ? 0
					: scanJiuCoin(i, storeScanReward.getEachObtain(), storeScanReward.getMaxCountEachCycle(),
							storeScanReward.getObtainCycle());

			PromoteShareSetting promoteShareSetting = jiuCoinSetting.getPromoteShareSetting();
			int promoteShareJiuCoin = promoteShareSetting == null ? 0
					: promoteShareJiuCoin(i, promoteShareSetting.getEachShareObtain(),
							promoteShareSetting.getMaxCountEachShareCycle(), promoteShareSetting.getShareCycle(),
							promoteShareSetting.getEachClickObtain(), promoteShareSetting.getMaxCountEachClickCycle(),
							promoteShareSetting.getClickCycle());

			SignInSetting signInSetting = jiuCoinSetting.getSignInSetting();
			int signInJiuCoin = signInSetting == null ? 0
					: signInJiuCoin(i, signInSetting.getDailyObtain(), signInSetting.getContinuousSetting());

			int giftBag = giftBagJiuCoin(i, jiuCoinSetting.getGiftBag());

			InvitationSetting invitationSetting = jiuCoinSetting.getInvitationSetting();
			int invitationJiuCoin = invitationSetting == null ? 0
					: invitationJiuCoin(i, invitationSetting.getEachObtain(), invitationSetting.getMaxCountCycle(),
							invitationSetting.getInvitationCycle(), invitationSetting.getReturnCycle(),
							invitationSetting.getMaxJiuCoinReturnCycle());

			map.put("registerJiuCoin", registerJiuCoin);
			map.put("scanJiuCoin", scanJiuCoin);
			map.put("promoteShareJiuCoin", promoteShareJiuCoin);
			map.put("signInJiuCoin", signInJiuCoin);
			map.put("giftBag", giftBag);
			map.put("invitationJiuCoin", invitationJiuCoin);
			int subTotal = 0;
			if (scanJiuCoin == -1 || promoteShareJiuCoin == -1 || invitationJiuCoin == -1) {
				subTotal = -1;
			} else {
				subTotal = registerJiuCoin + scanJiuCoin + promoteShareJiuCoin + signInJiuCoin + giftBag
						+ invitationJiuCoin;
			}

			map.put("subTotal", subTotal);

			totalRegisterJiuCoin += registerJiuCoin;

			if (scanJiuCoin == -1) {
				totalScanJiuCoin = -1;
			} else if (totalScanJiuCoin != -1) {
				totalScanJiuCoin += scanJiuCoin;
			}

			if (promoteShareJiuCoin == -1) {
				totalPromoteShareJiuCoin = -1;
			} else if (totalPromoteShareJiuCoin != -1) {
				totalPromoteShareJiuCoin += promoteShareJiuCoin;
			}

			totalSignInJiuCoin += signInJiuCoin;
			totalGiftBag += giftBag;

			if (invitationJiuCoin == -1) {
				totalInvitationJiuCoin = -1;
			} else if (totalInvitationJiuCoin != -1) {
				totalInvitationJiuCoin += invitationJiuCoin;
			}

			if (subTotal == -1) {
				totalSubTotal = -1;
			} else if (totalSubTotal != -1) {
				totalSubTotal += subTotal;
			}

			limitValues.add(map);
		}

		HashMap<String, Integer> totalMap = new HashMap<String, Integer>();
		totalMap.put("registerJiuCoin", totalRegisterJiuCoin);
		totalMap.put("scanJiuCoin", totalScanJiuCoin);
		totalMap.put("promoteShareJiuCoin", totalPromoteShareJiuCoin);
		totalMap.put("signInJiuCoin", totalSignInJiuCoin);
		totalMap.put("giftBag", totalGiftBag);
		totalMap.put("invitationJiuCoin", totalInvitationJiuCoin);
		totalMap.put("subTotal", totalSubTotal);

		limitValues.add(totalMap);

		return limitValues;
	}

	/**
	 * 注册玖币极限值
	 */
	private int registerJiuCoin(int day, int jiuCoin) {
		return day == 1 ? jiuCoin : 0;
	}

	/**
	 * 门店扫码玖币极限值
	 */
	private int scanJiuCoin(int day, int eachObtain, int maxCountEachCycle, int obtainCycle) {
		if (maxCountEachCycle == -1) { // 无限制
			return -1;
		}
		// 周期内玖币值
		if (obtainCycle == 1) {
			return eachObtain * maxCountEachCycle;
		}
		return day % obtainCycle == 1 ? eachObtain * maxCountEachCycle : 0;
	}

	/**
	 * 推广分享玖币极限值
	 */
	private int promoteShareJiuCoin(int day, int eachShareObtain, int maxCountEachShareCycle, int shareCycle,
			int eachClickObtain, int maxCountEachClickCycle, int clickCycle) {
		if (maxCountEachClickCycle == -1 || maxCountEachShareCycle == -1) {
			return -1;
		}
		// 分享链接玖币
		int shareJiuCoin = 0;
		if (shareCycle == 1) {
			shareJiuCoin = eachShareObtain * maxCountEachShareCycle;
		} else {
			shareJiuCoin = day % shareCycle == 1 ? eachShareObtain * maxCountEachShareCycle : 0;
		}

		// 点击链接玖币
		int promoteJiuCoin = 0;
		if (clickCycle == 1) {
			promoteJiuCoin = eachClickObtain * maxCountEachClickCycle;
		} else {
			promoteJiuCoin = day % clickCycle == 1 ? eachClickObtain * maxCountEachClickCycle : 0;
		}

		return shareJiuCoin + promoteJiuCoin;
	}

	/**
	 * 签到玖币极限值
	 */
	private int signInJiuCoin(int day, int dailyObtain, List<ContinuousSetting> settings) {
		if (day == 1)
			return dailyObtain;

		if (settings == null || settings.size() == 0)
			return dailyObtain;

		sortContinuousSettings(settings);

		// 判断当前天数符合其中某一条规则
		int targetIndex = -1;
		for (int i = 0; i < settings.size(); i++) {
			if (day == settings.get(i).getDays()) {
				targetIndex = i;
				break;
			}
			if (day < settings.get(i).getDays()) {
				targetIndex = i - 1;
				break;
			}
			targetIndex = i;
		}

		if (targetIndex == -1) { // 该天数不符合任何一项规则
			return dailyObtain;
		}
		return settings.get(targetIndex).getExtraJiuCoin() + dailyObtain;
	}

	// 将签到规则按天数从低到高排序
	private void sortContinuousSettings(List<ContinuousSetting> settings) {
		ContinuousSetting tmp;
		for (int i = 0; i < settings.size(); i++) {
			for (int j = i; j < settings.size(); j++) {
				if (settings.get(i).getDays() > settings.get(j).getDays()) {
					tmp = settings.get(i);
					settings.set(i, settings.get(j));
					settings.set(j, tmp);
				}
			}
		}
	}

	/**
	 * 每月礼包玖币极限值
	 */
	private int giftBagJiuCoin(int day, int giftBag) {
		return day == 1 ? giftBag : 0;
	}

	/**
	 * 邀请有礼玖币极限值
	 */
	private int invitationJiuCoin(int day, int eachObtain, int maxCountCycle, int invitationCycle, int returnCycle,
			int maxJiuCoinReturnCycle) {
		if (maxCountCycle == -1 || maxJiuCoinReturnCycle == -1) {
			return -1;
		}
		// 邀请用户获得的玖币
		int invitationJiuCoin = 0;
		if (invitationCycle == 1) {
			invitationJiuCoin = eachObtain * maxCountCycle;
		} else {
			invitationJiuCoin = day % invitationCycle == 1 ? eachObtain * maxCountCycle : 0;
		}
		// 被邀请人下单后返还的玖币
		int returnJiuCoin = 0;
		if (returnCycle == 1) {
			returnJiuCoin = maxJiuCoinReturnCycle;
		} else {
			returnJiuCoin = day % returnCycle == 1 ? maxJiuCoinReturnCycle : 0;
		}
		return invitationJiuCoin + returnJiuCoin;
	}

	public JSONObject getSettingByStringPropertyName(String propertyName) {
		String value = globalSettingDao.getSettingByStringPropertyName(propertyName);
		return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
	}
}
