package com.e_commerce.miscroservice.operate.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;
import com.e_commerce.miscroservice.commons.enums.EmptyEnum;
import com.e_commerce.miscroservice.commons.enums.system.DataDictionaryEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.operate.dao.DataDictionaryDao;
import com.e_commerce.miscroservice.operate.mapper.SqlMapper;
import com.e_commerce.miscroservice.operate.service.system.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/10/17 16:07
 * @Copyright 玖远网络
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    private Log logger = Log.getInstance(DataDictionaryServiceImpl.class);


    /**
     * 根据groupCode,code 更新字典表的值
     *
     * @return java.lang.String
     * @author Charlie
     * @date 2018/10/17 13:44
     */
    @Override
    public void updDataDictionaryByCodeAndGroupCode(DataDictionary dictionary) {
        if (BeanKit.hasNull (dictionary.getGroupCode (), dictionary.getCode ())) {
            throw ErrorHelper.me ("请求参数不能为空");
        }
        dictionary.setId (null);
        dictionary.setStatus (null);
        MybatisOperaterUtil.getInstance ().update (
                dictionary,
                new MybatisSqlWhereBuild (DataDictionary.class)
                        .eq (DataDictionary::getGroupCode, dictionary.getGroupCode ())
                        .eq (DataDictionary::getCode, dictionary.getCode ())
        );
    }

    /**
     * 查询字典表
     *
     * @param dictionary dictionary
     * @return com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary
     * @author Charlie
     * @date 2018/10/23 16:00
     */
    @Override
    public DataDictionary findDictionaryByCodeAndGroupCode(DataDictionary dictionary) {
        return MybatisOperaterUtil.getInstance ().findOne (
                new DataDictionary(),
                new MybatisSqlWhereBuild (DataDictionary.class)
                .eq (DataDictionary::getGroupCode, dictionary.getGroupCode ())
                .eq (DataDictionary::getCode, dictionary.getCode ())
        );
    }



    /**
     * 分销角色升级条件
     *
     * @return java.util.Map<java.lang.String   ,   java.lang.String>
     * @author Charlie
     * @date 2018/11/6 10:33
     */
    @Override
    public Map<String, DataDictionary> dstbRoleUpGradeFind() {
        String groupCode = DataDictionaryEnums.UPGRADE_CONDITION_DISTRIBUTOR.getGroupCode ();
        //查询
        List<DataDictionary> dataDictionaryList = dataDictionaryDao.findByGroup (groupCode);
        if (ObjectUtils.isEmpty (dataDictionaryList)) {
            return EmptyEnum.map ();
        }
        //拼接
        Map<String, DataDictionary> result = new HashMap<> (3);
        dataDictionaryList.stream ().forEach (action-> {
            result.put (action.getCode (), action);
        });
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void test(String gCode, String code) {
        logger.info("测试插入缓存, 新插入的值在同一个事务中修改是否生效");
        DataDictionary entity = new DataDictionary();
        entity.setComment(System.currentTimeMillis()+"");
        entity.setGroupCode(gCode);
        entity.setCode(code);
        int save = MybatisOperaterUtil.getInstance().save(entity);
        logger.info("save = {}" ,save);

        Long id = entity.getId();
        logger.info("id={}, comment={}", id, entity.getComment());

        DataDictionary history = MybatisOperaterUtil.getInstance().findOne(entity,
                new MybatisSqlWhereBuild(DataDictionary.class)
                        .eq(DataDictionary::getId, id)
        );

        logger.info("历史 history={}", history);

        entity.setComment("update after");
        int update = MybatisOperaterUtil.getInstance().update(
                entity,
                new MybatisSqlWhereBuild(DataDictionary.class)
                        .eq(DataDictionary::getId, id)
        );
        logger.info(" update ={}", update);

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testUpd(String gCode, String code) {
        logger.info("测试读已提交");
        DataDictionary history = MybatisOperaterUtil.getInstance().findOne(new DataDictionary(),
                new MybatisSqlWhereBuild(DataDictionary.class)
                        .eq(DataDictionary::getCode, code)
                        .eq(DataDictionary::getGroupCode, gCode)
        );

        logger.info("历史 history VAL={}", history.getVal());

        history.setVal(""+new SimpleDateFormat("HH:ss").format(new Date()));
        int update = MybatisOperaterUtil.getInstance().update(
                history,
                new MybatisSqlWhereBuild(DataDictionary.class)
                        .eq(DataDictionary::getId, history.getId())
        );
        logger.info("更新后 update ={}", update);

        DataDictionary afterUpd = MybatisOperaterUtil.getInstance().findOne(new DataDictionary(),
                new MybatisSqlWhereBuild(DataDictionary.class)
                        .eq(DataDictionary::getCode, code)
                        .eq(DataDictionary::getGroupCode, gCode)
        );
        logger.info("查找更新后 val={}", afterUpd.getVal());
    }
}
