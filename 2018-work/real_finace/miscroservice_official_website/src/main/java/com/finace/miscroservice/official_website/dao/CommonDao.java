package com.finace.miscroservice.official_website.dao;

import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.official_website.entity.response.ImageType;
import com.finace.miscroservice.official_website.entity.response.MsgTypeResponse;

import java.util.List;

public interface CommonDao {


    List<ImageType> imageType(Integer code);

    /**
     * 公告中心
     * @param code
     * @return
     */
    List<MsgTypeResponse> newsCenter(Integer code);
}
