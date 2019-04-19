/**
 * 文件存储、上传、下载处理
 */
package com.jiuy.core.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author LWS
 *
 */
public interface FileUtil {
//	public String uploadImgFile(final String basePath,String binarySystemImgStr) throws IOException;
	

	public String uploadFile(final String basePath,MultipartFile file, String isProductImage) throws IOException;
	public String downloadFile(String filename);
	public String removeFile(final String basePath,String fileName);
	public List<String> listAllFile(String basePath);
}
