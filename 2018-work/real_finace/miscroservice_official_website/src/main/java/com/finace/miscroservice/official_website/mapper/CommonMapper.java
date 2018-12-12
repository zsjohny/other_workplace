package com.finace.miscroservice.official_website.mapper;

import com.finace.miscroservice.official_website.entity.response.ImageType;
import com.finace.miscroservice.official_website.entity.response.MsgTypeResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommonMapper {



    List<ImageType> imageType(@Param("code") Integer code);

    List<MsgTypeResponse> newsCenter(@Param("code") Integer code);
}
