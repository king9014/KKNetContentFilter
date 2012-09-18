package cn.dreamfield.in;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.dreamfield.utils.HibernateUtil;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

public class Test {

	public static void main(String[] args) throws IOException {
		HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
		h.DownloadHtmlFromURL(new GameNewsContentSpiderable("http://www.ali213.net/news/html/2012-9/53698.html"));
//		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//		Transaction transaction = session.beginTransaction();
//		NetArticle netArticle = new NetArticle();
//		netArticle.setName("TEST");
//		netArticle.setOriginUrl("http://baidu.com");
//		session.save(netArticle);
//		transaction.commit();
	}

}
