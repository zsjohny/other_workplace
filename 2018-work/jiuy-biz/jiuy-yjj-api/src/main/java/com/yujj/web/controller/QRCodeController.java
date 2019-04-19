package com.yujj.web.controller;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.util.QRCodeUtil;


@Controller
@RequestMapping("/qr")
public class QRCodeController {

    @RequestMapping
    @ResponseBody
    public BufferedImage createQRCode(@RequestParam("content") String content,
                                      @RequestParam(value = "width", defaultValue = "200") int width,
                                      @RequestParam(value = "height", defaultValue = "200") int height) {
        return QRCodeUtil.getQR(content, width, height);
    }
}
