package com.finace.miscroservice.borrow.service.impl;

import com.finace.miscroservice.borrow.dao.ErrorLogDao;
import com.finace.miscroservice.borrow.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    @Autowired
    private ErrorLogDao errorLogDao;






}
