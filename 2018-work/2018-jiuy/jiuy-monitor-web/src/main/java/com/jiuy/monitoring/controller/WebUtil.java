package com.jiuy.monitoring.controller;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aison
 * @date 2018/4/24 16:18
 * @Copyright: 玖远网络
 */
public class WebUtil {

    /**
     * 获取request 中的参数返回map
     * @param request
     * @date:   2018/4/24 16:21
     * @author: Aison
     */
    public static Map<String, Object> getRequestMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> maps = new HashMap<String, Object>();
        String[] vals = null;
        for (Map.Entry<String, String[]> en : map.entrySet()) {

            vals = en.getValue();
            if (vals != null && vals.length > 0) {
                // 单个属性
                if (vals.length == 1 && !StringUtils.isBlank(vals[0])) {
                    maps.put(en.getKey(),vals[0]);
                }
                // 数组属性
                if (vals.length > 1) {
                    maps.put(en.getKey().replace("[]", ""), en.getValue());
                }
            }
        }
        return maps;
    }
}
