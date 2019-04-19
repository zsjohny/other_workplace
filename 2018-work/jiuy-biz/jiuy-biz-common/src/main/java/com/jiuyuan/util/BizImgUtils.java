package com.jiuyuan.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/7/16 15:08
 */
public class BizImgUtils{

    /**
     * 缩放图片
     *
     * @param src src
     * @param w w
     * @param h h
     * @return void
     * @author Charlie
     * @date 2018/7/18 14:59
     */
    public static BufferedImage zoomImage(BufferedImage src,int w,int h){
        double wr=0,hr=0;
        //获取缩放比例
        wr=w*1.0/src.getWidth();
        hr=h*1.0 / src.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        try {
            return (BufferedImage) ato.filter(src, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 文本生成图片
     *
     * @param width  width
     * @param height height
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 18:57
     */
    public static BufferedImage text2ImgApi(Integer width, Integer height) throws Exception {
        String fontStyle = "微软雅黑";
        // 创建图片
        BufferedImage image = new BufferedImage (width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics ();
        g.setClip (0, 0, width, height);
        //背景色
        g.setColor (new Color (242, 242, 242));
        g.fillRect (0, 0, width, height);
        //字体颜色
        g.setColor (Color.red);
        //设置画笔字体
        g.setFont (new Font (fontStyle, Font.BOLD, 30));
        g.drawString ("¥50", 0, 50);


        String activitySourcePrice = "¥300";
        g.setColor (Color.black);
        AttributedString as = new AttributedString (activitySourcePrice);
        as.addAttribute (TextAttribute.FONT, (new Font (fontStyle, Font.PLAIN, 20)));
        as.addAttribute (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        g.drawString (as.getIterator (), 100, 60);

        //活动底色
        g.setColor (Color.red);
        g.fillRect (40, 90, 90, 30);

        //活动说明
        String activityDescription = "团购价";
        g.setColor (Color.black);
        g.setFont (new Font (fontStyle, Font.PLAIN, 30));
        g.drawString (activityDescription, 40, 110);

        g.dispose ();
        return image;
    }


    /**
     * 将image输出成文件
     *
     * @param image          image
     * @param targetFilePath 输出文件,eg D:\test.jpg
     * @return boolean
     * @author Charlie
     * @date 2018/7/16 18:42
     */
    public static boolean bufferedImg2ImgFile(BufferedImage image, String targetFilePath) throws IOException {
        return ImageIO.write (
                image,
                targetFilePath.split ("\\.")[1],
                new File (targetFilePath));
    }


    /**
     * 将image输出成文件
     *
     * @param image          image
     * @param targetFilePath 输出文件,eg D:\test.jpg
     * @return boolean
     * @author Charlie
     * @date 2018/7/16 18:42
     */
    public static boolean bufferedImg2ImgFile(BufferedImage image, File targetFilePath) throws IOException {
        return ImageIO.write (
                image,
                targetFilePath.getAbsolutePath ().split ("\\.")[1],
                targetFilePath);
    }


    /**
     * 小图片贴到大图片形成一张图(合成)
     *
     * @param bigImg   大图
     * @param smallImg 小图
     * @param x 小图坐标
     * @param y 大图坐标
     * @author Charlie
     * @date 2018/7/16 17:59
     */
    public static BufferedImage overlapImage(BufferedImage bigImg, BufferedImage smallImg, int x, int y) {
        try {
            Graphics2D g = bigImg.createGraphics ();
            g.drawImage (smallImg, x, y, smallImg.getWidth (), smallImg.getHeight (), null);
            g.dispose ();
            return bigImg;
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }







    /**
     * 图片拼接
     *
     * @param isHorizontal isHorizontal
     * @param imgs         imgs
     * @author Charlie
     * @date 2018/7/16 18:05
     */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs) {
        // 生成新图片
        BufferedImage destImage;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            allw += img.getWidth ();
            allh += img.getHeight ();
            if (img.getWidth () > allwMax) {
                allwMax = img.getWidth ();
            }
            if (img.getHeight () > allhMax) {
                allhMax = img.getHeight ();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage (allw, allhMax, BufferedImage.TYPE_INT_RGB);
        }
        else {
            destImage = new BufferedImage (allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth ();
            int h1 = img.getHeight ();
            // 从图片中读取RGB
            int[] imagearrayone = new int[w1 * h1];
            // 逐行扫描图像中各个像素的RGB到数组中
            imagearrayone = img.getRGB (0, 0, w1, h1, imagearrayone, 0, w1);
            if (isHorizontal) {
                // 水平方向合并,设置上半部分或左半部分的RGB
                destImage.setRGB (wx, 0, w1, h1, imagearrayone, 0, w1);
            }
            else {
                // 垂直方向合并,设置上半部分或左半部分的RGB
                destImage.setRGB (0, wy, w1, h1, imagearrayone, 0, w1);
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }


    /**
     * 将图片路径加载成imgBuffered
     *
     * @param imgPath 图片文件的路径
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 19:02
     */
    public static BufferedImage imgFile2BufferedImg(String imgPath) throws IOException {
        return ImageIO.read (new File (imgPath));
    }


    /**
     * 将图片路径加载成imgBuffered
     *
     * @param imgPaths 图片文件的路径
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 19:02
     */
    private static BufferedImage[] imgFile2BufferedImg(String... imgPaths) throws IOException {
        BufferedImage[] imageArr = new BufferedImage[imgPaths.length];
        for (int i = 0; i < imgPaths.length; i++) {
            imageArr[i] = imgFile2BufferedImg(imgPaths[i]);
        }
        return imageArr;
    }


    /**
     * 将图片路径加载成imgBuffered
     *
     * @param in 图片文件资源
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 19:02
     */
    public static BufferedImage imgFile2BufferedImg(InputStream in) throws IOException {
        return ImageIO.read (in);
    }


    /**
     * 将图片路径加载成imgBuffered
     *
     * @param ins 图片文件资源
     * @return java.awt.image.BufferedImage
     * @author Charlie
     * @date 2018/7/16 19:02
     */
    private static BufferedImage[] imgFile2BufferedImg(InputStream... ins) throws IOException {
        BufferedImage[] imageArr = new BufferedImage[ins.length];
        for (int i = 0; i < ins.length; i++) {
            imageArr[i] = imgFile2BufferedImg(ins[i]);
        }
        return imageArr;
    }







    /**
     * 关于图片的几个demo
     *
     * @param args args
     * @return void
     * @author Charlie
     * @date 2018/7/16 19:19
     */
    public static void main(String[] args) throws Exception {
        //合并两图
        BufferedImage img = mergeImage (false, imgFile2BufferedImg ("D:\\baby.jpg", "D:\\chaplin.jpg"));
        boolean b = bufferedImg2ImgFile (img, "D:\\girls.jpg");
        System.out.println (b);

        //文本生成图片
        BufferedImage txtImg = text2ImgApi (
                1000,
                1000
        );
        bufferedImg2ImgFile (txtImg, "D:\\文本创建的文件.jpg");

        //贴图
        BufferedImage image = overlapImage (img, txtImg, 30, 30);
        bufferedImg2ImgFile (image, "D:\\最终图像.jpg");

    }


    /**
     * 关闭流
     *
     * @param cs 流
     * @return void
     * @author Charlie
     * @date 2018/7/17 15:42
     */
    public static void closeStream(Closeable... cs) {
        for (Closeable stream : cs) {
            if (stream != null) {
                try {
                    stream.close ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
