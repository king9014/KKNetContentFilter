package cn.dreamfield.in;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.conf.XmlConfigReader;
import cn.dreamfield.dao.NetInfoDao;
import cn.dreamfield.dao.NetInfoPageDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.utils.ArticleListUtilx;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

@Component
public class TestConf {
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	@Autowired
	private NetInfoDao netInfoDao;
	@Autowired
	private NetInfoPageDao netInfoPageDao;
	
	public void generateWebsiteListByName(String websiteName) {
		XmlConfigReader xReader = new XmlConfigReader();
		xReader.readConfigXMLAndstartListSpider();
		for(WebsiteConf wsc : KKConf.websiteConfs) {
			if(wsc.getWebsiteName().equals(websiteName)) {
				articleListUtilx.setWebsiteConf(wsc);
				articleListUtilx.startListSpider();
			}
		}
	}

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		TestConf tc = SpringUtil.ctx.getBean(TestConf.class);
//		NetInfoPage p = tc.netInfoPageDao.getNetInfoPageEntity(328L);
//		KK.INFO(p.getPageOriginUrl());
//		List<NetInfoPage> ps = tc.netInfoPageDao.getNetInfoPagesN();
//		for(NetInfoPage pi : ps) {
//			KK.INFO(pi.getPageCurrent() + " CURRENT");
//			KK.INFO(pi.getPageTotal() + " TOTAL");
//		}
//		NetInfo n = tc.netInfoDao.getNetInfoEntity("http://www.ali213.net/news/html/2012-10/56415.html");
//		KK.INFO(n.getInfoName());
		List<NetInfo> ns = tc.netInfoDao.getNetInfosByDate("2012-10-20", "2012-10-21");
		for(NetInfo ni : ns) {
			KK.INFO(ni.getInfoName());
		}
		tc.netInfoDao.deleteNetInfos(ns);
//		SpringUtil.ctx.getBean(TestConf.class).generateWebsiteListByName("youxia");
//		ArrayList<NetInfo> netInfos = SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.getNetInfos();
//		TempOptUtil.tempOptProcessForNetInfos(netInfos);
//		for(NetInfo n : SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.getNetInfos()) {
//			KK.INFO(n.getInfoName());
//			KK.INFO(n.getInfoIntro());
//			KK.DEBUG("====================================");
//		}
//		SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.runContentSpider();
	}

}
