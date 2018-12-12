package com.tunnel.repository

import com.tunnel.domain.Figure
import org.springframework.data.repository.CrudRepository

/**
 * Created by Ness on 2016/10/11.
 */
interface FigureCrudRepository extends CrudRepository<Figure, Long> {
FigureCrudRepository findByTunnelNumber(String tunnelNumber)

}
