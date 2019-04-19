/**
 * 
 */
package com.jiuyuan.constant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author LWS
 *
 */
@Deprecated
public final class Express  {

    /**
     * 支持的快递接口类型
     * @author LWS 
     *
     */
    public enum ExpressSupport  {
        /**
         * 顺丰快递
         */
        SF("shunfeng","顺丰快递"),
        
        /**
         * 天天快递
         */
        TT("tiantian","天天快递"),
        
        /**
         * 申通快递
         */
        ST("shentong","申通快递"),
        
        /**
         * 中通快递
         */
        ZT("zhongtong","中通快递"),
        
        /**
         * 圆通快递
         */
        YT("yuantong","圆通快递"),

        /**
         * 韵达快递
         */
        YD("yunda","韵达快递"),
    	
        /**
         * 汇通快递
         */
        HK("huitongkuaidi","汇通快递"),
    	
    	/**
    	 * EMS快递
    	 */
    	EMS("ems","邮政快递"),
    	
    	/**
    	 * 德邦快递
    	 */
    	DB("debangwuliu","德邦物流"),
    	
    	/**
    	 * 邮政包裹/平邮
    	 */
    	YZ("youzhengguonei","邮政包裹/平邮"),
    	
    	/**
    	 * 速尔快递
    	 */
    	SUER("suer","速尔快递"),
    	
    	/**
    	 * 宅急送
    	 */
    	ZJS("zhaijisong","宅急送");
        
    	
    	
        private String name;
        private String chnName;
        
        private ExpressSupport(String name,String chnName){
            this.name = name;
            this.chnName = chnName;
        }

        public String getValidName() {
            return name;
        }

        public String getChnName() {
            return chnName;
        }
        
        
        /**
         * 通过代号获取活动
         * @param shortName
         * @return
         */
        public static String getChnNameByName(String name ) {
            if (name == null || name.length()<1) {
                return "";
            }

            for (ExpressSupport expressSupport : ExpressSupport.values()) {
                if (expressSupport.getValidName().equals(name)) {
                    return expressSupport.getChnName();
                }
            }

            return "";
        }
        
        
        
        public static ExpressSupport parse(String name) {
            for (ExpressSupport support : values()) {
                if (StringUtils.equalsIgnoreCase(name, support.getValidName())) {
                    return support;
                }
            }
            return null;
        }
        public static List<ExpressSupport> getAllList() {
        	List<ExpressSupport> expressList = new ArrayList<ExpressSupport>();
        	for (ExpressSupport expressSupport : ExpressSupport.values()) {
        		expressList.add(expressSupport);
        		
        	}
        	return expressList;
        }
    };
}
