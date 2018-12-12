/**
 *
 */
package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.User;
import com.ouliao.domain.versionfirst.UserAliPay;
import com.ouliao.domain.versionfirst.UserReflect;
import com.ouliao.service.versionfirst.OuLiaoService;
import com.ouliao.service.versionfirst.UserAliPayService;
import com.ouliao.service.versionfirst.UserReflectService;
import com.xiaoluo.util.DesUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author xiaoluo
 * @version $Id: OuLiaoAliPayController.java, 2016年2月25日 下午8:52:11
 */
@Controller
@RequestMapping(value = "user/pay", method = RequestMethod.POST)
public class OuLiaoAliPayController {
    @Autowired
    private UserAliPayService userAliPayService;
    @Autowired
    private OuLiaoService ouLiaoService;
    @Autowired
    private UserReflectService userReflectService;

    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();


    // 支付金额
    @ResponseBody
    @RequestMapping(value = "alipay/saveOrder")
    public JSONObject saveOrder(@RequestParam("uid") String uid, @RequestParam("orderInfo") String orderInfo,
                                @RequestParam("key") String key, @RequestParam("payCount") Double payCount,
                                @RequestParam("sign") String sign, HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();


        key = key.trim().replaceAll(" ", "+");
        // sign = sign.trim().replaceAll(" ", "+");
        // sign = sign.split("&")[8].replaceAll("\\\"", "").replace("sign=",
        // "");

        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(sign) || payCount == null) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            if (request.getSession().getAttribute(key) == null || user == null) {
                jsonObject.put("code", 208);
                if (user != null) {
                    jsonObject.put("cid", user.getUserId());
                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 查询订单状态
            UserAliPay userAliPay = userAliPayService.queryUserAlipayByIdAndPayInfo(user.getUserId(), orderInfo);

            if (userAliPay != null) {

                // 二次检验是否是支付宝进行回调的处理结果
                // if ("true".equals(userAliPay.getIsAuthor())
                // &&
                // payCount.toString().equals(userAliPay.getPayCount().toString())
                // && sign.equals(userAliPay.getSign())) {

                if ("true".equals(userAliPay.getIsAuthor()) && payCount.equals(userAliPay.getPayCount())
                        && StringUtils.isNotEmpty(userAliPay.getSign())) {

                    // 保存订单信息
                    int num = userAliPayService.updateUserAlipayCountByPayId(payCount, user.getUserId(), orderInfo);

                    if (num == 1) {
                        // 存取本地的金钱数量
                        num = ouLiaoService.updateUserMoneyByUserId(
                                user.getUserMoney() == null ? payCount : user.getUserMoney() + payCount,
                                user.getUserId());

                        if (num == 1) {

                            // 删除订单信息
                            num = userAliPayService.deleteUserAlipayByUserAliPayId("1", userAliPay.getUserAliPayId());

                            if (num == 1) {
                                jsonObject.put("code", 200);
                            } else {
                                jsonObject.put("code", 210);
                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                        } else {
                            jsonObject.put("code", 210);
                            response.setStatus(HttpServletResponse.SC_OK);
                        }

                    }

                } else {
                    jsonObject.put("code", 210);
                    response.setStatus(HttpServletResponse.SC_OK);
                }

            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 生产订单
    @ResponseBody
    @RequestMapping(value = "alipay/{uid}/createOrder")
    public JSONObject createOrder(@PathVariable("uid") String uid, @RequestParam("key") String key,
                                  HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key)) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            if (request.getSession().getAttribute(key) == null || user == null) {
                jsonObject.put("code", 208);
                if (user != null) {
                    jsonObject.put("cid", user.getUserId());
                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            UserAliPay userAliPay = new UserAliPay();
            userAliPay.setIsDeleted("0");
            userAliPay.setUserCreateTime(new Date());
            userAliPay.setPayId(user.getUserId());
            userAliPay.setPayInfo(UUID.randomUUID().toString() + System.currentTimeMillis() + "");

            userAliPay = userAliPayService.createOrderByUserId(userAliPay);

            if (userAliPay != null) {
                jsonObject.put("code", 200);
                jsonObject.put("order", userAliPay.getPayInfo());
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }

    // 提现金额
    @ResponseBody
    @RequestMapping(value = "alipay/{uid}/offerPay")
    public JSONObject offerPay(@PathVariable("uid") String uid, @RequestParam("key") String key,
                               @RequestParam("money") Double money, @RequestParam(required = false, value = "name") String name,
                               @RequestParam(required = false, value = "account") String account, HttpServletRequest request,
                               HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();

        key = key.trim().replaceAll(" ", "+");

        try {
            if (StringUtils.isEmpty(key) || money == null) {
                jsonObject.put("code", 202);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 登录检测
            User user = ouLiaoService.queryUserByUserNum(uid);
            if (request.getSession().getAttribute(key) == null || user == null) {
                jsonObject.put("code", 208);
                if (user != null) {
                    jsonObject.put("cid", user.getUserId());
                }
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            String userNum = DesUtil.decrypt(key, user.getCurrentTime());
            if (!user.getUserNum().equals(userNum)) {
                jsonObject.put("code", 209);

                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 套现=====
            String userContract = "true".equals(user.getUserContract()) ? "true" : "false";

            String countNum = jedis.get("user:" + name + "reflect:" + account + "contract:" + userContract);

            if (countNum == null) {
                countNum = "0";
            }

            if ("true".equals(userContract)) {
                if (Double.parseDouble(countNum) > 2000 || money > 2000) {
                    jsonObject.put("code", 210);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            } else {
                if (Double.parseDouble(countNum) > 500 || money > 500) {
                    jsonObject.put("code", 210);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return jsonObject;
                }
            }

            // 60 * 60 * 24 * 30
            jedis.setex("user:" + name + "reflect:" + account + "contract:" + userContract, 60 * 60 * 24 * 30,
                    String.valueOf(Double.parseDouble(countNum) + money));

            // 参数载入
            Properties prop = new Properties();
            prop.load(new InputStreamReader(
                    UserCallMarkController.class.getClassLoader().getResourceAsStream("paramsSet.properties"),
                    "utf-8"));

            if (prop.get("ouliaoService").equals(user.getUserPhone())) {

                jsonObject.put("code", 220);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            if (user.getUserMoney() == null || user.getUserMoney() < money || user.getUserMoney() <= 1) {
                jsonObject.put("code", 214);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 减少用户的金钱数量
            int count = ouLiaoService.updateUserMoneyByUserId(user.getUserMoney() - money, user.getUserId());
            if (count == 0) {
                jsonObject.put("code", 210);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }

            // 存取体现提醒
            UserReflect userReflect = new UserReflect();
            userReflect.setCreateTime(new Date());
            userReflect.setUserId(user.getUserId());
            userReflect.setUserReflectMoney(money);
            userReflect.setUserName(StringUtils.isEmpty(name) == true ? user.getUserAlipayName() : name);
            userReflect.setUserAccount(StringUtils.isEmpty(account) == true ? user.getUserAlipayAccount() : account);
            userReflect.setIsDeleted("0");

            userReflectService.creatUserReflect(userReflect);

            final Properties properties = new Properties();

            properties.load(new InputStreamReader(
                    OuLiaoAliPayController.class.getClassLoader().getResourceAsStream("defaultEmailParam.properties"),
                    "utf-8"));

            if (userReflectService.queryReflectCountByAll() >= Long.parseLong(properties.getProperty("reflectCount"))) {

                Thread thread = new Thread() {
                    public void run() {
                        try {
                            List<UserReflect> lists = userReflectService.queryUserReflectAllByIsDeleted();
                            JSONObject json = null;
                            JSONArray jsonArray = new JSONArray();
                            List<Integer> ids = new ArrayList<>();

                            for (UserReflect us : lists) {
                                if (us == null) {
                                    continue;
                                }
                                json = new JSONObject();
                                json.put("姓名", us.getUserName());
                                json.put("支付宝账号", us.getUserAccount());
                                json.put("提现金额", us.getUserReflectMoney());
                                jsonArray.add(json);
                                ids.add(us.getUserReflectId());
                            }

                            HtmlEmail htmlEmail = new HtmlEmail();

                            htmlEmail.setHostName(
                                    DesUtil.decrypt(properties.getProperty("dbHost"), properties.getProperty("dbKey")));

                            htmlEmail.setAuthentication(
                                    DesUtil.decrypt(properties.getProperty("dbName"), properties.getProperty("dbKey")),
                                    DesUtil.decrypt(properties.getProperty("dbPassword"),
                                            properties.getProperty("dbKey")));

                            htmlEmail.setFrom(
                                    DesUtil.decrypt(properties.getProperty("dbName"), properties.getProperty("dbKey")),
                                    properties.getProperty("sendPerson"), "utf-8");

                            htmlEmail.addTo(DesUtil.decrypt(properties.getProperty("dbEmailTo"),
                                    properties.getProperty("dbKey")), "", "utf-8");
                            htmlEmail.setSubject(properties.getProperty("sendSubject"));
                            htmlEmail.setMsg(jsonArray.toString());
                            htmlEmail.setCharset("utf-8");
                            htmlEmail.send();

                            // int num =
                            // userReflectService.updateIsDeletedByUserReflectId(ids);
                            // if (num != 0) {
                            //
                            // //
                            // SendMsg.batchSend(properties.getProperty("phone"),
                            // // properties.getProperty("msg"));
                            // }

                        } catch (Exception e) {

                        }
                    }
                };
                thread.start();// 启动提醒
            }
            jedis.disconnect();
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;
    }
}
