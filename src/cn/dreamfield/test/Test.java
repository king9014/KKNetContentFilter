package cn.dreamfield.test;

import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.spider.GameNewsContentSpiderable;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.test.KK;

public class Test {

	public static void main(String[] args) {
		String str = "<img title=\"雷抒雁：生死之间，你得忍住泪水\" alt=\"来自有意思吧（www.u148.net）\" src=\"http://file3.u148.net/2012/9/images/1347081170354.jpg\" />";
		StrFilterChain chain = new StrFilterChain();
		chain.addStrFilter(new NetImageFilter());
		KK.DEBUG(chain.doFilter(str));
		KKContentSpider.getContentString(new GameNewsContentSpiderable(), "gbk");
	}

}
