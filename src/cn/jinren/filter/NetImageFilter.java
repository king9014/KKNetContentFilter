package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.dao.NetInfoImgDao;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.model.NetInfoImg;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

/**
 * <img />标签下载远程图片并替换成本地路径
 * @author KING
 *
 */
public class NetImageFilter implements StrFilter {
	
	private NetInfo netInfo;
	private Boolean isFirstImg = true;
	
	public NetImageFilter(NetInfo netInfo) {
		this.netInfo = netInfo;
	}
	
	@Override
	public String doFilter(String str) {
		String result = str;
		Pattern pattern = Pattern.compile("<img.+?src=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			String imageUrl = matcher.group(1);
			if(imageUrl.startsWith("/") | imageUrl.startsWith("../") | imageUrl.startsWith("../../")) {
				KK.LOG("IMG URL ERROR " + imageUrl);
			}
			if(KKConf.IS_IMAGE_DOWNLOAD.containsKey(netInfo.getInfoWebsite()) && KKConf.IS_IMAGE_DOWNLOAD.get(netInfo.getInfoWebsite())) {
				HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
				String relativePath = httpDownloadUtils.DownloadImageFromURL(imageUrl);
				if(isFirstImg && (null == netInfo.getInfoImgUrl() || "".equals(netInfo.getInfoImgUrl()))) {
					isFirstImg = false;
					netInfo.setInfoImgUrl("image/" + relativePath);
				}
				if(KKConf.CHANGE_IMAGE_URL.containsKey(netInfo.getInfoWebsite()) && KKConf.CHANGE_IMAGE_URL.get(netInfo.getInfoWebsite())) {
					result = result.replaceAll(imageUrl, "../../image/" + relativePath);
				}
				NetInfoImg netInfoImg = new NetInfoImg();
				netInfoImg.setImgInfoId(netInfo.getInfoId());
				netInfoImg.setImgUrl(relativePath);
				SpringUtil.ctx.getBean(NetInfoImgDao.class).saveNetInfoImg(netInfoImg);
			}
		}
		return result;
	}

}
