package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.conf.PatternReader;
import cn.dreamfield.conf.WebsiteConf;
import cn.dreamfield.dao.NetInfoPageDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoPage;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.spider.PaginationSpiderable;
import cn.jinren.test.KK;

public class PaginationFilter implements StrFilter {
	
	private NetInfo netInfo;
	
	public PaginationFilter(NetInfo netInfo) {
		this.netInfo = netInfo;
	}

	@Override
	public String doFilter(String str) {
		String result = str;
		PaginationSpiderable ps = PatternReader.getPaginationSpiderable(netInfo.getInfoWebsite());
		String pagination = "";
		Pattern p = Pattern.compile(ps.getPaginationPatt().replace("[KKSP]", "[\\w[\\W]]+?"));
		Matcher m = p.matcher(str);
		if(m.find()) {
			pagination = m.group();
			Pattern pa = Pattern.compile(ps.getPageNext().replace("[KKSP]", "([\\w[\\W]]+?)"));
			Matcher ma = pa.matcher(pagination);
			if(ma.find()) {
				String nextUrl = ma.group(1);
				KK.DEBUG("[START NEXT]: " + nextUrl);
				NetInfoPage cInfoPage = SpringUtil.ctx.getBean(NetInfoPageDao.class).getNetInfoPageEntity(nextUrl);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(null == cInfoPage) {
					cInfoPage = new NetInfoPage();
					cInfoPage.setPageOriginUrl(nextUrl);
					cInfoPage.setPageStatus("N");
					cInfoPage.setParentId(netInfo.getInfoId());
					
					pa = Pattern.compile(ps.getPageCurrent());
					ma = pa.matcher(pagination);
					if(ma.find()) {
						cInfoPage.setPageCurrent(Integer.parseInt(ma.group(1)) + 1);
						KK.DEBUG("当前页", Integer.parseInt(ma.group(1)) + 1);
					}
					pa = Pattern.compile(ps.getPageTotal());
					ma = pa.matcher(pagination);
					if(ma.find()) {
						cInfoPage.setPageTotal(Integer.parseInt(ma.group(1)));
						KK.DEBUG("总页数", ma.group(1));
					}
					SpringUtil.ctx.getBean(NetInfoPageDao.class).saveNetInfoPage(cInfoPage);
					
					String decode = "";
					for(WebsiteConf w : KKConf.websiteConfs) {
						if(w.getWebsiteName().equals(netInfo.getInfoWebsite())) {
							decode = w.getDecode();
						}
					}
					HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
					httpDownloadUtils.DownloadChildPageFromURL(netInfo, cInfoPage, decode);
				} else if("N".equals(cInfoPage.getPageStatus())) {
					String decode = "";
					for(WebsiteConf w : KKConf.websiteConfs) {
						if(w.getWebsiteName().equals(netInfo.getInfoWebsite())) {
							decode = w.getDecode();
						}
					}
					HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
					httpDownloadUtils.DownloadChildPageFromURL(netInfo, cInfoPage, decode);
				}
				
				
			}
		}
		result = result.replace(pagination, "");
		return result;
	}

}
