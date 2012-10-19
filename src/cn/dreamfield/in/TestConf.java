package cn.dreamfield.in;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.conf.XmlConfigReader;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.tempopt.TempOptUtil;
import cn.dreamfield.utils.ArticleListUtilx;
import cn.dreamfield.utils.HibernateUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

@Component
public class TestConf {
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	
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
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringUtil.ctx.getBean(TestConf.class).generateWebsiteListByName("youxia");
		ArrayList<NetInfo> netInfos = SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.getNetInfos();
		TempOptUtil.tempOptProcessForNetInfos(netInfos);
//		for(NetInfo n : SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.getNetInfos()) {
//			KK.INFO(n.getInfoName());
//			KK.INFO(n.getInfoIntro());
//			KK.DEBUG("====================================");
//		}
		SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.runContentSpider();
	}

}
