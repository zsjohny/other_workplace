package com.tunnel.repository

import com.tunnel.domain.TunnelShallowCover
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = TunnelShallowCover.class, idClass = Long.class)
interface TunnelShallowCoverRepository {

@Query(value = "select  * from tunnel_shallow_cover  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
List<TunnelShallowCover> findTunnelShallowCoverALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


@Query(value = "select  count(id) from tunnel_shallow_cover  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
Integer findTunnelShallowCoverCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
