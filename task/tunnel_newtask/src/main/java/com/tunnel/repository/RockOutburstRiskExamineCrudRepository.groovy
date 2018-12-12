package com.tunnel.repository

import com.tunnel.domain.RockOutburstRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface RockOutburstRiskExamineCrudRepository extends CrudRepository<RockOutburstRiskExamine, Long> {
RockOutburstRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
