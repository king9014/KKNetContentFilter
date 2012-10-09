package cn.jinren.spider;

public class SpiderableImpl implements Spiderable {
	
	private String url;
	private String start;
	private String end;

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public void setURL(String url) {
		this.url = url;
	}

	@Override
	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}

	@Override
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
