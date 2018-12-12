package com.tunnel.repository

import com.tunnel.domain.OtherRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = OtherRiskExamine.class, idClass = Long.class)
interface OtherRiskExamineRepository {

@Query(value = "select  * from other_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<OtherRiskExamine> findOtherRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from other_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findOtherRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
