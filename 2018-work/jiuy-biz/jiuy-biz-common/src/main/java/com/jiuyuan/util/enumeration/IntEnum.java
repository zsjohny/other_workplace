package com.jiuyuan.util.enumeration;

import com.jiuyuan.ext.mybatis.IntEnumTypeHandler;
import com.jiuyuan.ext.mybatis.RegisterTypeHandler;

@RegisterTypeHandler(IntEnumTypeHandler.class)
public interface IntEnum {

    int getIntValue();
}
