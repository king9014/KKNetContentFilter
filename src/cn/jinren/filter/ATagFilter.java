package cn.jinren.filter;

/**
 * »•µÙ≥¨¡¥Ω”±Í«©
 * @author KING
 *
 */
public class ATagFilter implements StrFilter{

	@Override
	public String doFilter(String str) {
		String result = str;
		result = result.replaceAll("<a.+?>", "");
		result = result.replaceAll("</a>", "");
		return result;
	}

}
