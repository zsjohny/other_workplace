package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.NewhbTypePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewhbTypeMapper {

    /**
     * 根据福利券类型获取福利券
     * @param hbtype
     * @return
     */
    List<NewhbTypePO> getNewhbTypeByHbType(@Param("hbtype") String hbtype);

    /**
     * 根据福利券id获取福利券
     * @param hblxid
     * @return
     */
    NewhbTypePO getNewhbTypeByLxId(@Param("hblxid") String hblxid);


}
