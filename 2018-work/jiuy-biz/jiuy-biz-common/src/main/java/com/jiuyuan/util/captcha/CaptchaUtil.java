package com.jiuyuan.util.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class CaptchaUtil {

    public static CaptchaCode generateCaptchaCode(int width, int height, int codeCount) {
        /**
         * xx
         */
        int xx = 0;
        /**
         * 字体高度
         */
        int fontHeight;
        /**
         * codeY
         */
        int codeY;

        xx = width / (codeCount + 2); // 生成随机数的水平距离
        fontHeight = height - 12; // 生成随机数的数字高度
        codeY = height - 8; // 生成随机数的垂直距离

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();

        // 创建一个随机数生成器类
        Random random = new Random();

        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体。
        gd.setFont(font);

        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生4条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }

        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        String randomCode = RandomStringUtils.randomAlphanumeric(codeCount);
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String strRand = String.valueOf(randomCode.charAt(i));
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(125);
            green = random.nextInt(255);
            blue = random.nextInt(200);

            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(strRand, (i + 1) * xx, codeY);
        }
        gd.dispose();
        
        return new CaptchaCode(randomCode, buffImg);
    }
}
