package com.tunnel.repository

import com.tunnel.domain.OverBreakRiskExmaine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface OverBreakRiskExmaineCrudRepository extends CrudRepository<OverBreakRiskExmaine, Long> {
OverBreakRiskExmaineCrudRepository findByTunnelNumber(String tunnelNumber)

}
