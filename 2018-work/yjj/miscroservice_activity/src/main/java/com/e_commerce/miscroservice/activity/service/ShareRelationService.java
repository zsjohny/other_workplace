package com.e_commerce.miscroservice.activity.service;

/**
 * 分享关系的Service
 */
public interface ShareRelationService {

    /**
     * 保存分享关系
     *
     * @param sharedId 被分享者Id
     * @param sharerId 分享者Id
     */
    void save(Long sharedId, Long sharerId);


    /**
     * 通过被分享者Id获取分享者Id
     *
     * @param sharedId 被分享者的Id
     * @return
     */
    Long findShareIdBySharedId(Long sharedId);
}
