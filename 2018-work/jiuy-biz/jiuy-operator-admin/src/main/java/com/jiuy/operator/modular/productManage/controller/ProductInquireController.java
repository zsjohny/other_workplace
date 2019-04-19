package com.jiuy.operator.modular.productManage.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyuan.service.common.monitor.IProductMonitorService;
import com.jiuyuan.util.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.operator.BadgeMapper;
import com.jiuyuan.entity.newentity.Badge;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.service.common.IBadgeService;
import com.jiuyuan.service.common.ICategoryNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 商品查询控制器
 *
 * @author fengshuonan
 * @Date 2018-01-23 09:53:13
 */
@Controller
@RequestMapping("/productInquire")
@Login
public class ProductInquireController extends BaseController {

    private String PREFIX = "/productManage/productInquire/";
    @Autowired
	private BadgeMapper badgeMapper;
	@Autowired
	private IBadgeService badgeService;
    @Autowired
    private IProductNewService productNewService;
    @Autowired
   	private ICategoryNewService categoryNewService;

    @Autowired
    private IProductMonitorService productMonitorService;

    /**
     * 跳转到商品查询首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "productInquire.html";
    }

    /**
     * 跳转到添加商品查询
     */
    @RequestMapping("/productInquire_add")
    public String productInquireAdd() {
        return PREFIX + "productInquire_add.html";
    }

    /**
     * 跳转到修改商品查询
     */
    @RequestMapping("/productInquire_edit")
    public String productInquireUpdate() {
        return PREFIX + "productInquire_edit.html";
    }
    /**
     * 获取商品查询列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "productIds",required = false,defaultValue = "")String productIds,//商品ids
					   @RequestParam(value = "brandName",required = false,defaultValue = "")String brandName,//品牌名称
					   @RequestParam(value = "state",required = false,defaultValue = "6")int state,//上架状态：默认上架状态
					   @RequestParam(value = "minLadderPriceStart",required = false,defaultValue = "0")double minLadderPriceStart,//起批价格始
					   @RequestParam(value = "minLadderPriceEnd",required = false,defaultValue = "0")double minLadderPriceEnd,//起批价格终
					   @RequestParam(value = "name",required = false,defaultValue = "")String name,//商品标题
					   @RequestParam(value = "categoryIds",required = false,defaultValue = "")String categoryIds,//分类ids
					   @RequestParam(value = "clothesNumber",required = false,defaultValue = "")String clothesNumber,//商品款号
					   @RequestParam(value = "memberLadderPriceFloor",required = false)BigDecimal memberLadderPriceFloor,//会员价过滤查询高值
					   @RequestParam(value = "memberLadderPriceCeil",required = false)BigDecimal memberLadderPriceCeil,//会员价过滤查询低值
					   @RequestParam(value = "badgeStatus",required = false,defaultValue = "-1") long badgeStatus//打标状态0：未达标 -1 全部 -2已打标
    		) {
		    	JsonResponse jsonResponse = new JsonResponse();
		    	try {
		    		Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
		    		List<Map<String,Object>> list  = productNewService.selectProductPage(
							memberLadderPriceFloor, memberLadderPriceCeil,
		    				productIds,brandName,state,minLadderPriceStart,minLadderPriceEnd,name,categoryIds,clothesNumber,badgeStatus,page);
		    		page.setRecords(list);
					productMonitorService.fillOperatorProductList(list,"productId");
		    		return super.packForBT(page);
				} catch (Exception e) {
		    		e.printStackTrace ();
					return jsonResponse.setError("新运营平台查询商品列表e:"+e.getMessage());
				}
    }
    
    @RequestMapping(value = "/getWillClearProductCount")
    @ResponseBody
    public Object getWillClearProductCount(@RequestParam(value = "productIds",required = false,defaultValue = "")String productIds,//商品ids
    		@RequestParam(value = "brandName",required = false,defaultValue = "")String brandName,//品牌名称
    		@RequestParam(value = "state",required = false,defaultValue = "-1")int state,//上架状态：默认上架状态
    		@RequestParam(value = "minLadderPriceStart",required = false,defaultValue = "0")double minLadderPriceStart,//起批价格始
    		@RequestParam(value = "minLadderPriceEnd",required = false,defaultValue = "0")double minLadderPriceEnd,//起批价格终
    		@RequestParam(value = "name",required = false,defaultValue = "")String name,//商品标题
    		@RequestParam(value = "categoryIds",required = false,defaultValue = "")String categoryIds,//分类ids
    		@RequestParam(value = "clothesNumber",required = false,defaultValue = "")String clothesNumber,//商品款号
    		@RequestParam(value = "badgeStatus",required = false,defaultValue = "-1") long badgeStatus//打标状态0：未达标 -1 全部 -2已打标
    		){
    		JsonResponse jsonResponse = new JsonResponse();
    		int count  = productNewService.getWillClearProductCount(productIds, brandName, state, minLadderPriceStart, minLadderPriceEnd, name, categoryIds, clothesNumber, badgeStatus);
    	
    		return jsonResponse.setSuccessful().setData(count);
    		}
    /**
     * 绑定多个id商品角标
     * @param badgeId
     * @param productIds
     * @param badgeName
     * @return
     */
//    @RequestMapping(value = "/bindProductBadgeId")
//    @ResponseBody
//    public JsonResponse bindProductBadgeId(@RequestParam(value = "badgeId",required = true)long badgeId,
//    		@RequestParam(value = "productIds",required = true)String productIds,
//    		@RequestParam(value = "badgeName",required = true)String badgeName){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
//    		int count = productNewService.bindBadgeProduct(badgeId,badgeName,productIds);
//    		return jsonResponse.setSuccessful().setData(count);
//		} catch (Exception e) {
//			return jsonResponse.setError("新运营平台查询商品列表e:"+e.getMessage());
//		}
//    	
//    }
//    /**
//     * 清除多个id商品角标
//     * @param productIds
//     * @return
//     */
//    @RequestMapping(value = "/clearProductBadgeId")
//    @ResponseBody
//    public JsonResponse clearProductBadgeId(@RequestParam(value = "productIds", required = true) String productIds){
//    	JsonResponse jsonResponse = new JsonResponse();
//		try {
//			int count = productNewService.clearProductBadge(productIds);
//			return jsonResponse.setSuccessful().setData(count);
//		} catch (Exception e) {
//			return jsonResponse.setError("清除指定商品id角标e:" + e.getMessage());
//		}
//    	
//    }
//    
//    
    
    /**
     * 按照条件绑定商品角标
     * @param badgeId
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/bindProductBadgeCondition")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse bindProductBadgeCondition(
    			@RequestParam(value = "badgeId",required = true)long badgeId,//角标id
    			@RequestParam(value = "productIds",required = false,defaultValue = "")String productIds,//商品ids
				@RequestParam(value = "brandName",required = false,defaultValue = "")String brandName,//品牌名称
				@RequestParam(value = "state",required = false,defaultValue = "-1")int state,//全部
				@RequestParam(value = "minLadderPriceStart",required = false,defaultValue = "0")double minLadderPriceStart,//起批价格始
				@RequestParam(value = "minLadderPriceEnd",required = false,defaultValue = "0")double minLadderPriceEnd,//起批价格终
				@RequestParam(value = "name",required = false,defaultValue = "")String name,//商品标题
				@RequestParam(value = "categoryIds",required = false,defaultValue = "")String categoryIds,//分类ids
				@RequestParam(value = "clothesNumber",required = false,defaultValue = "")String clothesNumber,//商品款号
				@RequestParam(value = "badgeStatus",required = false,defaultValue = "-1") long badgeStatus//打标状态0：未达标 -1 全部 -2已打标
					){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Wrapper<Badge> wrapper = new EntityWrapper<Badge>().eq("status", 0).eq("id", badgeId);
			List<Badge> list = badgeMapper.selectList(wrapper );
    		if (list.size()<1) {
    			return jsonResponse.setError("角标不存在！");
			}
    		String badgeImage = list.get(0).getBadgeImage();
    		String badgeName = list.get(0).getBadgeName();
    		int count  = productNewService.bindProductBadgeCondition(badgeId,badgeImage,badgeName,productIds, brandName, state, minLadderPriceStart, minLadderPriceEnd, name, categoryIds, clothesNumber, badgeStatus);
    		return jsonResponse.setSuccessful().setData(count);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("新运营平按条件清除角标e:"+e.getMessage());
		}
    	
    }
    
    
    /**
     * 按照条件清除
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/clearProductBadgeCondition")
    @ResponseBody
    public JsonResponse clearProductBadgeCondition(
    			@RequestParam(value = "productIds",required = false,defaultValue = "")String productIds,//商品ids
				@RequestParam(value = "brandName",required = false,defaultValue = "")String brandName,//品牌名称
				@RequestParam(value = "state",required = false,defaultValue = "-1")int state,//全部
				@RequestParam(value = "minLadderPriceStart",required = false,defaultValue = "0")double minLadderPriceStart,//起批价格始
				@RequestParam(value = "minLadderPriceEnd",required = false,defaultValue = "0")double minLadderPriceEnd,//起批价格终
				@RequestParam(value = "name",required = false,defaultValue = "")String name,//商品标题
				@RequestParam(value = "categoryIds",required = false,defaultValue = "")String categoryIds,//分类ids
				@RequestParam(value = "clothesNumber",required = false,defaultValue = "")String clothesNumber,//商品款号
				@RequestParam(value = "badgeStatus",required = false,defaultValue = "-1") long badgeStatus//打标状态0：未达标 -1 全部 -2已打标
					){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int count  = productNewService.clearProductBadgeCondition(productIds, brandName, state, minLadderPriceStart, minLadderPriceEnd, name, categoryIds, clothesNumber, badgeStatus);
    		return jsonResponse.setSuccessful().setData(count);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("新运营平台查询商品列表e:"+e.getMessage());
		}
    	
    }
    
    
    
    /**
	 * 获取角标管理列表
	 */
	@RequestMapping(value = "/badgeList")
	@ResponseBody
	public JsonResponse list() {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			Wrapper<Badge> wrapper = new EntityWrapper<Badge>().eq("status", 0);
			List<Badge> badgeList = badgeMapper.selectList(wrapper);
			List<Map<String,Object>> list = new ArrayList<>();
			for (Badge badge : badgeList) {
				Map<String,Object> map = new HashMap<>();
				map.put("badgeId", badge.getId());
				map.put("badgeName", badge.getBadgeName());
				map.put("badgeImage", badge.getBadgeImage());
				list.add(map);
			}
			return jsonResponse.setSuccessful().setData(list);
		} catch (Exception e) {
			return jsonResponse.setError("获取属性列表e：" + e.getMessage());
		}
	}
	/**
     *  获取商品分类列表，新建编辑商品时用于下拉选择分类
     * productId 商品ID
     * @return
     */
    @RequestMapping(value = "/getProductCategoryList")
    @ResponseBody
    public JsonResponse getProductCategoryList() {
    	JsonResponse jsonResponse = new JsonResponse();
		try {
//	    	获取商品分类列表，用于编辑商品选择商品分类
	    	List<CategoryNew> list = categoryNewService.getProductCategoryList();
	    	List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	    	for(CategoryNew categoryNew : list){
	    		Map<String,Object> map = new HashMap<String,Object>();
	    		map.put("categoryId", categoryNew.getId());//分类ID
	    		map.put("parentId", categoryNew.getParentId());//分类父id，0表示顶级分类
	    		map.put("categoryName", categoryNew.getCategoryName());//分类名称
	    		data.add(map);
	    		
	    	}
//	    	logger.info("获取商品分类列表，新建编辑商品时用于下拉选择分类,data:"+JSON.toJSONString(data));
    		return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 新增商品查询
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return SUCCESS_TIP;
    }

    /**
     * 删除商品查询
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改商品查询
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return SUCCESS_TIP;
    }

    /**
     * 商品查询详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }


	/**
	 * 设置商品会员价
	 *
	 * @param productId 商品id
	 * @param memberPriceJson 会员价json字符串
	 * @param memberLevel 会员等级
	 * @author Charlie
	 * @date 2018/8/14 8:53
	 */
    @RequestMapping("updateMemberPrice")
    @ResponseBody
	public JsonResponse updateMemberPrice(Long productId, String memberPriceJson, Integer memberLevel){
		JsonResponse response = JsonResponse.getInstance ();
		try {
			productNewService.updateMemberPrice (productId, memberPriceJson, memberLevel);
		} catch (Exception e) {
			if (e instanceof BizException) {
				BizException be = (BizException) e;
				return response.setError (be.getMsg ());
			}
			e.printStackTrace ();
			return response.setError (e.getMessage ());
		}

		return response.setSuccessful ();
	}

}
