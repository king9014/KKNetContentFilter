package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetInfo;

public class IntroFilter implements StrFilter {

	private NetInfo netInfo = null;
	
	public IntroFilter(NetInfo netInfo) {
		this.netInfo = netInfo;
	}
	
	@Override
	public String doFilter(String str) {
		String result = str;
		if(null != netInfo) {
			Pattern p = Pattern.compile("<div class=\"new_ddlei\">([\\w[\\W]]+?)</div>");
			Matcher m = p.matcher(str);
			if(m.find()) {
				if(null == netInfo.getInfoIntro() || "".equals(netInfo.getInfoIntro().trim())) {
					netInfo.setInfoIntro(m.group(1));
				}
			}
		}
		return result;
	}

}
