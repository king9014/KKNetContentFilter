package cn.dreamfield.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import cn.dreamfield.model.NetArticle;
import cn.dreamfield.utils.HibernateUtil;

@Component
public class NetArticleDao {

	private SessionFactory sessionFactory;
	
	public NetArticleDao() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public NetArticle getNetArticleEntity(String originUrl) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetArticle a where a.originUrl='" + originUrl +"'");
		@SuppressWarnings("unchecked")
		List<NetArticle> list = query.list();
		session.close();
		if(list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public void saveNetArticle(NetArticle netArticle) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(netArticle);
		transaction.commit();
		session.close();
	}
	
	public void updateNetArticle(NetArticle netArticle) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(netArticle);
		transaction.commit();
		session.close();
	}
	
	public List<NetArticle> getNetArticleIn(String category) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetArticle a where a.pageCorrent=1 and a.category='" + category + "'");
		@SuppressWarnings("unchecked")
		List<NetArticle> list = query.list();
		session.close();
		return list;
	}
	
	public NetArticle getNetArticleChild(Long id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetArticle a where a.pid=" + id);
		@SuppressWarnings("unchecked")
		List<NetArticle> list = query.list();
		session.close();
		if(list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public List<NetArticle> getNetArticleN() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetArticle a where a.isExist='N'");
		@SuppressWarnings("unchecked")
		List<NetArticle> list = query.list();
		session.close();
		return list;
	}
	
}
