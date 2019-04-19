package com.jiuy.rb.model.product; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * CategoryRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月14日 上午 10:00:53
 * @Copyright 玖远网络 
*/
@Data
public class CategoryRbQuery extends CategoryRb {

    /**
     * 未删除的
     */
    @JsonIgnoreProperties
    private Integer notDel;
    /**
     * 子类目
     */
    private List<CategoryRbQuery> children;
    /**
     * 最小权重
     */
    @JsonIgnoreProperties
    private String weightMin;
    /**
     * 最大权重
     */
    @JsonIgnoreProperties
    private String weightMax;

} 
