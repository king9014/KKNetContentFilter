package cn.jinren.spider;

public interface ListSpiderable extends Spiderable{
	/**
	 * ���ر�ץȡ������Ŀ��������ʽ
	 * @return
	 */
	public abstract String getElementPatt();
	/**
	 * ���ر�ץȡ������Ŀ���ֶ���Ŀ
	 * @return
	 */
	public abstract int getItemCount();
	/**
	 * ���ض�Ӧ����ֶε� ������ʽ
	 * @param ItemNo
	 * @return
	 */
	public abstract String getItemPatt(int ItemNo);
}
