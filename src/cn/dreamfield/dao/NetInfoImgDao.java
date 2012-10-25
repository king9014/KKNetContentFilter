package cn.dreamfield.dao;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import cn.dreamfield.model.NetInfoImg;
import cn.dreamfield.utils.HibernateUtil;

@Component
public class NetInfoImgDao {
	
	private SessionFactory sessionFactory;
	
	public NetInfoImgDao() {
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public Long getImgNetInfoId(String imgUrl) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoImg a where a.imgUrl='" + imgUrl +"'");
		NetInfoImg netInfoImg = (NetInfoImg) query.uniqueResult();
		session.close();
		return netInfoImg.getImgInfoId();
	}
	
	public List<NetInfoImg> getNetInfoImgs(Long infoId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from NetInfoImg a where a.imgInfoId=" + infoId);
		@SuppressWarnings("unchecked")
		List<NetInfoImg> netInfoImgs = query.list();
		session.close();
		return netInfoImgs;
	}
	
	public void saveNetInfoImg(NetInfoImg netInfoImg) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.save(netInfoImg);
		transaction.commit();
		session.close();
	}
	
}
