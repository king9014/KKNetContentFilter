package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetArticle;

public class TitleFilter implements StrFilter {
	
	private NetArticle netArticle = null;
	
	public TitleFilter() {
	}
	
	public TitleFilter(NetArticle netArticle) {
		this.netArticle = netArticle;
	}
	@Override
	public String doFilter(String str) {
		String result = str;
		if(null != netArticle) {
			if("BLANK".equals(netArticle.getName())) {
				Pattern p = Pattern.compile("<h1>(.+?)</h1>");
				Matcher m = p.matcher(str);
				if(m.find()) {
					netArticle.setName(m.group(1));
				}
			}
		}
		result = result.replaceAll("¡¾ÓÎÏÀ¹¥ÂÔ×é¡¿", "");
		return result;
	}

}
