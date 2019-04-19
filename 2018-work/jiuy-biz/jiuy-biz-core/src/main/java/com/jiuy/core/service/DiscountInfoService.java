package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.shopping.DiscountInfo;

public interface DiscountInfoService {

    Map<Long, List<DiscountInfo>> discountOfRelatedIdMap(int intValue);

	int batchAdd(Collection<DiscountInfo> multipleDiscounts);

	int batchDelete(List<DiscountInfo> multipleDiscounts);

	Map<Long, List<DiscountInfo>> itemsOfRelatedIdTypeMap(int type, Collection<Long> categoryIds);

}
