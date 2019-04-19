package com.jiuy.core.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.DictionaryDao;
import com.jiuyuan.entity.Dictionary;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    
    @Resource
    private DictionaryDao dictionaryDao;

    @Override
    public List<Dictionary> loadDictionaryProperty(String groupid, String dictid) {
        List<Dictionary> dictGroup = dictionaryDao.loadDictionaryByGroup(groupid,dictid);
        return dictGroup;
    }

}
