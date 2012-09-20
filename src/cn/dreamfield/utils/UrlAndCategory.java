package cn.dreamfield.utils;

public class UrlAndCategory {
	private String url;
	private String category;
	public UrlAndCategory(String url, String category) {
		this.setUrl(url);
		this.setCategory(category);
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}