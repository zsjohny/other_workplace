package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.ExpressSupplier;

@DBMaster
public interface ShopExpressSupplierMapper {

    
    ExpressSupplier getExpressSupplierByEngName(@Param("engName") String engName);
    
    List<ExpressSupplier> getExpressSupplierList(@Param("status") int status);

}
