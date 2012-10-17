package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

/**
 * <img />标签下载远程图片并替换成本地路径
 * @author KING
 *
 */
public class NetImageFilterx implements StrFilter {
	
	private NetArticle netArticle;
	private Boolean isFirstImg = true;
	
	public NetImageFilterx(NetArticle netArticle) {
		this.netArticle = netArticle;
	}
	
	public NetImageFilterx() {
		isFirstImg = false;
	}

	@Override
	public String doFilter(String str) {
		String result = str;
		Pattern pattern = Pattern.compile("<img.+?src=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			String imageUrl = matcher.group(1);
			if(imageUrl.startsWith("/") | imageUrl.startsWith("../") | imageUrl.startsWith("../../")) {
				KK.LOG(imageUrl);
			}
			if(KKConf.IS_IMAGE_DOWNLOAD) {
				HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
				String relativePath = httpDownloadUtils.DownloadImageFromURL(imageUrl);
				if(isFirstImg) {
					isFirstImg = false;
					netArticle.setImgUrl(relativePath);
				}
				if(KKConf.CHANGE_IMAGE_URL) {
					result = result.replaceAll(imageUrl, ".." + relativePath);
				}
			}
		}
		return result;
	}

}
