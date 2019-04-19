package com.yujj.ext.spring.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class ListRowMapper<T> implements RowMapper<List<T>> {

    private Class<T> elementType;

    public ListRowMapper(Class<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public List<T> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        List<T> result = new ArrayList<T>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            @SuppressWarnings("unchecked")
            T obj = (T) JdbcUtils.getResultSetValue(rs, i, elementType);
            result.add(obj);
        }
        return result;
    }
}
