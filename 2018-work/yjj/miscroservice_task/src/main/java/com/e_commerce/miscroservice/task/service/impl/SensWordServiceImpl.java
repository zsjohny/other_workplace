package com.e_commerce.miscroservice.task.service.impl;

import com.e_commerce.miscroservice.task.mapper.SensWordMapper;
import com.e_commerce.miscroservice.task.service.SensWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/29 10:45
 */
@Service
public class SensWordServiceImpl implements SensWordService {

    @Autowired
    private SensWordMapper sensWordMapper;

    @Override
    public List<String> findAll() {
        return sensWordMapper.findAll();
    }
}
