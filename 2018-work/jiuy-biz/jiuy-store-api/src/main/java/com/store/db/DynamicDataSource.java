package com.store.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return HandleDataSource.getDataSource();
    }

    public void setPutAll(Map<Object, Object> map) {
        HandleDataSource.putAll(map);
    }
}
