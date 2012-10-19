package cn.jinren.spider;

public interface PaginationSpiderable {
	public abstract String getPaginationPatt();
	public abstract String getPageNext();
	public abstract String getPageTotal();
	public abstract String getPageCurrent();
}
