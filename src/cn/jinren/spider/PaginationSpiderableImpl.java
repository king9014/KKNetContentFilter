package cn.jinren.spider;

public class PaginationSpiderableImpl implements PaginationSpiderable {
	private String patt;
	private String pageNext;
	private String pageTotal;
	private String pageCurrent;
	@Override
	public String getPaginationPatt() {
		return patt;
	}

	@Override
	public String getPageNext() {
		return pageNext;
	}

	@Override
	public String getPageTotal() {
		return pageTotal;
	}

	@Override
	public String getPageCurrent() {
		return pageCurrent;
	}
	public void setPatt(String patt) {
		this.patt = patt;
	}
	public void setPageNext(String pageNext) {
		this.pageNext = pageNext;
	}
	public void setPageTotal(String pageTotal) {
		this.pageTotal = pageTotal;
	}
	public void setPageCurrent(String pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

}
