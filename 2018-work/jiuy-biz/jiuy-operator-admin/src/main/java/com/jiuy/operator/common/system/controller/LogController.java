package com.jiuy.operator.common.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.annotion.BussinessLog;
import com.admin.common.annotion.Permission;
import com.admin.common.constant.SysConst;
import com.admin.common.constant.factory.PageFactory;
import com.admin.common.constant.state.BizLogType;
import com.admin.core.base.controller.BaseController;
import com.admin.core.support.BeanKit;
import com.baomidou.mybatisplus.mapper.SqlRunner;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.operator.common.system.dao.LogDao;
import com.jiuy.operator.common.system.persistence.dao.OperationLogMapper;
import com.jiuy.operator.common.system.warpper.LogWarpper;
import com.jiuyuan.util.anno.Login;

/**
 * 日志管理的控制器
 *
 * @author fengshuonan
 *
 * @Date 2017年4月5日 19:45:36
 */
@Controller
@RequestMapping("/log")
@Login
public class LogController extends BaseController {

    private static String PREFIX = "/system/log/";

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private LogDao logDao;

    /**
     * 跳转到日志管理的首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "log.html";
    }

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object list(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, @RequestParam(required = false) String logName, @RequestParam(required = false) Integer logType) {
        Page<Object> page = new PageFactory<>().defaultPage();
//        List<Map<String, Object>> result = logDao.getOperationLogs(page, beginTime, endTime, logName, BizLogType.valueOf(logType), page.getOrderByField(), page.isAsc());
        List<Object> result = new ArrayList<>();
        page.setRecords(result);
        return super.packForBT(page);
    }

    /**
     * 查询操作日志详情
     */
    @RequestMapping("/detail/{id}")
    @Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable Integer id) {
//        OperationLog operationLog = operationLogMapper.selectById(id);
//        Map<String, Object> stringObjectMap = BeanKit.beanToMap(operationLog);
        Map<String, Object> stringObjectMap = new HashMap<>();
        return super.warpObject(new LogWarpper(stringObjectMap));
    }

    /**
     * 清空日志
     */
    @BussinessLog(value = "清空业务日志")
    @RequestMapping("/delLog")
    @Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object delLog() {
        SqlRunner.db().delete("delete from operation_log");
        return super.SUCCESS_TIP;
    }
}
