package com.tunnel.repository

import com.tunnel.domain.TunnelExamineRelationQuery
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/18.
 */
interface TunnelExmainRelationQueryCrudRepository extends CrudRepository<TunnelExamineRelationQuery, Integer> {

    TunnelExamineRelationQuery findByTunnelNumberAndTableName(String tunnelNumber, String tableName)

    List<TunnelExamineRelationQuery> findByTunnelNumber(String tunnelNumber)

}
