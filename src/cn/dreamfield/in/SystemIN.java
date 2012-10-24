package cn.dreamfield.in;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.conf.XmlConfigReader;
import cn.dreamfield.dao.NetInfoDao;
import cn.dreamfield.dao.NetInfoPageDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.tempopt.TempOptUtil;
import cn.dreamfield.utils.ArticleListUtilx;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.ImageCutUtil;
import cn.dreamfield.utils.SpringUtil;

@Component
public class SystemIN {
	
	private String[] websiteNames = {"youxia"};
	
	public static void main(String[] args) throws IOException {
		SystemIN in = SpringUtil.ctx.getBean(SystemIN.class);
		
		in.generateWebsiteList();
//		in.reLoadNetInfosN();
//		in.generateFirstICO("youxia");
	}
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	@Autowired
	private NetInfoDao netInfoDao;
	@Autowired
	private NetInfoPageDao netInfoPageDao;
	
	private void generateWebsiteList() {
		for(String wn : websiteNames) {
			generateWebsiteListByName(wn);
			ArrayList<NetInfo> netInfos = articleListUtilx.getNetInfos();
			TempOptUtil.tempOptProcessForNetInfos(netInfos);
		}
		articleListUtilx.runContentSpider();
	}
	
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
	
	@SuppressWarnings("static-access")
	public void generateFirstICO(String websiteName) {
		String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Date d2 = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d2);
		calendar.add(calendar.DATE,1);
		d2 = calendar.getTime();
		String date2 = new SimpleDateFormat("yyyy-MM-dd").format(d2);
		List<NetInfo> infos = netInfoDao.getNetInfosByDateAndWebName(date1, date2, websiteName);
		for(NetInfo ni : infos) {
			if(null != ni.getInfoImgUrl() && ni.getInfoImgUrl().startsWith("image")) {
				//…˙≥…Õº∆¨Àı¬‘Õº
				String sImgUrl = ImageCutUtil.ImageCut(KKConf.FILE_ROOT + ni.getInfoImgUrl(), KKConf.IMG_SMALL_WIDTH, KKConf.IMG_SMALL_HIGTH);
				ni.setInfoImgUrl("ico/" + sImgUrl);
				netInfoDao.updateNetInfo(ni);
			}
		}
	}

	public void reLoadNetInfosN() {
		List<NetInfo> netInfos = netInfoDao.getNetInfosN();
		articleListUtilx.getNetInfos().clear();
		articleListUtilx.setNetInfos(netInfos);
		articleListUtilx.runContentSpider();
		List<NetInfoPage> pages = netInfoPageDao.getNetInfoPagesN();
		for(NetInfoPage ipage : pages) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			NetInfo ni = netInfoDao.getNetInfoEntity(ipage.getParentId());
			String decode = "";
			for(WebsiteConf w : KKConf.websiteConfs) {
				if(w.getWebsiteName().equals(ni.getInfoWebsite())) {
					decode = w.getDecode();
				}
			}
			HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
			httpDownloadUtils.DownloadChildPageFromURL(ni, ipage, decode);
		}
	}

}
