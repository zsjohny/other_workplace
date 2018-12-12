package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.enums.RedisKeyEnum;
import com.finace.miscroservice.commons.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 验证码的随机生成工具
 */
public class CodeGenerateUtil {

    private CodeGenerateUtil() {

    }

    private static Log log = Log.getInstance(CodeGenerateUtil.class);
    private static String BASE64SUFFIX = "data:image/png;base64,";

    // 图片的宽度。
    private static final int WIDTH = 160;
    // 图片的高度。
    private static final int HEIGHT = 40;
    // 验证码字符个数
    private static final int CODE_COUNT = 4;
    // 验证码干扰线数
    private static final int LINE_COUNT = 20;

    //默认的失效时间(单位分钟)
    private static final int DEFAULT_EXPIRE_TIME = 5;

    //随机数的生成器
    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    //随机数
    private static ThreadLocal<String> code = new ThreadLocal<>();

    //存储code的redis操作类
    private static ValueOperations<String, String> codeStrRedisTemplate;


    // 生成图片
    private static BufferedImage createImage() {
        int fontWidth = WIDTH / CODE_COUNT;// 字体的宽度
        int fontHeight = HEIGHT - 5;// 字体的高度
        int codeY = HEIGHT - 8;

        // 图像buffer
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();

        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        Font font = getFont(fontHeight);

        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < LINE_COUNT; i++) {
            int xs = random.nextInt(WIDTH);
            int ys = random.nextInt(HEIGHT);
            int xe = xs + random.nextInt(WIDTH);
            int ye = ys + random.nextInt(HEIGHT);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.01f;// 噪声率
        int area = (int) (yawpRate * WIDTH * HEIGHT);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);

            buffImg.setRGB(x, y, random.nextInt(255));
        }


        String str1 = randomStr(CODE_COUNT);// 得到随机字符

        code.set(str1);
        for (int i = 0; i < CODE_COUNT; i++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
            g.drawString(strRand, i * fontWidth + 3, codeY);
        }


        return buffImg;
    }

    // 得到随机字符
    private static String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    // 得到随机颜色
    private static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 产生随机字体
     */
    private static Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Wide Latin", Font.PLAIN, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
        return font[random.nextInt(5)];
    }


    /**
     * 获取存储操作类
     */
    public static ValueOperations<String, String> getRedisTemplate() {
        if (codeStrRedisTemplate == null) {
            codeStrRedisTemplate = (ValueOperations<String, String>) ApplicationContextUtil.getBean("codeStrRedisTemplate");

            if (codeStrRedisTemplate == null) {
                throw new RuntimeException("please instance codeStrRedisTemplate");
            }

        }
        return codeStrRedisTemplate;


    }

    /**
     * 验证验证码
     *
     * @param uid  设备标识
     * @param code 验证码
     * @param uri  拦截的请求连接
     * @return
     */
    public static Boolean verify(String uid, String code, String uri) {

        Boolean verifyFlag = Boolean.FALSE;

        if (StringUtils.isAnyEmpty(uid, code, uri)) {
            log.warn("用户所传参数uid={} code={} 为空 uri={}", uid, code, uri);
            return verifyFlag;
        }

        String saveCode = getRedisTemplate().get((RedisKeyEnum.CODE_STR_REDIS_PREFIX_KEY.toKey() + uid + ":" + uri).intern());


        if (code.equalsIgnoreCase(saveCode)) {
            verifyFlag = Boolean.TRUE;
            log.info("设备={} 验证码={} 验证通过", uid, code);
            //清除验证标识
            clear(uid, uri);
        } else {
            log.info("设备={} 原验证码={} 输入验证码为={} 验证失败", uid, saveCode, code);
        }

        return verifyFlag;

    }

    /**
     * uid清除验证
     *
     * @param uid 用户的设备uid
     * @param uri 请求的方法
     */
    private static void clear(String uid, String uri) {
        HashOperations<String, String, Map<String, Object>> limiterHashRedisTemplate = (HashOperations<String, String, Map<String, Object>>) ApplicationContextUtil.getBean("limiterHashRedisTemplate");

        if (limiterHashRedisTemplate == null) {
            log.warn("not instance limiterHashRedisTemplate");
            return;
        }
        limiterHashRedisTemplate.delete((RedisKeyEnum.LIMITER_HASH_REDIS_PREFIX_KEY.toKey() + uri).intern(), uid);
        log.info("用户uid={} 请求方法={} 拦截已经消除", uid, uri);


    }


    /**
     * 生成验证码
     *
     * @param uid      需要生成的设备
     * @param uri      需要生成验证码的请求
     * @param response 生成的响应流
     * @return
     */
    public static void generate(String uid, String uri, HttpServletResponse response) {

        if (StringUtils.isAnyEmpty(uid, uri)) {
            log.warn("所传的uid={} uri={}为空", uid, uri);
            return;
        }
        if (response == null) {
            log.warn("所传的输入流为空");
            return;
        }

        ServletOutputStream os = null;

        try {

            //获取是否是H5 h5直接base64
            if (UidUtils.isRegularUidByDevice(uid.split(":")[0], DeviceEnum.H5)) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ImageIO.write(createImage(), "png", baos);
                String base64Data = Base64.getEncoder().encodeToString(baos.toByteArray());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setContentType("utf-8");
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write(BASE64SUFFIX + base64Data);
                baos.close();

            } else {

                os = response.getOutputStream();
                //生成验证码
                ImageIO.write(createImage(), "png", os);

            }


            if (code != null) {

                log.info("用户={}生成图形验证码={},uri={}", uid, code.get().toLowerCase(), uri);
                //生成字符串验证码
                String codesResult = code.get().toLowerCase();

                //存储
                getRedisTemplate().set((RedisKeyEnum.CODE_STR_REDIS_PREFIX_KEY.toKey() + uid + ":" + uri).intern(), codesResult, DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);

                code.remove();
            }


        } catch (Exception e) {
            log.error("uid={} 生成随机验证码出错", uid, e);

        } finally {
            //关闭流
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("输入流关闭错误", e);
                }
            }

        }


    }


}