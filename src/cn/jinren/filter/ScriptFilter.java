package cn.jinren.filter;

/**
 * ȥ��<script></script>��ǩ����������
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
