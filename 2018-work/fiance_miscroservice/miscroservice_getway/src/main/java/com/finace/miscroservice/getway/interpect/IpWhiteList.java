package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Ip白名单列表
 */
public class IpWhiteList {


    private static Log log = Log.getInstance(IpWhiteList.class);

    private static String CONFIG_NAME = "whitelist.properties";


    private static final String SEQUENCE_SIGN = "{n}";
    private static final int MAX_SEQUENCE_COUNT = 128;


    /**
     * ==>map的 key为访问的方法 key支持递归的有序的书写 占位符为{n}
     * ==>value为访问域名或者是访问的Ip
     *
     * @1当是域名时候 需要全路径 例如 http://www.example.com
     * @2当是IP时候 支持模糊写入 例如 192.168.*.*
     */
    private static Multimap<String, String> whiteList;


    //所需IP的正则表达式
    private static Pattern pattern = Pattern.compile("((\\d{1,3}|\\*).){3}(\\d{1,3}|\\*)");


    static {

        HashMultimap<String, String> multimap = HashMultimap.create();

        whiteList = Multimaps.synchronizedMultimap(multimap);

        //加载白名单列表
        Properties properties = new Properties();
        try {
            properties.load(new ClassPathResource(CONFIG_NAME).getInputStream());

            String[] values;
            String key;
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                values = entry.getValue().toString().split(",");
                for (String str : values) {
                    key = String.valueOf(entry.getKey());
                    if (key.contains(SEQUENCE_SIGN)) {
                        String[] keys = key.split("\\" + SEQUENCE_SIGN);
                        for (int k = 0; k < MAX_SEQUENCE_COUNT; k++) {
                            whiteList.put(getSplitKey(keys, k), str);
                        }

                    } else {
                        whiteList.put(key, str);
                    }
                    log.info("加载路径={} 白名单={} 成功", entry.getKey(), str);
                }
            }

        } catch (IOException e) {
            log.error("加载白名单列表失败", e);
        }

    }

    /**
     * 获得分割的key
     *
     * @param keys    需要待分割的keys数组
     * @param linkKey 前缀的key
     * @return
     */
    private static String getSplitKey(String[] keys, Object linkKey) {

        StringBuilder keyBuild = new StringBuilder(keys.length);
        if (keys.length != 1) {
            for (int i = 0; i < keys.length - 1; i++) {
                keyBuild.append(keys[i]);
                keyBuild.append(linkKey);
            }
            keyBuild.append(keys[keys.length - 1]);
        } else {
            keyBuild.append(keys[0]);
            keyBuild.append(linkKey);
        }


        return keyBuild.toString();

    }


    /**
     * 检查是否是白名单列表
     *
     * @param request
     * @return true是白名单 false不是白名单
     */
    public static WhiteListCheckResult check(HttpServletRequest request) {

        WhiteListCheckResult checkResult = WhiteListCheckResult.FORBID;

        String method = request.getRequestURI();
        if (StringUtils.isEmpty(method)) {
            log.warn("Ip={} 所访问的方法为空 不给予白名单检查", Iptools.gainRealIp(request));

            return checkResult;
        }

        //先检查是否是需要访问的白名单方法
        Collection<String> _whiteLists = whiteList.get(method);

        if (_whiteLists == null || _whiteLists.isEmpty()) {
            log.info("用户所访问的方法={} 不是白名单,不给予白名单检查", method);
            checkResult = WhiteListCheckResult.NOT_CHECK;
            return checkResult;
        }


        String authHeader = request.getHeader("origin");
        if (authHeader == null || authHeader.isEmpty()) {
            authHeader = request.getHeader("referer");
            //防止指针异常
            authHeader = authHeader == null ? "" : authHeader;


        }
        String ip = Iptools.gainRealIp(request);

        //检查获取的待检查是否为空
        if (StringUtils.isEmpty(ip) && StringUtils.isEmpty(authHeader)) {
            log.warn("用户所传origin 和Ip 都为空 不给予通过");
            return checkResult;
        }


        //检查

        for (String str : _whiteLists) {
            //IP检查
            if (pattern.matcher(str).matches()) {
                if (checkIpSimilar(str, ip)) {
                    checkResult = WhiteListCheckResult.PASS;
                    break;
                }

            } else {
                //原路径检查
                if (authHeader.contains(str)) {
                    checkResult = WhiteListCheckResult.PASS;
                    break;
                }
            }
        }

        switch (checkResult) {
            case FORBID:
                log.info("Ip={} 访问方法={} 不在白名单中 拒绝访问", ip, method);
                break;

            case PASS:
                log.info("Ip={} 访问方法={} 在白名单中 略过检查", ip, method);
                break;
            default:
                //nothing

        }

        return checkResult;


    }

    public enum WhiteListCheckResult {

        PASS, FORBID, NOT_CHECK

    }


    /**
     * 检查Ip是否符合规范
     *
     * @param checkIp     检查的原Ip
     * @param waitCheckIp 待检查的Ip
     * @return
     */
    private static Boolean checkIpSimilar(String checkIp, String waitCheckIp) {

        Boolean equalRightFlag = Boolean.FALSE;
        //分割
        String[] origin = checkIp.split("\\.");
        String[] check = waitCheckIp.split("\\.");

        if (origin.length != 4 || check.length != 4) {
            log.warn("所比较的Ip格式不正确 checkIp={} waitIp={}", checkIp, waitCheckIp);
            return equalRightFlag;
        }

        int len = origin.length;

        String str;
        int _checkCount = 0;
        int _notCheckCount = 0;
        for (int i = 0; i < len; i++) {
            str = origin[i];
            if (str.equals("*")) {
                //略过检查
                _notCheckCount++;
                continue;
            }
            if (str.equals(check[i])) {
                _checkCount++;
            }
        }

        if (_notCheckCount == len) {
            log.info("Ip={} 符合全IP 特征", waitCheckIp);
            equalRightFlag = Boolean.TRUE;
            return equalRightFlag;
        }
        //检查是否符合
        if (_checkCount + _notCheckCount == len) {
            equalRightFlag = Boolean.TRUE;
            log.info("待检查的ip={} 符合Ip={} 特征", waitCheckIp, checkIp);
        } else {
            log.info("待检查的ip={} 不符合Ip={} 特征", waitCheckIp, checkIp);

        }


        return equalRightFlag;

    }


}
