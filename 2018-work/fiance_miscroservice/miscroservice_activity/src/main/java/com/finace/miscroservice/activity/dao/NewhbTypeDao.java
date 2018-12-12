package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.NewhbTypePO;

import java.util.List;

/**
 * 福利券类型Dao层
 */
public interface NewhbTypeDao {


    /**
     * 根据福利券类型获取福利券
     * @param hbtype
     * @return
     */
    public List<NewhbTypePO> getNewhbTypeByHbType(String hbtype);

    /**
     * 根据福利券id获取福利券
     * @param hblxid
     * @return
     */
    public  NewhbTypePO getNewhbTypeByLxId(String hblxid);
}
