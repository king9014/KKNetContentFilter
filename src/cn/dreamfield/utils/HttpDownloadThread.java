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
* Description: ��ָ����HTTP������Դ�ڱ������ļ���ʽ���
*/
public class HttpDownloadThread implements Runnable{

	private static int BUFFER_SIZE = 4096; //��������С
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
		//�����ļ���
		String fileName = dhFormat.format(date) + "_" + MD5Utils.MD5Encode(destUrl).toUpperCase();
		Pattern pattern = Pattern.compile("[.][\\w]+?$");
		Matcher matcher = pattern.matcher(destUrl);
		if(matcher.find()) { //ƥ���׺��
			String path = FILE_ROOT + ymFormat.format(date) + "/";
			File file = new File(path);
			if(!file.exists()) { //�ж�·���Ƿ���ڣ��������򴴽�
				file.mkdirs();
			}
			relativePath = ymFormat.format(date) + "/" + fileName + matcher.group();
			absolutePath = path + fileName + matcher.group();
			KK.LOG("�����ļ��� " + absolutePath);
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
		httpUrl = (HttpURLConnection) url.openConnection();//��������
		httpUrl.connect();//����ָ������Դ
		bis = new BufferedInputStream(httpUrl.getInputStream());//��ȡ����������
		
		fos = new FileOutputStream(absolutePath);//�����ļ�
		while ((size = bis.read(buf)) != -1) {//�����ļ�
			fos.write(buf, 0, size);
		}
		
		fos.close();
		bis.close();
		httpUrl.disconnect();
	}

}