package com.tunnel.repository

import com.tunnel.domain.TrafficAccidentRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface TrafficAccidentRiskExamineCrudRepository extends CrudRepository<TrafficAccidentRiskExamine, Long> {
TrafficAccidentRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
