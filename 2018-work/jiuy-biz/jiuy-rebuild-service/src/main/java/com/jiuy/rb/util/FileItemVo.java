package com.jiuy.rb.util;

import lombok.Data;

import java.io.InputStream;

/**
 * 文件上传实体
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 15:00
 * @Copyright 玖远网络
 */
@Data
public class FileItemVo {


    private String oldName;

    private String newName;

    private InputStream inputStream;

    private Long size;

}
