package com.jiuy.service.file;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.exception.BizException;

import com.jiuy.base.model.UserSession;
import com.jiuy.base.util.Biz;
import com.jiuy.mapper.file.FileListMapper;
import com.jiuy.model.file.FileItemVo;
import com.jiuy.model.file.FileList;
import com.jiuy.model.file.FileUtilVo;
import com.jiuy.model.file.OssConfigVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件相关的service
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 10:08
 * @Copyright 玖远网络
 */
@Service("fileService")
@Log4j2
public class FileServiceImpl implements IFileService{


    private final FileListMapper fileListMapper;

    @Autowired
    public FileServiceImpl(FileListMapper fileListMapper) {
        this.fileListMapper = fileListMapper;
    }

    /**
     * 多个文件上传..上传到oss 且存放到數據庫中
     *
     * @param fileUtilVo 文件上传的参数封装
     * @author Aison
     * @date 2018/6/11 9:59
     */
    @Override
    public List<FileList> upload2OssFile(FileUtilVo fileUtilVo, UserSession userSession) {

        List<FileList> fileLists =  upload2Oss(fileUtilVo,userSession);
        fileListMapper.insertBach(fileLists);
        return fileLists;
    }

    /**
     * 多个文件上传..上传到oss
     *
     * @param fileUtilVo 文件上传的参数封装
     * @author Aison
     * @date 2018/6/11 9:59
     */
    @Override
    public List<FileList> upload2Oss(FileUtilVo fileUtilVo,UserSession userSession) {

        List<FileList> fileLists = new ArrayList<>(5);
        String folderName = fileUtilVo.getFolderName();
        folderName  = folderName == null ? "CourseFiles/" : folderName;
        List<FileItemVo> fileItems =  fileUtilVo.getFileItems();
        String finalFolderName = folderName;
        fileItems.forEach(action->{
           try{
               Long start = System.currentTimeMillis();
               Long fileSize = action.getSize();
               String fileName = action.getOldName();
               String  url = ossLoad(action.getInputStream(), finalFolderName + action.getNewName(),fileUtilVo.getOssConfig(),fileSize);
               FileList fileList = new FileList();
               fileList.setUrl(url);
               fileList.setSize(fileSize);
               fileList.setCreateTime(new Date());
               fileList.setFileName(fileName);
               fileList.setFileCode(Biz.getUuid());
               fileList.setUploadUserId(userSession.getUserId().toString());
               fileList.setUploadUserName(userSession.getUserName());
               fileList.setImgType(fileUtilVo.getImgType().getCode());
               fileList.setPosition(fileUtilVo.getPosition());
               fileLists.add(fileList);
               log.info("上传文件" + fileName + "完成...耗时" + (System.currentTimeMillis() - start) / 1000 + "s");
           }catch (Exception e) {
               e.printStackTrace();
               throw BizException.instance(GlobalsEnums.FILE_UPLOAD_FAILED);
           }

        });
        return fileLists;
    }


    /**
     * 上传文件到oss
     * @param in  文件流
     * @param name  文件名
     * @param ossConfig oss的配置信息
     * @author Aison
     * @date 2018/6/11 10:05
     */
    private String ossLoad(InputStream in, String name, OssConfigVo ossConfig,Long size) throws Exception {

        String  url = ossConfig.getOssUrl();
        String  bucket = ossConfig.getOssBucket();

        // 创建ClientConfiguration实例，按照您的需要修改默认参数
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME选项
        conf.setSupportCname(false);
        //new OSSClient(END_POINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        OSSClient client = new OSSClient("http://"+ url, ossConfig.getOssAccessKeyId(),ossConfig.getOssAccessKeySecret(), conf);

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(size);
        try {
            PutObjectResult result = client.putObject(bucket, name, in, meta);
            log.info(result.getETag());
        } catch (Exception oe) {
            oe.printStackTrace();
            throw  BizException.instance(GlobalsEnums.FILE_UPLOAD_FAILED);
        } finally {
            in.close();
            client.shutdown();
        }
        return "http://" + bucket + "." + url + "/" + name;
    }



}
