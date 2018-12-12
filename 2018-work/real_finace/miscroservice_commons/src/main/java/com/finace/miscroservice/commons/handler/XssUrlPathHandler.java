package com.finace.miscroservice.commons.handler;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.finace.miscroservice.commons.auth.XSSInjectInterceptor.xssClean;

/**
 * Xss的url路径处理器
 */
public class XssUrlPathHandler extends UrlPathHelper {
    @Override
    public Map<String, String> decodePathVariables(HttpServletRequest request, Map<String, String> vars) {
        Map<String, String> result = super.decodePathVariables(request, vars);
        if (!CollectionUtils.isEmpty(result)) {
            for (String key : result.keySet()) {
                result.put(key, xssClean(request,result.get(key)));
            }
        }
        return result;
    }

    @Override
    public MultiValueMap<String, String> decodeMatrixVariables(HttpServletRequest request,
                                                               MultiValueMap<String, String> vars) {
        MultiValueMap<String, String> mvm = super.decodeMatrixVariables(request, vars);
        if (!CollectionUtils.isEmpty(mvm)) {
            for (String key : mvm.keySet()) {
                List<String> value = mvm.get(key);
                for (int i = 0; i < value.size(); i++) {
                    value.set(i, xssClean(request,value.get(i)));
                }
            }
        }

        return mvm;
    }


}
