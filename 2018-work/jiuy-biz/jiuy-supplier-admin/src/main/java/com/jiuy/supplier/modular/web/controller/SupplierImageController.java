/**
 * 
 */
package com.jiuy.supplier.modular.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yujj.util.file.OSSFileUtil;

/**
 * 文件、图片控制器
 * 
 * @author LWS
 *
 */
@Controller
@RequestMapping("/mgr")
public class SupplierImageController {
	
//	private final String DEFAULT_BASEPATH_NAME = "yjj-img-store-test";
	private final String DEFAULT_BASEPATH_NAME = "";//ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;
	@Autowired
	private OSSFileUtil ossFileUtil;
	
	private static Logger logger = Logger.getLogger(SupplierImageController.class);

//	/**
//	 * 上传图片(上传到项目的webapp/static/img)
//	 */
//	@RequestMapping(method = RequestMethod.POST, path = "/upload")
//	@ResponseBody
//	public Map<String, String> upload(@RequestPart("file") MultipartFile picture) {
//		String pictureName = UUID.randomUUID().toString() + ".jpg";
//		try {
//			String fileSavePath = gunsProperties.getFileUploadPath();
//			picture.transferTo(new File(fileSavePath + pictureName));
//		} catch (Exception e) {
//			throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
//		}
//		Map<String, String> file = new HashMap<String, String>();
//		file.put("filename", pictureName);
//		return file;
//	}
	
	
	
}
