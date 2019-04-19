package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.ShareRelation;

/**
 * 分享关系的dao
 */
public interface ShareRelationDao {

    /**
     * 保存分享关系
     *
     * @param relation 分享关系实体类
     */
    void save(ShareRelation relation);


    /**
     * 通过被分享者Id获取分享者信息
     *
     * @param sharedId 被分享者的Id
     * @return
     */
    ShareRelation findOneBySharedId(Long sharedId);
}
