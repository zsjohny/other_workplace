package com.yujj.web.controller.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jiuyuan.constant.FavoriteType;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.article.Article;
import com.jiuyuan.entity.favorite.UserFavorite;
import com.jiuyuan.entity.favorite.UserFavoriteVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.ArticleService;
import com.yujj.business.service.FavoriteService;
import com.yujj.business.service.ProductService;
import com.yujj.entity.product.Product;

@Service
public class FavoriteDelegator {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ArticleService articleService;

    @Autowired
    private FavoriteService favoriteService;

    public JsonResponse addFavorite(long id, FavoriteType type, long userId) {
        JsonResponse jsonResponse = new JsonResponse();

        if (type == null) {
        	return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (type == FavoriteType.PRODUCT) {
            Product product = productService.getProductById(id);
            if (product == null) {
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            
        }else if (type == FavoriteType.ARTICLE) {
            Article article = articleService.getArticleById(id);
            if (article == null) {
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
            
        } else if (type == FavoriteType.PARTNER) {
            // unimplements
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		} else {
			return jsonResponse
					.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }

		favoriteService.addFavorite(userId, id, type);

		return jsonResponse.setSuccessful();
    }
    
    public JsonResponse addLike(long id,  long userId) {
    	JsonResponse jsonResponse = new JsonResponse();

    	Article article = articleService.getArticleById(id);
    	if (article == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
    	}		
    	favoriteService.addLike(userId, id);
    	
    	return jsonResponse.setSuccessful();
    }
    

    
    public int getFavoriteArticleCount(long userId) {

    	return  articleService.getFavoriteArticleCount(userId);
    	
    
    }

	/**
	 * 删除用户的某一个收藏内容
	 * 
	 * @param id
	 * @param userId
	 * @param type
	 * @return
	 */
    public JsonResponse deleteUserFavorite(Long id, Long userId, FavoriteType type) {
        JsonResponse jsonResponse = new JsonResponse();

        if (type == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        } else if (type == FavoriteType.PRODUCT) {
            Product product = productService.getProductById(id);
            if (product == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }
        } else if (type == FavoriteType.PARTNER) {
            // unimplements
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
        }else if (type == FavoriteType.ARTICLE) {
            Article article = articleService.getArticleById(id);
            if (article == null) {
                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
            }
        }

        favoriteService.deleteFavorite(userId, id, type);

        return jsonResponse.setSuccessful();
	}

	/**
	 * 按照收藏类型获取收藏列表，按照修改时间倒序排列
	 * 
	 * @param userId
	 * @param type
	 * @param pageQuery
	 * @return
	 */
    public JsonResponse getFavorites(Long userId, FavoriteType type, PageQuery pageQuery) {
		JsonResponse jsonResponse = new JsonResponse();

		if (type == null) {
            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}

        List<UserFavorite> favorites = favoriteService.getFavorites(userId, type, pageQuery);
		List<UserFavoriteVO> voList = new ArrayList<UserFavoriteVO>();

		if (type == FavoriteType.PRODUCT) {
			List<Long> productIds = Lists.transform(favorites,
					new Function<UserFavorite, Long>() {

						@Override
						public Long apply(UserFavorite favorite) {
							return favorite.getRelatedId();
						}
					});

			Map<Long, Product> producMap = productService
					.getProductMap(productIds);

			for (UserFavorite favorite : favorites) {
				if (!producMap.containsKey(favorite.getRelatedId())) {
					continue;
				}
				UserFavoriteVO vo = new UserFavoriteVO();
				vo.setId(favorite.getId());
				vo.setCreateTime(favorite.getCreateTime());
				vo.setProduct(producMap.get(favorite.getRelatedId()));
				voList.add(vo);
			}
		}
		int count = favoriteService.getFavoritesCount(userId, type);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("favorites", voList);
		data.put("pageQuery", pageQueryResult);

		return jsonResponse.setSuccessful().setData(data);
	}
}
