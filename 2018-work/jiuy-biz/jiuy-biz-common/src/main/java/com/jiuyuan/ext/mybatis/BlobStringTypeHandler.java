package com.jiuyuan.ext.mybatis;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.yujj.util.StringUtil;



public class BlobStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
        throws SQLException {
        byte[] bytes = StringUtil.getBytes(parameter, "UTF-8");
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ps.setBinaryStream(i, bis, bytes.length);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj == null) {
            return null;
        } else {
            byte[] value = rs.getBytes(columnName);
            return StringUtil.newString(value, "UTF-8");
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object obj = cs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            byte[] value = cs.getBytes(columnIndex);
            return StringUtil.newString(value, "UTF-8");
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else {
            byte[] value = rs.getBytes(columnIndex);
            return StringUtil.newString(value, "UTF-8");
        }
    }

}
