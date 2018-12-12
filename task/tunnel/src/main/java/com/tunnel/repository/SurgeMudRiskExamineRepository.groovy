package com.tunnel.repository

import com.tunnel.domain.SurgeMudRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = SurgeMudRiskExamine.class, idClass = Long.class)
interface SurgeMudRiskExamineRepository {

@Query(value = "select  * from surge_mud_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<SurgeMudRiskExamine> findSurgeMudRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from surge_mud_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findSurgeMudRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
