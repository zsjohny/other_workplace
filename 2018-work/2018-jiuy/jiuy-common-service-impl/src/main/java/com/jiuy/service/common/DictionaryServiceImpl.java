package com.jiuy.service.common;

import com.jiuy.mapper.common.DataDictionaryMapper;
import com.jiuy.model.common.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 字典service
 * @version V1.0
 * @author Aison
 * @date 2018/4/24 17:33
 * @Copyright 玖远网络
 */
@Service("dictionaryService")
public class DictionaryServiceImpl implements IDictionaryService {

    @Autowired
    private DataDictionaryMapper dictionaryMapper;

    @Override
    public DataDictionary getByCode(String code,String groupp) {
        DataDictionary dy =  new DataDictionary();
        dy.setCode(code);
        dy.setGroupCode(groupp);
        return dictionaryMapper.selectOne(dy);
    }
}
