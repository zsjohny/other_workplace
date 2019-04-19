package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.DiscountInfoDao;
import com.jiuyuan.constant.DiscountType;
import com.jiuyuan.entity.shopping.DiscountInfo;

@Service
public class DiscountInfoServiceImpl implements DiscountInfoService {

    @Autowired
    private DiscountInfoDao discountInfoDao;

    @Override
    public Map<Long, List<DiscountInfo>> discountOfRelatedIdMap(int type) {
        List<DiscountInfo> discountInfos = discountInfoDao.discountsOfType(type);
        Map<Long, List<DiscountInfo>> discountInfosById = new HashMap<Long, List<DiscountInfo>>();

        long lastRelatedId = -1;
        List<DiscountInfo> discountInfos2 = null;
        for (DiscountInfo discountInfo : discountInfos) {
            long relatedId = discountInfo.getRelatedId();
            if (lastRelatedId != relatedId) {
                lastRelatedId = relatedId;
                discountInfos2 = new ArrayList<DiscountInfo>();
                discountInfosById.put(lastRelatedId, discountInfos2);
            }
            discountInfos2.add(discountInfo);
        }

        return discountInfosById;
    }

	@Override
	public int batchAdd(Collection<DiscountInfo> discounts) {
		if(discounts.size() < 1) {
			return 0;
		}
		
		return discountInfoDao.batchAdd(discounts);
	}

	@Override
	public int batchDelete(List<DiscountInfo> discounts) {
		if(discounts.size() < 1) {
			return 0;
		}
		
		return discountInfoDao.batchDelete(discounts);
	}

	@Override
	public Map<Long, List<DiscountInfo>> itemsOfRelatedIdTypeMap(int type, Collection<Long> relativeIds) {
		if(relativeIds.size() < 1) {
			return new HashMap<Long, List<DiscountInfo>>();
		}
		
		List<DiscountInfo> discountInfos = discountInfoDao.itemsOfRelatedIdType(DiscountType.CATEGORY.getIntValue(), relativeIds);
		
		Map<Long, List<DiscountInfo>> discountsMap = new HashMap<Long, List<DiscountInfo>>();
		long lastRelatedId = 0;
		List<DiscountInfo> discounts = null;
		for(DiscountInfo discountInfo : discountInfos) {
			long relatedId = discountInfo.getRelatedId();
			if(lastRelatedId != relatedId) {
				lastRelatedId = relatedId;
				discounts = new ArrayList<DiscountInfo>();
				discountsMap.put(lastRelatedId, discounts);
			}
			discounts.add(discountInfo);
		}
		
		return discountsMap;
	}

}
