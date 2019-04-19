package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.entity.article.ArticleVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.facade.ArticleFacade;
import com.yujj.entity.account.UserDetail;
import com.yujj.util.uri.UriBuilder;
import com.yujj.web.controller.delegate.FavoriteDelegator;

@Controller
@Login
@RequestMapping("/mobile/user/favorite")
public class MobileUserFavoriteController {

    @Autowired
    private FavoriteDelegator favoriteDelegator;
    
	
	@Autowired
	private ArticleFacade articleFacade;
	

    @RequestMapping("/add")
    @ResponseBody
    public JsonResponse addFavorite(@RequestParam("id") long id, @RequestParam("type") FavoriteType type,
                                    UserDetail userDetail) {
        return favoriteDelegator.addFavorite(id, type, userDetail.getUserId());
    }
    
    @RequestMapping("/addLike")
    @ResponseBody
    public JsonResponse addLike(@RequestParam("id") long id, UserDetail userDetail) {
    	return favoriteDelegator.addLike(id, userDetail.getUserId());
    }
    
    
    
    @RequestMapping("/favoriteArticleList")
    @ResponseBody
    public JsonResponse favoriteArticleList(PageQuery pageQuery, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	List<ArticleVO> articleList = articleFacade.getFavoriteArticleList(userDetail, pageQuery);
    	
    	int totalCount = favoriteDelegator.getFavoriteArticleCount(userDetail.getUserId());
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("articleList", articleList);
        data.put("pageQuery", pageQueryResult);
        if (pageQueryResult.isMore()) {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL + "/mobile/user/favorite/favoriteArticleList.json");
            builder.set("page", pageQueryResult.getPage() + 1);
            builder.set("pageSize", pageQueryResult.getPageSize());
            data.put("nextDataUrl", builder.toUri());
        }
        
    	
    	return jsonResponse.setSuccessful().setData(data);
    }

    /**
     * 删除用户收藏
     * 
     * @param id
     * @param type
     * @param userDetail
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResponse deleteFavorite(@RequestParam("id") long id, @RequestParam("type") FavoriteType type,
                                       UserDetail userDetail) {
        return favoriteDelegator.deleteUserFavorite(id, userDetail.getUserId(), type);
    }

    /**
     * 获取用户收藏列表
     * 
     * @param id
     * @param type
     * @param userDetail
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public JsonResponse getFavorites(@RequestParam("type") FavoriteType type, UserDetail userDetail, PageQuery pageQuery) {
        return favoriteDelegator.getFavorites(userDetail.getUserId(), type, pageQuery);
    }

}
