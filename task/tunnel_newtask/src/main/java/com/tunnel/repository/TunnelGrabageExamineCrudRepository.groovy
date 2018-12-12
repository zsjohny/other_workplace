package com.tunnel.repository

import com.tunnel.domain.TunnelGrabageExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelGrabageExamineCrudRepository extends CrudRepository<TunnelGrabageExamine, Long> {
TunnelGrabageExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
