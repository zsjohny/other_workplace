package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.CreditLogDao;
import com.finace.miscroservice.activity.service.CreditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreditLogServiceImpl implements CreditLogService {


    @Autowired
    private CreditLogDao creditLogDao;


}
