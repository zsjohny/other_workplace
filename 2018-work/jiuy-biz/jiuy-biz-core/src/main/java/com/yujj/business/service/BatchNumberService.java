package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.BatchNumber;
import com.yujj.dao.mapper.BatchNumberMapper;

@Service
public class BatchNumberService {

    @Autowired
    private BatchNumberMapper batchNumberMapper;

    public BatchNumber getBatchNumber(String batchNo, String supplierCode, int innerCode) {
        return batchNumberMapper.getBatchNumber(batchNo, supplierCode, innerCode);
    }

	public BatchNumber getBatchNumberPatch(String batchNo, String supplierCode, int innerCode) {
		return batchNumberMapper.getBatchNumberPatch(batchNo, supplierCode, innerCode);
	}

}
