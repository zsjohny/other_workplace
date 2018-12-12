package com.ouliao.controller.versionfirst;

import com.ouliao.domain.versionfirst.GeTuiMapper;
import com.ouliao.repository.versionfirst.GeTuiMapperCrudRepository;
import com.ouliao.repository.versionfirst.GeTuiMapperRepository;
import com.ouliao.service.versionfirst.OuLiaoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "user/getui")
public class GeTuiInterfacter {
    @Autowired
    private GeTuiMapperCrudRepository geTuiMapperCrudRepository;
    @Autowired
    private GeTuiMapperRepository geTuiMapperRepository;
    @Autowired
    private OuLiaoService ouLiaoService;
    private JedisPool pool = new JedisPool("localhost", 10088);
    private Jedis jedis = pool.getResource();

//    private Jedis jedis = new Jedis("local", 10088);

    @ResponseBody
    @RequestMapping(value = "loadAccount/{id}/{deviceSign}")
    public JSONObject loadAccount(@PathVariable("id") Integer ids, @PathVariable("deviceSign") Integer deviceSign,
                                  @RequestParam("cid") String cid,
                                  @RequestParam(value = "deviceToken", required = false) String getuiDeviceToken, HttpServletRequest request,
                                  HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        try {
            // 用户是否存在
            if (ouLiaoService.queryUserByUserId(ids) == null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            GeTuiMapper geTuiMapper = null;

            try {
                geTuiMapper = geTuiMapperRepository.queryIsExist(cid);
            } catch (Exception e) {
                List<GeTuiMapper> list = geTuiMapperRepository.queryAllIsExist(cid);
                geTuiMapper = null;
                geTuiMapperCrudRepository.delete(list);

            }
            // 根据客户端id删除用户
            if (geTuiMapper != null) {

                geTuiMapperCrudRepository.delete(geTuiMapper.getGeTuiMapperId());

            }
            // 根据用户删除客户端id
            try {
                geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(ids);
            } catch (Exception e) {
                List<GeTuiMapper> list = geTuiMapperRepository.queryAllIsExist(cid);
                geTuiMapper = null;
                geTuiMapperCrudRepository.delete(list);

            }
            if (geTuiMapper != null) {

                geTuiMapperCrudRepository.delete(geTuiMapper.getGeTuiMapperId());

            }
            // 保存个推
            geTuiMapper = new GeTuiMapper();

            geTuiMapper.setIsDeleted("0");
            geTuiMapper.setClientId(cid);
            geTuiMapper.setUserId(ids);
            geTuiMapper.setClientCata(deviceSign == 0 ? 0 : 1);
            geTuiMapper.setUserCreateTime(new Date());
            geTuiMapper.setGetuiDeviceToken(deviceSign == 1 ? getuiDeviceToken : "");
            geTuiMapperCrudRepository.save(geTuiMapper);
            try {
                // 检测是都是第一个客户端登录
                if (!jedis.exists(String.valueOf(ids))) {
                    jedis.set(String.valueOf(ids), "true");
                }
                jedis.disconnect();
            } catch (Exception e) {

            }
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

    @ResponseBody
    @RequestMapping(value = "deleAccount/{id}")
    public JSONObject deleAccount(@PathVariable("id") Integer ids, @RequestParam("cid") String cid,
                                  HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        try {
            // 用户是否存在
            if (ouLiaoService.queryUserByUserId(ids) == null) {
                jsonObject.put("code", 203);
                response.setStatus(HttpServletResponse.SC_OK);
                return jsonObject;
            }
            GeTuiMapper geTuiMapper = geTuiMapperRepository.queryIsExist(cid);

            // 根据客户端id删除用户
            if (geTuiMapper != null) {

                geTuiMapperCrudRepository.delete(geTuiMapper.getGeTuiMapperId());

            }
            // 根据用户删除客户端id
            geTuiMapper = geTuiMapperRepository.queryGeiTuiByUserId(ids);
            if (geTuiMapper != null) {

                geTuiMapperCrudRepository.delete(geTuiMapper.getGeTuiMapperId());

            }
            try {

                jedis.del(String.valueOf(ids));
                jedis.disconnect();
            } catch (Exception e) {

            }
            jsonObject.put("code", 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {

            jsonObject.put("code", 201);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return jsonObject;

    }

}
