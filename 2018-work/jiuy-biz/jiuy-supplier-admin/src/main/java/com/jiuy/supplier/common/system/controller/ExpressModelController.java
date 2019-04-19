package com.jiuy.supplier.common.system.controller;

import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuyuan.entity.express.ExpressModel;
import com.jiuyuan.entity.express.ExpressModelDetail;
import com.jiuyuan.service.common.area.AreaService;
import com.jiuyuan.service.common.express.IExpressModelService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.web.help.JsonResponse;
import org.beetl.ext.fn.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.List;

/**
 * @version V1.0
 * @Package com.jiuy.supplier.common.system.controller
 * @Description: 供应商运费controller
 * @author: Aison
 * @date: 2018/4/27 15:40
 * @Copyright: 玖远网络
 */
@Controller
@RequestMapping("/express")
public class ExpressModelController {


    @Autowired
    private IExpressModelService expressModelService;

    @Autowired
    private AreaService areaService;


    /**
     * 添加
     * @param expressModel
     * @date:   2018/4/28 11:22
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/addExpressModel")
    public JsonResponse addExpressModel(ExpressModel expressModel) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            expressModel.setSupplierId(supplierId);
            expressModelService.addExpressModel(expressModel);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 添加模板明细
     * @param expressModelDetail
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/addExpressModelDetail")
    @ResponseBody
    public JsonResponse addExpressModelDetail( ExpressModelDetail expressModelDetail) {

        try{
            long supplierId = ShiroKit.getUser().getId();
            Long id = expressModelService.addExpressModelDetail(supplierId,expressModelDetail);
            return new JsonResponse().setSuccessful().setData(id);
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 批量添加模板明细
     * @param expressModelDetailStr
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/addExpressModelDetails")
    @ResponseBody
    public JsonResponse addExpressModelDetails( String expressModelDetailStr) {

        try{
            long supplierId = ShiroKit.getUser().getId();
            List<ExpressModelDetail> expressModelDetails = BizUtil.jsonStrToListObject(expressModelDetailStr,List.class,ExpressModelDetail.class);
            List<Long> ids = expressModelService.addExpressModelDetails(supplierId,expressModelDetails);
            return new JsonResponse().setSuccessful().setData(ids);
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 删除模板明细
     * @param detailId
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/deleteExpressModelDetail")
    @ResponseBody
    public JsonResponse deleteExpressModelDetail( Long detailId) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            expressModelService.deleteExpressModelDetail(supplierId,detailId);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 批量删除模板明细
     * @param detailIds
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/deleteExpressModelDetails")
    @ResponseBody
    public JsonResponse deleteExpressModelDetails(String detailIds) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            expressModelService.deleteExpressModelDetails(supplierId,detailIds);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 批量修改详情
     * @param deleteIds 需要删除的详情
     * @param updates  需要修改的详情
     * @date:   2018/5/8 15:30
     * @author: Aison
     */
    @RequestMapping("/updateBatchDetail")
    @ResponseBody
    public JsonResponse updateBatchDetail(String deleteIds,String updates,String adds) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            expressModelService.updateDetailBatch(supplierId,deleteIds,updates,adds);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 更新模板
     * @param expressModel 模板封装类
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/updateExpressModel")
    @ResponseBody
    public JsonResponse updateExpressModel(ExpressModel expressModel) {
        try{
            long supplierId = ShiroKit.getUser().getId();

            expressModelService.updateModel(expressModel,supplierId);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }

    /**
     * 更新模板明细
     * @param expressModelDetail 模板明细封装类
     * @date:   2018/4/28 11:25
     * @author: Aison
     */
    @RequestMapping("/updateExpressModelDetail")
    @ResponseBody
    public JsonResponse updateExpressModelDetail(ExpressModelDetail expressModelDetail) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            expressModelService.updateModelDetail(expressModelDetail,supplierId);
            return new JsonResponse().setSuccessful();
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }


    /**
     * 获取行政区划的分租
     * @param
     * @date:   2018/4/28 16:09
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/getAreaGroup")
    public JsonResponse getAreaGroup() {
        return new JsonResponse().setSuccessful().setData(areaService.getGroupProvince());
    }

    /**
     *
     * @param skuInfo
     * @param provinceCode
     * @date:   2018/5/2 14:59
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/expressMoney")
    public JsonResponse expressMoney(String skuInfo,String provinceCode) {
        try{
            long supplierId = ShiroKit.getUser().getId();
            BigDecimal expressMoney =  expressModelService.countOrderExpressMoney(provinceCode,skuInfo,supplierId);
            return new JsonResponse().setSuccessful().setData(expressMoney);
        }catch (Exception e) {
            return BizUtil.exceptionHandler(e);
        }
    }
    
    /**
    * 查询某个供应商的运费模板
    * @date:   2018/5/2 14:59
    * @author: Aison
    */
    @ResponseBody
    @RequestMapping("/expressModel")
    public JsonResponse expressModel() {
    	 long supplierId = ShiroKit.getUser().getId();
    	 try {
    		 ExpressModel em = expressModelService.supplierExpress(supplierId);
    		 return new JsonResponse().setSuccessful().setData(em);
		} catch (Exception e) {
			return BizUtil.exceptionHandler(e);
		}
    }
    
    /**
     * 查询某个供应商的运费模板明细
     * @date:   2018/5/2 14:59
     * @author: Aison
     */
    @ResponseBody
    @RequestMapping("/expressModelDetails")
     public JsonResponse expressModelDetails() {
     	 long supplierId = ShiroKit.getUser().getId();
     	 try {
     		 List<ExpressModelDetail> ems = expressModelService.supplierExpressDetail(supplierId);
     		 return new JsonResponse().setSuccessful().setData(ems);
 		} catch (Exception e) {
 			return BizUtil.exceptionHandler(e);
 		}
     }

}
