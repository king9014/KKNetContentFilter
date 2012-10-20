package cn.dreamfield.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.PatternReader;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.dao.NetInfoDao;
import cn.dreamfield.model.NetInfo;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.ListSpiderable;
import cn.jinren.test.KK;

@Component
public class ArticleListUtilx {
	
	@Autowired
	private NetInfoDao netInfoDao;
	private ArrayList<NetInfo> netInfos = new ArrayList<NetInfo>();
	private WebsiteConf websiteConf;

	public WebsiteConf getWebsiteConf() {
		return websiteConf;
	}

	public void setWebsiteConf(WebsiteConf websiteConf) {
		this.websiteConf = websiteConf;
	}
	
	public ArrayList<NetInfo> getNetInfos() {
		return netInfos;
	}
	
	public ArticleListUtilx() {
	}
	
	public void startListSpider() {
		for(UrlAndCategory uc : websiteConf.getUcs()) {
			runListSpider(uc, 0);
		}
	}
	
	public void runListSpider(UrlAndCategory uc, int reLoadNum) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			KK.ERROR(e1);
		}
		ListSpiderable listSpiderable = null;
		//更加xml配置文件获得对应的ListSpiderable
		try {
			listSpiderable = PatternReader.getListSpiderable(websiteConf.getWebsiteName());
			listSpiderable.setURL(uc.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			KKContentSpider.getElementsFromWebx(listSpiderable, netInfos, websiteConf.getDecode());
		} catch (IOException e) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			KK.INFO("[LIST DOWN FAIL " + reLoadNum + "]: " + listSpiderable.getURL());
			if(reLoadNum > 1) {
				KK.LOG("[LIST DOWN FAIL]: " + listSpiderable.getURL());
			} else {
				runListSpider(uc, ++ reLoadNum);
			}
		}
		for(NetInfo netInfo : netInfos) {
			netInfo.setInfoCategory(uc.getCategory());
			netInfo.setInfoWebsite(websiteConf.getWebsiteName());
		}
	}
	
	public void runContentSpider() {
		for(NetInfo netInfo : netInfos) {
			NetInfo originInfo = netInfoDao.getNetInfoEntity(netInfo.getInfoOriginUrl());
			if(null == originInfo) {
				netInfo.setInfoStatus("N");
				netInfo.setInfoDateOpt(new Date());
				netInfoDao.saveNetInfo(netInfo);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				SpringUtil.ctx.getBean(HttpDownloadUtilx.class).DownloadHtmlFromURL(netInfo, websiteConf.getDecode());
			} else if("N".equals(originInfo.getInfoStatus())) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				SpringUtil.ctx.getBean(HttpDownloadUtilx.class).DownloadHtmlFromURL(netInfo, websiteConf.getDecode());
			}
		}
	}

	public void setNetInfos(List<NetInfo> netInfos) {
		this.netInfos = (ArrayList<NetInfo>) netInfos;
	}
	
}
