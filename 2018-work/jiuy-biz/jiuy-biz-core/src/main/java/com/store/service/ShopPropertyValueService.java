package com.store.service;

import java.util.*;

import com.jiuyuan.constant.PropertyNameEnum;
import com.jiuyuan.dao.mapper.shop.PropertyValueGroupMapper;
import com.jiuyuan.dao.mapper.shop.PropertyValueNewMapper;
import com.jiuyuan.entity.newentity.PropertyValueGroup;
import com.jiuyuan.entity.newentity.PropertyValueNew;
import com.jiuyuan.util.BizException;
import com.store.entity.PropNameVo;
import com.store.entity.PropValGroupVo;
import org.omg.IOP.ENCODING_CDR_ENCAPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.dao.mapper.ShopPropertyValueMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopPropertyValueLogMapper;
import com.store.entity.ShopPropertyValue;
import com.store.entity.ShopPropertyValueLog;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.util.ObjectUtils;

/**
 * @author QiuYuefan
 * @version 创建时间: 2017年6月19日 14:24:27
 */

@Service
public class ShopPropertyValueService{
    private static final Log logger = LogFactory.get();

    private static final int NORMAL = 0;
    @Autowired
    private ShopPropertyValueMapper shopPropertyValueMapper;

    @Autowired
    private ShopPropertyValueLogMapper shopPropertyValueLogMapper;

    /**
     * 判断是添加还是修改
     *
     * @param propertyNameId
     * @param propertyValueId
     * @param propertyValue
     * @return
     */
    public JsonResponse addUpdatePropertyValue(Long propertyNameId, Long propertyValueId, String propertyValue, @ClientIp String ip, ClientPlatform client, Long storeId) {
        try {
            JsonResponse jsonResponse = new JsonResponse();
            if (propertyNameId <= 0) {
                return jsonResponse.setError("该属性类别不存在");
            }
            else if (propertyValue == null || "".equals(propertyValue)) {
                return jsonResponse.setError("属性值不能为空");
            }
            else if (propertyValue.length() > 10) {
                return jsonResponse.setError("属性值长度不能超过10个汉字");
            }
            else if (propertyValueId == 0) {
                return addPropertyValue(propertyNameId, propertyValue, ip, client);
            }
            else if (propertyValueId < 0) {
                return jsonResponse.setError("属性值Id不能小于0");
            }
            else {
                return updatePropertyValue(propertyNameId, propertyValueId, propertyValue, ip, client, storeId);
            }
        } catch (Exception e) {
            return new JsonResponse().setError("添加或修改错误");

        }
    }

    /**
     * 修改属性值
     *
     * @param propertyNameId
     * @param propertyValueId
     * @param propertyValue
     * @return
     */
    public JsonResponse updatePropertyValue(Long propertyNameId, Long propertyValueId, String propertyValue, @ClientIp String ip, ClientPlatform client, Long storeId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopPropertyValue shopPropertyValue = new ShopPropertyValue();
            shopPropertyValue.setStoreId(storeId);
            shopPropertyValue.setId(propertyValueId);
            shopPropertyValue.setPropertyNameId(propertyNameId);
            shopPropertyValue.setPropertyValue(propertyValue);
            shopPropertyValue.setUpdateTime(System.currentTimeMillis());
            //updateById(shopPropertyValue);
            //Wrapper<ShopPropertyValue> wrapper = new EntityWrapper<ShopPropertyValue>().eq("id", propertyValueId);
            //int record = shopPropertyValueMapper.update(shopPropertyValue, wrapper);
            int record = shopPropertyValueMapper.updateById(shopPropertyValue);
            if (record != 1) {
                return jsonResponse.setError("修改错误");
            }

            ShopPropertyValueLog shopPropertyValueLog = new ShopPropertyValueLog();
            shopPropertyValueLog.setStoreId(storeId);
            shopPropertyValueLog.setPropertyValueId(propertyValueId);
            shopPropertyValueLog.setAdminId(1L);
            shopPropertyValueLog.setType(3);
            shopPropertyValueLog.setIp(ip);

            if (client != null) {
                String value = client.getPlatform().getValue();
                if ("pc".equals(value)) {
                    shopPropertyValueLog.setPlatform(0);
                }
                else if ("android".equals(value)) {
                    shopPropertyValueLog.setPlatform(1);
                }
                else if ("iphone".equals(value)) {
                    shopPropertyValueLog.setPlatform(2);
                }
            }

            shopPropertyValueLog.setVersion(client.getVersion());
            //shopPropertyValueLog.setNet(2);
            shopPropertyValueLog.setContent(propertyValue);
            shopPropertyValueLog.setCreateTime(System.currentTimeMillis());
            int logRecord = shopPropertyValueLogMapper.insert(shopPropertyValueLog);
            if (logRecord == 1) {
                return jsonResponse.setSuccessful();
            }
            else {
                return jsonResponse.setError("修改记录错误");
            }
        } catch (Exception e) {
            return jsonResponse.setError("修改错误");
        }

    }

    /**
     * 添加属性值
     *
     * @param propertyNameId
     * @param propertyValue
     * @return
     */
    private JsonResponse addPropertyValue(Long propertyNameId, String propertyValue, @ClientIp String ip, ClientPlatform client) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            EntityWrapper<ShopPropertyValue> wrapper = new EntityWrapper<ShopPropertyValue>();
            wrapper.setSqlSelect("select count(*) from shop_property_value");
            wrapper.where("property_name_id={0}", propertyNameId);
            Integer shopPropertyValueCount = shopPropertyValueMapper.selectCount(wrapper);
            if (shopPropertyValueCount >= 15) {
                return jsonResponse.setError("属性值不得大于15条");
            }

            ShopPropertyValue shopPropertyValue = new ShopPropertyValue();
            shopPropertyValue.setPropertyNameId(propertyNameId);
            shopPropertyValue.setPropertyValue(propertyValue);
            shopPropertyValue.setCreateTime(System.currentTimeMillis());
            shopPropertyValue.setUpdateTime(shopPropertyValue.getCreateTime());
            int record = shopPropertyValueMapper.insert(shopPropertyValue);
            if (record != 1) {
                return jsonResponse.setError("添加错误");
            }

            ShopPropertyValue shopPropertyValueRecord = new ShopPropertyValue();
            shopPropertyValueRecord.setPropertyValue(propertyValue);
            shopPropertyValue = shopPropertyValueMapper.selectOne(shopPropertyValueRecord);

            ShopPropertyValueLog shopPropertyValueLog = new ShopPropertyValueLog();
            shopPropertyValueLog.setPropertyValueId(shopPropertyValue.getId());
            shopPropertyValueLog.setAdminId(1L);
            shopPropertyValueLog.setType(1);
            shopPropertyValueLog.setIp(ip);

            if (client != null) {
                String value = client.getPlatform().getValue();
                if ("pc".equals(value)) {
                    shopPropertyValueLog.setPlatform(0);
                }
                else if ("android".equals(value)) {
                    shopPropertyValueLog.setPlatform(1);
                }
                else if ("iphone".equals(value)) {
                    shopPropertyValueLog.setPlatform(2);
                }
            }

            shopPropertyValueLog.setVersion(client.getVersion());
            //shopPropertyValueLog.setNet(2);
            shopPropertyValueLog.setContent(propertyValue);
            shopPropertyValueLog.setCreateTime(System.currentTimeMillis());
            int logRecord = shopPropertyValueLogMapper.insert(shopPropertyValueLog);
            if (logRecord == 1) {
                return jsonResponse.setSuccessful();
            }
            else {
                return jsonResponse.setError("添加记录错误");
            }
        } catch (Exception e) {
            return jsonResponse.setError("添加错误");
        }

    }


    /**
     * 获取参数值列表
     */
    public JsonResponse getPropertyValueList(Long propertyNameId, Long storeId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            Map<String, Object> columnMap = new HashMap<String, Object>();
            columnMap.put("property_name_id", propertyNameId);
            columnMap.put("store_id", storeId);
            List<ShopPropertyValue> shopPropertyValueList = shopPropertyValueMapper.selectByMap(columnMap);
            Map<String, Object> data = new HashMap<String, Object>();
            List<Map<String, String>> propertyValueList = new ArrayList<Map<String, String>>();
            for (ShopPropertyValue shopPropertyValue : shopPropertyValueList) {
                Map<String, String> propertyValue = new HashMap<String, String>();
                propertyValue.put("propertyValueId", shopPropertyValue.getId() + "");
                propertyValue.put("orderIndex", shopPropertyValue.getOrderIndex() + "");
                propertyValue.put("propertyValue", shopPropertyValue.getPropertyValue());
                propertyValueList.add(propertyValue);
            }
            data.put("propertyValueList", propertyValueList);
            return jsonResponse.setSuccessful().setData(data);
        } catch (Exception e) {
            return jsonResponse.setError("获取参数值列表错误");
        }

    }

    /**
     * 删除属性值
     */
    public JsonResponse deletePropertyValue(Long propertyValueId, @ClientIp String ip, ClientPlatform client, Long storeId) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            ShopPropertyValue shopPropertyValueRecord = new ShopPropertyValue();
            shopPropertyValueRecord.setStoreId(storeId);
            shopPropertyValueRecord.setId(propertyValueId);
            shopPropertyValueRecord.setStatus(- 1);
            Integer record = shopPropertyValueMapper.updateById(shopPropertyValueRecord);
            if (record != 1) {
                return jsonResponse.setError("删除错误");
            }

            ShopPropertyValueLog shopPropertyValueLog = new ShopPropertyValueLog();
            shopPropertyValueLog.setStoreId(storeId);
            shopPropertyValueLog.setPropertyValueId(propertyValueId);
            shopPropertyValueLog.setAdminId(1L);
            shopPropertyValueLog.setType(2);
            shopPropertyValueLog.setIp(ip);

            if (client != null) {
                String value = client.getPlatform().getValue();
                if ("pc".equals(value)) {
                    shopPropertyValueLog.setPlatform(0);
                }
                else if ("android".equals(value)) {
                    shopPropertyValueLog.setPlatform(1);
                }
                else if ("iphone".equals(value)) {
                    shopPropertyValueLog.setPlatform(2);
                }
            }

            shopPropertyValueLog.setVersion(client.getVersion());
            //shopPropertyValueLog.setNet(2);
            shopPropertyValueLog.setContent("删除");
            shopPropertyValueLog.setCreateTime(System.currentTimeMillis());
            int logRecord = shopPropertyValueLogMapper.insert(shopPropertyValueLog);
            if (logRecord == 1) {
                return jsonResponse.setSuccessful();
            }
            else {
                return jsonResponse.setError("删除记录错误");
            }
        } catch (Exception e) {
            return jsonResponse.setError("删除属性值错误");
        }

    }


    @Autowired
    private PropertyValueNewMapper propertyValueNewMapper;
    @Autowired
    private PropertyValueGroupMapper propertyValueGroupMapper;

    /**
     * 添加门店用户自己的商品属性值
     *
     * @param requestPropVal requestPropVal
     *                       主要包括:
     *                       propertyValueGroupId 属性值分组id
     *                       PropertyValue 属性值名称
     *                       OrderIndex 排序
     * @return com.jiuyuan.web.help.JsonResponse
     * @author Charlie(唐静)
     * @date 2018/7/8 22:53
     */
    public PropertyValueNew addShopPropValue(PropertyValueNew requestPropVal) {
        // 属性值分组存在
        if (ObjectUtils.isEmpty(requestPropVal.getPropertyValueGroupId()) || org.apache.commons.lang3.StringUtils.isBlank(requestPropVal.getPropertyValue())) {
            throw BizException.defulat().msg("请求参数不可为空");

        }

        PropertyValueGroup group = propertyValueGroupMapper.selectById(requestPropVal.getPropertyValueGroupId());
        if (ObjectUtils.isEmpty(group)) {
            throw BizException.defulat().msg("未找到值分组信息");
        }


        // 校验存在
        List<PropertyValueNew> history = propertyValueNewMapper.findStorePropValue(requestPropVal.getPropertyValue(), group.getId(), requestPropVal.getStoreId());
        if (! ObjectUtils.isEmpty(history)) {
            throw BizException.defulat().msg("属性值已存在,不可重复添加");
        }

        // insert
        PropertyValueNew entity = new PropertyValueNew();
        long curr = System.currentTimeMillis();
        entity.setPropertyValue(requestPropVal.getPropertyValue());
        entity.setPropertyValueGroupId(group.getId());
        entity.setSupplierId(-1L);
        entity.setCreateTime(curr);
        entity.setOrderIndex(requestPropVal.getOrderIndex());
        entity.setStatus(NORMAL);
        entity.setStoreId(requestPropVal.getStoreId());
        entity.setPropertyNameId(group.getPropertyNameId());
        entity.setUpdateTime(curr);
        Integer i = propertyValueNewMapper.insert(entity);
        if (i != 1) {
            throw BizException.defulat().msg("新增失败");
        }
        return entity;
    }




    /**
     * 查询门店用户的属性
     *
     * @param storeId storeId
     * @param codes   属性值关键字的逗号拼接, eg: 'color,size'
     * @return java.lang.Object
     * @author Charlie(唐静)
     * @date 2018/7/8 23:42
     */
    public Collection<PropNameVo> listPropValGroupVos(Long storeId, String codes) {

        // 切分codes
        if (org.apache.commons.lang3.StringUtils.isBlank(codes)) {
            throw BizException.defulat().msg("codes不可为空");
        }
        codes = codes.endsWith(",") ? codes : codes + ",";
        String[] codeArr = codes.split(",");
        if (codeArr.length == 0) {
            throw BizException.defulat().msg("codes不可为空");
        }

        boolean isLegal = PropertyNameEnum.containCodes(codeArr);
        if (! isLegal) {
            throw BizException.defulat().msg("未知的属性名编码");
        }
        //属性值id
        List<Long> propNameIds = PropertyNameEnum.findIdByCode(Arrays.asList(codeArr));
        //属性值分组

        Wrapper<PropertyValueGroup> groupWrapper = new EntityWrapper<>();
        groupWrapper.eq("status", NORMAL);
        groupWrapper.in("PropertyNameId", propNameIds);
        groupWrapper.orderBy("OrderIndex", false);
        List<PropertyValueGroup> groups = propertyValueGroupMapper.selectList(groupWrapper);
        if (ObjectUtils.isEmpty(groups)) {
            throw BizException.defulat().msg("未找到属性值分组信息");
        }

        //属性值
        List<PropValGroupVo> groupVos = new ArrayList<>(groups.size());
        groups.forEach(group -> {
            //查询门店用户拥有的每个分组下的属性值
            List<PropertyValueNew> values = propertyValueNewMapper.findStorePropValue(null, group.getId(), storeId);
            //构建
            PropValGroupVo vo = new PropValGroupVo();
            vo.setPropertyNameId(group.getPropertyNameId());
            vo.setSort(group.getOrderIndex());
            vo.setGroupName(group.getGroupName());
            vo.setGroupId(group.getId());
            vo.setPropertyValues(values);
            groupVos.add(vo);
        });

        return buildPropNameVo(groupVos).values();
    }



    /**
     * 构建返回值对象的组装
     *
     * @param groupVos 分组vo
     * @return java.util.Map key:code(color,size..) value:PropNameVo
     * @author Charlie(唐静)
     * @date 2018/7/10 9:13
     */
    private Map<String, PropNameVo> buildPropNameVo(List<PropValGroupVo> groupVos) {
        Map<String, PropNameVo> result = new HashMap<>(2);
        groupVos.forEach(group->{
            String code = PropertyNameEnum.findCodeById(group.getPropertyNameId());
            PropNameVo propNameVo = result.get(code);
            if (propNameVo == null) {
                List<PropValGroupVo> groupVoList = new ArrayList<>();
                groupVoList.add(group);

                propNameVo = new PropNameVo();
                propNameVo.setCodeName(code);
                propNameVo.setGroups(groupVoList);
                propNameVo.setPropNameId(group.getPropertyNameId());
                result.put(code, propNameVo);
            }
            else {
                List<PropValGroupVo> groupVoList = propNameVo.getGroups();
                groupVoList.add(group);
            }
        });
        return result;
    }


}


