package com.reliable.controller

import com.reliable.domain.FindPhoneRecord
import com.reliable.domain.User
import com.reliable.util.GainRealIpUtil
import net.sf.json.JSONObject
import org.apache.commons.lang.StringUtils
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import com.reliable.service.UserService
import com.reliable.util.DesIosAndAndroid
import com.reliable.util.Md5

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import java.text.DecimalFormat

/**
 * Created by nessary on 16-5-7.
 */
@Component
@Path("reliable")
class UserController {
    @Autowired
    private UserService userService;
    private JedisPool jedisPool = new JedisPool("localhost", 10088);
    private Jedis jedis = jedisPool.getResource();

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public String test() {

        return "hello"
    }


    @GET
    @Path("/gaincode/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject gaincode(@PathParam("phone") String phone) {

        JSONObject jsonObject = new JSONObject()

        if (!phone ==~ /((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}/) {
            jsonObject.put("code", 202)
        }

        String num = "";
        for (int i = 0; i < 6; i++) {
            num += String.valueOf(((int) (Math.random() * 10)));
        }

        int count = 0
        if (jedis.exists("user:" + phone)) {

            count = Integer.valueOf(jedis.get("user:" + phone))

            if (count > 5) {
                jsonObject.put("code", 204)
                jsonObject.put("num", 5)
                return jsonObject
                return
            }
        }
        count++
        jedis.setex("user:" + phone, 60 * 5, String.valueOf(count))
        jedis.set("user:" + phone + ":code", num)
        jedis.disconnect()
        jsonObject.put("code", 200)
        jsonObject.put("num", num)
        return jsonObject

    }

    @GET
    @Path("/compcode/{phone}/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject compcode(@PathParam("phone") String phone, @PathParam("code") String code) {
        JSONObject jsonObject = new JSONObject()


        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}")) {


            jsonObject.put("code", 202);

            return jsonObject;
        }

        if (!jedis.exists("user:" + phone + ":code")) {
            jsonObject.put("code", 203);

            return jsonObject;

        }
        String codeOrg = jedis.get("user:" + phone + ":code")


        if (codeOrg.equals(code)) {

            jedis.setex("user:" + phone + ":isComp", 60, "true")
            jedis.del("user:" + phone + ":code")
            jsonObject.put("code", 200);
        } else {
            jsonObject.put("code", 204);
        }

        jedis.disconnect()


        return jsonObject;


    }

    @GET
    @Path("/entry/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object entry(
            @PathParam("phone") String phone, @QueryParam("pass") String pass, @QueryParam("sex") String sex) {
        JSONObject jsonObject = new JSONObject()

        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || StringUtils.isEmpty(sex) || StringUtils.isEmpty(pass) || pass.length() < 5) {


            jsonObject.put("code", 202);

            return jsonObject;
        }


        if (!jedis.exists("user:" + phone + ":isComp")) {

            jsonObject.put("code", 203);

            return jsonObject;

        }


        if (jedis.exists("user:" + phone + ":list")) {

            jsonObject.put("code", 204);

            return jsonObject;

        }


        User user = new User(["phone": phone, "pass": pass, "sex": sex])

        userService.saveUser(user)

        jsonObject.put("code", 200);

        String key = Md5.MD5(pass + System.currentTimeMillis() + phone)

        jedis.set("user:" + phone + ":temp", key)
        jsonObject.put("key", key)

        jedis.del("user:" + phone + ":isComp")
        jedis.disconnect()

        return jsonObject


    }


    @GET
    @Path("/reset/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object reset(@PathParam("phone") String phone, @QueryParam("pass") String pass) {
        JSONObject jsonObject = new JSONObject()

        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || StringUtils.isEmpty(pass) || pass.length() < 5) {


            jsonObject.put("code", 202);

            return jsonObject;
        }

//        if (!jedis.exists("user:" + phone + ":isComp")) {
//
//            jsonObject.put("code", 203);
//
//            return jsonObject;
//
//        }

        if (!jedis.exists("user:" + phone + ":list")) {

            jsonObject.put("code", 204);

            return jsonObject;

        }



        User user = new User(["phone": phone, "pass": pass])

        userService.updateUser(user)

        jedis.del("user:" + phone + ":isComp")

        jedis.disconnect()

        jsonObject.put("code", 200);

        return jsonObject;
    }


    @GET
    @Path("/load/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject load(@PathParam("phone") String phone, @QueryParam("pass") String pass) {


        JSONObject jsonObject = new JSONObject()


        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || StringUtils.isEmpty(pass) || pass.length() < 5) {


            jsonObject.put("code", 202);

            return jsonObject;
        }

        User user = userService.findOne(new User("phone": phone))

        if (user == null) {

            jsonObject.put("code", 204);

            return jsonObject;

        }

        String passOut = DesIosAndAndroid.decryptDES(user.getPass(), phone)

        if (passOut.equals(pass)) {
            String key = Md5.MD5(pass + System.currentTimeMillis() + phone)

            jedis.set("user:" + phone + ":temp", key)
            jsonObject.put("key", key)


            long supportCount = userService.queryCount()
            long totalCount = userService.queryAllCount()
            if (supportCount / totalCount > 0.40) {

                jsonObject.put("outsideSupportCount", new DecimalFormat("#0.00").format(supportCount / totalCount * 100))
                jsonObject.put("gaySupportCount", new DecimalFormat("#0.00").format(85 + (double) Math.random() * 5))
            } else {

                jsonObject.put("outsideSupportCount", new DecimalFormat("#0.00").format(85 + (double) Math.random() * 5))
                jsonObject.put("gaySupportCount", new DecimalFormat("#0.00").format(85 + (double) Math.random() * 5))
            }
            jsonObject.put("code", 200);
            jsonObject.put("switch", true)
        } else {
            jsonObject.put("code", 203);
        }

        jedis.disconnect()
        return jsonObject

    }


    @GET
    @Path("/outside/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject outside(
            @PathParam("phone") String phone,
            @QueryParam("key") String key,
            @QueryParam("operateSys") String operateSys,
            @QueryParam("findPhone") String findPhone,
            @QueryParam("partTime") String partTime, @Context HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject()

        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || (!findPhone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || StringUtils.isEmpty(operateSys) || StringUtils.isEmpty(partTime) || StringUtils.isEmpty(findPhone) || StringUtils.isEmpty(key) || key.length() < 5) {


            jsonObject.put("code", 202);

            return jsonObject;
        }



        if (!jedis.exists("user:" + phone + ":list")) {
            jsonObject.put("code", 204);

            return jsonObject;
        }
        if (!jedis.exists("user:" + phone + ":temp")) {
            jsonObject.put("code", 205);

            return jsonObject;
        }

        String keySave = jedis.get("user:" + phone + ":temp")


        if (keySave.equals(key)) {

            String realIp = GainRealIpUtil.gainRealIp(request);

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://whois.pconline.com.cn/jsFunction.jsp?callback=jsShow&ip=" + realIp);

            HttpResponse httpResponse = httpClient.execute(post);


            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                try {
                    String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");

                    realIp = result.substring(35, result.indexOf("'", 27) + 8);
                } catch (Exception e) {

                }
            }






            FindPhoneRecord findPhoneRecord = new FindPhoneRecord(["phone": phone, "findPhone": findPhone, "createTime": new Date(), "realIp": realIp])

            userService.saveUserFind(findPhoneRecord)
            jsonObject.put("code", 200)
            jsonObject.put("result", "")

        } else {
            jsonObject.put("code", 203)

        }

        return jsonObject
    }


    @GET
    @Path("/isSupport/{phone}")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject isSupport(
            @PathParam("phone") String phone,
            @QueryParam("key") String key,
            @QueryParam("isSupport") String isSupport) {
        JSONObject jsonObject = new JSONObject()

        if ((!phone == ~"((13[0-9])|(15[^4,\\\\D])|(14[57])|(17([0-1]|[6-8]))|(18[0,2,5-9]))\\d{8}") || StringUtils.isEmpty(isSupport) || StringUtils.isEmpty(key) || key.length() < 5) {


            jsonObject.put("code", 202);

            return jsonObject;
        }



        if (!jedis.exists("user:" + phone + ":list")) {
            jsonObject.put("code", 204);

            return jsonObject;
        }
        if (!jedis.exists("user:" + phone + ":temp")) {
            jsonObject.put("code", 205);

            return jsonObject;
        }

        String keySave = jedis.get("user:" + phone + ":temp")


        if (keySave.equals(key)) {


            User user = new User(["phone": phone, "isSupport": "true"])

            userService.updateUser(user)
            jsonObject.put("code", 200);
        } else {
            jsonObject.put("code", 203);

        }






        return jsonObject
    }

}
