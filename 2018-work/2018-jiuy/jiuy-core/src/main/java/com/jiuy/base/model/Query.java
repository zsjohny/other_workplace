package com.jiuy.base.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jiuy.base.annotation.IgnoreCopy;
import lombok.Data;

/**
 * 查询类
 *
 * @author Aison
 * @date 2017年11月23日 下午11:00:51
 */
@Data
public class Query {

    @IgnoreCopy
    @JsonIgnoreProperties
    private Integer limit;

    @IgnoreCopy
    @JsonIgnoreProperties
    private Integer offset;

    @IgnoreCopy
    @JsonIgnoreProperties
    private String orderBy;

    @IgnoreCopy
    @JsonIgnoreProperties
    private boolean justList;


}
