package cn.jinren.spider;

public class Element {
	private String item0;
	private String item1;
	private String item2;
	private String item3;
	private String item4;
	private String item5;
	private int count;
	
	public String getItem0() {
		return item0;
	}

	public void setItem0(String item0) {
		this.item0 = item0;
	}

	public String getItem1() {
		return item1;
	}

	public void setItem1(String item1) {
		this.item1 = item1;
	}

	public String getItem2() {
		return item2;
	}

	public void setItem2(String item2) {
		this.item2 = item2;
	}

	public String getItem3() {
		return item3;
	}

	public void setItem3(String item3) {
		this.item3 = item3;
	}

	public String getItem4() {
		return item4;
	}

	public void setItem4(String item4) {
		this.item4 = item4;
	}

	public String getItem5() {
		return item5;
	}

	public void setItem5(String item5) {
		this.item5 = item5;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 获得对应编号字段的抓取结果内容
	 * @param ItemNo
	 * @param ItemContent
	 */
	public void setItem(int ItemNo, String ItemContent) {
		switch(ItemNo) {
		case 0:
			setItem0(ItemContent);
			break;
		case 1:
			setItem1(ItemContent);
			break;
		case 2:
			setItem2(ItemContent);
			break;
		case 3:
			setItem3(ItemContent);
			break;
		case 4:
			setItem4(ItemContent);
			break;
		case 5:
			setItem5(ItemContent);
			break;
		}
	}
	
	public String getItem(int ItemNo) {
		switch(ItemNo) {
		case 0:
			return getItem0();
		case 1:
			return getItem1();
		case 2:
			return getItem2();
		case 3:
			return getItem3();
		case 4:
			return getItem4();
		case 5:
			return getItem5();
		}
		return null;
	}
	
}
