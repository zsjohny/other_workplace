package com.tunnel.repository

import com.tunnel.domain.FireRiskExmaine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface FireRiskExmaineCrudRepository extends CrudRepository<FireRiskExmaine, Long> {
FireRiskExmaineCrudRepository findByTunnelNumber(String tunnelNumber)

}
