package cn.jinren.spider;

public class GameNewsContentSpiderable implements Spiderable {
	String url = "http://www.ali213.net/news/html/2012-8/50574.html";
	String start = "<div class=\"new_lei\">";
	String end = "<div class=\"lieb_bg new_d\">";
	
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
