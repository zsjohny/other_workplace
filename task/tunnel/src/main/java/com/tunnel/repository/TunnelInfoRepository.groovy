package com.tunnel.repository

import com.tunnel.domain.TunnelInfo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.data.repository.query.Param

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelInfoRepository extends Repository<TunnelInfo, Long> {


    @Query(value = "select  * from tunnel_info  limit :page ,:pageSize  ", nativeQuery = true)
    List<TunnelInfo> findTunnelInfoALl(@Param("page") Integer page, @Param("pageSize") Integer pageSize);


    @Query(value = "select  * from tunnel_info  where tunnelNumber like %:tunnelNumber% limit :page ,:pageSize  ", nativeQuery = true)
    List<TunnelInfo> findTunnelInfoALlByNumberAndPage(
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize, @Param("tunnelNumber") String tunnelNumber);


    @Query(value = "select  * from tunnel_info  where projectNumber like %:projectNumber%  ", nativeQuery = true)
    List<TunnelInfo> findTunnelInfoALlByNumber(@Param("projectNumber") String projectNumber);

    @Query(value = "select  count(id) from tunnel_info  where tunnelNumber like %:tunnelNumber%   ", nativeQuery = true)
    Integer findTunnelInfoCountALlByNumberAndPage(@Param("tunnelNumber") String tunnelNumber);


}
