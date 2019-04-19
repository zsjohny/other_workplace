package com.e_commerce.miscroservice.distribution.service;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:01
 * @Copyright 玖远网络
 */
public interface DataDictionaryService{


    /**
     * 根据groupCode, code查找
     *
     * @param code code
     * @param groupCode groupCode
     * @return com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary
     * @author Charlie
     * @date 2018/10/11 10:02
     */
    DataDictionary findByCodeAndGroupCode(String code, String groupCode);



}
