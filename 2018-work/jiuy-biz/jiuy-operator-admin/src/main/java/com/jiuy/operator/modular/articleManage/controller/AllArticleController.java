package com.jiuy.operator.modular.articleManage.controller;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.zxing.Result;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.OperatorArticle;
import com.jiuyuan.service.common.IOperatorArticleService;
import com.jiuyuan.service.common.OperatorArticleService;
import com.jiuyuan.util.TipsMessageException;
import com.jiuyuan.util.anno.AdminOperationLog;
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
 * 所有文章控制器
 *
 * @author fengshuonan
 * @Date 2018-04-13 15:46:48
 */
@Controller
@RequestMapping("/allArticle")
public class AllArticleController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AllArticleController.class);

    private String PREFIX = "/articleManage/allArticle/";
    
    @Autowired
    private IOperatorArticleService operatorArticleService;

    /**
     * 跳转到所有文章首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "allArticle.html";
    }

    /**
     * 跳转到添加所有文章
     */
    @RequestMapping("/allArticle_add")
    public String allArticleAdd() {
        return PREFIX + "allArticle_add.html";
    }

    /**
     * 跳转到修改所有文章
     */
    @RequestMapping("/allArticle_update/{allArticleId}")
    public String allArticleUpdate(@PathVariable Integer allArticleId, Model model) {
        return PREFIX + "allArticle_edit.html";
    }
    
    /**
     * 获取文章总数
     */
    @RequestMapping("/getArticleCount")
    @ResponseBody
    public Object getArticleCount(){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int data = operatorArticleService.getArticleCount();
    		return jsonResponse.setSuccessful().setData(data);
		} catch (TipsMessageException e) {
			logger.info(e.getFriendlyMsg());
			return jsonResponse.setError(e.getFriendlyMsg());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(ResultCode.SYSTEM_OPERATION_ERROR.getDesc());
		}
    }    

    /**
     * 获取所有文章列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(
    		           @RequestParam(value = "articleTitle", required = false, defaultValue = "") String articleTitle,
    		           @RequestParam(value = "articleAbstracts", required = false, defaultValue = "") String articleAbstracts
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
    		List<Map<String,Object>> list = operatorArticleService.list(page,articleTitle,articleAbstracts);
    		page.setRecords(list);
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
     * 新增所有文章
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @AdminOperationLog
    public Object add(
    		          @RequestParam("title") String title,
    		          @RequestParam(value = "abstracts",required = false, defaultValue = "") String abstracts,
    		          @RequestParam("previewImageUrl") String previewImageUrl,
    		          @RequestParam("content") String content,
    		          @RequestParam(value = "seoTitle", required = false, defaultValue = "") String seoTitle,
    		          @RequestParam(value = "seoDescriptor", required = false, defaultValue = "") String seoDescriptor,
    		          @RequestParam(value = "seoKeywords", required = false, defaultValue = "") String seoKeywords
    		          ) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorId = ShiroKit.getUser().getId();
    	try {
    		operatorArticleService.add(title, abstracts, previewImageUrl, content, seoTitle, seoDescriptor, seoKeywords, operatorId);
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
     * 删除所有文章
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @AdminOperationLog
    public Object delete(@RequestParam("operatorArticleId") long operatorArticleId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long operatorUserId = ShiroKit.getUser().getId();
    	try {
    		operatorArticleService.delete(operatorArticleId, operatorUserId);
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
     * 修改所有文章
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @AdminOperationLog
    public Object update(
    		  @RequestParam("title") String title,
	          @RequestParam(value = "abstracts",required = false, defaultValue = "") String abstracts,
	          @RequestParam("previewImageUrl") String previewImageUrl,
	          @RequestParam("content") String content,
	          @RequestParam(value = "seoTitle", required = false, defaultValue = "") String seoTitle,
	          @RequestParam(value = "seoDescriptor", required = false, defaultValue = "") String seoDescriptor,
	          @RequestParam(value = "seoKeywords", required = false, defaultValue = "") String seoKeywords,
	          @RequestParam("operatorArticleId") long operatorArticleId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		operatorArticleService.update(title, abstracts, previewImageUrl, content, seoTitle, seoDescriptor, seoKeywords, operatorArticleId);
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
     * 所有文章详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail(
    		@RequestParam("operatorArticleId") long operatorArticleId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = operatorArticleService.detail(operatorArticleId);
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
