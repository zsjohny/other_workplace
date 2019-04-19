package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ShareRelationDao;
import com.e_commerce.miscroservice.activity.entity.ShareRelation;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.stereotype.Repository;

@Repository
public class ShareRelationDaoImpl implements ShareRelationDao {

    private Log log = Log.getInstance(ShareRelationDaoImpl.class);

    @Override
    public void save(ShareRelation relation) {

        if (relation == null || relation.wasEmpty()) {
            log.warn("保存分享关系,所传参数为空");
            return;
        }
        log.info("开始保存分享关系表{}", relation);

        ShareRelation one = MybatisOperaterUtil.getInstance().findOne(new ShareRelation(), new
                MybatisSqlWhereBuild(ShareRelation.class).eq(ShareRelation::getSharedId, relation.getSharedId()));


        if (one == null) {
            int save = MybatisOperaterUtil.getInstance().save(relation);
            if (save > 0) {
                log.info("保存成功分享关系表", relation);
            }
            {
                log.info("保存失败分享关系表", relation);
            }

        }


    }

    @Override
    public ShareRelation findOneBySharedId(Long sharedId) {
        if (sharedId == null) {
            log.warn("查询分享关系的被分享者id为空");
            return null;
        }
        log.info("开始查询被分享者={}的分享关系", sharedId);
        return MybatisOperaterUtil.getInstance().findOne(new ShareRelation(), new
                MybatisSqlWhereBuild(ShareRelation.class).eq(ShareRelation::getSharedId, sharedId));
    }
}

