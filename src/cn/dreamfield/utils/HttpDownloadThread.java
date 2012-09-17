package cn.dreamfield.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jinren.test.KK;


/**
* Description: 将指定的HTTP网络资源在本地以文件形式存放
*/
public class HttpDownloadThread implements Runnable{

	private static int BUFFER_SIZE = 4096; //缓冲区大小
	private static String FILE_ROOT = "c:/kdownload/image/";
	
	private String destUrl;
	private String absolutePath;
	private String relativePath;
	
	private Boolean isStarted = false;
	
	public HttpDownloadThread(String destUrl) {
		this.destUrl = destUrl;
		loadDownloadInfo();
	}
	
	@Override
	public void run() {
		if(isStarted) {
			try {
				downloadFile();
			} catch (IOException e) {
				KK.ERROR(e);
			}
		}
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	
	private void loadDownloadInfo() {
		Date date = new Date();
		SimpleDateFormat ymFormat = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat dhFormat = new SimpleDateFormat("ddHH");
		//生成文件名
		String fileName = dhFormat.format(date) + "_" + MD5Utils.MD5Encode(destUrl).toUpperCase();
		Pattern pattern = Pattern.compile("[.][\\w]+?$");
		Matcher matcher = pattern.matcher(destUrl);
		if(matcher.find()) { //匹配后缀名
			String path = FILE_ROOT + ymFormat.format(date) + "/";
			File file = new File(path);
			if(!file.exists()) { //判断路径是否存在，不存在则创建
				file.mkdirs();
			}
			relativePath = ymFormat.format(date) + "/" + fileName + matcher.group();
			absolutePath = path + fileName + matcher.group();
			KK.LOG("下载文件到 " + absolutePath);
			isStarted = true;
		}
	}
	
	private void downloadFile() throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;

		url = new URL(destUrl);
		httpUrl = (HttpURLConnection) url.openConnection();//建立链接
		httpUrl.connect();//连接指定的资源
		bis = new BufferedInputStream(httpUrl.getInputStream());//获取网络输入流
		
		fos = new FileOutputStream(absolutePath);//建立文件
		while ((size = bis.read(buf)) != -1) {//保存文件
			fos.write(buf, 0, size);
		}
		
		fos.close();
		bis.close();
		httpUrl.disconnect();
	}

}