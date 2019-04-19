package com.jiuy.core.dao.mapper;

import com.jiuyuan.entity.BinaryData;

public interface BinaryDataDao {

	int add(BinaryData binaryData);

	BinaryData getById(long id);

	BinaryData getWaterMark();

}
