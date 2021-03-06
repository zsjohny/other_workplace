package com.jiuy.operator.common.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.annotion.BussinessLog;
import com.admin.common.annotion.Permission;
import com.admin.common.constant.DictConst;
import com.admin.common.constant.SysConst;
import com.admin.core.base.controller.BaseController;
import com.admin.core.exception.BizExceptionEnum;
import com.admin.core.exception.BussinessException;
import com.admin.core.log.LogObjectHolder;
import com.admin.core.util.ToolUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jiuy.operator.common.constant.factory.OperatorConstantFactory;
import com.jiuy.operator.common.system.dao.DictDao;
import com.jiuy.operator.common.system.persistence.dao.DictMapper;
import com.jiuy.operator.common.system.persistence.model.Dict;
import com.jiuy.operator.common.system.service.IDictService;
import com.jiuy.operator.common.system.warpper.DictWarpper;
import com.jiuyuan.util.anno.Login;

/**
 * 字典控制器
 *
 * @author fengshuonan
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
@Login
public class DictController extends BaseController {

    private String PREFIX = "/system/dict/";

    @Resource
    DictDao dictDao;

    @Resource
    DictMapper dictMapper;

    @Resource
    IDictService dictService;

    /**
     * 跳转到字典管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dict.html";
    }

    /**
     * 跳转到添加字典
     */
    @RequestMapping("/dict_add")
    public String deptAdd() {
        return PREFIX + "dict_add.html";
    }

    /**
     * 跳转到修改字典
     */
	@Permission(SysConst.ADMIN_NAME)
    @RequestMapping("/dict_edit/{dictId}")
    public String deptUpdate(@PathVariable Integer dictId, Model model) {
		Dict dict = dictMapper.selectById(dictId);
        model.addAttribute("dict", dict);
		List<Dict> subDicts = dictMapper.selectList(new EntityWrapper<Dict>().eq("pid", dictId));
        model.addAttribute("subDicts", subDicts);
        LogObjectHolder.me().set(dict);
        return PREFIX + "dict_edit.html";
    }

    /**
     * 新增字典
     *
     * @param dictValues 格式例如   "1:启用;2:禁用;3:冻结"
     */
	@BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictConst.DictMap)
    @RequestMapping(value = "/add")
	@Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object add(String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictName, dictValues)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        this.dictService.addDict(dictName, dictValues);
        return SUCCESS_TIP;
    }

    /**
     * 获取所有字典列表
     */
    @RequestMapping(value = "/list")
	@Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.dictDao.list(condition);
        return super.warpObject(new DictWarpper(list));
    }

    /**
     * 字典详情
     */
    @RequestMapping(value = "/detail/{dictId}")
	@Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object detail(@PathVariable("dictId") Integer dictId) {
        return dictMapper.selectById(dictId);
    }

    /**
     * 修改字典
     */
	@BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictConst.DictMap)
    @RequestMapping(value = "/update")
	@Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object update(Integer dictId, String dictName, String dictValues) {
        if (ToolUtil.isOneEmpty(dictId, dictName, dictValues)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_NULL);
        }
        dictService.editDict(dictId, dictName, dictValues);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除字典记录
     */
	@BussinessLog(value = "删除字典记录", key = "dictId", dict = DictConst.DeleteDict)
    @RequestMapping(value = "/delete")
	@Permission(SysConst.ADMIN_NAME)
    @ResponseBody
    public Object delete(@RequestParam Integer dictId) {

        //缓存被删除的名称
        LogObjectHolder.me().set(OperatorConstantFactory.me().getDictName(dictId));

        this.dictService.delteDict(dictId);
        return SUCCESS_TIP;
    }

}
