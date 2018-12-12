package com.finace.miscroservice.official_website.entity.response;

import com.finace.miscroservice.official_website.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 运营报告
 */
@Data
@ToString
public class ImageType extends BaseEntity{
    private String name;  //标题
    private String jumurl;  //跳转链接
    private String imgurl;//图片地址
    private String addtime;//添加时间
}
