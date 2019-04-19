package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.shopping.DiscountInfo;

public interface DiscountInfoDao {

    int batchAdd(Collection<DiscountInfo> multipleDiscounts);

    int delete(int type, long relatedId);

    List<DiscountInfo> getDiscount(int type, long relatedId);

    List<DiscountInfo> discountsOfType(int type);

	int batchDelete(List<DiscountInfo> discounts);

	List<DiscountInfo> itemsOfRelatedIdType(int type, Collection<Long> relatedIds);

}
