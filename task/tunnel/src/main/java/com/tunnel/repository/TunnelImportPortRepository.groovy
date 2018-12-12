package com.tunnel.repository

import com.tunnel.domain.TunnelImportPort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/19.
 */
@RepositoryDefinition(domainClass = TunnelImportPort.class, idClass = Long.class)
interface TunnelImportPortRepository {

    @Query(value = "select  * from tunnel_import_port  where tunnelNumber =:tunnelNumber limit :page ,:pageSize  ", nativeQuery = true)
    List<TunnelImportPort> findTunnelImportPortALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


    @Query(value = "select  count(id) from tunnel_import_port  where tunnelNumber =:tunnelNumber  ", nativeQuery = true)
    Integer findTunnelImportPortCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}