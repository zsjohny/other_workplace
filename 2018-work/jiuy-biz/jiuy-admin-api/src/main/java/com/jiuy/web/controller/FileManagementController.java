/**
 * 
 */
package com.jiuy.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jiuy.core.util.file.FileUtil;
import com.jiuyuan.constant.ThirdPartService;
import com.jiuyuan.util.anno.AdminOperationLog;

/**
 * 文件、图片控制器
 * 
 * @author LWS
 *
 */
@Controller
public class FileManagementController {
	
//	private final String DEFAULT_BASEPATH_NAME = "yjj-img-store-test";
	private final String DEFAULT_BASEPATH_NAME = ThirdPartService.OSS_DEFAULT_BASEPATH_NAME;
	@Resource(name = "ossFileUtil")
	private FileUtil fileUtil;
	
	private static Logger logger = Logger.getLogger(FileManagementController.class);

	/**
	 * 上传文件到指定文件系统 创建日期: 2015/06/09
	 *  创建内容: 1）上传文件到OSS中 
	 *  2） 将文件名存储到session中
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
    @AdminOperationLog
	public String uploadImageFromSession(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String storePath = null;
		String oldPath = request.getParameter("oldPath");
		String needWaterMark = request.getParameter("need_water_mark");

		try {
			if (request instanceof MultipartHttpServletRequest) {
				logger.debug("yes you are!");
				MultipartHttpServletRequest multiservlet = (MultipartHttpServletRequest) request;
				MultipartFile file = multiservlet.getFile("file");
				if (file == null) {
					logger.error("request file null oldPath:" + oldPath);
					return "";
				}
				logger.debug("request file name :" + file.getName());
				storePath = fileUtil.uploadFile(DEFAULT_BASEPATH_NAME, file, needWaterMark);
				
				//覆盖旧路径则删除
				if(StringUtils.isNotEmpty(StringUtils.trim(oldPath))){
					String key = oldPath.split("/")[oldPath.split("/").length - 1];
					fileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
				}
			} else {
				logger.debug("no wrong request!");
			}
			modelMap.addAttribute("images", storePath);
			return "json";
		} catch (IOException e) {
			logger.error("上传文件出现异常", e);
		} finally {

		}
		return "json";
	}
	
}
