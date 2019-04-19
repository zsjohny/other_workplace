package com.e_commerce.miscroservice.crm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotations.application.AnnotationUtils;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.crm.dao.CustomerPoolDao;
import com.e_commerce.miscroservice.crm.dao.UserDao;
import com.e_commerce.miscroservice.crm.entity.User;
import com.e_commerce.miscroservice.crm.entity.emuns.CustomerTypeEnums;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolGetRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import com.e_commerce.miscroservice.crm.entity.response.CustomerPoolResponse;
import com.e_commerce.miscroservice.crm.po.CustomerPoolPO;
import com.e_commerce.miscroservice.crm.service.CustomerPoolService;
import com.e_commerce.miscroservice.crm.utils.ExcelDoUtils;
import com.e_commerce.miscroservice.crm.utils.ExcelUtil;
import com.e_commerce.miscroservice.crm.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 21:51
 * @Copyright 玖远网络
 */
@Service
public class CustomerPoolServiceImpl implements CustomerPoolService {
    Log logger = Log.getInstance(CustomerPoolService.class);
    @Resource
    private CustomerPoolDao customerPoolDao;
    @Resource
    private UserDao userDao;
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";


    @Override
    public Response findAllCustomerPool(CustomerPoolsFindRequest request) {
        logger.info("查询所有公海池用户");
        if (request.getStatus()!=null&&request.getStatus()==1){
            request.setType("已领取");
        }else {
            request.setUserId(null);
        }
        PageHelper.startPage(request.getPageNumber(),request.getPageSize());
        List<CustomerPoolResponse> list = customerPoolDao.findAllCustomerPool(request);
        PageInfo<CustomerPoolResponse> poolResponsePageInfo = new PageInfo<CustomerPoolResponse>(list);
        return Response.success(poolResponsePageInfo);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addCustomer(CustomerPoolAddRequest request) {
        logger.info("添加公海池用户 addStatus={}",request.getAddStatus());
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            return Response.error(map.get("message"));
        }
        if (request.getAddStatus()==0){
            request.setType("未领取");
        }else
        if (request.getAddStatus()==1){
            request.setType("已领取");
        }else {
            logger.warn("添加公海池类型错误");
            return Response.error("添加公海池类型错误");
        }
        CustomerPoolPO customerPoolPO = customerPoolDao.findCustomByPhone(request.getPhone());
        if (customerPoolPO!=null){
            logger.warn("该用户Phone={}已存在",request.getPhone());
            return Response.error("该用户已存在");
        }
        int i = customerPoolDao.addCustomer(request);
        if (i==0){
           return Response.error("添加失败");
        }
        return Response.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response addCustomerExcels(MultipartFile file, Long userId, Integer addStatus) {
        if (file==null||addStatus==null){
            logger.warn("参数为空");
            return Response.error("参数为空");
        }
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //07以前excel 后缀为xls  07以后 excel  后缀 为 xlsx
        try {
            if (fileName.endsWith(xls) || fileName.endsWith(xlsx)) {
//                Map<String,Object> jsonObject = ExcelUtil.readExcelPOI(userId);

                   Map<String,Object> jsonObject = ExcelUtil.readExcelPOI(file,userId,addStatus);
                logger.info("msg={}", jsonObject.get("msg"));
                //所有excel导入的客户 集合
                List<CustomerPoolAddRequest> list = (List<CustomerPoolAddRequest>)jsonObject.get("list");
                if (list==null||list.size()==0){
                    logger.warn("未发现 新客户 请核对 内容是否正确");
                    return Response.error("未发现 新客户 请核对 内容是否正确");
                }
                List<CustomerPoolPO> customerPoolPOList = customerPoolDao.findCustomerListByPhone(list);
                List<CustomerPoolAddRequest> listNotIn = new ArrayList<>();
                Map<String,String> mapIn = new HashMap<>();
                customerPoolPOList.forEach(customerPoolPO -> {
                        mapIn.put(customerPoolPO.getPhone(),customerPoolPO.getPhone());
                });
                if (customerPoolPOList.size()>0){
                    list.forEach(request -> {
                        String phone = mapIn.get(request.getPhone());
                        if (phone == null){
                            listNotIn.add(request);
                        }
                    });
                    if (listNotIn.size()>0){
                        customerPoolDao.addCustomerList(listNotIn);
                    }
                }else {
                    customerPoolDao.addCustomerList(list);
                }
                //与数据库比对 筛选 未导入的客户
//                list.forEach(request -> {
//                    CustomerPoolPO poolPO = customerPoolDao.findCustomByPhone(request.getPhone());
//                    if (poolPO==null){
//                        listNotIn.add(request);
//                    }
//                });
//                if (listNotIn!=null&&listNotIn.size()>0){
//                    customerPoolDao.addCustomerList(listNotIn);
//                }
                return Response.success(jsonObject.get("msg"));
            } else {
                logger.warn("文件格式错误");
                return Response.error("文件格式错误");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

            return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response upCustomers(CustomerPoolGetRequest request) {
        Map map = AnnotationUtils.validate(request);
        if (map.get("result")==Boolean.FALSE){
            logger.warn("未选择客户");
            return Response.error("未选择客户");
        }
        String[] ids = request.getIds().split(",");
        if (0==request.getStatus()){
            return received(ids,request.getUserId());
        }
        if (1==request.getStatus()){
            return backReceived(ids,request.getUserId());
        }
        if (2==request.getStatus()){
            if (StringUtils.isEmpty(request.getAllotName())){
                logger.warn("分配的用户为空");
                return Response.error("分配人为空");
            }
            User user = userDao.findUserByUserId(request.getUserId());
            if (user==null){
                logger.warn("操作人员不存在 请重新登陆");
                return Response.error("操作人员不存在 请重新登陆");
            }
            if (!"11".equals(user.getUserType())){
               logger.warn("用户没有权限");
                return Response.error("用户没有权限");
            }
            return allot(ids,request.getUserId(),request.getAllotName());
        }
        return Response.error("操作失败");
    }

    /**
     * 分配
     * @param ids
     * @param userId
     * @param allotName
     * @return
     */
    private Response allot(String[] ids, Long userId,String allotName) {
        List<CustomerPoolPO> list = customerPoolDao.findCustomerListByIds(ids, CustomerTypeEnums.NOT_RECEIVED.getKey());
        if (list.size()<ids.length){
            logger.warn("选择的客户 包含其他状态的客户");
            return Response.error("选择的客户 包含已领取状态的客户");
        }
        User user = userDao.findUserByName(allotName);
        if (user==null){
            logger.warn("分配人员不存在");
            return Response.error("分配人员不存在");
        }
        customerPoolDao.updateCustomers(ids,Long.valueOf(user.getUserId()),CustomerTypeEnums.RECEIVED.getKey());
        return Response.success();
    }

    /**
     * 领取
     * @return
     */
    public Response received(String[] ids,Long  userId){
        List<CustomerPoolPO> list = customerPoolDao.findCustomerListByIds(ids, CustomerTypeEnums.NOT_RECEIVED.getKey());
        if (list.size()<ids.length){
            logger.warn("选择的客户 包含其他状态的客户");
            return Response.error("选择的客户 包含已领取状态的客户");
        }
        customerPoolDao.updateCustomers(ids,userId,CustomerTypeEnums.RECEIVED.getKey());
        return Response.success();
    }
    /**
     * 退回公海池
     */
    public Response backReceived(String[] ids,Long  userId){
        List<CustomerPoolPO> list = customerPoolDao.findCustomerListByIds(ids, CustomerTypeEnums.RECEIVED.getKey());
        if (list.size()<ids.length){
            logger.warn("选择的客户 包含其他状态的客户");
            return Response.error("选择的客户 包含其他状态的客户");
        }
        customerPoolDao.updateCustomers(ids,userId,CustomerTypeEnums.NOT_RECEIVED.getKey());
        return Response.success();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response test() {
        Long tim = System.currentTimeMillis();
        try {
            CustomerPoolAddRequest customerPoolAddRequest = new CustomerPoolAddRequest();
            customerPoolAddRequest.setAddStatus(0);
            customerPoolAddRequest.setUserId(1000L);
            if (customerPoolAddRequest.getAddStatus()==0){
                customerPoolAddRequest.setType("未领取");
            }else
            if (customerPoolAddRequest.getAddStatus()==1){
                customerPoolAddRequest.setType("已领取");
            }else {
                logger.warn("添加状态 错误");
                return Response.error("添加状态 错误");
            }
                Map<String,Object> jsonObject = ExcelDoUtils.readExcelPOIT(customerPoolAddRequest);
//                Map<String,Object> jsonObject = new HashMap<>();
                logger.info("msg={}", jsonObject.get("msg"));
                //所有excel导入的客户 集合
                List<CustomerPoolAddRequest> list = (List<CustomerPoolAddRequest>)jsonObject.get("list");
            if (list==null||list.size()==0){
                logger.warn("未发现 新客户 请核对 内容是否正确");
                return Response.error("未发现 新客户 请核对 内容是否正确");
            }
                List<CustomerPoolPO> customerPoolPOList = customerPoolDao.findCustomerListByPhone(list);
                List<CustomerPoolAddRequest> listNotIn = new ArrayList<>();
                //与数据库比对 筛选 未导入的客户
                list.forEach(request -> {
                    CustomerPoolPO poolPO = customerPoolDao.findCustomByPhone(request.getPhone());
                    if (poolPO==null){
                        listNotIn.add(request);
                    }
                });
                if (listNotIn!=null&&listNotIn.size()>0){
                    customerPoolDao.addCustomerList(listNotIn);
                }
                System.out.println("time->"+(System.currentTimeMillis()-tim));
                return Response.success(jsonObject.get("msg"));

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
