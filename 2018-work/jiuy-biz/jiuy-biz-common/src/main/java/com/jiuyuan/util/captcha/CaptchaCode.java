package com.jiuyuan.util.captcha;

import java.awt.image.BufferedImage;

public class CaptchaCode {
    private String code;

    private BufferedImage image;

    public CaptchaCode(String code, BufferedImage image) {
        this.code = code;
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
