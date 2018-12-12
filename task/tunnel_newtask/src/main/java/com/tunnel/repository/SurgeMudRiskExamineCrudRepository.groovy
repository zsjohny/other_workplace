package com.tunnel.repository

import com.tunnel.domain.SurgeMudRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface SurgeMudRiskExamineCrudRepository extends CrudRepository<SurgeMudRiskExamine, Long> {
SurgeMudRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
