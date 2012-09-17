package cn.dreamfield.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.PubDateFilter;
import cn.jinren.filter.ScriptFilter;
import cn.jinren.filter.SpecialStrFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;


/**
* Description: ��ָ����HTTP������Դ�ڱ������ļ���ʽ���
*/
@Component
public class HttpDownloadUtils {

	private static int BUFFER_SIZE = 4096; //��������С
	private static int IMG_DOWN_THREAD_NUM = 5; 
	private static int IMG_MAX_RELOAD_NUM = 3;
	private static int HTML_DOWN_THREAD_NUM = 2;
	private static int HTML_MAX_RELOAD_NUM = 2;
	private static String FILE_ROOT = "c:/kdownload/";
	private static String IMG_FILE_ROOT = FILE_ROOT + "image/";
	private static String HTML_FILE_ROOT = FILE_ROOT + "/html/";
	
	private ExecutorService imageDownloadThreadPool = Executors.newFixedThreadPool(IMG_DOWN_THREAD_NUM);
	private ExecutorService htmlDownloadThreadPool = Executors.newFixedThreadPool(HTML_DOWN_THREAD_NUM);
	
	public HttpDownloadUtils(){}
	
	public String DownloadImageFromURL(String url) {
		ImageDownloadThread downloadThread = new ImageDownloadThread(url, 0);
		String relativePath = downloadThread.getRelativePath();
		KK.DEBUG(url);
		imageDownloadThreadPool.execute(downloadThread);
		return relativePath;
	}
	
	public String DownloadHtmlFromURL(Spiderable spiderable) {
		HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, 0);
		String relativePath = downloadThread.getRelativePath();
		KK.DEBUG(spiderable.getURL());
		htmlDownloadThreadPool.execute(downloadThread);
		return relativePath;
	}
	
	class HtmlDownloadThread implements Runnable {
		private Spiderable spiderable;
		private String absolutePath;
		private String relativePath;
		private Boolean isStarted = false;
		private int reLoadNum;
		public HtmlDownloadThread(Spiderable spiderable, int reLoadNum) {
			this.spiderable = spiderable;
			this.reLoadNum = reLoadNum;
			loadDownloadInfo();
		}
		@Override
		public void run() {
			if(isStarted) {
				try {
					downloadFile();
				} catch (IOException e) {
					if(reLoadNum < HTML_MAX_RELOAD_NUM) {
						HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, reLoadNum ++);
						htmlDownloadThreadPool.execute(downloadThread);
					} else {
						KK.LOG(spiderable.getURL() + "-->" + e);
						//KK.ERROR(e);
					}
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
			String fileName = dhFormat.format(date) + "_" + MD5Utils.MD5Encode(spiderable.getURL()).toUpperCase() + ".html";
			String path = HTML_FILE_ROOT + ymFormat.format(date) + "/";
			File file = new File(path);
			if(!file.exists()) { //�ж�·���Ƿ���ڣ��������򴴽�
				file.mkdirs();
			}
			relativePath = ymFormat.format(date) + "/" + fileName;
			absolutePath = path + fileName;
			//KK.LOG("�����ļ��� " + absolutePath);
			isStarted = true;
		}
		
		private void downloadFile() throws IOException {
			String str = KKContentSpider.getContentString(spiderable, "gbk");
			StrFilterChain chain = new StrFilterChain();
			chain.addStrFilter(new NetImageFilter())
				.addStrFilter(new ScriptFilter())
				//.addStrFilter(new ATagFilter())
				.addStrFilter(new PubDateFilter())
				.addStrFilter(new SpecialStrFilter());
			String result = chain.doFilter(str);
			FileOutputStream fos = new FileOutputStream(absolutePath);//�����ļ�
			fos.write(result.getBytes());
			fos.close();
		}
	}
	
	class ImageDownloadThread implements Runnable {
		private String destUrl;
		private String absolutePath;
		private String relativePath;
		private Boolean isStarted = false;
		private int reLoadNum;
		public ImageDownloadThread(String destUrl, int reLoadNum) {
			this.destUrl = destUrl;
			this.reLoadNum = reLoadNum;
			loadDownloadInfo();
		}
		@Override
		public void run() {
			if(isStarted) {
				try {
					downloadFile();
				} catch (IOException e) {
					if(reLoadNum < IMG_MAX_RELOAD_NUM) {
						ImageDownloadThread downloadThread = new ImageDownloadThread(destUrl, reLoadNum ++);
						imageDownloadThreadPool.execute(downloadThread);
					} else {
						KK.LOG(destUrl + "-->" + e);
						//KK.ERROR(e);
					}
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
				String path = IMG_FILE_ROOT + ymFormat.format(date) + "/";
				File file = new File(path);
				if(!file.exists()) { //�ж�·���Ƿ���ڣ��������򴴽�
					file.mkdirs();
				}
				relativePath = ymFormat.format(date) + "/" + fileName + matcher.group();
				absolutePath = path + fileName + matcher.group();
				//KK.LOG("�����ļ��� " + absolutePath);
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
	
}