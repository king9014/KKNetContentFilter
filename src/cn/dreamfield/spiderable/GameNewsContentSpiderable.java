package cn.dreamfield.spiderable;

import cn.jinren.spider.Spiderable;

public class GameNewsContentSpiderable implements Spiderable {
	String url = "http://www.ali213.net/news/html/2012-9/53652_1.html";
	String start = "<div class=\"new_lei\">";
	String end = "<script src=\"../../js/pic.js\"></script>";
	
	public GameNewsContentSpiderable(String url) {
		this.url = url;
	}
	
	public GameNewsContentSpiderable() {
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
