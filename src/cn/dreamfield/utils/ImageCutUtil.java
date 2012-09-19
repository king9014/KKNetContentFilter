package cn.dreamfield.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import cn.jinren.test.KK;

public class ImageCutUtil {
	
	private static String ICO_FILE_ROOT = "c:/kdownload/ico/";

	public static String ImageCut(String srcImageFile, double standardWidth, double standardHeight) {
		String relativePath = "";
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd/");
			String destPath = ICO_FILE_ROOT + sdf.format(date) + MD5Util.MD5Encode(srcImageFile).toUpperCase();
			relativePath = sdf.format(date) + MD5Util.MD5Encode(srcImageFile).toUpperCase();
			File file = new File(ICO_FILE_ROOT + sdf.format(date));
			if(!file.exists()) {
				file.mkdirs();
			}
			Image img;
		    ImageFilter cropFilter;
		    // 读取源图像
		    BufferedImage bi = ImageIO.read(new File(srcImageFile));
		    int srcWidth = bi.getWidth(); 	// 源图宽度
		    int srcHeight = bi.getHeight(); // 源图高度
		    if (srcWidth > standardWidth && srcHeight > standardHeight) {
		    	Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int w = 0;
				int h = 0;
				
				double wScale = srcWidth / standardWidth;
				double hScale = srcHeight / standardHeight;
				int srcWidth2;
				int srcHeight2;
				if (wScale > hScale) {
					srcWidth2 = (int) (standardWidth * hScale);
			     	w = (srcWidth - srcWidth2) / 2;
			    	srcWidth = srcWidth2;
			     	h = 0;
				} else {
					srcHeight2 = (int) (standardHeight * wScale);
					h = (srcHeight - srcHeight2) / 2;
					srcHeight = srcHeight2;
					w = 0;
				}

				cropFilter = new CropImageFilter(w, h, srcWidth, srcHeight);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null);
				g.dispose();
				Pattern pattern = Pattern.compile("[.]([\\w]+?$)"); //文件后缀匹配
				Matcher matcher = pattern.matcher(srcImageFile);
				if(matcher.find()) {
					destPath = destPath + "." + matcher.group(1);
					relativePath = relativePath + "." + matcher.group(1);
					ImageIO.write(tag, matcher.group(1).toUpperCase(), new File(destPath));
				}
				imageScale(destPath, standardWidth, standardHeight);
		    }
		} catch (Exception e) {
		    KK.ERROR(e);
		}
		return relativePath;
	}
	
	public static void imageScale(String srcImageFile, double standardWidth, double standardHeight) {
		try {
		    BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
		    Image image = src.getScaledInstance((int) standardWidth, (int) standardHeight, Image.SCALE_DEFAULT);
		    BufferedImage tag = new BufferedImage((int) standardWidth,
		      (int) standardHeight, BufferedImage.TYPE_INT_RGB);
		    Graphics g = tag.getGraphics();
		    g.drawImage(image, 0, 0, null);
		    g.dispose();
		    Pattern pattern = Pattern.compile("[.]([\\w]+?$)"); //文件后缀匹配
			Matcher matcher = pattern.matcher(srcImageFile);
			if(matcher.find()) {
				ImageIO.write(tag, matcher.group(1).toUpperCase(), new File(srcImageFile)); // 输出到文件流
		    }
		} catch (IOException e) {
		    KK.ERROR(e);
		}
	}
}
