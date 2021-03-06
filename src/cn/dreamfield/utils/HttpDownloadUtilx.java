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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.PatternReader;
import cn.dreamfield.dao.NetInfoDao;
import cn.dreamfield.dao.NetInfoPageDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.tempopt.TempOptUtil;
import cn.jinren.filter.ATagFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;


/**
* Description: 将指定的HTTP网络资源在本地以文件形式存放
*/
@Component
public class HttpDownloadUtilx {

	private static int BUFFER_SIZE = 4096; //缓冲区大小
	private static int IMG_DOWN_THREAD_NUM = 5; 
	private static int IMG_MAX_RELOAD_NUM = 3;
	private static int HTML_DOWN_THREAD_NUM = 2;
	private static int HTML_MAX_RELOAD_NUM = 2;
	private static String FILE_ROOT = KKConf.FILE_ROOT;
	private static String IMG_FILE_ROOT = FILE_ROOT + "image/";
	private static String HTML_FILE_ROOT = FILE_ROOT + "html/";
	
	private ExecutorService imageDownloadThreadPool = Executors.newFixedThreadPool(IMG_DOWN_THREAD_NUM);
	private ExecutorService htmlDownloadThreadPool = Executors.newFixedThreadPool(HTML_DOWN_THREAD_NUM);
	
	@Autowired
	private NetInfoDao netInfoDao;
	@Autowired
	private NetInfoPageDao netInfoPageDao;
	
	public HttpDownloadUtilx(){}
	
	public String DownloadImageFromURL(String url) {
		ImageDownloadThread downloadThread = new ImageDownloadThread(url, 0);
		String relativePath = downloadThread.getRelativePath();
		KK.DEBUG(url);
		imageDownloadThreadPool.execute(downloadThread);
		return relativePath;
	}
	
	public void DownloadHtmlFromURL(NetInfo netInfo, String decode) {
		Spiderable spiderable = PatternReader.getContentSpiderable(netInfo.getInfoWebsite());
		spiderable.setURL(netInfo.getInfoOriginUrl());
		if("N".equals(netInfo.getInfoStatus())) {
			HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, decode, netInfo, 0);
			KK.DEBUG(spiderable.getURL());
			htmlDownloadThreadPool.execute(downloadThread);
		}
	}
	
	public void DownloadChildPageFromURL(NetInfo netInfo, NetInfoPage cInfoPage, String decode) {
		Spiderable spiderable = PatternReader.getContentSpiderable(netInfo.getInfoWebsite());
		spiderable.setURL(cInfoPage.getPageOriginUrl()); //给定子文章页面地址
		if("N".equals(cInfoPage.getPageStatus())) {
			HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, decode, netInfo, cInfoPage, 0);
			KK.DEBUG(spiderable.getURL());
			htmlDownloadThreadPool.execute(downloadThread);
		}
	}
	
	class HtmlDownloadThread implements Runnable {
		private Spiderable spiderable;
		private String absolutePath;
		private String relativePath;
		private Boolean isStarted = false;
		private Boolean isChildPage = false;
		private int reLoadNum;
		private NetInfo netInfo;
		private NetInfoPage cInfoPage;
		private String decode;
		public HtmlDownloadThread(Spiderable spiderable, String decode, NetInfo netInfo, int reLoadNum) {
			this.spiderable = spiderable;
			this.reLoadNum = reLoadNum;
			this.netInfo = netInfo;
			this.decode = decode;
			loadDownloadInfo();
		}
		public HtmlDownloadThread(Spiderable spiderable, String decode, NetInfo netInfo, NetInfoPage cInfoPage, int reLoadNum) {
			this.spiderable = spiderable;
			this.reLoadNum = reLoadNum;
			this.netInfo = netInfo;
			this.cInfoPage = cInfoPage;
			this.decode = decode;
			isChildPage = true;
			loadDownloadInfo();
		}
		@Override
		public void run() {
			if(isStarted) {
				try {
					downloadFile();
				} catch (IOException e) {
					if(reLoadNum < HTML_MAX_RELOAD_NUM) {
						try {
							Thread.sleep(1700);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						KK.INFO("[HTML RELOAD]: " + "[" + reLoadNum + "] " + spiderable.getURL()); 
						HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, decode, netInfo, ++ reLoadNum);
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
			SimpleDateFormat ymFormat = new SimpleDateFormat("yyMMdd");
			SimpleDateFormat dhFormat = new SimpleDateFormat("HHmm");
			//生成文件名
			String fileName = dhFormat.format(date) + "_" + MD5Util.MD5Encode(spiderable.getURL()) + ".html";
			String path = HTML_FILE_ROOT + ymFormat.format(date) + "/";
			File file = new File(path);
			if(!file.exists()) { //判断路径是否存在，不存在则创建
				file.mkdirs();
			}
			relativePath = ymFormat.format(date) + "/" + fileName;
			absolutePath = path + fileName;
			//KK.LOG("下载文件到 " + absolutePath);
			isStarted = true;
		}
		
		/**
		 * synchronized 主要考虑到在NetImageFilter中会新建下载线程，造成netArticle==null的可能性增加了
		 * @param str
		 * @throws IOException
		 */
		private synchronized void infoPersistent(String str) throws IOException {
			//拿到对应未本地化的数据模型
			if(null == netInfo) {
				throw new IOException("NetInfo--NULL");
			} else if("N".equals(netInfo.getInfoStatus())) {
				StrFilterChain chain = new StrFilterChain();
				TempOptUtil.addTempChainBeforeATag(chain, netInfo);
				chain.addStrFilter(new ATagFilter());
				TempOptUtil.addTempChainAfterATag(chain, netInfo);
				String result = chain.doFilter(str);
				FileOutputStream fos = new FileOutputStream(absolutePath);//建立文件
				fos.write(result.getBytes());
				fos.close();
				//在这可能要做一些持久化的配置
				netInfo.setInfoHtmlUrl(relativePath);
				netInfo.setInfoStatus("E");
				netInfoDao.updateNetInfo(netInfo);
			}
		}
		
		private synchronized void childInfoPersistent(String str) throws IOException {
			//拿到对应未本地化的数据模型
			if(null == cInfoPage) {
				throw new IOException("ChildNetInfo--NULL");
			} else if("N".equals(cInfoPage.getPageStatus())) {
				StrFilterChain chain = new StrFilterChain();
				TempOptUtil.addTempChainBeforeATag(chain, netInfo);
				chain.addStrFilter(new ATagFilter());
				TempOptUtil.addTempChainAfterATag(chain, netInfo);
				String result = chain.doFilter(str);
				FileOutputStream fos = new FileOutputStream(absolutePath);//建立文件
				fos.write(result.getBytes());
				fos.close();
				//在这可能要做一些持久化的配置
				if(2 == cInfoPage.getPageCurrent()) {
					netInfo.setInfoStatus("P");
					netInfo.setInfoCid(cInfoPage.getInfoPageId());
					netInfoDao.updateNetInfo(netInfo);
				}
				cInfoPage.setPageHtmlUrl(relativePath);
				cInfoPage.setPageStatus("E");
				netInfoPageDao.updateNetInfoPage(cInfoPage);
			}
		}
		
		private void downloadFile() throws IOException {
			String str = KKContentSpider.getContentString(spiderable, decode);
			if(isChildPage) {
				childInfoPersistent(str);
			} else {
				infoPersistent(str);
			}
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
						try {
							Thread.sleep(1700);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						KK.INFO("[IMG RELOAD]: " + destUrl); 
						ImageDownloadThread downloadThread = new ImageDownloadThread(destUrl, ++ reLoadNum);
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
			SimpleDateFormat ymFormat = new SimpleDateFormat("yyMMdd");
			SimpleDateFormat dhFormat = new SimpleDateFormat("HHmm");
			//生成文件名
			String fileName = dhFormat.format(date) + "_" + MD5Util.MD5Encode(destUrl);
			Pattern pattern = Pattern.compile("[.][\\w]+?$");
			Matcher matcher = pattern.matcher(destUrl);
			if(matcher.find()) { //匹配后缀名
				String path = IMG_FILE_ROOT + ymFormat.format(date) + "/";
				File file = new File(path);
				if(!file.exists()) { //判断路径是否存在，不存在则创建
					file.mkdirs();
				}
				relativePath = ymFormat.format(date) + "/" + fileName + matcher.group();
				absolutePath = path + fileName + matcher.group();
				//KK.LOG("下载文件到 " + absolutePath);
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
	
}