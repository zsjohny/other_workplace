package com.jiuyuan.service.store;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.store.SmartModuleMapper;
import com.jiuyuan.entity.common.DataDictionary;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.store.SmartModule;
import com.jiuyuan.service.common.DataDictionaryService;
import com.jiuyuan.service.common.StoreBusinessNewService;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * app门店智能模块 服务实现类
 * </p>
 *
 * @author Charlie(唐静)
 * @since 2018-05-09
 */
@Service
public class SmartModuleService implements ISmartModuleService {

    static Log logger = LogFactory.getLog(SmartModuleService.class);

    public static final String SMART_ARTICLE = "smartArticle";

    //字典表对应的groupCode
    private final String smartDictGroup = "page_module";

    @Autowired
    private SmartModuleMapper smartModuleMapper;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 查询门店模块
     *
     * @param storeId 门店id
     * @return
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @Override
    public List<SmartModule> getByStoreId(Long storeId) {
        Wrapper<SmartModule> wrapper = new EntityWrapper<>();
        wrapper.eq("store_id", storeId).orderBy("sort", true);
        return smartModuleMapper.selectList(wrapper);
    }


    /**
     * 更新模块
     *
     * @Param smartModule 需要更新的模块
     * @Param currentStoreId 当前用户storeId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upd(SmartModule smartModule, Long currentStoreId) {

        // 校验权限
        if (!verify(smartModule, currentStoreId)) {
            logger.warn("用户更新模块, 不具有修改的权限");
            return false;
        }

        // 判断是否继续业务
        smartModule.setUpdTime(System.currentTimeMillis());
        smartModuleMapper.updSmartModule(smartModule);

        return true;
    }

    /**
     * 向历史版本兼容, 维护用户表wxa_article_show 这个字段
     * 上线补加, 重写一下, 有重复查表
     *
     * @param smartId
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/1 15:11
     */
    @Deprecated
    private void compatibleHistoryV372(Long smartId, Long storeId) {

        //只对智能模块的文章做兼容
        SmartModule smartModule = smartModuleMapper.selectById(smartId);
        if (SMART_ARTICLE.equals(smartModule.getCode())) {
            storeBusinessNewService.updateWxaArticleShowById(storeId, Integer.parseInt(smartModule.getSwitcher()));
        }
    }

    @Autowired
    private StoreBusinessNewService storeBusinessNewService;

    /**
     * 新增当前用户的模板
     *
     * @param currentStoreId
     * @param openFlag       是否打开开关
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @Override
    public void add(Long currentStoreId, String openFlag) {
        //字典表关于该模块信息
        List<DataDictionary> dictionaryList = dataDictionaryService.getDictionaryGroup(smartDictGroup);
        if (!BizUtil.isNotEmpty(dictionaryList) && dictionaryList.size() > 0) {
            logger.warn("字典表查询智能模块, 未找到记录 smartDictGroup:" + smartDictGroup);
            return;
        }
        //用户没有的模块
        Collection<DataDictionary> clearModules = modulesFilter(dictionaryList, currentStoreId);

        //生成模块
        generateModule(currentStoreId, clearModules, openFlag);
    }

    /**
     * 新增当前用户的模板
     *
     * @param currentStoreId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long currentStoreId, StoreBusiness store) {
        //字典表关于该模块信息
        List<DataDictionary> dictionaryList = dataDictionaryService.getDictionaryGroup(smartDictGroup);

        if (!BizUtil.isNotEmpty(dictionaryList) && dictionaryList.size() > 0) {
            logger.warn("字典表查询智能模块, 未找到记录 smartDictGroup:" + smartDictGroup);
            return;
        }

        //用户没有的模块
        Collection<DataDictionary> clearModules = modulesFilter(dictionaryList, currentStoreId);

        //生成模块
        generateModule(currentStoreId, clearModules, String.valueOf(store.getWxaArticleShow()));
    }


    /**
     * 更新只能模块文章信息, 向372版本之前做历史兼容
     *
     * @param storeBusiness
     * @return void
     * @auther Charlie(唐静)
     * @date 2018/6/1 16:40
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeOrOpenArticleV372(StoreBusiness storeBusiness) {
        //更新用户字段
        storeBusinessNewService.closeOrOpenArticle(storeBusiness);

        //向新版本兼容, 更新智能模块信息
        Wrapper<SmartModule> filter = new EntityWrapper<>();
        filter.eq("store_id", storeBusiness.getId());
        List<SmartModule> smartModules = smartModuleMapper.selectList(filter);
        if (null != smartModules && smartModules.size() > 0) {
            for (SmartModule module : smartModules) {
                if (SmartModuleService.SMART_ARTICLE.equals(module.getCode())) {
                    module.setSwitcher(String.valueOf(storeBusiness.getWxaArticleShow()));
                    smartModuleMapper.updSmartModule(module);
                }
            }
        }

    }


    /**
     * 用户没有的模块code
     *
     * @param source
     * @param currentStoreId
     * @return 用户需要添加的模块的字典表信息
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    private Collection<DataDictionary> modulesFilter(List<DataDictionary> source, Long currentStoreId) {
        Map<String, DataDictionary> map = new HashMap<>(source.size());
        for (DataDictionary dictionary : source) {
            map.put(dictionary.getCode(), dictionary);
        }
        Set<String> codeSet = map.keySet();

        //用户已有模块
        Wrapper<SmartModule> wrapper = new EntityWrapper<>();
        wrapper.eq("store_id", currentStoreId).in("code", codeSet);
        List<SmartModule> smartModules = smartModuleMapper.selectList(wrapper);

        //删除已有模块并返回
        if (!BizUtil.isNotEmpty(smartModules) && smartModules.size() > 0) {
            return source;
        }
        for (SmartModule smartModule : smartModules) {
            if (codeSet.contains(smartModule.getCode())) {
                map.remove(smartModule.getCode());
            }
        }

        return map.values();
    }


    /**
     * 为用户生成模块
     *
     * @param currentStoreId 当前用户的id
     * @param source         需要添加模块的字典表信息
     * @Author Charlie(唐静)
     * @Date 18/05/10
     */
    void generateModule(Long currentStoreId, Collection<DataDictionary> source, String openFlag) {
        if (BizUtil.isNotEmpty(source) && source.size() > 0) {
            long now = System.currentTimeMillis();

            for (DataDictionary dict : source) {
                SmartModule entity = new SmartModule();
                entity.setUpdTime(now);
                entity.setCreateTime(now);
                entity.setStoreId(currentStoreId);
                entity.setSort(Integer.valueOf(dict.getComment()));
                entity.setName(dict.getName());
                entity.setCode(dict.getCode());

                //版本兼容
                if (SMART_ARTICLE.equals(dict.getCode())) {
                    //1打开, 2关闭
                    entity.setSwitcher(openFlag);
                } else {
                    entity.setSwitcher(dict.getVal());
                }
                smartModuleMapper.insert(entity);
            }
        }
    }


    /**
     * 批量更新模块
     *
     * @param list 需要更新的模块
     * @Param currentStoreId 当前用户storeId
     * @Author Charlie(唐静)
     * @Date 18/05/09
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpd(List<SmartModule> list, Long currentStoreId) {
        //校验
        if (!verify(list, currentStoreId)) {
            return false;
        }

        //更新操作
        if (list != null && list.size() > 0) {
            Long now = System.currentTimeMillis();
            for (SmartModule vo : list) {
                vo.setUpdTime(now);
                smartModuleMapper.updSmartModule(vo);

                //向历史版本兼容
                compatibleHistoryV372(vo.getId(), currentStoreId);
            }
        }
        return true;
    }


    /**
     * 校验用户具有修改权限
     *
     * @param smartModule
     * @return
     * @Authro Charlie(唐静)
     * @Date 18/05/09
     */
    private boolean verify(SmartModule smartModule, Long currentStoreId) {
        return verify(Arrays.asList(new SmartModule[]{smartModule}), currentStoreId);
    }

    /**
     * 校验用户具有修改权限
     *
     * @param list
     * @param currentStoreId 当前用户的门店id
     * @return
     * @Authro Charlie(唐静)
     * @Date 18/05/09
     */
    private boolean verify(List<SmartModule> list, Long currentStoreId) {
        if (BizUtil.isNotEmpty(list) && list.size() > 0) {
            //门店ids
            ArrayList<Long> ids = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                SmartModule smartModule = list.get(i);

                //模块id不能为空
                Long requestId = smartModule.getId();
                if (requestId == null) {
                    return false;
                }
                ids.add(requestId);

            }

            Wrapper<SmartModule> wrapper = new EntityWrapper<>();
            wrapper.eq("store_id", currentStoreId);
            wrapper.in("id", ids);
            List<SmartModule> smartModules = smartModuleMapper.selectList(wrapper);
            //未查记录 或查到记录数不对
            if (null == smartModules || smartModules.size() != list.size()) {
                StringBuilder builder = new StringBuilder();
                for (SmartModule module : list) {
                    builder.append(module.getId()).append(",");
                }
                String sIds = builder.substring(0, builder.length() - 1);
                logger.warn("用户修改智能模块, 不具有修改权限 {StoreId:" + currentStoreId + ",SmartModuleList:" + sIds + "}");
                return false;
            }
            return true;
        }
        logger.info("用户修改智能模块, 不具有修改权限 待修改只能模块的长度为0");
        return false;
    }

}
