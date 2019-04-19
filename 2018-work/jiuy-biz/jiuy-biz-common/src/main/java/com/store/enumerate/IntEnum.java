package com.store.enumerate;

import com.store.entity.IntEnumTypeHandler;
import com.store.entity.RegisterTypeHandler;

@RegisterTypeHandler(IntEnumTypeHandler.class)
public interface IntEnum {

    int getIntValue();
}
