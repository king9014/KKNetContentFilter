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

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.jinren.filter.ATagFilter;
import cn.jinren.filter.IntroFilter;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.PaginationFilter;
import cn.jinren.filter.PubDateFilter;
import cn.jinren.filter.ScriptFilter;
import cn.jinren.filter.SpecialStrFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.filter.TitleFilter;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;


/**
* Description: 将指定的HTTP网络资源在本地以文件形式存放
*/
@Component
public class HttpDownloadUtil {

	private static int BUFFER_SIZE = 4096; //缓冲区大小
	private static int IMG_DOWN_THREAD_NUM = 5; 
	private static int IMG_MAX_RELOAD_NUM = 3;
	private static int HTML_DOWN_THREAD_NUM = 2;
	private static int HTML_MAX_RELOAD_NUM = 2;
	private static String FILE_ROOT = "c:/kdownload/";
	private static String IMG_FILE_ROOT = FILE_ROOT + "image/";
	private static String HTML_FILE_ROOT = FILE_ROOT + "html/";
	
	private ExecutorService imageDownloadThreadPool = Executors.newFixedThreadPool(IMG_DOWN_THREAD_NUM);
	private ExecutorService htmlDownloadThreadPool = Executors.newFixedThreadPool(HTML_DOWN_THREAD_NUM);
	
	@Autowired
	private NetArticleDao netArticleDao;
	
	public HttpDownloadUtil(){}
	
	public String DownloadImageFromURL(String url) {
		ImageDownloadThread downloadThread = new ImageDownloadThread(url, 0);
		String relativePath = downloadThread.getRelativePath();
		KK.DEBUG(url);
		imageDownloadThreadPool.execute(downloadThread);
		return relativePath;
	}
	
	public void DownloadHtmlFromURL(Spiderable spiderable) {
		//防止同一篇文章的重复下载
		NetArticle originArticle = netArticleDao.getNetArticleEntity(spiderable.getURL());
		if(null == originArticle) {
			KK.WARN("DownloadHtmlFromURL(Spiderable)-->此方法不能在数据库中创建新文章");
			return;
		} else if("N".equals(originArticle.getIsExist())) {
			HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, 0);
			KK.DEBUG(spiderable.getURL());
			htmlDownloadThreadPool.execute(downloadThread);
		}
	}
	
	/**
	 * 此方法用于直接下载单篇文章 2012年9月19日 20:51:20
	 * @param spiderable
	 * @param category
	 */
	public void DownloadHtmlFromURL(Spiderable spiderable, String category) {
		//防止同一篇文章的重复下载
		NetArticle originArticle = netArticleDao.getNetArticleEntity(spiderable.getURL());
		if(null == originArticle) {
			NetArticle netArticle = new NetArticle();
			netArticle.setCategory(category);
			netArticle.setOriginUrl(spiderable.getURL()); 		//item0 --- url
			netArticle.setIsExist("N"); 						//文章还没有本地化，暂时设为N
			netArticle.setName("BLANK");						//在此创建的文章需要在Filter里填写标题
			netArticle.setOptDate(new Date());
			netArticleDao.saveNetArticle(netArticle);
			HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, 0);
			KK.DEBUG(spiderable.getURL());
			htmlDownloadThreadPool.execute(downloadThread);
		} else if("N".equals(originArticle.getIsExist())) {
			HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, 0);
			KK.DEBUG(spiderable.getURL());
			htmlDownloadThreadPool.execute(downloadThread);
		}
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
						KK.INFO("[HTML RELOAD]: " + "[" + reLoadNum + "] " + spiderable.getURL()); 
						HtmlDownloadThread downloadThread = new HtmlDownloadThread(spiderable, ++ reLoadNum);
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
			String fileName = dhFormat.format(date) + "_" + MD5Util.MD5Encode(spiderable.getURL()).toUpperCase() + ".html";
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
		private synchronized void articlePersistent(String str) throws IOException {
			//拿到对应未本地化的数据模型
			NetArticle netArticle = netArticleDao.getNetArticleEntity(spiderable.getURL());
			if(null == netArticle) {
				throw new IOException("NetArticle--NULL");
			} else if("N".equals(netArticle.getIsExist())) {
				StrFilterChain chain = new StrFilterChain();
				chain.addStrFilter(new PaginationFilter(netArticle))
					.addStrFilter(new NetImageFilter(netArticle))
					.addStrFilter(new ScriptFilter())
					.addStrFilter(new PubDateFilter(netArticle))
					.addStrFilter(new SpecialStrFilter())
					.addStrFilter(new ATagFilter())
					.addStrFilter(new TitleFilter(netArticle))
					.addStrFilter(new IntroFilter(netArticle));
				String result = chain.doFilter(str);
				FileOutputStream fos = new FileOutputStream(absolutePath);//建立文件
				fos.write(result.getBytes());
				fos.close();
				//在这可能要做一些持久化的配置
				netArticle.setHtmlUrl(relativePath);
				netArticle.setIsExist("Y");
				netArticleDao.updateNetArticle(netArticle);
			}
		}
		
		private void downloadFile() throws IOException {
			String str = KKContentSpider.getContentString(spiderable, "gbk");
			articlePersistent(str);
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
			String fileName = dhFormat.format(date) + "_" + MD5Util.MD5Encode(destUrl).toUpperCase();
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