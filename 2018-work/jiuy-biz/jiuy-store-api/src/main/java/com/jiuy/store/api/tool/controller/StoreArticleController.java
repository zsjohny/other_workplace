package com.jiuy.store.api.tool.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.jiuy.base.util.Biz;
import com.jiuy.rb.model.base.StoreArticleRb;
import com.jiuy.rb.service.base.IStoreArticleService;
import com.jiuyuan.service.store.ISmartModuleService;
import com.jiuyuan.util.anno.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreArticle;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.StoreArticleService;

import javax.annotation.Resource;

/**
 * <p>
 * 门店文章表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2018-01-02
 */
@Controller
@RequestMapping("/store/storeArticle")
public class StoreArticleController {
	@Autowired
	private ISmartModuleService iSmartModuleService;
	@Autowired
	private StoreArticleService storeArticleService;

	
	@Autowired
	private StoreBusinessNewService storeBusinessNewService;

	@Resource(name = "storeArticleServiceImpl")
	private IStoreArticleService storeArticleServiceRb;


	/**
	 * 添加文章
	 * 
	 * @param context
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/add/auth")
	@ResponseBody
	@Login
	public JsonResponse addArticle(
			@RequestParam(value = "top", required = false, defaultValue = "0")Integer top,
			@RequestParam(value = "publicState", required = false, defaultValue = "1")Integer publicState,
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "headImage", required = true) String headImage,
			@RequestParam(value = "context", required = true) String context,UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
//			StoreArticle storeArticle = new StoreArticle();
//			storeArticle.setStoreId(userDetail.getId());
//			storeArticle.setArticleContext(context);
//			storeArticle.setArticleTitle(title);
//			storeArticle.setHeadImage(headImage);
//			storeArticle.setCreateTime(System.currentTimeMillis());
//			storeArticle.setUpdateTime(System.currentTimeMillis());
//			storeArticleService.insertAndGetId(storeArticle);
			if (Biz.hasEmoji (context)) {
				return jsonResponse.setError ("文本内含有非法符号或者表情");
			}
			if ( Biz.hasEmoji (title)) {
				return jsonResponse.setError ("标题内含有非法符号或者表情");
			}

			StoreArticleRb article = new StoreArticleRb ();
			article.setStoreId (userDetail.getId ());
			article.setArticleContext (context);
			article.setArticleTitle(title);
			article.setHeadImage(headImage);
			article.setTop (top);
			article.setPublicState (publicState);
			StoreArticleRb newData = storeArticleServiceRb.add (article);
			return jsonResponse.setSuccessful().setData(newData);
		} catch (Exception e) {
			e.printStackTrace ();
			return jsonResponse.setError("添加失败！");
		}
	}



	/**
	 * 删除文章
	 * 
	 * @param articleId
	 * @return
	 */
	@RequestMapping("/delete/auth")
	@ResponseBody
	public JsonResponse deleteArticle(@RequestParam(value = "articleId", required = true) long articleId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			StoreArticle storeArticle = storeArticleService.getArticleById(articleId);
			if (storeArticle == null) {
				return jsonResponse.setError("该文章不存在！");
			}
			if (storeArticle.getStoreId() != storeId) {
				return jsonResponse.setError("该文章不存在！");
			}
			StoreArticle newStoreArticle = new StoreArticle();
			newStoreArticle.setId(articleId);
			newStoreArticle.setStatus(StoreArticle.article_status_delete);
			newStoreArticle.setUpdateTime(System.currentTimeMillis());
			storeArticleService.updateArticle(newStoreArticle);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("删除失败");
		}
	}

	/**
	 * 修改文章
	 * 
	 * @param articleId
	 * @return
	 */
	@RequestMapping("/update/auth")
	@ResponseBody
	public JsonResponse updateArticle(@RequestParam(value = "articleId", required = true) long articleId,
			UserDetail<StoreBusiness> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			StoreArticle storeArticle = storeArticleService.getArticleById(articleId);
			if (storeArticle == null ) {
				return jsonResponse.setError("该文章不存在！");
			}
			if (storeArticle.getStoreId() != storeId) {
				return jsonResponse.setError("该文章不存在！");
			}
			return jsonResponse.setSuccessful().setData(storeArticle);
		} catch (Exception e) {
			return jsonResponse.setError("修改文章失败！");
		}
	}

	/**
	 * 修改文章保存
	 * 
	 * @param articleId
	 * @param title
	 * @param context
	 * @return
	 */
	@RequestMapping("/saveUpdate/auth")
	@ResponseBody
	@Login
	public JsonResponse saveUpdateArticle(
			@RequestParam(value = "top", required = false)Integer top,
			@RequestParam(value = "publicState", required = false)Integer publicState,
			@RequestParam(value = "articleId", required = true) long articleId,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "headImage", required = false ,defaultValue = "") String headImage,
			@RequestParam(value = "context", required = false, defaultValue = "") String context,
			UserDetail<StoreArticle> userDetail) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			StoreArticleRb article = new StoreArticleRb ();
			article.setTop (top);
			article.setPublicState (publicState);
			article.setArticleTitle (title);
			article.setStoreId (userDetail.getId ());
			article.setArticleContext (context);
			article.setId (articleId);
			article.setHeadImage (headImage);
			storeArticleServiceRb.update (article);
//			long storeId = userDetail.getId();
//			StoreArticle storeArticle = storeArticleService.getArticleById(articleId);
//			if (storeArticle == null) {
//				return jsonResponse.setError("该文章不存在！");
//			}
//			if (storeArticle.getStoreId() != storeId) {
//				return jsonResponse.setError("该文章不存在！");
//			}
//			StoreArticle article = new StoreArticle();
//			article.setId(storeArticle.getId());
//			if (title != null) {
//				article.setArticleTitle(title);
//			}
//			if (context != null) {
//				article.setArticleContext(context);
//			}
//			if (headImage != null) {
//				article.setHeadImage(headImage);
//			}
//			article.setUpdateTime(System.currentTimeMillis());
//			storeArticleService.updateArticle(article);
			return jsonResponse.setSuccessful().setData(storeArticleService.selectById(articleId));
		} catch (Exception e) {
			return jsonResponse.setError("修改失败！");
		}
	}

	/**
	 * 文章列表
	 * 
	 * @param title
	 * @param context
	 * @param userDetail
	 * @return
	 */
//	@RequestMapping("/list")
//	@ResponseBody
	@Deprecated
	public JsonResponse getArticleList(@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "context", required = false, defaultValue = "") String context,
			UserDetail<StoreArticle> userDetail, PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			Wrapper<StoreArticle> wrapper = new EntityWrapper<StoreArticle>();
			wrapper.eq("store_id", storeId);
			if (title != null) {
				wrapper.like("article_title", title);
			}
			if (context != null) {
				wrapper.like("article_context", context);
			}
			wrapper.eq("status", StoreArticle.article_status_normal).orderBy("update_time", false);//最新时间排序
			List<StoreArticle> list = storeArticleService.selectList(wrapper, pageQuery);
			List<Map<String, Object>> listMap = new ArrayList<>();
			for (StoreArticle storeArticle : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("articleId", storeArticle.getId());
				map.put("title", storeArticle.getArticleTitle());
				Long updTime = storeArticle.getUpdateTime ();
				map.put("updateTime", updTime == null || updTime == 0L?"": parseLongTime2Str(updTime));
				map.put("headImage", storeArticle.getHeadImage());
				map.put("context", storeArticle.getArticleContext());
				listMap.add(map);
			}
			Wrapper<StoreArticle> wrapper1 = new EntityWrapper<StoreArticle>().eq("store_id", storeId).eq("status", StoreArticle.article_status_normal);
			
			int count = storeArticleService.selectCount(wrapper1 );
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
			Map<String, Object> map = new HashMap<>();
			map.put("pageQuery", pageQueryResult);
			map.put("articleList", listMap);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("查询文章列表失败！");
		}
	}


	/**
	 * 文章列表
	 *
	 * @param title
	 * @param context
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/list/auth")
	@ResponseBody
	@Login
	public JsonResponse getArticleListV379(@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "context", required = false, defaultValue = "") String context,
			UserDetail<StoreArticle> userDetail, PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
/*			Wrapper<StoreArticle> wrapper = new EntityWrapper<StoreArticle>();
			wrapper.eq("store_id", storeId);
			if (title != null) {
				wrapper.like("article_title", title);
			}
			if (context != null) {
				wrapper.like("article_context", context);
			}
			wrapper.eq("status", StoreArticle.article_status_normal).orderBy("update_time", false);//最新时间排序*/
			StoreArticle query = new StoreArticle ();
			query.setStoreId (storeId);
			query.setStatus (StoreArticle.article_status_normal);
			List<StoreArticle> list = storeArticleService.listArticlesSort(query, pageQuery, true);
			List<Map<String, Object>> listMap = new ArrayList<>();
			for (StoreArticle storeArticle : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("articleId", storeArticle.getId());
				map.put("title", storeArticle.getArticleTitle());
				Long updTime = storeArticle.getUpdateTime ();
				map.put("updateTime", updTime == null || updTime == 0L?"": parseLongTime2Str(updTime));
				map.put("headImage", storeArticle.getHeadImage());
				map.put("context", storeArticle.getArticleContext());
				map.put("top", storeArticle.getTop ());
				map.put("publicState", storeArticle.getPublicState ());
				listMap.add(map);
			}
			Wrapper<StoreArticle> wrapper1 = new EntityWrapper<StoreArticle>().eq("store_id", storeId).eq("status", StoreArticle.article_status_normal);

			int count = storeArticleService.selectCount(wrapper1 );
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
			Map<String, Object> map = new HashMap<>();
			map.put("pageQuery", pageQueryResult);
			map.put("articleList", listMap);
			return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError("查询文章列表失败！");
		}
	}

	/**
	 * 修改文章保存
	 */
	@RequestMapping("/closeOrOpen/auth")
	@ResponseBody
	public JsonResponse closeOrOpenArticle(UserDetail<StoreBusiness> userDetail,@RequestParam(value = "status",required = true)int status) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			long storeId = userDetail.getId();
			StoreBusiness storeBusiness = storeBusinessNewService.getById(storeId);
			if (storeBusiness == null) {
				return jsonResponse.setError("门店不存在！请核实storeId："+storeId);
			}
			StoreBusiness newStoreBusiness = new StoreBusiness();
			newStoreBusiness.setId(storeId);
			newStoreBusiness.setWxaArticleShow(status);
			iSmartModuleService.closeOrOpenArticleV372(newStoreBusiness);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			return jsonResponse.setError("修改失败！");
		} 
	}
	
	
	
	private String parseLongTime2Str(long time){
		SimpleDateFormat secFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		secFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return secFormatter.format(new Date(time));
	}
	
}
