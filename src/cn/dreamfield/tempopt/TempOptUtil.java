package cn.dreamfield.tempopt;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetInfo;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.StrFilter;
import cn.jinren.filter.StrFilterChain;

public class TempOptUtil {
	public static void trimTitleAndIntro(ArrayList<NetInfo> netInfos) {
		for(NetInfo netInfo : netInfos) {
			String newName = netInfo.getInfoName().substring(1, netInfo.getInfoName().length() - 1);
			netInfo.setInfoName(newName);
			String newIntro = netInfo.getInfoIntro().trim();
			newIntro = newIntro.replace("...(", "");
			netInfo.setInfoIntro(newIntro);
			HttpDownloadUtil httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
			String relativePath = httpDownloadUtils.DownloadImageFromURL(netInfo.getInfoImgUrl());
			netInfo.setInfoImgUrl("http://dreamfield.cn/lib/image/"+relativePath);
		}
	}
	
	public static StrFilterChain addTempChainBeforeATag(StrFilterChain chain) {
		
		return chain;
	}
	
	public static StrFilterChain addTempChainAfterATag(StrFilterChain chain) {
		u148TempFilterChain(chain);
		chain.addStrFilter(new NetImageFilter());
		return chain;
	}
	
	private static StrFilterChain u148TempFilterChain(StrFilterChain chain) {
		chain.addStrFilter(new StrFilter() {
			@Override
			public String doFilter(String str) {
				Pattern pa = Pattern.compile("<div class=\"article_info\">([\\w[\\W]]+?)</div>");
				Matcher ma = pa.matcher(str);
				if(ma.find()) {
					String tempStr = ma.group();
					tempStr = tempStr.replaceAll("�Ƽ���[\\w[\\W]]+?��", "");
					tempStr = tempStr.replaceAll("��Դ��[\\w[\\W]]+?��", "");
					tempStr = tempStr.replace("�ղ�", "");
					str = str.replace(ma.group(), tempStr);
				}
				return str;
			}
		});
		return chain;
	}
}
