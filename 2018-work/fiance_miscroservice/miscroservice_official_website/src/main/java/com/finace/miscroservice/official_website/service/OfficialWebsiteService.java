package com.finace.miscroservice.official_website.service;


import com.finace.miscroservice.commons.utils.Response;

/**
 * 标的service
 */
public interface OfficialWebsiteService {

    Response pcIndex();

    Response pcProductList(String isxs,Integer page,Integer time);

    Response pcProductDetail(Integer id);

    Response pcProductRecordes(Integer page,Integer borrorId);

    Response pcDataConllection();
}


















