package com.tunnel.dao

import com.tunnel.domain.TunnelExamineRelationQuery
import com.tunnel.util.BaseDomain

/**
 * Created by nessary on 16-10-9.
 */
interface BaseDomainDao {

    void saveTunnelExmaineRelationQuery(TunnelExamineRelationQuery tunnelExamineRelationQuery)

    void saveBaseDomain(BaseDomain baseDomain)

    void saveBaseDomainAll(List<BaseDomain> baseDomains)

    BaseDomain findBaseDomainByBaseDomain(BaseDomain baseDomain)

    List<BaseDomain> findBaseDomainALlByPage(BaseDomain baseDomain)

    void updateBaseDomainByBaseDomain(BaseDomain baseDomain)

    void deleteBaseDomainById(BaseDomain baseDomain)

    Integer findBaseDomainCountByBaseDomain(BaseDomain baseDomain)
    List<BaseDomain> findBaseDomainALl(BaseDomain baseDomain)

}
