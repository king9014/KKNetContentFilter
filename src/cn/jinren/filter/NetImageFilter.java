package cn.jinren.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.utils.HttpDownloadThread;
import cn.jinren.test.KK;

/**
 * <img />��ǩ����Զ��ͼƬ���滻�ɱ���·��
 * @author KING
 *
 */
public class NetImageFilter implements StrFilter {

	@Override
	public String doFilter(String str) {
		String result = str;
		Pattern pattern = Pattern.compile("<img.+?src=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			String imageUrl = matcher.group(1);
			HttpDownloadThread hdt = new HttpDownloadThread(imageUrl);
			new Thread(hdt).start();
			result = result.replaceAll(imageUrl, "../../image/" + hdt.getRelativePath());
		}
		return result;
	}

}
