package com.jiuy.rb.service.common;

import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.common.FileListRb;
import com.jiuy.rb.util.FileUtilVo;

import java.util.List;


/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 10:08
 * @Copyright 玖远网络
 */
public interface IFileService {

    /**
     * 多个文件上传..上传到oss
     *
     * @param fileUtilVo 文件上传的参数封装类
     * @author Aison
     * @date 2018/6/11 9:59
     */
    List<FileListRb> upload2Oss(FileUtilVo fileUtilVo, UserSession userSession) ;

    /**
     * 多个文件上传..上传到oss 且存放到數據庫中
     *
     * @param fileUtilVo 文件上传的参数封装
     * @author Aison
     * @date 2018/6/11 9:59
     */
    List<FileListRb> upload2OssFile(FileUtilVo fileUtilVo, UserSession userSession);
}
