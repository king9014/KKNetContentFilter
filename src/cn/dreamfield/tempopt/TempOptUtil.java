package cn.dreamfield.tempopt;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetInfo;
import cn.dreamfield.utils.HttpDownloadUtilx;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.filter.ImageCutFilter;
import cn.jinren.filter.IntroFilter;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.PaginationFilter;
import cn.jinren.filter.PubDateFilter;
import cn.jinren.filter.ScriptFilter;
import cn.jinren.filter.SpecialStrFilter;
import cn.jinren.filter.StrFilter;
import cn.jinren.filter.StrFilterChain;

public class TempOptUtil {
	public static void tempOptProcessForNetInfos(ArrayList<NetInfo> netInfos) {
		for(NetInfo netInfo : netInfos) {
			if("u148".equals(netInfo.getInfoWebsite())) {
				trimTitleAndIntro(netInfo);
			}
			if("youxia".equals(netInfo.getInfoWebsite())) {
				netInfo.setInfoName(netInfo.getInfoName().replaceAll("【游侠攻略组】", ""));
			}
		}
	}
	
	public static StrFilterChain addTempChainBeforeATag(StrFilterChain chain, NetInfo netInfo) {
		chain.addStrFilter(new ScriptFilter());
		if("youxia".equals(netInfo.getInfoWebsite())) {
			chain.addStrFilter(new PaginationFilter(netInfo));
		}
		return chain;
	}
	
	public static StrFilterChain addTempChainAfterATag(StrFilterChain chain, NetInfo netInfo) {
		chain.addStrFilter(new NetImageFilter(netInfo));
		if("u148".equals(netInfo.getInfoWebsite())) {
			chain.addStrFilter(u148TempFilter());
		}
		if("youxia".equals(netInfo.getInfoWebsite())) {
			chain.addStrFilter(new SpecialStrFilter());
			chain.addStrFilter(new IntroFilter(netInfo));
			chain.addStrFilter(new PubDateFilter(netInfo));
		}
		return chain;
	}
	
	// u148
	private static void trimTitleAndIntro(NetInfo netInfo) {
		String newName = netInfo.getInfoName().substring(1, netInfo.getInfoName().length() - 1);
		netInfo.setInfoName(newName);
		String newIntro = netInfo.getInfoIntro().trim();
		newIntro = newIntro.replace("...(", "");
		netInfo.setInfoIntro(newIntro);
		HttpDownloadUtilx httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtilx.class);
		String relativePath = httpDownloadUtils.DownloadImageFromURL(netInfo.getInfoImgUrl());
		netInfo.setInfoImgUrl("http://dreamfield.cn/lib/image/"+relativePath);
	}
	
	private static StrFilter u148TempFilter() {
		return new StrFilter() {
			@Override
			public String doFilter(String str) {
				Pattern pa = Pattern.compile("<div class=\"article_info\">([\\w[\\W]]+?)</div>");
				Matcher ma = pa.matcher(str);
				if(ma.find()) {
					String tempStr = ma.group();
					tempStr = tempStr.replaceAll("推荐：[\\w[\\W]]+?┊", "");
					tempStr = tempStr.replaceAll("来源：[\\w[\\W]]+?┊", "");
					tempStr = tempStr.replace("收藏", "");
					str = str.replace(ma.group(), tempStr);
				}
				return str;
			}
		};
	}
}
