package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.ExpressSupplier;

@DBMaster
public interface ExpressSupplierMapper {

    
    ExpressSupplier getExpressSupplierByEngName(@Param("engName") String engName);
    
    List<ExpressSupplier> getExpressSupplierList(@Param("status") int status);

}
