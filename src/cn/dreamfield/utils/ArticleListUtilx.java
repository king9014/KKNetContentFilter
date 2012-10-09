package cn.dreamfield.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.PatternReader;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.SpiderableConst;
import cn.jinren.filter.TitleFilter;
import cn.jinren.spider.Element;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.ListSpiderable;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;

@Component
public class ArticleListUtilx {
	
	@Autowired
	private NetArticleDao netArticleDao;
	
	private WebsiteConf websiteConf;

	public WebsiteConf getWebsiteConf() {
		return websiteConf;
	}

	public void setWebsiteConf(WebsiteConf websiteConf) {
		this.websiteConf = websiteConf;
	}

	public ArticleListUtilx() {
	}
	
	public void startListSpider() {
		for(UrlAndCategory uc : websiteConf.getUcs()) {
			runListSpider(uc, 0);
		}
	}
	
	public void runListSpider(UrlAndCategory uc, int reLoadNum) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			KK.ERROR(e1);
		}
		ListSpiderable listSpiderable = null;
		//更加xml配置文件获得对应的ListSpiderable
		try {
			listSpiderable = PatternReader.getListSpiderable(websiteConf.getWebsiteName());
			listSpiderable.setURL(uc.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Element> elements = new ArrayList<Element>();
		try {
			KKContentSpider.getElementsFromWeb(listSpiderable, elements, websiteConf.getDecode());
		} catch (IOException e) {
			KK.INFO("[LIST DOWN FAIL " + reLoadNum + "]: " + listSpiderable.getURL());
			if(reLoadNum > 1) {
				KK.LOG("[LIST DOWN FAIL]: " + listSpiderable.getURL());
			} else {
				runListSpider(uc, ++ reLoadNum);
			}
		}
		/*
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
				netArticle.setCategory(uc.getCategory());
				netArticle.setWebsite(SpiderableConst.WEBSITE_NAME);
				netArticle.setOriginUrl(destUrl); 			//item0 --- url
				netArticle.setIsExist("N"); 				//文章还没有本地化，暂时设为N
				String title = element.getItem1();			//item1 --- name	
				title = new TitleFilter().doFilter(title);	//过滤标题中的 特殊字符
				netArticle.setName(title);				   
				netArticle.setOptDate(new Date());
				netArticleDao.saveNetArticle(netArticle);
				HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
				Spiderable contentSpiderable = null;
				//更加xml配置文件获得对应的ContentSpiderable
				try {
					contentSpiderable = (Spiderable)Class.forName(SpiderableConst.CONTENT_SPIDERABLE_NAME).newInstance();
					contentSpiderable.setURL(destUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//游侠网的新闻内容 ---AliNewsContentSpiderable
				h.DownloadHtmlFromURL(contentSpiderable);
			} else if("N".equals(originArticle.getIsExist())) {
				Spiderable contentSpiderable = null;
				//更加xml配置文件获得对应的ContentSpiderable
				try {
					contentSpiderable = (Spiderable)Class.forName(SpiderableConst.CONTENT_SPIDERABLE_NAME).newInstance();
					contentSpiderable.setURL(destUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
				HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
				//游侠网的新闻内容 ---AliNewsContentSpiderable
				h.DownloadHtmlFromURL(contentSpiderable);
			}
		}*/
	}
	
	
}
