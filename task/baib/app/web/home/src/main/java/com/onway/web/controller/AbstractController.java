/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.onway.baib.common.util.Base64;
import com.onway.baib.core.cache.SysConfigCacheManager;
import com.onway.baib.core.cache.code.CodeGenerateComponent;
import com.onway.common.lang.StringUtils;
import com.onway.web.controller.template.ControllerTemplate;

/**
 * 控制器基类
 * 
 * @author li.hong
 * @version $Id: AbstractController.java, v 0.1 2016年9月2日 下午5:25:30 li.hong Exp
 *          $
 */
public class AbstractController {

	/** logger */

	protected static final String USER_CREDIT_LIMIT_APPLY_EXIT = "该用户的信用额度申请已经存在";

	protected static final String EXCEPTION_MESSAGE = "服务异常，请稍后尝试";

	protected static final String SYS_BUSY = "系统繁忙,请稍后再试";

	protected static final String UPDATE_MESSAGE = "您需要升级到最新版本才能使用该功能";

	protected static final String ILLEGAL_REQUEST = "非法请求";

	protected static final String TOKEN_ERROR = "token不正确";

	protected static final String USER_ID = "userId";

	protected static final String TOKEN = "token";

	protected static final String APP_TYPE = "appType";

	protected static final String VERSION = "version";

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(AbstractController.class);

	// *********所有的组件**********************************************

	@Resource
	protected ControllerTemplate controllerTemplate;

	@Resource
	protected SysConfigCacheManager sysConfigCacheManager;

    @Resource
	protected CodeGenerateComponent codeGenerateComponent;

	// *********所有的client*******************************************

	/** 用户信息查询客户端 */

	/** 用户手机token信息操作客户端 */

	// **********所有的DAO*********************************************
	

	/**
	 * 获取客户端访问ip地址
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = (String) request.getParameter("loginIP"); // 兼容PC端请求IP记录
		if (StringUtils.isBlank(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isBlank(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isBlank(ip) || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/*
	 * base64图片上传
	 */
     public String uploadBaseImg(String path,String insuranceImg){
//         String baseFile="";
//         for (String s : insuranceImg.split("&")) {
//             if (!StringUtils.contains(s, imgNo)) {
//                 continue;
//             }
//             baseFile = s;
//         }
//         
//         if (baseFile.indexOf(imgNo) != -1) {
//             baseFile = baseFile.replace(imgNo, "");
//         }
//         
         /**
          * Base64 上传图片
          */
        Base64 base64 =  new Base64();
            System.out.println(insuranceImg);
            //base64 解码
            byte[] byteArray = base64.decode(insuranceImg);
            // 调整异常数据  
            for (byte b : byteArray) {
                if(b<0)
                    b+=256;
            }
            String imageName = this.getImageName();
            try {
                OutputStream out = new FileOutputStream(path+File.separator+imageName);
                out.write(byteArray);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
          }
        return imageName;
     }
     
     
     /*
      * 多图片上传
      */
     public String uploadImgs(String path,String baseFile,String imgPath){
         /**
          * Base64 上传图片
          */
        Base64 base64 =  new Base64();
        StringTokenizer st = new StringTokenizer(baseFile,",");
        StringBuilder imageNames=new StringBuilder(); //将图片用逗号分隔保存
         while(st.hasMoreTokens() ){
             String sttoken=st.nextToken();
            System.out.println(sttoken);
            //base64 解码
            byte[] byteArray = base64.decode(sttoken);
            // 调整异常数据  
            for (byte b : byteArray) {
                if(b<0)
                    b+=256;
            }
            String imageName = this.getImageName();
            try {
                OutputStream out = new FileOutputStream(path+File.separator+"post"+File.separator+imageName);
                out.write(byteArray);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageNames.append(imgPath+File.separator+"post"+File.separator+imageName);
            imageNames.append(",");
        }
         imageNames.deleteCharAt(imageNames.length()-1); 
         
         return imageNames.toString();
     }
     

  /**
    * 根据系统规则得到图片名称
    */
    public String getImageName(){
        return UUID.randomUUID().toString()+".jpg";
    }



}
