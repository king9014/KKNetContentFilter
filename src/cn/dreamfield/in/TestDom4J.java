package cn.dreamfield.in;

import cn.dreamfield.utils.XmlConfigReaderUtil;

public class TestDom4J {

	public static void main(String[] args) throws Exception {
		XmlConfigReaderUtil xmlUtil = new XmlConfigReaderUtil();
		xmlUtil.readConfigXMLAndstartListSpider(false);
	}
	
}
