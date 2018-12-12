/**
 *
 */
package com.ouliao.util;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaoluo
 * @version $Id: SwitchOn.java, 2016年4月11日 上午10:51:49
 */
@Controller

public class SwitchOn {
    @RequestMapping(value = "user/switch ")
    @ResponseBody
    public Boolean uploadPro() {

        return true;

    }

    @RequestMapping(value = "user/version ")
    @ResponseBody
    public JSONObject uploadVersion() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", false);
        jsonObject.put("version", "2.0.0");
        return jsonObject;

    }

    @RequestMapping(value = "sangzi2015")
    public String login() {
        return "index";
    }
}
