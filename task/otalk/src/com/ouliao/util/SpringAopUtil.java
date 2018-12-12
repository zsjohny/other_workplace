package com.ouliao.util;

import com.ouliao.domain.versionfirst.GeTuiMapper;
import com.ouliao.repository.versionfirst.GeTuiMapperCrudRepository;
import com.ouliao.repository.versionfirst.GeTuiMapperRepository;
import com.xiaoluo.util.GainRealIpUtil;
import com.xiaoluo.util.Getui;
import com.xiaoluo.util.Log4jUtil;
import com.xiaoluo.util.Md5;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

// 代表aop执行的顺序的问题，值越小越优先--可以用于验证和写日志的顺序先后
@Order(1)
@Aspect
@Component
public class SpringAopUtil {
    private static Logger logger = Log4jUtil.init(SpringAopUtil.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GeTuiMapperRepository geTuiMapperRepository;

    @Autowired
    private GeTuiMapperCrudRepository geTuiMapperCrudRepository;

    private Jedis jedis = new Jedis("localhost", 10088);

    // 前置通知，*包含任意个位置的..任意参数
    @Before(value = "execution(public * com.ouliao.controller.*.*.*(.. ))")
    public void beforeValidation(JoinPoint joinPoint) {

        logger.info("用户ip:" + GainRealIpUtil.gainRealIp(request) + ",用户操作:" + joinPoint.getSignature().getName());

        long count = 0;
        String ip = GainRealIpUtil.gainRealIp(request).replaceAll("\\.", "");
        try {

            if (jedis.exists("ip:" + ip)) {
                String code = jedis.get("ip:" + ip);
                count = Long.valueOf(code);
                if (count > 1000) {
                    throw new RuntimeException("频率太快");

                }

            }
            ++count;
            jedis.setex("ip:" + ip, 60, String.valueOf(count));

        } catch (Exception e) {

            try {
                Thread.sleep(400);
                if (jedis.exists("ip:" + ip)) {
                    String code = jedis.get("ip:" + ip);
                    count = Long.valueOf(code);
                    if (count > 1000) {
                        throw new RuntimeException("频率太快");

                    }

                }
                ++count;
                jedis.setex("ip:" + ip, 60, String.valueOf(count));
            } catch (Exception e2) {

                logger.warn(
                        "用户ip:" + GainRealIpUtil.gainRealIp(request) + ",用户过渡操作:" + joinPoint.getSignature().getName());

            }

        }
        String input = request.getHeader("allTalk");

        String value = new sun.misc.BASE64Encoder()
                .encode(Md5.MD5("ouliao2012sangzi" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR)).getBytes());

        if (!value.equals(input)) {

           /* throw new RuntimeException("非法进入...");*/
        }

    }

    // 返回通知
    // JoinPoint代表切入点
    @AfterReturning(pointcut = "execution(public * com.ouliao.controller.*.*.*(.. ))", returning = "object")
    public void afterValidation(JoinPoint joinPoint, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 208);

        try {
            if (object.toString().contains("{\"code\":208")) {
                Integer userId = (Integer) JSONObject.fromObject(object).get("cid");

                GeTuiMapper geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(userId);

                JSONObject json = new JSONObject();

                json.put("loadStatus", "reflesh");

                if (geTuiMapper != null) {

                    if (0 == geTuiMapper.getClientCata()) {
                        Getui.SendAndroid(json.toString(), geTuiMapper.getClientId());

                    }
                }

            }

        } catch (Exception e) {

        }
    }

	/*
     * // 环绕通知
	 *
	 * @Around(value = "execution(public * com.aop.proxy.Shopping.*(.. )) )")
	 * public Object aroundValidation(ProceedingJoinPoint point) { // 执行方法
	 * Object object = null;
	 *
	 * try { // 相当于前置通知 System.out.println("into globalHandle..."); object =
	 * point.proceed(); // 相当于后置返回通知 } catch (Throwable e) { // 相当于异常通知 } //
	 * 相当于后置通知 return point;
	 *
	 * }
	 */

}
