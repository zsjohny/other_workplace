package com.jiuy.operator.common.system.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.service.common.vedio.MainVedioService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.vedio.MainVedio;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title:  VedioMainController.java
 * @Package com.jiuy.operator.common.system.controller
 * @Description: 处理app首页视频的增删改查
 * @author: Aison
 * @date:   2018/4/23 16:12
 * @version V1.0
 * @Copyright: 玖远网络
 */
@Controller
@RequestMapping("/vedio")
@Login
public class VedioMainController extends BaseController {


    @Autowired
    private MainVedioService mainVedioService;

    /**
     * 添加首页视频数据
     * @param   mainVedio  vedio对象的封装
     * @date:   2018/4/23 16:21
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/addMainVedio")
    public JsonResponse addMainVedio(MainVedio mainVedio) {

        try{
            Integer userId = ShiroKit.getUser().getId();
            mainVedioService.addMainVedio(mainVedio,userId);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
           return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 修改首页视频数据
     * @param mainVedio
     * @date:   2018/4/23 16:26
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/updateMainVedio")
    public JsonResponse updateMainVedio(MainVedio mainVedio) {

        try{
            Integer userId = ShiroKit.getUser().getId();
            mainVedioService.updateMainVedio(mainVedio,userId);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 删除首页视频数据
     * @param id
     * @date:   2018/4/23 16:26
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JsonResponse delete(Long id) {
        try{
            mainVedioService.delete(id);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 获取首页的视频列表
     * @param request
     * @date:   2018/4/23 16:43
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/mainVedioList")
    public JsonResponse mainVedioList(HttpServletRequest request) {
        try{
            List<MainVedio> mainVedios = mainVedioService.mainVedioList(getRequestMap(request));
            return new JsonResponse().setSuccessful().setData(mainVedios);
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }



    public  Map<String, Object> getRequestMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> maps = new HashMap<String, Object>();
        String[] vals = null;
        for (Map.Entry<String, String[]> en : map.entrySet()) {
            vals = en.getValue();
            if (vals != null && vals.length > 0) {
                // 单个属性
                if (vals.length == 1 && !StringUtils.isBlank(vals[0])) {
                    maps.put(en.getKey(), BizUtil.filterSqlString(vals[0]));
                }
                // 数组属性
                if (vals.length > 1) {
                    maps.put(en.getKey().replace("[]", ""), en.getValue());
                }
            }
        }
        return maps;
    }
}
