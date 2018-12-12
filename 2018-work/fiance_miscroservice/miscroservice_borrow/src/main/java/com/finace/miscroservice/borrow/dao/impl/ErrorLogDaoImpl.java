package com.finace.miscroservice.borrow.dao.impl;

import com.finace.miscroservice.borrow.dao.ErrorLogDao;
import com.finace.miscroservice.borrow.mapper.ErrorLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogDaoImpl implements ErrorLogDao {

    @Autowired
    private ErrorLogMapper errorLogMapper;









}
