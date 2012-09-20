package cn.dreamfield.utils;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.jinren.test.KK;

public class XmlConfigReaderUtil {
	private File inputXml;

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
    
    public void readParameterFromConfigXML() {
    	Element root = getRootElement();
		for (Iterator<?> ie = root.elementIterator(); ie.hasNext();) {
			Element element = (Element) ie.next();
			KK.INFO("========[READ FROM CONF]========[" + element.getName() + "]========");
			if("websites".equals(element.getName())) {
				readWebsites(element);
			}
	    }
    }
    
    public void readWebsites(Element website) {
    	
    }

}