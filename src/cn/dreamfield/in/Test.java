package cn.dreamfield.in;

import java.io.IOException;
import java.util.List;

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.dreamfield.utils.ArticleListUtil;
import cn.dreamfield.utils.GenerateListFileUtil;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.test.KK;

public class Test {

	public static void main(String[] args) throws IOException {
		LoadAll();
		//generateList();
		//reLoadN();
	}
	
	public static void reLoadN() {
		List<NetArticle> list = SpringUtil.ctx.getBean(NetArticleDao.class).getNetArticleN();
		for(NetArticle n : list) {
			KK.INFO("[HTML RELOAD]: " + n.getOriginUrl()); 
			SpringUtil.ctx.getBean(HttpDownloadUtil.class).DownloadHtmlFromURL(new GameNewsContentSpiderable(n.getOriginUrl()));
		}
	}
	
	public static void generateList() {
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("pcnews");
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("tvnews");
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("phonenews");
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("gametest");
		SpringUtil.ctx.getBean(GenerateListFileUtil.class).generateList("gamefuture");	
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
