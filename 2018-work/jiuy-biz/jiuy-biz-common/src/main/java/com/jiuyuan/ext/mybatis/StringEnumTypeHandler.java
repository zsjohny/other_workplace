package com.jiuyuan.ext.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.jiuyuan.util.enumeration.StringEnum;
import com.jiuyuan.util.enumeration.StringEnumCache;

public class StringEnumTypeHandler<E extends Enum<E> & StringEnum> extends BaseTypeHandler<E> {

    private Class<E> enumClass;

    public StringEnumTypeHandler(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getStringValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj == null) {
            return null;
        } else {
            String value = rs.getString(columnName);
            return StringEnumCache.getEnumValue(enumClass, value);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object obj = cs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            String value = cs.getString(columnIndex);
            return StringEnumCache.getEnumValue(enumClass, value);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            String value = rs.getString(columnIndex);
            return StringEnumCache.getEnumValue(enumClass, value);
        }
    }
}
