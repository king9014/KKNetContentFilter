package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.test.KK;

public class PaginationFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		String pagination = "";
		Pattern p = Pattern.compile("<div align=\"center\" class=\"page_fenye\">.+?</div>");
		Matcher m = p.matcher(str);
		if(m.find()) {
			pagination = m.group();
			Pattern pa = Pattern.compile("<a id=\"after_this_page\" href=\"(.+?)\">下一页</a>");
			Matcher ma = pa.matcher(pagination);
			if(ma.find()) {
				String nextUrl = ma.group(1);
				KK.DEBUG("[START NEXT]: " + nextUrl);
				HttpDownloadUtil httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
				httpDownloadUtils.DownloadHtmlFromURL(new GameNewsContentSpiderable(nextUrl));
			}
			pa = Pattern.compile("class=\"currpage\"[\\D]+?([\\d]+?)[\\D]+?");
			ma = pa.matcher(pagination);
			if(ma.find()) {
				KK.DEBUG("当前页", ma.group(1));
			}
			pa = Pattern.compile("共([\\d]+?)页");
			ma = pa.matcher(pagination);
			if(ma.find()) {
				KK.DEBUG("总页数", ma.group(1));
			}
		}
		result = result.replace(pagination, "");
		return result;
	}

}
