package cn.dreamfield.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.dreamfield.spiderable.NewsListSpiderable;
import cn.jinren.filter.TitleFilter;
import cn.jinren.spider.Element;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.ListSpiderable;
import cn.jinren.test.KK;

@Component
public class ArticleListUtil {
	
	@Autowired
	private NetArticleDao netArticleDao;
	private ArrayList<UrlAndCategory> urls = new ArrayList<UrlAndCategory>();  
	
	public ArticleListUtil() {
		urls.add(new UrlAndCategory("http://www.ali213.net/News/listhtml/List_1_1.html", "pcnews"));
		urls.add(new UrlAndCategory("http://www.ali213.net/News/listhtml/List_7_1.html", "tvnews"));
		urls.add(new UrlAndCategory("http://www.ali213.net/News/listhtml/List_8_1.html", "phonenews"));
		urls.add(new UrlAndCategory("http://www.ali213.net/News/listhtml/List_3_1.html", "gametest"));
		urls.add(new UrlAndCategory("http://www.ali213.net/News/listhtml/List_9_1.html", "gamefuture"));
	}
	
	public void startListSpider() {
		for(UrlAndCategory uc : urls) {
			runListSpider(uc, 0);
		}
	}
	
	public void runListSpider(UrlAndCategory uc, int reLoadNum) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			KK.ERROR(e1);
		}
		//游侠网的新闻列表---NewsListSpiderable
		ListSpiderable listSpiderable = new NewsListSpiderable(uc.url);
		ArrayList<Element> elements = new ArrayList<Element>();
		try {
			KKContentSpider.getElementsFromWeb(listSpiderable, elements, "gbk");
		} catch (IOException e) {
			KK.INFO("[LIST DOWN FAIL " + reLoadNum + "]: " + listSpiderable.getURL());
			if(reLoadNum > 1) {
				KK.LOG("[LIST DOWN FAIL]: " + listSpiderable.getURL());
			} else {
				runListSpider(uc, ++ reLoadNum);
			}
		}
		for(Element element : elements) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				KK.ERROR(e1);
			}
			String destUrl = element.getItem0();
			//防止同一篇文章的重复下载
			NetArticle originArticle = netArticleDao.getNetArticleEntity(destUrl);
			if(null == originArticle) {
				NetArticle netArticle = new NetArticle();
				netArticle.setCategory(uc.category);
				netArticle.setOriginUrl(destUrl); 			//item0 --- url
				netArticle.setIsExist("N"); 				//文章还没有本地化，暂时设为N
				String title = element.getItem1();			//item1 --- name	
				title = new TitleFilter().doFilter(title);	//过滤标题中的 特殊字符
				netArticle.setName(title);				   
				netArticle.setOptDate(new Date());
				netArticleDao.saveNetArticle(netArticle);
				HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
				//游侠网的新闻内容 ---GameNewsContentSpiderable
				h.DownloadHtmlFromURL(new GameNewsContentSpiderable(destUrl));
			} else if("N".equals(originArticle.getIsExist())) {
				HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
				//游侠网的新闻内容 ---GameNewsContentSpiderable
				h.DownloadHtmlFromURL(new GameNewsContentSpiderable(destUrl));
			}
		}
	}
	
	class UrlAndCategory {
		private String url;
		private String category;
		public UrlAndCategory(String url, String category) {
			this.url = url;
			this.category = category;
		}
	}
}
