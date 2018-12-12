package com.tunnel.repository

import com.tunnel.domain.EnvirRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = EnvirRiskExamine.class, idClass = Long.class)
interface EnvirRiskExamineRepository {

@Query(value = "select  * from envir_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<EnvirRiskExamine> findEnvirRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from envir_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findEnvirRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
