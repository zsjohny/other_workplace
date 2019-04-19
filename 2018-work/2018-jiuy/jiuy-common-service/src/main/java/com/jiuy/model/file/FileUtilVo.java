package com.jiuy.model.file;

import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件上传的参数封装类
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 13:37
 * @Copyright 玖远网络
 */
@Data
public class FileUtilVo {

    /**
     * 工厂方法
     * @param request request
     * @param postfix 后缀
     * @param folderName 文件名称
     * @param size  大小
     * @param ossConfig oss参数
     * @author Aison
     * @date 2018/6/11 15:02
     */
    public static FileUtilVo instance(HttpServletRequest request,String[] postfix,String folderName,Long size,Integer position,ImgType imgType,OssConfigVo ossConfig) {

        size = size == null ? 1024 * 1024L :size * 1024 *1024L;
        FileUtilVo fileUtilVo = new FileUtilVo();
        fileUtilVo.setFolderName(folderName);
        fileUtilVo.setPostfix(postfix);
        fileUtilVo.setSize(size);
        fileUtilVo.setOssConfig(ossConfig);
        fileUtilVo.setPosition(position);
        fileUtilVo.setImgType(imgType);
        fileUtilVo.setFileItems(fileUtilVo.transform(request));
        return fileUtilVo;
    }

    /**
     * 文件后缀
     */
    private String[] postfix;
    /**
     * 文件夹名称
     */
    private String folderName;
    /**
     * 允许大小
     */
    private Long size;
    /**
     * oss配置信息
     */
    private OssConfigVo ossConfig;
    /**
     * 上传的实体
     */
    private List<FileItemVo> fileItems;
    /**
     * 文件类型 1:商品,2:头像,3:
     */
    private ImgType imgType;
    /**
     * 文件后缀
     */
    private Integer position;

    /**
     * 转好换 如果上微服务..service层中不能访问request对象 把流传过去
     * @param request request
     * @author Aison
     * @date 2018/6/11 14:45
     */
    private List<FileItemVo> transform(HttpServletRequest request)  {

        List<FileItemVo> fileItems = new ArrayList<>(5);
        String myFileName ;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()) {
                FileItemVo fileItem = new FileItemVo();
                // 记录上传过程起始时的时间，用来计算上传时间
                String pre = Biz.getUuid();
                MultipartFile file = multiRequest.getFile(iterator.next());
                Long fileSize = file.getSize();
                if (fileSize > size) {
                    throw new BizException(GlobalsEnums.FILE_TOO_BIG);
                }
                // 取得当前上传文件的文件名称
                myFileName = file.getOriginalFilename();
                // 如果文件名为空 或者不允许上传
                if (Biz.hasEmpty(myFileName) || !checkFix(myFileName.trim(), postfix)) {
                    throw  BizException.def().msg("文件后缀不合法");
                }

                String newFileName = pre + myFileName.substring(myFileName.lastIndexOf("."), myFileName.length());
                try {
                    fileItem.setInputStream(file.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileItem.setNewName(newFileName);
                fileItem.setOldName(myFileName);
                fileItem.setSize(fileSize);
                fileItems.add(fileItem);
            }
        }
        return fileItems;
    }


    /**
     * 检查文件后缀
     * @param fileName 文件名称
     * @param args 后缀集合
     * @author Aison
     * @date 2018/6/11 10:00
     */
    private boolean checkFix(String fileName, String[] args) {
        if (args == null || args.length == 0) {
            return true;
        }
        String fileExtension = fileName.contains (".") ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).trim() : "";
        if ("".equals(fileExtension)){
            return false;
        }
        for (String ext : args) {
            if (ext.trim().equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}




