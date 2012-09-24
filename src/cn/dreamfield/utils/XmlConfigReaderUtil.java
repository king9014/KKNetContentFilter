package cn.dreamfield.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dreamfield.spiderable.SpiderableConst;
import cn.jinren.test.KK;

public class XmlConfigReaderUtil {
	private File inputXml;
	private ArrayList<UrlAndCategory> ucs = new ArrayList<UrlAndCategory>();
	private HashMap<String, String> websiteMap = new HashMap<String,String>();

	public HashMap<String, String> getWebsiteMap() {
		return websiteMap;
	}

	public ArrayList<UrlAndCategory> getUcs() {
		return ucs;
	}

	public XmlConfigReaderUtil() {
    	this.inputXml = new File("./src/kkspider.cfg.xml");
    }
	
    public XmlConfigReaderUtil(File inputXml) {
    	this.inputXml = inputXml;
    }
 
    public Document getDocument() {
    	SAXReader saxReader = new SAXReader();
    	Document document = null;
    	try {
    		document = saxReader.read(inputXml);
    	} catch (DocumentException e) {
    		e.printStackTrace();
    	}
    	return document;
    }
    
    public Element getRootElement() {
    	return getDocument().getRootElement();
	}
    
    public void readConfigXMLAndstartListSpider(Boolean isStart) {
    	Element root = getRootElement();
		for (Iterator<?> ie = root.elementIterator(); ie.hasNext();) {
			Element element = (Element) ie.next();
			KK.INFO("========[READ FROM CONF]========[" + element.getName() + "]========");
			if ("configs".equals(element.getName())){
				readConfigs(element);
			} else if("websites".equals(element.getName())) {
				readWebsites(element, isStart);
			}
	    }
    }
    
    private void readConfigs(Element configs) {
    	for (Iterator<?> ie = configs.elementIterator(); ie.hasNext();) {
			Element property = (Element) ie.next();
			if("file_root".equals(property.attributeValue("name"))) {
				UtilConst.FILE_ROOT = (String) property.getData();
				KK.INFO(property.getData());
			} else if("is_image_download".equals(property.attributeValue("name"))) {
				if("false".equals(property.getData()))
					UtilConst.IS_IMAGE_DOWNLOAD = false;
				else 
					UtilConst.IS_IMAGE_DOWNLOAD = true;
				KK.INFO(property.getData());
			} else if("change_image_url".equals(property.attributeValue("name"))) {
				if("false".equals(property.getData()))
					UtilConst.CHANGE_IMAGE_URL = false;
				else 
					UtilConst.CHANGE_IMAGE_URL = true;
				KK.INFO(property.getData());
			}
	    }
	}

	public void readWebsites(Element websites, Boolean isStart) {
    	for (Iterator<?> ie = websites.elementIterator(); ie.hasNext();) {
			Element website = (Element) ie.next();
			//读取站点名称
			if(null != website.attributeValue("name")) {
				SpiderableConst.WEBSITE_NAME = website.attributeValue("name");
				KK.INFO(SpiderableConst.WEBSITE_NAME);
			}
			for (Iterator<?> iee = website.elementIterator(); iee.hasNext();) {
				Element item = (Element) iee.next();
				if("listspiderable".equals(item.getName())) {
					SpiderableConst.LIST_SPIDERABLE_NAME = item.attributeValue("name");
					KK.INFO(item.attributeValue("name"));
				} else if("contentspiderable".equals(item.getName())) {
					SpiderableConst.CONTENT_SPIDERABLE_NAME = item.attributeValue("name");
					websiteMap.put(SpiderableConst.WEBSITE_NAME, SpiderableConst.CONTENT_SPIDERABLE_NAME);
					KK.INFO(item.attributeValue("name"));
				} else if("listurls".equals(item.getName())) {
					for (Iterator<?> ieee = item.elementIterator(); ieee.hasNext();) {
						Element url = (Element) ieee.next();
						ucs.add(new UrlAndCategory(url.attributeValue("url"), url.attributeValue("category")));
				    }
				}
				//一个website读取完成
				if(isStart) {
					ArticleListUtil articleListUtil = SpringUtil.ctx.getBean(ArticleListUtil.class);
					articleListUtil.setUrls(ucs);
					articleListUtil.startListSpider();
				}
		    }
	    }
    }

}