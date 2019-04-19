package com.e_commerce.miscroservice.crm.controller;


import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.helper.util.application.conver.DownFilesUtils;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolAddRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolGetRequest;
import com.e_commerce.miscroservice.crm.entity.request.CustomerPoolsFindRequest;
import com.e_commerce.miscroservice.crm.service.CustomerPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 客户管理_公海池
 *
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 21:00
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping("/pool")
public class CustomerPoolController {

    @Autowired
    private CustomerPoolService customerPoolService;

    /**
     * 查询所有的公海池用户-客户管理
     *
     * @param name      用户姓名
     * @param phone     用户手机号
     * @param province  省
     * @param city      市
     * @param district  区
     * @param timeStart 创建时间 起
     * @param timeEnd   创建时间 止
     * @param type      用户类型
     * @param belonger  所属员工用户姓名
     * @param userId    用户所属id
     * @param status    查询状态 0：所有的 公海池 1： 所属id下的 用户
     * @return
     */
    @Consume(CustomerPoolsFindRequest.class)
    @RequestMapping("/find/all")
    public Response findAllCustomerPool(String name, String phone, String province, String city, String district, String timeStart,
                                        String timeEnd, Integer type, String belonger, Long userId, Integer status, Integer pageSize,
                                        Integer pageNumber) {

        return customerPoolService.findAllCustomerPool((CustomerPoolsFindRequest) ConsumeHelper.getObj());
    }


    /**
     * 添加客户
     *
     * @param userId               归属用户id
     * @param businessName         企业名称
     * @param artificialPersonName 法人姓名
     * @param businessLicence      营业执照
     * @param businessUrl          公司网址
     * @param position             职位
     * @param customerSource       客户来源
     * @param customerGrade        客户类型
     * @param name                 姓名
     * @param phone                客户电话
     * @param province             省
     * @param city                 市
     * @param district             区
     * @param profession           所属行业
     * @param mainBusiness         主营业务
     * @param type                 状态 0：未领取 1：已领取 2：已废弃 3：已签约
     * @param addStatus            添加状态 0公海池 1 客户管理池
     * @param lastestRecord        最新跟进记录
     * @param belonger             归属人
     * @return
     */
    @Consume(CustomerPoolAddRequest.class)
    @RequestMapping("/customer/add")
    public Response addCustomer(
            Long userId, String businessName, String artificialPersonName, String businessLicence, String businessUrl, String position,
            String customerSource, String customerGrade, String name, String phone, String province, String city, String district,
            String profession, String mainBusiness, Integer type, Integer addStatus, String lastestRecord, String belonger
    ) {

        return customerPoolService.addCustomer((CustomerPoolAddRequest) ConsumeHelper.getObj());
    }


    /**
     * 通过 excel添加 客户
     *
     * @param file      excel文件
     * @param userId    所属用户id
     * @param addStatus 添加状态 0  公海池 1  客户管理
     *                  {
     *                  "msg": "",
     *                  "code": 200,
     *                  "data": {}
     *                  }
     * @return com.e_commerce.miscroservice.commons.utils.Response
     * @author hyf
     * @date 2018/9/19 23:25
     */
    @RequestMapping("/excel/add")
    public Response addCustomerExcels(MultipartFile file, Long userId, Integer addStatus) {

        return customerPoolService.addCustomerExcels(file, userId,  addStatus);
    }

    @Consume( CustomerPoolAddRequest.class)
    @RequestMapping("/test")
    public Response test() {
        return customerPoolService.test();
    }

    /**
     * 处理 公海池用户 领取-退回-分配
     *
     * @param ids       客户id  用“,”分割
     * @param userId    所属id
     * @param status    领取 退回  公海池状态 0 领取  1 退回 2分配
     * @param allotName 被分配的用户 姓名
     *                  "msg": "",
     *                  * "code": 200,
     *                  * "data": {}
     *                  * }
     * @return
     */
    @Consume(CustomerPoolGetRequest.class)
    @RequestMapping("/customer/manage")
    public Response upCustomers(String ids, Long userId, Integer status, String allotName) {
        return customerPoolService.upCustomers((CustomerPoolGetRequest) ConsumeHelper.getObj());
    }

    /**
     * 下载 demo文件
     * @param response
     */
    @RequestMapping("/down/excel")
    public void downDemo(HttpServletResponse response){
        String path=Thread.currentThread().getContextClassLoader().getResource("demo.xlsx").getPath();
        DownFilesUtils.download(path,response);
    }
}
