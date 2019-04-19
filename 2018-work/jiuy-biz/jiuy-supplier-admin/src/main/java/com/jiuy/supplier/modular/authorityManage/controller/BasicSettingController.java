package com.jiuy.supplier.modular.authorityManage.controller;

import com.admin.core.base.controller.BaseController;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.UserNewMapper;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 基础设置控制器
 *
 * @author fengshuonan
 * @Date 2018-03-13 10:36:18
 */
@Controller
@RequestMapping("/basicSetting")
public class BasicSettingController extends BaseController {
    private String PREFIX = "/authorityManage/basicSetting/";
    private static final Log logger = LogFactory.get(ProductAuthorityController.class);
	@Autowired
	private UserNewMapper supplierUserMapper;
    /**
     * 跳转到基础设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "basicSetting.html";
    }

    /**
     * 跳转到添加基础设置
     */
    @RequestMapping("/basicSetting_add")
    public String basicSettingAdd() {
        return PREFIX + "basicSetting_add.html";
    }

    /**
     * 跳转到修改基础设置
     */
    @RequestMapping("/basicSetting_update/{basicSettingId}")
    public String basicSettingUpdate(@PathVariable Integer basicSettingId, Model model) {
        return PREFIX + "basicSetting_edit.html";
    }

    /**
     * 获取基础设置列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增基础设置
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除基础设置
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改基础设置
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			UserNew supplier = supplierUserMapper.selectById(userId);
			String campaignImage = supplier.getCampaignImage();
			return jsonResponse.setSuccessful().setData(campaignImage);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("修改商品宣传图失败！");
		}
    }
   /**
    * 修改商品宣传图保存
    * @param campaignImage
    * @return
    */
    @RequestMapping(value = "/updateSave")
    @ResponseBody
    public Object updateSave(@RequestParam(value = "campaignImage",required = false ,defaultValue = "")String campaignImage) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
		if (userId == 0) {
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			UserNew supplier = new UserNew();
			supplier.setId(userId);
			supplier.setCampaignImage(campaignImage);
			supplierUserMapper.updateById(supplier);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("修改商品宣传图失败！");
		}
    }

    /**
     * 基础设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
