package cn.jinren.spider;

public interface Spiderable {
	/**
	 * 返回被抓取的网页地址
	 * @return
	 */
	public abstract String getURL();
	public abstract void setURL(String url);
	/**
	 * 返回被抓取的网页内容的开始标识
	 * @return
	 */
	public abstract String getStart();
	/**
	 * 返回被抓取的网页内容的结束标识
	 * @return
	 */
	public abstract String getEnd();
	
}
