package cn.jinren.spider;

public interface ListSpiderable extends Spiderable{
	/**
	 * 返回被抓取内容条目的正则表达式
	 * @return
	 */
	public abstract String getElementPatt();
	/**
	 * 返回被抓取内容条目的字段数目
	 * @return
	 */
	public abstract int getItemCount();
	/**
	 * 返回对应编号字段的 正则表达式
	 * @param ItemNo
	 * @return
	 */
	public abstract String getItemPatt(int ItemNo);
}
