package com.tunnel.repository

import com.tunnel.domain.TunnelInfo
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelInfoCrudRepository extends CrudRepository<TunnelInfo, Long> {
    TunnelInfo findByTunnelNumber(String tunnelNumber)
}
