package com.tunnel.repository

import com.tunnel.domain.TunnelGrabageExamine
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = TunnelGrabageExamine.class, idClass = Long.class)
interface TunnelGrabageExamineRepository {

@Query(value = "select  * from tunnel_grabage_examine  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<TunnelGrabageExamine> findTunnelGrabageExamineALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from tunnel_grabage_examine  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findTunnelGrabageExamineCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
