package com.jiuyuan.entity.express;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.BizUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @version V1.0
 * @Package com.jiuyuan.entity.express
 * @Description:
 * @author: Aison
 * @date: 2018/5/7 11:01
 * @Copyright: 玖远网络
 */
public class ExpressUtilVo {


    public static ExpressUtilVo getInstance(String data) {
        ExpressUtilVo expressUtilVo = new ExpressUtilVo();
        expressUtilVo.setData(data);
        return expressUtilVo;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    private String data;
    private String provinceName;

    private Long addressId;

    private Map<String,ExpressItem> expressItemMap = new HashMap<>();

    /**
     * 获取分组
     * @param
     * @date:   2018/5/7 11:23
     * @author: Aison
     */
    public Map<String, ExpressItem> getExpressItemMap() {
        JSONObject jsonObject = JSONObject.parseObject(this.data);
        Set<String> keySet = jsonObject.keySet();
        for (String key : keySet) {
            String json = jsonObject.getString(key);
            expressItemMap.put(key,BizUtil.json2bean(json,ExpressItem.class));
        }
        return expressItemMap;
    }



    public void setExpressItemMap(Map<String, ExpressItem> expressItemMap) {
        this.expressItemMap = expressItemMap;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public class ExpressItem {

        private String provinceName;
        private String skuinfos;




        public String getProvinceName() {
            return provinceName;
        }
        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }
        public String getSkuinfos() {
            return skuinfos;
        }
        public void setSkuinfos(String skuinfos) {
            this.skuinfos = skuinfos;
        }

    }


    public static void main(String[] args) {

        String 	infos = "{\n" +
                "  key1:{\n" +
                "\t provinceName:\"浙江省\",\n" +
                "\t skuinfos:\"\",\n" +
                "\t},\n" +
                "  order2:{\n" +
                "\t provinceName:\"背景\",\n" +
                "\t skuinfos:\"\",\n" +
                "\t}\n" +
                "}";
        ExpressUtilVo expressUtilVo = new ExpressUtilVo();
        expressUtilVo.setData(infos);
        expressUtilVo.getExpressItemMap();
        System.out.println(expressUtilVo);
    }

}
