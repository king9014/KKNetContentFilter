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
		@SuppressWarnings("unchecked")
		List<NetInfoPage> list = query.list();
		session.close();
		if(list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
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

}
