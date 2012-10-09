package cn.dreamfield.in;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.conf.XmlConfigReader;
import cn.dreamfield.utils.ArticleListUtilx;
import cn.dreamfield.utils.SpringUtil;

@Component
public class TestConf {
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	
	public void t() {
		XmlConfigReader xReader = new XmlConfigReader();
		xReader.readConfigXMLAndstartListSpider();
		for(WebsiteConf w : KKConf.websiteConfs) {
			if(w.getWebsiteName().equals("itouxian")) {
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
		SpringUtil.ctx.getBean(TestConf.class).t();
	}

}
