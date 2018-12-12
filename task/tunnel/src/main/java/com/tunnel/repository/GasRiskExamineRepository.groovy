package com.tunnel.repository

import com.tunnel.domain.GasRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = GasRiskExamine.class, idClass = Long.class)
interface GasRiskExamineRepository {

@Query(value = "select  * from gas_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<GasRiskExamine> findGasRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from gas_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findGasRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
