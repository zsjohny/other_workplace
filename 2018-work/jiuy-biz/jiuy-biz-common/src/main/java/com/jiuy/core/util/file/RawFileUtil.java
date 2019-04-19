/**
 * 直接往硬件磁盘的文件系统中存储文件
 */
package com.jiuy.core.util.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jiuy.core.exception.ServerUnknownException;

/**
 * @author hadoop
 *
 */
public class RawFileUtil implements FileUtil {

	/* (non-Javadoc)
	 * @see com.jiuy.core.util.file.FileUtil#uploadFile(java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public  String uploadFile(final String basePath,MultipartFile file, String needWaterMark) throws IOException{
		String fileName = file.getOriginalFilename();
		if(fileName.contains("/")){
			throw new ServerUnknownException("get file name with error, please use other browser and try again");
		}
		String storeFileName = String.valueOf(fileName) + new Date().getTime();
        File tempFile = new File(basePath, storeFileName );  
        if (!tempFile.getParentFile().exists()) {  
            tempFile.getParentFile().mkdir();  
        }  
        if (!tempFile.exists()) {  
            tempFile.createNewFile();  
        }  
        file.transferTo(tempFile);  
		return storeFileName;
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.util.file.FileUtil#downloadFile(java.lang.String)
	 */
	@Override
	public String downloadFile(String filename) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jiuy.core.util.file.FileUtil#listAllFile(java.lang.String)
	 */
	@Override
	public List<String> listAllFile(String basePath) {
		return null;
	}

	@Override
	public String removeFile(String basePath, String fileName) {
		return null;
	}
	
//	@Override
//	public String uploadImgFile(String basePath, String binarySystemImgStr) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
