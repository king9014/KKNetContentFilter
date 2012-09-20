package cn.dreamfield.spiderable;

import cn.jinren.spider.Spiderable;

public class NewsContentSpiderable implements Spiderable {
	String url = "http://www.ali213.net/news/html/2012-9/53652_1.html";
	String start = "<div class=\"new_lei\">";
	String end = "<div class=\"lieb_bg new_d\">";
	
	public NewsContentSpiderable(String url) {
		this.url = url;
	}
	
	public NewsContentSpiderable() {
	}
	
	@Override
	public String getURL() {
		return url;
	}

	@Override
	public String getStart() {
		return start;
	}

	@Override
	public String getEnd() {
		return end;
	}

}
