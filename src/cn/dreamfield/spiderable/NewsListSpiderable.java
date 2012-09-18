package cn.dreamfield.spiderable;

import cn.jinren.spider.ListSpiderable;

public class NewsListSpiderable implements ListSpiderable {

	@Override
	public String getURL() {
		return "http://www.ali213.net/News/listhtml/List_1_1.html";
	}

	@Override
	public String getStart() {
		return "<div class=\"liebyjp\">单机资讯</div>";
	}

	@Override
	public String getEnd() {
		return "<div class=\"liebyj_libiao3\">";
	}

	@Override
	public String getElementPatt() {
		return "<div class=\"liebyj_lei3\">(.+?)</div>";
	}

	@Override
	public int getItemCount() {
		return 2;
	}

	@Override
	public String getItemPatt(int ItemNo) {
		switch(ItemNo) {
		case 0:
			return "</span><a href=\"(.+?)\" target=\"_blank\">";
		case 1:
			return "\" target=\"_blank\">(.+?)</a>";
		}
		return "============";
	}

}
