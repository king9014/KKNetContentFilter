package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetArticle;

public class IntroFilter implements StrFilter {

	private NetArticle netArticle = null;
	
	public IntroFilter(NetArticle netArticle) {
		this.netArticle = netArticle;
	}
	
	@Override
	public String doFilter(String str) {
		String result = str;
		if(null != netArticle) {
			Pattern p = Pattern.compile("<div class=\"new_ddlei\">([\\w[\\W]]+?)</div>");
			Matcher m = p.matcher(str);
			if(m.find()) {
				if(1l == netArticle.getPageCorrent()) {
					netArticle.setIntro(m.group(1));
				}
			}
		}
		return result;
	}

}
