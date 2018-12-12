package com.tunnel.repository

import com.tunnel.domain.FireRiskExmaine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = FireRiskExmaine.class, idClass = Long.class)
interface FireRiskExmaineRepository {

@Query(value = "select  * from fire_risk_exmaine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<FireRiskExmaine> findFireRiskExmaineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from fire_risk_exmaine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findFireRiskExmaineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
