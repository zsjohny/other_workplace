package com.jiuy.store.api.tool.controller;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.SmartModule;
import com.jiuyuan.service.store.ISmartModuleService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * app门店智能模块 前端控制器
 * </p>
 *
 * @author Charlie(唐静)
 * @since 2018-05-09
 */
@Controller
@RequestMapping( "/smartModule" )
@Login
public class SmartModuleController{

    @Autowired
    private ISmartModuleService iSmartModuleService;

    /**
     * 查询门店智能模板
     *
     * @return
     * @Author Charlie(唐静)
     * @Data 18/05/09
     */
    @RequestMapping( "/find/auth" )
    @ResponseBody
    public JsonResponse find(UserDetail<StoreBusiness> userDetail) {
        List<SmartModule> datas = iSmartModuleService.getByStoreId(userDetail.getId());
        return JsonResponse.getInstance().setSuccessful().setData(datas);
    }

    /**
     * 更新单个门店智能模块
     *
     * @param smartModule
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @RequestMapping( "/upd/auth" )
    @ResponseBody
    public JsonResponse upd(SmartModule smartModule, UserDetail<StoreBusiness> userDetail) {
        //当前用户门店id
        Long currentStoreId = userDetail.getId();
        if (iSmartModuleService.upd(smartModule, currentStoreId)) {
            return JsonResponse.getInstance().setSuccessful();
        } else {
            return JsonResponse.getInstance().setError("请求参数不合法");
        }
    }


    /**
     * 批量更新门店智能模块
     *
     * @param data
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @RequestMapping( "/batchUpd/auth" )
    @ResponseBody
    public JsonResponse batchUpd(String data, UserDetail<StoreBusiness> userDetail) {
        try {
            List<SmartModule> smartModules = BizUtil.jsonStrToListObject(data, List.class, SmartModule.class);
            //当前用户门店id
            Long currentStoreId = userDetail.getId();

            if (iSmartModuleService.batchUpd(smartModules, currentStoreId)) {
                return JsonResponse.getInstance().setSuccessful();
            } else {
                return JsonResponse.getInstance().setError("请求参数不合法");
            }

        } catch (Exception e) {
            return JsonResponse.getInstance().setError("更新失败");
        }
    }


    /**
     * 新增门店模板
     *
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @RequestMapping( "/refresh/auth" )
    @ResponseBody
    public JsonResponse add(UserDetail<StoreBusiness> userDetail) {
        try {
            iSmartModuleService.add(userDetail.getId(), userDetail.getUserDetail());
            return JsonResponse.getInstance().setSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResponse.getInstance().setError("更新失败");
        }

    }


    /**
     * 初始化并查询模块列表
     * @param userDetail
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    @RequestMapping( "/findWithInit/auth" )
    @ResponseBody
    public JsonResponse findWithInit(UserDetail<StoreBusiness> userDetail) {
        long storeId = userDetail.getId();
        try {
            //初始化
            iSmartModuleService.add(storeId, userDetail.getUserDetail());
            //查询
            List<SmartModule> datas = iSmartModuleService.getByStoreId(storeId);
            return JsonResponse.getInstance().setSuccessful().setData(datas);
        } catch (Exception e) {
            return JsonResponse.getInstance().setError("模块初始化异常");
        }
    }


}
