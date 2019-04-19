package com.e_commerce.miscroservice.task.entity;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/29 10:52
 */
@Table(value = "t_sens_word", commit = "敏感词库")
public class SensWord {

    @Id
    private Long id;

    @Column(value = "value", commit = "敏感词", length = 50, isNUll = false)
    private String value;

    private String vaees;

}
