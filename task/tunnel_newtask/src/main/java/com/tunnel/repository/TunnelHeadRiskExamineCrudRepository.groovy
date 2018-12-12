package com.tunnel.repository

import com.tunnel.domain.TunnelHeadRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TunnelHeadRiskExamineCrudRepository extends CrudRepository<TunnelHeadRiskExamine, Long> {
TunnelHeadRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
