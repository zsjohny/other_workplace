package com.store.entity;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.store.enumerate.IntEnum;

public class IntEnumTypeHandler<E extends Enum<E> & IntEnum> extends BaseTypeHandler<E> {

    private Class<E> enumClass;

    public IntEnumTypeHandler(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getIntValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj == null) {
            return null;
        } else {
            int value = rs.getInt(columnName);
            return IntEnumCache.getEnumValue(enumClass, value);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object obj = cs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            int value = cs.getInt(columnIndex);
            return IntEnumCache.getEnumValue(enumClass, value);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            int value = rs.getInt(columnIndex);
            return IntEnumCache.getEnumValue(enumClass, value);
        }
    }
}
