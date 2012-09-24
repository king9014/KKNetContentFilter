package cn.dreamfield.in;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.AliNewsContentSpiderable;
import cn.dreamfield.utils.ArticleListUtil;
import cn.dreamfield.utils.GenerateListFileUtil;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.dreamfield.utils.UrlAndCategory;
import cn.dreamfield.utils.XmlConfigReaderUtil;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;

public class SystemIN {

	public static void main(String[] args) throws IOException {
//		loadAllByXMLConfig();
//		reLoadNByXMLConfig();
		generateListByXMLConfig();
	}
	
	public static void reLoadN() {
		List<NetArticle> list = SpringUtil.ctx.getBean(NetArticleDao.class).getNetArticleN();
		for(NetArticle n : list) {
			KK.INFO("[HTML RELOAD]: " + n.getOriginUrl()); 
			SpringUtil.ctx.getBean(HttpDownloadUtil.class).DownloadHtmlFromURL(new AliNewsContentSpiderable(n.getOriginUrl()));
		}
	}
	
	public static void reLoadNByXMLConfig() {
		XmlConfigReaderUtil xmlUtil = new XmlConfigReaderUtil();
		xmlUtil.readConfigXMLAndstartListSpider(false);
		List<NetArticle> list = SpringUtil.ctx.getBean(NetArticleDao.class).getNetArticleN();
		for(NetArticle n : list) {
			KK.INFO("[HTML RELOAD]: [" + n.getWebsite() + "]: " + n.getOriginUrl()); 
			try {
				Spiderable sp = (Spiderable) Class.forName(xmlUtil.getWebsiteMap().get(n.getWebsite())).newInstance();
				sp.setURL(n.getOriginUrl());
				SpringUtil.ctx.getBean(HttpDownloadUtil.class).DownloadHtmlFromURL(sp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadAllByXMLConfig() {
		XmlConfigReaderUtil xmlUtil = new XmlConfigReaderUtil();
		xmlUtil.readConfigXMLAndstartListSpider(true);
	}
	
	public static void generateList() {
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("pcnews");
	}
	
	public static void generateListByXMLConfig() {
		XmlConfigReaderUtil xmlUtil = new XmlConfigReaderUtil();
		xmlUtil.readConfigXMLAndstartListSpider(false);
		ArrayList<UrlAndCategory> ucs = xmlUtil.getUcs();
		ArrayList<String> categorys = new ArrayList<String>();
		for(UrlAndCategory uc : ucs) {
			Boolean isRepeat = false;
			String category = uc.getCategory();
			for(String str : categorys) {
				if(str.equals(category)) {
					isRepeat = true;
				}
			}
			if(!isRepeat) {
				categorys.add(category);
			}
		}
		for(String c : categorys) {
			SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList(c);
		}
	}
	
	public static void LoadAll() {
		ArticleListUtil articleListUtil = SpringUtil.ctx.getBean(ArticleListUtil.class);
		articleListUtil.startListSpider();
	}
	
	public static void test() {
		NetArticle n = new NetArticle();
		n.setOriginUrl("");
		new NetImageFilter(n).doFilter("<img src=\"http://imgs.ali213.net/news/IndexTJ/2012/09/17/3blj.jpg\" /></a>");
	}

}
