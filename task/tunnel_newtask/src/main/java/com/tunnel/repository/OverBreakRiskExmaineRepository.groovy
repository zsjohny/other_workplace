package com.tunnel.repository

import com.tunnel.domain.OverBreakRiskExmaine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = OverBreakRiskExmaine.class, idClass = Long.class)
interface OverBreakRiskExmaineRepository {

@Query(value = "select  * from over_break_risk_exmaine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<OverBreakRiskExmaine> findOverBreakRiskExmaineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from over_break_risk_exmaine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findOverBreakRiskExmaineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
