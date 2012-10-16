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
import cn.jinren.spider.Spiderable;
import cn.jinren.spider.SpiderableImpl;

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
		return properties.getProperty(key, "#BLANK#");
	}
	
	public static ListSpiderable getListSpiderable(String website) {
		ListSpiderableImpl listSpiderableImpl = new ListSpiderableImpl();
		listSpiderableImpl.setURL(PatternReader.getValue(website + ".list.url"));
		listSpiderableImpl.setStart(PatternReader.getValue(website + ".list.start"));
		listSpiderableImpl.setEnd(PatternReader.getValue(website + ".list.end"));
		listSpiderableImpl.setElementPatt(PatternReader.getValue(website + ".list.elementPatt"));
		listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.elementName"));
		listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.elementUrl"));
		listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.elementImgUrl"));
		listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.elementDate"));
		listSpiderableImpl.addElements(PatternReader.getValue(website + ".list.elementIntro"));
		return listSpiderableImpl;
	}
	
	public static Spiderable getContentSpiderable(String website) {
		SpiderableImpl spiderableImpl = new SpiderableImpl();
		spiderableImpl.setURL(PatternReader.getValue(website + ".content.url"));
		spiderableImpl.setStart(PatternReader.getValue(website + ".content.start"));
		spiderableImpl.setEnd(PatternReader.getValue(website + ".content.end"));
		return spiderableImpl;
	}
	
	// ListSpider≤‚ ‘»Îø⁄
	public static void main(String[] args) {
		Spiderable s = PatternReader.getContentSpiderable("youxia");
		System.out.println(s.getURL());
		System.out.println(s.getStart());
		System.out.println(s.getEnd());
	}
}
