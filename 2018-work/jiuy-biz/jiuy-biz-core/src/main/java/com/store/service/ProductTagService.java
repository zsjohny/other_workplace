package com.store.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.product.ProductTagVO;
import com.store.dao.mapper.ProductTagMapper;


@Service
public class ProductTagService {

    @Autowired
    private ProductTagMapper productTagMapper;

	/**
	 * @author DongZhong
	 */
	public Map<Long, ProductTagVO> getProductTagNames() {
		// TODO Auto-generated method stub
		return productTagMapper.getProductTagNames();
	}

}
