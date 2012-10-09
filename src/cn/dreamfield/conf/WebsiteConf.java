package cn.dreamfield.conf;

import java.util.ArrayList;

import cn.dreamfield.utils.UrlAndCategory;

public class WebsiteConf {
	private String websiteName;
	private String decode;
	private ArrayList<UrlAndCategory> ucs = new ArrayList<UrlAndCategory>();

	public String getDecode() {
		return decode;
	}
	public void setDecode(String decode) {
		this.decode = decode;
	}

	public ArrayList<UrlAndCategory> getUcs() {
		return ucs;
	}
	public void addUc(UrlAndCategory uc) {
		this.ucs.add(uc);
	}
	public void setUcs(ArrayList<UrlAndCategory> ucs) {
		this.ucs = ucs;
	}
	
	public String getWebsiteName() {
		return websiteName;
	}
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
}
