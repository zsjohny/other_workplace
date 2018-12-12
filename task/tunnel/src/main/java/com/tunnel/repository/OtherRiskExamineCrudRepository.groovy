package com.tunnel.repository

import com.tunnel.domain.OtherRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface OtherRiskExamineCrudRepository extends CrudRepository<OtherRiskExamine, Long> {
OtherRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
