package com.tunnel.repository

import com.tunnel.domain.AssistTunnel
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = AssistTunnel.class, idClass = Long.class)
interface AssistTunnelRepository {

@Query(value = "select  * from assist_tunnel  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<AssistTunnel> findAssistTunnelALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from assist_tunnel  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findAssistTunnelCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


    @Modifying
    @Query("update AssistTunnel set acccessTunnelAndAirshaftRelationWithMainTunnel=:acccessTunnelAndAirshaftRelationWithMainTunnel where id=:id")
    void updateAcccessTunnelAndAirshaftRelationWithMainTunnelPictureByAcccessTunnelAndAirshaftRelationWithMainTunnel(@Param("acccessTunnelAndAirshaftRelationWithMainTunnel") String acccessTunnelAndAirshaftRelationWithMainTunnel, @Param("id") Long id)

}
