package cn.dreamfield.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import cn.dreamfield.model.NetInfo;
import cn.dreamfield.utils.HibernateUtil;

@Component
public class NetInfoDao {
	
	private SessionFactory sessionFactory;
	
	public NetInfoDao() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public NetInfo getNetInfoEntity(String originUrl) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoOriginUrl='" + originUrl +"'");
		@SuppressWarnings("unchecked")
		List<NetInfo> list = query.list();
		session.close();
		if(list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public void saveNetInfo(NetInfo netInfo) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(netInfo);
		transaction.commit();
		session.close();
	}
	
	public void updateNetInfo(NetInfo netInfo) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.update(netInfo);
		transaction.commit();
		session.close();
	}
	
}
