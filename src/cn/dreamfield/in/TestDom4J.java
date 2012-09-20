package cn.dreamfield.in;

import cn.dreamfield.spiderable.SpiderableConst;
import cn.dreamfield.utils.XmlConfigReaderUtil;
import cn.jinren.test.KK;

public class TestDom4J {

	public static void main(String[] args) {
		XmlConfigReaderUtil xmlUtil = new XmlConfigReaderUtil();
		xmlUtil.readParameterFromConfigXML();
		SpiderableConst.CONTENT_SPIDERABLE_NAME = "test";
		KK.DEBUG(SpiderableConst.CONTENT_SPIDERABLE_NAME);
	}
	
}
