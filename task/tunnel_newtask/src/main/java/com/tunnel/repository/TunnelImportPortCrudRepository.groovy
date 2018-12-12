package com.tunnel.repository

import com.tunnel.domain.TunnelImportPort
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelImportPortCrudRepository extends CrudRepository<TunnelImportPort, Long> {
    TunnelImportPortCrudRepository findByTunnelNumber(String tunnelNumber)

}
