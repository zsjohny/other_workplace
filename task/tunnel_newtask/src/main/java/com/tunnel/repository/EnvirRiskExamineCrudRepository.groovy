package com.tunnel.repository

import com.tunnel.domain.EnvirRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface EnvirRiskExamineCrudRepository extends CrudRepository<EnvirRiskExamine, Long> {
EnvirRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
