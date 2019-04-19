package com.jiuy.wxa.api.controller.wxa;

import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.SmartModule;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.service.store.ISmartModuleService;
import com.jiuyuan.web.help.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/smartModule")
public class SmartModuleController {

    @Autowired
    private ISmartModuleService iSmartModuleService;


    @Autowired
    private StoreBusinessNewService storeBusinessNewService;

    /**
     * 初始化并查询模块列表
     *
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    @RequestMapping("/findWithInit")
    @ResponseBody
    public JsonResponse findWithInit(Long storeId) {

        StoreBusiness storeBusiness = storeBusinessNewService.findStoreDisplayImagesAndWxaAppIdById(storeId);

        try {
            //初始化
            iSmartModuleService.add(storeId, String.valueOf(storeBusiness.getWxaArticleShow()));


            String storeImgs = storeBusiness.getStoreDisplayImages();

            int c = 1;
            if (storeImgs == null || storeImgs.length() < 1) {
                c = 0;
            }

            List<SmartModule> datas = iSmartModuleService.getByStoreId(storeId);

            for (SmartModule sm : datas) {
                if (sm == null) {
                    continue;
                }

                //设置门店展示照片
                if ("smartPicture".equals(sm.getCode())) {
                    sm.setImgCount(c);
                }
            }

            return JsonResponse.getInstance().setSuccessful().setData(datas);
        } catch (Exception e) {
            return JsonResponse.getInstance().setError("模块初始化异常");
        }
    }


}
