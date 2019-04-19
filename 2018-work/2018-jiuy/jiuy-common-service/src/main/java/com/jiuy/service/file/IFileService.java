package com.jiuy.service.file;

import com.jiuy.base.model.UserSession;
import com.jiuy.model.file.FileList;
import com.jiuy.model.file.FileUtilVo;

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
    List<FileList> upload2Oss(FileUtilVo fileUtilVo,UserSession userSession) ;

    /**
     * 多个文件上传..上传到oss 且存放到數據庫中
     *
     * @param fileUtilVo 文件上传的参数封装
     * @author Aison
     * @date 2018/6/11 9:59
     */
    List<FileList> upload2OssFile(FileUtilVo fileUtilVo, UserSession userSession);
}
