package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.logistics.LOLocation;


public interface LOLocationDao {

	List<LOLocation> search(int type);

    long OnDuplicateKeyUpd(LOLocation lOLocation);

	LOLocation getById(long deliveryLocation);
}
