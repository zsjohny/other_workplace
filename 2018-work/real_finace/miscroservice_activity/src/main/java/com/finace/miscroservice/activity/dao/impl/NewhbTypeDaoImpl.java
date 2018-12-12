package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.NewhbTypeDao;
import com.finace.miscroservice.activity.mapper.NewhbTypeMapper;
import com.finace.miscroservice.activity.po.NewhbTypePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 福利券类型dao实现类
 */
@Component
public class NewhbTypeDaoImpl implements NewhbTypeDao {

    @Autowired
    private NewhbTypeMapper newhbTypeMapper;

    @Override
    public List<NewhbTypePO> getNewhbTypeByHbType(String hbtype) {
        return newhbTypeMapper.getNewhbTypeByHbType(hbtype);
    }

    @Override
    public NewhbTypePO getNewhbTypeByLxId(String hblxid) {
        return newhbTypeMapper.getNewhbTypeByLxId(hblxid);
    }
}
