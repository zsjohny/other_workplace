/**
 * OSS（阿里开放式云存储提供的文件上传、下载功能）
 */
package com.jiuy.core.util.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.jiuy.core.dao.mapper.BinaryDataDao;
import com.jiuy.core.exception.ServerUnknownException;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.entity.BinaryData;
import com.jiuyuan.util.WaterMarker;

/**
 * @author LWS
 *
 */
public class OSSFileUtil implements FileUtil {
	
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private BinaryDataDao binaryDataDao;
	
	public OSSFileUtil(){
		/*// 创建ClientConfiguration实例
		ClientConfiguration conf = new ClientConfiguration();
		// 设置HTTP最大连接数为10
		conf.setMaxConnections(10);
		// 设置TCP连接超时为5000毫秒
		conf.setConnectionTimeout(5000);
		// 设置最大的重试次数为3
		conf.setMaxErrorRetry(3);
		// 设置Socket传输数据超时的时间为2000毫秒
		conf.setSocketTimeout(2000);*/
		// 使用OSSClient默认配置
		client = new OSSClient(END_POINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}
	
	/**
	 * 文件二进制转输入流
	 * 1. String --> InputStream
	 * @param str
	 * @return
	 */
	private InputStream String2InputStream(String str){
		   ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		   return stream;
		}
	
//	2. InputStream --> String
	private String inputStream2String(InputStream is) throws IOException{
	   BufferedReader in = new BufferedReader(new InputStreamReader(is));
	   StringBuffer buffer = new StringBuffer();
	   String line = "";
	   while ((line = in.readLine()) != null){
	     buffer.append(line);
	   }
	   return buffer.toString();
	}
	/**
	 * 3、File --> InputStream
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private InputStream File2InputStream(File file) throws IOException{
		InputStream in = new FileInputStream(file);//new InputStream();
		return in;
	}
	
	/**
	 * 4、InputStream --> File
	 * @param is
	 * @return
	 * @throws IOException
	 */
//	private InputStream file2InputStream(File file) throws IOException{
//		OutputStream os = new FileOutputStream(file);
//		int bytesRead = 0;
//		byte[] buffer = new byte[8192];
//		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//		os.write(buffer, 0, bytesRead);
//		}
//		os.close();
//		ins.close();
//	}
//	/**
//	 * 二进制字符串上传文件阿里云
//	 */
//	@Override
//	public String uploadImgFile(String basePath, String binarySystemImgStr)throws IOException {
////		String fileName = file.getOriginalFilename();
////		if (fileName.contains("/")) {
////			throw new ServerUnknownException("get file name with error, please use other browser and try again");
////		}
////		String suffix = "";
////		if(StringUtils.isNotEmpty(StringUtils.trim(fileName))){
////			if(StringUtils.indexOf(fileName, '.') != -1){
////				String[] fileNameArr = StringUtils.split(fileName, ".");
////				suffix =  "."+ fileNameArr[fileNameArr.length - 1] ;
////			}
////		}
//		
//		InputStream is = String2InputStream(binarySystemImgStr);
//		
//		String storeFileName = String.valueOf(new Date().getTime()) + String.valueOf(System.currentTimeMillis()) +".jpg" ;
//		// 获取Bucket的存在信息
//		boolean exists = client.doesBucketExist(basePath);
//		// 输出结果
//		if (!exists) {
//			// 新建一个Bucket
//			client.createBucket(basePath);
//		}
//		
////		InputStream content = null;
//		// 创建上传Object的Metadata
//		ObjectMetadata meta = new ObjectMetadata();
////		content = file.getInputStream();
//		// 必须设置ContentLength
//		//meta.setContentLength(file.getSize());
//		meta.setContentLength(binarySystemImgStr.length());
//		
//		// 上传Object.
//		PutObjectResult result = client.putObject(basePath, storeFileName,is, meta);
//		// 打印ETag
//		logger.debug(result.getETag());
//		// 生成访问链
//		return IMG_SERVICE + SPLASH + storeFileName ;
//	}
	
	@Override
	public String uploadFile(String basePath, MultipartFile file, String needWaterMark)
			throws IOException {
		String fileName = file.getOriginalFilename();
		if (fileName.contains("/")) {
			throw new ServerUnknownException(
					"get file name with error, please use other browser and try again");
		}
		String suffix = "";
		if(StringUtils.isNotEmpty(StringUtils.trim(fileName))){
			if(StringUtils.indexOf(fileName, '.') != -1){
				String[] fileNameArr = StringUtils.split(fileName, ".");
				suffix =  "."+ fileNameArr[fileNameArr.length - 1] ;
			}
		}
		String storeFileName = String.valueOf(new Date().getTime()) + String.valueOf(System.currentTimeMillis()) +suffix ;
		// 获取Bucket的存在信息
		boolean exists = client.doesBucketExist(basePath);
		// 输出结果
		if (!exists) {
			// 新建一个Bucket
			client.createBucket(basePath);
		}
		
		InputStream content = null;
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		content = file.getInputStream();
		// 必须设置ContentLength
		meta.setContentLength(file.getSize());
		
		/*
		 * 是否要加水印
		 */
		if (needWaterMark != null && StringUtils.equals(needWaterMark, "YES")) {
			JSONObject jsonObject = globalSettingService.getJsonObject(GlobalSettingName.WATERMARK);
			String enable = jsonObject.getString("enable");
			
			if (StringUtils.equals(enable, "YES")) {
				BinaryData binaryData2 = binaryDataDao.getWaterMark();
				byte[] bytes = WaterMarker.markImageByIcon(file.getInputStream(), binaryData2.getContent());
				
				content = new ByteArrayInputStream(bytes); 
				meta.setContentLength(bytes.length);
			}
		}
		
		// 上传Object.
		PutObjectResult result = client.putObject(basePath, storeFileName,
				content, meta);
		// 打印ETag
		logger.debug(result.getETag());
		// 生成访问链
		return IMG_SERVICE + SPLASH + storeFileName ;
	}
	

	@Override
	public String downloadFile(String filename) {
		return null;
	}

	@Override
	public List<String> listAllFile(String basePath) {
		return null;
	}

	@Override
	public String removeFile(String basePath, String key) {
		// 为安全考虑删除权限将不再提供， 定期统一处理
		// client.deleteObject(basePath, key);
		return key;
	}
	
	/*private String generateSignature(String source){
		final String algorithm = "HmacSHA1";
		final String charset = "US-ASCII";
		try {
			Mac mac = Mac.getInstance(algorithm);
			SecretKey secretKey = new SecretKeySpec(ACCESS_KEY_SECRET.getBytes(charset), algorithm);
			mac.init(secretKey);
			byte[] text = source.toString().getBytes(charset);
			byte[] finalText = mac.doFinal(text);
			return Base64Util.encode(new String(finalText));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
			logger.error(e);
		}
		return null;
	}*/
	
	/**
	private final String ACCESS_KEY_ID = "BbH5y38r89PDNLi1";
	private final String ACCESS_KEY_SECRET = "fiZUw81vGtOmb7JC0qZE6I6BfiOogw";
	private final String END_POINT = "http://oss-cn-shenzhen.aliyuncs.com";
	private final String IMG_SERVICE = "http://yjj-img-store-test.oss-cn-shenzhen.aliyuncs.com";
	*/
	
//	private final String ACCESS_KEY_ID = "A9WHV1n0hmkIkWgN";
//	private final String ACCESS_KEY_SECRET = "UmZxmB2CAcl2svvVcnnimc1My2Oajm";
//	private final String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
//	private final String IMG_SERVICE = "http://yjj-img-dev.oss-cn-hangzhou.aliyuncs.com";
	private final String ACCESS_KEY_ID = ThirdPartService.OSS_ACCESS_KEY_ID;
	private final String ACCESS_KEY_SECRET = ThirdPartService.OSS_ACCESS_KEY_SECRET;
	private final String END_POINT = ThirdPartService.OSS_END_POINT;
	private final String IMG_SERVICE = ThirdPartService.OSS_IMG_SERVICE;
	// 初始化一个OSSClient
	private OSSClient client = null;
	private final String SPLASH = "/";
	
	private static Logger logger = Logger.getLogger(OSSFileUtil.class);
	
}
