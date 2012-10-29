package cn.dreamfield.in;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.conf.XmlConfigReader;
import cn.dreamfield.dao.NetInfoDao;
import cn.dreamfield.dao.NetInfoImgDao;
import cn.dreamfield.dao.NetInfoPageDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoImg;
import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.tempopt.TempOptUtil;
import cn.dreamfield.utils.ArticleListUtilx;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.HttpUploadUtil;
import cn.dreamfield.utils.ImageCutUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

@Component
public class SystemIN {
	
	private String[] websiteNames = {"u148"};
	
	public static void main(String[] args) throws IOException {
		SystemIN in = SpringUtil.ctx.getBean(SystemIN.class);
		
		in.generateWebsiteList();
//		in.reLoadNetInfosN();
//		in.generateFirstICO("youxia");
//		in.uploadNetInfosToday("u148");
	}
	
	@Autowired
	private ArticleListUtilx articleListUtilx;
	@Autowired
	private NetInfoDao netInfoDao;
	@Autowired
	private NetInfoPageDao netInfoPageDao;
	@Autowired
	private NetInfoImgDao netInfoImgDao;
	
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
	
	@SuppressWarnings("static-access")
	public void uploadNetInfosToday(String websiteName) {
		String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Date d2 = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(d2);
		calendar.add(calendar.DATE,1);
		d2 = calendar.getTime();
		String date2 = new SimpleDateFormat("yyyy-MM-dd").format(d2);
		List<NetInfo> infos = netInfoDao.getNetInfosByDateAndWebName(date1, date2, websiteName);
//		uploadIco();
		for(NetInfo info : infos) {
			if("N".equals(info.getInfoStatus()))	continue;
			try {
				String res = HttpUploadUtil.visitUrl(KKConf.SEARCH_INFO_EXIST_URL + info.getInfoOriginUrl());
				if(res.endsWith("exist"))	continue;
			} catch (IOException e) {
				e.printStackTrace();
			}
			Boolean canInsertInfo = true;
			List<NetInfoImg> netInfoImgs = netInfoImgDao.getNetInfoImgs(info.getInfoId());
			for(NetInfoImg img : netInfoImgs) {
				File file = new File(KKConf.FILE_ROOT + "image/" + img.getImgUrl());
				KK.DEBUG(KKConf.FILE_ROOT + "image/" + img.getImgUrl());
				String res = null;
				try {
					res = HttpUploadUtil.postUpload(file, KKConf.IMG_UPLOAD_URL);
				} catch (IOException e) {
					res = "";
				}
				if(!res.endsWith("SUCCESS")) {
					canInsertInfo = false;
					KK.INFO("not success upload image ...");
				}
			}
			File file = new File(KKConf.FILE_ROOT + "html/" + info.getInfoHtmlUrl());
			String res = null;
			try {
				res = HttpUploadUtil.postUpload(file, KKConf.HTML_UPLOAD_URL);
			} catch (IOException e) {
				res = "";
			}
			if(!res.endsWith("SUCCESS")) {
				canInsertInfo = false;
				KK.INFO("not success upload html ...");
			}
			if(canInsertInfo) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", info.getInfoName());
				map.put("website", info.getInfoWebsite());
				map.put("category", info.getInfoCategory());
				map.put("htmlUrl", info.getInfoHtmlUrl().replaceAll("[\\d]{6}/", "upload/"));
				map.put("originUrl", info.getInfoOriginUrl());
				map.put("imgUrl", info.getInfoImgUrl().replaceAll("[\\d]{6}/", "upload/"));
				map.put("date", new SimpleDateFormat("yyyy-MM-dd").format(info.getInfoDate()));
				map.put("intro", info.getInfoIntro());
				map.put("cid", "0");
				map.put("status", info.getInfoStatus());
				try {
					KK.INFO(HttpUploadUtil.postUrl(KKConf.INSERT_INFO_URL, map));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void uploadIco() {
		List<NetInfoImg> netInfoImgs = netInfoImgDao.getNetInfoImgs(0L);
		for(NetInfoImg img : netInfoImgs) {
			File file = new File(KKConf.FILE_ROOT + "image/" + img.getImgUrl());
			String res = null;
			try {
				res = HttpUploadUtil.postUpload(file, KKConf.IMG_UPLOAD_URL);
			} catch (IOException e) {
				res = "";
			}
			if(!res.endsWith("SUCCESS")) {
				KK.INFO("not success upload ico ...");
			}
		}
	}

}
