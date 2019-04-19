package com.jiuy.operator.modular.urserManage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.GroundWithdrawCashRecordMapper;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.entity.newentity.GroundWithdrawCashRecord;
import com.jiuyuan.service.common.IGroundUserService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 用户管理控制器
 *
 * @author fengshuonan
 * @Date 2017-11-02 14:19:17
 */
@Controller
@RequestMapping("/pushManage")
@Login
public class PushManageController extends BaseController {
	private static final Log logger = LogFactory.get("PushManageController");
    private String PREFIX = "/urserManage/pushManage/";
    
    private static final int REGION_MANAGER = 1;
    private static final int PROVINCE_MANAGER = 2;
    private static final int AREA_MANAGER = 3;
    private static final int CITY_MANAGER = 4;

    @Autowired
    private IGroundUserService groundUserService;
    
    @Autowired
    private GroundUserMapper groundUserMapper;
    
    @Autowired
	private GroundWithdrawCashRecordMapper groundWithdrawCashRecordMapper;
    /**
     * 跳转到用户管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "pushManage.html";
    }

    /**
     * 跳转到添加用户管理
     */
    @RequestMapping("/pushManage_add")
    public String pushManageAdd() {
        return PREFIX + "pushManage_add.html";
    }

    /**
     * 跳转到修改用户管理
     */
    @RequestMapping("/pushManage_edit")
    public String pushManageEdit() {
        return PREFIX + "pushManage_edit.html";
    }


	/**
	 * 获取地推人员列表
	 * @param name 姓名
	 * @param phone 手机号码
	 * @param userType 职位类型 -1:全部,1:大区总监,2:省区经理,3:区域主管,4:城市经理
	 * @param province 省名
	 * @param city 城市名
	 * @param district 区名
	 * @param pname 上级姓名
	 * @param pphone 上级手机号
	 * @param minClientCount 客户数下限
	 * @param maxClientCount 客户数上限
	 * @param minTotalSales 销售额下限
	 * @param maxTotalSales 销售额上限
	 * @param managerId 管理员ID
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(@RequestParam(value = "name" , required = false, defaultValue = "") String name,
			                 @RequestParam(value = "id",required = false,defaultValue = "0") long id,
			                 @RequestParam(value = "phone" , required = false, defaultValue = "") String phone,
			                 @RequestParam(value = "userType" , required = false, defaultValue = "-1") int userType,
			                 @RequestParam(value = "province" , required = false, defaultValue = "") String province,
			                 @RequestParam(value = "city" , required = false, defaultValue = "") String city,
			                 @RequestParam(value = "district" , required = false, defaultValue = "") String district,
			                 @RequestParam(value = "pname" , required = false, defaultValue = "") String pname,
			                 @RequestParam(value = "pphone" , required = false, defaultValue = "") String pphone,
			                 @RequestParam(value = "minClientCount" , required = false) Integer minClientCount,
			                 @RequestParam(value = "maxClientCount" , required = false) Integer maxClientCount,
			                 @RequestParam(value = "minTotalSales" , required = false) Double minTotalSales,
			                 @RequestParam(value = "maxTotalSales" , required = false) Double maxTotalSales,
			                 @RequestParam(value = "managerId" ,required = false,defaultValue = "") String managerId){
		
//		logger.info("获取地推人员列表");
		JsonResponse jsonResponse = new JsonResponse();
		Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		try {
			data = groundUserService.listPage(page,name,id,phone,userType,province,city,district,pname,pphone,minClientCount,maxClientCount,minTotalSales,maxTotalSales,managerId);
			page.setRecords(data);
			return super.packForBT(page);	
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 获取职位类型数量
	 * @param name 姓名
	 * @param phone 手机号码
	 * @param userType 职位类型 -1:全部,1:大区总监,2:省区经理,3:区域主管,4:城市经理
	 * @param province 省名
	 * @param city 城市名
	 * @param district 区名
	 * @param pname 上级姓名
	 * @param pphone 上级手机号
	 * @param minClientCount 客户数下限
	 * @param maxClientCount 客户数上限
	 * @param minTotalSales 销售额下限
	 * @param maxTotalSales 销售额上限
	 * @param managerId 管理员ID
	 * @return
	 */
	@RequestMapping(value = "/count")
	@ResponseBody
	public Object count(@RequestParam(value = "name" , required = false, defaultValue = "") String name,
			                 @RequestParam(value = "id",required = false, defaultValue= "0") long id,
			                 @RequestParam(value = "phone" , required = false, defaultValue = "") String phone,
			                 @RequestParam(value = "userType" , required = false, defaultValue = "-1") int userType,
			                 @RequestParam(value = "province" , required = false, defaultValue = "") String province,
			                 @RequestParam(value = "city" , required = false, defaultValue = "") String city,
			                 @RequestParam(value = "district" , required = false, defaultValue = "") String district,
			                 @RequestParam(value = "pname" , required = false, defaultValue = "") String pname,
			                 @RequestParam(value = "pphone" , required = false, defaultValue = "") String pphone,
			                 @RequestParam(value = "minClientCount" , required = false) Integer minClientCount,
			                 @RequestParam(value = "maxClientCount" , required = false) Integer maxClientCount,
			                 @RequestParam(value = "minTotalSales" , required = false) Double minTotalSales,
			                 @RequestParam(value = "maxTotalSales" , required = false) Double maxTotalSales,
			                 @RequestParam(value = "managerId" ,required = false,defaultValue = "") String managerId){
		JsonResponse jsonResponse = new JsonResponse();
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		try {
			data = groundUserService.count(name,id,phone,userType,province,city,district,pname,pphone,minClientCount,maxClientCount,minTotalSales,maxTotalSales,managerId);
			return jsonResponse.setSuccessful().setData(data);	
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
	}

    /**
     * 添加地推人员
     * @param status 状态(-1：删除 0：可用  1：禁用)
     * @param name 用户姓名
     * @param phone 手机号码
     * @param userType 地推角色id 1:大区总监 2:省区经理3:区域主管4:城市经理
     * @param pid 大区总监 这里填0
     * @param pphone 上级手机 大区总监 这里填""
     * @param pname 上级姓名 大区总监 这里填""
     * @param puserType 上级的地推角色类型 1:大区总监 2:省区经理3:区域主管4:城市经理 大区总监 这里填 0
     * @param province 省
     * @param city 城市
     * @param district 区域
     * @param bankAccountNo 银行开户号
     * @param bankAccountName 银行开户名
     * @param bankName 开户银行
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse add(@RequestParam( value = "status") int status,
    		                @RequestParam( value = "name") String name,
    		                @RequestParam( value = "phone") String phone,
    		                @RequestParam( value = "userType") int userType,
    		                @RequestParam( value = "pid") long pid,
    		                @RequestParam( value = "pphone") String pphone,
    		                @RequestParam( value = "pname") String pname,
    		                @RequestParam( value = "puserType") int puserType,
    		                @RequestParam( value = "province") String province,
    		                @RequestParam( value = "city") String city,
    		                @RequestParam( value = "district") String district,
    		                @RequestParam( value = "bankAccountNo") String bankAccountNo,
    		                @RequestParam( value = "bankAccountName") String bankAccountName,
    		                @RequestParam( value = "bankName") String bankName) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		//判断手机号码是否唯一
    		if(!checkPhone(phone,-1)){
    			throw new RuntimeException("手机号码已被使用");
    		}
    		//判断是否填全了上级资料
    		if(userType != REGION_MANAGER&&!checkSupervisor(pid,pphone,pname,userType)){
    			throw new RuntimeException("填写的上级资料不正确");
    		}
    		//添加地推人员
    		groundUserService.add(status,name,phone,userType,pid,pphone,pname,puserType,province,city,district,bankAccountNo,bankAccountName,bankName);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
        
    }
    
    /**
     * 获取上级信息
     */
    @RequestMapping("/getSupervisorInfo")
    @ResponseBody
    public JsonResponse getSupervisorInfo(@RequestParam(value = "userType") int userType,
    		                              @RequestParam(value = "phone") String phone){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		Map<String,Object> data = groundUserService.getSupervisorInfo(userType,phone);			
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    
    
    /**
     * 检测上级是否存在
     * @param pid
     * @param pphone
     * @param ppname
     * @return true 为填写正确 false 为不正确
     */
    private boolean checkSupervisor(long pid, String pphone, String ppname, int userType) {
    	GroundUser groundUser = groundUserMapper.selectById(pid);
    	if(groundUser == null){
    		return false;
    	}
    	int pUserType = groundUser.getUserType();
    	String phone = groundUser.getPhone();
    	String name = groundUser.getName();
    	//非城市经理的userType
    	if(userType != CITY_MANAGER && pUserType + 1 != userType){
    		return false;
    	}
    	//城市经理的userType
    	if(userType == CITY_MANAGER ){
    		if(pUserType != CITY_MANAGER && pUserType != AREA_MANAGER){
    			return false;
    		}
    	}
    	if(!phone.equals(pphone) || !name.equals(ppname) ){
    		return false;
    	}
		return true;
	}

	/**
     * 检测手机号码是否唯一
     * @param phone
     * @param groundUserId
     * @return true 为唯一   false为不唯一
     */
    private boolean checkPhone(String phone, long groundUserId) {
    	//获取该手机号的用户
    	Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>();
    	wrapper.eq("phone", phone);
    	List<GroundUser> list = groundUserMapper.selectList(wrapper);
    	for(GroundUser groundUser :list){
    		long id = groundUser.getId();
    		if(id != groundUserId){
    			return false;
    		}
    	}
		return true;
	}

	/**
     * 删除用户管理 
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改地推用户状态
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @AdminOperationLog
    @Transactional(rollbackFor = Exception.class)
    public JsonResponse update(@RequestParam(value = "status") int status,
    		                   @RequestParam(value = "id") int id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	//更改是否可用的状态
    	try {
    		GroundUser groundUser = groundUserMapper.selectById(id);
    		
    		GroundUser groundUser2=new GroundUser();
    		groundUser2.setId(groundUser.getId());
    		groundUser2.setStatus(status);
    		groundUserMapper.updateById(groundUser2);
    		Wrapper<GroundWithdrawCashRecord> wrapper = new EntityWrapper<GroundWithdrawCashRecord>();
    		if (status==0) {
    			wrapper.eq("ground_user_id", id).eq("withdraw_cash_status", GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE);
    			List<GroundWithdrawCashRecord> groundWithdrawCashRecordList = groundWithdrawCashRecordMapper.selectList(wrapper);
    			for (GroundWithdrawCashRecord groundWithdrawCashRecord : groundWithdrawCashRecordList) {
    				GroundWithdrawCashRecord cashRecord=new GroundWithdrawCashRecord();
    				cashRecord.setId(groundWithdrawCashRecord.getId());
    				Integer withdrawType = groundWithdrawCashRecord.getWithdrawType();
    				//0：主动申请
    				if (withdrawType==0) {
    					cashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING);
					}
    				//1：自动提现
    				if (withdrawType==1) {
    					cashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE);
					}
    				groundWithdrawCashRecordMapper.updateById(cashRecord);
    			}
			}
    		if(status==1){
    			wrapper.eq("ground_user_id", id).eq("withdraw_cash_status", GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_DOING);
    			List<GroundWithdrawCashRecord> groundWithdrawCashRecordList = groundWithdrawCashRecordMapper.selectList(wrapper);
    			for (GroundWithdrawCashRecord groundWithdrawCashRecord : groundWithdrawCashRecordList) {
    				GroundWithdrawCashRecord cashRecord=new GroundWithdrawCashRecord();
    				cashRecord.setId(groundWithdrawCashRecord.getId());
    				cashRecord.setWithdrawCashStatus(GroundWithdrawCashRecord.WITHDRAWCASHSTATUS_FREEZE);
    				groundWithdrawCashRecordMapper.updateById(cashRecord);
    			}
    		}
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }

    /**
     * 地推用户详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public JsonResponse detail(@RequestParam(value = "id") int id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
			GroundUser groundUser = groundUserMapper.selectById(id);
    		return jsonResponse.setSuccessful().setData(groundUser);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    

    
    
//    /**
//     * 获取下级列表
//     */
//    @RequestMapping(value = "/getSubordinateList")
//    @ResponseBody
//    public JsonResponse getSubordinateList(@RequestParam("id") int id){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
//			
//		} catch (Exception e) {
//			jsonResponse.setError(e.getMessage());
//		}
//    	return jsonResponse.setSuccessful();
//    }
}
