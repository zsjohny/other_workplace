package com.jiuy.store.tool.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Client;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.util.file.FileUtil;

/**
 * <p>
 * 公共接口
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping( "/mobile/common" )
public class CommonController{
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    Log log = LogFactory.get();
    private final String DEFAULT_BASEPATH_NAME = Client.OSS_DEFAULT_BASEPATH_NAME;
    @Resource( name = "ossFileUtil" )
    private FileUtil fileUtil;


    /**
     * 删除云平台文件，用户上传图片成功后删除老图片
     *
     * @return
     */
    @RequestMapping( "/rFile" )
    @ResponseBody
    public JsonResponse list(@RequestParam( required = true ) String urls,
                             UserDetail userDetail, HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            //覆盖旧路径则删除
            if (StringUtils.isNotEmpty(StringUtils.trim(urls))) {
                String[] urlArr = urls.split(",");
                for (String url : urlArr) {
                    String key = url.split("/")[url.split("/").length - 1];
                    fileUtil.removeFile(DEFAULT_BASEPATH_NAME, key);
                    logger.info("删除了文件，url：" + url);
                }
            }
            return jsonResponse.setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse.setError(e.getMessage());
        }
    }
}