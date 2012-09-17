package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jinren.test.KK;
/**
 * [�������1032 | ʱ��:2012-9-17 11:55:21 | �� �� С] ��Ҫ���� ��ҪͶ��
 * ����Ϊ
 * ʱ��:2012-9-17 11:55:21
 * @author KING
 *
 */
public class PubDateFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		String dateStrParent = "";
		String dateStr = "";
		Pattern p = Pattern.compile("<div class=\"new_sl\">.+?</div>");
		Matcher m = p.matcher(str);
		if(m.find()) {
			dateStrParent = m.group();
			Pattern pa = Pattern.compile("(ʱ��:.+?)&");
			Matcher ma = pa.matcher(dateStrParent);
			if(ma.find()) {
				dateStr = ma.group(1);
			}
		}
		KK.DEBUG("dateStrParent", dateStrParent);
		KK.DEBUG("dateStr", dateStr);
		result = result.replace(dateStrParent, dateStr);
		return result;
	}

}
