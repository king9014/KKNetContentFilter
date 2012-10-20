package cn.dreamfield.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.utils.HibernateUtil;

@Component
public class NetInfoPageDao {
	
	private SessionFactory sessionFactory;
	
	public NetInfoPageDao() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public NetInfoPage getNetInfoPageEntity(String originUrl) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoPage a where a.pageOriginUrl='" + originUrl +"'");
		NetInfoPage netInfoPage = (NetInfoPage) query.uniqueResult();
		session.close();
		return netInfoPage;
	}
	
	public NetInfoPage getNetInfoPageEntity(Long pageId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoPage a where a.infoPageId=" + pageId);
		NetInfoPage netInfoPage = (NetInfoPage) query.uniqueResult();
		session.close();
		return netInfoPage;
	}
	
	public List<NetInfoPage> getNetInfoPagesN() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoPage a where a.pageStatus='N'");
		@SuppressWarnings("unchecked")
		List<NetInfoPage> netInfoPages = query.list();
		session.close();
		return netInfoPages;
	}
	
	public List<NetInfoPage> getNetInfoPagesByParent(Long parentId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoPage a where a.parentId=" + parentId);
		@SuppressWarnings("unchecked")
		List<NetInfoPage> netInfoPages = query.list();
		session.close();
		return netInfoPages;
	}
	
	public void saveNetInfoPage(NetInfoPage netInfoPage) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(netInfoPage);
		transaction.commit();
		session.close();
	}
	
	public void updateNetInfoPage(NetInfoPage netInfoPage) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(netInfoPage);
		transaction.commit();
		session.close();
	}
	
	public void deleteNetInfoPages(List<NetInfoPage> pages) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for(NetInfoPage p : pages) {
			session.delete(p);
		}
		transaction.commit();
		session.close();
	}

}
