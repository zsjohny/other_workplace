package com.songxm.credit.dao.credit.deversion.domain;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by sxm on 17/3/25.
 */
@Data
public abstract class BaseDO {
    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 最后修改人
     */
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * 最后修改时间
     */
    @Column(name = "updated_at")
    private Date updatedAt;
}
