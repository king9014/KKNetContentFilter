package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

/**
 * <img />标签下载远程图片并替换成本地路径
 * @author KING
 *
 */
public class NetImageFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		Pattern pattern = Pattern.compile("<img.+?src=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			String imageUrl = matcher.group(1);
			HttpDownloadUtil httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
			String relativePath = httpDownloadUtils.DownloadImageFromURL(imageUrl);
			result = result.replaceAll(imageUrl, "../../image/" + relativePath);
		}
		return result;
	}

}
