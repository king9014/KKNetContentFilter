package cn.jinren.filter;

public class SpecialStrFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		result = result.replace("������������", "����:");
		return result;
	}

}
