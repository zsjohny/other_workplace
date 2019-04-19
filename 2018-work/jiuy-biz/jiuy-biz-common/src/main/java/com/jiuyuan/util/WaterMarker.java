
package com.jiuyuan.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片水印
 */
@SuppressWarnings("restriction")
public class WaterMarker {
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String srcImgPath = "e:/2.jpg";
		String iconPath = "e:/3.png";
		String targerPath = "e:/img_mark_icon2.jpg";
		long startTime = System.currentTimeMillis();
		byte[] bytes = image2Bytes(iconPath);
		// 给图片添加水印
		WaterMarker.markImageByIcon(iconPath, srcImgPath, targerPath);
		InputStream inputStream = new FileInputStream(srcImgPath);
		WaterMarker.markImageByIcon(inputStream, bytes);
		System.out.println("markImageByIcon1 time: " + (System.currentTimeMillis() - startTime));

	}

	static byte[] image2Bytes(String imgSrc) throws Exception  
    {  
        FileInputStream fin = new FileInputStream(new File(imgSrc));  
        //可能溢出,简单起见就不考虑太多,如果太大就要另外想办法，比如一次传入固定长度byte[]  
        byte[] bytes  = new byte[fin.available()];  
        //将文件内容写入字节数组，提供测试的case  
        fin.read(bytes);  
          
        fin.close();  
        return bytes;  
}  
	
	/**
	 * 给图片添加水印
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印、可设置水印图片旋转角度
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {

		
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));

			int width = srcImg.getWidth(null);
			int height = srcImg.getHeight(null);

			BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);

			// 得到Image对象。
			Image img = imgIcon.getImage();

			float alpha = 0.5f; // 透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			System.out.println("width,height: " + width + "," + height);
			// 表示水印图片的位置
			g.drawImage(img, width / 4, height / 8, null);
			g.drawImage(img, width - img.getWidth(null)/2, height / 32, null);
			g.drawImage(img, -img.getWidth(null)/2, height / 2, null);
			g.drawImage(img, width / 2, height / 2 - height / 16, null);
			g.drawImage(img, width / 8, height - height / 8, null);
			g.drawImage(img, width - img.getWidth(null)/2, height - height / 4, null);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

			g.dispose();

			os = new FileOutputStream(targerPath);

			// 生成图片
			ImageIO.write(buffImg, "JPG", os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static byte[] markImageByIcon(InputStream inputStream, byte[] content) {
		
		try {
			Image srcImg = ImageIO.read(inputStream);

			int width = srcImg.getWidth(null);
			int height = srcImg.getHeight(null);

			BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(content);

			// 得到Image对象。
			Image img = imgIcon.getImage();

			float alpha = 0.5f; // 透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			System.out.println("width,height: " + width + "," + height);
			// 表示水印图片的位置
			g.drawImage(img, width / 4, height / 8, null);
			g.drawImage(img, width - img.getWidth(null)/2, height / 32, null);
			g.drawImage(img, -img.getWidth(null)/2, height / 2, null);
			g.drawImage(img, width / 2, height / 2 - height / 16, null);
			g.drawImage(img, width / 8, height - height / 8, null);
			g.drawImage(img, width - img.getWidth(null)/2, height - height / 4, null);

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

			g.dispose();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(buffImg);
			
			
			return os.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}
	
	
	
}