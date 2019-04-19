package com.jiuy.rb.service.impl.common;

import com.baomidou.mybatisplus.mapper.Condition;

import com.jiuy.base.exception.BizException;
import com.jiuy.base.util.Biz;
import com.jiuy.rb.mapper.common.DataDictionaryRbMapper;
import com.jiuy.rb.model.common.DataDictionaryRb;
import com.jiuy.rb.model.common.DataDictionaryRbQuery;
import com.jiuy.rb.service.common.IDataDictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @version V1.0
 * @author Aison
 * @date 2018/4/24 18:41
 * @Copyright 玖远网络
 */
@Service("dataDictionaryServiceRb")
public class DataDictionaryServiceImpl implements IDataDictionaryService {

    @Resource(name = "dataDictionaryRbMapper")
    private DataDictionaryRbMapper dataDictionaryRbMapper ;

    /**
     * 通过code查询字典
     * @param code code
     * @date   2018/4/24 18:43
     * @author Aison
     */
    @Override
    public DataDictionaryRb getByCode(String code,String groupCode) {

        DataDictionaryRbQuery dictionary = new DataDictionaryRbQuery();
        dictionary.setCode(code);
        dictionary.setGroupCode(groupCode);
        return dataDictionaryRbMapper.selectOne(dictionary);
    }

    /**
     * 查找某一组字典
     * @param group_code  group_code
     * @date   2018/4/24 18:45
     * @author Aison
     */
    @Override
    public List<DataDictionaryRb> getDictionaryGroup(String group_code) {

        DataDictionaryRbQuery dataDictionaryRb = new DataDictionaryRbQuery();
        dataDictionaryRb.setGroupCode(group_code);
        dataDictionaryRb.setStatus(1);
        return  dataDictionaryRbMapper.selectList(dataDictionaryRb);
    }

    /**
     * 查找多组字典
     * @param group_codes group_codes
     * @date   2018/4/24 18:45
     * @author Aison
     */
    public List<DataDictionaryRb> getDictionaryGroups(String[] group_codes) {
        if(group_codes == null || group_codes.length==0){
            throw BizException.def().msg("groupCodes 不能为空");
        }
        DataDictionaryRbQuery dataDictionaryRb = new DataDictionaryRbQuery();
        dataDictionaryRb.setGroupCodeIn(Biz.list2SQLString(new ArrayList<>(Arrays.asList(group_codes))));
        dataDictionaryRb.setStatus(1);
        return  dataDictionaryRbMapper.selectList(dataDictionaryRb);
    }


    /**
     * 根据groupCode, code 查询一条记录
     * @param groupCode 字典表 group_code字段
     * @param code 字典表 code字段
     * @return com.jiuyuan.entity.common.DataDictionary
     * @author Charlie(唐静)
     * @date 2018/5/31 11:15
     */
    public DataDictionaryRb getByGroupAndCode(String groupCode, String code) {
        if (groupCode == null || code == null) {
            throw BizException.def().msg("请求参数不能为空");
        }

        DataDictionaryRbQuery filter = new DataDictionaryRbQuery();
        filter.setGroupCode(groupCode);
        filter.setCode(code);
        try {
            return dataDictionaryRbMapper.selectOne(filter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询字典表找到两条记录 {groupCode:"+groupCode+",code:"+code+"}");
        }
    }
}
