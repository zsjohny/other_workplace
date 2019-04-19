package com.jiuyuan.service.common.file;

import com.jiuyuan.dao.mapper.common.FileListMapper;
import com.jiuyuan.entity.common.FileList;
import com.jiuyuan.util.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common.file
 * @Description: 文件services
 * @author: Aison
 * @date: 2018/4/27 18:06
 * @Copyright: 玖远网络
 */
@Service
public class FileService {

    @Autowired
    private FileListMapper fileListMapper;

    public void addFile(FileList fileList) {
        if(fileListMapper.insert(fileList) == 0) {
            throw BizException.defulat().msg("保存文件失败");
        }
    }

}
