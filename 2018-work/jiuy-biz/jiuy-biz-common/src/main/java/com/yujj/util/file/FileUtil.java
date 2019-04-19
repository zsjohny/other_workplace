/**
 * 文件存储、上传、下载处理
 */
package com.yujj.util.file;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author LWS
 *
 */
public interface FileUtil {

	public String uploadFile(final String basePath,MultipartFile file) throws IOException;
	public String downloadFile(String filename);
	public String removeFile(final String basePath,String fileName);
	public List<String> listAllFile(String basePath);
}
