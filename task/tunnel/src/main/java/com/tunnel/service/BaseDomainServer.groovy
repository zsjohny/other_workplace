package com.tunnel.service

import com.tunnel.domain.TunnelExamineRelationQuery
import com.tunnel.util.BaseDomain

/**
 * Created by Ness on 2016/10/8.
 */
interface BaseDomainServer {

    void saveTunnelExmaineRelationQuery(TunnelExamineRelationQuery tunnelExamineRelationQuery)

    void saveBaseDomain(BaseDomain baseDomain)
    void saveBaseDomainAll(List<BaseDomain> baseDomains)

    BaseDomain findBaseDomainByBaseDomain(BaseDomain baseDomain)

    List<BaseDomain> findBaseDomainALlByPage(BaseDomain baseDomain)

    List<BaseDomain> findBaseDomainALl(BaseDomain baseDomain)

    void updateBaseDomainByBaseDomain(BaseDomain baseDomain)

    void deleteBaseDomainById(BaseDomain baseDomain)

    Integer findBaseDomainCountByBaseDomain(BaseDomain baseDomain)

}
