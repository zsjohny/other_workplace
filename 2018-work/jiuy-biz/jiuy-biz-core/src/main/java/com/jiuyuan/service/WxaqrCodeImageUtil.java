package com.jiuyuan.service;

import com.jiuyuan.util.BizImgUtils;
import com.jiuyuan.util.ImageUtil;
import com.jiuyuan.util.JiuyMultipartFile;
import com.jiuyuan.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @类功能说明: java拼接多张图片，生成的格式是jpg、bmp、gif等，如果其他格式则报错或者photoshop或者画图工具打不开
 * @修改人员名: yang
 * @修改日期： 2016-3-10 上午10:35:04
 * @创建时间： 2016-3-10 上午10:35:04
 * -------------------------------------------------------------------------------------------
 * 修改次数         修改人员    修改内容                       修改原因
 * @备注：只能拼接同类型的图片（不能连续拼接）、宽度需要一致（高度可以不限制）
 * @版本： V1.0
 */
@Service
public class WxaqrCodeImageUtil {


    private static final Logger logger = LoggerFactory.getLogger(WxaqrCodeImageUtil.class);

    //测试类
    public static void main(String[] args) {

        getPath();
    }


    private static String getPath() {
        String filePath = "/tmp/sgenerateProductShareDescriptionImgerver_deploy_port_dir/image/data/wxaProductShareImageTempDir/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        return filePath;
    }


    /**
     * 阿里云图片地址转为服务器本地地址
     * wxaProductShareImageTempDir
     */
    public static String aliImgToServerImg(String aliImage, HttpServletRequest request) {
        //阿里云图片地址转为服务器本地地址
//		String s = UUID.randomUUID().toString();
        String filePath = getPath();
        logger.info("aliImgToServerImg.filePath:" + filePath);
        WxaqrCodeImageUtil.createfile(filePath);

        //下载到指定文件夹
        String[] split = aliImage.split("/");
        String imageName = split[split.length - 1];
        logger.info("aliImgToServerImg.imageName:" + imageName);
        File file = new File(filePath + "/" + imageName);
        InputStream inputStream = WxaqrCodeImageUtil.getInputStreamByGet(aliImage);
        WxaqrCodeImageUtil.saveData(inputStream, file);
        logger.info("aliImgToServerImg.ile.getPath():" + file.getPath());
//		String webRoot = WebUtil.getWebBaseUrl(request,"https");
//		logger.info("aliImgToServerImg.webRoot:"+webRoot);
        String webRoot = "";
        String serverImg = webRoot + "/wxaProductShareImageTempDir/" + imageName;
        logger.info("aliImgToServerImg.serverImg:" + serverImg);
        return serverImg;
    }

    //获取小程序商品分享图片
    public static Map<String, Object> getWxaqrCodeImage(String[] images) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        //生成存放图片的临时文件夹
        String s = UUID.randomUUID().toString();
        String filePath = "tmp/server_deploy_port_dir/image/share/" + s + "/";
        logger.info("获取小程序商品分享图片filePath:" + filePath);
        data.put("filePath", filePath);
        WxaqrCodeImageUtil.createfile(filePath);

        //下载图片到临时文件夹
        File[] files = new File[images.length];
        for (int i = 0; i < images.length; i++) {
            String[] split = images[i].split("/");
            String imageName = split[split.length - 1];
            files[i] = new File(filePath + "/" + imageName);
            InputStream inputStream = WxaqrCodeImageUtil.getInputStreamByGet(images[i]);
            WxaqrCodeImageUtil.saveData(inputStream, files[i]);
        }
        logger.info("下载图片到临时文件夹完成:" + filePath);
        //拼接小程序商品分享图片
        logger.info("获取小程序商品分享图片filePathNew:" + filePath);
        MultipartFile multipartFile = null;
        if (files.length > 0) {
            logger.info("开始Java将商品详情图和二维码拼接成长图:");
            multipartFile = ImageUtil.merge(files, filePath);
            logger.info("完成Java将商品详情图和二维码拼接成长图:");
        }

        data.put("multipartFile", multipartFile);
        return data;
    }

    //获取小程序商品分享图片
    public static Map<String, Object> getWxaqrCodeImage(String image) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        //生成存放图片的临时文件夹
        String s = UUID.randomUUID().toString();
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        String filePath = servletContext.getRealPath("/" + s);
        logger.info("filePath:" + filePath);
//		System.out.println(filePath);
        data.put("filePath", filePath);
        WxaqrCodeImageUtil.createfile(filePath);

        //下载图片到临时文件夹
        String[] split = image.split("/");
        String imageName = split[split.length - 1];
        File file = new File(filePath + "/" + imageName);
        InputStream inputStream = WxaqrCodeImageUtil.getInputStreamByGet(image);
        WxaqrCodeImageUtil.saveData(inputStream, file);
        logger.info("下载图片到临时文件夹完成:" + filePath);
//		logger.info("files:"+JSON.toJSONString(files));
        logger.info("filePath:" + file.getPath());
        MultipartFile partFile = new JiuyMultipartFile(file);

        data.put("multipartFile", partFile);
        return data;
    }

    //创建临时文件夹
    public static void createfile(String filename) {
        File file = new File(filename);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
    }

    //
    //删除文件
    public static void deleteFile(File file) {
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {//遍历目录下所有的文件
                    deleteFile(files[i]);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }

    //
///** 
//    * Java拼接多张图片 
//    *  
//    * @param imgs 
//    * @param type 
//    * @param dst_pic 
//    * @return 
//    */  
//   public static boolean merge(String[] imgs, String type, String dst_pic) {  
//    //获取需要拼接的图片长度
//       int len = imgs.length;  
//       //判断拼接图片的张数是否大于0
//       if (len < 1) {  
//           return false;  
//       }  
//       File[] src = new File[len];  
//       BufferedImage[] images = new BufferedImage[len];  
//       int[][] ImageArrays = new int[len][];  
//       for (int i = 0; i < len; i++) {  
//           try {  
//               src[i] = new File(imgs[i]);  
//               images[i] = ImageIO.read(src[i]);  
//           } catch (Exception e) {  
//               e.printStackTrace();  
//               return false;  
//           }  
//           int width = images[i].getWidth();  
//           System.out.println(width);
//           int height = images[i].getHeight();  
//        // 从图片中读取RGB 像素
//           ImageArrays[i] = new int[width * height];
//           ImageArrays[i] = images[i].getRGB(0, 0, width, height,  ImageArrays[i], 0, width);  
//       }  
// 
//       int dst_height = 0;  
//       int dst_width = images[0].getWidth();  
//     //合成图片像素
//       for (int i = 0; i < images.length; i++) {  
//           dst_width = dst_width > images[i].getWidth() ? dst_width     : images[i].getWidth();  
//           dst_height += images[i].getHeight();  
//       }  
//       //合成后的图片
//       System.out.println("宽度:"+dst_width);  
//       System.out.println("高度:"+dst_height);  
//       if (dst_height < 1) {  
//           System.out.println("dst_height < 1");  
//           return false;  
//       }  
//       // 生成新图片   
//       try {  
//           // dst_width = images[0].getWidth();   
//           BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,  
//                   BufferedImage.TYPE_INT_RGB);  
//           int height_i = 0;  
//           for (int i = 0; i < images.length; i++) {  
//               ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),  
//                       ImageArrays[i], 0, dst_width);  
//               height_i += images[i].getHeight();  
//           }  
// 
//           File outFile = new File(dst_pic);  
//           ImageIO.write(ImageNew, type, outFile);// 写图片 ，输出到硬盘 
//       } catch (Exception e) {  
//           e.printStackTrace();  
//           return false;  
//       }  
//       return true;  
//   }  
//   
//   public static void zoomImage(String src,String dest,int w,int h) throws Exception {
//       
//       double wr=0,hr=0;
//       File srcFile = new File(src);
//       File destFile = new File(dest);
//
//       BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
//       Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);//设置缩放目标图片模板
//       
//       wr=w*1.0/bufImg.getWidth();     //获取缩放比例
//       hr=h*1.0 / bufImg.getHeight();
//
//       AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
//       Itemp = ato.filter(bufImg, null);
//       try {
//           ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile); //写入缩减后的图片
//       } catch (Exception ex) {
//           ex.printStackTrace();
//       }
//   }
//   
    // 通过get请求得到读取器响应数据的数据流
    public static InputStream getInputStreamByGet(String url) {
        InputStream connInputStream = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                connInputStream = conn.getInputStream();
                return cloneInputStream(connInputStream);
//               return connInputStream;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            BizImgUtils.closeStream(connInputStream);
            conn.disconnect();
        }
        return null;
    }


    /**
     * @param input input
     * @return java.io.InputStream
     * @author Charlie
     * @date 2019/1/4 11:52
     */
    private static InputStream cloneInputStream(InputStream input) {
        InputStream inputStream;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            inputStream = new ByteArrayInputStream(baos.toByteArray());
            return inputStream;
        } catch (IOException e) {
            logger.error("复制一个流,IOException");
            return null;
        } finally {
            BizImgUtils.closeStream(baos);
        }
    }


    // 将服务器响应的数据流存到本地文件
    public static void saveData(InputStream is, File file) {
        try (BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(
                     new FileOutputStream(file));) {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}