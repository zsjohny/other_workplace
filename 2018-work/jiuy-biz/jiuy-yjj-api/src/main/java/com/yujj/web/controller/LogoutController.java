package com.yujj.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jiuyuan.service.SecurityService;
import com.jiuyuan.util.LoginUtil;
import com.jiuyuan.util.spring.ControllerUtil;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping(method = RequestMethod.GET)
    public String logout(@RequestParam(value = "target_url", required = false) String targetUrl,
                         RedirectAttributes redirectAttributes, HttpServletResponse response) {
        LoginUtil.deleteLoginCookie(response);

        if (StringUtils.isNotBlank(targetUrl) && StringUtils.startsWith(targetUrl, "/logout")) {
            targetUrl = "/";
        } else {
            targetUrl = securityService.getSafeRedirectUrl(targetUrl);
        }

        redirectAttributes.addAttribute("logged_out", true);
        return ControllerUtil.redirect(targetUrl);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse logout(HttpServletResponse response) {
        LoginUtil.deleteLoginCookie(response);
        return new JsonResponse().setSuccessful();
    }
}
