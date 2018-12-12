package com.finace.miscroservice.official_website.service;

import com.finace.miscroservice.commons.utils.Response;

public interface CommonService {
    Response runReport(Integer page);

    Response activeCenter(Integer page);


    Response newsCenter(Integer page);
}
