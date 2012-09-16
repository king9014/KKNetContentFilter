package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jinren.test.KK;

public class NetImageFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		Pattern pattern = Pattern.compile("<img.+?src=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			KK.INFO(matcher.group(1));
		}
		return result;
	}

}
