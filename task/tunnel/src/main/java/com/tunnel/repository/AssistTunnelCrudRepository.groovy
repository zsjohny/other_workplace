package com.tunnel.repository

import com.tunnel.domain.AssistTunnel
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface AssistTunnelCrudRepository extends CrudRepository<AssistTunnel, Long> {
AssistTunnelCrudRepository findByTunnelNumber(String tunnelNumber)

}
