package cn.jinren.spider;

import java.util.ArrayList;

public class ListSpiderableImpl implements ListSpiderable {
	
	private String url;
	private String start;
	private String end;
	private String elementPatt;
	private int count;
	private ArrayList<String> elements = new ArrayList<String>();

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
	
	@Override
	public String getElementPatt() {
		return elementPatt;
	}
	
	public void setElementPatt(String elementPatt) {
		this.elementPatt = elementPatt;
	}

	@Override
	public int getItemCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String getItemPatt(int ItemNo) {
		return elements.get(ItemNo);
	}
	
	public void addElements(String element) {
		this.elements.add(element);
	}

}
