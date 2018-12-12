package com.tunnel.repository

import com.tunnel.domain.GasRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface GasRiskExamineCrudRepository extends CrudRepository<GasRiskExamine, Long> {
GasRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
