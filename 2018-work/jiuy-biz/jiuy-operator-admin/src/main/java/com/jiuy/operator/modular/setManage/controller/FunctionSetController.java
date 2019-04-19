package com.jiuy.operator.modular.setManage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ActivityPlaceNewMapper;
import com.jiuyuan.dao.mapper.supplier.CategoryNewMapper;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.newentity.ActivityPlaceNew;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.CategoryNew;
import com.jiuyuan.entity.newentity.HomeMenu;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.TagNew;
import com.jiuyuan.service.common.IBrandNewService;
import com.jiuyuan.service.common.IHomeMenuService;
import com.jiuyuan.service.common.ITagNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

/**
 * 功能设置控制器
 *
 * @author fengshuonan
 * @Date 2018-02-23 16:36:41
 */
@Controller
@RequestMapping("/functionSet")
public class FunctionSetController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(FunctionSetController.class);
    private String PREFIX = "/setManage/functionSet/";
    @Autowired
    private IHomeMenuService homeMenuService;
    
    
    @Autowired
    private CategoryNewMapper categoryNewMapper;
	@Autowired
	private IBrandNewService brandNewService;
	@Autowired
	private ITagNewService tagNewService;
	@Autowired
	private ActivityPlaceNewMapper activityPlaceNewMapper;
	@Autowired
	private ProductNewMapper productNewMapper;

	
   
    /**
     * 获取app首页菜单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody 
    public Object list(){
    	Page<HomeMenu> page = new PageFactory<HomeMenu>().defaultPage();
		try {
			//获取所有订单
			List<HomeMenu> list  = homeMenuService.getHomeMenuList(page);
			page.setRecords(list);
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取app首页菜单列表:"+e.getMessage());
    		throw new RuntimeException("获取app首页菜单列表:"+e.getMessage());
		}
    }
    
   
    /**
     * 获取app首页菜单
     */
    @RequestMapping(value = "/info")
   	@ResponseBody
   	public JsonResponse info(
   			@RequestParam(value = "homeMenuId",required = true) long homeMenuId//菜单ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			HomeMenu  homeMenu = homeMenuService.getHomeMenu(homeMenuId);
   			return jsonResponse.setSuccessful().setData(homeMenu);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError("获取app首页菜单：" + e.getMessage());
   		}
   	}
    
    /**
   	 * 添加功能设置
   	 */
    @RequestMapping(value = "/add")
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse add(@RequestParam(value = "menuName",required = true) String menuName,//菜单名称
   			@RequestParam(value = "menuType",required = true) int menuType,//跳转类型（ 3：商品，4：类目8：品牌首页  14:标签商品 15:专场 ）
   			@RequestParam(value = "targetId",required = true) long targetId,//跳转目标ID
   			@RequestParam(value = "weight",required = false,defaultValue = "0") int weight//排序权重
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			checkMenuNameRepeat(menuName,0);
   			checkIdValid(menuType, targetId);
   			homeMenuService.addHomeMenu( menuName, menuType, targetId, weight);
   			return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
   	}
    
    
    
   
    /**
     *  验证菜单名称重复
     * @param menuName
     */
    private void checkMenuNameRepeat(String menuName,long homeMenuId) {
    	List<HomeMenu> homeMenuList = homeMenuService.getHomeMenuListByMenuName(menuName);
     	if(homeMenuList != null && homeMenuList.size() > 0){
     		HomeMenu homeMenu = homeMenuList.get(0);
     		if(homeMenu.getId() != homeMenuId){
     			throw new RuntimeException("菜单已存在");
     		}
    	}
	}


	/**
   	 * 修改功能设置
   	 */
    @RequestMapping(value = "/update")
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse update(
   			@RequestParam(value = "homeMenuId",required = true) long homeMenuId,//菜单ID
   			@RequestParam(value = "menuName",required = true) String menuName,//菜单名称
   			@RequestParam(value = "menuType",required = true) int menuType,//跳转类型（ 3：商品，4：类目8：品牌首页  14:标签商品 15:专场 ）
   			@RequestParam(value = "targetId",required = true) long targetId,//跳转目标ID
   			@RequestParam(value = "weight",required = false,defaultValue = "0") int weight//排序权重
   			) {
    	logger.info("menuType:"+menuType+",targetId:"+targetId);
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			checkMenuNameRepeat(menuName,homeMenuId);
   			checkIdValid(menuType, targetId);
   			logger.info("menuType:"+menuType+",targetId:"+targetId+",验证通过");
   			homeMenuService.updHomeMenu(homeMenuId, menuName, menuType, targetId, weight);
   			return jsonResponse.setSuccessful();
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError( e.getMessage());
   		}
   	}

    /**
     * 
     * @param menuType menuType跳转类型  组类型：跳转类型（ 3：商品，4：类目8：品牌首页  14:标签商品 15:专场 ）
     * @param targetId
     */
	private void checkIdValid(int menuType, long targetId) {
		if( menuType == 3){
			ProductNew productNew = productNewMapper.selectById(targetId);
			
			logger.info("menuType:"+menuType+",targetId:"+targetId+",productNew:"+productNew);
			if(productNew == null){
				throw new RuntimeException("指定跳转目标ID不存在");
			}else{
				if(productNew.getState() != 6){
					throw new RuntimeException("该商品不为上架状态，不可设置");
				}
			}
		}else if(menuType == 4){
			CategoryNew categoryNew = categoryNewMapper.selectById(targetId);
			logger.info("menuType:"+menuType+",targetId:"+targetId+",categoryNew:"+categoryNew);
			if(categoryNew == null){
				throw new RuntimeException("指定跳转目标ID不存在");
			}else if(categoryNew.getStatus() != 0){
				throw new RuntimeException("该商品不为上架状态，不可设置");
			}else if(categoryNew.getCategoryType() != 0){
				throw new RuntimeException("该分类类型不正确");
			}
		}else if(menuType == 8){
			BrandNew brand = brandNewService.getBrandByBrandId(targetId);
			logger.info("menuType:"+menuType+",targetId:"+targetId+",brand:"+brand);
			if(brand == null){
				throw new RuntimeException("指定跳转目标ID不存在");
			}else if(brand.getStatus() != 0){
				throw new RuntimeException("该品牌不可用");
			} 
		}else if(menuType == 14){
			TagNew tagNew = tagNewService.selectById(targetId);
			logger.info("menuType:"+menuType+",targetId:"+targetId+",tagNew:"+tagNew);
			if(tagNew == null){
				throw new RuntimeException("指定跳转目标ID不存在");
			}else if(tagNew.getStatus() != 0){
				throw new RuntimeException("该标签不可用");
			}
		}else if(menuType == 15){
			ActivityPlaceNew activityPlaceNew = activityPlaceNewMapper.selectById(targetId);
			logger.info("menuType:"+menuType+",targetId:"+targetId+",activityPlaceNew:"+activityPlaceNew);
			if(activityPlaceNew == null){
				throw new RuntimeException("指定跳转目标ID不存在");
			}else if(activityPlaceNew.getStatus() != 0){
				throw new RuntimeException("该专场不可用");
			}
		}
	}
    
    
    /**
     * 删除功能设置
     */
    @RequestMapping(value = "/delete")
   	@ResponseBody
   	@AdminOperationLog
   	public JsonResponse delete(
   			@RequestParam(value = "homeMenuId",required = true) long homeMenuId//菜单ID
   			) {
   		JsonResponse jsonResponse = new JsonResponse();
   		try {
   			homeMenuService.delHomeMenu(homeMenuId);
   			return jsonResponse.setSuccessful();
   		} catch (Exception e) {
   			e.printStackTrace();
   			return jsonResponse.setError("添加功能设置e：" + e.getMessage());
   		}
   	}
    /**
     * 跳转到功能设置首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "functionSet.html";
    }

    /**
     * 跳转到添加功能设置
     */
    @RequestMapping("/functionSet_add")
    public String functionSetAdd() {
        return PREFIX + "functionSet_add.html";
    }

    /**
     * 跳转到修改功能设置
     */
    @RequestMapping("/functionSet_update")
    public String functionSetUpdate() {
        return PREFIX + "functionSet_edit.html";
    }
    /**
     * 功能设置详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
