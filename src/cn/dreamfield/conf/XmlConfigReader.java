package cn.dreamfield.conf;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dreamfield.utils.UrlAndCategory;
import cn.jinren.test.KK;

public class XmlConfigReader {
	private File inputXml;

	public XmlConfigReader() {
    	this.inputXml = new File("./src/kkspider.cfg.xml");
    }
	
    public XmlConfigReader(File inputXml) {
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
    
    public void readConfigXMLAndstartListSpider() {
    	Element root = getRootElement();
		for (Iterator<?> ie = root.elementIterator(); ie.hasNext();) {
			Element element = (Element) ie.next();
			KK.INFO("========[READ FROM CONF]========[" + element.getName() + "]========");
			if ("configs".equals(element.getName())){
				readConfigs(element);
			} else if("websites".equals(element.getName())) {
				readWebsites(element);
			}
	    }
    }
    
    private void readConfigs(Element configs) {
    	for (Iterator<?> ie = configs.elementIterator(); ie.hasNext();) {
			Element property = (Element) ie.next();
			if("file_root".equals(property.attributeValue("name"))) {
				KKConf.FILE_ROOT = (String) property.getData();
				KK.INFO(property.getData());
			}
	    }
	}

	public void readWebsites(Element websites) {
    	for (Iterator<?> ie = websites.elementIterator(); ie.hasNext();) {
    		WebsiteConf websiteConf = new WebsiteConf();
			Element website = (Element) ie.next();
			//读取站点名称
			if(null != website.attributeValue("name")) {
				websiteConf.setWebsiteName(website.attributeValue("name"));
			}
			if(null != website.attributeValue("decode")) {
				websiteConf.setDecode(website.attributeValue("decode"));
			}
			for (Iterator<?> iee = website.elementIterator(); iee.hasNext();) {
				Element item = (Element) iee.next();
				if("property".equals(item.getName())) {
					if("is_image_download".equals(item.attributeValue("name"))) {
						KK.INFO(websiteConf.getWebsiteName() + " [IMAGE DOWN]: " + item.getData());
						if("false".equals(item.getData())) {
							KKConf.IS_IMAGE_DOWNLOAD.put(websiteConf.getWebsiteName(), false);
						} else {
							KKConf.IS_IMAGE_DOWNLOAD.put(websiteConf.getWebsiteName(), true);
						}
					} else if("change_image_url".equals(item.attributeValue("name"))) {
						KK.INFO(websiteConf.getWebsiteName() + " [CHANGE IMG URL]: " + item.getData());
						if("false".equals(item.getData())) {
							KKConf.CHANGE_IMAGE_URL.put(websiteConf.getWebsiteName(), false);
						} else {
							KKConf.CHANGE_IMAGE_URL.put(websiteConf.getWebsiteName(), true);
						}
					}
				} else if("listurls".equals(item.getName())) {
					for (Iterator<?> ieee = item.elementIterator(); ieee.hasNext();) {
						Element url = (Element) ieee.next();
						websiteConf.addUc(new UrlAndCategory(url.attributeValue("url"), url.attributeValue("category")));
				    }
				}
		    }
			//一个website读取完成
			KKConf.websiteConfs.add(websiteConf);
	    }
    }
}
