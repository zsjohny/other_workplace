package com.tunnel.repository

import com.tunnel.domain.ShapeRiskExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = ShapeRiskExamine.class, idClass = Long.class)
interface ShapeRiskExamineRepository {

@Query(value = "select  * from shape_risk_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<ShapeRiskExamine> findShapeRiskExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from shape_risk_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findShapeRiskExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
