package cn.dreamfield.conf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import cn.jinren.spider.Element;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.spider.ListSpiderable;
import cn.jinren.spider.ListSpiderableImpl;

public class PatternReader {
	
	private static Properties properties;
	
	static {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("./src/kkspider.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return properties.getProperty(key, "[BLANK]");
	}
	
	public static ListSpiderable getListSpiderable(String website) {
		ListSpiderableImpl listSpiderableImpl = new ListSpiderableImpl();
		listSpiderableImpl.setURL(PatternReader.getValue(website + ".list.url"));
		listSpiderableImpl.setStart(PatternReader.getValue(website + ".list.start"));
		listSpiderableImpl.setEnd(PatternReader.getValue(website + ".list.end"));
		listSpiderableImpl.setElementPatt(PatternReader.getValue(website + ".list.elementPatt"));
		int elementCount = Integer.parseInt(PatternReader.getValue(website + ".list.elementCount"));
		listSpiderableImpl.setCount(elementCount);
		for(int i=1; i<=elementCount; i++) {
			listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.element" + i));
		}
		return listSpiderableImpl;
	}
	
	// ListSpider²âÊÔÈë¿Ú
	public static void main(String[] args) {
		ListSpiderable l = PatternReader.getListSpiderable("youxia");
		try {
			KKContentSpider.getElementsFromWeb(l, new ArrayList<Element>(), "gbk");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
