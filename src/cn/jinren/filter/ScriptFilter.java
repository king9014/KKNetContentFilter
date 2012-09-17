package cn.jinren.filter;

/**
 * 去掉<script></script>标签和其中内容
 * @author KING
 *
 */
public class ScriptFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		result = result.replaceAll("<script.+?>([\\w[\\W]]+?)</script>", "");
		return result;
	}

}
