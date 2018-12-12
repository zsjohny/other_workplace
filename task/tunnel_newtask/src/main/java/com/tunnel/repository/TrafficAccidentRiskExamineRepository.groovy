package com.tunnel.repository

import com.tunnel.domain.TrafficAccidentRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = TrafficAccidentRiskExamine.class, idClass = Long.class)
interface TrafficAccidentRiskExamineRepository {

@Query(value = "select  * from traffic_accident_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<TrafficAccidentRiskExamine> findTrafficAccidentRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from traffic_accident_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findTrafficAccidentRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
