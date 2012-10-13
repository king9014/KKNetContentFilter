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

@Component
public class TestConf {
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	
	public void generateWebsiteListByName(String websiteName) {
		XmlConfigReader xReader = new XmlConfigReader();
		xReader.readConfigXMLAndstartListSpider();
		for(WebsiteConf w : KKConf.websiteConfs) {
			if(w.getWebsiteName().equals(websiteName)) {
				articleListUtilx.setWebsiteConf(w);
				articleListUtilx.startListSpider();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringUtil.ctx.getBean(TestConf.class).generateWebsiteListByName("u148");
		ArrayList<NetInfo> netInfos = SpringUtil.ctx.getBean(TestConf.class).articleListUtilx.getNetInfos();
		TempOptUtil.trimTitleAndIntro(netInfos);
	}

}
