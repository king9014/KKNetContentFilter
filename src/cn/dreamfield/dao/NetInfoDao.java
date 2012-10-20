package cn.dreamfield.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.utils.HibernateUtil;

@Component
public class NetInfoDao {
	
	private SessionFactory sessionFactory;
	@Autowired
	private NetInfoPageDao netInfoPageDao;
	
	public NetInfoDao() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public NetInfo getNetInfoEntity(String originUrl) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoOriginUrl='" + originUrl +"'");
		NetInfo netInfo = (NetInfo) query.uniqueResult();
		session.close();
		return netInfo;
	}
	
	public NetInfo getNetInfoEntity(Long infoId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoId=" + infoId);
		NetInfo netInfo = (NetInfo) query.uniqueResult();
		session.close();
		return netInfo;
	}
	
	public List<NetInfo> getNetInfosN() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoStatus='N'");
		@SuppressWarnings("unchecked")
		List<NetInfo> netInfos = query.list();
		session.close();
		return netInfos;
	}
	
	public List<NetInfo> getNetInfosByDateAndWebName(String date1, String date2, String webName) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoWebsite ='" + webName + "' and a.infoDateOpt between '" + date1 + "' and '" + date2 + "'");
		@SuppressWarnings("unchecked")
		List<NetInfo> netInfos = query.list();
		session.close();
		return netInfos;
	}
	
	public List<NetInfo> getNetInfosByDate(String date1, String date2) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfo a where a.infoDateOpt between '" + date1 + "' and '" + date2 + "'");
		@SuppressWarnings("unchecked")
		List<NetInfo> netInfos = query.list();
		session.close();
		return netInfos;
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
	
	public void deleteNetInfos(List<NetInfo> netInfos) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for(NetInfo ni : netInfos) {
			List<NetInfoPage> pages = netInfoPageDao.getNetInfoPagesByParent(ni.getInfoId());
			netInfoPageDao.deleteNetInfoPages(pages);
			session.delete(ni);
		}
		transaction.commit();
		session.close();
	}
	
}
