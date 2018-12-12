package com.finace.miscroservice.commons.utils.tools;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** 
 *  
 * 上传图片　工具类 大图片路径,生成小图片路径, 大图片文件名,生成小图片文名, 生成小图片宽度,生成小图片高度, 是否等比缩放(默认为true)) 
 *  
 * @author Administrator 
 *  
 */  
public class UploadUtil  {  
	 public File getFile(MultipartFile imgFile,List fileTypes,String path,String brandName,String typeName) {  
	        String fileName = imgFile.getOriginalFilename();  
	        //获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名  
	         String ext = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());  
	         //对扩展名进行小写转换  
	         ext = ext.toLowerCase();  
	           
	         File file = null;  
	         if(fileTypes.contains(ext)) {       
	        	String   finelURL= fileName.substring(0,fileName.lastIndexOf('.') - 1);
	        	 //如果扩展名属于允许上传的类型，则创建文件  
	             file = this.creatFolder(typeName, brandName,path, new SimpleDateFormat("yyyymmddhhmmss").format(new Date())+".jpg");  
	             try {  
	            	
	                imgFile.transferTo(file);                   //保存上传的文件  
	            } catch (IllegalStateException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	         }  
	         return file;  
	    }  
	      
	    /** 
	     * 检测与创建一级、二级文件夹、文件名 
	            这里我通过传入的两个字符串来做一级文件夹和二级文件夹名称 
	           通过此种办法我们可以做到根据用户的选择保存到相应的文件夹下 
	            
	     */  
	 public File creatFolder(String typeName,String brandName,String path,String fileName) {  
	         File file = null;  
	         typeName = typeName.replaceAll("/", "");               //去掉"/"  
	         typeName = typeName.replaceAll(" ", "");               //替换半角空格  
	         typeName = typeName.replaceAll(" ", "");               //替换全角空格  
	          
	         brandName = brandName.replaceAll("/", "");             //去掉"/"  
	         brandName = brandName.replaceAll(" ", "");             //替换半角空格  
	         brandName = brandName.replaceAll(" ", "");             //替换全角空格  
	        // File firstFolder = new File("/data/upload" + typeName);         //一级文件夹   
	         File firstFolder = new File(path+"data/images/avatar" + typeName);         //一级文件夹  
	         if(firstFolder.exists()) {                             //如果一级文件夹存在，则检测二级文件夹  
	             File secondFolder = new File(firstFolder,brandName);  
	             if(secondFolder.exists()) {                        //如果二级文件夹也存在，则创建文件  
	                 file = new File(secondFolder,fileName);  
	             }else {                                            //如果二级文件夹不存在，则创建二级文件夹  
	                 secondFolder.mkdir();  
	                 file = new File(secondFolder,fileName);        //创建完二级文件夹后，再合建文件  
	             }  
	         }else {                                                //如果一级不存在，则创建一级文件夹  
	             firstFolder.mkdir();  
	             File secondFolder = new File(firstFolder,brandName);  
	             if(secondFolder.exists()) {                        //如果二级文件夹也存在，则创建文件  
	                 file = new File(secondFolder,fileName);  
	             }else {                                            //如果二级文件夹不存在，则创建二级文件夹  
	                 secondFolder.mkdir();  
	                 file = new File(secondFolder,fileName);  
	             }  
	         }  
	         return file;  
	    }
	
	
	 /**  
	     * 实现图像的等比缩放  
	     * @param source  
	     * @param targetW  
	     * @param targetH  
	     * @return  
	     */  
	    private static BufferedImage resize(BufferedImage source, int targetW,   
	            int targetH) {   
	        // targetW，targetH分别表示目标长和宽   
	        int type = source.getType();   
	        BufferedImage target = null;   
	        double sx = (double) targetW / source.getWidth();   
	        double sy = (double) targetH / source.getHeight();   
	        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放   
	        // 则将下面的if else语句注释即可   
	        if (sx < sy) {   
	            sx = sy;   
	            targetW = (int) (sx * source.getWidth());   
	        } else {   
	            sy = sx;   
	            targetH = (int) (sy * source.getHeight());   
	        }   
	        if (type == BufferedImage.TYPE_CUSTOM) { // handmade   
	            ColorModel cm = source.getColorModel();   
	            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,   
	                    targetH);   
	            boolean alphaPremultiplied = cm.isAlphaPremultiplied();   
	            target = new BufferedImage(cm, raster, alphaPremultiplied, null);   
	        } else  
	            target = new BufferedImage(targetW, targetH, type);   
	        Graphics2D g = target.createGraphics();   
	        // smoother than exlax:   
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
	                RenderingHints.VALUE_INTERPOLATION_BICUBIC);   
	        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));   
	        g.dispose();   
	        return target;   
	    }   
	  
	    /**  
	     * 实现图像的等比缩放和缩放后的截取  
	     * @param inFilePath 要截取文件的路径  
	     * @param outFilePath 截取后输出的路径  
	     * @param width 要截取宽度  
	     * @param hight 要截取的高度  
	     * @param proportion  
	     * @throws Exception  
	     */  
	       
	    public static void saveImageAsJpg(String inFilePath, String outFilePath,   
	            int width, int hight, boolean proportion)throws Exception {   
	         File file = new File(inFilePath);   
	         InputStream in = new FileInputStream(file);   
	         File saveFile = new File(outFilePath);   
	  
	        BufferedImage srcImage = ImageIO.read(in);   
	        if (width > 0 || hight > 0) {   
	            // 原图的大小   
	            int sw = srcImage.getWidth();   
	            int sh = srcImage.getHeight();   
	            // 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去   
	                srcImage = resize(srcImage, width, hight);   
	        }   
	        // 缩放后的图像的宽和高   
	        int w = srcImage.getWidth();   
	        int h = srcImage.getHeight();   
	        // 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取   
	        if (w == width) {   
	            // 计算X轴坐标   
	            int x = 0;   
	            int y = h / 2 - hight / 2;   
	            saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);   
	        }   
	        // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取   
	        else if (h == hight) {   
	            // 计算X轴坐标   
	            int x = w / 2 - width / 2;   
	            int y = 0;   
	            saveSubImage(srcImage, new Rectangle(x, y, width, hight), saveFile);   
	        }   
	        in.close();   
	    }   
	    /**  
	     * 实现缩放后的截图  
	     * @param image 缩放后的图像  
	     * @param subImageBounds 要截取的子图的范围  
	     * @param subImageFile 要保存的文件  
	     * @throws IOException  
	     */  
	    private static void saveSubImage(BufferedImage image,   
	            Rectangle subImageBounds, File subImageFile) throws IOException {   
	        if (subImageBounds.x < 0 || subImageBounds.y < 0  
	                || subImageBounds.width - subImageBounds.x > image.getWidth()   
	                || subImageBounds.height - subImageBounds.y > image.getHeight()) {   
	            System.out.println("Bad   subimage   bounds");   
	            return;   
	        }   
	        BufferedImage subImage = image.getSubimage(subImageBounds.x,subImageBounds.y, subImageBounds.width, subImageBounds.height);   
	        String fileName = subImageFile.getName();   
	        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);   
	       ImageIO.write(subImage, formatName, subImageFile);   
	       
	    }   

}  
