package com.tunnel.repository

import com.tunnel.domain.RockOutburstRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = RockOutburstRiskExamine.class, idClass = Long.class)
interface RockOutburstRiskExamineRepository {

@Query(value = "select  * from rock_outburst_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<RockOutburstRiskExamine> findRockOutburstRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from rock_outburst_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findRockOutburstRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
