package com.tunnel.repository

import com.tunnel.domain.TunnelShallowCover
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelShallowCoverCrudRepository extends CrudRepository<TunnelShallowCover, Long> {
TunnelShallowCoverCrudRepository findByTunnelNumber(String tunnelNumber)

}
