package com.tunnel.repository

import com.tunnel.domain.TunnelHeadRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = TunnelHeadRiskExamine.class, idClass = Long.class)
interface TunnelHeadRiskExamineRepository {

@Query(value = "select  * from tunnel_head_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<TunnelHeadRiskExamine> findTunnelHeadRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from tunnel_head_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findTunnelHeadRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
