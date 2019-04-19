package com.jiuyuan.util.enumeration;

import com.jiuyuan.ext.mybatis.RegisterTypeHandler;
import com.jiuyuan.ext.mybatis.StringEnumTypeHandler;

@RegisterTypeHandler(StringEnumTypeHandler.class)
public interface StringEnum {

    String getStringValue();
}
