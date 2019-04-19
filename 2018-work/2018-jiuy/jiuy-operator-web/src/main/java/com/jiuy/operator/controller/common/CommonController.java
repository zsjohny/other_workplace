package com.jiuy.operator.controller.common;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.model.file.FileList;
import com.jiuy.model.file.FileUtilVo;
import com.jiuy.model.file.ImgType;
import com.jiuy.model.file.OssConfigVo;
import com.jiuy.service.file.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通用的controller
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 9:46
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/admin")
public class CommonController {


    private final IFileService fileService;

    private final OssConfigVo ossConfig;

    @Autowired
    public CommonController(IFileService fileService, OssConfigVo ossConfig) {
        this.fileService = fileService;
        this.ossConfig = ossConfig;
    }

    /**
     * 上传文件
     * @author Aison
     * @date 2018/6/11 9:48
     */
    @RequestMapping("upload")
    public ResponseResult upload(HttpServletRequest request) {

        FileUtilVo fileUtilVo =  FileUtilVo.instance(request,null,"product/",5L,1,ImgType.USER_AVATAR,ossConfig);
        List<FileList> fileLists = fileService.upload2OssFile(fileUtilVo,UserSession.getUserSession());
        return ResponseResult.instance().success().setData(fileLists);
    }

}
