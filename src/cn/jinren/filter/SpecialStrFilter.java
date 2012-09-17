package cn.jinren.filter;

public class SpecialStrFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		result = result.replace("¡¾ÓÎÏÀµ¼¶Á¡¿", "µ¼¶Á:");
		return result;
	}

}
