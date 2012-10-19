package cn.jinren.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetInfo;
import cn.jinren.test.KK;

/**
 * [�������1032 | ʱ��:2012-9-17 11:55:21 | �� �� С] ��Ҫ���� ��ҪͶ��
 * ����Ϊ
 * ʱ��:2012-9-17 11:55:21
 * @author KING
 *
 */
public class PubDateFilter implements StrFilter {
	
	private NetInfo NetInfo;
	
	public PubDateFilter(NetInfo NetInfo) {
		this.NetInfo = NetInfo;
	}

	@Override
	public String doFilter(String str) {
		String result = str;
		String dateStrParent = "";
		String dateStr = "";
		Pattern p = Pattern.compile("<div class=\"new_sl\">.+?</div>");
		Matcher m = p.matcher(str);
		if(m.find()) {
			dateStrParent = m.group();
			Pattern pa = Pattern.compile("ʱ��:(.+?)&");
			Matcher ma = pa.matcher(dateStrParent);
			if(ma.find()) {
				dateStr = "ʱ��:" + ma.group(1);
				SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
				try {
					Date date = format.parse(ma.group(1));
					NetInfo.setInfoDate(date);
				} catch (ParseException e) {
					KK.ERROR(e);
				}
			}
		}
		//KK.DEBUG("dateStrParent", dateStrParent);
		//KK.DEBUG("dateStr", dateStr);
		result = result.replace(dateStrParent, dateStr);
		return result;
	}

}
