package cn.dreamfield.model;

public class NetInfoPage {
	private Long infoPageId;
	private int pageTotal;
	private int pageCurrent;
	private String pageHtmlUrl;
	private String pageOriginUrl;
	private Long parentId;
	private String pageStatus;
	public Long getInfoPageId() {
		return infoPageId;
	}
	public void setInfoPageId(Long infoPageId) {
		this.infoPageId = infoPageId;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public int getPageCurrent() {
		return pageCurrent;
	}
	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}
	public String getPageHtmlUrl() {
		return pageHtmlUrl;
	}
	public void setPageHtmlUrl(String pageHtmlUrl) {
		this.pageHtmlUrl = pageHtmlUrl;
	}
	public String getPageOriginUrl() {
		return pageOriginUrl;
	}
	public void setPageOriginUrl(String pageOriginUrl) {
		this.pageOriginUrl = pageOriginUrl;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPageStatus() {
		return pageStatus;
	}
	public void setPageStatus(String pageStatus) {
		this.pageStatus = pageStatus;
	}
}
