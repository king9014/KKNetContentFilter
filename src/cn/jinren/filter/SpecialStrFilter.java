package cn.jinren.filter;

public class SpecialStrFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		result = result.replace("【游侠导读】", "导读:");
		result = result.replaceAll("alt=\"游侠网\"", "");
		return result;
	}

}
