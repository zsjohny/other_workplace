/**
 * 
 */
package com.jiuyuan.util.ha.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author LWS
 *
 */
public class DynamicRouteDataSource extends AbstractRoutingDataSource{

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

}
