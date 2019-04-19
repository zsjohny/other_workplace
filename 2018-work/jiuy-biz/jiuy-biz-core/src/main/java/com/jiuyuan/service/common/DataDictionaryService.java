package com.jiuyuan.service.common;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.common.DataDictionaryMapper;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.util.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @version V1.0
 * @Package com.jiuyuan.service.common
 * @Description:
 * @author: Aison
 * @date: 2018/4/24 18:41
 * @Copyright: 玖远网络
 */
@Service
public class DataDictionaryService {
    public static final String MEMBER_PACKAGE_TYPE_GROUP_KEY = "memberPackageType";

    @Autowired
    DataDictionaryMapper dataDictionaryMapper ;

    /**
     * 通过code查询字典
     * @param code
     * @date:   2018/4/24 18:43
     * @author: Aison
     */
    public DataDictionary getBycode(String code) {

        DataDictionary dictionary = new DataDictionary();
        dictionary.setCode(code);
        return dataDictionaryMapper.selectOne(dictionary);
    }

    /**
     * 查找某一组字典
     * @param group_code
     * @date:   2018/4/24 18:45
     * @author: Aison
     */
    public List<DataDictionary> getDictionaryGroup(String group_code) {

       return  dataDictionaryMapper.selectList(Condition.create().eq("group_code",group_code).eq("status",1));
    }

    /**
     * 查找多组字典
     * @param group_codes
     * @date:   2018/4/24 18:45
     * @author: Aison
     */
    public List<DataDictionary> getDictionaryGroups(String[] group_codes) {
        if(group_codes == null || group_codes.length==0){
            throw BizException.defulat().msg("groupCodes 不能为空");
        }
        return  dataDictionaryMapper.selectList(Condition.create().in("group_code",group_codes).eq("status",1));
    }


    /**
     * 根据groupCode, code 查询一条记录
     * @param groupCode 字典表 group_code字段
     * @param code 字典表 code字段
     * @return com.jiuyuan.entity.common.DataDictionary
     * @auther Charlie(唐静)
     * @date 2018/5/31 11:15
     */
    public DataDictionary getByGroupAndCode(String groupCode, String code) {
        if (groupCode == null || code == null) {
            throw BizException.defulat().msg("请求参数不能为空");
        }

        DataDictionary filter = new DataDictionary();
        filter.setGroupCode(groupCode);
        filter.setCode(code);
        try {
            return dataDictionaryMapper.selectOne(filter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询字典表找到两条记录 {groupCode:"+groupCode+",code:"+code+"}");
        }
    }

    public List<DataDictionary> getByGroupAndLikeComment(String groupCode, String comment) {
        Wrapper<DataDictionary> wrapper = new EntityWrapper<> ();
        wrapper.eq ("group_code", groupCode);
        wrapper.like ("comment", comment);
        return dataDictionaryMapper.selectList (wrapper);
    }
}
