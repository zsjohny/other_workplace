package com.e_commerce.miscroservice.activity.service.impl;

import com.e_commerce.miscroservice.activity.dao.ShareRelationDao;
import com.e_commerce.miscroservice.activity.entity.ShareRelation;
import com.e_commerce.miscroservice.activity.service.ShareRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShareRelationServiceImpl implements ShareRelationService {

    @Autowired
    private ShareRelationDao shareRelationDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long sharedId, Long sharerId) {
        ShareRelation shareRelation = new ShareRelation();
        shareRelation.setSharedId(sharedId);
        shareRelation.setSharerId(sharerId);
        shareRelationDao.save(shareRelation);
    }


    @Override
    public Long findShareIdBySharedId(Long sharedId) {
        ShareRelation oneBySharedId = shareRelationDao.findOneBySharedId(sharedId);
        return oneBySharedId == null ? null : oneBySharedId.getSharerId();
    }
}

