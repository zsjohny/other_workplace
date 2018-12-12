package com.tunnel.repository

import com.tunnel.domain.ShapeRiskExamine
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface ShapeRiskExamineCrudRepository extends CrudRepository<ShapeRiskExamine, Long> {
ShapeRiskExamineCrudRepository findByTunnelNumber(String tunnelNumber)

}
