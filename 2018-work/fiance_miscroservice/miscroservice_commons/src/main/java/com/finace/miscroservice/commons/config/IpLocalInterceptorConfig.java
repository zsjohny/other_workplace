package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IP本地拦截配置
 */
public class IpLocalInterceptorConfig {
    private Log logger = Log.getInstance(IpLocalInterceptorConfig.class);


    private final Map<String, AccessRecord> ipIntercept = new ConcurrentHashMap<>(1000);

    /**
     * 是否允许访问
     *
     * @param res 访问请求
     * @return
     */
    public boolean isAccess(HttpServletRequest res) {
        String ip = Iptools.gainRealIp(res);
        if (StringUtils.isEmpty(ip)) {
            logger.warn("空Ip地址正在进行访问......");

            return false;

        } else {
            AccessRecord accessRecord = ipIntercept.get(ip);

            long currentTime = System.currentTimeMillis();

            if (accessRecord == null) {
                accessRecord = new AccessRecord();
                accessRecord.setAccessTimes(0);
                accessRecord.setFirstAccessTime(currentTime);
            }
            long intervalTime = currentTime - accessRecord.getFirstAccessTime();

            if (intervalTime > ACCESS_INTERVAL_TIMES) {
                accessRecord.setAccessTimes(0);
                accessRecord.setFirstAccessTime(currentTime);
            }
            int count = accessRecord.getAccessTimes();

            accessRecord.setAccessTimes(++count);

            ipIntercept.put(ip, accessRecord);


            if (intervalTime < ACCESS_INTERVAL_TIMES && accessRecord.getAccessTimes() > ACCESS_FORBIDDEN_TIMES) {
                logger.warn("ip{} 时间间隔 {} 毫秒 访问路径={}, {} 次 ,已拦截 ", ip, intervalTime, res.getRequestURI(), accessRecord.getAccessTimes());
                return false;

            }


        }
        return true;
    }

    private final int ACCESS_FORBIDDEN_TIMES = 10;
    private final int ACCESS_INTERVAL_TIMES = 2000;


    /**
     * 访问记录
     */
    private class AccessRecord {
        /**
         * 第一次访问时间
         */
        private long firstAccessTime;

        /**
         * 访问的次数
         */
        private int accessTimes;

        public long getFirstAccessTime() {
            return firstAccessTime;
        }

        public void setFirstAccessTime(long firstAccessTime) {
            this.firstAccessTime = firstAccessTime;
        }

        public int getAccessTimes() {
            return accessTimes;
        }

        public void setAccessTimes(int accessTimes) {
            this.accessTimes = accessTimes;
        }

        public void setAccessTimes(Integer accessTimes) {
            this.accessTimes = accessTimes;
        }

        @Override
        public String toString() {
            return "AccessRecord{" +
                    "firstAccessTime=" + firstAccessTime +
                    ", accessTimes=" + accessTimes +
                    '}';
        }
    }

}
