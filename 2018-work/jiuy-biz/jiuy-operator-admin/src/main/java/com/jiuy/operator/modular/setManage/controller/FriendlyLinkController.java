package com.jiuy.operator.modular.setManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.common.IOperatorLinkService;
import com.jiuyuan.service.common.OperatorLinkService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.web.help.JsonResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 友情链接控制器
 *
 * @author fengshuonan
 * @Date 2018-04-13 15:33:06
 */
@Controller
@RequestMapping("/friendlyLink")
public class FriendlyLinkController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(FriendlyLinkController.class);

    private String PREFIX = "/setManage/friendlyLink/";
    
    @Autowired
    private IOperatorLinkService operatorLinkService;

    /**
     * 跳转到友情链接首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "friendlyLink.html";
    }

    /**
     * 跳转到添加友情链接
     */
    @RequestMapping("/friendlyLink_add")
    public String friendlyLinkAdd() {
        return PREFIX + "friendlyLink_add.html";
    }

    /**
     * 跳转到修改友情链接
     */
    @RequestMapping("/friendlyLink_update/{friendlyLinkId}")
    public String friendlyLinkUpdate(@PathVariable Integer friendlyLinkId, Model model) {
        return PREFIX + "friendlyLink_edit.html";
    }

    /**
     * 获取友情链接列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() {
    	JsonResponse jsonResponse  = new JsonResponse();
    	try {
    		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
    		List<Map<String,Object>> data = operatorLinkService.list(page);
    		page.setRecords(data);
    		return this.packForBT(page);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    	
    }

    /**
     * 新增友情链接
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestParam("title") String title,
    		          @RequestParam("replaceImageTitle") String replaceImageTitle,
    		          @RequestParam("imageUrl") String imageUrl,
    		          @RequestParam("linkUrl") String linkUrl,
    		          @RequestParam(value = "sort", required = false, defaultValue = "1") int sort
    		          ) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorUserId = ShiroKit.getUser().getId();
    	try {
    		operatorLinkService.add(operatorUserId, title, replaceImageTitle, imageUrl, linkUrl, sort);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }

    /**
     * 删除友情链接
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(
    		             @RequestParam("operatorRelationUrlId") long operatorRelationUrlId
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorUserId = ShiroKit.getUser().getId();
    	try {
    		operatorLinkService.delete(operatorUserId, operatorRelationUrlId);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }


    /**
     * 修改友情链接
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(
    		  @RequestParam("title") String title,
	          @RequestParam("replaceImageTitle") String replaceImageTitle,
	          @RequestParam("imageUrl") String imageUrl,
	          @RequestParam("linkUrl") String linkUrl,
	          @RequestParam(value = "sort", required = false, defaultValue = "1") int sort,
	          @RequestParam("operatorRelationUrlId") long operatorRelationUrlId
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorUserId = ShiroKit.getUser().getId();
    	try {
    		operatorLinkService.update(operatorUserId, title, replaceImageTitle, imageUrl, linkUrl, sort, operatorRelationUrlId);
    		return jsonResponse.setSuccessful();
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }

    /**
     * 友情链接详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(
    		             @RequestParam("operatorRelationUrlId") long operatorRelationUrlId
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = operatorLinkService.detail(operatorRelationUrlId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }
}
