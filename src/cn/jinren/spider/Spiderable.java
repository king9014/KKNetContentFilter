package cn.jinren.spider;

public interface Spiderable {
	/**
	 * ���ر�ץȡ����ҳ��ַ
	 * @return
	 */
	public abstract String getURL();
	public abstract void setURL(String url);
	/**
	 * ���ر�ץȡ����ҳ���ݵĿ�ʼ��ʶ
	 * @return
	 */
	public abstract String getStart();
	/**
	 * ���ر�ץȡ����ҳ���ݵĽ�����ʶ
	 * @return
	 */
	public abstract String getEnd();
	
}
